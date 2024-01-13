package com.jdl.xdh.pipeline.cache;

import com.jdl.xdh.pipeline.FilterChainPipeline;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存事件过滤器链
 *
 * @author coderxdh
 * @date 2023/12/6 23:55
 */
@Slf4j
public class FilterChainCache {
    private static final Map<String, FilterChainPipeline> cache = new ConcurrentHashMap<>();

    public static boolean containsKey(String key) {
        boolean result = cache.containsKey(key);
        log.info("cache contain pipeline: {}", result);
        return result;
    }

    public static FilterChainPipeline get(String key) {
        log.info("Get the pipeline with key :{} from the cache", key);
        return cache.get(key);
    }

    public static void put(String key, FilterChainPipeline value) {
        log.info("Save the pipeline with key :{} to the cache", key);
        cache.put(key, value);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static void clear() {
        cache.clear();
    }
}
