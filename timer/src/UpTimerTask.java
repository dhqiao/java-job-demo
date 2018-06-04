import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

/**
 * @Description: 闹钟-定时喝水
 * @Author: felix
 * @Date: Created in 11:47 2018/5/7
 * @Modified By:
 */
public class UpTimerTask extends TimerTask {

    // 喝水时间列表
    private static List<Integer> drinkTimes;

    /**
     * 静态初始化
     */
    static {
        initDrinkTimes();
    }

    /**
     * 初始化喝水时间
     */
    private static void initDrinkTimes(){
        drinkTimes = new ArrayList<Integer>();
        drinkTimes.add(7); // 喝水时间，单位小时
        drinkTimes.add(9);
        drinkTimes.add(11);
        drinkTimes.add(15);
        drinkTimes.add(17);
        drinkTimes.add(21);
    }

    /**
     * 执行
     */
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("检查是否到了喝水时间");
        // int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int hour = calendar.get(Calendar.SECOND);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String time = format.format(Calendar.getInstance().getTime());

        if(drinkTimes.contains(hour)) {
            System.out.println("--------------喝水时间到了--------------");
            System.out.println("当前时间为：" + time);
        }
    }

}
