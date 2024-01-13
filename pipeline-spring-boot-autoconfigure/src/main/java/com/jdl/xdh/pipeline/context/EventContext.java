package com.jdl.xdh.pipeline.context;


import com.jdl.xdh.pipeline.common.BaseEnum;


/**
 * 事件过滤器上下文
 */
public interface EventContext {

    /**
     * 获取业务编码
     *
     * @return
     */
    BaseEnum getBizEnum();

    /**
     * 是否继续链
     *
     * @return
     */
    boolean continueChain();
}
