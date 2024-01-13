package com.jdl.xdh.pipeline.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 事件过滤器本地配置
 *
 * @author: xudehui1
 * @date: 2023-12-02 15:49
 */
@Configuration
@ConfigurationProperties(prefix = "pipeline")
public class FilterProperties {

    private Map<String, List<FilterDefinition>> businessFilters;

    @Data
    public static class FilterDefinition {
        private String name;
        private int priority;
    }

    public Map<String, List<FilterDefinition>> getBusinessFilters() {
        return businessFilters;
    }

    public void setBusinessFilters(Map<String, List<FilterDefinition>> businessFilters) {
        this.businessFilters = businessFilters;
    }
}

