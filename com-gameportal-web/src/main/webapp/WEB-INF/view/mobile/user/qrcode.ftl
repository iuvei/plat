<!DOCTYPE html>
<html>
<head>
    <#include "/common/config.ftl">
	${meta}
    <title>${title}--我的推荐二维码</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta name="format-detection" content="telephone=no"/>
    <link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/public/base.css"/>
	<script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
	<script type="text/javascript" src="${zy_path}static/js/jquery.qrcode.min.js"></script>
    <script type="text/javascript" src="${zy_path}mstatic/js/public/base.min.js"></script>
<script type="text/javascript">
$(function(){
	var str = "http://sea51.com/registermp.html?aff=${userInfo.uiid}";
	$('#code').qrcode(str);
	
	$("#sub_btn").click(function(){
		$("#code").empty();
		var str = toUtf8($("#mytxt").val());
		
		$("#code").qrcode({
			render: "table",
			width: 60,
			height:60,
			text: str
		});
	});
})
function toUtf8(str) {   
    var out, i, len, c;   
    out = "";   
    len = str.length;   
    for(i = 0; i < len; i++) {   
    	c = str.charCodeAt(i);   
    	if ((c >= 0x0001) && (c <= 0x007F)) {   
        	out += str.charAt(i);   
    	} else if (c > 0x07FF) {   
        	out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));   
        	out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));   
        	out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
    	} else {   
        	out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));   
        	out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
    	}   
    }   
    return out;   
}  
</script>
</head>
<body class=" ft-normal">
    <header id="header"></header>
    <div id="code" style="text-align: center;margin-top: 15px;"></div>
    <p class="_plr30 color-999 ft-26 _mt10 lh-s t-center">1.恭喜，您也拥有自己的二维码了喔。</p>
    <p class="_plr30 color-999 ft-26 _mt10 lh-s t-center">2.快速截屏发给您的好友，扫码后可以注册了喔。</p>
</body>
</html>