package com.atguigu.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gulimall.thread")
@Data
public class ThreadProperties {

    public Integer coreSize;
    public Integer maxSize;
    public Integer time;
    public Integer queueSize;
}
