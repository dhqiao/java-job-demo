package com.felix.quartzjdbc;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class MyJob implements Job{
    private static final Logger log = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {
        context.isRecovering();
        Long time = System.currentTimeMillis();


        log.info("....................Job 启动 .................." + time);

        log.info("current instance id: " + context.getFireInstanceId());
        log.info("Hello quzrtz  " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

        log.info("....................Job 结束 .....................");

        // 出错后要不要接着执行，还是中断
        try {
            // 一个异常例子
            float calculation = 4815/(time%4);
        } catch (Exception e) {
            JobExecutionException e2 = new JobExecutionException(e);
            // true 表示 Quartz 会自动取消所有与这个 job 有关的 trigger，从而避免再次运行 job
             e2.setUnscheduleAllTriggers(true);

            // true 表示立即重新执行作业
            e2.setRefireImmediately(true);
            throw e2;
        }

    }



}
