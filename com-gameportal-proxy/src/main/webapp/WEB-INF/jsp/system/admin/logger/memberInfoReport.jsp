<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 下线月报表</title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@include file="../include/meta.jsp"%>
		<%@include file="../include/css.jsp"%>
	</head>
	<body>
	 <%@include file="../top.jsp"%>
	<%--  <%@include file="gamePlatformMoney.jsp" %> --%>
	 <div class="container-fluid" id="main-container">
			<a href="#" id="menu-toggler"><span></span></a><!-- menu toggler -->
			<%@include file="../include/left.jsp" %>
			<div id="main-content" class="clearfix">
					<div id="breadcrumbs">
						
						<ul class="breadcrumb">
							<li><i class="icon-home"></i> <a href="/manage/index.do">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>
							<li class="active">下线月报表<font color="red">(操作提示：点击操作列中查询按钮，即可查询用户游戏输赢,其中总优惠不包含总洗码)</font></li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
							<form class="form-search" action="${ctx}userproxy/queryProxyUserReport.do" method="post" name="reportFrm" id="reportFrm">
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
									<button type="submit" class="btn btn-purple btn-small reportFrm">搜索<i class="icon-search icon-on-right"></i></button>
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
											<th>钱包余额</th>
											<th>总存款</th>
											<th>总提款</th>
											<th>总优惠</th>
											<th>总洗码</th>
											<th>游戏输赢</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody class="_reportList">
										<c:if test="${not empty reportList}">
											<c:forEach items="${reportList }" var="report">
												<tr>
													<td>${report.account }</td>
													<c:if test="${not empty report.uname}">
														<td>${fn:substring(report.uname, 0, 1)}**</td>
													</c:if>
													<c:if test="${empty report.uname}">
														<td></td>
													</c:if>
													<td class="text-danger">${report.money}</td>
													<td>${report.depositTotal }</td>
													<td>${report.withdrawalTotal }</td>
													<td>${report.preferentialTotal }</td>
													<td>${report.ximaTotal }</td>
													<td>--</td>
													<td><button class="btn btn-mini btn-success _winlessQuery" data="${report.account }"><i class="icon-ok">查询</i></button></td>
												</tr>
											</c:forEach>
											<!-- 小计 -->
											<tr>
													<td><span style="font-weight: bold;color:red;">${downuserMinTotal.account }</span></td>
													<td></td>
													<td class="text-danger">${downuserMinTotal.money}</td>
													<td>${downuserMinTotal.depositTotal }</td>
													<td>${downuserMinTotal.withdrawalTotal }</td>
													<td>${downuserMinTotal.preferentialTotal }</td>
													<td>${downuserMinTotal.ximaTotal }</td>
													<td></td>
													<td></td>
											</tr>
											<!-- 总计 -->
											<tr>
													<td><span style="font-weight: bold;color:green;">总计:</span></td>
													<td></td>
													<td class="text-danger">${downuserMaxTotal.moneyTotal}</td>
													<td>${downuserMaxTotal.depositTotal }</td>
													<td>${downuserMaxTotal.withdrawalTotal }</td>
													<td>${downuserMaxTotal.preferentialTotal }</td>
													<td>${downuserMaxTotal.ximaTotal }</td>
													<td>--</td>
													<td><button class="btn btn-mini btn-success _winlessTotalQuery"><i class="icon-ok">查询</i></button></td>
											</tr>
										</c:if>
										<c:if test="${empty reportList}">
											<tr><td colspan="9" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty reportList}">
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
		var target;
		$(function(){
			$("._winlessQuery").each(function(){
				$(this).click(function(){
					target=$(this);
					target.addClass("disabled");
					$.ajax({
					    url: "${ctx}userproxy/querySingerUserReport.do", 
					    dataType: "json", 
					    async: true,
					    data: {account:target.attr("data"),startDate:$("#starttime").val(),endDate:$("#endtime").val(),type:"1"},
					    type: "POST",
					    success: function(res) {
					        if(res.success){
					        	target.parent().prev().text(res.winorless);
					        }
					        target.removeClass("disabled");
					    },
					    error: function() {
					    	alert("网络异常,请稍后再试!");
					    	target.removeClass("disabled");
					    }
					});
				});
			});
			
			$("._winlessTotalQuery").click(function(){
				target=$(this);
				target.addClass("disabled");
				$.ajax({
				    url: "${ctx}userproxy/querySingerUserReport.do", 
				    dataType: "json", 
				    async: true,
				    data: {account:'',startDate:$("#starttime").val(),endDate:$("#endtime").val(),type:"0"},
				    type: "POST",
				    success: function(res) {
				        if(res.success){
				        	target.parent().prev().text(res.winorless);
				        }
				        target.removeClass("disabled");
				    },
				    error: function() {
				    	alert("网络异常,请稍后再试!");
				    	target.removeClass("disabled");
				    }
				});
			});
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
			
			$(".reportFrm").click(function(){
				reportFrm.submit();
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
		            	$("._reportList").html("<tr><td colspan='9' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
		            	reportFrm.submit();
		            }
		        }
		        element.bootstrapPaginator(options);
		    })
		    $(".nav-list").find("li").removeClass("active");
			$("#queryProxyUserReport",".nav-list").addClass("active");
		});
		//选择会员
		function changeRadio(){
			$("#queryMoney").removeClass("disabled");
		}

		//查询会员游戏余额
		function flushMoney(){
			$("._gameMoney").html("<tr><td colspan='2' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
			$("#flushMoney").addClass("disabled");
			var account=$("input[name='checks']:checked").val();
			var apipwd=$("input[name='checks']:checked").next("input[name='apiPwd']").val();
			$.ajax({
			    url: "${ctx}userproxy/queryGameMoney.do", 
			    dataType: "json", 
			    async: true,
			    data: {account:account,apipassword:apipwd},
			    type: "POST",
			    success: function(res) {
			        if(res.success){
			        	var html="";
			        	jQuery.each(res.data, function(i,item){   
			               html+="<tr><td>"+item.gpname+"</td><td>"+item.money+"</td></tr>";
			            });   
			        	$("._gameMoney").html(html);
			        }else{
			        	alert(res.data);
			        }
			        $("#flushMoney").removeClass("disabled");
			    },
			    error: function() {
			    	alert("网络异常,请稍后再试!");
			    	$("#flushMoney").removeClass("disabled");
			    }
			});
		}
	</script>
	</body>
</html>