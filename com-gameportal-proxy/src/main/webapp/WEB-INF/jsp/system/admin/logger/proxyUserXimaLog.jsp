<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 下线会员洗码记录</title>
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
							<li class="active">下线会员洗码记录</li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
							<form class="form-search" action="${ctx}userproxy/queryProxyUserXimaLog.do" method="post" name="proxyUserXimaFrm">
									<span class="input-icon">
										<label>会员账号：</label>
										<input type="text" id="uaccount" name="account" class="form-control" style="width: 100px;" value="${pd.account}">
									</span>
									<span class="input-icon">
										<label>开始时间：</label>
										<input type="text" id="starttime" name="startDate" class="form-control  date-picker" placeholder="开始时间" data-date-format="yyyy-mm-dd" style="width:80px;" value="${pd.startDate}" readonly>
									</span>
									<span class="input-icon">
										<label>结束时间：</label>
										<input type="text" id="endtime" name="endDate" class="form-control  date-picker" placeholder="结束时间" data-date-format="yyyy-mm-dd" style="width:80px;" value="${pd.endDate}" readonly>
									</span>
									<span class="input-icon">
										<input type="hidden" name="currentPage" id="currentPage" value="${page.currentPage}" />
										<button type="submit" class="btn btn-purple btn-small _queryproxyUserXimaFrm">搜索<i class="icon-search icon-on-right"></i></button>
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
											<th>代理账号</th>
											<th>代理姓名</th>
											<th>洗码比例</th>
											<th>洗码金额</th>
											<!-- <th>优惠金额</th> -->
											<th>有效投注额</th>
											<th>结算时间</th>
											<th>状态</th>
										</tr>
									</thead>
									<tbody class="_queryproxyUserXimaList">
										<c:if test="${not empty ximaList}">
											<c:forEach items="${ximaList }" var="xima">
												<tr>
													<td>${xima.account }</td>
													<c:if test="${not empty xima.uname}">
														<td>${fn:substring(xima.uname, 0, 1)}**</td>
													</c:if>
													<c:if test="${empty xima.uname}">
														<td></td>
													</c:if>
													<td>${xima.proxyaccount }</td>
													<td>${xima.proxyuname }</td>
													<td>${xima.ximascale }</td>
													<td class="text-danger">${xima.ximamoney}</td>
													<%-- <td class="text-danger">${xima.yhmoney}</td> --%>
													<td class="text-danger">${xima.validmoney}</td>
													<td>${fn:substring(xima.ximatime,0,19) }</td>
													<c:if test="${xima.status == 0}">
														<th class="text-danger">未入账</th>
													</c:if>
													<c:if test="${xima.status == 1}">
														<th class="text-warning">已入账</th>
													</c:if>
													<c:if test="${xima.status == 2}">
														<th class="text-purple">审核不通过</th>
													</c:if>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${empty ximaList}">
											<tr><td colspan="10" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty ximaList}">
								<div class="row-fluid" style="border-top:none;">
									<div class="span6"><div class="dataTables_info" style="padding-top:8px;">共 ${page.totalResult } 条数据 - 每页显示${page.showCount }条 - 共 ${page.totalPage }页</div></div>
									<div class="span6" style="padding-right:8px;">
										 <ul id="paginator" style="margin:0px; float: right;"></ul>
									</div>
								</div>
								</c:if>
							</div><!--/span-->
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
			
			$("._queryproxyUserXimaFrm").click(function(){
				proxyUserXimaFrm.submit();
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
		            	$("._queryproxyUserXimaList").html("<tr><td colspan='9' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
		            	proxyUserXimaFrm.submit();
		            }
		        }
		        element.bootstrapPaginator(options);
		    })
		    
		    $(".nav-list").find("li").removeClass("active");
			$("#queryProxyUserXimaLog",".nav-list").addClass("active");
		});
	
	</script>
	</body>
</html>