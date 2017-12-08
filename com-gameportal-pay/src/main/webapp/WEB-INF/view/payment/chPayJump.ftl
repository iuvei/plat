<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm4">
		<input type="hidden" name="p0_Cmd" id="p0_Cmd" value="${pay.p0cmd}"/><br />
	 	<input type="hidden" name="p1_MerId" id="p1_MerId" value="${pay.p1MerId}"/><br />
		<input type="hidden" name="p2_Order" id="p2_Order" value="${pay.p2Order}"/><br />
	    <input type="hidden" name="p3_Cur" id="p3_Cur" value="${pay.p3Cur}"/><br />
	    <input type="hidden" name="p4_Amt" id="p4_Amt" value="${pay.p4Amt}"/><br />
	    <input type="hidden" name="p8_Url" id="p8_Url" value="${pay.p8Url}"/><br />
	    <input type="hidden" name="p9_MP" id="p9_MP"  value="${pay.p9MP}"/><br />
	    <input type="hidden" name="pa_FrpId" id="pa_FrpId" value="${pay.paFrpId}"/><br />
	    <input type="hidden" name="pg_BankCode" id="pg_BankCode" value="${pay.pgBankCode}"/><br />
	    <input type="hidden" name="pi_Url" id="pi_Url" value="${pay.piUrl}"/><br />
	    <input type="hidden" name="hmac" id="hmac" value="${pay.hmac}"/><br />
		<script language="javascript">
	      	document.getElementById("frm4").submit();
	    </script>
	</form>
</body>
</html>
