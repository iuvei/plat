<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../../../config.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="shortcut icon" href="/static/favicon.ico" type="image/x-icon" />
<base href="<%=basePath%>">
<!-- jsp文件头和头部 -->
<%@ include file="top.jsp"%>

		<style type="text/css">
		.commitopacity{position:absolute; width:100%; height:100px; background:#7f7f7f; filter:alpha(opacity=50); -moz-opacity:0.8; -khtml-opacity: 0.5; opacity: 0.5; top:0px; z-index:99999;}
		.success_ico,.error_ico{background: url(${ctx}static/img/icon_check.png);height: 50px;width: 50px;display: block;overflow: hidden;}
		.success_ico{background-position: 0px -51px; float: left;}
		.error_ico{background-position: -110px -51px;}
		.errorColor{color:#b94a48;}
		.successColor{color:#468847;}
		</style>

</head>
<body>

	<!-- 页面顶部¨ -->
	<%@ include file="head.jsp"%>

	<div class="container-fluid" id="main-container">
		<a href="#" id="menu-toggler"><span></span></a>
		<!-- menu toggler -->

		<!-- 左侧菜单 -->
		<%@ include file="left.jsp"%>

		<div id="main-content" class="clearfix">

			<div id="jzts" style="display:none; width:100%; position:fixed; z-index:99999999;">
			<div class="commitopacity" id="bkbgjz"></div>
			<div style="padding-left: 70%;padding-top: 1px;">
				<div style="float: left;margin-top: 3px;"><img src="<%=basePath%>static/images/loadingi.gif" /> </div>
				<div style="margin-top: -5px;"><h4 class="lighter block red">&nbsp;加载中 ...</h4></div>
			</div>
			</div>

			<div>
				<iframe name="mainFrame" id="mainFrame" frameborder="0" src="${ctx}login/tab" style="margin:0 auto;width:100%;height:100%;"></iframe>
			</div>

			<!-- 换肤 -->
			<div id="ace-settings-container">
				<div class="btn btn-app btn-mini btn-warning" id="ace-settings-btn">
					<i class="icon-cog"></i>
				</div>
				<div id="ace-settings-box">
					<div>
						<div class="pull-left">
							<select id="skin-colorpicker" class="hidden">
								<option data-class="default" value="#438EB9"
									<c:if test="${user.SKIN =='default' }">selected</c:if>>#438EB9</option>
								<option data-class="skin-1" value="#222A2D"
									<c:if test="${user.SKIN =='skin-1' }">selected</c:if>>#222A2D</option>
								<option data-class="skin-2" value="#C6487E"
									<c:if test="${user.SKIN =='skin-2' }">selected</c:if>>#C6487E</option>
								<option data-class="skin-3" value="#D0D0D0"
									<c:if test="${user.SKIN =='skin-3' }">selected</c:if>>#D0D0D0</option>
							</select>
						</div>
						<span>&nbsp; 选择皮肤</span>
					</div>
					<div>
						<label><input type='checkbox' name='menusf' id="menusf"
							onclick="menusf();" /><span class="lbl" style="padding-top: 5px;">菜单缩放</span></label>
					</div>
				</div>
			</div>
			<!--/#ace-settings-container-->
			
		</div>
		<!-- #main-content -->
	</div>
	<!--/.fluid-container#main-container-->
	<!-- basic scripts -->
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='${ctx}static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="<%=basePath%>static/js/bootstrap.min.js"></script>
		<script src="<%=basePath%>static/js/ace-elements.min.js"></script>
		<script src="<%=basePath%>static/js/ace.min.js"></script>
		<!-- 引入 -->
		
		<script type="text/javascript" src="<%=basePath%>static/js/jquery.cookie.js"></script>
		<script type="text/javascript" src="<%=basePath%>static/js/myjs/menusf.js"></script>
		<script type="text/javascript" src="<%=basePath%>static/js/bootbox.min-4.4.js"></script><!-- 确认窗口 -->
		<script type="text/javascript">
		$(function() {
			if (typeof ($.cookie('menusf')) == "undefined") {
				$("#menusf").attr("checked", true);
				$("#sidebar").attr("class", "");
			} else {
				$("#sidebar").attr("class", "menu-min");
			}
		});
		function cmainFrame(){
			var hmain = document.getElementById("mainFrame");
			var bheight = document.documentElement.clientHeight;
			hmain .style.width = '100%';
			hmain .style.height = (bheight  - 51) + 'px';
			var bkbgjz = document.getElementById("bkbgjz");
			bkbgjz .style.height = (bheight  - 41) + 'px';
			
		}
		cmainFrame();
		window.onresize=function(){  
			cmainFrame();
		};
		jzts();
		
		var success = '<i class="success_ico"></i>';
		var error = '<i class="error_ico"></i>';
		//操作提示
		function alertBox(obj){
			var msg = obj.type ? success : error;
			var ico = obj.type ? '<h4 class="successColor">&nbsp;'+obj.message+'</h4>' : '<h4 class="errorColor">&nbsp;'+obj.message+'</h4>';
			bootbox.dialog({
				  message: '<table><tr><td>'+msg+'</td><td>'+ico+'</td></tr></table>',
				  title: obj.type?'<h4 class="successColor">成功提示!</h4>':'<h4 class="errorColor">错误提示!</h4>',
				  // 是否显示body的遮罩，默认true
				  backdrop: false
			});
			//obj.type?3000:4000
			window.setTimeout("bootbox.hideAll()", 3000);
		}
		</script>
</body>
</html>