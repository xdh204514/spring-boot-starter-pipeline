package com.jdl.xdh.pipeline.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author coderxdh
 * @date 2023/12/10 20:33
 */
@Slf4j
public class ThreadPoolManager {
    // 创建一个根据需要创建新线程的线程池，该线程池根据工作负载自动调整线程数量。该线程池使用ForkJoinPool来实现任务的分配和执行
    private static final ExecutorService pipelineExecutorService = Executors.newWorkStealingPool();

    public static ExecutorService getPipelineExecutorService() {
        log.info("getPipelineExecutorService");
        return pipelineExecutorService;
    }
}
