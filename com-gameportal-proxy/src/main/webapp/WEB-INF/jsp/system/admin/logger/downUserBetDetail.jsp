<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 会员投注明细</title>
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
						<!-- 
						<ul class="breadcrumb">
							<li><i class="icon-home"></i> <a href="#">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>
							<li class="active">Tables</li>
						</ul>
						 -->
						<!--.breadcrumb-->
						<div id="nav-search">
							<form class="form-search" action="${ctx}userproxy/queryDownUserDetail.do" method="post" name="downuserDetailFrm">
									<span class="input-icon">
										<button class="btn btn-danger btn-small" onclick="javascript:history.back();">返回<i class="icon-reply icon-2x icon-only"></i></button>
									</span>
									<span class="input-icon">
										<label>会员账号：</label>
										<input type="text" id="uaccount" name="account" class="form-control" style="width: 100px;" value="${pd.account}">
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
										<button type="submit" class="btn btn-purple btn-small _querydownuserDetailFrm">搜索<i class="icon-search icon-on-right"></i></button>
									</span>
							</form>

						</div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<!-- PAGE CONTENT BEGINS HERE -->
						<div class="row-fluid">
							<div class="span12">
								<table id="table_paylog" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>游戏平台</th>
											<th>账号</th>
											<th>游戏</th>
											<th>押注单号</th>
											<th>押注金额</th>
											<th>有效投注额</th>
											<th>派彩金额</th>
											<th>输赢金额</th>
											<th>投注时间</th>
											<th>下注前余额</th>
										</tr>
									</thead>
									<tbody class="_downUserDetailList">
										<c:if test="${not empty betList}">
											<c:forEach items="${betList }" var="bet">
												<tr>
													<td>${bet.platformcode }</td>
													<td>${bet.account }</td>
													<td>${bet.gamename }</td>
													<td>${bet.betno }</td>
													<td class="text-danger">${bet.betamount}</td>
													<td class="text-danger">${bet.validBetAmount}</td>
													<td class="text-danger">${bet.profitamount}</td>
													<td class="text-danger">${bet.finalamount}</td>
													<td>${fn:substring(bet.betdate,0,19) }</td>
													<td class="text-danger">${bet.beforeCerdit}</td>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${empty betList}">
											<tr><td colspan="10" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty betList}">
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
			
			
			$('#etimepicker').timepicker({
				minuteStep: 1,
				showSeconds: true,
				showMeridian: false
			});
			
			$('#stimepicker').timepicker({
				minuteStep: 1,
				showSeconds: true,
				showMeridian: false
			});

			
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
			
			$("._querydownuserDetailFrm").click(function(){
				downuserDetailFrm.submit();
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
		            	$("._downUserDetailList").html("<tr><td colspan='10' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
		            	downuserDetailFrm.submit();
		            }
		        }
		        element.bootstrapPaginator(options);
		    })
		    $(".nav-list").find("li").removeClass("active");
			$("#queryProxyUserReport",".nav-list").addClass("active");
		});
	</script>
	</body>
</html>