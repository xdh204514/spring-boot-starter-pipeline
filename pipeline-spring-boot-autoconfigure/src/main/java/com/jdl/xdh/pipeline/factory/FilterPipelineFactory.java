package com.jdl.xdh.pipeline.factory;

import com.jdl.xdh.pipeline.EventFilter;
import com.jdl.xdh.pipeline.FilterChainPipeline;
import com.jdl.xdh.pipeline.config.FilterProperties;
import com.jdl.xdh.pipeline.cache.FilterChainCache;
import com.jdl.xdh.pipeline.common.BaseEnum;
import com.jdl.xdh.pipeline.creator.FilterChainBuilder;
import com.jdl.xdh.pipeline.creator.FilterConfigReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 事件过滤器链工厂，负责根据不同场景创建过滤器链
 *
 * @author: xudehui1
 * @date: 2023-12-02 15:51
 */

@Slf4j
@AllArgsConstructor
public class FilterPipelineFactory {

    private final FilterChainBuilder filterChainBuilder;
    private final FilterConfigReader filterConfigReader;

    /**
     * 基于读取配置文件+业务编码动态组装过滤器链
     *
     * @param baseEnum
     * @return
     */
    public FilterChainPipeline createPipelineFromLocalConfig(BaseEnum baseEnum) {
        String bizCode = baseEnum.getCode();
        if (FilterChainCache.containsKey(bizCode)) {
            return FilterChainCache.get(bizCode);
        }
        List<FilterProperties.FilterDefinition> definitions = filterConfigReader.getFilterDefinitionsFromLocal(baseEnum);
        if (definitions == null) {
            throw new IllegalStateException("No filter definitions found for business code: " + baseEnum.getCode());
        }
        FilterChainPipeline<EventFilter> pipeline = filterChainBuilder.buildPipelineFromDefinition(definitions);
        FilterChainCache.put(bizCode, pipeline);
        return pipeline;
    }

    /**
     * 基于传入的过滤器配置对象(JSON 格式)+业务编码动态组装过滤器链
     *
     * @param jsonStrConfig
     * @param baseEnum
     * @return
     */
    public FilterChainPipeline createPipelineFromRemoteConfig(String jsonStrConfig, BaseEnum baseEnum) {
        List<FilterProperties.FilterDefinition> definitions = filterConfigReader.getFilterDefinitionsFromRemote(jsonStrConfig, baseEnum);
        return filterChainBuilder.buildPipelineFromDefinition(definitions);
    }


    public FilterChainPipeline createFilterChainPipelineWithAnno(BaseEnum baseEnum) {
        String bizCode = baseEnum.getCode();
        if (FilterChainCache.containsKey(bizCode)) {
            return FilterChainCache.get(bizCode);
        }
        List<Class<?>> definitions = filterConfigReader.getFilterDefinitionsFromAnno(baseEnum);
        FilterChainPipeline<EventFilter> pipeline = filterChainBuilder.buildPipelineFromAnnoClass(definitions);
        FilterChainCache.put(bizCode, pipeline);
        return pipeline;
    }
}

