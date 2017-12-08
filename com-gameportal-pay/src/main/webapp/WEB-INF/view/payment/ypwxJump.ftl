<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>微信扫码支付</title>
<link href="/css/zf_style.css" type="text/css" rel="stylesheet" />
<link href="/css/wechat_pay.css" rel="stylesheet" />
</head>
<body>
<div class="body">
	<h1 class="mod-title">
	<span class="ico-wechat"></span><span class="text">微信支付</span>
	</h1>
	<div class="mod-ct">
		<div class="order">
		</div>
		<div class="amount">
			<span>￥</span>${amount}
		</div>
		<div class="qr-image" id="showqrcode">
           <img  src="${code_img_url}" width="280px" height="260px" style="margin-left:10px; margin-top:20px" />
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
				<p style="color:red;">
					请使用手机登录微信扫一扫
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
