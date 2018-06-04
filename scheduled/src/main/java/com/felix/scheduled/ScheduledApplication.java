package com.felix.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EnableScheduling 注解开启对定时任务的支持
 */
@SpringBootApplication
@EnableScheduling
public class ScheduledApplication {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledApplication.class);

	public static void main(String[] args) {
		logger.info("spring boot scheduled start");
		SpringApplication.run(ScheduledApplication.class, args);
	}
}
