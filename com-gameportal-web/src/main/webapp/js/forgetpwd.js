var pwdInterValTimer;
var pwdtotalCount = 120; //间隔函数，1秒执行  
var pwdCount; //当前剩余秒数  
var actionPath="/";
var userInfo=function(){
	return{
		validInfo:function(){
			var account = $("#account");
			var telphone =$("#telphone");
			var code = $("#vcode");
			if(account.val() ==''){
				account.focus();
				$("#account_tip").html("请输入用户名!").css("color","red");
				return false;
			}else if(!(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/,/^\S{6,8}$/.test(account.val()))){
				account.focus();
				$("#account_tip").html("6-8个字符,仅限含有字母和数字的组合!").css("color","red");
				return false;
			}else{
				$("#account_tip").html("验证通过!").css("color","green");
			}
			if(telphone.val() ==''){
				telphone.focus();
				$("#telphone_tip").html('请输入联系电话!').css("color","red");
				return false;
			}else if(!(/^1\d{10}$/.test(telphone.val()))){
				$("#telphone_tip").html('手机号码输入不正确!').css("color","red");
				telphone.focus();
				return false;
			}else{
				$("#telphone_tip").html("验证通过!").css("color","green");
			}
			
			if(code.val().length != 4){
				$("#vcode_tip").html("请输入4位数字验证码!").css("color","red");
				code.focus();
				return false;
			}else{
				$("#vcode_tip").html("验证通过!").css("color","green");
			}
			return true;
		}
	}
}();
function getPwd(){
	if(userInfo.validInfo()){
		$("#LbtnSubmit").html('正在提交...');
		$("#LbtnSubmit").addClass("ui-disabled").attr('disabled',true);
		var user ={
				account:$("#account").val().trim(),
				phone:$("#telphone").val().trim(),
				key:$("#vcode").val().trim()
		};
		//pwdCount =pwdtotalCount;
		$.ajax({
			type : "POST",
			url : actionPath + "resetLoginPwd.do",
			data : user,
			async : true,
			dataType : "json",
			success : function(data) {
				if(data.code ==0){
					alert("找回密码成功！新的登录密码稍后将以短信方式发送给您，请注意查收!");
					//pwdInterValTimer = window.setInterval(calcTime, 1000); //启动计时器，1秒执行一次  
					$("#account").val('');
					$("#telphone").val('');
					$("#vcode").val('');
					$("#vcode","#j-modal-forgetpwd").click();
					$("#LbtnSubmit").addClass("ui-disabled").attr('disabled',true);
					$("#LbtnSubmit").val('找回密码');
					$(".close").click();
				}else{
					alert(data.info);
					$("#LbtnSubmit").val('找回密码');
					$("#LbtnSubmit").removeClass("ui-disabled").attr('disabled',false);//启用按钮  
				}
			},
			error : function() {
				alert('网络异常，请稍后再试！');
				$("#LbtnSubmit").val('找回密码');
				$("#LbtnSubmit").removeClass("ui-disabled").attr('disabled',false);//启用按钮  
			}
		});
	}
}
function calcTime(){
	if (pwdCount <= 0) {                  
        window.clearInterval(pwdInterValTimer);//停止计时器  
        $("#LbtnSubmit").removeClass("ui-disabled").attr('disabled',false);//启用按钮  
        $("#_tip").text("温馨提示：新密码将以短信方式发送给您，如果您的预留手机号无法接收短信，请联系在线客服！");
    }  
    else {  
    	pwdCount--;
    	 $("#_tip").text("请在" + pwdCount + "秒后再进行密码找回操作。");
        $.cookie("pwdCount",pwdCount);
        $.cookie("pwdInterValTimer",pwdInterValTimer);
        $("#LbtnSubmit").addClass("ui-disabled").attr('disabled',true);
    } 
}

$(function(){
	/**
	pwdCount =  $.cookie("pwdCount");
	if(!isNaN(pwdCount) && 'undefined' !=pwdCount &&  $.cookie("pwdCount") !="0" &&  $.cookie("pwdCount") != null){
		//设置button效果，开始计时  
		$("#LbtnSubmit").addClass("ui-disabled").attr('disabled',true);
        $("#_tip").text("请在" + pwdCount + "秒后在进行密码找回操作。");
        window.clearInterval($.cookie("pwdInterValTimer"));  
        pwdInterValTimer = window.setInterval(calcTime, 1000); //启动计时器，1秒执行一次 
    }else{
      $.cookie("pwdCount",0);
      $("#LbtnSubmit").removeClass("ui-disabled").attr('disabled',false);
      //$("._tip").text('');
    }
    */
})