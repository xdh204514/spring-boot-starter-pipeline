package com.jdl.xdh.pipeline.filter;

import com.jdl.xdh.pipeline.AbstractEventFilter;
import com.jdl.xdh.pipeline.context.AbstractEventContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author: xudehui1
 * @date: 2024-02-25 13:57
 */
@Slf4j
public class DefaultEventFilter extends AbstractEventFilter<AbstractEventContext> {
    /**
     * 业务处理方法
     *
     * @param context
     */
    @Override
    protected void handle(AbstractEventContext context) {
        if (Objects.nonNull(context) && Objects.nonNull(context.getBizEnum())) {
            log.info("{}：使用默认处理器", context.getBizEnum().getName());
            return;
        }
        log.info("使用默认处理器");
    }

    /**
     * 是否执行该处理器
     *
     * @param context
     * @return
     */
    @Override
    protected boolean isSupport(AbstractEventContext context) {
        return true;
    }
}
