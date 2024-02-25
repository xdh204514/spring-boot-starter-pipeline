package com.jdl.xdh.pipeline.creator;

import com.jdl.xdh.pipeline.EventFilter;
import com.jdl.xdh.pipeline.FilterChainPipeline;
import com.jdl.xdh.pipeline.config.FilterProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 负责构建和组装过滤器链
 *
 * @author coderxdh
 * @date 2023/12/10 22:12
 */
@AllArgsConstructor
public class FilterChainBuilder {

    private final FilterInstantiator filterInstantiator;

    public FilterChainPipeline<EventFilter> buildPipelineFromDefinition(List<FilterProperties.FilterDefinition> definitions) {
        FilterChainPipeline<EventFilter> pipeline = new FilterChainPipeline<>();
        if (CollectionUtils.isEmpty(definitions)) {
            definitions = buildDefaultFilterDefinition();
        }
        definitions.stream()
                .sorted(Comparator.comparingInt(FilterProperties.FilterDefinition::getPriority))
                .forEachOrdered(definition -> {
                    EventFilter filter = filterInstantiator.createFilter(definition);
                    pipeline.addFirst(filter);
                });
        return pipeline;
    }

    public FilterChainPipeline<EventFilter> buildPipelineFromAnnoClass(List<Class<?>> definitions) {
        FilterChainPipeline<EventFilter> pipeline = new FilterChainPipeline<>();
        if (CollectionUtils.isEmpty(definitions)) {
            definitions = buildDefaultFilterDefinition2();
        }
        List<EventFilter> eventFilterList = definitions.stream()
                .map(filterInstantiator::createFilter)
                .sorted(Comparator.comparingInt(EventFilter::getPriority))
                .collect(Collectors.toList());
        eventFilterList.forEach(filter -> pipeline.addFirst(filter));
        return pipeline;
    }

    private List<FilterProperties.FilterDefinition> buildDefaultFilterDefinition() {
        List<FilterProperties.FilterDefinition> defaultFilterList = new ArrayList<>();

        FilterProperties.FilterDefinition definition = new FilterProperties.FilterDefinition();
        definition.setName("com.jdl.xdh.pipeline.filter.DefaultEventFilter");
        definition.setPriority(1);

        defaultFilterList.add(definition);
        return defaultFilterList;
    }

    private List<Class<?>> buildDefaultFilterDefinition2() {
        try {
            return Collections.singletonList(Class.forName("com.jdl.xdh.pipeline.filter.DefaultEventFilter"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
