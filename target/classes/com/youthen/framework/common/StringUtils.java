package com.youthen.framework.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.Assert;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public final class StringUtils {

    public static final List<String> BASIC_STRING = Arrays.asList("&", "<", ">", "\"");

    private StringUtils() {
    }

    /**
     * <p>
     * <table>
     * <tr>
     * <th>aString1</th>
     * <th>aString2</th>
     * <th>返回值</th>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>"abc"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>"def"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"abcef"</td>
     * <td>"abc"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"ABC"</td>
     * <td>"abc"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>null</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>"abc"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>null</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>""</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>null</td>
     * <td>true</td>
     * </tr>
     * </table>
     * 
     * @param aString1
     *            文字列1
     * @param aString2
     *            文字列2
     * @return aString1 和 aString2 的值相同的场合 true、其它场合 false
     */
    public static boolean isEqual(final String aString1, final String aString2) {
        boolean boolReturn = false;
        if (aString1 != null && aString2 != null) {
            boolReturn = aString1.equals(aString2);
        } else if (aString1 == null && aString2 == null) {
            boolReturn = true;
        }
        return boolReturn;
    }

    /**
     * <p>
     * <table>
     * <tr>
     * <th>aString1</th>
     * <th>aString2</th>
     * <th>返回值</th>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>"abc"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>"def"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"abcef"</td>
     * <td>"abc"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"ABC"</td>
     * <td>"abc"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"abc"</td>
     * <td>null</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>"abc"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>null</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>""</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>null</td>
     * <td>false</td>
     * </tr>
     * </table>
     * 
     * @param aString1
     *            文字列1
     * @param aString2
     *            文字列2
     * @return aString1 和 aString2 的值相同的场合 true、其它场合 false
     */
    public static boolean isNotEqual(final String aString1, final String aString2) {
        return !isEqual(aString1, aString2);
    }

    /**
     * <p>
     * <table>
     * <tr>
     * <th>aString</th>
     * <th>返回值</th>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"   "</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"   abc"</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"abc   "</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>" a b c "</td>
     * <td>false</td>
     * </tr>
     * </table>
     * 
     * @param aString
     *            输入文字
     * @return aString 为null且为空白的场合为 true、其它场合为 false
     */
    public static boolean isEmpty(final String aString) {
        if (aString != null && aString.length() > 0) {
            final int strLen = aString.length();
            for (int i = strLen - 1; i >= 0; i--) {
                if (!Character.isWhitespace(aString.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * <p>
     * <table>
     * <caption>输入值和结果</caption>
     * <tr>
     * <th>aString</th>
     * <th>返回值</th>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"   "</td>
     * <td>false</td>
     * </tr>
     * <tr>
     * <td>"   abc"</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>"abc   "</td>
     * <td>true</td>
     * </tr>
     * <tr>
     * <td>" a b c "</td>
     * <td>true</td>
     * </tr>
     * </table>
     * 
     * @param aString
     *            入力文字列
     * @return aString 非空为 true、否则false
     */
    public static boolean isNotEmpty(final String aString) {
        return !isEmpty(aString);
    }

    /**
     * @param aString 対象文字列
     * @param aMap 変換定義(key: 元、 value: 先)
     * @return 変換后文字列
     */
    public static String replaceEach(final String aString, final Map<String, String> aMap) {

        final int size = aMap.size();
        final String[] keys = new String[size];
        final String[] vals = new String[size];

        int count = 0;
        for (final Map.Entry<String, String> entry : aMap.entrySet()) {
            keys[count] = entry.getKey();
            vals[count] = entry.getValue();
            count++;
        }
        return org.apache.commons.lang.StringUtils.replaceEach(aString, keys, vals);

    }

    /**
     * <p>
     * <table>
     * <caption>输入值和结果</caption>
     * <tr>
     * <th>aString</th>
     * <th>aLength</th>
     * <th>返回值</th>
     * </tr>
     * <tr>
     * <td>"1234567890"</td>
     * <td>3</td>
     * <td>{"123", "456", "789", "0"}</td>
     * </tr>
     * <tr>
     * <td>"123"</td>
     * <td>3</td>
     * <td>{"123"}</td>
     * </tr>
     * <tr>
     * <td>""</td>
     * <td>3</td>
     * <td>{""}</td>
     * </tr>
     * <tr>
     * <td>null</td>
     * <td>3</td>
     * <td>IllegalArgumentException</td>
     * </tr>
     * <tr>
     * <td>"123"</td>
     * <td>0</td>
     * <td>IllegalArgumentException</td>
     * </tr>
     * </table>
     * 
     * @param aString 分割前文字列
     * @param aLength 分割文字数
     * @return 分割後文字列
     */
    public static String[] splitByLength(final String aString, final int aLength) {

        Assert.notNull(aString, "aString must not be null.");
        Assert.isTrue(aLength >= 1, "aLength must be more than 1.");

        if (aString.length() <= aLength) {
            return new String[] {aString};
        }

        final int remainder = aString.length() % aLength;
        final int arraySize;
        if (remainder > 0) {
            arraySize = aString.length() / aLength + 1;
        } else {
            arraySize = aString.length() / aLength;
        }

        final String[] splited = new String[arraySize];
        for (int i = 0; i < arraySize; i++) {
            final int begeinIndex = aLength * i;
            int endIndex = aLength * i + aLength;
            if (endIndex > aString.length()) {
                endIndex = begeinIndex + remainder;
            }
            splited[i] = aString.substring(begeinIndex, endIndex);
        }
        return splited;
    }

    public static String defaultString(final Object aObj) {
        if (aObj == null) {
            return "";
        }
        return ConvertUtils.convert(aObj);
    }

    public static String escapeHtml(final String aStr) {
        if (StringUtils.isNotEmpty(aStr)) {
            return aStr.toString().replaceAll(BASIC_STRING.get(0), "&amp;")
                    .replaceAll(BASIC_STRING.get(1), "&lt;")
                    .replaceAll(BASIC_STRING.get(2), "&gt;")
                    .replaceAll(BASIC_STRING.get(3), "&quot;");
        }
        return "";
    }

    /**
     * lt
     * 
     * @param args 字符串数组
     * @return 返回过滤掉重复元素的数组
     */
    public static String[] filterRepeatArrE(final String[] arr) {
        final List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            if (!stringList.contains(arr[i])) {
                stringList.add(arr[i]);
            }
        }
        return stringList.toArray(new String[0]);
    }

    public static void main(final String[] args) {
        final String[] arr = {"N.A.", "b", "A", "A", "a", "b"};
        final String[] a = StringUtils.filterRepeatArrE(arr);
        for (final String s : a) {
            System.out.println(s);
        }
    }

}
