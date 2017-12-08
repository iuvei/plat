<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 查询下线会员</title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@include file="../include/meta.jsp"%>
		<%@include file="../include/css.jsp"%>
	</head>
	<body>
	 <%@include file="../top.jsp"%>
	 <%@include file="setXimaScale.jsp" %>
	 <div class="container-fluid" id="main-container">
			<a href="#" id="menu-toggler"><span></span></a><!-- menu toggler -->
			<%@include file="../include/left.jsp" %>
			<div id="main-content" class="clearfix">
					<div id="breadcrumbs">
						
						<ul class="breadcrumb">
							<li><i class="icon-home"></i> <a href="/manage/index.do">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>
							<li class="active">查询下线会员</li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
							<form class="form-search" action="${ctx}userproxy/queryDownUser.do" method="post" name="downUserFrm">
									<input type="hidden" id="isximaFlag" value="${sessionScope.isximaFlag}" />
									<span class="input-icon">
										<button class="btn btn-primary btn-small" disabled="disabled" id="ximaBtn" data-toggle="modal" data-target="#setXimaScaleModal" onclick="return false;">设置洗码</button>
									</span>
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
											<th>选择</th>
											<th>会员账号</th>
											<th>真实姓名</th>
											<th>洗码比例</th>
											<th>注单数量</th>
											<th>投注金额</th>
											<th>有效投注额</th>
											<th>派彩金额</th>
											<th>盈亏金额</th>
										</tr>
									</thead>
									<tbody class="_downuserList">
										<c:if test="${not empty totalList}">
											<c:forEach items="${totalList }" var="total">
												<tr>
													<td><input type="radio" name="checks" value="${total.uiid}" onclick="changeRadio()" style="opacity:100; text-align: center;"/></td>
													<td><a  style="cursor:pointer" title="点击查询会员投注明细"  onclick="memberBetdetail('${total.account}')">${total.account}</a></td>
													<c:if test="${not empty total.uname}">
														<td>${fn:substring(total.uname, 0, 1)}**</td>
													</c:if>
													<c:if test="${empty total.uname}">
														<td></td>
													</c:if>
													<td class="text-danger">${total.ximascale}</td>
													<td class="text-danger">${total.betTotel}</td>
													<td class="text-danger">${total.betAmountTotal}</td>
													<td class="text-danger">${total.validBetAmountTotal}</td>
													<td class="text-danger">${total.profitamountTotal}</td>
													<td class="text-danger">${total.finalamountTotal}</td>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${empty totalList}">
											<tr><td colspan="9" class="center">暂无记录。</td></tr>
										</c:if>
									</tbody>
								</table>
								
								<c:if test="${not empty totalList}">
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
		
		$("._queryDownUserFrm").click(function(){
			downUserFrm.submit();
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
	            	$("._downuserList").html("<tr><td colspan='9' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
	            	downUserFrm.submit();
	            }
	        }
	        element.bootstrapPaginator(options);
	    })
	    
	    $(".nav-list").find("li").removeClass("active");
		$("#queryDownUser",".nav-list").addClass("active");
	});
	
	// 会员注单明细
	function memberBetdetail(account){
		var startDate=$("#starttime").val();
		var endDate=$("#endtime").val();
		window.location.href="${ctx}userproxy/queryDownUserDetail.do?account="+account+"&startDate="+startDate+"&endDate="+endDate;
	}
	//选择会员
	function changeRadio(){
		var isXimaFlag =$("#isximaFlag").val();
		if(isXimaFlag=='1'){
			$("#ximaBtn").removeAttr("disabled");
		}
	}
	//保存
	function saveXimaScale(){
		var uiid=$("input[name='checks']:checked").val();
		var ximascale=$("#ximaScale").val();
		$.ajax({
			type: "POST",
			url: '${ctx}userproxy/setXimaScale.do',
	    	data: {	uiid:uiid,
	    			ximascale:ximascale
	        },
			dataType:'json',
			cache: false,
			success: function(data){
				if(data.success){	
					alert(data.msg);
					location.href="${ctx}userproxy/queryDownUser.do";
				}else{
					alert(data.msg);
				}
			},
			error:function(){
				alert("网络异常，请稍后再试");
			}
	    		   
		});
	}
	//重置表单
	function resetSetXimaScaleFrm(){
		$("#ximaScale").val('');
	}
	</script>
	</body>
</html>