<!DOCTYPE html>
<html>
<head>
    <#include "/common/config.ftl">
	${meta}
    <title>${title}-资金管理</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<body style="background-color: #fff;">
		<!--后台头部-->
		<header class="ht_header">
			<div class="header_left f18">
				<span onclick="history.go(-1);">资金管理</span>
			</div>
			<div class="header_right f14">
				 ${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
			</div>
		</header>
		<!--后台头部结束-->
		<!--资金管理分类-->
		<section class="zjgl_fl">
			<ul>
				<li><a href="${action_path}mobile/depositmp.html"><b></b><p class="f14">充  值</p></a></li>
				<li><a href="${action_path}mobile/transfermp.html"><b></b><p class="f14">转  账</p></a></li>
				<li><a href="${action_path}mobile/withdrawalmp.html"><b></b><p class="f14">提  款</p></a></li>
			</ul>
		</section>
		<!--资金管理分类   结束-->
		<section class="wrap_01">
			<div class="overflow lh80">
				<div class="fl f16 grey_2">平台余额</div><div class="fr"><img src="${zy_path}mstatic/images/zjgl_05.png" style="max-width: 60%;margin-top:.1rem;	" class="refresh"/></div>
			</div>
		</section>
		<!--平台余额-->
		<section class="ptye">
			<div class="wrap_01">
				<ul>
					<li class="f14"><b class="fl"></b><div class="fr tc f16" style="width:70%"><span>${accountMoney}&nbsp;元</span><p class="f14">中心钱包</p></div></li>
					<#list gameList as game>
						<#if game.uname =='PT'>
							<li class="f14"><span id="PT"><img src="${zy_path}mstatic/images/s-loading.gif" style="width:16px;"/></span><p class="f14">PT厅</p></li>
						<#elseif game.uname =='MG'>
							<li class="f14"><span id="MG"><img src="${zy_path}mstatic/images/s-loading.gif" style="width:16px;"/></span><p class="f14">MG厅</p></li>
						<#elseif game.uname =='AGIN'>
							<li class="f14"><span  id="AGIN"><img src="${zy_path}mstatic/images/s-loading.gif" style="width:16px;"/></span><p class="f14">AG国际厅</p></li>
						<#elseif game.uname =='SA'>
							<li class="f14"><span  id="SA"><img src="${zy_path}mstatic/images/s-loading.gif" style="width:16px;"/></span><p class="f14">SA沙龙厅</p></li>
						</#if>
					</#list>
					<li class="f14"><a href="javascript:void(0)">+</a></li>					
				</ul>
			</div>
		</section>
		<!--平台余额   结束-->
		<section class="wrap_01">
			<div class="overflow lh80">
				<div class="fl f16 grey_2">完善资料</div></div>
			</div>
		</section>
		<!--资料查询-->
		<section class="zlcx">
			<div class="wrap_01">
				<ul>
					<#--
					<li class="f14"><a href="ht_xgmma.html"><b></b><p class="f14" onclick="window.location.href='ht_xgmma.html'">修改密码</p></a></li>
					<li class="f14"><a href="ht_ckmx.html"><b></b><p class="f14">存款明细</p></a></li>
					<li class="f14"><a href="ht_zzmx.html"><b></b><p class="f14">转账记录</p></a></li>
					-->
					<li><a href="${action_path}mobile/personalmp.html"><b></b><p class="f14">完善信息</p></a></li>
					<li><a href="${action_path}mobile/verifymp.html"><b></b><p class="f14">信息认证</p></a></li>
					<li class="f14"><a href="${action_path}mobile/bindCardmp.html"><b></b><p class="f14">绑定银行卡</p></a></li>
				</ul>
			</div>
		</section>
		<!--资料查询   结束-->
		<#include '${action_path}common/mfooter.ftl' />
	</body>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${zy_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(".refresh").click(function(){
			window.location.reload();
		})
		
		$(function(){
			if($("#MG")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getMGAccountBalance.do",
					dataType : "json",
					data:{
							gpname:'MG'
					},
					async : true,
					success : function(data) {
						if(data!=null){
							for(var i=0;i<data.length;i++){
								var game = data[i];
								$("#MG").html(game.money);
							}
						}
					},
					error: function(){
						$("#MG").html("0");
					}
				});
			}
			if($("#AGIN")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getGameBalance.do",
					dataType : "json",
					data:{
							gpname:'AGIN'
					},
					async : true,
					success : function(data) {
						var money = data[data.length-1].money;
						$("#AGIN").html(money);
					},
					error: function(){
						$("#AGIN").html("0");
					}
				});
			}
			
			if($("#PT")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getGameBalance.do",
					dataType : "json",
					data:{
							gpname:'PT'
					},
					async : true,
					success : function(data) {
						var money = data[data.length-1].money;
						$("#PT").html(money);
					},
					error: function(){
						$("#PT").html("0");
					}
				});
			}
			
			if($("#SA")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getSAAccountBalance.do",
					dataType : "json",
					data:{
							gpname:'SA'
					},
					async : true,
					success : function(data) {
						var money = data[data.length-1].money;
						$("#SA").html(money);
					},
					error: function(){
						$("#SA").html("0");
					}
				});
			}
			
			/**
			if($("#AG")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getGameBalance.do",
					dataType : "json",
					data:{
							gpname:'AG'
					},
					async : true,
					success : function(data) {
						var money = data[data.length-1].money;
						$("#AG").html(money);
					},
					error: function(){
						$("#AG").html("0");
					}
				});
			}
			
			
			if($("#BBIN")){
				$.ajax({
					type : "POST",
					url : actionPath+"manage/capital/getGameBalance.do",
					dataType : "json",
					data:{
							gpname:'BBIN'
					},
					async : true,
					success : function(data) {
						var money = data[data.length-1].money;
						$("#BBIN").html(money);
					},
					error: function(){
						$("#BBIN").html("0");
					}
				});
			}
			*/
		});   
	</script>
</html>