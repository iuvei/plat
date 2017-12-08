<!DOCTYPE html>
<html>
<head>
	<#include "/common/config.ftl">
	${meta}
	<meta charset="UTF-8">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <meta name="format-detection" content="telephone=yes">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<link rel="Bookmark" href="favicon.ico">
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/mobiscroll.2.17.0.min.css"/>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${zy_path}mstatic/js/mobiscroll.2.17.0.min.js" type="text/javascript" charset="utf-8"></script>
	
    <script src="${zy_path}mstatic/js/base.js?v=1"></script>
    <script src="${zy_path}mstatic/js/myjs/code.js"></script>
    <script type="text/javascript">
		
    </script>
</head>
<body>
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
		<div class="modal" style="margin-top:.15rem;z-index: 0;">
		  <#assign isok=userMsg.uname?if_exists && userMsg.identitycard?if_exists && userMsg.phone?if_exists && 
    	  userMsg.email?if_exists && userMsg.birthday?if_exists />
			<div class="input_box">
				<#if userMsg.uname?if_exists>
            		<input type="hidden" id="truename" value="${userMsg.uname}"/>
            		<input type="text" value="${userMsg.uname}" readonly/>
            	<#else>
                	<input type="text" style="padding-left:36%;width:70%" id="truename"/>
            	</#if> 
				<b class="icon_user2 f14" style="width:37.5%;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;真实姓名：</b>
				<p class="f12"><#if userMsg.uname?if_exists>&nbsp;<#else> 必须与提款卡姓名一致</#if></p>
			</div>
			<div class="input_box">
				 <#if userMsg.identitycard?if_exists && userMsg.identitycard !=''>
            		<input type="hidden" style="padding-left:36%;width:70%" class="input_card" id="identitycard" value="${userMsg.identitycard}"/>
            		<input type="text" style="padding-left:36%;width:70%" class="input_card" value="${userMsg.identitycard?substring(0,6)}****${userMsg.identitycard?substring(10)}" readonly/>
            	<#else>
            		<input type="text" style="padding-left:36%;width:70%" id="identitycard" class="input_card" value="" onkeyup="changeToUperCase(this);"/>
            	</#if>
				<b class="icon_card f14"  style="width:37.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;身份证号：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<#if userMsg.phone?if_exists && userMsg.phonevalid =='1'>
                 	<input name="phone" id="phone" maxlength="50" type="hidden" value="${userMsg.phone}" style="padding-left:35%;width:70%"/>
            		<input maxlength="50" type="text" style="padding-left:35%;width:70%" value="<#if userMsg.phone?if_exists>${userMsg.phone?substring(0,3)}****${userMsg.phone?substring(7)}</#if>" readonly/>
            	<#else>
            		<input style="padding-left:35%;width:70%" name="phone" id="phone" maxlength="50" type="text" value="${userMsg.phone}"/>
            	</#if>
				<b class="icon_tel2 f14"  style="width:37.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手机号码：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				 <#if userMsg.email?if_exists && userMsg.emailvalid =='1'>
            		 <input name="email" id="email" maxlength="50" type="hidden" value="${userMsg.email}" readonly style="padding-left:35%;width:70%"/>
            		 <input maxlength="50" type="text" value="***${userMsg.email?substring(3)}" readonly style="padding-left:35%;width:70%"/>
            	<#else>
                    <input name="email" id="email" maxlength="50" type="text" value="${userMsg.email}" style="padding-left:35%;width:70%"/>
            	</#if>
				<b class="icon_email f14"  style="width:37.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;电子邮箱：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				<input type="text" style="padding-left:34%;width:70%" class="input_qq" name="QQ" id="QQ" value="${userMsg.qq}"/>
				<b class="icon_qq f14"  style="width:37.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;QQ：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<div class="input_box">
				 <#if userMsg.birthday?if_exists>
           			<input type="hidden" name="birthday" id="birthday" value="${userMsg.birthday}" readonly style="padding-left:35%;width:70%"/>
           			<input type="text" id="demo_date" value="${userMsg.birthday}" readonly style="padding-left:35%;width:70%"/>
            	<#else>
                	<input type="text" style="padding-left:35%;width:70%" id="demo_date" class="input_birth" name="birthday" value="${userMsg.birthday}"  readonly/>
            	</#if>
				<b class="icon_birth f14"  style="width:37.5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;生&nbsp;&nbsp;&nbsp;&nbsp;日：</b>
				<p class="f12">&nbsp;</p>
			</div>
			<#if !isok>
				<div class="input_box">
					<div class="overflow" style="height: 1rem;">
						<input type="text"  style="width:35%;padding-left:35%;width:15%;" class="fl" id="vcode">
						<b class="icon_yz2" style="width:35%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;验证码：</b>
						<a class="f14 grey_3 fr" href="javascript:void(0)" onclick="imgCode2();">刷新验证码</a>
						<a class="a_code">
		               		<img src="${action_path}validationCode/agentcode.do" id="imgCodeAgent" onclick="imgCode2();" class="fl img_code"/>
		                </a>
					</div>
					<p class="f12" id="v_code">&nbsp;</p>
				</div>
				<div class="input_box" style="width:100%;margin-bottom:2rem;">
					<input type="button" id="submitBtn" value="提&nbsp交" style="background-color: #417be7;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';" onclick="tishi()"/>
				</div>
			<#else>
				<div class="input_box" style="width:100%;margin-bottom:2rem;">
					<p>温馨提示：您的资料已完善，如要修改请联系客服。</p>
				</div>
			</#if>
		</div>
	</body>
	<!--资料查询   结束-->
   <#include '${action_path}common/mfooter.ftl' />
	<!--弹出层  提示-->
	<div class="grey_div  undis" style="z-index: 99;"></div>
	<div id="tishi" class="undis modal" style="margin-top:1.5rem;position: absolute;">
		<div class="overflow modal_header">
			<div class="fl">
				温馨提示
			</div>
			<div class="fr header_close">
				&#215;
			</div>
		</div>
		<p class="grey_2 f14 tc lh80">您的用户信息已修改成功!</p>
		<div class="overflow btns_modal">
			<div class="fr">
				<a href="javascript:void(0)" class="quxiao f14">取消</a>
			</div>
			<div class="fl">
				<a href="javascript:void(0)" class="queding f14">确定</a>
			</div>
		</div>
	</div>
	<script src="${zy_path}mstatic/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${zy_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>

	<script>
	 $(function(){
			var hei=$(window).height();
			$(".swiper-slide").height(hei);
		})
		
        $(function () {
            //时间选择
          var submitBtn = $("#submitBtn");
          submitBtn.click(function(){
    			var truename = "";
    			if($("#truename").length > 0){
    				truename = $("#truename").val();
					if(base.valid.isNanme(truename) == false){
						$("#truename").parent().find(".f12").html('* 真实姓名格式不正确！');
						$("#truename").focus();
						return;
					}else{
						$("#truename").parent().find(".f12").html('&nbsp;');
					}
    			}
    			var identitycard = "";
    			if($("#identitycard").length > 0){
    				identitycard = $("#identitycard").val();
					if(base.valid.isIDcard(identitycard) == false){
						$("#identitycard").parent().find(".f12").html('* 真实姓名格式不正确！');
						$("#identitycard").focus();
						return;
					}else{
						$("#identitycard").parent().find(".f12").html('&nbsp;');
					}
    			}
    			var phone = "";
    			if($("#phone").length > 0){
	    			phone = $("#phone").val();
					if(base.valid.isMobile(phone) == false){
						$("#phone").parent().find(".f12").html('* 您输入的手机号码格式不正确！');
						$("#phone").focus();
						return;
					}else{
						$("#phone").parent().find(".f12").html('&nbsp;');
					}
    			}
    			var email = "";
    			if($("#email").length > 0){
	    			email = $("#email").val();
					if(base.valid.isEmail(email) == false){
						$("#email").parent().find(".f12").html('* 您输入的Email格式不正确');
						$("#email").focus();
						return;
					}else{
						$("#email").parent().find(".f12").html('&nbsp;');
					}
    			}
    			var vcode = $("#vcode");
				if(vcode.val() ==''){
					$("#v_code").html("* 请输入验证码!");
					vcode.focus();
					return;
				}else{
					$("#v_code").html('&nbsp;');
				}
    			
    			if(submitBtn.hasClass('_click')){
    				return
    		    }
    			submitBtn.text('正在提交...');
    		    
				submitBtn.addClass('_click');
    			$.ajax({
    				type : "POST",
					url : "${action_path}mobile/modifyUserInfo.do",
					data : {
						truename : encodeURI(truename),
						identitycard : identitycard,
						phone : phone,
						email : email,
						QQ : $("#QQ").val(),
						birthday : $("#demo_date").val(),
						code : vcode.val()
					},
					dataType : "JSON",
					async : true,
					success : function(data) {
						if (data.code == "1") {
					 		$("#tishi").show();
	  						$("#tishi").prev().show();
						} else if (data.code == "0") {
							$("#v_code").html(data.info);
							$("#submitBtn").text('提 交');
							$("#submitBtn").removeClass('_click');
						} else if(data.code == "9"){
							$("#submitBtn").text('提 交');
							$("#v_code").html('* 验证码输入错误！');
							vcode.focus();
						}else {
							$("#v_code").html('* 网络异常，请稍后再试！');
							$("#submitBtn").text('提 交');
							$("#submitBtn").removeClass('_click');
						}
					}
    			});
    		});
    		
    	});
    	
        function changeToUperCase(v){
		   v.value=v.value.toUpperCase();
		}
	
	  var o_src="${zy_path}mstatic/images/login_04.png";
	  var n_src="${zy_path}mstatic/images/login_04_2.png";
	  $(".input_check").click(function(){
	  	if($(".input_check").attr("checked")=="checked"){
		  	$(".img_check").attr("src",o_src);
		  }else{
		  	$(".img_check").attr("src",n_src);
		  }
	  });
	 $(function () {
		$('#demo_date').mobiscroll().date({
            theme: 'sense-ui',     
            mode: 'scroller',       
            display: 'modal', 
            lang: 'zh'       
        });
	 });
	 function tishi(){
	  	$("#submitBtn").click();
	 }
	 $(".quxiao").click(function(){
	 	$("#tishi").hide();
	  	$("#tishi").prev().hide();
	 });
	 $(".header_close").click(function(){
	 	$("#tishi").hide();
	  	$("#tishi").prev().hide();
	 });
	 $(".queding").click(function(){
	 	$("#tishi").hide();
	  	$("#tishi").prev().hide();
	  	$(".input_card").attr("disabled","disabled");
	  	$(".input_birth").attr("disabled","disabled");
	  	$(".input_qq").attr("disabled","disabled");
	  	window.location.reload();
	 });
    </script>
</html>