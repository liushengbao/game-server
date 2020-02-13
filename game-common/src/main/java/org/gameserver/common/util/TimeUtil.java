package org.gameserver.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间工具类
 *
 * @Author: shengbao
 * @Date: 2020/3/9 14:33
 */
public class TimeUtil {

    /** 时间戳 **/
    public static int getTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /** 获取今天零点时间 **/
    public static int getMorningTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /** 获取指定时间偏移xx天 **/
    public static int getOneTimeDiffAnyDays(int time, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) time * 1000L);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 昨日零点时间
     *
     * @return
     */
    public static int getYesterdayMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * 格式化时间戳为 yyyy-MM-dd HH:mm:ss
     *
     * @param time
     *            秒数
     * @return
     */
    public static String getDateByUnixTime(int time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(time * 1000L);
        return date;
    }

    /**
     * 是否是同一天
     *
     * @param srcTime
     * @param tarTime
     * @return
     */
    public static boolean isSameDay(int srcTime, int tarTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis((long) srcTime * 1000L);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis((long) tarTime * 1000L);
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 是否是今天
     * @param time
     * @return
     */
    public static boolean isToday(int time) {
        return isSameDay(time, TimeUtil.getTimestamp());
    }


}
