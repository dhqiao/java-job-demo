package com.felix.scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Configuration：表明该类是一个配置类
 * @EnableAsync：开启异步事件的支持
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    private int corePoolSize = 5; // 线程数
    private int maxPoolSize = 200;
    private int queueCapacity = 10;
    /**
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;

    }
    */

    //多线程测试测试
    @Bean
    public Executor taskExecutor()
    {
        return Executors.newScheduledThreadPool(30);
    }
}

