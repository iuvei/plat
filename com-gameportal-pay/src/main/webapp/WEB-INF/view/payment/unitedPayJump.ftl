<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>跳转中......</title>
</head>
<body style="background:url(/images/pay_loading.gif) no-repeat fixed center;">
	<form action="${submitUrl}" method="post" id="frm2">
		<input type="hidden" class="form-control" name="merchantCode" value="${pay.merchantCode}" />
		<input type="hidden" class="form-control" name="outUserId" value="${pay.outUserId}" />
		<input type="hidden" class="form-control" name="outOrderId" value="${pay.outOrderId}" />
		<input type="hidden" class="form-control" name="totalAmount" value="${pay.totalAmount}" />
		<input type="hidden" class="form-control" name="goodsName" value="${pay.goodsName}" />
		<input type="hidden" class="form-control" name="goodsDescription" value="${pay.goodsDescription}" />
		<input type="hidden" class="form-control" name="merchantOrderTime" value="${pay.merchantOrderTime}" />
		<input type="hidden" class="form-control" name="latestPayTime" value="${pay.latestPayTime}" />
		<input type="hidden" class="form-control" name="notifyUrl" value="${pay.notifyUrl}" />
		<input type="hidden" class="form-control" name="merUrl" value="${pay.merUrl}" />
		<input type="hidden" class="form-control" name="randomStr" value="${pay.randomStr}" />
		<input type="hidden" class="form-control" name="sign" value="${pay.sign}" />
		
      <script language="javascript">
      	document.getElementById("frm2").submit();
      </script>
    </form>
</body>