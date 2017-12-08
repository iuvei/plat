<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 个人中心</title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@include file="../include/meta.jsp"%>
		<%@include file="../include/css.jsp"%>
	</head>
	<body>
	 <%@include file="../top.jsp"%>
	 <div class="container-fluid" id="main-container">
			<a href="#" id="menu-toggler"><span></span></a><!-- menu toggler -->
			<%@include file="../include/left.jsp" %>
			<div id="main-content" class="clearfix">
					<div id="breadcrumbs">

						<ul class="breadcrumb">

							<li><i class="icon-home"></i> <a href="#">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>

							<li class="active">账户详情</li>
						</ul><!--.breadcrumb-->
						<div id="nav-search">

							<form class="form-search">

									<span class="input-icon">

										<input autocomplete="off" id="nav-search-input" type="text" class="input-small search-query" placeholder="Search ..." />

										<i id="nav-search-icon" class="icon-search"></i>

									</span>

							</form>

						</div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="row-fluid">
							<!-- PAGE CONTENT BEGINS HERE -->
							<div class="row-fluid">
								<h3 class="text-danger"><span style="font-weight:bold;">代理推广地址</span>：${proxyURL}</h3>
								<hr>
					  			<h3 class="text-danger"><span style="font-weight:bold;">下线人数</span>：${downUserCount}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">当前钱包余额</span>：${totolMoney}</h3>
					  			<hr>
					  			<h3 class="text-danger"><span style="font-weight:bold;">今日点击量</span>：<font color="red">${todayPv}</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">今日注册人数</span>：<font color="red">${todayRegNum }</font>&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">今日存款量</span>：<font color="red">${todayDeposit}</font></h3>
					  			<hr>
					  			<h3 class="text-danger"><span style="font-weight:bold;">本月点击量</span>：<font color="green">${monthPv}</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">本月注册人数</span>：<font color="green">${monthRegNum}</font>&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">本月存款量</span>：<font color="green">${monthDeposit}</font></h3>
					  			<hr>
					  			<h3 class="text-danger"><span style="font-weight:bold;">洗码比例</span>：${ximascale}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-weight:bold;">占成：${returnscale}</h3>
							</div>
							 
							<!-- PAGE CONTENT ENDS HERE -->
						 </div><!--/row-->
	
					</div><!--/#page-content-->
					  
					<div id="ace-settings-container">
						<div class="btn btn-app btn-mini btn-warning" id="ace-settings-btn">
							<i class="icon-cog"></i>
						</div>
						<div id="ace-settings-box">
							<div>
								<div class="pull-left">
									<select id="skin-colorpicker" class="hidden">
										<option data-class="default" value="#438EB9">#438EB9</option>
										<option data-class="skin-1" value="#222A2D">#222A2D</option>
										<option data-class="skin-2" value="#C6487E">#C6487E</option>
										<option data-class="skin-3" value="#D0D0D0">#D0D0D0</option>
									</select>
								</div>
								<span>&nbsp; Choose Skin</span>
							</div>
							<div><input type="checkbox" class="ace-checkbox-2" id="ace-settings-header" /><label class="lbl" for="ace-settings-header"> Fixed Header</label></div>
							<div><input type="checkbox" class="ace-checkbox-2" id="ace-settings-sidebar" /><label class="lbl" for="ace-settings-sidebar"> Fixed Sidebar</label></div>
						</div>
					</div><!--/#ace-settings-container-->
			</div><!-- #main-content -->
		</div><!--/.fluid-container#main-container-->
		<a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse">
			<i class="icon-double-angle-up icon-only"></i>
		</a>
		<%@include file="../include/javascript.jsp"%>
		
		<script type="text/javascript">
		
		</script>
	</body>
</html>