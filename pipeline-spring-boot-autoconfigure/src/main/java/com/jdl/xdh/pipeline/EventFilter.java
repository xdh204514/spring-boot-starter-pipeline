package com.jdl.xdh.pipeline;


import com.jdl.xdh.pipeline.context.EventContext;

import java.util.List;

/**
 * 事件过滤器
 *
 * @param <T>
 */
public interface EventFilter<T extends EventContext> {

    /**
     * 事件过滤器优先级
     */
    int getPriority();

    void setPriority(int priority);

    /**
     * 同步执行事件过滤器链
     *
     * @param context
     * @param chain
     */
    void doFilter(T context, EventFilterChain chain);

    /**
     * 执行单个事件过滤器
     *
     * @param context
     */
    void doFilter(T context);

    /**
     * 异步执行事件过滤器组
     *
     * @param context
     * @param filterList
     */
    void doFilterAsync(T context, List<EventFilter> filterList);
}
