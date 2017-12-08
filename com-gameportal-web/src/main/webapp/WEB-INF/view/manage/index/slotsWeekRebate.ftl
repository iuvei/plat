<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--积分兑换</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" >
    	$(function(){
			// 积分兑换
			$("#act2").click(function(){
				$(this).val("正在兑换积分...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/getSlotsWeekRebate.do",
		            success: function (result){ 
		            	$("#act2").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.success){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act2").val("我要兑换");
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
    <div class="title size-bigger">钱宝积分兑换</div>
    <div class="main">
        <div class="user-layer ui-mt30">
            <div class="user-box ui-alignCenter ui-radius">
                <img src="/static/images/user/promotion/20170610_4.jpg">
                <h2 class="size-big ui-song" style="font-weight:bold;">投注送积分，积分换现金</h2>
                <div class="ui-ddl fl size-normal ui-radius" style="border:0;width:250px;margin-left:45px;">总积分：<font color="red">&nbsp;${integral}&nbsp;</font>分</div>
	            <input type="button" id="act2" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="我要兑换" />
            </div>
        </div>
    </div>

</body>

</html>