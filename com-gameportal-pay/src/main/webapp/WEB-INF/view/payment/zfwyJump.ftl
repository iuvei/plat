<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm7"><br>
		<input type="hidden" name="sign" value="${pay.sign}" />
		<input type="hidden" name="merchant_code" value="${pay.merchantCode}" />
		<input type="hidden" name="service_type" value="${pay.serviceType}" />
		<input type="hidden" name="interface_version" value="${pay.interfaceVersion}" />
		<input type="hidden" name="input_charset" value="${pay.inputCharset}" />	
		<input type="hidden" name="notify_url" value="${pay.notifyUrl}"/>
		<input type="hidden" name="sign_type" value="${pay.signType}" />		
		<input type="hidden" name="order_no" value="${pay.orderNo}"/>
		<input type="hidden" name="order_time" value="${pay.orderTime}" />	
		<input type="hidden" name="order_amount" value="${pay.orderAmount}"/>
		<input type="hidden" name="product_name" value="${pay.productName}" />	
		<input type="hidden" name="return_url" value="${pay.returnUrl}"/>
		<input type="hidden" name="extend_param" value="${pay.extendParam}"/>		
		<input type="hidden" name="bank_code" value="" />
		<input type="hidden" name="redo_flag" value="${pay.redoFlag}"/>
		<script language="javascript">
	      	document.getElementById("frm7").submit();
	    </script>
	</form>
</body>
</html>
