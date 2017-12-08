<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--个人资料</title>
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/user.css" />
    <script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/public/utils.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/user/user.js"></script>
    <script src="${zy_path}js/base.js?v=1"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js?v=1"></script>
    <script type="text/javascript" >
    	$(function(){
    		$("#submitBtn").click(function(){
    			var truename = "";
    			if($("#truename").length > 0){
    				truename = $("#truename").val();
					if(base.valid.isNanme(truename) == false){
						$("#truename_tip").html('真实姓名格式不正确。');
						$("#truename_tip").removeClass("ui-none");
						$("#truename").focus();
						return;
					}else{
						$("#truename_tip").addClass("ui-none");
					}
    			}
    			var identitycard = "";
    			if($("#identitycard").length > 0 && $("#identitycard").val() !=''){
    				identitycard = $("#identitycard").val();
					if(base.valid.isIDcard(identitycard) == false){
						$("#identitycard_tip").html('身份证号码格式不正确。');
						$("#identitycard_tip").removeClass("ui-none");
						$("#identitycard").focus();
						return;
					}else{
						$("#identitycard_tip").addClass("ui-none");
					}
    			}
    			var phone = "";
    			if($("#phone").length > 0){
	    			phone = $("#phone").val();
					if(base.valid.isMobile(phone) == false){
						$("#phone_tip").html('您输入的手机号码格式不正确。');
						$("#phone_tip").removeClass("ui-none");
						$("#phone").focus();
						return;
					}else{
						$("#phone_tip").addClass("ui-none");
					}
    			}
    			var email = "";
    			if($("#email").length > 0){
	    			email = $("#email").val();
					if(base.valid.isEmail(email) == false){
						$("#email_tip").html('您输入的Email格式不正确。');
						$("#email_tip").removeClass("ui-none");
						$("#email").focus();
						return;
					}else{
						$("#email_tip").addClass("ui-none");
					}
    			}
    			var vcode = $("#vcode");
				if(vcode.val() =='' || vcode.val().length != 4){
					$("#vcode_tip").html('请输入4位数字验证码。');
					$("#vcode_tip").removeClass("ui-none");
					vcode.focus();
					return;
				}else {
					$("#vcode_tip").addClass("ui-none");
				}
    			
    			$("#submitBtn").val('正在提交...');
				$("#submitBtn").attr('disabled',true);
			
    			$.ajax({
    				type : "POST",
					url : "${action_path}manage/modifyUserInfo.do",
					data : {
						truename : truename,
						identitycard : identitycard,
						phone : phone,
						email : email,
						QQ : $("#QQ").val(),
						birthday : $("#birthday").val(),
						code : vcode.val()
					},
					dataType : "json",
					async : true,
					success : function(data) {
						if (data.code == "1") {
							$.remind('您的用户信息已经修改成功！',success_callback);
						} else if (data.code == "0") {
							$.msg(data.info,3);
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
						} else if(data.code == "9"){
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
							$("#vcode_tip").html(data.info);
							$("#vcode_tip").removeClass("ui-none");
							vcode.focus();
						}else {
							$.msg('网络异常，请稍后再试！',3);
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
						}
					}
    			});
    		});
    		
    		function success_callback(){
    			window.location.reload();
    		}
    	});
    	
    	function changeToUperCase(v){
		   v.value=v.value.toUpperCase();
		}
    </script>
</head>

<body>
    <div class="title size-bigger">个人资料</div>
    <div class="main">
        <div class="user-layer ui-mt30">
            <form action="javascript:;">
                <div class="ui-inputBox">
                    <label><span class="fl"><font class="color-orange">*</font> 真实姓名：</span>
                    	<#if userMsg.uname??>
                    		<span>${userMsg.uname}</span>
                    	<#else>
                        	<input class="fl " name="truename" id="truename" type="text" value="" placeholder="必须与提款卡号/支付宝姓名一致" autofocus />修改真实姓名请联系在线客服。
                    	</#if>
                    </label>
                    <p id="truename_tip" class="size-tiny error ui-none">验证不通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl"> 身份证号：</span>
                    	<#if userMsg.identitycard?exists && userMsg.identitycard !=''>
                    		<span>****${userMsg.identitycard?substring(4)}</span>
                    	<#else>
	                        <input class="fl" type="text" name="identitycard" id="identitycard" value="" onkeyup="changeToUperCase(this);" placeholder="请输入您的身份证号" />修改身份证请联系在线客服。
                    	</#if>
                    	
                    </label>
                    <p id="identitycard_tip" class="size-tiny error ui-none">验证不通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl"><font class="color-orange">*</font> 联系电话：</span>
                        <#if userMsg.phone?? && userMsg.phonevalid =='1'>
                    		<span><#if userMsg.phone??>***${userMsg.phone?substring(3)}</#if></span>
                    	<#else>
	                        <input class="fl" name="phone" id="phone" maxlength="50" type="text" value="${userMsg.phone}" placeholder="请输入您的手机号码" />手机号码是您更改真实姓名的唯一方式。
                    	</#if>
                    </label>
                    <p id="phone_tip" class="size-tiny ui-none error">验证不通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl"><font class="color-orange">*</font> E-mail邮箱：</span>
                    	<#if userMsg.email?? && userMsg.emailvalid =='1'>
                    		<span><#if userMsg.email??>***${userMsg.email?substring(3)}</#if></span>
                    	<#else>
	                        <input class="fl" name="email" id="email" maxlength="50" type="text" value="${userMsg.email}" placeholder="请输入您的电子邮箱地址" />电子邮件用于找回密码。
                    	</#if>
                    </label>
                    <p id="email_tip" class="size-tiny ui-none error">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl">QQ：</span>
                    	<#if userMsg.qq?if_exists>
                    		<span>***${userMsg.qq?substring(3)}</span>
                    	<#else>
                        	<input class="fl" name="QQ" id="QQ" type="text" value="${userMsg.qq}" placeholder="请输入您的QQ号码" />填写QQ方便我们能及时联系您。
                    	</#if>
                    </label>
                    <p class="size-tiny"></p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl">生日：</span>
                    	<#if userMsg.birthday?if_exists>
                    		${userMsg.birthday}&nbsp;&nbsp;
                    	<#else>
                        <div class="fl ui-datePicker ui-radius">
                            <div class="ui-calendar ui-radius"></div>
                            <input name="birthday" id="birthday" type="text" value="${userMsg.birthday}" placeholder="请输入您的生日" />
                            <span></span>
                        </div></#if>VIP会员可领取生日礼金。
                    </label>
                    <p class="size-tiny"></p>
                </div>
                <div class="ui-inputBox">
                
                    <label><span class="fl"><font class="color-orange">*</font> 验证码：</span>
                        <input name="code" maxlength="4" id="vcode" class="verifyCode fl" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode2();"><img id="imgCodeAgent" class="ui-block" src="${action_path}validationCode/agentcode.do" /></a>请输入验证码。
                    </label>
                    <p id="vcode_tip" class="size-tiny error ui-none">验证通过</p>
                </div>
                <input type="button" id="submitBtn" class="submit ui-btn ui-block ui-radius color-white bg-orange bg-brownHover ui-transition" value="提交" />
                <p class="color-brown">真实姓名、身份证号、手机号码、生日设定后不可以修改，如特殊情况需要修改请联系客服。</p>
            </form>
        </div>
    </div>

</body>

</html>