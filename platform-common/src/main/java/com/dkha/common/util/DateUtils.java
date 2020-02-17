package com.dkha.common.util;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Spring
 * @Since 2016/8/15 14:10
 * @Description 日期工具类
 */
public class DateUtils extends PropertyEditorSupport {

    public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 以毫秒表示的时间
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long SECOND_IN_MILLIS = 1000;

    // 指定模式的时间格式
    private static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 当前日历，这里用中国时间表示
     *
     * @return 以当地时区表示的系统当前日历
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 指定毫秒数表示的日历
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日历
     */
    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));
        return cal;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // getDate
    // 各种方式获取的Date
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * @param dt
     * @return (1 = 星期天, 2 = 星期一, … … 7 = 星期六
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定时间属于周几
     *
     * @param date 指定日期
     * @return
     */
    public static int getChinaWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }

    /**
     * 指定毫秒数表示的日期
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日期
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }

    /**
     * 时间戳转换为字符串
     *
     * @param time
     * @return
     */
    public static String timestamptoStr(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date_sdf);
    }

    /**
     * 字符串转换时间戳
     *
     * @param str
     * @return
     */
    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, date_sdf);
        return new Timestamp(date.getTime());
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @param sdf
     * @return
     */
    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     *
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(SimpleDateFormat date_sdf) {
        Date date = getDate();
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateformat(String date, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date _date = null;
        try {
            _date = sformat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sformat.format(_date);
    }

    /**
     * 日期转换为字符串
     *
     * @param date     日期
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, SimpleDateFormat date_sdf) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 指定毫秒数的时间戳
     *
     * @param millis 毫秒数
     * @return 指定毫秒数的时间戳
     */
    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    /**
     * 以字符形式表示的时间戳
     *
     * @param time 毫秒数
     * @return 以字符形式表示的时间戳
     */
    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }

    /**
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * 指定日期的时间戳
     *
     * @param date 指定日期
     * @return 指定日期的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 指定日历的时间戳
     *
     * @param cal 指定日历
     * @return 指定日历的时间戳
     */
    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp gettimestamp() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(dt);
        Timestamp buydate = Timestamp.valueOf(nowTime);
        return buydate;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // getMillis
    // 各种方式获取的Millis
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return new Date().getTime();
    }

    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 指定时间戳的毫秒数
     *
     * @param ts 指定时间戳
     * @return 指定时间戳的毫秒数
     */
    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatDate
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日
     *
     * @return 默认日期按“年-月-日“格式显示
     */
    public static String formatDate() {
        return date_sdf.format(getCalendar().getTime());
    }

    /**
     * 获取时间字符串
     */
    public static String getDataString(SimpleDateFormat formatstr) {
        return formatstr.format(getCalendar().getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Calendar cal) {
        return date_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Date date) {
        return date_sdf.format(date);
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日“格式显示
     */
    public static String formatDate(long millis) {
        return date_sdf.format(new Date(millis));
    }

    /**
     * 默认日期按指定格式显示
     *
     * @param pattern 指定的格式
     * @return 默认日期按指定格式显示
     */
    public static String formatDate(String pattern) {
        return getSDFormat(pattern).format(getCalendar().getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param cal     指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Calendar cal, String pattern) {
        return getSDFormat(pattern).format(cal.getTime());
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param date    指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日 时：分
     *
     * @return 默认日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime() {
        return time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(long millis) {
        return time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Calendar cal) {
        return time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Date date) {
        return time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // formatShortTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 默认方式表示的系统当前日期，具体格式：时：分
     *
     * @return 默认日期按“时：分“格式显示
     */
    public static String formatShortTime() {
        return short_time_sdf.format(getCalendar().getTime());
    }

    /**
     * 指定毫秒数表示日期的默认显示，具体格式：时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“时：分“格式显示
     */
    public static String formatShortTime(long millis) {
        return short_time_sdf.format(new Date(millis));
    }

    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Calendar cal) {
        return short_time_sdf.format(cal.getTime());
    }

    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Date date) {
        return short_time_sdf.format(date);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // parseDate
    // parseCalendar
    // parseTimestamp
    // 将字符串按照一定的格式转化为日期或时间
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     * @throws ParseException
     */
    public static Date parseDate(String src, String pattern)
            throws ParseException {
        return getSDFormat(pattern).parse(src);

    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     * @throws ParseException
     */
    public static Calendar parseCalendar(String src, String pattern)
            throws ParseException {

        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatAddDate(String src, String pattern, int amount)
            throws ParseException {
        Calendar cal;
        cal = parseCalendar(src, pattern);
        cal.add(Calendar.DATE, amount);
        return formatDate(cal);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     * @throws ParseException
     * @throws ParseException
     */
    public static Timestamp parseTimestamp(String src, String pattern)
            throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }

    // ////////////////////////////////////////////////////////////////////////////
    // dateDiff
    // 计算两个日期之间的差值
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag   计算标志，表示按照年/月/日/时/分/秒等计算
     * @param calSrc 减数
     * @param calDes 被减数
     * @return 两个日期之间的差值
     */
    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

        long millisDiff = getMillis(calSrc) - getMillis(calDes);

        if (flag == 'y') {
            return (calSrc.get(calSrc.YEAR) - calDes.get(calDes.YEAR));
        }
        /*月*/
        if (flag == 'M') {
            return (calSrc.get(calSrc.MONTH) - calDes.get(calDes.MONTH));
        }
        //这里的一天是指60*60*24这个毫秒数
        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }
        //这里的一天是指天文学上面的一天
        if (flag == 'D') {
            return (calSrc.get(calSrc.DAY_OF_YEAR) - calDes.get(calDes.DAY_OF_YEAR));
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }

        return 0;
    }

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag  计算标志，表示按照年/月/日/时/分/秒等计算
     * @param start 开始日期
     * @param end   结束日期
     * @return 两个日期之间的差值
     */
    public static int dateDiff(char flag, Date start, Date end) {
        Calendar s = date2Calendar(start);
        Calendar e = date2Calendar(end);
        return dateDiff(flag, e, s);
    }

    /**
     * 计算两个时间之间相差的天数
     *
     * @param start 开始日期
     * @param end   结束日期
     */
    public static int dayDiff(Date start, Date end) {
        return dateDiff('d', start, end);
    }

    /**
     * String类型 转换为Date,
     * 如果参数长度为10 转换格式”yyyy-MM-dd“
     * 如果参数长度为19 转换格式”yyyy-MM-dd HH:mm:ss“
     * * @param text
     * String类型的时间值
     */
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    setValue(this.date_sdf.parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(this.datetimeFormat.parse(text));
                } else {
                    throw new IllegalArgumentException(
                            "Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException(
                        "Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            setValue(null);
        }
    }

    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.YEAR);
    }

    public static int getHour() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.MINUTE);
    }

    public static int getYear(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取date当天的开始时间
     *
     * @return Date
     */
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        return start;
    }

    /**
     * 获取date当天的开始时间
     *
     * @return String
     */
    public static String getDayStartTimeByString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(getDayStartTime(date));
    }

    /**
     * 获取date当天的结束时间
     *
     * @return
     */
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date end = calendar.getTime();
        return end;
    }

    /**
     * 获取Date当天的结束时间
     *
     * @return String
     */
    public static String getDayEndTimeByString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(getDayEndTime(date));
    }

    /**
     * 向前推算时间
     *
     * @param date 基础时间，以此时间为基础开始推算
     * @param days 向前推算天数
     * @return 推算结果
     */
    public static Date getDateBeforeByDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - days);
        return calendar.getTime();
    }

    /**
     * 向后推算时间
     *
     * @param date 基础时间，以此时间为基础开始推算
     * @param days 向后推算天数
     * @return 推算结果
     */
    public static Date getDateAfterByDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return calendar.getTime();
    }

    /**
     * 向后推算事件
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date getDateAfterByMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minutes);
        return calendar.getTime();
    }

    /**
     * 向前推算事件
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date getDateBeforeMyMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minutes);
        return calendar.getTime();
    }

    /**
     * 向前推算时间
     *
     * @param date   基础时间，以此时间为基础开始推算
     * @param months 向前推算月份数
     * @return
     */
    public static Date getDateBeforeByMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -months);
        return calendar.getTime();
    }

    /**
     * 向后推算时间
     *
     * @param date   基础时间，以此时间为基础开始推算
     * @param months 向后推算月份数
     * @return
     */
    public static Date getDateAfterByMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 向前推算时间
     *
     * @param date    基础时间， 以此时间为基础时间开始推算
     * @param hours   向前推算的小时数
     * @param minutes 向前推算的分钟数
     * @return
     */
    public static Date getDateBeforeByHoursAndMinutes(Date date, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        calendar.set(Calendar.MINUTE, calendar.get(calendar.MINUTE) - minutes);
        return calendar.getTime();
    }

    /**
     * 向后推算时间
     *
     * @param date    基础时间，以此时间为基础时间开始推算
     * @param hours   向后推算的小时数
     * @param minutes 向后推算的分钟数
     * @return
     */
    public static Date getDateAfterByHoursAndMinutes(Date date, int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * date2Calendar
     *
     * @param date
     * @return
     */
    public static Calendar date2Calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * calendar2Date
     *
     * @param calendar
     * @return
     */
    public static Date calendar2Date(Calendar calendar) {
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(calendar.MONTH) + 1;
    }

    /**
     * 获取时间月份
     *
     * @param date
     * @return
     */
    public static int getMonthByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(calendar.MONTH) + 1;
    }

    /**
     * 获取当前是哪一天
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前date中的号数
     *
     * @param date
     * @return
     */
    public static int getCurrentDayByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(calendar.YEAR);
    }

    /**
     * 获取Date对应的Month的第一天
     *
     * @param date
     * @return
     */
    public static String getMonthFirstDayByDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return format.format(c.getTime());

    }

    /**
     * 获取Date对应的Month的最后一天
     *
     * @param date
     * @return
     */
    public static String getMonthLastDayByDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        return format.format(cale.getTime());
    }

    /**
     * 计算是否为平年闰年
     *
     * @param year
     * @return
     */
    public static Boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        } else {
            if (year % 4 == 0) {
                return true;
            }
        }
        return false;
    }

    public static int getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    /**
     * 将12:00这种格式的时间转换为javaDate类型时间，其中的年月日为当前时间的年月日
     *
     * @param dateStr
     * @return
     */
    public static Date hourAndMinuteStr2Date(String dateStr) {
        int year = getCurrentYear();
        int month = getCurrentMonth();
        int day = getCurrentDay();
        dateStr = year + "-" + month + "-" + day + " " + dateStr;
        Date date = str2Date(dateStr, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        return date;
    }

    /**
     * 根据传入的Date，将12:00这种格式的时间转换为javaDate类型时间，其中的年月日为date对应的年月日
     *
     * @param dateStr
     * @param date
     * @return
     */
    public static Date hourAndMinuteStr2Date(String dateStr, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateStr = year + "-" + month + "-" + day + " " + dateStr;
        Date resultDate = str2Date(dateStr, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        return resultDate;
    }

    /**
     * 获取时间中的小时和分钟 如23:12
     *
     * @param date
     * @return 23:12
     */
    public static String getDateHourAndMinuteStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
        String minute = calendar.get(Calendar.MINUTE) + "";
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return hour + ":" + minute;
    }

    /**
     * 获取和东八区对应的时区差 如果返回结果小于0，说明所在的时区低于东八区，反之高于东八区
     *
     * @param dateArea
     * @return mapd的key分别为hour和minute分别对应相差的小时和分钟数
     */
    public static Map<String, Integer> getDateAreaDiff(String dateArea) {
        /*判断时区*/
        int hoursDiff = 0;  //相差的小时数,如果为负数，表示时间慢于北京时间，反之快于北京时间
        int minutesDiff = 0;  //相差的分钟数,如果为负数，表示时间慢于北京时间，反之快于北京时间
        if (dateArea.contains("-")) {  //如果为西时区,东时区快于西时区
            dateArea = dateArea.replace("-", "");
            double timeDiff = (Double.parseDouble(dateArea) / 100) + 8;  //相差的时间
            String timeDiffStr = timeDiff + "";
            if (Double.toString(timeDiff).contains(".")) {  //如果为非整数小时差
                String[] timeSplits = timeDiffStr.split("\\.");
                hoursDiff = 0 - Integer.parseInt(timeSplits[0]);
                minutesDiff = 0 - Integer.parseInt(timeSplits[1]);
            } else {
                hoursDiff = 0 - (int) timeDiff;  //相差整数小时数
            }
        } else {  //为东时区
            dateArea = dateArea.replace("+", "");
            if (Double.parseDouble(dateArea) / 100 - 8 > 0) {  //如果所在时区大于东八区
                double timeDiff = (Double.parseDouble(dateArea) / 100) - 8;  //相差的时间
                String timeDiffStr = timeDiff + "";
                if (timeDiffStr.contains(".")) {  //如果相差为非整数小时差
                    String[] timeSplits = timeDiffStr.split("\\.");
                    hoursDiff = Integer.parseInt(timeSplits[0]);
                    minutesDiff = Integer.parseInt(timeSplits[1]);
                } else {  //相差为整数小时差
                    hoursDiff = (int) timeDiff;
                }
            } else {  //如果所在的时区低于东八区
                String currentDateArea = Double.toString(Double.parseDouble(dateArea) / 100);
                if (currentDateArea.contains(".")) {  //如果所在的时区不为整数时区
                    /*以60进制计算时间差*/
                    String[] splits = currentDateArea.split("\\.");
                    int currentDate = 60 * Integer.parseInt(splits[0]) + (int) (Double.parseDouble(splits[1]));
                    int BeiJingDate = 8 * 60;
                    int dateDiff = currentDate - BeiJingDate;  //获取相差分钟数
                    hoursDiff = dateDiff / 60;
                    minutesDiff = dateDiff % 60;
                } else {  //如果所在的时区为整数时区
                    double timeDiff = Double.parseDouble(currentDateArea);  //相差的时间
                    hoursDiff = (int) timeDiff;
                }
            }
        }
        Map<String, Integer> map = new HashMap();
        map.put("hour", hoursDiff);
        map.put("minute", minutesDiff);
        return map;
    }

    /**
     * 格式化字符串形式日期
     *
     * @param date
     * @param yyyyMMdd
     * @return
     */
    public static String formatDateStr(String date, SimpleDateFormat yyyyMMdd) {
        try {
            Date parse = yyyyMMdd.parse(date);
            return yyyyMMdd.format(parse);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long getFirstDayNextNextWeek() {
        Calendar calc = Calendar.getInstance();
        //设置一周的第一天是星期一
        calc.setFirstDayOfWeek(Calendar.MONDAY);
        //在今天的基础上加一周
        calc.add(Calendar.WEEK_OF_YEAR, 1);
        //获取当前设置的一周第一天
        int initDay = calc.getFirstDayOfWeek();// 每个星期的第一天根据locale不同而不同
        //下周的第一天
        calc.set(Calendar.DAY_OF_WEEK, initDay);
        // 下下周的第一天(+7)
        calc.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(calc.getTime());
        dateStr += " 00:00:00";
        Long time = calc.getTimeInMillis();
        try {
            Date date = datetimeFormat.parse(dateStr);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 通过年份计算年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeByYear(Date birthday) {
        if (birthday == null) {
            return 0;
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
        String birthYear = yyyy.format(birthday);
        String nowYear = yyyy.format(now);
        return Integer.parseInt(nowYear) - Integer.parseInt(birthYear);

    }
    public static  int getAge(Date birthDay)  {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;//当前日期在生日之前，年龄减一
                }
            }else{
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

	/*public Date getDateByLocal(Locale locale) {
		new Date()
	}*/

    /**
     * 获取一天开始时间 eg 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天结束时间 eg 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DateUtils.getMonthLastDayByDate(new Date(2017 - 1900, 2, 1)));
    }
}