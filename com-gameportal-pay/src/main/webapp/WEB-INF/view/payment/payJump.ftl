<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm1">
      <input type="hidden" name="Mer_code" value="${pay.merCode}">
      <input type="hidden" name="Billno" value="${pay.billNo}">
      <input type="hidden" name="Amount" value="${pay.amount}" >
      <input type="hidden" name="Date" value="${pay.date}">
      <input type="hidden" name="Currency_Type" value="${pay.currencyType}">
      <input type="hidden" name="Gateway_Type" value="${pay.gatewayType}">
      <input type="hidden" name="Lang" value="${pay.lang}">
      <input type="hidden" name="Merchanturl" value="${pay.merchanturl}">
      <input type="hidden" name="FailUrl" value="${pay.failUrl}">
      <input type="hidden" name="ErrorUrl" value="${pay.failUrl}">
      <input type="hidden" name="Attach" value="${pay.attach}">
      <input type="hidden" name="OrderEncodeType" value="${pay.orderEncodeType}">
      <input type="hidden" name="RetEncodeType" value="${pay.retEncodeType}">
      <input type="hidden" name="Rettype" value="${pay.rettype}">
      <input type="hidden" name="ServerUrl" value="${pay.serverUrl}">
      <input type="hidden" name="SignMD5" value="${pay.signMD5}">
      <script language="javascript">
      	document.getElementById("frm1").submit();
      </script>
    </form>
</body>