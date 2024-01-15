package com.jdl.xdh.pipeline.creator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jdl.xdh.pipeline.EventFilter;
import com.jdl.xdh.pipeline.annotation.EventFilterAnnotation;
import com.jdl.xdh.pipeline.common.BaseEnum;
import com.jdl.xdh.pipeline.config.FilterProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 负责读取和解析过滤器配置（本地或远程
 *
 * @author coderxdh
 * @date 2023/12/10 22:16
 */
@Slf4j
@AllArgsConstructor
public class FilterConfigReader {

    private final ApplicationContext applicationContext;
    private final FilterProperties filterProperties;

    public List<FilterProperties.FilterDefinition> getFilterDefinitionsFromLocal(BaseEnum baseEnum) {
        List<FilterProperties.FilterDefinition> definitions = filterProperties.getBusinessFilters().get(baseEnum.getCode());
        if (definitions == null) {
            throw new IllegalStateException("No filter definitions found for business code: " + baseEnum.getCode());
        }
        return definitions;
    }

    public List<FilterProperties.FilterDefinition> getFilterDefinitionsFromRemote(String jsonStrConfig, BaseEnum baseEnum) {
        FilterProperties filterProperties = null;
        try {
            filterProperties = JSON.parseObject(jsonStrConfig, new TypeReference<FilterProperties>() {
            });
        } catch (Exception e) {
            log.error("Serialization exception occurred for remote config: {}", jsonStrConfig, e);
            throw new RuntimeException("Serialization exception occurred for remote config");
        }

        Map<String, List<FilterProperties.FilterDefinition>> businessFilters = filterProperties.getBusinessFilters();
        List<FilterProperties.FilterDefinition> definitions = businessFilters.get(baseEnum.getCode());
        if (definitions == null) {
            throw new IllegalStateException("No filter definitions found for business code: " + baseEnum.getCode());
        }
        return definitions;
    }

    public List<Class<?>> getFilterDefinitionsFromAnno(BaseEnum baseEnum) {
        List<Class<?>> filterClasses = Arrays.asList(applicationContext.getBeanNamesForType(EventFilter.class))
                .stream()
                .map(beanName -> applicationContext.getType(beanName))
                .collect(Collectors.toList());

        List<Class<?>> definitions = filterClasses.stream()
                .filter(filterClass -> {
                    EventFilterAnnotation annotation = filterClass.getAnnotation(EventFilterAnnotation.class);
                    return annotation != null && baseEnum.getCode().equals(annotation.bizCode());
                }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(definitions)) {
            throw new IllegalStateException("No filter definitions found for business code: " + baseEnum.getCode());
        }

        return definitions;
    }
}
