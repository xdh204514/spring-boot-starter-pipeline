package com.jdl.xdh.pipeline;


import com.alibaba.fastjson.JSON;
import com.jdl.xdh.pipeline.config.ThreadPoolManager;
import com.jdl.xdh.pipeline.context.EventContext;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 模板方法
 * 同步执行和异步执行的实现逻辑
 *
 * @param <T>
 */
@Slf4j
public abstract class AbstractEventFilter<T extends EventContext> implements EventFilter<T> {

    public int priority = 0;

    /**
     * 同步执行过滤器链
     *
     * @param context
     * @param chain
     */
    @Override
    public void doFilter(T context, EventFilterChain chain) {
        handle(context);
        if (context.continueChain()) {
            chain.fireNext(context);
        }
    }

    /**
     * 执行单个过滤器
     *
     * @param context
     */
    @Override
    public void doFilter(T context) {
        handle(context);
    }

    /**
     * 异步执行过滤器组
     *
     * @param context
     * @param filterList
     */
    @Override
    public void doFilterAsync(T context, List<EventFilter> filterList) {
        log.info("链：{}", JSON.toJSONString(filterList));
        Map<Integer, List<EventFilter>> groupedFilters = filterList.stream()
                .collect(Collectors.groupingBy(
                        EventFilter::getPriority,
                        () -> new TreeMap<Integer, List<EventFilter>>(Comparator.reverseOrder()), // 自定义比较器,
                        Collectors.toList()));

        ExecutorService pipelineExecutorService = ThreadPoolManager.getPipelineExecutorService();
        CompletableFuture<Void> previousFuture = CompletableFuture.completedFuture(null);

        for (List<EventFilter> filters : groupedFilters.values()) {
            // thenCompose() 方法可以将一个异步操作的结果作为输入传递给下一个异步操作，从而实现操作链的组合
            previousFuture = previousFuture.thenCompose(result -> executeFiltersInParallel(context, filters, pipelineExecutorService));
        }

        // 处理所有的异步操作完成后的逻辑
        previousFuture.whenComplete((result, throwable) -> {
            if (throwable != null) {
                // 异常处理逻辑
            }
        }).join(); // 等待所有操作完成
    }

    private CompletableFuture<Void> executeFiltersInParallel(T context, List<EventFilter> filters, ExecutorService executorService) {
        List<CompletableFuture<Void>> filterFutures = new ArrayList<>();

        log.info("分组优先级：{}", JSON.toJSONString(filters));
        for (EventFilter filter : filters) {
            CompletableFuture<Void> filterFuture = CompletableFuture.runAsync(() -> {
                try {
                    filter.doFilter(context);
                } catch (Exception e) {
                    throw new CompletionException(e); // 将异常转换为CompletionException
                }
            }, executorService);
            filterFutures.add(filterFuture);
        }

        // allOf 方法是 CompletableFuture 类提供的一个静态方法，用于等待多个 CompletableFuture 对象的完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(filterFutures.toArray(new CompletableFuture[0]));

        return allFutures;
    }


    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    /**
     * 业务处理方法
     *
     * @param context
     */
    protected abstract void handle(T context);
}
