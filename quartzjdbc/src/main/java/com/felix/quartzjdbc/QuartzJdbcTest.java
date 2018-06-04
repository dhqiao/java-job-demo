package com.felix.quartzjdbc;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.List;

public class QuartzJdbcTest {

    public static void main(String[] args) throws SchedulerException, ParseException {
        // 再次执行是会报异常，异常信息如下：
        // org.quartz.ObjectAlreadyExistsException: Unable to store Job : 'jGroup2.job2_2',
        // because one already exists with this identification.
        //startSchedule();
        resumeJob();
    }
    /**
     * 开始一个simpleSchedule()调度
     */
    public static void startSchedule() {
        try {
            // 1.创建一个JobDetail实例，指定Quartz
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    // 任务执行类
                    .withIdentity("job2_2", "jGroup2")
                    .storeDurably(true)
                    // 任务名，任务组
                    .build();

            //SimpleScheduleBuilder builder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(5);

            // 触发器类型
            CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");
            // 2、创建Trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger2_2", "tGroup2").startNow()
                    //.withSchedule(builder.withMisfireHandlingInstructionDoNothing()) // 所有的misfire不管，执行下一个周期的任务
                    //.withSchedule(builder.withMisfireHandlingInstructionFireAndProceed()) // 会合并部分的misfire,正常执行下一个周期的任务
                    .withSchedule(builder.withMisfireHandlingInstructionIgnoreMisfires()) // 所有misfire的任务会马上执行
                    // @document: https://blog.csdn.net/u010648555/article/details/53672738
                    .build();

            // 3、创建Scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            // 4、调度执行
            scheduler.scheduleJob(jobDetail, trigger);
            try {
                Thread.sleep(60000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //关闭调度器
            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中找到已经存在的job，并重新开始调度
     */
    public static void resumeJob() {
        try {

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job2_2", "jGroup2");
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            System.out.println(triggers);
            //SELECT TRIGGER_NAME, TRIGGER_GROUP FROM {0}TRIGGERS WHERE SCHED_NAME = {1} AND JOB_NAME = ? AND JOB_GROUP = ?
            // 重新恢复在jGroup2组中，名为job2_2的 job的触发器运行
            if(triggers.size() > 0){
                for (Trigger tg : triggers) {
                    // 根据类型判断
                    if ((tg instanceof CronTrigger) || (tg instanceof SimpleTrigger)) {
                        // 恢复job运行
                        scheduler.resumeJob(jobKey);
                    }
                }
                scheduler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
