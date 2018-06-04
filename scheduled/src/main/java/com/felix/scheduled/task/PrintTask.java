package com.felix.scheduled.task;
import com.felix.scheduled.ScheduledApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 参考文档
 * https://docs.spring.io/spring/docs/5.0.4.BUILD-SNAPSHOT/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html
 */
@Component
//@Configurable
public class PrintTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledApplication.class);


    /**
     * 每小时的10分执行该方法
     * Corn表达式
     *
     * 多线程的优势需要异步执行才能发挥出优势，如果串行执行，没必要搞多线程
     */
    //@Async
    @Scheduled(cron = "* * * * * *")
    public void cron1() throws Exception
    {
        Thread.sleep(2000);
        logger.info("执行测试cron1时间："+ new Date(System.currentTimeMillis()));
    }
    //@Async
    @Scheduled(cron = "* * * * * *")
    public void cron2() throws Exception
    {
        Thread.sleep(2000);
        logger.info("执行测试cron2时间："+ new Date(System.currentTimeMillis()));
    }

    /**
     * 是上一个调用开始后再次调用的延时（不用等待上一次调用完成）
     * 固定间隔时间
     */
    //@Async
    //@Scheduled(fixedRate = 1000 * 1)
    public void fixedRate() throws Exception
    {
        Thread.sleep(2000);
        logger.info("执行测试fixedRate时间："+ new Date(System.currentTimeMillis()));
    }

    /**
     * 上一个调用完成后再次调用的延时调用
     * 固定等待时间
     */
    //@Scheduled(fixedDelay = 1000 * 1)
    public void fixedDelay() throws Exception
    {
        Thread.sleep(2000);
        logger.info("执行测试fixedDelay时间："+ new Date(System.currentTimeMillis()));
    }

    /**
     * 第一次被调用前的延时，单位毫秒
     */
    //@Scheduled(initialDelay = 1000 * 10,fixedDelay = 1000 * 2)
    public void initialDelay() throws Exception
    {
        logger.info("执行测试initialDelay时间："+ new Date(System.currentTimeMillis()));
    }

}
