$(function(){
	var hei=$(window).height();
	$(".swiper-slide").height(hei);
	$("#loginname").focus();
	$("#mlogin").click(function(){
		var loginname = $("#loginname");
		var password = $("#password");
		var vcode =$("#validationCode");
		
		if(loginname.val() ==''){
			loginname.parent().find("p").html('* 请输入游戏账号！');
			return;
		}else{
			loginname.parent().find("p").html('&nbsp;');
		}
		if(password.val() ==''){
			password.parent().find("p").html('* 请输入登录密码！');
			return;
		}else{
			password.parent().find("p").html('&nbsp;');
		}
		
		if(vcode.val() ==''){
			vcode.parent().next().html('* 请输入验证码！');
			return;
		}else{
			vcode.parent().next().html('&nbsp;');
		}
		
		$('#mlogin').attr("disabled",true);
		$('#mlogin').val("登录中...");
		$.ajax({
			url : actionPath+"loginSubmit.do",
			async : true,
			type : "POST",
			data : {
				"username" : loginname.val(),
				"password" : password.val(),
				"rememberme" : false,
				"validationCode" : vcode.val()
			},
			dataType : "json",
			success : function(data) {
				$('#mlogin').attr("disabled",false);
				$.cookie("loginname",loginname.val());
				if (data.code == "1") {
					window.location.href = actionPath+"indexmp.do";
				} else if (data.code == "2") {
					$('#mlogin').val("登录");
					loginname.focus();
					loginname.parent().find("p").html(data.info);
				} else if(data.code == "3"){
					$('#mlogin').val("登录");
					password.focus();
					password.parent().find("p").html(data.info);
				} else if(data.code == "4"){
					$('#mlogin').val("登录");
					imgCode();
					vcode.focus();
					vcode.parent().next().html(data.info);
				}
			},
			error : function() {
				$('#mlogin').attr("disabled",false);
				$('#mlogin').val("登录");
				ui.msg('网络异常，请稍后再试!',3);
			}
		});
	});
})