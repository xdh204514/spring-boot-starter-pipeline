package com.jdl.xdh.pipeline.destroy;

import com.jdl.xdh.pipeline.config.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: xudehui1
 * @date: 2024-01-13 16:04
 */
@Slf4j
public class DestroySomeThing {

    @PreDestroy
    public void shutdown() {
        log.info("pipelineExecutorService shutdown");
        ExecutorService pipelineExecutorService = ThreadPoolManager.getPipelineExecutorService();
        pipelineExecutorService.shutdown();
        try {
            if (! pipelineExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.info("Forcing shutdownNow of pipelineExecutorService as it didn't respond to shutdown");
                pipelineExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pipelineExecutorService.shutdownNow();
        }
    }
}
