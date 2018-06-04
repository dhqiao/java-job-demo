package com.example.quartzed;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description: quartz ram 执行内容逻辑实现
 * @Author: felix
 * @Date: Created in 11:47 2018/5/7
 * @Modified By:
 */
public class RAMJob implements Job{

    private static Logger _log = LoggerFactory.getLogger(RAMJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        _log.info("----------------------------------------------------------");
        _log.info("Say hello to Quartz" + new Date());
        _log.info("----------------------------------------------------------");
    }

}
