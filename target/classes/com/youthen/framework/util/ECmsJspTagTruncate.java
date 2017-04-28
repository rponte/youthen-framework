/**
 * youthen Sisqp Sytem 2011
 */
package com.youthen.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Sisqp Project Source Code
 * 
 * @copyright youthen
 * @author wangkai
 * @date 2011-7-21
 */
public class ECmsJspTagTruncate extends TagSupport {

    /** Serial version UID required for safe serialization. */
    private static final long serialVersionUID = -4040833186555867977L;

    /** The log object for this class. */
    // private static final Log LOG = CmsLog.getLog(ECmsJspTagTruncate.class);

    private static final String DEFAULT_SUFFIX = "...";

    private String m_value;
    private int m_length;
    private String m_suffix;
    private boolean m_charBoundary;

    private static String bSubstring(final String s, final int length, final String suffix)
            throws UnsupportedEncodingException {

        final byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始

        if (s.length() < 3) {
            return s;
        }

        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0) i = i - 1;
            // 该UCS2字符是字母或数字，则保留该字符
            else i = i + 1;
        }

        return (new String(bytes, 0, i, "Unicode")) + ((i < bytes.length) ? suffix : "");
    }

    private static String truncateString(final String value, final int length, final String suffix,
            final boolean charBoundary) {
        if (charBoundary) {
            try {
                return bSubstring(value, length, suffix);
            } catch (final UnsupportedEncodingException e) {
                return "";
            }
        } else {
            if (value.length() <= length) return value;
            return value.substring(0, length) + suffix;
        }
    }

    // 获得字符子串
    @SuppressWarnings("unused")
    private static String bSubstr(final String s, final int pos, final int length) throws UnsupportedEncodingException {

        final byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = pos;

        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1) {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0) i = i - 1;
            // 该UCS2字符是字母或数字，则保留该字符
            else i = i + 1;
        }

        return (new String(bytes, pos, i, "Unicode"));
    }

    // / 将字符串换行
    @SuppressWarnings("unused")
    private static String turnNewLine(final Object value, final int length) throws UnsupportedEncodingException {
        String tmp;

        // 如果为空返回N.A.
        try {
            tmp = value.toString();
        } catch (final Exception e) {
            return "N.A.";
        }

        final byte[] bytes = tmp.getBytes("Unicode");

        // 如果字符串小于需要换行的长度，直接返回
        if (bytes.length <= length) {
            return tmp;
        }
        int hang;

        hang = bytes.length / length + 1;

        String rtn = "";
        for (int i = 0; i < hang; i++) {
            // rtn = rtn + bSubstr(tmp, i * len,len) + "<br />";
            rtn = rtn + tmp.substring(i * length, length) + "<br / >";
        }
        //
        return rtn;
    }

    @Override
    public int doStartTag() throws JspException {
        final String value = getValue();
        final int length = getLength();
        final String suffix = getSuffix();
        final boolean charBoundary = getCharBoundary();

        try {
            this.pageContext.getOut().print(truncateString(value, length, suffix, charBoundary));
        } catch (final IOException ex) {

            throw new javax.servlet.jsp.JspException(ex);
        }

        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    @Override
    public int doEndTag() {

        // need to release manually, JSP container may not call release as required (happens with Tomcat)
        release();
        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        this.m_value = null;
        this.m_length = 0;
        this.m_suffix = null;
        this.m_charBoundary = false;
    }

    public String getValue() {
        if (this.m_value == null) return "";
        return this.m_value;
    }

    public int getLength() {
        if (this.m_length < 0) return 0;
        return this.m_length;
    }

    public String getSuffix() {
        if (this.m_suffix == null) return "";
        return this.m_suffix;
    }

    public boolean getCharBoundary() {
        return this.m_charBoundary;
    }

    public void setValue(final String value) {
        if (value != null) this.m_value = value;
    }

    public void setLength(final int length) {
        if (length > 0) this.m_length = length;
        else this.m_length = 0;
    }

    public void setSuffix(final String suffix) {
        if (suffix != null) this.m_suffix = suffix;
        else this.m_suffix = DEFAULT_SUFFIX;
    }

    public void setCharBoundary(final boolean charBoundary) {
        this.m_charBoundary = charBoundary;
    }
}
