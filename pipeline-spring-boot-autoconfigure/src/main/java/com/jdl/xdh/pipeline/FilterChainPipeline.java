package com.jdl.xdh.pipeline;

import com.jdl.xdh.pipeline.exception.ErrorHandler;

/**
 * 构造事件处理器链式结构
 *
 * @param <T>
 */
public class FilterChainPipeline<T extends EventFilter> {
    private DefaultFilterChain last;
    private ErrorHandler errorHandler;

    public FilterChainPipeline() {
    }

    public DefaultFilterChain getFilterChain() {
        return this.last;
    }

    public FilterChainPipeline addFirst(T filter) {
        DefaultFilterChain newChain = new DefaultFilterChain(this.last, filter);
        this.last = newChain;
        return this;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        if (last != null) {
            last.setErrorHandler(errorHandler);
        }
    }
}
