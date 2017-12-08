<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm3">
		<input type="hidden" name="apiCode" value="${pay.apiCode}" /> <br/>
		<input type="hidden" name="versionCode" value="${pay.versionCode}" /> <br/>
		<input type="hidden" name="inputCharset" value="${pay.inputCharset}" /> <br/><br/>
		<input type="hidden" name="signType" value="${pay.signType}" /> <br/>
		<input type="hidden" name="redirectURL" value="${pay.redirectURL}" /> <br/>
		<input type="hidden" name="notifyURL" value="${pay.notifyURL}" /> <br/><br/>
		<input type="hidden" name="partner" value="${pay.partner}" /> <br/>
		<input type="hidden" name="buyer" value="${pay.buyer}" /> <br/>
		<input type="hidden" name="buyerContactType" value="${pay.buyerContactType}" /> <br/>
		<input type="hidden" name="buyerContact" value="${pay.buyerContact}" /> <br/><br/>
		<input type="hidden" name="outOrderId" value="${pay.outOrderId}"/> <br/>
		<input type="hidden" name="amount" value="${pay.amount}" /> <br/>
		<input type="hidden" name="currency" value="${pay.currency}" /> <br/>
		<input type="hidden" name="paymentType" value="${pay.paymentType}"/> <br/>
		<input type="hidden" name="retryFalg" value="${pay.retryFalg}"/> <br/>
		<input type="hidden" name="submitTime" value="${pay.submitTime}" /> <br/>
		<input type="hidden" name="timeout" value="${pay.timeout}"/> <br/><br/>
		<input type="hidden" name="clientIP" value="${pay.clientIP}"/> <br/><br/>
		<input type="hidden" name="interfaceCode" value="${pay.interfaceCode}"/> <br/><br/>
		<input type="hidden" name="sign" value="${pay.sign}" /><br/>
		<script language="javascript">
	      	document.getElementById("frm3").submit();
	    </script>
	</form>
</body>
</html>
