<!DOCTYPE html>
<html>
	<head>
		<#include "/common/config.ftl">
		${meta}
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0" />
   		<meta name="format-detection" content="telephone=no" />
		<title>${title}--我要转账</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<style>
		.top {font-size: 16px;font-weight: 700;height: 40px;line-height: 40px;text-align: center;color: rgba(255, 255, 255, .4);background: #ff6600;}
		.top img {height: 18px;margin-bottom: 3px;margin-right: 5px;vertical-align: middle;}
	</style>
	<body style="background-color: #fff;">
		<!--后台头部-->
		<header class="ht_header">
			<div class="header_left f18">
				<span onclick="history.go(-1);"><img src="${zy_path}mstatic/images/ck_04.png" style="width:.23rem;position: relative;top:.05rem"/> 我要转账</span>
			</div>
			<div class="header_right f14">
				${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
			</div>
		</header>
		<!--后台头部结束-->
		<div class="top" ng-click="getBalance()">
			<span ng-if="balanceLoaded &amp;&amp; currentUser.TestState!=2" class="ng-scope"><img ng-src="/mstatic/images/reload.png" src="/mstatic/images/reload.png"><span class="ng-binding">${accountMoneyCount!}</span></span>
		</div>
		<section class="ck_box" id="zcpt">
			<div class="wrap_01" >
				<b class="b3"></b><span class="grey f16">转出平台</span><i class="zs"></i>
				<input type="hidden" id="transferOut" />
			</div>
		</section>
		<section class="ck_box" id="zrpt">
			<div class="wrap_01">
				<b class="b4"></b><span class="grey f16">转入平台</span><i class="zs"></i>
				<input type="hidden" id="transferIn" />
			</div>
		</section>
		<div class="modal" style="top:0;position: relative;z-index: 0;">
			<div class="input_box" style="width:100%">
				<input type="text" placeholder="" id="transferNum" maxlength="10"/>
				<b class="icon_money">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;转出金额：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<div class="overflow" style="height: 1rem;">
					<input type="text"  style="width:35%;padding-left:39%;width:15%;" class="fl" id="vcode" maxlength="4">
					<b class="icon_yz" style="width:35%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;验证码：</b>
					<a class="f14 grey_3 fr" href="javascript:void(0)" onclick="imgCode2();">刷新验证码</a>
					<a class="a_code">
	               		<!--<img src="images/pcrimg.do" class="fl img_code"/>-->
	               		<img src="${action_path}validationCode/agentcode.do" id="imgCodeAgent" class="fl img_code" onclick="imgCode2();"/>
	                </a>
					
				</div>
				<p class="f12">&nbsp;</p>
			</div>
			<input type="hidden" id="agBill" value="${agBill}" />
		    <input type="hidden" id="aginBill" value="${aginBill}" />
		    <input type="hidden" id="bbinBill" value="${bbinBill}" />
		    <input type="hidden" id="ptBill" value="${ptBill}" />
		    <input type="hidden" id="mgBill" value="${mgBill}" />
		    <input type="hidden" id="saBill" value="${saBill}" />
			<div class="input_box" style="width:100%;margin:.8rem 0 2rem 0;">
				<input type="button" id="transfer_sub"  value="确定转账" style="background-color: #417be7;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';"/>
			</div>
		</div>
		<#include '${action_path}common/mfooter.ftl' />
		<!--转出平台-->
		<div class="grey_div  undis"></div>
		<div id="zcpt_tan" class="undis modal" style="position: absolute;">
			<h1 class="f18">请您选择您要转出的平台</h1>
			<a class="close">&#215;</a>
			<section class="ck_box" style="margin:0 auto .3rem" id="myPackageOut">
				<div class="wrap_01">
					<a class="grey f16" href="javascript:void(0)" data="AA">中心钱包</a>
				</div>
			</section>
			<#list gameAccountList as game>
				<#if game.uname !='BBIN'>
	            <section class="ck_box" style="margin:0 auto .3rem">
					<div class="wrap_01">
						<a class="grey f16" href="javascript:void(0)" data="${game.uname}">${game.fullname}</a>
					</div>
				</section>
				</#if>
	         </#list>
			<div class="input_box" style="width:100%;margin:0 auto 2rem ;text-align: center;">
				<input class="grey_2 quxiao" href="javascript:;" type="button"  value="取 消"  style="width:20%;font-family: 'microsoft yahei';border:1px solid #ccc;padding:.2rem;background-color: transparent;"/>
			</div>
		</div>
		<!--转入平台-->
		<div class="grey_div  undis"></div>
		<div id="zrpt_tan" class="undis modal" style="position: absolute;">
			<h1 class="f18">请您选择您要转入的平台</h1>
			<a class="close">&#215;</a>
			<section class="ck_box" style="margin:0 auto .3rem" id="myPackageIn">
				<div class="wrap_01">
					<a class="grey f16" href="javascript:void(0)" data="AA">中心钱包</a>
				</div>
			</section>
			<#list gameAccountList as game>
				<#if game.uname !='BBIN'>
	            <section class="ck_box" style="margin:0 auto .3rem">
					<div class="wrap_01">
						<a class="grey f16" href="javascript:void(0)" data="${game.uname}">${game.fullname}</a>
					</div>
				</section>
				</#if>
	         </#list>
	         
			<div class="input_box" style="width:100%;margin:0 auto 2rem ;text-align: center;">
				<input class="grey_2 quxiao" href="javascript:;" type="button"  value="取 消"  style="width:20%;font-family: 'microsoft yahei';border:1px solid #ccc;padding:.2rem;background-color: transparent;"/>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${zy_path}mstatic/js/jquery-1.8.3.min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/li.js" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/myjs/code.js" charset="utf-8"></script>
	<script type="text/javascript">
		var outId = "";
		var inId ="";
		var regx = /^[1-9]+[0-9]*]*$/;
		$(".close").click(function(){
			$("#zcpt_tan").hide();
			$("#zcpt_tan").prev().hide();
			$("#zrpt_tan").hide();
			$("#zrpt_tan").prev().hide();
		});
		$(".quxiao").click(function(){
			$("#zcpt_tan").hide();
			$("#zcpt_tan").prev().hide();
			$("#zrpt_tan").hide();
			$("#zrpt_tan").prev().hide();
		});
		$("#zcpt").click(function(){
			$("#zcpt_tan").show();
			$("#zcpt_tan").prev().show();
		});
		$("#zrpt").click(function(){
			$("#zrpt_tan").find("section").show();
			$("#zrpt_tan").show();
			if(outId =='AA'){
				$("#myPackageIn").hide();
			}else if(outId !='' && outId !='AA'){
				$("#zrpt_tan").find("section").hide();
				$("#myPackageIn").show();
			}
			$("#zrpt_tan").prev().show();
		});
		
		$("#zcpt_tan .ck_box").click(function(){
			var yh = $(this).find("a").html();
			outId = $(this).find("a").attr("data");
			$("#transferOut").val(outId);
			$("#zcpt").find("span").html(yh);
			$("#zcpt").find(".zs").hide();
			$("#zcpt_tan").hide();
			$("#zcpt_tan").prev().hide();
			$("#transferIn").val('');
			$("#zrpt").find("span").html('转入平台');
		});
		$("#zrpt_tan .ck_box").click(function(){
			var yh = $(this).find("a").html();
			inId = $(this).find("a").attr("data");
			$("#transferIn").val(inId);
			$("#zrpt").find("span").html(yh);
			$("#zrpt").find(".zs").hide();
			$("#zrpt_tan").hide();
			$("#zrpt_tan").prev().hide();
		});
		
		$("#transfer_sub").click(function(){
	        var transferNum =$("#transferNum");
	        var vcode = $("#vcode");
			if(outId ==''){
				ui.msg('请选择转出平台！',3);
				return;
			}
			if(inId == ''){
				ui.msg('请选择转入平台！',3);
				return;
			}
			if(transferNum.val() ==''){
				transferNum.parent().find(".f12").html("* 请输入转账金额!");
				transferNum.focus();
				return;
			}else if(!regx.test(transferNum.val())){
				transferNum.parent().find(".f12").html("* 转账金额输入错误!");
				transferNum.focus();
				return;
			}else{
				transferNum.parent().find(".f12").html('');
			}
			if(vcode.val().length != 4){
				vcode.parent().next().html("* 请输入4位数字验证码!").show();
				vcode.focus();
				return;
			}
			$("#transfer_sub").val('正在提交...');
			$("#transfer_sub").attr('disabled',true);
			$.ajax({
				type : "POST",
				url : "/manage/capital/gameTransfer.do?timeStamp="+new Date().getTime(),
				data : {
					transferOut : outId,
					transferIn : inId,
					transferNum : transferNum.val(),
					code : vcode.val(),
					agBill : $("#agBill").val(),
					aginBill : $("#aginBill").val(),
					ptBill : $("#ptBill").val(),
					mgBill : $("#mgBill").val(),
					bbinBill : $("#bbinBill").val(),
					saBill : $("#saBill").val()
				},
				dataType : "json",
				success : function(data) {
					if (data.code == "1") {
						ui.alert({title:'温馨提示',content:'转账成功，祝君盈利多多！',icon:'ok',callback:function(){
				 			window.location.href='/mobile/accountmp.do';
				 		}});
						imgCode2();
					} else if (data.code == "0") {
						ui.msg(data.info,2);
						transferNum.focus();
						$("#transfer_sub").val('确认转账');
						$("#transfer_sub").attr('disabled',false);
					} else if (data.code == "9") {
						ui.msg(data.info,2);
						vcode.focus();
						$("#transfer_sub").val('确认转账');
						$("#transfer_sub").attr('disabled',false);
					}else {
						ui.msg('网络异常，请稍后再试！',2);
						$("#transfer_sub").val('确认转账');
						$("#transfer_sub").attr('disabled',false);
					}
				},
				error: function(){
					ui.msg('网络异常，请稍后再试！',2);
					$("#transfer_sub").val('确认转账');
					$("#transfer_sub").attr('disabled',false);
				}
			});
		});
	</script>
</html>
