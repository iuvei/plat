<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm7"><br>
		<input name='input_charset' type='hidden' value= "${pay.inputCharset}"/><br>
		<input name='inform_url' type='hidden' value= "${pay.informUrl}"/><br>
		<input name='return_url' type='hidden' value= "${pay.returnUrl}"/><br>
		<input name='pay_type' type='hidden' value= "${pay.payType}"/><br>
		<input name='bank_code' type='hidden' value= "${pay.bankCode}"/><br>
		<input name='merchant_code' type='hidden' value= "${pay.merchantCode}" /><br>
		<input name='order_no' type='hidden' value= "${pay.orderNo}" /><br>
		<input name='order_amount' type='hidden' value= "${pay.orderAmount}"/><br>
		<input name='order_time' type='hidden' value= "${pay.orderTime}"/><br>
		<input name='req_referer' type='hidden' value= "${pay.reqReferer}"/><br>
		<input name='customer_ip' type='hidden' value= "${pay.customerIp}"/><br>
		<input name='return_params' type='hidden' value= "${pay.returnParams}"/><br>
		<input name='sign' type='hidden' value= "${pay.sign}"/><br>
		<script language="javascript">
	      	document.getElementById("frm7").submit();
	    </script>
	</form>
</body>
</html>
