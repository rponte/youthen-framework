// ============================================================
// Copyright(c) youthen Incorporated All Right Reserved.
// File: $Id$
// ============================================================

package com.youthen.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeatherUtil {

    private static String SERVICES_HOST = "www.webxml.com.cn";
    private static String WEATHER_SERVICES_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/";
    private static String PROVINCE_CODE_URL = WEATHER_SERVICES_URL
            + "getRegionProvince";
    private static String CITY_CODE_URL = WEATHER_SERVICES_URL
            + "getSupportCityString?theRegionCode=";
    private static String WEATHER_QUERY_URL = WEATHER_SERVICES_URL
            + "getWeather?theUserID=&theCityCode=";

    public WeatherUtil() {
    }

    public static void main(final String[] args) throws Exception {
        final int provinceCode = 311102;// getProvinceCode("上海"); // 311102
        final int cityCode = 2013;// getCityCode(provinceCode, "上海"); // 2013
        final List<String> weatherList = getWeather(cityCode);
        int i = 0;
        for (final String weather : weatherList) {
            System.out.println(i + "=" + weather);
            i++;
        }
    }

    public static int getProvinceCode(final String provinceName) {
        Document document;
        final DocumentBuilderFactory documentBF = DocumentBuilderFactory.newInstance();
        documentBF.setNamespaceAware(true);
        int provinceCode = 0;
        try {
            final DocumentBuilder documentB = documentBF.newDocumentBuilder();
            final InputStream inputStream = getSoapInputStream(PROVINCE_CODE_URL); // 具体webService相关
            document = documentB.parse(inputStream);
            final NodeList nodeList = document.getElementsByTagName("string"); // 具体webService相关
            final int len = nodeList.getLength();
            for (int i = 0; i < len; i++) {
                final Node n = nodeList.item(i);
                final String result = n.getFirstChild().getNodeValue();
                final String[] address = result.split(",");
                final String pName = address[0];
                final String pCode = address[1];
                if (pName.equalsIgnoreCase(provinceName)) {
                    provinceCode = Integer.parseInt(pCode);
                }
            }
            inputStream.close();
        } catch (final DOMException e) {
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return provinceCode;
    }

    public static int getCityCode(final int provinceCode, final String cityName) {
        Document doc;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        int cityCode = 0;
        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final InputStream is = getSoapInputStream(CITY_CODE_URL + provinceCode); // 具体webService相关
            doc = db.parse(is);
            final NodeList nl = doc.getElementsByTagName("string"); // 具体webService相关
            final int len = nl.getLength();
            for (int i = 0; i < len; i++) {
                final Node n = nl.item(i);
                final String result = n.getFirstChild().getNodeValue();
                final String[] address = result.split(",");
                final String cName = address[0];
                final String cCode = address[1];
                if (cName.equalsIgnoreCase(cityName)) {
                    cityCode = Integer.parseInt(cCode);
                }
            }
            is.close();
        } catch (final DOMException e) {
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return cityCode;
    }

    public static InputStream getSoapInputStream(final String url) {
        InputStream inputStream = null;
        try {
            final URL urlObj = new URL(url);
            final URLConnection urlConn = urlObj.openConnection();
            urlConn.setRequestProperty("Host", SERVICES_HOST); // 具体webService相关
            urlConn.connect();
            inputStream = urlConn.getInputStream();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static List<String> getWeather(final int cityCode) {
        final List<String> weatherList = new ArrayList<String>();
        Document document;
        final DocumentBuilderFactory documentBF = DocumentBuilderFactory.newInstance();
        documentBF.setNamespaceAware(true);
        try {
            final DocumentBuilder documentB = documentBF.newDocumentBuilder();
            final InputStream inputStream = getSoapInputStream(WEATHER_QUERY_URL + cityCode);
            document = documentB.parse(inputStream);
            final NodeList nl = document.getElementsByTagName("string");

            for (int i = 0; i < nl.getLength(); i++) {
                final Node n = nl.item(i);
                final String weather = n.getFirstChild().getNodeValue();
                weatherList.add(weather);
            }

            inputStream.close();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (final DOMException e) {
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return weatherList;
    }
}
