<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm5"><br>
		<input name='MemberID' type='hidden' value= "${pay.memberID}"/><br>
		<input name='TerminalID' type='hidden' value= "${pay.terminalID}"/><br>
		<input name='InterfaceVersion' type='hidden' value= "${pay.interfaceVersion}"/><br>
		<input name='KeyType' type='hidden' value= "${pay.keyType}"/><br>
		<input name='PayID' type='hidden' value= "${pay.payID}"/><br>
		<input name='TradeDate' type='hidden' value= "${pay.tradeDate}" /><br>
		<input name='TransID' type='hidden' value= "${pay.transID}" /><br>
		<input name='OrderMoney' type='hidden' value= "${pay.orderMoney}"/><br>
		<input name='PageUrl' type='hidden' value= "${pay.pageUrl}"/><br>
		<input name='ReturnUrl' type='hidden' value= "${pay.returnUrl}"/>	<br>
		<input name='Signature' type='hidden' value="${pay.signature}"/> <br>
		<input name='NoticeType' type='hidden' value= "${pay.noticeType}"/> <br>
		<script language="javascript">
	      	document.getElementById("frm5").submit();
	    </script>
	</form>
</body>
</html>
