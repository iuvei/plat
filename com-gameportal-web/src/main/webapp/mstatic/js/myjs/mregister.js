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
					account.parent().find(".grey_5").hide();
					account.parent().find(".f12").show().html('* 请输入6-11位字母数字组合内容！');
					account.focus();
					return;
				}else{
					account.parent().find(".grey_5").show().html('&nbsp;');
					account.parent().find(".f12").hide();
				}
				/**
				checkUname();
				if(isUname == false){
					account.parent().find(".grey_5").hide();
					account.parent().find(".f12").html('* 用户账号验证未通过！');
					account.focus();
					return;
				}else{
					account.parent().find(".grey_5").show().html('&nbsp;');
					account.parent().find(".f12").hide();
				}
				*/
				if(pwd_default.test(passwd.val()) == false){
					passwd.parent().find(".grey_5").hide();
					passwd.parent().find(".f12").show().html('* 请输入6-12位字母数字组合密码！');
					passwd.focus();
					return;
				}else{
					passwd.parent().find(".grey_5").show().html('&nbsp;');
					passwd.parent().find(".f12").hide();
				}
				if(pwd_default.test(surepasswd.val()) == false){
					surepasswd.parent().find(".grey_5").hide();
					surepasswd.parent().find(".f12").show().html('* 请输入6-12位字母数字组合密码！');
					surepasswd.focus();
					return;
				}else{
					surepasswd.parent().find(".grey_5").show().html('&nbsp;');
					surepasswd.parent().find(".f12").hide();
				}
				if(surepasswd.val() != passwd.val()){
					surepasswd.parent().find(".grey_5").hide();
					surepasswd.parent().find(".f12").show().html('* 两次登录密码不一致！');
					passwd.focus();
					return;
				}else{
					surepasswd.parent().find(".grey_5").show().html('&nbsp;');
					surepasswd.parent().find(".f12").hide();
				}
				if(regx_mobile.test(phone.val()) == false){
					phone.parent().find(".grey_5").hide();
					phone.parent().find(".f12").show().html('* 手机号码输入不正确！');
					phone.focus();
					return;
				}else{
					phone.parent().find(".grey_5").hide();
					phone.parent().find(".f12").hide();
				}
				/**
				checkPhone();
				if(isPhone == false){
					phone.parent().find(".grey_5").hide();
					phone.parent().find(".f12").show().html('* 手机号码验证未通过！');
					phone.focus();
					return;
				}else{
					phone.parent().find(".grey_5").show().html('&nbsp;');
					phone.parent().find(".f12").hide();
				}
				*/
				if(vcode.val() == '' || vcode.val().length ==0){
					vcode.parent().next().html('* 请输入验证码！');
					vcode.focus();
					return;
				}
				
				$("#register_sub").val('正在注册...');
				if($("#register_sub").hasClass('_click')){
					return;
				}
				$("#register_sub").addClass('_click');
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
							window.location.href = actionPath+"indexmp.do";
						} else if (data.code == "2") {
							account.parent().find(".f12").show().html('* '+data.info);
							$("#register_sub").val('立即注册');
							$("#register_sub").removeClass('_click');
						} else if (data.code == "3") {
							phone.parent().find(".f12").show().html('* '+data.info);
							$("#register_sub").val('立即注册');
							$("#register_sub").removeClass('_click');
						}else if (data.code == "9") {
							vcode.parent().next().html('* '+data.info);
							$("#register_sub").val('立即注册');
							$("#register_sub").removeClass('_click');
						}else {
							$("#register_sub").val('立即注册');
							$("#register_sub").removeClass('_click');
							ui.msg('网络异常，请稍后再试!',3);
						}
					},
					error: function(){
						$("#register_sub").val('立即注册');
						$("#register_sub").val('立即注册');
						$("#register_sub").removeClass('_click');
						ui.msg('网络异常，请稍后再试!',3);
					}	
				});
			});
		}
	}
	window.register = register;
})(jQuery, window, document);

$(function() {
	$("#account").focus();
	register.init();
});

function checkPhone(){
	var phone = $("#phone").val();
	if(phone != '' && phone.length !=0){
		if(regx_mobile.test(phone) == false){
			$("#phone").parent().find(".grey_5").hide();
			$("#phone").parent().find(".f12").show().html('* 手机号码输入不正确！');
			return;
		}else{
			$("#phone").parent().find(".grey_5").show().html('&nbsp;');
			$("#phone").parent().find(".f12").hide();
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
					$("#phone").parent().find(".grey_5").show().html('&nbsp;');
					$("#phone").parent().find(".f12").hide();
				}else {
					isPhone = false;
					$("#phone").parent().find(".grey_5").hide();
					$("#phone").parent().find(".f12").show().html('* '+data.info);
				}
			},
			error : function() {
				$("#phone").parent().find(".grey_5").hide();
				$("#phone").parent().find(".f12").show().html('* 网络异常，请稍后再试！');
			}
		});
	}else{
		$("#phone").parent().find(".grey_5").hide();
		$("#phone").parent().find(".f12").show().html('* 请填写手机号码！');
	}
}

function checkPasswd(){
	var passwd = $("#passwd");
	var surepasswd = $("#surepasswd");
	if(passwd.val() !='' && passwd.val().length > 0){
		if(pwd_default.test(passwd.val()) == false){
			passwd.parent().find(".grey_5").hide();
			passwd.parent().find(".f12").show().html('* 请输入6-12位字母数字组合密码！');
			passwd.focus();
			return;
		}else{
			passwd.parent().find(".grey_5").show().html("&nbsp;");
			passwd.parent().find(".f12").hide();
		}
	}else{
		passwd.parent().find(".grey_5").hide();
		passwd.parent().find(".f12").show().html('* 密码长度6-12个字符！');
	}
	if(surepasswd.val() !='' && surepasswd.val().length > 0){
		if(pwd_default.test(surepasswd.val()) == false){
			surepasswd.parent().find(".grey_5").hide();
			surepasswd.parent().find(".f12").show().html('* 请输入6-12位字母数字组合密码！');
			return false;
		}
	}else{
		surepasswd.parent().find(".grey_5").hide();
		surepasswd.parent().find(".f12").show().html('* 请再次填写登录密码！');
		return false;
	}
	if(passwd.val() !='' && passwd.val().length > 0
			&& surepasswd.val() !='' && surepasswd.val().length > 0){
		if(surepasswd.val() != passwd.val()){
			surepasswd.parent().find(".grey_5").hide();
			surepasswd.parent().find(".f12").show().html('* 两次登录密码不一致！');
			return;
		}else{
			surepasswd.parent().find(".grey_5").show().html('&nbsp;')
			surepasswd.parent().find(".f12").hide();
		}
	}
}

function checkUname(){
	var account = $("#account").val();
	if(account !='' && account.length > 0){
		if(regx_default.test(account) == false){
			$("#account").parent().find(".grey_5").hide();
			$("#account").parent().find(".f12").show().html('* 请输入6-11位字母数字组合内容！');
			$("#account").focus();
			isUname = false;
			return;
		}else{
			$("#account").parent().find(".grey_5").show().html('&nbsp;');
			$("#account").parent().find(".f12").hide()
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
					$("#account").parent().find(".grey_5").show().html('&nbsp;')
					$("#account").parent().find(".f12").hide();
				}else {
					isUname = false;
					$("#account").parent().find(".grey_5").hide();
					$("#account").parent().find(".f12").show().html('* '+data.info);
				}
			},
			error : function() {
				$("#account").parent().find(".grey_5").hide();
				$("#account").parent().find(".f12").show().html('* 网络异常，请稍后再试!');
			}
		});
	}else{
		$("#account").parent().find(".grey_5").hide();
		$("#account").parent().find(".f12").show().html('* 请填写用户名!');
	}
	return isUname;
}

function checkCode(){
	var vcode = $("#vcode").val();
	if(vcode =='' || vcode.length < 0){
		$("#vcode").parent().next().html('* 请输入验证码！');
		$("#account").parent().find(".grey_5").hide();
		$("#account").parent().find(".f12").show().html('* 请填写用户名!');
	}else{
		$("#vcode").parent().next().html('');
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
				$("#vcode").parent().next().html('');
			}else {
				$("#vcode").parent().next().html('* 请输入验证码！');
				return;
			}
		},
		error : function() {
			$("#vcode").parent().next().html('* 网络异常，请稍后再试！');
		}
	});
}