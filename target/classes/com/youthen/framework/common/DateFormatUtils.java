package com.youthen.framework.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import com.youthen.framework.common.exception.ApplicationException;

public final class DateFormatUtils {

    private DateFormatUtils() {
    }

    public static Date parse(final String aPattern, final String aDate) {
        try {
            return DateUtils.parseDateStrictly(aDate, new String[] {aPattern});
        } catch (final ParseException e) {
            throw new ApplicationException(new SimpleAppMessage("XFW70004"), e);
        }
    }

    public static boolean checkParse(final String aPattern, final String aDate) {
        boolean ret = false;
        try {
            parse(aPattern, aDate);
            ret = true;
        } catch (final ApplicationException e) {
            ret = false;
        }
        return ret;
    }

    public static boolean checkParseFormat(final String aPattern, final String aDate) {
        boolean ret = false;
        try {
            final Date parseDate = parse(aPattern, aDate);
            final String formatDate = format(aPattern, parseDate);
            if (aDate.equals(formatDate)) {
                ret = true;
            } else {
                ret = false;
            }
        } catch (final ApplicationException e) {
            ret = false;
        }
        return ret;
    }

    public static String format(final String aPattern, final Date aDate) {
        return org.apache.commons.lang.time.DateFormatUtils.format(aDate, aPattern);
    }

    public static String format(final Date aDate, final String aPattern) {
        return org.apache.commons.lang.time.DateFormatUtils.format(aDate, aPattern);
    }

    /**
     * 日期转字符串
     * 
     * @param date
     * @param format
     * @return string
     */
    public static String formatDate(final Date date, final String format) {
        if (date == null) {
            return "";
        }
        final SimpleDateFormat dateformat = new SimpleDateFormat(format);
        return dateformat.format(date);
    }

    /**
     * 字符窜转日期
     * 
     * @param date
     * @param format
     * @return date
     */
    public static Date stringToDate(final String date, final String format) {
        if (StringUtils.isBlank(date)) return null;
        final SimpleDateFormat dateformat = new SimpleDateFormat(format);
        try {
            return dateformat.parse(date);
        } catch (final ParseException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 日期+添加天数
     * 
     * @param curDate
     * @param days
     * @return Date
     */
    public static Date addDay(final Date curDate, final int days) {
        final Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(DateFormatUtils.formatDate(curDate, "yyyy")),
                Integer.parseInt(DateFormatUtils.formatDate(curDate, "MM")) - 1,
                Integer.parseInt(DateFormatUtils.formatDate(curDate, "dd")) + days);
        return c.getTime();
    }

    /**
     * 日期+添加月
     * 
     * @param curDate
     * @param months
     * @return Date
     */
    public static Date addMonth(final Date curDate, final int months) {
        final String resultDate =
                FormatStrUtils.getResultDate(DateFormatUtils.format("yyyy-MM-dd", curDate), months, "月");
        final String[] resultDateArray = resultDate.split("-");
        final Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(resultDateArray[0]),
                Integer.parseInt(resultDateArray[1]) - 1,
                Integer.parseInt(resultDateArray[2]));
        return c.getTime();
    }

    /**
     * 日期+添加年
     * 
     * @param curDate
     * @param years
     * @return Date
     */
    public static Date addYear(final Date curDate, final int years) {
        final String resultDate =
                FormatStrUtils.getResultDate(DateFormatUtils.format("yyyy-MM-dd", curDate), years, "年");
        final String[] resultDateArray = resultDate.split("-");
        final Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(resultDateArray[0]),
                Integer.parseInt(resultDateArray[1]) - 1,
                Integer.parseInt(resultDateArray[2]));
        return c.getTime();
    }

    /**
     * 计算两日期之间差多少天。
     * 
     * @param date1
     * @param date2
     * @return days
     */
    public static int daysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;
        date1 = DateFormatUtils.parse("yyyy-MM-dd", DateFormatUtils.format("yyyy-MM-dd", date1));
        date2 = DateFormatUtils.parse("yyyy-MM-dd", DateFormatUtils.format("yyyy-MM-dd", date2));

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        final long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        final long time2 = cal.getTimeInMillis();
        final long days = (time2 - time1) / (1 * 60 * 60 * 1000 * 24);
        return (int) days;
    }

    public static void main(final String[] args) {
        System.out.println(DateFormatUtils.daysBetween(new Date(),
                DateFormatUtils.parse("yyyy-MM-dd", "2015-11-04")));
        System.out.println(DateFormatUtils.format("yyyy-MM-dd", DateFormatUtils.addYear(new Date(), 1)));
    }
}
