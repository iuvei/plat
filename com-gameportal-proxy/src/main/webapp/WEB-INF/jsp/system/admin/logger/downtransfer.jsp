<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../config.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN" style="overflow: hidden; height: 100%;">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title><%=title%> - 下级会员转账</title>
<link rel="stylesheet" href="/static/css/chosen.css">
<link rel="stylesheet" href="/static/css/jquery-ui-1.10.2.custom.min.css">

<%@include file="../include/meta.jsp"%>
<%@include file="../include/css.jsp"%>
</head>
<body>
<%@include file="../top.jsp" %>
 <div class="container-fluid" id="main-container">
	<a href="#" id="menu-toggler"><span></span></a><!-- menu toggler -->
	<%@include file="../include/left.jsp" %>
	<div id="main-content" class="clearfix">
			<div id="breadcrumbs">
				
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="/manage/index.do">Home</a><span class="divider"><i class="icon-angle-right"></i></span></li>
					<li class="active">下线会员转账</li>
				</ul>
				<!--.breadcrumb-->
				<div id="nav-search"></div><!--#nav-search-->

			</div><!--#breadcrumbs-->

			<div id="page-content" class="clearfix">
				
				<div class="row-fluid">
					<!-- PAGE CONTENT BEGINS HERE -->
					<div class="row-fluid">
						<div class="span12">
							<div class="widget-box">
								<div class="widget-header widget-header-blue widget-header-flat wi1dget-header-large">
									<h4 class="lighter">请填写转账信息</h4>
								</div>
								<div class="widget-body">
								 <div class="widget-main">
									<div class="row-fluid">
										<div id="fuelux-wizard" class="row-fluid">
										  <ul class="wizard-steps">
											<li data-target="#step1" class="active" style="min-width: 25%; max-width: 25%;"><span class="step">1</span> <span class="title">填写信息</span></li>
											<li data-target="#step2" style="min-width: 25%; max-width: 25%;" class=""><span class="step">2</span> <span class="title">确认转账</span></li>
											<li data-target="#step3" style="min-width: 25%; max-width: 25%;" class=""><span class="step">3</span> <span class="title">转账完成</span></li>
										  </ul>
										</div>
										<hr>
										<div class="step-content row-fluid position-relative">
											<div class="step-pane active" id="step1">
												<form class="form-horizontal">
													<div class="control-group">
														<label class="control-label">当前额度：</label>
														<label style="padding-top:7px;">&nbsp;&nbsp;<font color="green" id="balance">${accountMoney.totalamount}</font></label>
													</div>
													<div class="control-group">
														<label class="control-label" for="lowAccount">收款人：</label>
														<div class="controls">
															<input type="text" id="lowAccount" style="height:30px;">
															<span><font color="#d16e6c">只能输入您的直接下级会员账号</font></span>
														</div>
													</div>
													<div class="control-group">
														<label class="control-label" for="amount">转账金额：</label>
														<div class="controls">
															<input type="text" id="amount" style="height:30px;">
															<span><font color="#d16e6c">不能大于您当前的额度且只能输入整数</font></span>
														</div>
													</div>
												</form>
											</div>
											
											<div class="step-pane" id="step2">
												<form class="form-horizontal">
													<div class="control-group">
														<label class="control-label">当前额度：</label>
														<label style="padding-top:7px;">&nbsp;&nbsp;${accountMoney.totalamount}</label>
													</div>
													<div class="control-group">
														<label class="control-label">收款人：</label>
														<div class="controls">
															<label style="padding-top:7px;" id="tranferAccount"></label>
														</div>
													</div>
													<div class="control-group">
														<label class="control-label">转账金额：</label>
														<div class="controls">
															<label style="padding-top:7px;color:red;" id="tranferAmount"></label>
														</div>
													</div>
													<div class="control-group">
														<label class="control-label">转账后额度：</label>
														<div class="controls">
															<label style="padding-top:7px;color:red;" id="afterBalance"></label>
														</div>
													</div>
												</form>
											</div>
											
											<div class="step-pane" id="step3">
												<div class="center">
													<h3 class="green" id="result">恭喜，转账成功!</h3>
													<button class="btn btn-success btn-next" id="oper_ok">确&nbsp;定</button>
												</div>
											</div>
										</div>
										
										<hr>
										
										<div class="row-fluid wizard-actions">
											<button class="btn btn-prev"><i class="icon-arrow-left"></i>上一步</button>
											<button class="btn btn-success btn-next" data-last="完成 ">下一步<i class="icon-arrow-right icon-on-right"></i></button>
										</div>
									</div>
								 </div><!--/widget-main-->
								</div><!--/widget-body-->
							</div>
						</div>
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
<script src="${ctx }plugins/js/qunit-1.11.0.js"></script>
<script src="${ctx }static/js/fuelux.wizard.js"></script>
<script src="${ctx }static/js/chosen.jquery.min.js"></script>
<script type="text/javascript">
$(function() {
	$('#fuelux-wizard').ace_wizard().on('change' , function(e, info){
		if(info.step == 1){
			return check();
		}else if(info.step == 2){
			$(".btn-next").attr("disabled",true);
			$.ajax({
				type : "POST",
				url : "/userproxy/proxyTransfer.do",
				data : {"account":$("#lowAccount").val(),"amount": $("#amount").val()},
				async : false,
				dataType : "json",
				success : function(data) {
					$(".btn-next").removeAttr("disabled");
					if(data.result =='1'){
						/**
						layer.alert('操作提示', {
						  icon: 1,
						  content:data.msg,
						  skin: 'layui-layer-lan'
						});
						*/
					}else{
						$("#result").html(data.msg);
						layer.alert('操作提示', {
						  icon: 2,
						  content:data.msg,
						  skin: 'layui-layer-lan'
						});
					}
				},
				error : function() {
					layer.alert('操作提示', {
					  icon: 2,
					  content:'系统异常，请稍后再试!',
					  skin: 'layui-layer-lan'
					});
					return false;
				}
			});
		}
	});
	
	$("#oper_ok").click(function(){
		window.location.href="/userproxy/membertransfer.do";
	});
});
function check(){
	var low = $("#lowAccount");
	var amount = $("#amount"); 
	var reg =/^[0-9]*[1-9][0-9]*$/;
	var flag = true;
	if(low.val() ==''){
		layer.alert('操作提示', {
		  icon: 2,
		  content:'请输入收款人账号!',
		  skin: 'layui-layer-lan'
		});
		low.focus();
		return false;
	}
	if(amount.val() ==''){
		layer.alert('操作提示', {
		  icon: 2,
		  content:'请输入转账金额!',
		  skin: 'layui-layer-lan'
		});
		amount.focus();
		return false;
	}
	if(isNaN(amount.val())){
		layer.alert('操作提示', {
		  icon: 2,
		  content:'转账金额输入有误!',
		  skin: 'layui-layer-lan'
		});
		amount.focus();
		return false;
	}
	if(!reg.test(amount.val())){
		layer.alert('操作提示', {
		  icon: 2,
		  content:'转账金额只能输入整数!',
		  skin: 'layui-layer-lan'
		});
		amount.focus();
		return false;
	}
	if(parseInt($("#balance").text()) <parseInt(amount.val())){
		layer.alert('操作提示', {
		  icon: 2,
		  content:'转账金额不能大于您的当前额度!',
		  skin: 'layui-layer-lan'
		});
		amount.focus();
		return false;
	}
	$.ajax({
		type : "POST",
		url : "/userproxy/validateLower.do",
		data : {"account":low.val(),"amount":amount.val()},
		async : false,
		dataType : "json",
		success : function(data) {
			if(data.result =='1'){
				$("#tranferAccount").html("&nbsp;"+low.val());
				$("#tranferAmount").html("&nbsp;"+amount.val());
				$("#afterBalance").html("&nbsp;"+data.msg);
				flag=true;
			}else{
				if(data.type =='1'){
					layer.alert('操作提示', {
					  icon: 2,
					  content:data.msg,
					  skin: 'layui-layer-lan'
					});
					$("#lowAccount").focus();
					flag=false;
				}else{
					layer.alert('操作提示', {
					  icon: 2,
					  content:data.msg,
					  skin: 'layui-layer-lan'
					});
					amount.focus();
					flag=false;
				}
			}
		},
		error : function() {
			layer.alert('操作提示', {
				  icon: 2,
				  content:'系统异常，请稍后再试',
				  skin: 'layui-layer-lan'
				});
			flag=false;
		}
	});
	return flag;
}
</script>
</body>
</html>