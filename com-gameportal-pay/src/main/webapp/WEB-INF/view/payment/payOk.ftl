<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>收银台- 支付成功</title>
<style>
*{
margin: 0px auto;
}
</style>
</head>
<body style="margin: 0px auto;">
	<form action="/manage/capital/userDeposit.do" method="post" id="frm1">
	 	<div style="width:100%;height:100%;text-align:center;margin-top:10%;">
        	<div>
        		<table width="200px" border="0" cellpadding="0" cellspacing="0" align="center">
        			<tr>
        				<td><label><img src="/images/g.png"/></label></td>
        				<td align="left"><label style="color:#006d04;font-size:16px;font-weight:bold;">该笔订单支付成功！</label></td>
        			</tr>
        		</table>
            </div>
            <div style="border:1px solid gray;width: 450px; height: 200px;margin-top:5px;background-color:#ffffdd;">
               <table width="100%" border="0" cellpadding="0" cellspacing="5">
               	<tr><td align="left" valign="middle" height="30"colspan="2">以下是您的支付信息：</td></tr>
                <tr>
                 	<td align="left" valign="middle" height="30" width="20%">订单编号：</td>
                 	<td align="left" valign="middle" height="30">${order.platformorders}</td>
                 </tr>
                  <tr>
                 	<td align="left" valign="middle" height="30" width="20%">创建日期：</td>
                 	<td align="left" valign="middle" height="30">${order.createDate?date}</td>
                  </tr>
                  <tr>
                	 <td align="left" valign="middle" height="30" width="20%">订单金额：</td>
                 	<td align="left" valign="middle" height="30"><font color="red">${order.amount?string("currency")}元</font></td>
                 </tr>
               </table>
               </div>
            </div>
        </div>
    </form>
</body>