<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--钱宝抢红包</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" >
    	$(function(){
			// 申请存款红包
			$("#act2").click(function(){
				$(this).val("正在申请红包...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/getRedReward.do",
		            success: function (result){ 
		            	$("#act2").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.code =='1'){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act2").val("马上领取");
		            	}
		            }  
			   	});
			});
			
			// 申请存款红包
			$("#act3").click(function(){
				$(this).val("正在刷新...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/refreshRedRewardNumber.do",
		            success: function (result){ 
		            	$("#act3").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.code =='1'){
		            		$("#number").text(result.number);
		            	}else{
		            		$.msg(result.msg,3);
		            	}
		            	$("#act3").val("刷新名额");
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
    <div class="title size-bigger">钱宝抢红包</div>
    <div class="main">
        <div class="user-layer ui-mt30" style="float:left;">
            <div class="user-box ui-alignCenter ui-radius">
                <img src="/static/images/user/promotion/hongbao.jpg">
                <h2 class="size-big ui-song" style="font-weight:bold;">随机大红包等你拿</h2>
                <#if !isStart>
                	<input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition ui-disabled" value="马上开始" disabled="disabled"/>
                <#else>
	                <#if isClose>
	                	 <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition ui-disabled" value="已结束" disabled="disabled"/>
	                <#elseif isFull>
	                	 <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition ui-disabled" value="名额已满" disabled="disabled"/>
	                <#else>
		                <#if isPanlic>
			                <input type="button" id="act2" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="马上领取" />
		                <#else>
			                <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition ui-disabled" value="已领取" disabled="disabled"/>
		                </#if>
	                </#if>
                </#if>
            </div>
        </div>
        <div class="user-layer ui-mt30" style="float:left;width:500px;">
       		 <h2 class="size-big ui-song" style="font-weight:bold;">抢红包规则:</h2>
			 <h4 class="size-big ui-song"><font color="#FF0000"><b>火热预告中：3月5日起，28元红包天天送即将来袭！</b></font></h4>
       		 <h4 class="size-big ui-song">1、5天内存款&ge;500元即可获得一次抢红包的机会。</h4>
       		 <h4 class="size-big ui-song">2、红包金额固定为28元，无限数量。</h4>
			 <h4 class="size-big ui-song">3、每人每日限领1个红包。</h4>
			 <h4 class="size-big ui-song">4、红包只限在MG/SA平台进行游戏。</h4>
       		 <h4 class="size-big ui-song">5、中心钱包余额和第三方各平台游戏总余额不能超过2元。</h4>
       		 <h4 class="size-big ui-song">6、红包不与返水共享。</h4>
       		 <h4 class="size-big ui-song">7、红包最高出款168元。</h4>
			 <h4 class="size-big ui-song">8、本活动解释权归钱宝娱乐所有。</h4><br/>
	     	 <#if !isStart>
	       		 <h4 class="size-big ui-song">下轮开放时间：<font color="red">${startTime!"敬请期待！"}</font></h4>
       		 <#else>
       		 	 <#if isPanlic>
	       		 	<h4 class="size-big ui-song">本轮剩余名额：<span style="color:red" id="number">${number}</span>&nbsp;个</h4>
	       		 	<input type="button" id="act3" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" value="刷新名额" style="width: 180px;margin: 7px auto;"/>
	       		 </#if>
       		 </#if>
        </div>
    </div>

</body>

</html>