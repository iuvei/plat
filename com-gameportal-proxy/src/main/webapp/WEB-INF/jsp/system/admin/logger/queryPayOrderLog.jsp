<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 会员提款记录</title>
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
						<li><i class="icon-home"></i> <a href="/manage/index.do">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>
						<li class="active">下线提款记录</li>
					</ul>
					<!--.breadcrumb-->
					<div id="nav-search"></div><!--#nav-search-->

				</div><!--#breadcrumbs-->
				<div id="page-content" class="clearfix">
					<div class="page-header position-relative" style="padding-bottom:0px;">
						<form class="form-search" action="${ctx}userproxy/queryPayOrderLog.do" method="post" name="payOrderLogsFrm">
							<span class="input-icon">
									<label>会员账号：</label>
									<input type="text"name="account" class="form-control" style="width: 100px;" value="${pd.account}">
								</span>
								<span class="input-icon">
									<label>开始时间：</label>
									<input type="text" id="starttime" name="startDate" class="form-control  date-picker" placeholder="开始时间" data-date-format="yyyy-mm-dd" style="width:80px;" value="${pd.startDate}" readonly>
									<div class="input-append bootstrap-timepicker"><div class="bootstrap-timepicker-widget dropdown-menu"><table><tbody><tr><td><a href="#" data-action="incrementHour"><i class="icon-chevron-up"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="incrementMinute"><i class="icon-chevron-up"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="incrementSecond"><i class="icon-chevron-up"></i></a></td></tr><tr><td><input type="text" name="hour" class="bootstrap-timepicker-hour" maxlength="2"></td> <td class="separator">:</td><td><input type="text" name="minute" class="bootstrap-timepicker-minute" maxlength="2"></td> <td class="separator">:</td><td><input type="text" name="second" class="bootstrap-timepicker-second" maxlength="2"></td></tr><tr><td><a href="#" data-action="decrementHour"><i class="icon-chevron-down"></i></a></td><td class="separator"></td><td><a href="#" data-action="decrementMinute"><i class="icon-chevron-down"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="decrementSecond"><i class="icon-chevron-down"></i></a></td></tr></tbody></table></div>
										<input id="stimepicker" type="text" class="input-small" value="${pd.startsfm }">
										<span class="add-on"><i class="icon-time"></i></span>
									</div>
								</span>
								<span class="input-icon">
									<label>结束时间：</label>
									<input type="text" id="endtime" name="endDate" class="form-control  date-picker" placeholder="结束时间" data-date-format="yyyy-mm-dd" style="width:80px;" value="${pd.endDate}" readonly>
									<div class="input-append bootstrap-timepicker"><div class="bootstrap-timepicker-widget dropdown-menu"><table><tbody><tr><td><a href="#" data-action="incrementHour"><i class="icon-chevron-up"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="incrementMinute"><i class="icon-chevron-up"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="incrementSecond"><i class="icon-chevron-up"></i></a></td></tr><tr><td><input type="text" name="hour" class="bootstrap-timepicker-hour" maxlength="2"></td> <td class="separator">:</td><td><input type="text" name="minute" class="bootstrap-timepicker-minute" maxlength="2"></td> <td class="separator">:</td><td><input type="text" name="second" class="bootstrap-timepicker-second" maxlength="2"></td></tr><tr><td><a href="#" data-action="decrementHour"><i class="icon-chevron-down"></i></a></td><td class="separator"></td><td><a href="#" data-action="decrementMinute"><i class="icon-chevron-down"></i></a></td><td class="separator">&nbsp;</td><td><a href="#" data-action="decrementSecond"><i class="icon-chevron-down"></i></a></td></tr></tbody></table></div>
										<input id="etimepicker" type="text" class="input-small" value="${pd.endsfm }">
										<span class="add-on"><i class="icon-time"></i></span>
									</div>
								</span>
								<span class="input-icon">
									<input type="hidden" name="currentPage" id="currentPage" value="${page.currentPage}" />
									<button type="submit" class="btn btn-purple btn-small _querypayOrderlogsFrm">搜索<i class="icon-search icon-on-right"></i></button>
								</span>
						</form>
					</div>
						<!-- PAGE CONTENT BEGINS HERE -->
						<div class="row-fluid">
							<div class="span12">
								<table id="table_paylog" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>会员账号</th>
											<th>会员姓名</th>
											<th>交易金额</th>
											<th>交易类型</th>
											<th>操作时间</th>
											<th>处理状态</th>
											<th>备注</th>
										</tr>
									</thead>
									<tbody class="_orderList">
										<c:if test="${not empty orderList}">
											<c:forEach items="${orderList }" var="order">
												<tr>
													<td>${order.uaccount }</td>
													<c:if test="${not empty order.urealname}">
														<td>${fn:substring(order.urealname, 0, 1)}**</td>
													</c:if>
													<c:if test="${empty order.urealname}">
														<td></td>
													</c:if>
													<td class="text-danger">${order.amount}</td>
													<c:if test="${order.paytyple == 1}">
														<th class="text-danger">提款</th>
													</c:if>
													<td>${fn:substring(order.deposittime,0,19) }</td>
													<c:if test="${order.status == 0}">
														<th class="text-danger">作废</th>
													</c:if>
													<c:if test="${order.status == 1}">
														<th class="text-warning">发起</th>
													</c:if>
													<c:if test="${order.status == 2}">
														<th class="text-pink">待付款</th>
													</c:if>
													<c:if test="${order.status == 3}">
														<th class="text-primary">处理成功</th>
													</c:if>
													<c:if test="${order.status == 4}">
														<th class="text-purple">处理失败</th>
													</c:if>
													<td>${order.remarks }</td>
												</tr>
											</c:forEach>
											<!-- 小计 -->
											<tr>
												<td><span style="font-weight: bold;color:red;">${payOrderMinTotal.uaccount }</span></td>
												<td></td>
												<td class="text-danger">${payOrderMinTotal.amount}</td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
											<!-- 总计-->
											<tr>
												<td><span style="font-weight: bold;color:green;">${payOrderMaxTotal.uaccount }</span></td>
												<td></td>
												<td class="text-danger">${payOrderMaxTotal.amount}</td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
										</c:if>
										<c:if test="${empty orderList}">
											<tr><td colspan="7" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty orderList}">
								<div class="row-fluid" style="border-top:none;">
									<div class="span6"><div class="dataTables_info" style="padding-top:8px;">共 ${page.totalResult } 条数据 - 每页显示${page.showCount }条 - 共 ${page.totalPage }页</div></div>
									<div class="span6" style="padding-right:8px;">
										 <ul id="paginator" style="margin:0px; float: right;"></ul>
									</div>
								</div>
								</c:if>
							</div><!--/span-->
							<!-- PAGE CONTENT ENDS HERE -->
	
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
		<script src="${ctx }plugins/js/bootstrap-paginator.js"></script>
		<script src="${ctx }plugins/js/qunit-1.11.0.js"></script>
		<script type="text/javascript">
		$(function(){
			$('#starttime').datepicker({ 
			 	startDate:"${starttime}",
				endDate:"${endtime}"
			 });
			$('#endtime').datepicker({ 
			 	startDate:"${starttime}",
				endDate:"${endtime}"
			 });
			var checkin = $('#starttime').datepicker().on('changeDate',function(){
				checkin.hide();
				  $('#endtime')[0].focus();
			}).data('datepicker');
			
			var checkout = $('#endtime').datepicker().on('changeDate', function(ev) {
				  checkout.hide();
				}).data('datepicker');
			
			
			$('#stimepicker').timepicker({
				minuteStep: 1,
				showSeconds: true,
				showMeridian: false
			});
			$('#etimepicker').timepicker({
				minuteStep: 1,
				showSeconds: true,
				showMeridian: false
			});
			
			$("._querypayOrderlogsFrm").click(function(){
				payOrderLogsFrm.submit();
			});
			
			test("page", function(){
		        var element = $('#paginator');
		        var options = {
		          	bootstrapMajorVersion:3,
		            currentPage: '${page.currentPage}',
		            totalPages:'${page.totalPage}',
		            itemTexts: function (type, page, current) {
		                switch (type) {
		                case "first":
		                    return "首页";
		                case "prev":
		                    return "上一页";
		                case "next":
		                    return "下一页";
		                case "last":
		                    return "尾页";
		                case "page":
		                    return page;
		                }
		            },
		            onPageClicked: function (event, originalEvent, type, page) {
		            	$("#currentPage").val(page);
		            	$("._orderList").html("<tr><td colspan='7' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
		            	payOrderLogsFrm.submit();
		            }
		        }
		        element.bootstrapPaginator(options);
		    })
		    $(".nav-list").find("li").removeClass("active");
			$("#queryPayOrderLog",".nav-list").addClass("active");
		});
	</script>
	</body>
</html>