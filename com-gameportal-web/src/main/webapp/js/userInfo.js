var InterValTimer;
var totalCount = 120; //间隔函数，1秒执行  
var currentCount; //当前剩余秒数  
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
	//currentCount =totalCount;
	//$("#getSmsCode").attr("disabled", "true");
	 $("#getSmsCode").addClass("ui-disabled");
	 $.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/user/sendSmsCode.do",
            data: {phone: $("#telphone").val().trim()},  
            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
            success: function (data){ 
              if(data.code =='1'){
            	$.msg("短信验证码发送成功，请注意查收。",1);
              	//$.cookie("currentCount",0);
          	    //设置button效果，开始计时  
              	//$("#getSmsCode").attr("disabled", "true");  
            	 $("#getSmsCode").removeClass("ui-disabled");
		        //$("#getSmsCode").html("请" + currentCount + "秒后再试！");  
		        //InterValTimer = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次  
              }else{
            	 //$("#getSmsCode").removeClass("disabled");
            	 $("#getSmsCode").removeClass("ui-disabled");
            	 $.msg(data.info, 3);
              }
            }  
    });  
}
function setRemainTime(){
	 if (currentCount == 0) {                  
        window.clearInterval(InterValTimer);//停止计时器  
        //$("#getSmsCode").removeAttr("disabled");//启用按钮  
        $("#getSmsCode").removeClass("ui-disabled");
        $("#getSmsCode").html("重新发送手机验证码");
    }  
    else {  
    	currentCount--;
        $("#getSmsCode").html("请" + currentCount + "秒后再试！");
        $.cookie("currentCount",currentCount);
        $.cookie("InterValTimer",InterValTimer);
    } 
}
function verificationPhone(){
	if($("#verifyPhone").hasClass("ui-disabled")){
		return;
	}
	 //$("#verifyPhone").attr("disabled", "true");  
	 $("#verifyPhone").addClass("ui-disabled");
	 if($("#vcode").val() !='' && $("#vcode").val() !=undefined){
	   $.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/user/verificationPhone.do",
            data: {phone: $("#telphone").val().trim(),vcode:$("#vcode").val().trim()},  
            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
            success: function (data){ 
              if(data.code =='1'){
            	$.cookie("currentCount",0);
            	$.confirm(data.info,callBack_ok);
              }else{
            	  $.msg(data.info, 3);
            	  $("#verifyPhone").removeClass("ui-disabled");
                 //$("#verifyPhone").removeAttr("disabled");
              }
            }  
	   });
   }else{
	   $("#vcode").focus();
	   $.msg("请输入手机短信验证码。", 3);
	   $("#verifyPhone").removeClass("ui-disabled");
	   return;
   }
}

function callBack_ok(){
	location.href = actionPath+"manage/user/verfityInfo.do";
//	location.reload();
}

$(function(){
	/**
	currentCount =  $.cookie("currentCount");
	if(!isNaN(currentCount) && 'undefined' !=currentCount &&  $.cookie("currentCount") > "0"){
		//设置button效果，开始计时  
        //$("#getSmsCode").attr("disabled", "true");  
        $("#getSmsCode").addClass("ui-disabled");
        $("#getSmsCode").html("请" + currentCount + "秒后再试！");
        window.clearInterval($.cookie("InterValTimer"));  
        InterValTimer = window.setInterval(setRemainTime, 1000); //启动计时器，1秒执行一次 
    }else{
      $.cookie("currentCount",0);
      $("#getSmsCode").html("发送手机验证码");
    }
    */
	$("#verifyEmail").click(function(){
		/**
		if($("#phonevalid").val() !='1'){
			$.msg("您的手机号码还没有验证通过，不能验证邮箱。", 3);
			return;
		}
		*/
		if($("#verifyEmail").hasClass("ui-disabled")){
			return ;
		}
		if($("#email").val() ==''){
			$("#email").focus();
			$.msg("请输入您的 E-mail邮箱。", 3);
			return;
		}
		if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+\.([a-zA-Z])+$/.test($("#email").val())){
			$("#email").focus();
			$.msg("请输入您的正确的 E-mail邮箱。", 3);
			return;
		}
		//$("#verifyEmail").attr("disabled", "true");  
		$("#verifyEmail").addClass("ui-disabled");  
		  $.ajax({  
	            type: "POST", 
	            dataType: "json",  
	            url: actionPath+"mail/save.do",
	            data: {email: $("#email").val().trim()},  
	            error: function (XMLHttpRequest, textStatus, errorThrown) { },  
	            success: function (data){ 
	              if(data.code =='1'){
	            	$.confirm(data.info,callBack_ok);
	              }else{
	            	 $.msg(data.info, 3);
	                 //$("#verifyEmail").removeAttr("disabled");
	              }
	              $("#verifyEmail").removeClass("ui-disabled");  
	            }  
		   });
	});
	
	$("#refresh").click(function(){
		$.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/refresh.do",
            error: function () { },  
            success: function (data){ 
             $("#reward").text(data.rewardCount);
            }  
	   });
	});
	
	
	$("#activityId").click(function(){
		$(this).html("<b>正在申请礼金...</b>");
		$(this).attr("disabled", "true");
		$.ajax({  
            type: "POST", 
            dataType: "json",  
            url: actionPath+"manage/activityReward.do",
            error: function () { },  
            success: function (result){ 
            	$("#activityId").removeAttr("disabled");
            	if(result.success){
            		alert(result.msg);
            		$("#activityId").hide();
            		 // 刷新玩家余额。
            		$("#accountMoney").text(result.money);
            		$("#cphContent_lblTotalCredit").text(result.money);
            	}else{
            		alert(result.msg);
            		$("#activityId").html("<b>领取礼金</b>");
            	}
            }  
	   });
	});
	
});