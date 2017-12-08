// JavaScript Document
var actionPath="/";
!(function($, window, document, undefined) {
	var index = {
		init : function() {
			$('#loginBtn').click(this.login);
		},
		// 账号登录
		login : function() {
			var username = $('#loginname'), password = $('#password'), validationCode = $('#validationCode'), _this = $(this);
			if (username.val() == '' || username.val().length == 0) {
				alert("请输入您的登录账号！");
				//$.msg("请输入您的登录账号！",3);
				username.focus();
				return;
			}
			if (password.val() == '' || password.val().length == 0) {
				//$.msg("请输入您的登录密码！",3);
				alert("请输入您的登录密码！");
				password.focus();
				return;
			}
			if(validationCode.val().length != 4){
				//$.msg("请输入4位数字验证码！",3);
				alert("请输入4位数字验证码");
				validationCode.focus();
				return;
			}
			$('#loginBtn').attr("disabled",true);
			$('#loginBtn').val("登录中...");
			$.ajax({
				url : actionPath+"loginSubmit.do",
				async : true,
				type : "POST",
				data : {
					"username" : username.val(),
					"password" : password.val(),
					"rememberme" : $("#rememberme").attr("checked")=="checked",
					"validationCode" : validationCode.val()
				},
				dataType : "json",
				success : function(data) {
					$('#loginBtn').attr("disabled",false);
					$.cookie("loginname",username.val());
					if (data.code == "1") {
						if (data.status == 1) {
							top.location.href = actionPath+"index.html";
						} else {
							top.location.href = actionPath+"index.html";
						}
					} else if (data.code == "2") {
						$('#loginBtn').val("登录");
						username.focus();
						alert(data.info);
						//$.msg(data.info,3);
					} else if(data.code == "3"){
						$('#loginBtn').val("登录");
						password.focus();
						alert(data.info);
						//$.msg(data.info,3);
					} else if(data.code == "4"){
						$('#loginBtn').val("登录");
						imgCode();
						validationCode.focus();
						alert(data.info);
						//$.msg(data.info,3);
					}
				},
				error : function() {
					//$.msg("网络异常，请稍后再试！",3);
					$('#loginBtn').attr("disabled",false);
					$('#loginBtn').val("登录");
					alert("网络异常，请稍后再试！");
				}
			});
		},
		register:function(){
			window.location.href= actionPath+"register.html";
		}
	}

	window.index = index;

})(jQuery, window, document);

$(function() {
	index.init();
	$("#loginname").val($.cookie("loginname"));
	$(document).keydown(function(event){ 
		if(event.keyCode==13){
			$('#loginBtn').click();
		} 
	}); 
});