package com.felix.quartza;

import java.util.Date;

import org.quartz.*;

/**
 * @Description: quartz 执行内容逻辑实现
 * @Author: felix
 * @Date: Created in 11:47 2018/5/7
 * @Modified By:
 */
// 设定的时间间隔为1秒,但job执行时间是5秒
// 设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行
// 否则会在1秒时再启用新的线程执行
//@DisallowConcurrentExecution
public class HelloQuartz implements Job {

    // JobExecutionContext 提供调度上下文各种信息，运行时数据保存在jobDataMap
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JobDetail detail = context.getJobDetail();
        String name = detail.getJobDataMap().getString("name");
        String name2 = detail.getJobDataMap().getString("name2");
        System.out.println("-----------------------------------------------------------------");
        System.out.println("say hello to " + name +" and " + name2 + " at " + new Date());
        System.out.println("-----------------------------------------------------------------");
    }
}