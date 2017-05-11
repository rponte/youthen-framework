package com.youthen.framework.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 字符串转换的工具类
 * 
 * @copyright youthen
 * @author 杨新伦
 * @date 2014-3-4
 */
public class FormatStrUtils {

    /**
     * 解决字符换行和空格显示的问题
     * 
     * @param content
     * @return 字符串
     */
    public static String n2brInV(String content) {
        if (content != null) {
            content = content.replaceAll("\r |\r", "<br/>").replaceAll(" ", "&nbsp;");
        }
        return content;
    }

    /**
     * 解决空格显示的问题
     * 
     * @param content
     * @return 字符串
     */
    public static String n2brInE(String content) {
        if (content != null) {
            content = content.replaceAll("\r ", "\r").replaceAll("&lt;", "<");
        }
        return content;
    }

    /**
     * img input标签加结束标签
     * 
     * @param str
     * @return String
     */
    public static String imgOrInputHtmlConvert(String str, final int imgFromIdx, final int inputFromIdx) {
        int tmpImgFromIdx = imgFromIdx;
        int tmpInputFromIdx = inputFromIdx;
        // ie标签处理
        str = str.replaceAll("IMG", "img").replaceAll("INPUT", "input");
        if (str.indexOf("<img", tmpImgFromIdx) > 0) {
            final int endImgIdx = str.indexOf(">", str.indexOf("<img", tmpImgFromIdx));
            tmpImgFromIdx = endImgIdx;
            if (!str.substring(endImgIdx + 1, endImgIdx + 7).equals("</img>")) {
                str = str.substring(0, endImgIdx + 1) + "</img>" + str.substring(endImgIdx + 1);
            }
        }
        if (str.indexOf("<input", tmpInputFromIdx) > 0) {
            final int endInputIdx = str.indexOf(">", str.indexOf("<input", tmpInputFromIdx));
            tmpInputFromIdx = endInputIdx;
            if (!str.substring(endInputIdx + 1, endInputIdx + 9).equals("</input>")) {
                str = str.substring(0, endInputIdx + 1) + "</input>" + str.substring(endInputIdx + 1);
            }
        }
        if (imgFromIdx != tmpImgFromIdx || inputFromIdx != tmpInputFromIdx) {
            return imgOrInputHtmlConvert(str, tmpImgFromIdx, tmpInputFromIdx);
        }
        return str.toLowerCase().replaceAll("n.a.", "N.A.");
    }

    /**
     * 把判断对象是否是数组，如果不是转化成数组
     * 
     * @param content
     * @return
     */
    public static Object getArray(final Object obj) {
        String[] rObj = new String[0];
        if (obj instanceof String[]) {
            rObj = (String[]) obj;
        } else if (obj instanceof List) {
            final List objList = (List) obj;
            rObj = new String[objList.size()];
            for (int i = 0; i < objList.size(); i++) {
                rObj[i] = objList.get(i).toString();
            }
        } else if (obj != null) {
            rObj = new String[1];
            rObj[0] = obj.toString();
        }
        return rObj;
    }

    /**
     * 把判断字符串日期是否是周末
     * 
     * @param content
     * @return
     */
    public static boolean isWeeked(final String dateStr) {
        final Calendar c = Calendar.getInstance();
        final String[] dateStrArray = dateStr.split("-");
        if (dateStrArray.length != 3) return false;
        try {
            c.set(Integer.parseInt(dateStrArray[0]), Integer.parseInt(dateStrArray[1]) - 1,
                    Integer.parseInt(dateStrArray[2]));
        } catch (final Exception e) {
            return false;
        }
        final int weekIdx = c.get(Calendar.DAY_OF_WEEK) - 1;
        return weekIdx == 0 || weekIdx == 6;
    }

    /**
     * 判断Session用户是是存在以subRole开头的角色
     */
    public static Boolean ifLikeGranted(final String subRole) {
        final Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        final Collection<? extends GrantedAuthority> granted = currentUser.getAuthorities();
        for (final GrantedAuthority grantedAuthority : granted) {
            String[] subRoleArray = new String[] {subRole};
            if (subRole.indexOf(",") > -1) {
                subRoleArray = subRole.split(",");
            }
            for (int i = 0; i < subRoleArray.length; i++) {
                if (grantedAuthority.toString().indexOf(subRoleArray[i]) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断今天是否是一个月最后一天
     */
    public static boolean isLastDayOfMonth() {
        final Date curDate = new Date();
        final String curDateStr = DateFormatUtils.format("yyyy-MM-dd", curDate);
        final String nextDayStr = FormatStrUtils.getResultDate(curDateStr, 5, "日");
        return !curDateStr.substring(0, 7).equals(nextDayStr.substring(0, 7));
    }

    /**
     * 把判断数组中是否存在某个元素
     * 
     * @param content
     * @return
     */
    public static boolean contains(final Object array, final Object element) {
        final String[] strArray = (String[]) getArray(array);
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].trim().equals(element.toString().trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取默认select
     */
    public static List<Map<String, String>> getDefMapList() {
        final List<Map<String, String>> selectTblList = new ArrayList<Map<String, String>>();
        final Map<String, String> recorderMap1 = new HashMap<String, String>();
        recorderMap1.put("OPT", "是");
        final Map<String, String> recorderMap2 = new HashMap<String, String>();
        recorderMap2.put("OPT", "否");

        selectTblList.add(recorderMap1);
        selectTblList.add(recorderMap2);
        return selectTblList;
    }

    /**
     * 日期计算函数(添加年、月、日)
     */
    public static String getResultDate(final String dateVal, final Integer addNum, final String addType) {
        String resultDate = "";
        final String[] dateArray = dateVal.substring(0, 10).split("-");
        if (dateArray.length == 3) {
            final Calendar c = Calendar.getInstance();
            final int resultYear = Integer.parseInt(dateArray[0]) + (addType.equals("年") ? addNum : 0);
            final int resultMonth = Integer.parseInt(dateArray[1]) - 1 + (addType.equals("月") ? addNum : 0);
            int resultDay = Integer.parseInt(dateArray[2]) + (addType.equals("日") ? addNum : 0);
            c.set(resultYear, resultMonth, 1);
            final int maxDay = c.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            if (!addType.equals("日") && resultDay > maxDay) {
                resultDay = maxDay;
            }
            c.set(resultYear, resultMonth, resultDay);
            resultDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        }
        return resultDate;
    }

    /**
     * 根据接受限度、内控限度、结果获取判断结果
     * 
     * @param accMeasure
     * @param riskMeasure
     * @param testResult
     * @return String
     */
    public static String getTIResult(String accMeasure, String riskMeasure, String testResult) {
        accMeasure = accMeasure.replaceAll(" ", "").replaceAll("　", "").replaceAll("&gt;", ">").replaceAll("&lt;", "<");
        // accMeasure = accMeasure.replaceAll("[^0-9.]", "");
        riskMeasure =
                riskMeasure.replaceAll(" ", "").replaceAll("　", "").replaceAll("&gt;", ">").replaceAll("&lt;", "<");
        // riskMeasure = riskMeasure.replaceAll("[^0-9.]", "");
        testResult = testResult.replaceAll(" ", "").replaceAll("　", "");
        if (StringUtils.isEmpty(testResult)) {
            return "";
        }
        String resultIdx = "0";
        if (accMeasure.equals("符合") || riskMeasure.equals("符合")) {
            resultIdx = testResult.equals("符合") ? "2" : "0";
        } else {
            LinkedList<Integer> idxList = new LinkedList<Integer>();
            final LinkedList<Integer> accMeasureIdxList = getFomulaIdx(idxList, accMeasure);
            idxList = new LinkedList<Integer>();
            final LinkedList<Integer> riskMeasureIdxList = getFomulaIdx(idxList, riskMeasure);
            if (accMeasureIdxList.size() == 1) {
                resultIdx =
                        getResultByOneFomula(accMeasure, accMeasureIdxList.get(0).intValue(), testResult) ? "1"
                                : resultIdx;
            } else if (accMeasureIdxList.size() == 2) {
                resultIdx = getResultByTwoFomula(accMeasure, accMeasureIdxList, testResult) ? "1" : resultIdx;
            }

            if (riskMeasureIdxList.size() == 1) {
                resultIdx =
                        getResultByOneFomula(riskMeasure, riskMeasureIdxList.get(0).intValue(), testResult) ? "2"
                                : resultIdx;
            } else if (riskMeasureIdxList.size() == 2) {
                resultIdx = getResultByTwoFomula(riskMeasure, riskMeasureIdxList, testResult) ? "2" : resultIdx;
            }
        }
        return resultIdx;
    }

    /**
     * 根据接受限度、内控限度、结果获取判断结果:此处用于环境检测
     * 
     * @param accMeasure
     * @param riskMeasure
     * @param testResult
     * @return String
     */
    public static String getTIResultForHj(String accMeasure, String riskMeasure, String testResult) {
        accMeasure = accMeasure.replaceAll(" ", "").replaceAll("　", "").replaceAll("&gt;", ">").replaceAll("&lt;", "<");
        accMeasure = accMeasure.replaceAll("[^0-9.]", "");
        riskMeasure =
                riskMeasure.replaceAll(" ", "").replaceAll("　", "").replaceAll("&gt;", ">").replaceAll("&lt;", "<");
        riskMeasure = riskMeasure.replaceAll("[^0-9.]", "");
        testResult = testResult.replaceAll(" ", "").replaceAll("　", "");
        if (StringUtils.isEmpty(testResult)) {
            return "";
        }
        String resultIdx = "0";
        if (accMeasure.equals("符合") || riskMeasure.equals("符合")) {
            resultIdx = testResult.equals("符合") ? "2" : "0";
        } else {
            LinkedList<Integer> idxList = new LinkedList<Integer>();
            final LinkedList<Integer> accMeasureIdxList = getFomulaIdx(idxList, accMeasure);
            idxList = new LinkedList<Integer>();
            final LinkedList<Integer> riskMeasureIdxList = getFomulaIdx(idxList, riskMeasure);
            if (accMeasureIdxList.size() == 1) {
                resultIdx =
                        getResultByOneFomula(accMeasure, accMeasureIdxList.get(0).intValue(), testResult) ? "1"
                                : resultIdx;
            } else if (accMeasureIdxList.size() == 2) {
                resultIdx = getResultByTwoFomula(accMeasure, accMeasureIdxList, testResult) ? "1" : resultIdx;
            }

            if (riskMeasureIdxList.size() == 1) {
                resultIdx =
                        getResultByOneFomula(riskMeasure, riskMeasureIdxList.get(0).intValue(), testResult) ? "2"
                                : resultIdx;
            } else if (riskMeasureIdxList.size() == 2) {
                resultIdx = getResultByTwoFomula(riskMeasure, riskMeasureIdxList, testResult) ? "2" : resultIdx;
            }
        }
        return resultIdx;
    }

    private final static String[] folumaArray = new String[] {"≥", "≤", ">", "<", "="};

    private static LinkedList<Integer> getFomulaIdx(final LinkedList<Integer> idxList, final String str) {
        int resultNum = -1;
        for (int i = 0; i < folumaArray.length; i++) {
            final int idx = str.indexOf(folumaArray[i]);
            if (idx >= 0) {
                final Integer intIdx = Integer.valueOf(idx);

                if (idxList.size() > 0 && idxList.get(idxList.size() - 1).intValue() > idx) {
                    idxList.addFirst(intIdx);
                } else {
                    idxList.add(intIdx);
                }
                resultNum = i;
                break;
            }
        }
        if (resultNum > -1) {
            return getFomulaIdx(idxList, str.replaceFirst(folumaArray[resultNum], "F"));
        }
        return idxList;
    }

    private static boolean getResultByOneFomula(final String str, final int idx, final String testResult) {
        final Pattern pattern = Pattern.compile("([+-]?)\\d*\\.?\\d+");
        if (!pattern.matcher((str.indexOf("%") > 0) ? str.substring(1, str.length() - 1) : str.substring(1)).matches()
                || !pattern.matcher(
                        (testResult.indexOf("%") > 0) ? testResult.substring(0, testResult.length() - 1) : testResult)
                        .matches()) {
            return false;
        }

        final double targetNum =
                (str.indexOf("%") > 0) ? Double.parseDouble(str.substring(1, str.length() - 1)) / 100 : Double
                        .parseDouble(str.substring(1));
        final double testResultNum =
                (testResult.indexOf("%") > 0)
                        ? Double.parseDouble(testResult.substring(0, testResult.length() - 1)) / 100 : Double
                                .parseDouble(testResult);
        switch (str.charAt(idx)) {
            case '=':
                return testResultNum == targetNum;
            case '>':
                return testResultNum > targetNum;
            case '<':
                return testResultNum < targetNum;
            case '≥':
                return testResultNum >= targetNum;
            case '≤':
                return testResultNum <= targetNum;
            default:
                return false;
        }
    }

    private static boolean getResultByTwoFomula(final String str, final LinkedList<Integer> fomulaList,
            final String testResult) {
        final int firstFomulaIdx = fomulaList.get(0).intValue();
        final int secondFomulaIdx = fomulaList.get(1).intValue();

        final char firstFomulaStr = str.charAt(firstFomulaIdx);

        if (getResultByOneFomula(firstFomulaStr + testResult, 0, str.substring(0, firstFomulaIdx))) {
            if (getResultByOneFomula(str.substring(secondFomulaIdx), 0, testResult)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 下拉信息是否被选中HTML
     * 
     * @param curVal
     * @param tmpVal
     * @param tmpTxt
     * @return String
     */
    public static String getselectedhtml(final Object tmpVal, final Object tmpTxt, Object curVal) {
        if (curVal == null) curVal = "";
        return "<option value='" + tmpVal + "' "
                + (curVal.toString().equals(tmpVal.toString()) ? "selected='selected'>" + tmpTxt.toString()
                        + "</option>" : ">" + tmpTxt.toString() + "</option>");

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

    public static String fmtUptDate(final String uptDate) {
        final Date date = DateFormatUtils.parse("yyyyMMddHHmmss", uptDate);
        return DateFormatUtils.format("yyyy-MM-dd HH:mm:ss", date);
    }

    public static String format(final Date date, final String format) {
        if (date == null) return "";
        return org.apache.commons.lang.time.DateFormatUtils.format(date, format);
    }

    /**
     * 判断数值是否为数组
     * 
     * @param objValue
     * @return 数值是否为数组
     */
    public static boolean isArray(final Object objValue) {
        if (objValue == null) {
            return false;
        } else if (objValue instanceof String) {
            return false;
        } else if (objValue instanceof Collection<?>) {
            return true;
        } else if (objValue instanceof Map<?, ?>) {
            return true;
        } else if (objValue instanceof Object[]) {
            return true;
        }
        return false;
    }

    public static String getPlanDate(final Date lastCheckDate, final Integer curMonth, final Integer cycle) {
        if (lastCheckDate == null || curMonth == null || cycle == null || cycle == 0) {
            return "";
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(lastCheckDate);
        final Calendar curCal = Calendar.getInstance();
        curCal.setTime(new Date());
        final Calendar cal2 = Calendar.getInstance();
        cal2.set(curCal.get(Calendar.YEAR), curMonth - 1, cal1.get(Calendar.DAY_OF_MONTH));
        final int mouthSpec = cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        if (mouthSpec % cycle == 0) {
            return FormatStrUtils.getResultDate(DateFormatUtils.format("yyyy-MM-dd", cal1.getTime()), mouthSpec, "月");
        }
        return "";
    }
}
