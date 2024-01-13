package com.jdl.xdh.pipeline.exception;

import com.jdl.xdh.pipeline.context.EventContext;

/**
 * 异常处理器
 *
 * @author: xudehui1
 * @date: 2023-12-02 20:47
 */
public interface ErrorHandler {
    void handleError(Exception e, EventContext context) throws Exception;
}
