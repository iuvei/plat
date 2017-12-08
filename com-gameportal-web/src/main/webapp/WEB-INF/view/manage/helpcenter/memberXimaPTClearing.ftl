<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 老虎机洗码</title>
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
        <form id="ptClearingFrm" name="ptClearingFrm" action="${action_path}manage/helpxima/queryPTMemberXima.do" method="post">
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:15px;">
          <tr>
            <td valign="middle" class="regname">选择日期：
                <input name="startDate" type="text" class="reginput" id="startDate" value="${startDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'2015-12-20'})" size="20" maxlength="30" />
            </td>
            <td valign="middle" class="regname">
            	<button type="button" id="ptclearing_save" class="submitbtn2" style="border: 0;" onclick="ximaFun()"><b>洗码</b></button>
            </td>
            <td valign="middle">
            <table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td valign="middle"><button type="button" id="ptclearing_btn" class="submitbtn2" style="border: 0;"><b>查询</b></button></td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </form>
        <table width="100%" border="0" align="center" cellpadding="5" cellspacing="1">
            <tr>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong><input type="checkbox" name="checkVals" id="checkVals" onclick="allCheck(this);"/></strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>游戏平台</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>游戏名称</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>会员账号</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>会员姓名</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>注单数量</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>投注金额</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>有效投注额</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>洗码值#比例</strong></td>
              <td height="40" align="center" valign="middle" class="tabletitle"><strong>派彩金额</strong></td>
              <#--<td height="40" align="center" valign="middle" class="tabletitle"><strong>结算状态</strong></td>-->
              </tr>
             <#if betLogTotal??>
	             <#list betLogTotal as bet>
		            <tr onmouseover="style.backgroundColor='#f4f4f4'" onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
		              <td height="30" align="center" valign="middle"><input type="checkbox" name="checkVal" value="${bet.clearingstatus}"/></td>
		              <td height="30" align="center" valign="middle">${bet.platformcode}</td>
		              <td height="30" align="center" valign="middle">${bet.gamename}</td>
		              <td height="30" align="center" valign="middle">${bet.account}</td>
		              <td height="30" align="center" valign="middle">${bet.uname}</td>
		              <td height="30" align="center" valign="middle">${bet.betTotel}</td>
		              <td height="30" align="center" valign="middle">${bet.betAmountTotal}</td>
		              <td height="30" align="center" valign="middle">${bet.validBetAmountTotal}</td>
		              <td height="30" align="center" valign="middle">${bet.ximaAmount}</td>
		              <td height="30" align="center" valign="middle">${bet.profitamountTotal}</td>
		              <#--<td height="30" align="center" valign="middle">
		               <#if bet.clearingstatus == 0>
		              	<span style="color:red;">未结算</span>
		               <#else>
		               	<span style="color:green;">已结算</span>
		               </#if>
		              </td>-->
		            </tr>
	            </#list>
            <#else>
            <tr onmouseover="style.backgroundColor='#f4f4f4'" onmouseout="style.backgroundColor=''" bgcolor="#e4e4e4">
          		<td height="30" align="center" valign="middle" colspan="10"><font color="red">您还没有可洗码数据</font></td>
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
		$("#ptclearing_btn").click(function(){
			$("#ptclearing_btn").attr('disabled',true);
			ptClearingFrm.submit();
		});
	});
}(jQuery);

function fenye(pageNo){
	var startDate = jQuery("#startDate").val();
	var url = "${action_path}manage/helpxima/queryPTMemberXima.do?startDate="+startDate+"&pageNo="+pageNo;
	window.location.href = url;
}

function allCheck(obj){
 	if(obj.checked){    
        $("input[name='checkVal']").attr("checked", true);   
    }else{    
        $("input[name='checkVal']").attr("checked", false); 
    } 
}

function ximaFun(){
	var itcIds = '8#PT';
	var clearingDate=$("#startDate").val();
	$.ajax({
			type : "POST",
			url : "${action_path}manage/helpxima/saveMemberXimaMain.do",
			dataType : "json",
			data:{
				gameplat : itcIds,
				startTime : clearingDate
			},
			async : true,
			success : function(data) {
				if(data.code=="1"){
					alert(data.msg);
					ptClearingFrm.submit();
				}else{
					alert(data.msg);
				}
			},
			error: function(){
				alert('网络异常，请稍后再试!');
			}
	});
}
</script>
</body>
</html>