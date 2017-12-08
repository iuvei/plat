<!DOCTYPE html>
<html>
<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>钱宝娱乐--帐户中心--每日签到</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css"/>
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css"/>
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <script type="text/javascript" >
    	$(function(){
			// 申请注册彩金
			$("#act5").click(function(){
				$(this).val("正在申请签到...");
				if($("#act5").hasClass("_click")){
				 	return;
				}
				$("#act5").addClass("_click");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}getSignReward.do",
		            success: function (result){ 
		            	$("#act5").removeClass("_click");
		            	if(result.success){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act5").val("我要签到");
		            	}
		            }  
			   	});
			});
			
			function success_callback(){
				window.location.reload();
			}
		});
    </script>
</head>

<body>
<div class="title size-bigger">钱宝今日签到</div>
<div class="main">
    <div class="user-layer ui-mt30">
        <div class="user-box ui-alignCenter ui-radius">
            <img src="/static/images/user/promotion/20170608_4.jpg">
            <h2 class="size-big ui-song" style="font-weight:bold;">存款签到 彩金送不停</h2>
            <#if isSign>
            	<input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="已签到"  disabled="disabled"/>
            <#else>
            	<input type="button" id="act5" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="我要签到"/>
            </#if>
        </div>
    </div>
</div>

</body>

</html>