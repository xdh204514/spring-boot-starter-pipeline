package com.jdl.xdh.pipeline;

import com.jdl.xdh.pipeline.config.FilterProperties;
import com.jdl.xdh.pipeline.creator.FilterChainBuilder;
import com.jdl.xdh.pipeline.creator.FilterConfigReader;
import com.jdl.xdh.pipeline.creator.FilterInstantiator;
import com.jdl.xdh.pipeline.destroy.DestroySomeThing;
import com.jdl.xdh.pipeline.factory.FilterPipelineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xudehui1
 * @date: 2024-01-13 14:41
 */
@Configuration
// 必须，绑定我们的配置文件类
@EnableConfigurationProperties(FilterProperties.class)
public class PipelineGenerateAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FilterProperties filterProperties;


    public FilterConfigReader getFilterConfigReader() {
        return new FilterConfigReader(applicationContext, filterProperties);
    }

    @Bean
    public FilterChainBuilder getFilterChainBuilder() {
        return new FilterChainBuilder(getFilterInstantiator());
    }

    @Bean
    public FilterInstantiator getFilterInstantiator() {
        return new FilterInstantiator(applicationContext);
    }

    @Bean
    public FilterPipelineFactory filterPipelineService() {
        return new FilterPipelineFactory(getFilterChainBuilder(), getFilterConfigReader());
    }

    @Bean
    public DestroySomeThing destroySomeThing(){
        return new DestroySomeThing();
    }
}
