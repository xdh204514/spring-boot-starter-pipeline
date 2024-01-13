package com.jdl.xdh.pipeline.creator;

import com.jdl.xdh.pipeline.EventFilter;
import com.jdl.xdh.pipeline.config.FilterProperties;
import com.jdl.xdh.pipeline.annotation.EventFilterAnnotation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author coderxdh
 * @date 2023/12/10 22:13
 */
@AllArgsConstructor
public class FilterInstantiator {

    private final ApplicationContext applicationContext;

    public EventFilter createFilter(FilterProperties.FilterDefinition definition) {
        try {
            EventFilter filter = (EventFilter) applicationContext.getBean(Class.forName(definition.getName()));
            filter.setPriority(definition.getPriority());
            return filter;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to create filter instance for: " + definition.getName(), e);
        }
    }

    public EventFilter createFilter(Class<?> filterClass) {
        EventFilterAnnotation annotation = filterClass.getAnnotation(EventFilterAnnotation.class);
        int priority = annotation.priority();
        EventFilter filter = (EventFilter) applicationContext.getBean(filterClass);
        filter.setPriority(priority);
        return filter;
    }
}
