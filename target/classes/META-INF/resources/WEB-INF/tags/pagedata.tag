<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/paging.jsp"%>
<%@ attribute name="pagedata" required="true" rtexprvalue="true" type="com.youthen.framework.common.PageBean"%>
<%@ attribute name="pageBeanName" rtexprvalue="true" type="java.lang.String"%>
<c:set var="pageBeanName" value="${empty pageBeanName?'pageBean':pageBeanName}"/>
<script>
PageObject.pageSize = '${pagedata.pageCount}';
PageObject.asc = '${pagedata.asc}';
PageObject.setSortIcon("${pagedata.sortColumnName}");
</script>
<input type="hidden" name="${pageBeanName}.sortColumnName" id="sortColumnName" value="${pagedata.sortColumnName}">
<input type="hidden" name="${pageBeanName}.asc" id="asc" value="${pagedata.asc}">
<input type="hidden" name="${pageBeanName}.current" id="currentPage" value="${pagedata.current}">
<input type="hidden" name="${pageBeanName}.pageSize" id="currentPage" value="${pagedata.pageSize}">
<div class="fy">
   	<p>
   		<fmt:message key="common.page.info">
    		<fmt:param value="${pagedata.current}"/>
    		<fmt:param value="${pagedata.pageCount}"/>
    		<fmt:param value="${pagedata.recordCount}"/>
   		</fmt:message>
    	<a href="###" class="fy_left0" onClick="PageObject.firstPage();"></a>
    	<a href="###" class="fy_left" onClick="PageObject.prePage();"></a>
    	<input type="text" name="goToPage" id="goToPage" value="${pagedata.current}" class="input_fy_bg"/>
		<a href="###" onclick="PageObject.goToPage(this)">Go</a>
    	<a href="###" class="fy_right" onClick="PageObject.nextPage();"></a>
    	<a href="###" class="fy_right0" onClick="PageObject.lastPage();"></a>
   	</p>
</div>