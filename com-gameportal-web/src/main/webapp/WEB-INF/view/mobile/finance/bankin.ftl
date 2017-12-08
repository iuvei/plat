<!DOCTYPE html>
<html>
	<head>
	 	<#include "/common/config.ftl">
		${meta}
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<title>扫码支付</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<body style="background-color: #fff;">
		<!--后台头部-->
		<header class="ht_header">
			<div class="header_left f18">
				<span onclick="window.location.href='/mobile/depositmp.do';"><img src="${zy_path}mstatic/images/ck_04.png" style="width:.23rem;position: relative;top:.05rem"/> 存款</span>
			</div>
			<div class="header_right f14">
			 ${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
			</div>
		</header>
		<!--后台头部结束-->
		<section class="tc mt5">
			<img src="${zy_path}images/${card.remarks}" style="width:2.56rem;"/>
		</section>
		<section class="wrap_01 mt5 grey_2">
			<p class="f16">${card.bankaddr}账号：${card.ccno}</p>
			<p class="f16">用户名：${card.ccholder}</p>
		</section>
		
		<section class="wrap_01 ckc_ts mt5 f16 lh5" style="margin-bottom: 3rem;">
			<h3 class="red">温馨提示：</h3>
			<p class="grey_2">用本订单将于<span class="red">5分钟内失效</span>，请截屏上图二维码后进行支付，<span class="red">如需帮助请联系客服</span>谢谢您的支持！</p>
		</section>
		
		<#include '${action_path}common/mfooter.ftl' />
	</body>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${zy_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>
	
</html>
