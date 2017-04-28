<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="pagedata"%>
<style>
.fy { position:relative; height:20px; margin-bottom:10px; }
.fy p { background:url(../images/fy_bg.gif) no-repeat; /*width:175px;*/ width:230px; height:20px; text-align:center; position:absolute; top:0; right:0; font-size: 11px; line-height: 20px; }
a.fy_left { position:absolute; left:0; top:0; height:20px; width:18px; display:inline-block; margin-left: 18px; }
a.fy_right { position:absolute; right:0; top:0; height:20px; width:18px; display:inline-block; margin-right: 20px; }
a.fy_left0 { position:absolute; left:0; top:0; height:20px; width:18px; display:inline-block; }
a.fy_right0 { position:absolute; right:0; top:0; height:20px; width:18px; display:inline-block; }

.input_fy_bg {
	border:1px solid #d2d2d0; 
	color:#666666;
	vertical-align:middle;
	height:14px;
	width:20px; 
	margin:0px 2px 2px auto;
	background-color:#ffffff;
	font-size:11px;
	}
</style>
<script type="text/javascript">
//分页开始
var PageObject={};

PageObject.asc = "${pageBean.asc}";

/**
 * 搜索事件
 */
PageObject.search=function(e){
	$("#currentPage").attr("value",1);	
	PageObject.callSearch();
};

/**
 * 转到第一页
 */
PageObject.firstPage=function(e){
	if (PageObject.pageSize==0) {
		return;
	}
	$("#currentPage").attr("value",1);	
	PageObject.callSearch();
};

/**
 * 转到前一页
 */
PageObject.prePage=function(e){
	if (PageObject.pageSize==0) return;
	var currentPage = parseInt($("#currentPage").attr("value"))-1;
	if (currentPage<1){
		return;
	}
	$("#currentPage").attr("value",currentPage);			
	PageObject.callSearch();
};

/**
 * 转到下一页
 */
PageObject.nextPage=function(e){
	if (PageObject.pageSize==0) return;
	var currentPage = parseInt($("#currentPage").val())+1;
	if (currentPage>PageObject.pageSize){
		return;
	}
	$("#currentPage").attr("value",currentPage);	
	PageObject.callSearch();
};

/**
 * 转到最后一页
 */
PageObject.lastPage=function(e){
	if (PageObject.pageSize==0) return;
	$("#currentPage").attr("value",PageObject.pageSize);	
	PageObject.callSearch();
};

/**
 * 排序事件
 */
PageObject.sort=function(sortColumnName){
	$("#currentPage").attr("value",1);
	$("#sortColumnName").attr("value",sortColumnName);
	$("#asc").attr("value",PageObject.asc=="true"?"false":"true");	
	PageObject.callSearch();
};

/*
 * 显示排序图标
 */
PageObject.setSortIcon=function(sortColumnName){
	 $("#"+sortColumnName+"Col").html(""); 
	var sortImage;
	if (PageObject.asc) {
		sortImage='<em><img src="images/icon_9.gif" /><img src="images/icon_12.gif" /></em>';
	}else{
		sortImage='<em><img src="images/icon_11.gif" /><img src="images/icon_10.gif" /></em>';
	}
    $("#"+sortColumnName+"Col").html(sortImage); 
};

/**
 *转到指定页
 */
PageObject.goToPage=function(e){
	if(PageObject.pageSize==0) return;
	var num = $(e).parent().find("#goToPage").val();
	if(num=='' || (isNaN(num)) || parseInt(num)<=0 || parseInt(num)>parseInt(PageObject.pageSize)){
		$("#goToPage").val('');
		return false;
	}else{
		$("#currentPage").attr("value",num);
		PageObject.callSearch();
	}	
}

/**
 * 查询
 */
PageObject.callSearch=function(e){
	$('#pageForm').submit();
};
</script>
