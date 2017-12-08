var isPhone = false;
var isUname = false;
var regx_default = /^[0-9a-zA-Z]{6,11}$/;
var pwd_default = /^[0-9a-zA-Z]{6,12}$/;
var regx_mobile = /^1\d{10}$/;
var actionPath ="/";
!(function($, window, document, undefined) {
	
	var register = {
		init : function() {
			/**
			$("#account").blur(function(){
				checkUname();
			});
			$("#passwd").blur(function(){
				checkPasswd();
			});
			$("#surepasswd").blur(function(){
				checkPasswd();
			});
			$('#phone').blur(function() {
				checkPhone();
			});
			$("#vcode").blur(function() {
				checkCode();
			});
			*/
			$('#register_sub').click(function(){
				var account = $("#account");
				var passwd = $("#passwd");
				var surepasswd = $("#surepasswd");
				
				var phone = $("#phone");
				var vcode = $("#vcode");
				if(regx_default.test(account.val()) == false){
					$("#accountTip").html('请输入6-11位字母数字组合内容！').removeClass("pass").addClass("error").show();
					$("#account").focus();
					return;
				}else{
					$("#accountTip").hide();
				}
				
				/**
				checkUname();
				if(isUname == false){
					$("#accountTip").html('用户账号验证未通过!').removeClass("pass").addClass("error").show();
					$("#account").focus();
					return;
				}
				*/
				if(pwd_default.test(passwd.val()) == false){
					$("#passwdTip").html('请输入6-12位字母数字组合密码！').removeClass("pass").addClass("error").show();
					$("#passwd").focus();
					return;
				}else{
					$("#passwdTip").hide();
				}
				
				if(pwd_default.test(surepasswd.val()) == false){
					$("#surepasswdTip").html('请输入6-12位字母数字组合密码！').removeClass("pass").addClass("error").show();
					$("#surepasswd").focus();
					return;
				}else{
					$("#surepasswdTip").hide();
				}
				if(surepasswd.val() != passwd.val()){
					$("#surepasswdTip").html('两次登录密码不一致！').removeClass("pass").addClass("error").show();
					$("#passwd").focus();
					return;
				}else{
					$("#surepasswdTip").hide();
				}
				
				if(regx_mobile.test(phone.val()) == false){
					$("#phoneTip").html('手机号码输入不正确！').removeClass("pass").addClass("error").show();
					$("#phone").focus();
					return;
				}else{
					$("#phoneTip").hide();
				}
				/**
				checkPhone();
				if(isPhone == false){
					$("#phoneTip").html('手机号码验证未通过！').removeClass("pass").addClass("error").show();
					$("#phone").focus();
					return;
				}
				*/
				if(vcode.val() == '' || vcode.val().length ==0){
					$("#vcodeTip").html('请输入验证码！').removeClass("pass").addClass("error").show();
					$("#vcode").focus();
					return;
				}else{
					$("#vcodeTip").hide();
				}
				
				$("#register_sub").val('正在注册...');
				$("#register_sub").attr('disabled',true);
				$.ajax({
					type : "POST",
					url : actionPath+"register/index.do",
					data : {
						account : account.val().trim(),
						passwd : passwd.val().trim(),
						phone : phone.val().trim(),
						code : vcode.val().trim()
					},
					async : true,
					dataType : "json",
					success : function(data) {
						if (data.code == "1") {
							window.location.href = actionPath+"manage/index.do";
						} else if (data.code == "2") {
							//$.msg(data.info,3);
							$("#accountTip").html(data.info).removeClass("pass").addClass("error").show();
							$("#account").focus();
							$("#register_sub").val('立即注册');
							$("#register_sub").attr('disabled',false);
						}else if (data.code == "3") {
							//$.msg(data.info,3);
							$("#phoneTip").html(data.info).removeClass("pass").addClass("error").show();
							$("#phone").focus();
							$("#register_sub").val('立即注册');
							$("#register_sub").attr('disabled',false);
						}else if (data.code == "9") {
							$("#vcodeTip").html(data.info).removeClass("pass").addClass("error").show();
							$("#register_sub").val('立即注册');
							$("#register_sub").attr('disabled',false);
						}else {
							$.msg('网络异常，请稍后再试！',2);
							$("#register_sub").val('立即注册');
							$("#register_sub").attr('disabled',false);
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#register_sub").val('立即注册');
						$("#register_sub").attr('disabled',false);
					}	
				});
			});
		}
	}
	window.register = register;
})(jQuery, window, document);

$(function() {
	register.init();
	/**
	$("#rulesCheckBox").click(function () {
        $("#register_sub").toggleClass("ui-disabled", !$(this).is(":checked"));
        if($(this).is(":checked")){
        	$("#register_sub").removeAttr('disabled');
        }else{
        	$("#register_sub").attr('disabled',true);
        }
    });
    */
});

function checkPhone(){
	var phone = $("#phone").val();
	if(phone != '' && phone.length !=0){
		if(regx_mobile.test(phone) == false){
			$("#phoneTip").html('手机号码输入不正确。').removeClass("pass").addClass("error").show();
			return;
		}
		$.ajax({
			url : actionPath+"register/validform/3.do?"+Math.random(),
			async : true,
			type : "GET",
			data : {
				"param" : phone.trim()
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "y") {
					isPhone = true;
					$("#phoneTip").html(data.info).removeClass("error").addClass("pass").show();
				}else {
					isPhone = false;
					$("#phoneTip").html(data.info).removeClass("pass").addClass("error").show();
				}
			},
			error : function() {
				$.msg("网络异常，请稍后再试！",2);
			}
		});
	}else{
		$("#phoneTip").html('请填写手机号码！').removeClass("pass").addClass("error").show();
	}
}

function checkPasswd(){
	var passwd = $("#passwd");
	var surepasswd = $("#surepasswd");
	if(passwd.val() !='' && passwd.val().length > 0){
		if(pwd_default.test(passwd.val()) == false){
			$("#passwdTip").html('请输入6-12位字母数字组合密码！').removeClass("pass").addClass("error").show();
			passwd.focus();
			return;
		}else{
			$("#passwdTip").html('验证通过！').removeClass("error").addClass("pass").show();
		}
	}else{
		$("#passwdTip").html('密码长度6-12个字符！').removeClass("pass").addClass("error").show();
	}
	if(surepasswd.val() !='' && surepasswd.val().length > 0){
		if(pwd_default.test(surepasswd.val()) == false){
			$("#surePasswdTip").html('请输入6-12位字母数字组合密码！').removeClass("pass").addClass("error").show();
			return false;
		}
	}else{
		$("#surePasswdTip").html('请再次填写登录密码！').removeClass("pass").addClass("error").show();
		return false;
	}
	if(passwd.val() !='' && passwd.val().length > 0
			&& surepasswd.val() !='' && surepasswd.val().length > 0){
		if(surepasswd.val() != passwd.val()){
			$("#surePasswdTip").html('两次登录密码不一致！').removeClass("pass").addClass("error").show();
			return;
		}else{
			$("#surePasswdTip").html('验证通过！').removeClass("error").addClass("pass").show();
		}
	}
}

function checkUname(){
	var account = $("#account").val();
	if(account !='' && account.length > 0){
		if(regx_default.test(account) == false){
			$("#accountTip").html('请输入6-11位字母数字组合内容').removeClass("pass").addClass("error").show();
			$("#account").focus();
			return;
		}
		$.ajax({
			url : actionPath+"register/validform/1.do",
			async : true,
			type : "GET",
			data : {
				"param" : account.trim()
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "y") {
					isUname = true;
					$("#accountTip").addClass("pass").removeClass("error").html(data.info).show();
				}else {
					isUname = false;
					$("#accountTip").removeClass("pass").addClass("error").html(data.info).show();
				}
			},
			error : function() {
				$.msg("网络异常，请稍后再试！",2);
			}
		});
	}else{
		$("#accountTip").html('请填写用户名！').removeClass("pass").addClass("error").show();
	}
}

function checkCode(){
	var vcode = $("#vcode").val();
		if(vcode =='' || vcode.length < 0){
			$("#vcodeTip").html('请输入验证码').removeClass("pass").addClass("error").show();
			//$("#vcode").focus();
		}else{
			$("#vcodeTip").html('请填写验证码！').removeClass("pass").addClass("error").show();
		}
		$.ajax({
			url : actionPath+"register/validform/5.do",
			async : true,
			type : "GET",
			data : {
				"param" : vcode.trim()
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "y") {
					$("#vcodeTip").addClass("pass").removeClass("error").html(data.info).show();
				}else {
					$("#vcodeTip").removeClass("pass").addClass("error").html(data.info).show();
					return;
				}
			},
			error : function() {
				$.msg("网络异常，请稍后再试！",2);
			}
		});
}

