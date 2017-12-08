<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN" style="overflow: hidden; height: 100%;">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title><%=title%> - 代理转账记录</title>
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
							<li class="active">下线转账记录</li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
							<form class="form-search" action="${ctx}userproxy/downtransferlist.do" method="post" name="transferlogsFrm">
									<span class="input-icon">
										<label>会员账号：</label>
										<input type="text" id="uaccount" name="uaccount" class="form-control"  value="${pd.uaccount}">
									</span>
									<span class="input-icon">
										<label>开始时间：</label>
										<input type="text" id="starttime" name="startDate" class="form-control  date-picker" placeholder="开始时间" data-date-format="yyyy-mm-dd" style="width:110px;" value="${pd.startDate}">
									</span>
									<span class="input-icon">
											<label>结束时间：</label>
								<input type="text" id="endtime" name="endDate" class="form-control  date-picker" placeholder="结束时间" data-date-format="yyyy-mm-dd" style="width:110px;" value="${pd.endDate}">
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
								<table id="table_transferlog" class="table table-striped table-bordered table-hover dataTable">
									<thead>
										<tr>
											<th>转账时间</th>
											<th>收款人</th>
											<th>转账金额</th>
											<th>转账前金额</th>
											<th>转账后金额</th>
										</tr>
									</thead>
									<tbody class="_logList">
										<c:if test="${not empty logs}">
											<c:forEach items="${logs}" var="log">
												<tr>
													<td><fmt:formatDate value="${log.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
													<td>${log.laccount }</td>
													<td>${log.amount }</td>
													<td>${log.beforebalance }</td>
													<td>${log.afterbalance }</td>
												</tr>
											</c:forEach>
										</c:if>
										<c:if test="${empty logs}">
											<tr><td colspan="5" class="center">暂无记录！</td></tr>
										</c:if>
									</tbody>
								</table>
								<c:if test="${not empty logs}">
									<div class="row-fluid" style="border-top:none;">
										<div class="span6"><div class="dataTables_info" style="padding-top:8px;">共 ${page.totalResult } 条数据 - 每页显示${page.showCount }条 - 共 ${page.totalPage }页</div></div>
										<div class="span6" style="padding-top:8px;">
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

	
	$("._querytransferlogsFrm").click(function(){
		transferlogsFrm.submit();
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
            	$("._logList").html("<tr><td colspan='5' class='center'><img src='/static/images/loadingi.gif'/>&nbsp;&nbsp;加载中,请稍后...</td></tr>");
            	transferlogsFrm.submit();
            }
        }
        element.bootstrapPaginator(options);
    })
});
</script>
</body>
</html>