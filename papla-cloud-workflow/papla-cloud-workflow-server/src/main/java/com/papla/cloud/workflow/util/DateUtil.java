package com.papla.cloud.workflow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能：该类用于操作 日期和时间常用
 * 作者：林鹏
 * 时间：2014年4月18日
 * 版本：2.0
 * <p>
 * 修改记录
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class DateUtil {

    /**
     * 得到当前日期(java.sql.Date类型)
     * 注意：返回的时间只到秒
     *
     * @return
     */
    public static java.sql.Date getCurrentDate() {
        Calendar oneCalendar = Calendar.getInstance();
        int year = oneCalendar.get(Calendar.YEAR);
        int month = oneCalendar.get(Calendar.MONTH);
        int day = oneCalendar.get(Calendar.DATE);
        int hours = oneCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = oneCalendar.get(Calendar.MINUTE);
        int second = oneCalendar.get(Calendar.SECOND);
        oneCalendar.clear();
        oneCalendar.set(year, month, day, hours, minute, second);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 把日期格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDate2String(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 根据指定的格式将日期转换为字符串
     *
     * @param date
     * @param dateRegx
     * @return
     */
    public static String formatDate2String(Date date, String dateRegx) {
        SimpleDateFormat format = new SimpleDateFormat(dateRegx);
        return format.format(date);
    }

}
