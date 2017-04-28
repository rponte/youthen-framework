package com.youthen.framework.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 * 分页封装类
 * 
 * @author cxh
 * @param <D>
 */
public class PageBean<D> {

    private final Map<String, String> operatorMap = new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {
            put("eq", "=");
            put("like", "like");
            put("date1", ">=");
            put("date2", "<=");

        }
    };

    public PageBean() {
    }

    public PageBean(final int aCurrent, final int aPageSize) {
        this.current = new Integer(aCurrent);
        this.pageSize = new Integer(aPageSize);
    }

    // 传递的参数或是配置的参数
    private Integer current; // 当前页
    private Integer pageSize; // 每页显示多少条记录
    private Integer recordCount; // 总记录数

    // 查询数据库
    private List<D> recordList; // 本页的数据列表

    // 查询数据库
    private List objList; // 本页的数据列表

    // 计算
    private Integer pageCount; // 总页数

    // 升序或降序字段名
    private String sortColumnName;

    // 升序或降序字段名
    private String fromHql;

    // 升序或降序
    private String asc;

    private String queryCode;

    // 查询条件
    private Map<String, String> whereMap = new HashMap<String, String>();

    // 查询条件
    private String whereHql;

    public List<D> getRecordList() {
        return this.recordList;
    }

    public void setRecordList(final List<D> aRecordList) {
        this.recordList = aRecordList;
    }

    public Integer getPageCount() {
        if (this.pageCount == null || this.pageCount.intValue() < 1) {
            final int count = this.getRecordCount().intValue();
            final int size = this.getPageSize().intValue();
            final int elseNum = count % size;
            if (count == 0) return new Integer(1);
            return new Integer((count - elseNum) / size + (elseNum == 0 ? 0 : 1));
        }
        return this.pageCount;
    }

    public Integer getPageSize() {
        return (this.pageSize == null || this.pageSize.intValue() < 1) ? new Integer(10) : this.pageSize;
    }

    public void setPageSize(final Integer aPageSize) {
        this.pageSize = aPageSize;
    }

    public Integer getRecordCount() {
        return (this.recordCount == null || this.recordCount.intValue() < 0) ? new Integer(0) : this.recordCount;
    }

    public void setRecordCount(final Integer aRecordCount) {
        this.recordCount = aRecordCount;
    }

    public Integer getCurrent() {
        return (this.current == null || this.current.intValue() < 1) ? new Integer(1) : this.current;
    }

    public void setCurrent(final Integer aCurrent) {
        this.current = aCurrent;
    }

    public String getAsc() {
        return this.asc;
    }

    public void setAsc(final String aAsc) {
        this.asc = aAsc;
    }

    public Map<String, String> getWhereMap() {
        return this.whereMap;
    }

    public void setWhereMap(final Map<String, String> aWhereMap) {
        this.whereMap = aWhereMap;
    }

    @SuppressWarnings("unchecked")
    public void setWhereMap(final HttpServletRequest request) {
        final Map requestMap = request.getParameterMap();
        final Iterator<String> it = requestMap.keySet().iterator();
        Iterator<String> it2 = this.operatorMap.keySet().iterator();
        while (it.hasNext()) {
            final String key = it.next();
            while (it2.hasNext()) {
                final String key2 = it2.next();
                if (key.startsWith(key2)) {
                    final String valStr = request.getParameter(key);
                    if (StringUtils.isNotEmpty(valStr)) {
                        this.whereMap.put(key, valStr);
                    }
                    break;
                }
            }
            it2 = this.operatorMap.keySet().iterator();
        }
    }

    public String getWhereHqlByWhereMap() {
        final StringBuffer tmpWhereSql = new StringBuffer("1=1");
        for (final String key : this.whereMap.keySet()) {
            final String[] keyArray = key.split("_");
            if (keyArray.length > 1 && this.getOperatorMap().get(keyArray[0]) != null
                    && StringUtils.isNotEmpty(this.whereMap.get(key))) {
                tmpWhereSql.append(" and tmpObj." + key.replace(keyArray[0] + "_", "")
                        + " " + this.getOperatorMap().get(keyArray[0]) + ":" + key.replaceAll("\\.", ""));
            }
        }
        return tmpWhereSql.toString();
    }

    public String getWhereSqlByWhereMap() {
        final StringBuffer tmpWhereSql = new StringBuffer("1=1");
        for (final String key : this.whereMap.keySet()) {
            final String[] keyArray = key.split("_");
            if (keyArray.length > 1 && this.getOperatorMap().get(keyArray[0]) != null
                    && StringUtils.isNotEmpty(this.whereMap.get(key))) {
                tmpWhereSql.append(" and " + key.replace(keyArray[0] + "_", "")
                        + " " + this.getOperatorMap().get(keyArray[0]) + ":" + key.replaceAll("\\.", ""));
            }
        }
        return tmpWhereSql.toString();
    }

    public String getSortColumnName() {
        return this.sortColumnName;
    }

    public void setSortColumnName(final String aSortColumnName) {
        this.sortColumnName = aSortColumnName;
    }

    public Map<String, String> getOperatorMap() {
        return this.operatorMap;
    }

    public List getObjList() {
        return this.objList;
    }

    public void setObjList(final List aObjList) {
        this.objList = aObjList;
    }

    public String getWhereHql() {
        return this.whereHql == null ? "" : this.whereHql;
    }

    public void setWhereHql(final String aWhereHql) {
        this.whereHql = aWhereHql;
    }

    public String getFromHql() {
        return this.fromHql;
    }

    public void setFromHql(final String aFromHql) {
        this.fromHql = aFromHql;
    }

    public String getQueryCode() {
        return this.queryCode;
    }

    public void setQueryCode(final String aQueryCode) {
        this.queryCode = aQueryCode;
    }

}
