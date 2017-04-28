package com.youthen.framework.util.logging;

import org.apache.log4j.PatternLayout;

/**
 * BusinessLogic annotation。
 * 
 * @author LiXin
 * @version $Revision: 1 $<br>
 *          $Date: 2014-07-14 19:26:55 $
 */
public class SisqpSMTPPatternLayout extends PatternLayout {

    String charset = "UTF-8";

    /**
     * Charset设定。
     * 
     * @param aCharset Charset
     */
    public void setCharset(final String aCharset) {
        this.charset = aCharset;
    }

    /**
     * Charset取得。
     * 
     * @return Charset
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * @see org.apache.log4j.Layout#getContentType()
     */
    @Override
    public String getContentType() {
        final StringBuilder result = new StringBuilder();
        result.append("text/plain; ");
        result.append("charset=\"");
        result.append(getCharset());
        result.append("\"");
        return result.toString();
    }

}
