package com.mokylin.util;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.chrono.ISOChronology;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** @author Liwei */
public class TimeUtils {

    private static final Map<String, ThreadLocal<SimpleDateFormat>> sdfMap =
        new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    public static final SimpleDateFormat YMDHMSS_FORMAT =
        getSimpleDateFormat("YYYY_MM_dd_HH_mm_ss");
    public static final SimpleDateFormat YMD_FORMAT = getSimpleDateFormat("YYYY_MM_dd");

    public static Date DEFAULT_YMD;
    public static Date DEFAULT_YMDHMSS;

    static {
        try {

            DEFAULT_YMD = YMD_FORMAT.parse("2000_01_01");
            DEFAULT_YMDHMSS = YMD_FORMAT.parse("2000_01_01_01_01_01");
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }

    }

    private static final ISOChronology chronology = ISOChronology.getInstance();

    public static final SimpleDateFormat getSimpleDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdf = sdfMap.get(pattern);
        if (sdf == null) {
            sdf = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(pattern);
                }
            };
            sdfMap.put(pattern, sdf);
        }
        return sdf.get();
    }


    public static int toHourOfDay(long instant) {
        return chronology.hourOfDay().get(instant);
    }

    /**
     * 返回多少号，取值[1-31]
     *
     * @param instant
     * @return
     */
    public static int toDayOfMonth(long instant) {
        return chronology.dayOfMonth().get(instant);
    }

    /**
     * 返回多少号，取值[1-12]
     *
     * @param instant
     * @return
     */
    public static int toMonthOfYear(long instant) {
        return chronology.monthOfYear().get(instant);
    }

    public static int toYear(long instant) {
        return chronology.year().get(instant);
    }

    // 获得下周星期一零点时间
    public static long getNextSpecifiedMonday(long curTime, int weekCount) {
        return getNextWeekDay(curTime, weekCount, 1);
    }

    /** 获取指定时间的下一个月一号零点的时间点 */
    public static long getTheFirstDayOfNextSpecifiedMonth(long time, int monthCount) {
        LocalDate today = new LocalDate(time);
        return today.plusMonths(monthCount).withDayOfMonth(1).toDate().getTime();
    }

    // 获得下个星期天（包括今天）
    public static long getNextSundayIncludeToday(long curTime) {
        final LocalDate today = new LocalDate(curTime);
        int old = today.getDayOfWeek();
        int sunday = 0;

        if (sunday < old) {
            sunday += 7;
        }
        LocalDate next = today.plusDays(sunday - old);
        return next.toDate().getTime();
    }

    // 获得下个星期六
    public static long getNextSaturday(long curTime, int weekCount) {
        return getNextWeekDay(curTime, weekCount, 6);
    }

    public static long getNextWeekDay(long curTime, int weekCount, int day) {
        final LocalDate today = new LocalDate(curTime);
        int old = today.getDayOfWeek();

        if (day <= old) {
            day += 7 * weekCount;
        }
        LocalDate next = today.plusDays(day - old);
        return next.toDate().getTime();
    }

    /**
     * 判断某一个操作，自上次执行的时间点lastOpTime之后，当前时间点currentTime经过了 多少次重置时间点resetTime
     *
     * <p>
     */
    public static int getDoDailyResetCount(long currentTime, long lastOpTime, LocalTime resetTime) {
        if (currentTime <= lastOpTime) {
            return 0;
        }

        long resetMillisOfDay = resetTime.getMillisOfDay();
        return Days.daysBetween(new LocalDate(lastOpTime - resetMillisOfDay),
            new LocalDate(currentTime - resetMillisOfDay)).getDays();
    }

    private TimeUtils() {
    }

    public static boolean isValidMonthDay(long currentTime, int dayOfMonth) {
        LocalDate localDate = new LocalDate(currentTime);
        int maxDayOfMonth = localDate.dayOfMonth().getMaximumValue();
        return dayOfMonth >= 1 && dayOfMonth <= maxDayOfMonth;
    }

    public static boolean isWorkDay(long ctime) {
        LocalDate localDate = new LocalDate(ctime);
        int day = localDate.dayOfWeek().get();
        return day != DateTimeConstants.SATURDAY && day != DateTimeConstants.SUNDAY;
    }

    public static boolean isSameWeek(long a, long b) {
        LocalDate localDate = new LocalDate(a);
        LocalDate localDate1 = new LocalDate(b);
        return localDate.getWeekOfWeekyear() == localDate1.getWeekOfWeekyear() &&
            localDate.getWeekyear() == localDate1.getWeekyear();
    }
}
