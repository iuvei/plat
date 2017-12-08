<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<div id="sidebar">
  <ul class="nav nav-list">
	<li class="active" id="index">
	  <a href="${ctx}manage/index.do">
		<i class="icon-laptop"></i>
		<span>账户详情</span>
	  </a>
	</li>

	<li id="queryProxyClearing">
	  <a href="${ctx}userproxy/queryProxyClearing.do">
		<i class="icon-edit"></i>
		<span>结算详情</span>
	  </a>
	</li>
	<!-- 
	<li id="queryProxyClearingLog">
	  <a href="${ctx}userproxy/queryProxyClearingLog.do">
		<i class="icon-list"></i>
		<span>结算记录</span>
	  </a>
	</li>
	 -->
	<c:if test="${memberinfo.uiid ==31987 || memberinfo.uiid ==33193 || memberinfo.uiid ==17 || memberinfo.uiid ==20 || memberinfo.uiid ==77815 || memberinfo.uiid ==91714}">
	<li id="queryDownProxy">
	  <a href="${ctx}userproxy/queryDownProxy.do" >
		<i class="icon-search"></i>
		<span>查询下线代理</span>
	  </a>
	</li>
 	</c:if>
	<li id="queryDownUser">
	  <a href="${ctx}userproxy/queryDownUser.do" >
		<i class="icon-search"></i>
		<span>查询下线会员</span>
	  </a>
	</li>
	
	
	 <li id="membertransfer">
	  <a href="${ctx}userproxy/membertransfer.do">
		<i class="icon-credit-card"></i>
		<span>下线红包派发</span>
	  </a>
	</li>
	 
	 <li id="downtransferlist">
	  <a href="${ctx}userproxy/downtransferlist.do">
		<i class="icon-list"></i>
		<span>下线转账记录</span>
	  </a>
	</li>
	
	<li id="queryDepositLog">
	  <a href="${ctx}userproxy/queryDepositLog.do">
		<i class="icon-credit-card"></i>
		<span>下线存款记录</span>
	  </a>
	</li>
	
	<li id="queryPayOrderLog">
	  <a href="${ctx}userproxy/queryPayOrderLog.do">
		<i class="icon-credit-card"></i>
		<span>下线提款记录</span>
	  </a>
	</li>
	
	<li id="queryPunishOrderLog">
	  <a href="${ctx}userproxy/queryPunishOrderLog.do">
		<i class="icon-credit-card"></i>
		<span>下线扣款记录</span>
	  </a>
	</li>
	

	<li id="queryProxyUserReport">
	  <a href="${ctx}userproxy/queryProxyUserReport.do">
		<i class="icon-bar-chart"></i>
		<span>下线月报表</span>
	  </a>
	</li>

	<li id="queryProxyUserXimaLog">
	  <a href="${ctx}userproxy/queryProxyUserXimaLog.do">
		<i class="icon-list"></i>
		<span>下线洗码记录</span>
	  </a>
	</li>

	<li id="logout">
	  <a href="${ctx }login/logout.do">
		<i class="icon-off"></i>
		<span>安全退出</span>
	  </a>
	</li>

</ul><!--/.nav-list-->
<div id="sidebar-collapse"><i class="icon-double-angle-left"></i></div>
</div><!--/#sidebar-->