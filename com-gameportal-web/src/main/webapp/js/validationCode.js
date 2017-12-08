function checkValidationCode(){
	var validationCode = $("#vcode").val();
	$.ajax({
		url : actionPath+"validationCode/validform.do?"+(new Date()).getTime(),
		async : true,
		type : "GET",
		data : {
			"param" : validationCode.trim(),
			"isLogin" : false
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "y") {
				$("#vcodeTip").html('<font color="#006d04">'+data.info+'</font>');
			}else {
				$("#vcodeTip").html('<font color="red">'+data.info+'</font>');
			}
		},
		error : function() {
			alert("网络异常，请稍后再试！e");
		}
	});
}
$(function() {
	$("#vcode").keyup(function(){
	    if($("#vcode").val().length == 4){
	    	checkValidationCode();
	    }else{
	    	jQuery("#vcodeTip").html('请输入验证码。');
	    }
	});
})