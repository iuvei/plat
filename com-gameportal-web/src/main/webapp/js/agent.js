var agent = function() {
	return {
		register : function() {
			var truename = $("#name");
			var tel = $("#phone");
			var qqskype = $("#qqskype");
			var email = $("#email");
			var spreadfs = $("#cphContent_cphContent_ddlType").find("option:selected").text();
			var code = $("#vcode");
			if (truename.val() == "") {
				$.msg("真实姓名不能为空！",3);
				truename.focus();
				return;
			}
			if(email.val() ==''){
				$.msg("E-mail邮箱不能为空！",3);
				email.focus();
				return;
			}else if(!(/^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/.test(email.val()))){
				$.msg("E-mail输入不正确！",3);
				email.focus();
				return;
			}
			
			if(qqskype.val() ==''){
				$.msg("qskype不能为空！",3);
				qqskype.focus();
				return;
			}
			
			if(tel.val() ==''){
				$.msg("手机号不能为空。",3);
				tel.focus();
				return;
			}
			if(!/^1\d{10}$/.test(tel.val())){
				$.msg("手机号输入不正确！",3);
				tel.focus();
				return;
			}
			if(code.val().length != 4){
				$.msg("请输入4位数字验证码!",3);
				code.focus();
				return;
			}
			$("#submit").val('正在提交...');
			$("#submit").attr('disabled',true);
			var proxy = {
				trueName : truename.val().trim(),
				telphone : tel.val().trim(),
				qqskype : qqskype.val().trim(),
				aemail : email.val().trim(),
				spreadfs : spreadfs,
				code : code.val().trim()
			};
			$.ajax({
				type : "POST",
				url : "/proxy/register.do",
				data : proxy,
				async : true,
				dataType : "json",
				success : function(data) {
					if(data.code ==0){
						$.msg("代理账号申请成功，市场部代理专员会在2小时内联系您。",1);
						truename.val('');
						tel.val('');
						qqskype.val('');
						email.val('');
						//spreadfs.val('');
						code.val('');
						$("#submit").val('提交申请');
						$("#submit").attr('disabled',false);
						imgCode2();
						//$("#vcode").attr("src","/validationCode/pcrimg.do?r="+(new Date()).getTime());
					}else{
						$.msg(data.info,3);
						$("#submit").val('提交申请');
						$("#submit").attr('disabled',false);
					}
				},
				error : function() {
					$.msg('网络异常，请稍后再试！',2);
					$("#submit").val('提交申请');
					$("#submit").attr('disabled',false);
				}
			});
		}
	}
}();
$(function() {
	$("#submit").click(function() {
		agent.register();
	});
})