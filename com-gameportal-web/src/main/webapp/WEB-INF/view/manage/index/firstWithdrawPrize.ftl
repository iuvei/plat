<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--8-88元免费彩金领取</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" >
    	$(function(){
			// 申请首提彩金
			$("#act1").click(function(){
				$(this).val("正在申请礼金...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/activityReward.do",
		            success: function (result){ 
		            	$("#act1").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.success){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act1").val("马上领取");
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
    <div class="title size-bigger">8-88元免费彩金领取</div>
    <div class="main">
        <div class="user-layer ui-mt30">
            <div class="user-box ui-alignCenter ui-radius">
                <img src="/static/images/user/promotion/st.jpg">
                <h2 class="size-big ui-song" style="font-weight:bold;">认证手机、邮箱即送<font class="ui-yahei">8-88</font>元</h2>
                
                <#if withdrawalFlag ==0>
	                <input type="button" id="act1" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="马上领取" />
                <#else>
	                <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition ui-disabled" value="已领取" disabled="disabled"/>
                </#if>
                <#-- <a class="color-blue color-brownHover ui-song" href="javascript:;"><b>查看详情</b></a> -->
            </div>
        </div>
    </div>

</body>

</html>