import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: 闹钟执行执行类
 * @Author: felix
 * @Date: Created in 11:51 2018/5/7
 * @Modified By:
 */
public class UpTimerTaskTest {
    public static void main(String[] arg) throws InterruptedException {
        TimerTask task = new UpTimerTask();
        Calendar  calendar= Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 5); // 5秒钟后执行

        Date firstTime = calendar.getTime();
        //间隔：1小时
        //long period = 1000 * 60 * 60;
        long period = 1000;  // 体验时间 1秒钟出发一次
        //测试时间每分钟一次
        //period = 1000 * 60;

        Timer timer = new Timer();

        /**
         * 详细解释清参考api文档 java.lang.Timer
         *
         1、在特定时间执行任务，只执行一次
         public void schedule(TimerTask task,Date time)

         2、在特定时间之后执行任务，只执行一次
         public void schedule(TimerTask task,long delay)

         3、指定第一次执行的时间，然后按照间隔时间，重复执行
         public void schedule(TimerTask task,Date firstTime,long period)

         4、在特定延迟之后第一次执行，然后按照间隔时间，重复执行
         public void schedule(TimerTask task,long delay,long period)

         参数：

         delay： 延迟执行的毫秒数，即在delay毫秒之后第一次执行
         period：重复执行的时间间隔

         5、第一次执行之后，特定频率执行，与3同
         public void scheduleAtFixedRate(TimerTask task,Date firstTime,long period)

         6、在delay毫秒之后第一次执行，后按照特定频率执行
         public void scheduleAtFixedRate(TimerTask task,long delay,long period)


         方法名称schedule()和scheduleAtFixedRate()两者的区别

         <1>schedule()方法更注重保持间隔时间的稳定：保障每隔period时间可调用一次
         <2>scheduleAtFixedRate()方法更注重保持执行频率的稳定：保障多次调用的频率趋近于period时间，如果任务执行时间大于period，会在任务执行之后马上执行下一次任务
         */
        System.out.println("firstTime" + firstTime);
        timer.scheduleAtFixedRate(task, firstTime, period);
        //timer.schedule(task, firstTime, period);
//        /Thread.sleep(200000);
        // 取消定时器
        timer.cancel();
    }
}

