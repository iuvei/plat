<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 我的结算记录</title>
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
							<li class="active">我的结算记录</li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
							<form class="form-search" action="${ctx}userproxy/queryProxyClearingLog.do" method="post" name="downUserFrm">
									<span class="input-icon">
										<label>会员账号：</label>
										<input type="text" id="uaccount" name="account" class="form-control" style="width: 100px;" value="${pd.uaccount}">
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
										<button type="submit" class="btn btn-purple btn-small _queryDownUserFrm">搜索<i class="icon-search icon-on-right"></i></button>
									</span>
							</form>
						</div>
						<!-- PAGE CONTENT BEGINS HERE -->
						<div class="row-fluid">
							<div class="span12">
								<table id="table_paylog" class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th>账号</th>
											<th>姓名</th>
											<th>结算金额</th>
											<th>结算比例</th>
											<th>盈亏</th>
											<th>投注总额</th>
											<th>洗码总额</th>
											<th>总优惠</th>
											<th>实际盈亏</th>
											<th>结算类型</th>
											<th>结算日期</th>
											<th>备注</th>
											<th>状态</th>
										</tr>
									</thead>
									<tbody class="_proxyClearingLogList">
										<c:if test="${not empty proxyList}">
											<c:forEach items="${proxyList }" var="proxy">
												<tr>
													<td>${proxy.account }</td>
													<c:if test="${not empty proxy.uname}">
														<td>${fn:substring(proxy.uname, 0, 1)}**</td>
													</c:if>
													<c:if test="${empty proxy.uname}">
														<td></td>
													</c:if>
													<td class="text-danger">${proxy.clearingAmount}</td>
													<td>${proxy.clearingScale }</td>
													<td>${proxy.finalamountTotal }</td>
													<td>${proxy.validBetAmountTotal }</td>
													<td>${proxy.ximaAmount }</td>
													<td>${proxy.preferentialTotal }</td>
													<td>${proxy.realPL }</td>
													<c:if test="${proxy.clearingType == 0}">
														<th class="text-danger">输值结算按月</th>
													</c:if>
													<c:if test="${proxy.clearingType == 1}">
														<th class="text-warning">按月洗码</th>
													</c:if>
													<c:if test="${proxy.clearingType == 2}">
														<th class="text-pink">按天洗码</th>
													</c:if>
													<td>${fn:substring(proxy.clearingTime,0,19) }</td>
													<td>${proxy.clearingRemark }</td>
													<c:if test="${proxy.clearingStatus == 0}">
														<th class="text-danger">未入账</th>
													</c:if>
													<c:if test="${proxy.clearingStatus== 1}">
														<th class="text-warning">已入账</th>
													</c:if>
													<c:if test="${proxy.clearingStatus == 2}">
														<th class="text-pink">记录</th>
													</c:if>
													<c:if test="${proxy.clearingStatus == 3}">
														<th class="text-primary">撤除记录</th>
													</c:if>
													<c:if test="${proxy.clearingStatus == 4}">
														<th class="text-purple">未洗码</th>
													</c:if>
													<c:if test="${proxy.clearingStatus == 5}">
														<th class="text-yellow">已洗码</th>
													</c:if>
													<c:if test="${proxy.clearingStatus == 6}">
														<th class="text-info">洗码记录</th>
													</c:if>
												</tr>
											</c:forEach>
											<!-- 小计 -->
											<tr>
													<td><span style="font-weight: bold;color:red;">${proxyminTotal.account }</span></td>
													<td></td>
													<td class="text-danger">${proxyminTotal.clearingAmount}</td>
													<td></td>
													<td class="text-danger">${proxyminTotal.finalamountTotal }</td>
													<td class="text-danger">${proxyminTotal.validBetAmountTotal }</td>
													<td class="text-danger">${proxyminTotal.ximaAmount }</td>
													<td class="text-danger">${proxyminTotal.preferentialTotal }</td>
													<td class="text-danger">${proxyminTotal.realPL }</td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
											</tr>
											<!-- 总计  -->
											<tr>
													<td><span style="font-weight: bold;color:green;">${proxyMaxTotal.account }</span></td>
													<td></td>
													<td class="text-danger">${proxyMaxTotal.clearingAmount}</td>
													<td></td>
													<td class="text-danger">${proxyMaxTotal.finalamountTotal }</td>
													<td class="text-danger">${proxyMaxTotal.validBetAmountTotal }</td>
													<td class="text-danger">${proxyMaxTotal.ximaAmount }</td>
													<td class="text-danger">${proxyMaxTotal.preferentialTotal }</td>
													<td class="text-danger">${proxyMaxTotal.realPL }</td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
											</tr>
										</c:if>
										<c:if test="${empty proxyList}">
											<tr><td colspan="13" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty proxyList}">
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
			
			$("._queryproxyClearingLogFrm").click(function(){
				proxyClearingLogFrm.submit();
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
		            	$("._proxyClearingLogList").html("<tr><td colspan='13s' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
		            	proxyClearingLogFrm.submit();
		            }
		        }
		        element.bootstrapPaginator(options);
		    })
		    
		    $(".nav-list").find("li").removeClass("active");
			$("#queryProxyClearingLog",".nav-list").addClass("active");
		});

		function changeRadio(obj){
			var status=$(obj).parent("td").children("input[name='clearstatus']").val();
			var isXimaFlag='${isximaFlag}';
			if(isXimaFlag=='1' && status=='4'){
				$("#ximaBtn").removeClass("disabled");
			}else{
				$("#ximaBtn").addClass("disabled");	
			}
		}

		function zzXima(){
			var clearingId=$("input[name='checks']:checked").val();
			var jsDate=$("input[name='checks']:checked").next("input[name='jdDate']").val();
			if(confirm("提示:自助洗码后会将您的洗码金额分配到下线会员，确定要自助洗码吗?")){
				$.ajax({
				    url: "${ctx}userproxy/ziZhuXima.do", 
				    dataType: "json", 
				    async: true,
				    data: {clearingid:clearingId,jsdate:jsDate},
				    type: "POST",
				    success: function(res) {
				    	console.log(res);
				        if(res.success){
				        	alert(res.msg);
				        	location.href="${ctx}userproxy/queryProxyClearingLog.do";
				        }else{
				        	alert(res.msg);
				        }
				    },
				    error: function() {
				    	alert("网络异常,请稍后再试!");
				    }
				});
			}
		}
	</script>
	</body>
</html>