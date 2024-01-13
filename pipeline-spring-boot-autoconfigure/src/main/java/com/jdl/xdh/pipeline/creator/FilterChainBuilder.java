package com.jdl.xdh.pipeline.creator;

import com.jdl.xdh.pipeline.EventFilter;
import com.jdl.xdh.pipeline.FilterChainPipeline;
import com.jdl.xdh.pipeline.config.FilterProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        List<EventFilter> eventFilterList = definitions.stream()
                .map(filterInstantiator::createFilter)
                .sorted(Comparator.comparingInt(EventFilter::getPriority))
                .collect(Collectors.toList());
        eventFilterList.forEach(filter -> pipeline.addFirst(filter));
        return pipeline;
    }
}
