<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link href="/style/mystyle.css" rel="stylesheet" type="text/css" />
<title>${fkMember.account}-会员详情</title>
</head>
<body>
<div class="pd-20">
	<table class="table table-border table-bordered table-bg mt-5">
	    <thead>
	      <tr>
	        <th colspan="8" scope="col" class="green">会员基本信息</th>
	      </tr>
	      <tr class="text-c">
	        <th>会员账号</th>
	        <th>用户名</th>
	        <th>会员类型</th>
	        <th>出生日期</th>
	        <th>等级</th>
	        <th>电话</th>
	        <th>邮箱</th>
	        <th>QQ</th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr class="text-c">
	        <td>${fkMember.account}</td>
	        <td>${fkMember.uname}</td>
	        <td>
	        	<#if fkMember.accounttype==0>
	        		<span style="color:blue;">普通会员</span>
	        	</#if>
	        	<#if fkMember.accounttype==1>
	        		<span style="color:red;">代理会员</span>
	        	</#if>
	        	<#if fkMember.accounttype==2>
	        		<span>信用代理</span>
	        	</#if>
	        	<#if fkMember.accounttype==3>
	        		<span style="color:green;">信用会员</span>
	        	</#if>
	        </td>
	        <td>${fkMember.birthday}</td>
	        <td>${fkMember.grade}级</td>
	        <#if !showFlag>
		        <td><#if fkMember.phone??>${fkMember.phone?substring(0,3)}****${fkMember.phone?substring(7)}&nbsp;</#if>(<#if fkMember.phonevalid ==1><font color="green">已认证</font><#else><font color="red">未认证</font></#if>)</td>
		        <td><#if fkMember.email?? && fkMember.email?length gt 4> *****${fkMember.email[4..]}&nbsp;</#if>(<#if fkMember.emailvalid ==1><font color="green">已认证</font><#else><font color="red">未认证</font></#if>)</td>
		        <td><#if fkMember.qq?? && fkMember.qq?length gt 3>****${fkMember.qq[3..]}</#if></td>
	        <#else>
		       	<td>${fkMember.phone}&nbsp;(<#if fkMember.phonevalid ==1><font color="green">已认证</font><#else><font color="red">未认证</font></#if>)</td>
		        <td>${fkMember.email}&nbsp;(<#if fkMember.emailvalid ==1><font color="green">已认证</font><#else><font color="red">未认证</font></#if>)</td>
		        <td>${fkMember.qq}</td>
	        </#if>
	      </tr>
	    </tbody>
   </table>
  <table class="table table-border table-bordered table-bg mt-5">
  	<thead>
	      <tr>
	        <th colspan="5" scope="col" class="green">其他信息</th>
	      </tr>
	      <tr class="text-c">
	        <th>钱包余额</th>
	        <th>所属代理</th>
	        <th>启用/禁用状态</th>
	        <th>登录次数</th>
	        <th>备注</th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr class="text-c">
	        <td>${fkMember.accountMoney}</td>
	        <td>${fkMember.puname}</td>
	        <td>
	         <#if fkMember.status==0>
	        	<span style="color:red;">禁用</span>
	         </#if>
	         <#if fkMember.status==1>
	        		<span style="color:blue;">首次登录</span>
	         </#if>
	         <#if fkMember.status==2>
	        		<span style="color:green;">正常登录</span>
	         </#if>
	         <#if fkMember.status==3>
	        		<span style="color:orange;">审核通过</span>
	         </#if>
	        </td>
	        <td>${fkMember.logincount}</td>
	        <td class="text-l"><font color="red">${fkMember.remark}</font></td>
	      </tr>
	    </tbody>
   </table>
   <table class="table table-border table-bordered table-bg mt-5">
  	<thead>
	      <tr>
	        <th colspan="6" scope="col" class="green">注册信息</th>
	      </tr>
	      <tr class="text-c">
	        <th>注册日期</th>
	        <th>注册IP</th>
	        <th>注册网址</th>
	        <th>客户端信息</th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr class="text-c">
	        <td>${fkMember.createDate}</td>
	        <td>${fkMember.regip}(<font color="green"><#if fkMember.iparea??> ${fkMember.iparea}<#else>未知</#if></font>)</td>
	        <td class="text-l">${fkMember.url}</td>
	        <td class="text-l">${fkMember.regdevice}</td>
	      </tr>
	    </tbody>
   </table>
   <div class="cl pd-5 bg-1 bk-gray mt-5">
   		<span class="green">关联账号</span>：${fkMember.relaaccount}
   </div>
   <table class="table table-border table-bordered table-bg mt-5" style="width:50%;">
  	<thead>
	      <tr>
	        <th colspan="3" scope="col" class="green">游戏管理</th>
	      </tr>
	      <tr class="text-c">
	        <th>游戏平台名称</th>
	        <th>第三方余额</th>
	        <th>操作</th>
	      </tr>
	    </thead>
	    <tbody>
	    	<#list gamePlatforms as gf>
	    	 <#if qbh>
	    	 	<#if gf.gpname !='BBIN' && gf.gpname !='SA'>
	    	 		<tr class="text-c">
		        <td>${gf.fullname}</td>
		        <td id="${gf.gpname}"></td>
		        <td class="text-l">
		        	<u style="cursor:pointer" class="maincolor" onclick="queryMoney('${gf.gpname}','${fkMember.uiid}')" title="查询余额">查询余额</u>&nbsp;&nbsp;
		        	<#if gf.gpname =='PT' || gf.gpname =='SA'><u style="cursor:pointer" class="maincolor" onclick="logoutGame('${gf.gpname}','${fkMember.uiid}')" title="登出游戏" id="${gf.gpname}${fkMember.uiid}">登出游戏</u></#if>
		        </td>
		      </tr>
	    	 	</#if>
	    	 <#else>
	    	 <tr class="text-c">
		        <td>${gf.fullname}</td>
		        <td id="${gf.gpname}"></td>
		        <td class="text-l">
		        	<u style="cursor:pointer" class="maincolor" onclick="queryMoney('${gf.gpname}','${fkMember.uiid}')" title="查询余额">查询余额</u>&nbsp;&nbsp;
		        	<#if gf.gpname =='PT' || gf.gpname =='SA'><u style="cursor:pointer" class="maincolor" onclick="logoutGame('${gf.gpname}','${fkMember.uiid}')" title="登出游戏" id="${gf.gpname}${fkMember.uiid}">登出游戏</u></#if>
		        </td>
		      </tr>
		      </#if>
	    	</#list>
	    </tbody>
   </table>
   <table class="table table-border table-bordered table-bg mt-5">
  	<thead>
	      <tr>
	        <th colspan="4" scope="col" class="green">输赢报表(<font color="red">会员盈亏 = 提款总额 - 存款总额</font>)</th>
	      </tr>
	      <tr class="text-c">
	        <th>存款总额</th>
	        <th>提款总额</th>
	        <th>优惠总额</th>
	        <th>会员盈亏</th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr class="text-c">
	        <td>${fkMember.paymoney}</td>
	        <td>${fkMember.withdrawalMoney}</td>
	        <td>${fkMember.couponMoney}</td>
	        <td><#if fkMember.winMoney lt 0><font color="green">${fkMember.winMoney}</font><#else><font color="red">${fkMember.winMoney}</font></#if></td>
	      </tr>
	    </tbody>
   </table>
  <div id="tab-system" class="HuiTab mt-5">
		<div class="tabBar cl"><span>资金流水</span><span>存款记录</span><span>提款记录</span><span>转账记录</span><span>优惠记录</span><span>登录日志</span><span>备注记录</span></div>
		<div class="tabCon mt-5">
		   <table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>类型</th>
			        <th>金额</th>
			        <th>主账户原额度</th>
			        <th>游戏平台原额度</th>
			        <th>操作时间</th>
			        <th>备注</th>
			      </tr>
			    </thead>
			    <tbody>
			     <#if payorderlog?size gt 0>
		             <#list payorderlog as payorder>
				      	<tr class="text-c">
				       	 	<td>
				       	 		<#if payorder.type==0>
				       	 			<font color="green">存款</font>
				       	 		<#elseif payorder.type==1>
				       	 			<font color="#ed9c28">提款</font>
				       	 		<#elseif payorder.type==2>
				       	 			<font color="#3276b1">优惠赠送</font>
				       	 		<#elseif payorder.type==3>
				       	 			<font color="red">扣款</font>
				       	 		<#elseif payorder.type==8>
				       	 			<font color="#ff0302">洗码</font>
				       	 		<#elseif payorder.type==9>
				       	 			<font color="#39b3d7">转账</font>
				       	 		</#if>
				       	 	</td>
				        	<td><#if payorder.amount lt 0><font color="red">${payorder.amount}</font><#else><font color="green">${payorder.amount}</font></#if></td>
				        	<td>${payorder.walletlog}</td>
				        	<td>${payorder.gamelog}</td>
				        	<td>${payorder.createtime?date}</td>
				        	<td class="text-l">${payorder.remark}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="6">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		<div class="tabCon mt-5">
		  <table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>会员账号</th>
			        <th>姓名</th>
			        <th>存款前钱包余额</th>
			        <th>存款金额</th>
			        <th>存款后钱包余额</th>
			        <th>存款方式</th>
			        <th>状态</th>
			        <th>存款时间</th>
			        <th>备注</th>
			      </tr>
			    </thead>
			    <tbody>
			     <#if payorderlist?size gt 0>
		             <#list payorderlist as payorder>
				      	<tr class="text-c">
				       	 	<td>${payorder.uaccount}</td>
				        	<td>${payorder.urealname}</td>
				        	<td>${payorder.beforebalance}</td>
				        	<td>${payorder.amount}</td>
				        	<td>${payorder.laterbalance}</td>
				        	<td><#if payorder.paymethods==0>
				        			<span style="color:red;">公司入款</span>
				        		</#if>
				        		<#if payorder.paymethods==1>
				        			<span style="color:blue;">在线支付</span>
				        		</#if>
				        	</td>
				        	<td>
				        		<#if payorder.status ==0>
				        			<span style="color:blue;">作废</span>
				        		<#elseif payorder.status ==1>
				        			<span style="color:#FF9900;">提交充值</span>
				        		<#elseif payorder.status ==2>
				        			<span style="color:orange;">处理中</span>
				        		<#elseif payorder.status ==3>
				        			<span style="color:#009900;">成功</span>
				        		<#elseif payorder.status ==4>
				        			<span style="color:#FF0000;">失败</span>
				        		</#if>
				        	</td>
				        	<td>${payorder.updateDate?date}</td>
				        	<td class="text-l">${payorder.cwremarks}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="9">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		<div class="tabCon mt-5">
			<table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>会员账号</th>
			        <th>姓名</th>
			        <th>提款后钱包余额</th>
			        <th>提款金额</th>
			        <th>提款后钱包余额</th>
			        <th>状态</th>
			        <th>提款时间</th>
			        <th>备注</th>
			      </tr>
			    </thead>
			    <tbody>
			      <#if withdrawOrderList?size gt 0>
		             <#list withdrawOrderList as payorder>
				      	<tr class="text-c">
				       	 	<td>${payorder.uaccount}</td>
				        	<td>${payorder.urealname}</td>
				        	<td>${payorder.beforebalance}</td>
				        	<td>${payorder.amount}</td>
				        	<td>${payorder.laterbalance}</td>
				        	<td>
				        		<#if payorder.status ==1>
				        			<span style="color:#FF9900;">发起</span>
				        		<#elseif payorder.status ==3>
				        			<span style="color:#009900;">成功</span>
				        		<#elseif payorder.status ==4>
				        			<span style="color:#FF0000;">失败</span>
				        		</#if>
				        	</td>
				        	<td>${payorder.updateDate?date}</td>
				        	<td class="text-l">${payorder.cwremarks}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="8">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		<div class="tabCon mt-5">
			<table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>会员账号</th>
			        <th>姓名</th>
			        <th>转账金额</th>
			        <th>转账后钱包余额</th>
			        <th>转帐时间</th>
			        <th>备注</th>
			      </tr>
			    </thead>
			    <tbody>
				    <#if transferList?size gt 0>
		             <#list transferList as transfer>
				      	<tr class="text-c">
				       	 	<td>${transfer.account}</td>
				        	<td>${transfer.truename}</td>
				        	<td>${transfer.amount}</td>
				        	<td>${transfer.balance}</td>
				        	<td>${transfer.createDate?date}</td>
				        	<td class="text-l">${transfer.gamename}>>>${transfer.togamename}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="6">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		
		<div class="tabCon mt-5">
			<table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>会员账号</th>
			        <th>姓名</th>
			        <th>赠送前钱包余额</th>
			        <th>优惠金额</th>
			        <th>赠送后钱包余额</th>
			        <th>状态</th>
			        <th>创建时间</th>
			        <th>备注</th>
			      </tr>
			    </thead>
			    <tbody>
			      <#if couponorderList?size gt 0>
		             <#list couponorderList as payorder>
				      	<tr class="text-c">
				       	 	<td>${payorder.uaccount}</td>
				        	<td>${payorder.urealname}</td>
				        	<td>${payorder.beforebalance}</td>
				        	<td>${payorder.amount}</td>
				        	<td>${payorder.laterbalance}</td>
				        	<td>
				        		<#if payorder.status ==1>
				        			<span style="color:#FF9900;">发起</span>
				        		<#elseif payorder.status ==3>
				        			<span style="color:#009900;">成功</span>
				        		<#elseif payorder.status ==4>
				        			<span style="color:#FF0000;">失败</span>
				        		</#if>
				        	</td>
				        	<td>${payorder.updateDate?date}</td>
				        	<td class="text-l">${payorder.cwremarks}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="7">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		
		<div class="tabCon mt-5">
			<table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>登陆时间</th>
			        <th>登录IP</th>
			        <th>区域</th>
			        <th>登录来源</th>
			        <th>客户端信息</th>
			      </tr>
			    </thead>
			    <tbody>
			      <#if loginLoglist?size gt 0>
		             <#list loginLoglist as login>
				      	<tr class="text-c">
				        	<td width="120">${login.logintime?date}</td>
				        	<td class="text-l">${login.loginip}</td>
				        	<td class="text-l" width="120">${login.iparea}</td>
				        	<td class="text-l">${login.loginsource}</td>
				        	<td class="text-l" title="${login.logindevice}">${login.logindevice}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="7">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
		
		<div class="tabCon mt-5">
			<table class="table table-border table-bordered table-bg mt-5">
		  	<thead>
			      <tr class="text-c">
			        <th>操作人</th>
			        <th>备注时间</th>
			        <th>备注信息</th>
			      </tr>
			    </thead>
			    <tbody>
			      <#if remarkList?size gt 0>
		             <#list remarkList as r>
				      	<tr class="text-c">
				        	<td class="text-l">${r.operator}</td>
				        	<td width="120">${r.createdate?date}</td>
				        	<td class="text-l">${r.remark}</td>
				      	</tr>
				      </#list>
				   <#else>
				   		<tr class="text-c">
				   			<td align="center" valign="middle" colspan="3">暂无数据</td>
				   		</tr>
				   </#if>
			    </tbody>
		   </table>
		</div>
	</div>
</div>
<script type="text/javascript" src="/js/myjs/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="/js/myjs/jquery.icheck.min.js"></script> 
<script type="text/javascript" src="/js/myjs/H-ui.js"></script> 
<script type="text/javascript" src="/js/myjs/H-ui.admin.js"></script>
<script type="text/javascript">
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
});

function queryMoney(gname,uiid){
	$("#"+gname).text('查询中...');
	$.ajax({
		url : "/manage/memberinfo/queryUserGameMoney/"+gname+"/"+uiid+".do?r="+Math.random(),
		async : true,
		type : "POST",
		dataType : "json",
		success : function(data) {
			if (data.success) {
				$("#"+gname).text(data.msg);
			}else {
				$("#"+gname).text('0');
			}
		},
		error : function() {
			$("#"+gname).text('0');
			alert("网络异常，请稍后再试。");
		}
	});
}

function logoutGame(gname,uiid){
	$("#"+gname+uiid).text('正在登出...');
	$.ajax({
		url : "/manage/memberinfo/logoutGame/"+gname+"/"+uiid+".do?r="+Math.random(),
		async : true,
		type : "POST",
		dataType : "json",
		success : function(data) {
			if (data.success) {
				alert(data.msg);
				$("#"+gname+uiid).text('登出游戏');
			}else {
				alert(data.msg);
				$("#"+gname+uiid).text('登出游戏');
			}
		},
		error : function() {
			alert("网络异常，请稍后再试。");
			$("#"+gname+uiid).text('登出游戏');
		}
	});
}
</script>
</body>
</html>