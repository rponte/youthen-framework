// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 。
 * 
 * @author Mzj
 * @author Modifier By $Author: $
 * @version $Revision: $<br>
 *          $Date: $
 */
public class WeatherTest {

    static String httpUrl =
            "http://www.kuaidi100.com/poll/query.do?customer=99F71E48649BF786D0EDD8053B0298AF&sign=e4cc7baae6f9c36714db3b47ca9a64ec&param={'com':'shunfeng','num':'605125245240','from':'湖北武汉','to':'上海闵行'}";

    public static void main(final String[] args) throws Exception {
        final String jsonResult = request(httpUrl);
        System.out.println(jsonResult);
    }

    public static String request(final String httpUrl) {
        BufferedReader reader = null;
        String result = null;
        final StringBuffer sbf = new StringBuffer();
        try {
            final URL url = new URL(httpUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();
            final InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
