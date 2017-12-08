<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>支付宝扫码支付</title>
<link href="/css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div class="sweep">
   <div class="wrap">
      <div class="h100" id="res">
         <div class="m26" style="font-size:14px;">
               <h1><div id="msg">订单提交成功，请您尽快付款！</div></h1>
               <div class="num"><span>商户订单号:${jf.outTradeNo}</span>
               <br />
               <span>商品信息:<font  color=#ff6400 style="font-size:15px;"></font></span>
               <span class="color1 ml16">请您在提交订单后 <span class="orange">10分钟</span> 内完成支付，否则订单会自动取消。</span></div>
         </div>
      </div>
<!--订单信息代码结束-->
<!--扫描代码-->
      <div class="s-con" id="codem">
         <div class="title"><span class="blue" style="font-size:20px;"><span>应付金额：</span><span class="orange">${jf.amount}</span> 元
		 <br><span style="font-size:12px;">此交易委托支付宝（中国）网络技术有限公司代收款</span></div>
         <div class="scan_zfb"><img src="${imageqr}" /></div>
         <div class="question">
            <div class="new"></div>
         </div>
      </div>
<!--扫描代码结束-->
<!--底部代码-->
      <div class="s-foot">支付网关版权所有 2012-2017</div>

<a logo_size="83x30"  logo_type="realname"  href="http://www.anquan.org" ></a>
<a id="_pingansec_bottomimagesmall_shiming" href="http://si.trustutn.org/info?sn=377161209026042948560&certType=1"><img src="http://v.trustutn.org/images/cert/bottom_small_img.png"/></a>
<!--底部代码结束-->
   </div>
</div>
</body>
</html>
