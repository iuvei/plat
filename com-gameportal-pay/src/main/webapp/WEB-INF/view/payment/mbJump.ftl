<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm7"><br>
		<input name='apiName' type='hidden' value= "${pay.apiName}"/><br>
		<input name='apiVersion' type='hidden' value= "${pay.apiVersion}"/><br>
		<input name='platformID' type='hidden' value= "${pay.platformID}"/><br>
		<input name='merchNo' type='hidden' value= "${pay.merchNo}"/><br>
		<input name='orderNo' type='hidden' value= "${pay.orderNo}"/><br>
		<input name='tradeDate' type='hidden' value= "${pay.tradeDate}" /><br>
		<input name='amt' type='hidden' value= "${pay.amt}" /><br>
		<input name='merchUrl' type='hidden' value= "${pay.merchUrl}"/><br>
		<input name='tradeSummary' type='hidden' value= "${pay.tradeSummary}"/><br>
		<input name='merchParam' type='hidden' value= "${pay.merchParam}"/><br>
		<input name='choosePayType' type='hidden' value= "${pay.choosePayType}"/><br>
		<input name='signMsg' type='hidden' value= "${pay.signMsg}"/><br>
		<script language="javascript">
	      	document.getElementById("frm7").submit();
	    </script>
	</form>
</body>
</html>
