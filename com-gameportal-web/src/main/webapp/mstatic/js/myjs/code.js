//刷新验证码
function imgCode(){
	$("#imgCode").attr("src",actionPath+"validationCode/pcrimg.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode2(){
	$("#imgCodeAgent").attr("src",actionPath+"validationCode/agentcode.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode3(){
	$("#imgCodeQr").attr("src",actionPath+"validationCode/qrcode.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode4(){
	$("#imgCodewx").attr("src",actionPath+"validationCode/wxcode.do?r="+(new Date()).getTime());
}