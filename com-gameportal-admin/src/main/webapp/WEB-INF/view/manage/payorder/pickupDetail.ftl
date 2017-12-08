<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>钱宝娱乐-会员出纳柜台</title>
<style type="text/css">
body, form, p, ul, dl, input {
	margin: 0;
	padding: 0;
	height: 20px
}
body {
	background: #f7f7f7;
	color: #333;
	font: 12px/18px "sans serif",tahoma,verdana,helvetica;
	margin: 10px;
}
div {
	margin: auto;
}
table{
	border-collapse:collapse;
	border-spacing:0;
	font-size:inherit;
	font:100%;
	margin-bottom:1em;
	margin:0 auto;
}
th,td {
	padding:.5em;
}
th {
	font-weight:bold;
	text-align:center;
}
button{
	margin-left:120px;
}
#queryBalance{
color:red;
}
</style>
</head>
<body>
	<input type="hidden" id="poid" value="${payOrder.poid}" />
	<table style="width:500px;" cellpadding="2" cellspacing="2" border="1" bordercolor="#999999">
		<tbody>
			<tr>
				<td colspan="4" style="background-color:#CCCCCC;">
					<strong>出款订单详情</strong>
				</td>
			</tr>
			<tr>
				<td width="80">
					订单编号：
				</td>
				<td>
					${payOrder.poid}
				</td>
			</tr>
			<tr>
				<td>
					会员账号：
				</td>
				<td>
					${payOrder.uaccount}
				</td>
			</tr>
			<tr>
				<td>
					会员姓名：
				</td>
				<td>
					${payOrder.openname}
				</td>
			</tr>
			<tr>
				<td>
					银行卡名称：
				</td>
				<td>
					${payOrder.bankname}
				</td>
			</tr>
			<tr>
				<td>
					银行卡号：
				</td>
				<td>
					${payOrder.bankcard}
				</td>
			</tr>
			<tr>
				<td>
					出款金额：
				</td>
				<td>
					<font color="red">${payOrder.amount}</font>&nbsp;元
				</td>
			</tr>
			<tr>
				<td>
					订单状态：
				</td>
				<td>
					<#if payOrder.status==1>
						<font color="red">发起</font>
					<#elseif payOrder.status==2>
						<font color="red">待出款</font>
					<#elseif payOrder.status==3>
						<font color="red">出款成功</font>
					</#if>
				</td>
			</tr>
			<tr>
				<td>
					出款口令：
				</td>
				<td>
					<input type="password" id="vcode"/> 
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<#if payOrder.status==2>
						<button type="button" id="withdrawlbtn">点击出款</button>
					</#if>
					<button type="button" id="withdrawlbalancebtn">查询余额</button>
					<span id="queryBalance"></span>
				</td>
			</tr>
		</tbody>
		
	</table>
<script src="/js/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript">
$(function(){
	$('#withdrawlbtn').click(function(){
		if($("#vcode").val() ==''){
			alert('请输入出款口令。');
			$("#vcode").focus();
			return;
		}
		$("#withdrawlbtn").text('出款中...');
		if($(this).hasClass('clickme')){
			return;
		}
		$("#withdrawlbtn").addClass("clickme");
		$.ajax({
			url : "/manage/pickuporder/customerWithdrawl.do?r="+Math.random(),
			async : true,
			type : "POST",
			data:{vcode:$("#vcode").val(),poid:$("#poid").val()},
			dataType : "json",
			success : function(data) {
				data =eval('('+data+')');
				$("#withdrawlbtn").removeClass("clickme");
				if (data.success) {
					$("#queryBalance").text(data.balance);
					$("#withdrawlbtn").text('点击出款');
				}else {
					alert(data.msg);
					$("#withdrawlbtn").text('点击出款');
				}
			},
			error : function() {
				alert("网络异常，请稍后再试。");
				$("#withdrawlbtn").text('点击出款');
				$("#withdrawlbtn").removeClass("clickme");
			}
		});
	});
	
	$('#withdrawlbalancebtn').click(function(){
		$("#withdrawlbalancebtn").text('查询中...');
		if($(this).hasClass('clickme')){
			return;
		}
		$("#withdrawlbalancebtn").addClass("clickme");
		$.ajax({
			url : "/manage/pickuporder/getWithdrawlBalance.do?r="+Math.random(),
			async : true,
			type : "POST",
			dataType : "json",
			success : function(data) {
				data =eval('('+data+')');
				$("#withdrawlbalancebtn").removeClass("clickme");
				if (data.result) {
					$("#queryBalance").text(data.balance);
					$("#withdrawlbalancebtn").text('查询余额');
				}else {
					alert(data.msg);
					$("#withdrawlbalancebtn").text('查询余额');
				}
			},
			error : function() {
				alert("网络异常，请稍后再试。");
				$("#withdrawlbalancebtn").text('查询余额');
				$("#withdrawlbalancebtn").removeClass("clickme");
			}
		});
	});
});
</script>
</body>
</html>