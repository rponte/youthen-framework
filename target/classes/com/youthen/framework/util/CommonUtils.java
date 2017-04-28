package com.youthen.framework.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang.StringUtils;

/**
 * 共通函数
 * 
 * @copyright youthen
 * @author Li Xin
 * @date 2011-5-12
 */
public class CommonUtils {

    public void test() {

    }

    public static void main(final String[] main) {
        System.out.println(getVertifyCode(4));
    }

    /**
     * 生成6位验证码。
     * 
     * @param lenth
     * @return
     */
    public static String getVertifyCode(final int lenth) {
        final Random random = new Random();
        String sRand = "";
        for (int i = 0; i < lenth; i++) {
            // final int wordType = random.nextInt(3);

            final char retWord = getSingleNumberChar();
            // char retWord = 0;
            // switch (wordType) {
            // case 0:
            // retWord = getSingleNumberChar();
            // break;
            // case 1:
            // retWord = getLowerOrUpperChar(0);
            // break;
            // case 2:
            // retWord = getLowerOrUpperChar(1);
            // break;
            // }

            sRand += String.valueOf(retWord);
        }

        return sRand;
    }

    /**
     * 取得一个随机数
     * 
     * @return
     */
    public static char getSingleNumberChar() {
        final Random random = new Random();
        final int numberResult = random.nextInt(10);
        final int ret = numberResult + 48;
        return (char) ret;
    }

    /**
     * 取得一个随机字母
     * 
     * @return
     */
    public static char getLowerOrUpperChar(final int upper) {
        final Random random = new Random();
        final int numberResult = random.nextInt(26);
        int ret = 0;
        if (upper == 0) {// 小写
            ret = numberResult + 97;
        } else if (upper == 1) {// 大写
            ret = numberResult + 65;
        }
        return (char) ret;
    }

    public static String[] removeFromArray(final String[] arr, final int index) {
        if (arr == null || arr.length == 0 || index >= arr.length) return arr;
        final String[] newArr = new String[arr.length - 1];
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != index) {
                newArr[j++] = arr[i];
            }
        }
        return newArr;
    }

    public static String[] addToArray(String[] arr, final String value) {
        if (arr == null) {
            arr = new String[1];
            arr[0] = value;
            return arr;
        }
        final String[] newArr = new String[arr.length + 1];
        int i = 0;
        for (final String str : arr) {
            newArr[i++] = str;
        }
        newArr[newArr.length - 1] = value;
        return newArr;
    }

    /**
     * 把文件转成byte
     * 
     * @param filePath
     * @return byte[]
     */
    public static byte[] fileToByte(final String filePath) {

        byte len[];
        try {
            final File tmpFile = new File(filePath);
            final FileInputStream in = new FileInputStream(tmpFile);
            final BufferedInputStream inFile = new BufferedInputStream(in);
            final int a = Integer.parseInt(String.valueOf(tmpFile.length()));
            len = new byte[a];
            while (inFile.read(len) != -1) {
            }
        } catch (final Exception e) {
            throw new java.lang.RuntimeException(e);
        }

        return len;
    }

    /**
     * 判断数组中是否存在某个值
     * 
     * @param arr
     * @param value
     * @return boolean
     */
    public static boolean arrayExistValue(final int[] arr, final int value) {
        boolean result = false;
        for (final int item : arr) {
            if (value == item) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 字符窜数组按分割付拼接成字符窜
     * 
     * @param arr
     * @param split
     *            分割付
     * @return string
     */
    public static String arrayToString(final String[] arr, final String split) {
        if (arr == null) {
            return null;
        }
        String result = "";
        for (final String str : arr) {
            result += str + split;
        }
        return result;
    }

    public static Date addDay(final Date date, final int days) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date addMonth(final Date date, final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
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
     * 计算总页数
     * 
     * @param rowSize
     * @param numPerPage
     * @return int
     */
    public static int countPages(final int rowSize, final int numPerPage) {
        return (rowSize + (numPerPage - 1)) / numPerPage;
    }

    /**
     * 计算两个日期间相隔的天数
     * 
     * @param d1
     * @param d2
     * @return int
     */
    public static int getDayBetween(final java.util.Date d1, final java.util.Date d2) {
        return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int getDayBetween1(java.util.Date d1, java.util.Date d2) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d1 = sdf.parse(sdf.format(d1));
            d2 = sdf.parse(sdf.format(d2));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 判断某个实例是否是集合或数组。
     * 
     * @param obj
     * @return boolean
     */
    public static boolean isArray(final Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Iterable || obj.getClass().isArray();
    }

    /*
     * 字符串转日期
     */
    public static Date parseDate(final String dateStr, final String format) {
        if (dateStr == null || "".equals(dateStr)) return null;
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(dateStr, new String[] {format});
        } catch (final ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把字符窜转成指定类型
     * 
     * @param type
     * @param value
     * @return object
     */
    @SuppressWarnings("rawtypes")
    private static Object getValue(final Class type, final String value) {
        if (value == null) return null;

        if (type == String.class) {
            return value;
        } else if (type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class) {
            return Long.parseLong(value);
        } else if (type == Date.class) {
            return stringToDate(value, "yyyy-MM-dd");
        } else {
            return value;
        }
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd
     * 
     * @param dateDate
     * @param format
     *            日期格式 如：yyyy-MM-dd，yyyy-MM-dd HH:mm:ss等
     * @return
     */
    public static String dateToStr(final Date date, final String format) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        final String dateString = formatter.format(date);
        return dateString;
    }

    public static List<String[]> getKeyValue(final Map<String, Object> requestMap) {

        int length = 0;
        for (final String key : requestMap.keySet()) {
            if (key.startsWith("DB_")) {
                length++;
            }
        }

        final List<String[]> aColName_value = new ArrayList<String[]>();

        for (final String key : requestMap.keySet()) {
            if (key.startsWith("DB_")) {
                final String[] col_value = new String[2];
                col_value[0] = key.substring(3, key.length());

                final Object obj = requestMap.get(key);
                if (CommonUtils.isArray(obj)) {
                    final String[] ary = (String[]) obj;
                    String value = "";

                    if (ary.length > 1) {

                        for (final String v : ary) {
                            value += v + "|";
                        }

                        col_value[1] = value.substring(0, value.length() - 1);
                    } else {
                        value = ary[0];

                        col_value[1] = value;
                    }
                } else {
                    col_value[1] = (String) requestMap.get(key);
                }
                if (StringUtils.isNotEmpty(col_value[1])) {
                    aColName_value.add(col_value);
                }
            }
        }

        return aColName_value;
    }

    public static String arryToStr(final String[] arry) {
        String cols = "";
        if (arry != null) {

            for (final String item : arry) {
                cols += item + ",";
            }
            cols = cols.substring(0, cols.length() - 1);
        }
        return cols;
    }

}
