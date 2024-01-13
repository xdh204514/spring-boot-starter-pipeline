package com.jdl.xdh.pipeline.context;


import com.jdl.xdh.pipeline.common.BaseEnum;
import lombok.Getter;
import lombok.Setter;


/**
 * 抽象实现
 */
public abstract class AbstractEventContext implements EventContext {

    private final BaseEnum bizEnum;

    @Getter
    @Setter
    private boolean continueChain = true;

    public AbstractEventContext(BaseEnum bizEnum) {
        this.bizEnum = bizEnum;
    }

    @Override
    public BaseEnum getBizEnum() {
        return bizEnum;
    }

    @Override
    public boolean continueChain() {
        return continueChain;
    }
}
