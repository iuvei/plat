<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>QQ钱包扫码支付</title>
<link href="/css/zf_style.css" type="text/css" rel="stylesheet" />
<link href="/css/wechat_pay.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery-1.8.0.js"></script>
<script type="text/javascript" src="/js/jquery.qrcode.js"></script>
<script type="text/javascript" src="/js/utf.js"></script>
</head>
<body>
<div class="body">
	<h1 class="mod-title">
	<span class="ico-wechat"></span><span class="text">QQ钱包支付</span>
	</h1>
	<div class="mod-ct">
		<div class="order">
		</div>
		<div class="amount">
			<span>￥</span>${amount}
		</div>
		<div class="qr-image" id="showqrcode">
           <img  src="" width="280px" height="260px" style="margin-left:10px; margin-top:20px" />
		</div>
		<!--detail-open 加上这个类是展示订单信息，不加不展示-->
		<div class="detail detail-open" id="orderDetail" style="">
			<dl class="detail-ct" style="display: block;">				
				<dt>交易单号</dt>
				<dd id="billId">${orderNO}</dd>
			</dl>			
		</div>
		<div class="tip">
			<span class="dec dec-left"></span>
			<span class="dec dec-right"></span>
			<div class="ico-scan">
			</div>
			<div class="tip-text">
				<p>
					请使用QQ钱包扫一扫
				</p>
				<p>
					扫描二维码完成支付
				</p>
			</div>
		</div>
	</div>
	<div class="foot">
		<div class="inner">
			<p> 
				Copyright @ 工业和信息化部备案号: 黔ICP备16006408号-1 增值电信： 黔B2-20160068
			</p>
		</div>
	</div>
</div>
</body>
</html>
<script>
$(function(){
	sQrcode('${code_img_url}');
});

function sQrcode(qdata){
	if(qdata ==''){
		$("#showqrcode").empty().html("通道维护中,请采用其他支付通道进行充值！");
	}else{
		$("#showqrcode").empty().qrcode({		// 调用qQcode生成二维码
				render : "canvas",    			// 设置渲染方式，有table和canvas，使用canvas方式渲染性能相对来说比较好
				text : qdata,    				// 扫描了二维码后的内容显示,在这里也可以直接填一个网址或支付链接
				width : "200",              	// 二维码的宽度
				height : "200",             	// 二维码的高度
				background : "#ffffff",     	// 二维码的后景色
				foreground : "#000000",     	// 二维码的前景色
				src: ""    	// 二维码中间的图片
		});	
	}
}
</script>
