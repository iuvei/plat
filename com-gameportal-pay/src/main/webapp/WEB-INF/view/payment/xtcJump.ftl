<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="GET" id="frm3">
		<input type="hidden" name="partner" value="${pay.parter}" /> <br/>
		<input type="hidden" name="banktype" value="${pay.banktype}" /> <br/>
		<input type="hidden" name="paymoney" value="${pay.paymoney}" /> <br/><br/>
		<input type="hidden" name="ordernumber" value="${pay.ordernumber}" /> <br/>
		<input type="hidden" name="callbackurl" value="${pay.callbackurl}" /> <br/>
		<input type="hidden" name="hrefbackurl" value="" /> <br/><br/>
		<input type="hidden" name="sign" value="${pay.sign}" /> <br/><br/>
		<script language="javascript">
	      	document.getElementById("frm3").submit();
	    </script>
	</form>
</body>
</html>