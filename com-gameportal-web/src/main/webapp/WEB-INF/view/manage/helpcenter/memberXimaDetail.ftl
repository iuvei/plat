<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 自助洗码明细</title>
${meta}
<link href="${zy_path}css/main.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/menu.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="${zy_path}js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/menu.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
<script src="${zy_path}js/My97DatePicker/WdatePicker.js"></script>
<script src="${zy_path}js/base.js"></script>
</head>
<body>
<!-- 报表查询开始 -->
<table width="1000" border="0" cellspacing="0" cellpadding="0">
  <tr>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="432" align="center" valign="top" style="padding:10px; background:url(${zy_path}images/line.jpg) repeat-x top; background-color:#CCCCCC;">
          <table width="100%" border="0" align="center" cellpadding="5" cellspacing="1">
            <tr>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>操作类型</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>操作时间</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>游戏平台</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>反水金额(元)</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>洗码开始日期</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>洗码结束时间</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>参数日志</strong></td>
              </tr>
             <#if memberXimaDetail??>
	             <#list memberXimaDetail as ximadetail>
		            <tr onmouseover="style.backgroundColor='#f4f4f4'" onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
		              <td height="30" align="center" valign="middle">
		               <#if ximadetail.opttype == 0>
		              	<span style="color:red;">自助洗码</span>
		               </#if>
		               <#if ximadetail.opttype == 1>
		               <span style="color:green;">洗码清零</span>
		               </#if>
		                <#if ximadetail.opttype == 2>
		                <span style="color:gray;">强制洗码</span>
		               </#if>
		              </td>
		              <td height="30" align="center" valign="middle">${ximadetail.opttime?date}</td>
		              <td height="30" align="center" valign="middle">${ximadetail.gpname}</td>
		              <td height="30" align="center" valign="middle">${ximadetail.amount}</td>
		              <td height="30" align="center" valign="middle">${ximadetail.ymdstart?date}</td>
		              <td height="30" align="center" valign="middle">${ximadetail.ymdend?date}</td>
		              <td height="30" align="center" valign="middle">${ximadetail.paramlog}</td>
		            </tr>
	            </#list>
            <#else>
            <tr onmouseover="style.backgroundColor='#f4f4f4'" onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
          		<td height="30" align="center" valign="middle" colspan="7"><font color="red">您还没有洗码明细记录</font></td>
          	</tr>
          </#if>
          </table>
          <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:15px;">
            <tr>
              <td align="center" valign="middle" class="pagetext">
				共有【${total}】条记录    共${pageCount}页
                	<#if pageCount gt 1>
                		<#if pageNo gt 1 >
                			<a href="javascript:fenye(${pageNo}-1)" title="上一页">上一页</a>
                		</#if>
                		<#if pageNo lt pageCount >
                			<a href="javascript:fenye(${pageNo}+1)" title="下一页">下一页</a>
                		</#if>
                	</#if>
			</td>
            </tr>
          </table></td>
      </tr>
    </table>
	
	</td>
  </tr>
</table>
<script type="text/javascript">
!function($){
	$(function(){
		memberSetCurr();
	});
}(jQuery);

function fenye(pageNo){
	var url = "${action_path}manage/helpxima/queryMemberXimaMainDetail.do?pageNo="+pageNo;
	window.location.href = url;
}
</script>
</body>
</html>