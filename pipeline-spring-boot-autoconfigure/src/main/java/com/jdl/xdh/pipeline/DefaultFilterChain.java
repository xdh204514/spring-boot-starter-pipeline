package com.jdl.xdh.pipeline;

import com.jdl.xdh.pipeline.context.EventContext;
import com.jdl.xdh.pipeline.exception.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 事件过滤器处理对外提供的方法
 *
 * @param <T>
 */
public class DefaultFilterChain<T extends EventContext> implements EventFilterChain<T> {

    private EventFilterChain<T> next;
    private EventFilter<T> filter;
    private ErrorHandler errorHandler;

    public DefaultFilterChain(EventFilterChain chain, EventFilter filter) {
        this.next = chain;
        this.filter = filter;
    }

    /**
     * 同步事件过滤器处理方法
     *
     * @param context
     */
    @Override
    public void handle(T context) {
        try {
            filter.doFilter(context, this);
        } catch (Exception e) {
            if (errorHandler != null) {
                try {
                    errorHandler.handleError(e, context);
                } catch (Exception ex) {
                    throw e;
                }
            } else {
                throw e; // 如果没有设置错误处理器，则将异常继续抛出
            }
        }
    }

    /**
     * 异步事件过滤器处理方法
     *
     * @param context
     */
    @Override
    public void handleAsync(T context) {
        try {
            List<EventFilter> list = new ArrayList<>();
            DefaultFilterChain temp = (DefaultFilterChain) this;
            while (temp.next != null) {
                list.add(temp.filter);
                temp = (DefaultFilterChain) temp.next;
            }
            list.add(temp.filter);


            filter.doFilterAsync(context, list);
        } catch (Exception e) {
            if (errorHandler != null) {
                try {
                    errorHandler.handleError(e, context);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                throw e; // 如果没有设置错误处理器，则将异常继续抛出
            }
        }
    }

    @Override
    public void fireNext(T ctx) {
        EventFilterChain nextChain = this.next;
        if (Objects.nonNull(nextChain)) {
            nextChain.handle(ctx);
        }
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
