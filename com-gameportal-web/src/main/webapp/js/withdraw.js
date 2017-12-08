$(function(){
	/**
	var count = $.cookie("curCount");
	if(!isNaN(count) && 'undefined' !=count &&  $.cookie("curCount") !="0"){
		curCount = count;
		$("#getSmsCode").addClass("ui-disabled");
		InterValTimer = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次  
	}else{
		$.cookie("curCount",0);
	}
	*/
	
	$("#setAtmPwd_sub").click(function(){
		
		var atmpasswd = $("#atmpasswd");
		var atmpwdok = $("#atmpwdok");
		var vcode = $("#atmvcode");
		
		if(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/,/^\S{6,15}$/.test(atmpasswd.val()) == false){
			$("#atmpasswd_tip").html("提款密码格式不正确：密码长度6-15个字符,以及必须含有字母和数字的组合。");
			$("#atmpasswd_tip").removeClass("ui-none");
			atmpasswd.focus();
			return;
		}else{
			$("#atmpasswd_tip").addClass("ui-none");
			
		}
		if(atmpwdok.val() == '' || atmpwdok.val().length == 0){
			$("#atmpwdok_tip").html("请输入确认提款密码。");
			$("#atmpwdok_tip").removeClass("ui-none");
			atmpwdok.focus();
			return;
		}else{
			$("#atmpwdok_tip").addClass("ui-none");
			
		}
		if(atmpwdok.val() != atmpasswd.val()){
			$("#atmpwdok_tip").html("两次提款密码不一致，请检查后重新输入。");
			$("#atmpwdok_tip").removeClass("ui-none");
			atmpwdok.focus();
			return;
		}else{
			$("#atmpwdok_tip").addClass("ui-none");
			
		}
		if(vcode.val().length != 4){
			$("#atmvcode_tip").html("请输入4位数字验证码。");
			$("#atmvcode_tip").removeClass("ui-none");
			vcode.focus();
			return;
		}else{
			$("#atmvcode_tip").addClass("ui-none");
		}
		
		$("#setAtmPwd_sub").val('正在提交...');
		$("#setAtmPwd_sub").attr('disabled',true);
		$.ajax({
			url: actionPath+"manage/capital/setAtmPwd.do",
			async: true,
			type: 'POST',
			dataType : "json",
			data: {
				atmPwd : atmpasswd.val(),
				vcode : vcode.val()
			},
			success: function(data){
			 	if (data.code == "1") {
			 		$.remind(data.info,success_callback);
			 		//callBack(data.info,'${action_path}manage/capital/userCaseout.do');
				} else{
					$.msg(data.info,2);
					$("#setAtmPwd_sub").val('确认提款');
					$("#setAtmPwd_sub").attr('disabled',false);
				} 
			},
			error: function(){
				$.msg('网络异常，请稍后再试！',2);
				$("#setAtmPwd_sub").val('确认提款');
				$("#setAtmPwd_sub").attr('disabled',false);
			}	
		});
	});
	
	function success_callback(){
		window.location.reload();
	}
	
	
	$("#caseout_sub").click(function(){
		var caseoutPwd = $("#caseoutPwd");
		var caseoutFigure = $("#caseoutFigure");
		var caseoutBankname = $("#caseoutBankname");
		var caseoutBankcard = $("#caseoutBankcard");
		var caseoutBankperson = $("#caseoutBankperson");
		var caseoutBankfh = $("#caseoutBankfh");
		var alipayname = $("#alipayname");
		var alipay = $("#alipay");
		var way =$("#way").attr("data");
		var phone =$("#telphone");
		var vcode = $("#vcode");
		if(way =='1'){
			if(caseoutBankname.val() == '' || caseoutBankname.val().length==0){
				$("#caseoutBankname_tip").html("提款到帐银行不能为空。");
				$("#caseoutBankname_tip").removeClass("ui-none");
				return;
			}else{
				$("#caseoutBankname_tip").addClass("ui-none");
			}
			if(caseoutBankperson.val() == '' || caseoutBankperson.val().length==0){
				$("#caseoutBankperson_tip").html("账户姓名不能为空。");
				$("#caseoutBankperson_tip").removeClass("ui-none");
				return;
			}else{
				$("#caseoutBankperson_tip").addClass("ui-none");
			}
			if(caseoutBankcard.val() == '' || caseoutBankcard.val().length==0){
				$("#caseoutBankcard_tip").html("银行卡号不能为空。");
				$("#caseoutBankcard_tip").removeClass("ui-none");
				return;
			}else{
				$("#caseoutBankcard_tip").addClass("ui-none");
			}
			
	    }else{
	    	if(alipayname.val() == '' || alipayname.val().length==0){
				$("#alipayname_tip").html("支付宝真实姓名不能为空。");
				$("#alipayname_tip").removeClass("ui-none");
				return;
			}else{
				$("#alipayname_tip").addClass("ui-none");
			}
	    	if(alipay.val() == '' || alipay.val().length==0){
	    		$("#alipay_tip").html("支付宝账号不能为空。");
	    		$("#alipay_tip").removeClass("ui-none");
	    		return;
	    	}else{
	    		$("#alipay_tip").addClass("ui-none");
	    	}
	    }
		if(caseoutFigure.val() == '' || caseoutFigure.val().length == 0){
    		$("#caseoutFigure_tip").html("提款金额不能为空。");
    		$("#caseoutFigure_tip").removeClass("ui-none");
    		return;
		}else if(caseoutFigure.val() < 100){
			$("#caseoutFigure_tip").html("单笔提款不能少于100。");
			$("#caseoutFigure_tip").removeClass("ui-none");
			return;
    	}else{
    		$("#caseoutFigure_tip").addClass("ui-none");
    	}
		
		if(caseoutPwd.val() == '' || caseoutPwd.val().length == 0){
			$("#caseoutPwd_tip").html("请输入您的提款密码。");
			$("#caseoutPwd_tip").removeClass("ui-none");
			return;
    	}else{
    		$("#caseoutPwd_tip").addClass("ui-none");
    	}
		if(phone && phone.val() ==''){
			$("#phone_tip").html("请输入手机号码。");
			$("#phone_tip").removeClass("ui-none");
			return;
		}else{
			$("#phone_tip").addClass("ui-none");
		}
		if(vcode.val() == '' || vcode.val().length == 0){
			$("#vcode_tip").html("请输入验证码。");
			$("#vcode_tip").removeClass("ui-none");
			return;
		}else{
			$("#vcode_tip").addClass("ui-none");
		}
		
		$("#caseout_sub").val('正在提交...');
		$("#caseout_sub").attr('disabled',true);
		$.ajax({
			url: actionPath + "manage/capital/saveCaseout.do",
			async: true,
			type: 'POST',
			dataType : "json",
			data: {
				caseoutPwd : caseoutPwd.val(),
				caseoutFigure : caseoutFigure.val(),
				caseoutBankname : caseoutBankname.val(),
				caseoutBankcard : caseoutBankcard.val(),
				caseoutBankperson : caseoutBankperson.val(),
				caseoutBankfh : caseoutBankfh.val(),
				alipayname : alipayname.val(),
				alipay : alipay.val(),
				way : way,
				code : vcode.val()
			},
			success: function(data){
			 	if (data.code == "1") {
					$('#withdraw_content').addClass("ui-none");
					$('#showMwssage').removeClass("ui-none");
					imgCode2();
				} else if (data.code == "0" || data.code =="-1") {
					$.msg(data.info,2);
					$("#caseout_sub").val('确认提款');
					$("#caseout_sub").attr('disabled',false);
				}else if (data.code == "9") {
					$("#vcode_tip").html(data.info);
					$("#vcode_tip").removeClass("ui-none");
					imgCode2();
					$("#caseout_sub").val('确认提款');
					$("#caseout_sub").attr('disabled',false);
				}else if (data.code == "10") {
					$.msg('提款失败：'+data.info,2);
					caseoutFigure.focus();
					imgCode2();
					$("#caseout_sub").val('确认提款');
					$("#caseout_sub").attr('disabled',false);
				}else {
					$.msg('网络异常，请稍后再试！',2);
					$("#caseout_sub").val('确认提款');
					$("#caseout_sub").attr('disabled',false);
				}
			},
			error: function(){
				$.msg('网络异常，请稍后再试！',2);
				$("#caseout_sub").val('确认提款');
				$("#caseout_sub").attr('disabled',false);
			}	
		});	
	});
	
});

function payTypeChange(index){
	$(document).find("form").each(function(){
		$(this).addClass("ui-none");
	});
	$(document).find("form").eq(index-1).removeClass("ui-none");
}


var InterValTimer;
var totalCount = 120; //间隔函数，1秒执行  
var curCount; //当前剩余秒数  
function sendSms(){
	if($("#getSmsCode").hasClass("ui-disabled")){
		return ;
	}
	if($("#telphone").val() ==''){
		$("#telphone").focus();
		$.msg("请输入您的手机号码。", 3);
		return;
	}
	if(!/^1\d{10}$/.test($("#telphone").val())){
		$("#telphone").focus();
		$.msg('手机号码输入不正确。', 3);
		return;
	}
	//curCount =totalCount;
	//$("#getSmsCode").attr("disabled", "true");
	$("#getSmsCode").addClass("ui-disabled");
	 $.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/capital/getSmsCode.do",
            data: {phone: $("#telphone").val().trim()},  
            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
            success: function (data){ 
              if(data.code =='0'){
            	$.msg("短信验证码发送成功，请注意查收。",1);
              	//$.cookie("curCount",0);
          	    //设置button效果，开始计时  
              	//$("#getSmsCode").attr("disabled", "true");  
                //$("#getSmsCode").addClass("ui-disabled");
		        //$("#getSmsCode").html("请" + curCount + "秒后再试");  
		       // InterValTimer = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次  
              }else{
            	 //$("#getSmsCode").removeClass("disabled");
            	 $("#getSmsCode").removeClass("ui-disabled");
            	 $.msg(data.info, 3);
              }
            }  
    });  
}
function setRemainTime(){
	 if (curCount <= 0) {                  
        window.clearInterval(InterValTimer);//停止计时器  
        //$("#getSmsCode").removeAttr("disabled");//启用按钮  
        $("#getSmsCode").removeClass("ui-disabled");
        $("#getSmsCode").html("发送手机验证码");
    }  
    else {  
    	curCount--;
        $("#getSmsCode").html("请" + curCount + "秒后再试");
        $.cookie("curCount",curCount);
        $.cookie("InterValTimer",InterValTimer);
    } 
}