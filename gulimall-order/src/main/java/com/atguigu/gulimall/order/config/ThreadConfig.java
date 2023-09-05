package com.atguigu.gulimall.order.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(ThreadProperties.class)
@Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadProperties threadProperties){
        return new ThreadPoolExecutor(threadProperties.getCoreSize(), threadProperties.getMaxSize(), threadProperties.getTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadProperties.getQueueSize()), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

}
