package com.jdl.xdh.pipeline.annotation;

import java.lang.annotation.*;

/**
 * 事件过滤器注解
 *
 * @author: xudehui1
 * @date: 2023-12-03 22:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EventFilterAnnotation {
    String bizCode();

    int priority();
}
