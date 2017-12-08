<!DOCTYPE html>
<html>
	<head>
	<#include "/common/config.ftl">
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	    <meta name="keywords" content="">
	    <meta name="description" content="">
	    <meta name="format-detection" content="telephone=yes">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<link rel="Bookmark" href="favicon.ico">
		<title>手机验证</title>
	</head>
	<link rel="stylesheet" type="text/css" href="/mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="/mstatic/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="/mstatic/css/li.css"/>
	<script src="/mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="/mstatic/js/jquery.cookie.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(function(){
			var hei=$(window).height();
			$(".swiper-slide").height(hei);
		})
	</script>
	<body>
		<!--后台头部-->
		<header class="ht_header">
			<div class="header_left f18">
				<span onclick="history.go(-1);">信息验证</span>
			</div>
			<div class="header_right f14">
				 ${verifyUser.account} | <span id="layout" onclick="window.location.href='/loginOutmp.html'">退出</span>
			</div>
		</header>
		<!--后台头部结束-->
		<div class="modal" style="margin-top:.15rem;z-index: 0;">
			<div class="input_box">
				<div class="overflow" style="height: 1rem;position: relative;">
					<#if verifyUser.emailvalid ==1>
						<input type="text" style="padding-left:35%;width:69%" value="${verifyUser.email}" id="email" readonly>
						<b class="icon_email f14" style="width:34%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电子邮箱：</b>
						<a class="f14 fr" href="javascript:void(0)" style="background-color: #417be7;position: absolute;top:0.05rem;right:0;height: .48rem;line-height: .2rem;width:21.5%;text-align:center ;">已认证</a>
					<#else>
						<input type="text" style="padding-left:35%;width:69%" value="${verifyUser.email}" id="email">
						<b class="icon_email f14" style="width:34%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电子邮箱：</b>
						<a class="f14 fr" href="javascript:void(0)" id="verifyEmail" style="background-color: #417be7;position: absolute;top:0.05rem;right:0;height: .48rem;line-height: .2rem;width:21.5%;text-align:center ;">立即验证</a>
					</#if>
				</div>
				<p class="f12">&nbsp;</p>
			</div>
			
			<div class="input_box">
				<div class="overflow" style="height: 1rem;position: relative;">
					<#if verifyUser.phonevalid ==1>
						<input type="text"  style="padding-left:32%;width:48%;" class="fl" value="${verifyUser.phone}" readonly>
						<b class="icon_tel2" style="width:30.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手机号码：</b>
						<a class="f14 fr" href="javascript:void(0)" style="background-color: #417be7;position: absolute;top:0.05rem;right:0;height: .48rem;line-height: .2rem;width:21.5%;text-align:center ;">已认证</a>
					<#else>
						<input type="text"  style="padding-left:32%;width:48%;" class="fl" value="${verifyUser.phone}" id="telphone" maxlength="11">
						<b class="icon_tel2" style="width:30.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手机号码：</b>
						<a class="f14 fr" href="javascript:void(0)" id="sendPhoneCode" style="background-color: #417be7;position: absolute;top:0.05rem;right:0;height: .48rem;line-height: .2rem;width:21.5%;text-align:center ;">发送验证</a>	
					</#if>
				</div>
				<p class="f12">&nbsp;</p>
			</div>
			<#if verifyUser.phonevalid ==0>
			<div class="input_box">
				<input type="text" style="padding-left:39%;width:69%" id="vcode"/>
				<b class="icon_yz2 f14"  style="width:38%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;输入验证码：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box" style="width:100%;margin-bottom:2rem;">
				<input type="button" value="提&nbsp交" id="verifyPhone"style="background-color: #417be7;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';" />
			</div>
			</#if>
		</div>
		<!--资料查询   结束-->
		<#include '/common/mfooter.ftl' />
	
	</body>
	<script src="/mstatic/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="/mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>

	<script>
		function refresh(){
			window.location.reload();
		}
		$(function(){
			$("#verifyEmail").click(function(){
				var email =$("#email");
				if(email.val() ==''){
					email.parent().next().html("* 请输入您的电子邮箱!");
					return;
				}else if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(email.val())){
					email.parent().next().html("* 邮箱输入格式错误!");
					return;
				}
				if($("#verifyEmail").hasClass("_click"))return;
				$("#verifyEmail").html('认证中');
				$("#verifyEmail").addClass('_click');
				$.ajax({
					type : "POST",
					url : "/mail/save.do",
					data : {
						email : email.val()
					},
					async : true,
					success : function(data) {
						data = eval('('+data+')');
						if (data.code == "1") {
					 		email.parent().next().html("* 恭喜，您的邮箱绑定成功!");
					 		setTimeout('refresh()',1000);  
						} else if (data.code == "0") {
							email.parent().next().html("* "+data.info);
							$("#verifyEmail").html('认证邮箱');
							$("#verifyEmail").removeClass('_click');
						}else {
							email.parent().next().html("* 网络异常，请稍后再试!");
							$("#verifyEmail").html('认证邮箱');
							$("#verifyEmail").removeClass('_click');
						}
					}
				})
			});
			
			$("#sendPhoneCode").click(function(){
				sendSms();
			});
			
			$("#verifyPhone").click(function(){
				verificationPhone();
			});
			
		});
	
	
var InterValTimer;
var totalCount = 120; //间隔函数，1秒执行  
var currentCount; //当前剩余秒数  
function sendSms(){
	if($("#sendPhoneCode").hasClass("ui-disabled")){
		return ;
	}
	var telphone=$("#telphone");
	if($("#telphone").val() ==''){
		$("#telphone").focus();
		telphone.parent().next().html('* 请输入您的手机号码!');
		return;
	}
	if(!/^1\d{10}$/.test($("#telphone").val())){
		$("#telphone").focus();
		telphone.parent().next().html('* 手机号码输入不正确!');
		return;
	}
	 $("#sendPhoneCode").addClass("ui-disabled");
	 $.ajax({  
            type: "POST", 
            dataType: "json",  
            url: "/manage/user/sendSmsCode.do",
            data: {phone: $("#telphone").val().trim()},  
            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
            success: function (data){ 
              if(data.code =='1'){
            	telphone.parent().next().html('* 短信验证码发送成功，请注意查收!');
              }else{
            	 $("#sendPhoneCode").removeClass("ui-disabled");
            	 telphone.parent().next().html('* '+data.info);
              }
            }  
    });  
}
function setRemainTime(){
	 if (currentCount == 0) {                  
        window.clearInterval(InterValTimer);//停止计时器  
        $("#sendPhoneCode").removeClass("ui-disabled");
        $("#sendPhoneCode").html("发送验证");
    }  
    else {  
    	currentCount--;
        $("#sendPhoneCode").html("请" + currentCount + "秒后再试！");
        $.cookie("currentCount",currentCount);
        $.cookie("InterValTimer",InterValTimer);
    } 
}
function verificationPhone(){
    var vcode =$("#vcode");
	if($("#verifyPhone").hasClass("ui-disabled")){
		return;
	}
	 $("#verifyPhone").addClass("ui-disabled");
	 if($("#vcode").val() !='' && $("#vcode").val() !=undefined){
	   $.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/user/verificationPhone.do",
            data: {phone: $("#telphone").val().trim(),vcode:$("#vcode").val().trim()},  
            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
            success: function (data){ 
              if(data.code =='1'){
            	$.cookie("currentCount",0);
            	vcode.parent().find(".f12").html('* '+data.info);
            	setTimeout('refresh()',1000); 
              }else{
            	   telphone.parent().next().html('* '+data.info);
            	  $("#verifyPhone").removeClass("ui-disabled");
              }
            }  
	   });
   }else{
	   $("#vcode").focus();
	   vcode.parent().find(".f12").html('* 请输入手机短信验证码!');
	   $("#verifyPhone").removeClass("ui-disabled");
	   return;
   }
}
    </script>
</html>
