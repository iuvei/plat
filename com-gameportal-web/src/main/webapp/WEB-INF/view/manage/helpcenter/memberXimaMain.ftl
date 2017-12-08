<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 自助洗码数据</title>
${meta}
<link href="${zy_path}css/main.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/menu.css" rel="stylesheet" type="text/css" />
<link href="${zy_path}css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" language="javascript" src="${zy_path}js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/menu.js"></script>
<script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
<script src="${zy_path}js/My97DatePicker/WdatePicker.js"></script>
<script src="${zy_path}js/base.js"></script>
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDialog.js"></script>
<style type="text/css">
	.hand{
		background-color: #f4f4f4;
		cursor: pointer;
	}
</style>
<script type="text/javascript" language="javascript">    
var diag = new Dialog();    
</script>
</head>
<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top" class="top">
	<!--头部-->	
	<#include "common/top.ftl">
	</td>
  </tr>
  <#include "common/bulletin.ftl" />
  <tr>
    <td height="100" align="center" valign="middle" bgcolor="#e4e4e4">
<!--导航菜单开始-->		
<#include "common/header.ftl">
<!--导航菜单结束-->	
	</td>
  </tr>
  
  <tr>
    <td align="center" valign="top" style="padding-top:15px; padding-bottom:15px;">
	
<!-- 报表查询开始 -->
<table width="1000" border="0" cellspacing="0" cellpadding="0">
  <tr style="background-color:#CCCCCC;">
    <td width="197" align="center" valign="top" style="background-color:#CCCCCC; border-right:solid 3px #b18702;">
	<#include "manage/common/menu.ftl">
	</td>
    <td width="800" align="center" valign="top">
	
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center" valign="top" style="background-color:#CCCCCC;">
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="20%" class="regtitle">洗码记录</a></td>
              <td width="80%" style="border-top:solid 3px #ada9aa;font-size:16px;color:red;">（点击表中每行数据可以查询洗码明细记录）</td>
            </tr>
        </table>
		
		</td>
      </tr>
      <tr>
        <td height="432" align="center" valign="top" style="padding:10px; background:url(${zy_path}images/line.jpg) repeat-x top; background-color:#CCCCCC;">
        <form id="ximaMainForm" name="ximaMainForm" action="${action_path}manage/helpxima/queryMemberXimaMain.do" method="post">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:15px;">
          <tr>
         	<td valign="middle"><button type="button" id="AGXima_btn" onclick="AGXimaFun()" class="submitbtn2" style="border: 0; width:100px;"><b>真人洗码</b></button></td>
         	<td valign="middle"><button type="button" id="PTXima_btn" onclick="PTXimaFun()" class="submitbtn2" style="border: 0; width:100px;"><b>老虎机洗码</b></button></td>
            <td valign="middle" class="regname">选择日期：
                <input name="startDate" type="text" class="reginput" id="startDate" value="${startDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,minDate:'${startDate}',maxDate:'${startDate}'})" size="20" maxlength="30" />
              &nbsp;—&nbsp;
              <input name="endDate" type="text" class="reginput" id="endDate" value="${endDate}" size="20" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,minDate:'${endDate}',maxDate:'${endDate}'})" maxlength="30" />
            </td>
            <td valign="middle">
            <table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td valign="middle"><button type="button" id="memberXima_btn" class="submitbtn2" style="border: 0;"><b>查询</b></button></td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </form>
          <table width="100%" border="0" align="center" cellpadding="5" cellspacing="1">
            <tr>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>状态</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>游戏平台</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>会员账号</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>会员姓名</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>反水总金额(元)</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>洗码开始时间</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>洗码结束时间</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>更新时间</strong></td>
              </tr>
             <#if memberXimaList??>
	             <#list memberXimaList as ximamain>
		            <tr onmouseover="style.backgroundColor='#f4f4f4';style.cursor='pointer'" onclick="memberXimaDetailFun();"  onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
		              <td height="30" align="center" valign="middle">
		               <#if ximamain.locked == 0>
		              	<span style="color:red;">未入账</span>
		               </#if>
		               <#if ximamain.locked == 1>
		               <span style="color:green;">已入账</span>
		               </#if>
		                <#if ximamain.locked == 2>
		                <span style="color:gray;">审核失败</span>
		               </#if>
		              </td>
		              <td height="30" align="center" valign="middle">${ximamain.gpid}</td>
		              <td height="30" align="center" valign="middle">${ximamain.account}</td>
		              <td height="30" align="center" valign="middle">${ximamain.name}</td>
		              <td height="30" align="center" valign="middle">${ximamain.total}</td>
		              <td height="30" align="center" valign="middle">${ximamain.ymdstart?date}</td>
		              <td height="30" align="center" valign="middle">${ximamain.ymdend?date}</td>
		              <td height="30" align="center" valign="middle">${ximamain.updatetime?date}</td>
		            </tr>
	            </#list>
            <#else>
            <tr onmouseover="style.backgroundColor='#f4f4f4'" onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
          		<td height="30" align="center" valign="middle" colspan="8"><font color="red">您还没有洗码记录</font></td>
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
<!-- 报表查询结束 --></td>
  </tr>
  <tr>
    <td align="center" valign="top">
<!--底部-->
	<#include "common/footer.ftl">
	
	</td>
  </tr>
</table>
<#include "common/leftpos.ftl">
<script type="text/javascript">
!function($){
	$(function(){
		memberSetCurr();
		$("#memberXima_btn").click(function(){
			$("#memberXima_btn").attr('disabled',true);
			ximaMainForm.submit();
		});
	});
}(jQuery);
  
function fenye(pageNo){
	var startDate = jQuery("#startDate").val();
	var endDate = jQuery("#endDate").val();
	var url = "${action_path}manage/helpxima/queryMemberXimaMain.do?startDate="+startDate+"&endDate="+endDate+"&pageNo="+pageNo;
	window.location.href = url;
}

function memberXimaDetailFun(){
 	 diag.Drag=true;
	 diag.Title ="会员洗码明细记录";
	 diag.URL = "${action_path}manage/helpxima/queryMemberXimaMainDetail.do";
	 diag.Width = 1000;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
	 var html = $("#_DialogDiv_0").html();
     html = html.replace(new RegExp("plugins","gm"),'/plugins');
     $("#_DialogDiv_0").html(html);
}

function PTXimaFun(){
 	 diag.Drag=true;
	 diag.Title ="老虎机结算";
	 diag.URL = "${action_path}manage/helpxima/queryPTMemberXima.do";
	 diag.Width = 1000;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
	 var html = $("#_DialogDiv_0").html();
     html = html.replace(new RegExp("plugins","gm"),'/plugins');
     $("#_DialogDiv_0").html(html);
}

function AGXimaFun(){
 	 diag.Drag=true;
	 diag.Title ="真人结算";
	 diag.URL = "${action_path}manage/helpxima/queryAGMemberXima.do";
	 diag.Width = 1000;
	 diag.Height = 500;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
	 var html = $("#_DialogDiv_0").html();
     html = html.replace(new RegExp("plugins","gm"),'/plugins');
     $("#_DialogDiv_0").html(html);
}
</script>
</body>
</html>