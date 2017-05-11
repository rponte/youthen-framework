/**
 * youthen Sisqp Sytem 2011
 */
package com.youthen.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @copyright youthen
 * @author LiXin
 * @date 2011-6-2
 */
@XStreamAlias("root")
public class Map2XMLUtils {

    private List<Item> items;

    @XStreamAlias("field")
    private static class Item {

        /**
         * 构造函数
         * 
         * @param id
         * @param value
         */
        @SuppressWarnings("hiding")
        public Item(final String id, final Object value) {
            this.id = id;
            if (value instanceof String) {
                this.value = (String) value;
            } else {
                this.list = value;
            }
        }

        /**
         * 名称
         */
        @XStreamAsAttribute()
        private final String id;

        /**
         * 名称
         */
        @XStreamAsAttribute()
        private String value;

        /**
         * 内容
         */
        private Object list;
    }

    /**
     * 把map类object转成xml
     * 
     * @param requestMap
     * @return XML
     */
    public static String map2XML(final Map<String, Object> requestMap) {
        if (CollectionUtils.isEmpty(requestMap)) {
            return null;
        }
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final List<String> keys = new ArrayList<String>();
        for (final String key : requestMap.keySet()) {
            keys.add(key);
            final Object value = requestMap.get(key);
            if (value != null && value instanceof String[]) {
                final String[] toValue = (String[]) value;
                if (toValue.length == 1) {
                    toValue[0] = convertSpecialChar(toValue[0]);
                    map.put(key, toValue[0]);
                } else {
                    for (int i = 0; i < toValue.length; i++) {
                        toValue[i] = convertSpecialChar(toValue[i]);
                    }
                    map.put(key, toValue);
                }
            } else {
                if (value != null && value instanceof String) {
                    final String tmpStr = convertSpecialChar(value.toString());
                    map.put(key, tmpStr);
                } else {
                    map.put(key, value);
                }
            }
        }
        final Map2XMLUtils content = new Map2XMLUtils();
        content.items = new ArrayList<Item>();
        Collections.sort(keys);

        for (final String key : keys) {
            content.items.add(new Item(key, map.get(key)));
        }
        final XStream xstream = new XStream();
        xstream.processAnnotations(Map2XMLUtils.class);
        return xstream.toXML(content);
    }

    /**
     * 把xml转成Content类object
     * 
     * @param xml
     * @return Map
     */
    @SuppressWarnings("synthetic-access")
    public static Map<String, Object> xml2Map(final String xml) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }

        final XStream xstream = new XStream();
        xstream.processAnnotations(Map2XMLUtils.class);
        final Map2XMLUtils content = (Map2XMLUtils) xstream.fromXML(xml);

        final Map<String, Object> finalResult = new HashMap<String, Object>();
        for (final Item item : content.items) {
            if (item.value != null) {
                finalResult.put(item.id, item.value);
            } else if (item.list != null) {
                finalResult.put(item.id, item.list);
            } else {
                finalResult.put(item.id, "");
            }
        }
        return finalResult;
    }

    /**
     * 转换特殊字符：解决页面特殊字符的Bug
     * 
     * @param str
     * @return str
     */
    public static String convertSpecialChar(String str) {
        if (str != null) {
            // 双引号
            if (str.indexOf("\"") > -1) {
                str = str.replaceAll("\"", "&quot;");
            }
            // 大于号
            if (str.indexOf(">") > -1) {
                str = str.replaceAll(">", "&gt;");
            }
            // 小于号
            if (str.indexOf("<") > -1) {
                str = str.replaceAll("<", "&lt;");
            }
        }
        return str;
    }

    /**
     * test
     * 
     * @param arg
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static void main(final String[] arg) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("S_01_03_CREATE_NAME", new String[] {"张三"});
        map.put("S_01_01_CREATE_DATE", new Date());
        map.put("S_01_02_OCCURRED_AREAS", new String[] {"A1", "A2", "A3"});

        final String xml = ""
                + "<root>"
                + "<items>"
                + "<field id=\"S_CHILD_01_03_RELATED_PERSON_USERID\">"
                + "<list class=\"string-array\">"
                + "<string>area1</string>"
                + "<string>area2</string>"
                + "<string>area3</string>"
                + "</list>"
                + "</field>"
                + "<field id=\"S_CHILD_01_05_EQUIPMENT_ID\">"
                + "<list class=\"string-array\">"
                + "<string>1011-001</string>"
                + "<string>1012-001</string>"
                + "</list>"
                + "</field>"
                + "</items>"
                + "</root>";

        final Map obj = xml2Map(xml);

        final String[] userIds = (String[]) obj.get("S_CHILD_01_03_RELATED_PERSON_USERID");
        for (final String id : userIds) {
            // System.out.println(id);
        }

    }

}
