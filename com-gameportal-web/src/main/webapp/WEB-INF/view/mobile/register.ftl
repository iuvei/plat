<!DOCTYPE html>
<html>
	<head>
		<#include "${action_path}common/config.ftl">
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<title>${title}--注册</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
	<body>
		<!--头部-->
		<#include "${action_path}common/mheader.ftl">
		<!--头部结束-->
		<div class="modal" style="top:0;position: relative;z-index: 0;">
			<h1 class="f18">填写注册信息</h1>
			<div class="input_box">
				<input type="text" placeholder="" id="account"/>
				<b class="icon_user">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账号：</b>
				<p class="grey_5">* 请输入6-11位长度字符!</p>
				<p class="f12" style="display:none;">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="password" placeholder="" id="passwd"/>
				<b class="icon_pass">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密码：</b>
				<p class="grey_5">* 请输入6-12位长度且包含字母、数字!</p>
				<p class="f12" style="display:none;">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="password" placeholder="" style="width:63%" id="surepasswd"/>
				<b class="icon_pass" style="width:40%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;确认密码：</b>
				<p class="grey_5">* 请输入6-12位长度且包含字母、数字!</p>
				<p class="f12" style="display:none;">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="tel" placeholder="" id="phone"/>
				<b class="icon_tel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手机号：</b>
				<p class="grey_5">* 请输入11位长度手机号码!</p>
				<p class="f12" style="display:none;">&nbsp;</p>
			</div>
			<div class="input_box">
				<div class="overflow" style="height: 1rem;">
					<input type="text"  style="width:35%;padding-left:35%;width:15%;" class="fl" id="vcode" maxlength="4">
					<b class="icon_yz" style="width:35%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;验证码：</b>
					<a class="f14 grey_3 fr" href="javascript:imgCode2();">刷新验证码</a>
					<a class="a_code">
						<img src="${action_path}validationCode/agentcode.do" class="fl img_code" id="imgCodeAgent"/>
	                </a>
				</div>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="checkbox" class="input_check"/><span class="lh80">我同意《用户协议》和《隐私条款》</span> 
				<img src="${zy_path}mstatic/images/login_04.png" class="img_check"/>
			</div>
			<div class="input_box" style="width:90%;margin-bottom:2rem;">
				<input type="button" value="立即注册" id="register_sub"  style="background-color: #ff7800;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';"/>
			</div>
		</div>
		<#include '${action_path}common/mfooter.ftl' />
	</body>
	<script src="${action_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/myjs/mregister.js?v=3" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/myjs/code.js" charset="utf-8"></script>
	<script type="text/javascript">
		$(".login_on").click(function(){
		  	$("#mymodal").show();
		  	$("#mymodal").prev().show();
		  });
		  $(".close").click(function(){
		  	$("#mymodal").hide();
		  	$("#mymodal").prev().hide();
		  	$("#mmzh_id").hide();
		  	$("#mmzh_id").prev().hide();
		  	$("#zhbcz").hide();
		  	$("#zhbcz").prev().hide();
		  	$("#shiwan").hide();
		  	$("#shiwan").prev().hide();
		  });
		  $(".mmzh").click(function(){
		  	$("#mymodal").hide();
		  	$("#mymodal").prev().hide();
		  	$("#mmzh_id").show();
		  	$("#mmzh_id").prev().show();
		  });
		  $("#btn_mmzh").click(function(){
		  	$("#zhbcz").show();
		  	$("#zhbcz").prev().show();
		  });
		  $(".header_close").click(function(){
		  	$("#zhbcz").hide();
		  	$("#zhbcz").prev().hide();
		  });
		 $(".sw").click(function(){
		 	$("#shiwan").show();
		 	$("#shiwan").prev().show();
		 })
	</script>
</html>
