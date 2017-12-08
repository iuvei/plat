<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${proxyobj.proxyaccount}-代理详情</title>
<link rel="stylesheet" href="/js/jquery/calendar.css" />
<style type="text/css">
body, form, p, ul, dl, input, textarea {
	margin: 0;
	padding: 0;
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
}
th,td {
	padding:.5em;
}
th {
	font-weight:bold;
	text-align:center;
}
</style>
</head>
<body>
<form id="proxyinfoFrom" name="proxyinfoFrom" action="/manage/proxy/proxyfrominfo.do">
<input type="hidden" name="id" value="${proxyuid}"/>
查询日期：<input type="text" name="startdate" calendar="YYYY-MM-DD" value="${startdate}"/> 至 <input type="text" name="enddate" calendar="YYYY-MM-DD" value="${enddate}"/> <button type="button" id="searchbtn">搜索</button>
</form>
<br>
<p>
	<table style="width:800px;" cellpadding="2" cellspacing="2" border="1" bordercolor="#999999">
		<tbody>
			<tr>
				<td colspan="4" style="background-color:#CCCCCC;">
					<strong>个人信息</strong><strong></strong><strong></strong><br />
				</td>
			</tr>
			<tr>
				<td style="width:100px;">
					代理帐号：
				</td>
				<td>
					${proxyobj.proxyaccount}
				</td>
				<td style="width:100px;">
					代理姓名：
				</td>
				<td>
					${proxyobj.proxyname}
				</td>
			</tr>
			<tr>
				<td>
					代理域名：
				</td>
				<td>
					${proxyobj.proxydomain}
				</td>
				<td>
					代理模式：
				</td>
				<td>
					<#if proxyobj.clearingtype==1>
						<font color="red">按月洗码</font>
					<#elseif proxyobj.clearingtype == 2>
						<font color="red">按天洗码</font>
					<#else>
						<font color="green">输值结算</font>
					</#if>
				</td>
			</tr>
			<tr>
				<td>
					占成比例：
				</td>
				<td>
					${proxyobj.returnscale}
				</td>
				<td>
					洗码比例：
				</td>
				<td>
					${proxyobj.ximascale}
				</td>
			</tr>
			<tr>
				<td>
					是否洗码：
				</td>
				<td>
					<#if proxyobj.ximaStatus == 1>
						<font color="green">是</font>
					<#else>
						<font color="red">否</font>
					</#if>
				</td>
				<td>
					下线总人数：
				</td>
				<td>
					${proxyobj.lowerUser}人
				</td>
			</tr>
			<tr>
				<td colspan="4" style="background-color:#CCCCCC;">
					<strong>推广数据</strong>（以下数据是 ${startdate} 至 ${enddate} 的数据）<br />
				</td>
			</tr>
			<tr>
				<td>
					注册人数：
				</td>
				<td>
					${proxyobj.lowecCount}人
				</td>
				<td>
					活跃人数：
				</td>
				<td>
					${proxyobj.activeUser}人
				</td>
			</tr>
			<tr>
				<td>
					首充人数：
				</td>
				<td>
					${proxyobj.fcCount}人
				</td>
				<td>
					首充/次充金额：
				</td>
				<td>
					${proxyobj.fcAmount}元/${proxyobj.fcTotalAmount}元（RMB）
				</td>
			</tr>
			<tr>
				<td>
					充值人数：
				</td>
				<td>
					${proxyobj.payusercount}人
				</td>
				<td>
					充值比数：
				</td>
				<td>
					${proxyobj.paycount}笔
				</td>
			</tr>
			<tr>
				<td>
					充值金额：
				</td>
				<td>
					${proxyobj.payAmountTotal}元（RMB）
				</td>
				<td>
					提款金额：
				</td>
				<td>
					${proxyobj.atmAmountTotal}元（RMB）
				</td>
			</tr>
			<tr>
				<td>
					提款人数：
				</td>
				<td>
					${proxyobj.atmusercount}人
				</td>
				<td>
					提款比数：
				</td>
				<td>
					${proxyobj.atmcount}笔
				</td>
			</tr>
			<tr>
				<td>
					扣款金额：
				</td>
				<td colspan="3">
					${proxyobj.buckleAmount}元（RMB）
				</td>
			</tr>
			<tr>
				<td colspan="4" style="background-color:#CCCCCC;">
					<strong>游戏数据</strong><br />
				</td>
			</tr>
			<tr>
				<td>
					注单数量：<br />
				</td>
				<td>
					${proxyobj.betTotel} 注
				</td>
				<td>
					总投注额：
				</td>
				<td>
					${proxyobj.betAmountTotal}元（RMB）
				</td>
			</tr>
			<tr>
				<td>
					有效投注额：<br />
				</td>
				<td>
					${proxyobj.validBetAmountTotal}元（RMB）
				</td>
				<td>
					总优惠：
				</td>
				<td>
					${proxyobj.preferentialTotal}元（RMB）
				</td>
			</tr>
			<tr>
				<td>
					总洗码：<br />
				</td>
				<td>
					${proxyobj.ximaAmountTotal}元（RMB）
				</td>
				<td>
					游戏输赢：
				</td>
				<td>
					<#assign winloss=proxyobj.winlossTotal?number/>
					<#if winloss gt 0>
						<font color="green">${proxyobj.winlossTotal}元（RMB）</font>
					<#else>
						<font color="red">${proxyobj.winlossTotal}元（RMB）</font>
					</#if>
				</td>
			</tr>
			<tr>
				<td>
					累计负盈利：
				</td>
				<td colspan="3">
					<font color="red">${proxyobj.recordAmount}元（RMB）</font>
				</td>
			</tr>
			<tr>
				<td>
					实际盈亏：
				</td>
				<td>
					<#assign realyk=proxyobj.realPLs?number/>
					<#if realyk gt 0>
						<font color="green">${realyk}元（RMB）</font>
					<#else>
						<font color="red">${realyk}元（RMB）</font>
					</#if>
				</td>
				<td colspan="2">
					小提示：实际盈亏计算公式->实际盈亏 = 游戏输赢-(总洗码+总优惠)
				</td>
			</tr>
			<tr>
				<td>
					占成佣金：
				</td>
				<td>
					<#assign commission=proxyobj.commissionAmount?number/>
					<#if commission gt 0>
						<font color="green">${proxyobj.commissionAmount}元（RMB）</font>
					<#else>
						<font color="red">${proxyobj.commissionAmount}元（RMB）</font>
					</#if>
				</td>
				<td colspan="2">
					小提示：佣金计算公式->占成佣金 = 实际盈亏*占成比例
				</td>
			</tr>
		</tbody>
	</table>
</p>
<script src="/js/jquery/jquery-1.8.3.js"></script>
<script src="/js/jquery/calendar.js"></script>
<script type="text/javascript">
$(function(){
	$('#searchbtn').click(function(){
		$('#searchbtn').text('正在搜索...');
		$('#searchbtn').attr('disabled',true);
		proxyinfoFrom.submit();
	});
});
</script>
</body>
</html>