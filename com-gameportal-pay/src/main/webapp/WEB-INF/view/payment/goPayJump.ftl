<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="hidden/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm4">
		<input type="hidden" name="version" id="version" value="${pay.version}"/><br />
	 	<input type="hidden" name="charset" id="charset" value="${pay.charset}"/><br />
		<input type="hidden" name="language" id="language" value="${pay.language}"/><br />
	    <input type="hidden" name="signType" id="signType" value="${pay.signType}"/><br />
	    <input type="hidden" name="tranCode" id="tranCode" value="${pay.tranCode}"/><br />
	    <input type="hidden" name="merchantID" id="merchantID" value="${pay.merchantID}"/><br />
	    <input type="hidden" name="merOrderNum" id="merOrderNum" value="${pay.merOrderNum}"/><br />
	    <input type="hidden" name="tranAmt" id="tranAmt" value="${pay.tranAmt}"/><br />
	    <input type="hidden" name="feeAmt" id="feeAmt" value="${pay.feeAmt}"/><br />
	    <input type="hidden" name="currencyType" id="currencyType" value="${pay.currencyType}"/><br />
	    <input type="hidden" name="frontMerUrl" id="frontMerUrl" value="${pay.frontMerUrl}"/><br />
	    <input type="hidden" name="backgroundMerUrl" id="backgroundMerUrl" value="${pay.backgroundMerUrl}"/><br />
	    <input type="hidden" name="tranDateTime" id="tranDateTime" value="${pay.tranDateTime}"/><br />
	    <input type="hidden" name="virCardNoIn" id="virCardNoIn" value="${pay.virCardNoIn}"/><br />
	    <input type="hidden" name="tranIP" id="tranIP" value="${pay.tranIP}"/><br />
	    <input type="hidden" name="isRepeatSubmit" id="isRepeatSubmit" value="${pay.isRepeatSubmit}"/><br />
	    <input type="hidden" name="signValue" id="signValue" value="${pay.signValue}"/><br />
	    <input type="hidden" name="gopayServerTime" id="gopayServerTime" value="" /><br/>
		<script language="javascript">
	      	document.getElementById("frm4").submit();
	    </script>
	</form>
</body>
</html>
