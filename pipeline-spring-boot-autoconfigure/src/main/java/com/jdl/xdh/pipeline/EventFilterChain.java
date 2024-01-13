package com.jdl.xdh.pipeline;


import com.jdl.xdh.pipeline.context.EventContext;
import com.jdl.xdh.pipeline.exception.ErrorHandler;

/**
 * 事件处理流
 *
 * @param <T>
 */
public interface EventFilterChain<T extends EventContext> {

    /**
     * 同步事件过滤器处理
     *
     * @param context
     */
    void handle(T context);

    /**
     * 异步事件过滤器处理
     *
     * @param context
     */
    void handleAsync(T context);

    /**
     * 开启下一个鉴权
     *
     * @param ctx
     */
    void fireNext(T ctx);

    /**
     * 全链路异常处理器
     *
     * @param errorHandler
     */
    void setErrorHandler(ErrorHandler errorHandler);
}
