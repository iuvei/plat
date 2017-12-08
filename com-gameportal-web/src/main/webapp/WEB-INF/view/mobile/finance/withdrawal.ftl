<!DOCTYPE html>
<html>
  <head>
  	<#include "/common/config.ftl">
	${meta}
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>提款</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="/favicon.ico">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm-extend.min.css">
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<style type="text/css">
		header{height:2.5rem;}
		header .header_left{line-height: 2.5rem;width:7rem;font-size: .92rem;height: 2.5rem;}
		header .header_right{line-height: 2rem;margin-right: .2rem;font-size: .72rem;}
		.img_01{float: left;width:1.4rem;height:1.3rem}
		.qka_box{border:2px solid #ccc;width:96%;margin:0 auto;padding:.3rem;border-radius: 5px;margin-top:.5rem;}
		.qka_box .picker_style{width:88%;float: right;height: 1.3rem;font-size: .75rem;
				background-size: cover;border: 0;background: url(${zy_path}mstatic/images/ck_03.png) no-repeat center right;}
		.qka_box2{position: relative;border-radius: 0;}
		.qka_box2 .picker_style{background:url(${zy_path}mstatic/images/login_01.jpg) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box2 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		p{margin:.2rem 0;color:#96999b}
		.qka_box3 .qka_box .picker_style{background: url(${zy_path}mstatic/images/login_03.jpg) no-repeat center left;background-size: contain;
		padding-left:55% ;}
		.qka_box3 span{left:22%}
		.qka_box3 .img3{margin-left: 2%;height:2.1rem;position: relative;top:.5rem;border:2px solid #ccc;padding:.3rem;}
		.qka_box3 .fr{font-size: .72rem;position: relative;top:1rem;}
		.qka_box4{padding:0;border:0;}
		.qka_box4 input[type=button]{width:100%;height:2.2rem;font-size: .92rem;background-color: #417be7;color:#fff;border:0}
		
		.qka_box8{position: relative;border-radius: 0;}
		.qka_box8 .picker_style{background:url(${zy_path}mstatic/images/login_02.jpg) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box8 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		.qka_box5{position: relative;border-radius: 0;}
		.qka_box5 .picker_style{background:url(${zy_path}mstatic/images/qk_01.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box5 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		.qka_box6{position: relative;border-radius: 0;}
		.qka_box6 .picker_style{background:url(${zy_path}mstatic/images/qk_02.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box6 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		
		.qka_box7{position: relative;border-radius: 0;}
		.qka_box7 .picker_style{background:url(${zy_path}mstatic/images/qk_03.png) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box7 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		.qka_box9{position: relative;border-radius: 0;}
		.qka_box9 .picker_style{background:url(${zy_path}mstatic/images/login_01_116.jpg) no-repeat center left; background-size: contain;
				padding-left:35%;}
		.qka_box9 span{display: block;position: absolute;left:12%;top:.4rem;font-size: .75rem;color:#96999b}
		
		footer ul{list-style: none;padding:0;margin:0;}
		footer ul li img{width:1rem;height:1rem;margin-top:.2rem;}
		footer ul li p{font-size: .72rem;}
		.f12{color: #cc0000;
		    margin-top: .1rem;
		    width: 100%;
		    clear: left;
		    font-size: .26rem;
		}
	</style>
  </head>
  <body style="">
  	
    <div class="page-group">
        <div class="page page-current" style="background-color: #fff;">
        	<!--后台头部-->
			<header class="ht_header">
				<div class="header_left">
					<span onclick="history.go(-1);">
						<img src="${zy_path}mstatic/images/ck_04.png" style="width:.5rem;position: relative;top:.05rem;left:-.2rem"/> 资金管理</span>
				</div>
				<div class="header_right f14">
					${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
				</div>
			</header>
			<!--后台头部结束-->
			<#if cardPackage?? && userInfo.atmpasswd??>
			<!--银行账户-->
			<section class="wrap_01 overflow qka_box qka_box5">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${cardPackage.cardnumber}" readonly id="caseoutBankcard"/>
				<span>银行账号:</span>
			</section>
			<!--选择银行-->
			<section class="wrap_01 overflow qka_box qka_box6">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${cardPackage.province}${cardPackage.city}" readonly id="caseoutBankcard"/>
				<span>开户省市:</span>
			</section>
        	<!--选择省市-->
        	<section class="wrap_01 overflow qka_box qka_box7">
				<input type="text"  placeholder="" class="picker_style" style="width:100%" value="${cardPackage.openingbank}" readonly id="caseoutBankcard"/>
				<span>开户地址:</span>
			</section>
			<section class="wrap_01 overflow qka_box qka_box9">
				<input type="text"  placeholder="最低提款100元" class="picker_style" style="width:100%" id="caseoutFigure"/>
				<span>提款金额:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<section class="wrap_01 overflow qka_box qka_box8">
				<input type="password" class="picker_style" style="width:100%" id="atmpwd"/>
				<span>提款密码:</span>
			</section>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<!--开户类型-->
			<#else>
				<section class="wrap_01 overflow qka_box qka_box8">
					<input type="password" class="picker_style" style="width:100%" id="atmpwd"/>
					<span>提款密码:</span>
				</section>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
				<section class="wrap_01 overflow qka_box qka_box8">
					<input type="password" class="picker_style" style="width:100%" id="atmpwdok"/>
					<span>确认密码:</span>
				</section>
				<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			</#if>
			<!--验证码-->
			<div class="overflow wrap_01 qka_box3">
				<section class="wrap_01 overflow qka_box qka_box2 fl" style="width:50%">
					<input type="text"  placeholder="" class="picker_style" style="width:100%" id="vcode"/>
					<span>验证码:</span>
				</section>
				<img src="${action_path}validationCode/agentcode.do" class="img3 fl" id="imgCodeAgent" onclick="imgCode2()">
				<a href="javascript:;" class="fr" onclick="imgCode2()">刷新验证码</a>
			</div>
			<p class="f12" style="margin-left: 10px;">&nbsp;</p>
			<#if userInfo.atmpasswd??>
				<div class="qka_box qka_box4">
					<input class="grey_2 quxiao" href="javascript:;" type="button"  value="提 交" id="caseout_sub" />
				</div>
			<#else>
				<div class="qka_box qka_box4">
					<input class="grey_2 quxiao" href="javascript:;" type="button"  value="确认提款" id="caseout_atm" />
				</div>
			</#if>
        	<footer >
				<ul>
					<li><a href="/indexmp.html"><img src="${zy_path}mstatic/images/icon_01.png" /><p>首页</p></a></li>
					<li><a href="/favomp.html"><img src="${zy_path}mstatic/images/icon_02.png" /><p>活动</p></a></li>
					<li><a href="http://chat6.livechatvalue.com/chat/chatClient/chatbox.jsp?companyID=672531&configID=47081&jid=3980564989"><img src="${zy_path}mstatic/images/icon_03.png" /><p>客服</p></a></li>
					<li><a href="/mobile/accountmp.html"><img src="${zy_path}mstatic/images/icon_04.png" /><p>资金管理</p></a></li>
				</ul>
			</footer>
			
        </div>
    </div>
    <script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
    <script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm-extend.min.js' charset='utf-8'></script>
    <script type="text/javascript" src="${zy_path}mstatic/js/myjs/code.js" charset="utf-8"></script>
	<script>
	  $(function(){
	  	 $("#caseout_atm").click(function(){
	  	 	var atmpwd = $("#atmpwd");
	  	 	var atmpwdok = $("#atmpwdok");
	  	 	var vcode =$("#vcode");
	  	 	if(atmpwd.val() ==''){
	  	 		atmpwd.parent().next().html("* 请输入提款密码!");
	  	 		atmpwd.focus();
	  	 		return;
	  	 	}else{
	  	 		atmpwd.parent().next().html("&nbsp;");
	  	 	}
	  	 	if(atmpwdok.val() ==''){
	  	 		atmpwdok.parent().next().html("* 请输入确认提款密码!");
	  	 		atmpwdok.focus();
	  	 		return;
	  	 	}else{
	  	 		atmpwdok.parent().next().html("&nbsp;");
	  	 	}
	  	 	
	  	 	if(atmpwd.val() != atmpwdok.val()){
	  	 		atmpwdok.parent().next().html("* 两次密码输入不一致!");
	  	 		atmpwdok.focus();
	  	 		return;
	  	 	}else{
	  	 		atmpwdok.parent().next().html("&nbsp;");
	  	 	}
	  	 	
	  	 	if(vcode.val() == '' || vcode.val().length == 0){
				vcode.parent().parent().next().html("* 请输入验证码!");
				$("#vcode").focus();
				return;
			}else{
				vcode.parent().parent().next().html("&nbsp;");
			}
			
	  	 	if($("#caseout_atm").hasClass('_click')){
				return;
			}
	  	 	$("#caseout_atm").val('正在提交...');
			$("#caseout_atm").addClass('_click');
			$.ajax({
				type : "POST",
				url : "/mobile/setAtmPwdp.do",
				data : {
					atmPwd : atmpwd.val(),
					vcode : vcode.val()
				},
				async : true,
				success : function(data) {
					data = eval('('+data+')');
					if (data.code == "1") {
				 		vcode.parent().parent().next().html("* 恭喜，提款密码成功!");
				 		setTimeout('refresh()',1000);  
					} else if (data.code == "0") {
						vcode.parent().parent().next().html("* "+data.info);
						$("#caseout_atm").val('提 交');
						$("#caseout_atm").removeClass('_click');
					}else if (data.code == "9") {
						$("#caseout_atm").val('提 交');
						vcode.parent().parent().next().html("* "+data.info);
						$("#caseout_atm").removeClass('_click');
						$("#vcode").focus();
					} else {
						vcode.parent().parent().next().html("* 网络异常，请稍后再试!");
						$("#caseout_atm").val('提 交');
						$("#caseout_atm").removeClass('_click');
					}
				}
			})
	  	 });
	  	 
	  	 $("#caseout_sub").click(function(){
	  	     var caseoutFigure =$("#caseoutFigure");
	  	     var atmpwd =$("#atmpwd");
	  	     var vcode =$("#vcode");
	  	     
	  	     if(caseoutFigure.val() ==''){
	  	 		caseoutFigure.parent().next().html("* 请输入确认提款金额!");
	  	 		caseoutFigure.focus();
	  	 		return;
	  	 	}else if(isNaN(caseoutFigure.val())){
				caseoutFigure.parent().next().html("* 提款金额输入错误!");
				return;
	    	}else if(caseoutFigure.val() < 100){
				caseoutFigure.parent().next().html("* 单笔提款不能少于100!");
				return;
	    	}else{
	  	 		caseoutFigure.parent().next().html("&nbsp;");
	  	 	}
	  	 	
	  	 	if(atmpwd.val() ==''){
	  	 		atmpwd.parent().next().html("* 请输入提款密码!");
	  	 		atmpwd.focus();
	  	 		return;
	  	 	}else{
	  	 		atmpwd.parent().next().html("&nbsp;");
	  	 	}
	  	 	
	  	 	if(vcode.val() == '' || vcode.val().length == 0){
				vcode.parent().parent().next().html("* 请输入验证码!");
				$("#vcode").focus();
				return;
			}else{
				vcode.parent().parent().next().html("&nbsp;");
			}
			if($("#caseout_sub").hasClass('_click')){
				return;
			}
	  	 	$("#caseout_sub").val('正在提交...');
			$("#caseout_sub").addClass('_click');
			$.ajax({
				url: "/mobile/finance/saveCaseout.do",
				async: true,
				type: 'POST',
				dataType : "JSON",
				data: {
					caseoutPwd : atmpwd.val(),
					caseoutFigure : caseoutFigure.val(),
					code : vcode.val()
				},
				success: function(data){
					data = eval('('+data+')');
				 	if (data.code == "1") {
				 		vcode.parent().parent().next().html("* 恭喜，您的提款申请已成功提交!");
				 		setTimeout('refresh()',1000);  
					} else if (data.code == "0" || data.code =="-1") {
						vcode.parent().parent().next().html("* "+data.info);
						vcode.focus();
						$("#caseout_sub").val('确认提款');
						$("#caseout_sub").removeClass('_click');
					}else if (data.code == "9") {
						vcode.parent().parent().next().html("* "+data.info);
						imgCode2();
						$("#caseout_sub").val('确认提款');
						$("#caseout_sub").removeClass('_click');
					}else if (data.code == "10") {
						vcode.parent().parent().next().html("* "+data.info);
						caseoutFigure.focus();
						imgCode2();
						$("#caseout_sub").val('确认提款');
						$("#caseout_sub").removeClass('_click');
					}else {
						vcode.parent().parent().next().html("* 网络异常，请稍后再试！");
						$("#caseout_sub").val('确认提款');
						$("#caseout_sub").removeClass('_click');
					}
				},
				error: function(){
					vcode.parent().parent().next().html("* 网络异常，请稍后再试！");
					$("#caseout_sub").val('确认提款');
					$("#caseout_sub").removeClass('_click');
				}	
			});	
	  	 });
	  
	  });
	  
	  function refresh(){
		window.location.reload();
		}
	</script>
  </body>
</html>