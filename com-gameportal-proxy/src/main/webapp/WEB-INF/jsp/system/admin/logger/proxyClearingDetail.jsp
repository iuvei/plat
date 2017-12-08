<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title><%=title%> - 我的结算详情</title>
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
							<li class="active">${proxyobj.proxyaccount }结算详情</li>
						</ul>
						
						<!--.breadcrumb-->
						<div id="nav-search"></div><!--#nav-search-->

					</div><!--#breadcrumbs-->

					<div id="page-content" class="clearfix">
						<div class="page-header position-relative" style="padding-bottom:0px;">
								<!-- 条件查询开始 -->
								<form class="form-inline" action="${ctx}userproxy/proxyClearingDetail.do" method="post" name="queryProxyClearingFrms">
									<span class="input-icon">
										<button class="btn btn-danger btn-small" onclick="javascript:history.back();">返回<i class="icon-reply icon-2x icon-only"></i></button>
									</span>
									<input type="hidden" name="account" value="${proxyobj.proxyaccount }" />
									<span class="input-icon">
										<label>开始时间：</label>
										<input type="text" id="starttime" name="startDate" class="form-control  date-picker" placeholder="开始时间" data-date-format="yyyy-mm-dd" style="width:110px;" value="${pd.startdate}">
									</span>
									<span class="input-icon">
										<label>结束时间：</label>
										<input type="text" id="endtime" name="endDate" class="form-control  date-picker" placeholder="结束时间" data-date-format="yyyy-mm-dd" style="width:110px;" value="${pd.enddate}">
									</span>
									<button type="submit" class="btn btn-primary _queryProxyClearingFrms"><i class="icon-search icon-on-right"></i>搜索</button>
								</form>
							</div>
						<!-- PAGE CONTENT BEGINS HERE -->
						<div class="row-fluid">
							<div class="span12">
								<table id="table_paylog" class="table table-striped table-bordered table-hover">
									<tbody>
										<tr>
											<td colspan="4" style="background-color:#eff3f8;">
												<strong>代理信息</strong><strong></strong><strong></strong><br />
											</td>
										</tr>
										<tr>
											<td style="width:120px;">
												代理帐号：
											</td>
											<td>
												${proxyobj.proxyaccount}
											</td>
											<td style="width:120px;">
												代理姓名：
											</td>
											<td>
												${proxyobj.proxyname}
											</td>
										</tr>
										<tr>
											<td>
												代理域名：
											</td>
											<td>
												${proxyobj.proxydomain}
											</td>
											<td>
												代理模式：
											</td>
											<td>
												<c:choose>
													<c:when test="${proxyobj.clearingtype==1}"><font color="red">按月洗码</font></c:when>
													<c:when test="${proxyobj.clearingtype==2}"><font color="red">按天洗码</font></c:when>
													<c:otherwise><font color="green">输值结算</font></c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<%-- <td>
												占成比例：
											</td>
											<td>
												${proxyobj.returnscale}
											</td> --%>
											<td>
												是否洗码：
											</td>
											<td>
												<c:choose>
													<c:when test="${proxyobj.ximaStatus == 1 }"><font color="green">是</font></c:when>
													<c:otherwise>	<font color="red">否</font></c:otherwise>
												</c:choose>
											</td>
											<td>
												洗码比例：
											</td>
											<td>
												${proxyobj.ximascale}
											</td>
										</tr>
										<tr>
											<td>
												下线总人数：
											</td>
											<td colspan="3">
												${proxyobj.lowerUser}人
											</td>
										</tr>
										<tr>
											<td colspan="4" style="background-color:#eff3f8;">
												<strong>推广数据</strong>（以下数据是 ${pd.startdate} 至 ${pd.enddate} 的数据）<br />
											</td>
										</tr>
										<tr>
											<td>
												注册人数：
											</td>
											<td>
												${proxyobj.lowecCount}人
											</td>
											<td>
												活跃人数：
											</td>
											<td>
												${proxyobj.activeUser}人
											</td>
										</tr>
										<tr>
											<td>
												充值人数：
											</td>
											<td>
												${proxyobj.payusercount}人
											</td>
											<td>
												充值比数：
											</td>
											<td>
												${proxyobj.paycount}笔
											</td>
										</tr>
										<tr>
											<td>
												充值金额：
											</td>
											<td>
												${proxyobj.payAmountTotal}元（RMB）
											</td>
											<td>
												提款金额：
											</td>
											<td>
												${proxyobj.atmAmountTotal}元（RMB）
											</td>
										</tr>
										<tr>
											<td>
												提款人数：
											</td>
											<td>
												${proxyobj.atmusercount}人
											</td>
											<td>
												提款比数：
											</td>
											<td>
												${proxyobj.atmcount}笔
											</td>
										</tr>
										<tr>
											<td>
												扣款金额：
											</td>
											<td colspan="3">
												${proxyobj.buckleAmount}元（RMB）
											</td>
										</tr>
										<tr>
											<td colspan="4" style="background-color:#eff3f8;">
												<strong>盈亏数据</strong><br />
											</td>
										</tr>
										<tr>
											<td>
												注单数量：<br />
											</td>
											<td>
												${proxyobj.betTotel} 注
											</td>
											<td>
												总投注额：
											</td>
											<td>
												${proxyobj.betAmountTotal}元（RMB）
											</td>
										</tr>
										<tr>
											<td>
												有效投注额：<br />
											</td>
											<td>
												${proxyobj.validBetAmountTotal}元（RMB）
											</td>
											<td>
												总优惠：
											</td>
											<td>
												${proxyobj.preferentialTotal}元（RMB）
											</td>
										</tr>
										<tr>
											<td>
												总洗码：<br />
											</td>
											<td>
												${proxyobj.ximaAmountTotal}元（RMB）
											</td>
											<td>
												游戏输赢：
											</td>
											<td>
												<font color="green">${proxyobj.winlossTotal}</font>元（RMB）
											</td>
										</tr>
										<tr>
											<td>
												累计负盈利：
											</td>
											<td colspan="3">
												<font color="red">${proxyobj.recordAmount}</font>元（RMB）
											</td>
										</tr>
										<tr>
											<td>
												实际盈亏：
											</td>
											<td>
												<font color="green">${proxyobj.realPLs}</font>元（RMB）
											</td>
											<td colspan="2">
												<font color="red">小提示：实际盈亏 = 游戏输赢-(总洗码+总优惠)+总扣款</font>
											</td>
										</tr>
										<tr>
											<td>
												占成佣金：
											</td>
											<td>
												<font color="green">${proxyobj.commissionAmount}</font>元（RMB）
											</td>
											<td colspan="2">
												<font color="red">小提示：占成佣金 = 实际盈亏*占成比例</font>
											</td>
										</tr>
									</tbody>
								</table>
								
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
			$("._queryProxyClearingFrms").click(function(){
				queryProxyClearingFrms.submit();
			});
			
			$(".nav-list").find("li").removeClass("active");
			$("#queryDownProxy",".nav-list").addClass("active");
		});
		
	</body>
</html>