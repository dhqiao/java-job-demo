package com.felix.quartza;

import static java.util.Calendar.*;
import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule;
import static org.quartz.DateBuilder.newDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import java.util.GregorianCalendar;

/**
 * @Description: quartz 体验 入口文件
 * @Author: felix
 * @Date: Created in 11:47 2018/5/7
 * @Modified By:
 */
public class QuartzStart {

    public static void main(String[] args) {
        try {

            // 1. 创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 2. 定义一个Trigger
            Trigger trigger = getCronTrigger();
            //Trigger trigger1 = getSimpleTrigger();

            // 3. 定义一个JobDetail
            JobDetail job = newJob(HelloQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
                    .withIdentity("job1", "group1") //定义name/group
                    .withDescription(" this is a testjob") //设置描述
                    .usingJobData("name", "felix") //加入属性到ageJobDataMap
                    .usingJobData("name2", "elle") //加入属性到ageJobDataMap
                    .build();

            job.getJobDataMap().put("name3", "quertz"); //加入属性name3到JobDataMap

            // 4. 根据trigger调度job
            scheduler.scheduleJob(job, trigger);

            // 5. 开始运行
            scheduler.start();

            // 运行一段时间后关闭
            Thread.sleep(100000);

            // 6. 关闭
            scheduler.shutdown(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用简单触发器
     *
     指定从某一个时间开始，以一定的时间间隔（单位是毫秒）执行的任务。

     它适合的任务类似于：9:00 开始，每隔1小时，执行一次（withIntervalInHours...）。

     它的属性有：
                 repeatInterval 重复间隔
                 repeatCount    重复次数。实际执行次数是 repeatCount+1。因为在startTime的时候一定会执行一次。　**

     * @return simpleTrigger
     */
    public static Trigger getSimpleTrigger() {
        return newTrigger().withIdentity("trigger1", "group1") //定义name/group
                .startNow()                       // 一旦加入scheduler，立即生效
                .withSchedule(
                        simpleSchedule()          // 使用SimpleTrigger
                        .withIntervalInSeconds(1) // 每隔一秒执行一次
                        .repeatForever())         // 一直执行
                .build();
        /**
         simpleSchedule()
         .withIntervalInMinutes(1) //每分钟执行一次
         .withRepeatCount(10) //次数为10次
         .build();
         */
    }

    /**
     * 简单触发器，设置排除时间 - 稍复杂一点的触发机制
     * Quartz体贴地为我们提供以下几种Calendar，注意，所有的Calendar既可以是排除，也可以是包含，取决于：

     1. HolidayCalendar  指定特定的日期，比如20140613。精度到天。
     2. DailyCalendar    指定每天的时间段（rangeStartingTime, rangeEndingTime)，格式是HH:MM[:SS[:mmm]]。也就是最大精度可以到毫秒。
     3. WeeklyCalendar   指定每星期的星期几，可选值比如为java.util.Calendar.SUNDAY。精度是天。
     4. MonthlyCalendar  指定每月的几号。可选值为1-31。精度是天
     5. AnnualCalendar   指定每年的哪一天。使用方式如上例。精度是天。
     6. CronCalendar     指定Cron表达式。精度取决于Cron表达式，也就是最大精度可以到秒。
     *
     * @param scheduler
     * @return simpleTrigger
     * @throws SchedulerException
     */
    public static Trigger getSimpleTriggerWithExcluded(Scheduler scheduler) throws SchedulerException {

        // 定义一个每年执行Calendar，精度为天，即不能定义到2.25号下午2:00
        AnnualCalendar cal = new AnnualCalendar();
        java.util.Calendar excludeDay = new GregorianCalendar();

        // 设置排除2.25这个日期
        excludeDay.setTime(newDate().inMonthOnDay(4, 5).build());
        cal.setDayExcluded(excludeDay, true);

        // scheduler加入这个Calendar
        scheduler.addCalendar("FebCal", cal, false, false);

        return newTrigger().withIdentity("trigger1", "group1")
                .startNow()                   // 一旦加入scheduler，立即生效
                .modifiedByCalendar("FebCal") // 使用Calendar
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();
    }

    /**
     *  CalendarIntervalTrigger支持的间隔单位有秒，分钟，小时，天，月，年，星期。

     相较于SimpleTrigger有两个优势：1、更方便，比如每隔1小时执行，你不用自己去计算1小时等于多少毫秒。
                                  2、支持不是固定长度的间隔，比如间隔为月和年。但劣势是精度只能到秒。

     它适合的任务类似于：9:00 开始执行，并且以后每周 9:00 执行一次

     它的属性有:
             interval 执行间隔
             intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）
     * @return trigger
     */
    public static Trigger getCalendarIntervalTrigger() {
        return newTrigger().withIdentity("trigger1", "group1") //定义name/group
                .startNow()
                .withSchedule(calendarIntervalSchedule()
                        .withIntervalInDays(1)  // 每天执行一次
                        )
                .build();
    }

    /**
     * 指定每天的某个时间段内，以一定的时间间隔执行任务。并且它可以支持指定星期。

     它适合的任务类似于：指定每天9:00 至 18:00 ，每隔70秒执行一次，并且只要周一至周五执行。

     它的属性有:
             startTimeOfDay 每天开始时间
             endTimeOfDay   每天结束时间
             daysOfWeek     需要执行的星期
             interval       执行间隔
             intervalUnit   执行间隔的单位（秒，分钟，小时，天，月，年，星期）
             repeatCount    重复次数
     *
     * @return trigger
     */
    public static Trigger getDailyTimeIntervalTrigger() {
        return newTrigger().withIdentity("trigger1", "group1") //定义name/group
              .startNow()
              .withSchedule(dailyTimeIntervalSchedule()
                .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(9, 0)) // 第天9：00开始
                .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(16, 0))  // 16：00 结束
                .onDaysOfTheWeek(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY)        // 周一至周五执行
                .withIntervalInHours(1)                                           // 每间隔1小时执行一次
                .withRepeatCount(100)                                             // 最多重复100次 @todo 是每天 还是 总数
              ).build();
    }

    /**
     *适合于更复杂的任务，它支持类型于Linux Cron的语法（并且更强大）。基本上它覆盖了以上三个Trigger的绝大部分能力（但不是全部）

     它适合的任务类似于：每天0:00,9:00,18:00各执行一次。

     它的属性只有: Cron表达式

     cron生成器：http://cron.qqe2.com/

     字段	                    允许值	                   允许的特殊字符
     秒（Seconds）	          0~59的整数	       	        , - * /    四个字符
     分（Minutes）	          0~59的整数	       	        , - * /    四个字符
     小时（Hours）	          0~23的整数	       	        , - * /    四个字符
     日期（DayofMonth）	      1~31的整数     	       	,- * ? / L W C     八个字符     --但是你需要考虑你月的天数
     月份（Month）	          1~12的整数或者JAN-DEC	    , - * /    四个字符
     星期（DayofWeek）	      1~7的整数或者SUN-SAT     	, - * ? / L C #     八个字符
     年(可选，留空)（Year）	  1970~2099	                , - * /    四个字符

     比详细的讲解资料：https://www.cnblogs.com/javahr/p/8318728.html

     * @return trigger
     */
    public static Trigger getCronTrigger() {
        return newTrigger().withIdentity("trigger1", "group1") //定义name/group
                .startNow()//一旦加入scheduler，立即生效
                .withSchedule(cronSchedule("*/1 * 8-17 * * ?") // 每天8:00-17:00，每隔2分钟执行一次
                ).build();
    }

}
