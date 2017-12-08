<!DOCTYPE html>
<html>
	<head>
		<#include "${action_path}common/config.ftl">
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	    <meta name="keywords" content="">
	    <meta name="description" content="">
	    <meta name="format-detection" content="telephone=yes">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<link rel="Bookmark" href="favicon.ico">
		<title>${title}--登录</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
	<script type="text/javascript">
		$(function(){
			var hei=$(window).height();
			$(".swiper-slide").height(hei);
		})
		function refresh(){
			$("#imgCode").attr("src","/validationCode/pcrimg.do?r="+(new Date()).getTime());	
		}
	</script>

	<body>
		<div class="modal">
			<h1 class="f18">用户登录</h1>
			<div class="input_box">
				<input type="text" placeholder="" id="loginname" maxlength="15"/>
				<b class="icon_user">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账号：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="password" placeholder="" id="password" maxlength="15"/>
				<b class="icon_pass">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;密码：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<div class="overflow" style="height: 1rem;">
					<input type="text"  style="width:35%;padding-left:35%;width:15%;" class="fl" id="validationCode" maxlength="4">
					<b class="icon_yz" style="width:35%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;验证码：</b>
					<a class="f14 grey_3 fr" href="javascript:imgCode();">刷新验证码</a>
					<a class="a_code">
						<img src="${action_path}validationCode/pcrimg.do" class="fl img_code" id="imgCode" onclick="refresh();"/>
	                </a>
				</div>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="checkbox" class="input_check"/><span class="lh80">记住账户</span>
				<!-- <span onclick="window.location.href='zhmm.html'">找回密码</span> -->
				<img src="${action_path}mstatic/images/login_04.png" class="img_check"/>
			</div>
			<div class="input_box" style="width:100%;margin-bottom: 0;">
				<input type="button" id="mlogin" value="登录" style="background-color: #417be7;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';"/>
			</div>
		</div>
	</body>
	<script src="${action_path}mstatic/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${action_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>

	<script>
	   var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 30,
    });
      var o_src="/images/login_04.png";
	  var n_src="/images/login_04_2.png";
	  $(".input_check").click(function(){
	  	if($(".input_check").attr("checked")=="checked"){
		  	$(".img_check").attr("src",o_src);
		  }else{
		  	$(".img_check").attr("src",n_src);
		  }
	  })
    </script>
</html>
<script type="text/javascript" src="${zy_path}mstatic/js/jquery.cookie.js" charset="utf-8"></script>
<script type="text/javascript" src="${zy_path}mstatic/js/myjs/mlogin.js" charset="utf-8"></script>
<script type="text/javascript" src="${zy_path}mstatic/js/myjs/code.js" charset="utf-8"></script>
