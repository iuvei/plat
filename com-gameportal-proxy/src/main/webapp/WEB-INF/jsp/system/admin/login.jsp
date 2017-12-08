<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../../../../config.jsp" %>
<!DOCTYPE html>
<html lang="en">

<head>
<title>${pd.SYSNAME}</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" href="/static/favicon.ico" type="image/x-icon" />
<link href="<%=basePath%>static/css/bootstrap.min.css" rel="stylesheet" />
<link href="<%=basePath%>static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="<%=basePath%>static/css/font-awesome.min.css" />
<link rel="stylesheet" href="<%=basePath%>static/css/ace.min.css" />

<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.5.1.min.js"></script>

</head>
<body class="login-layout">
		<div class="container-fluid" id="main-container">
			<div id="main-content">
				<div class="row-fluid">
					<div class="span12">
						
<div class="login-container">
<div class="row-fluid">
	<div class="center">
		<h1><span class="white">代理管理后台</span></h1>
	</div>
</div>
<div class="space-6"></div>
<div class="row-fluid">
<div class="position-relative">
	<div id="login-box" class="visible widget-box no-border">
		<div class="widget-body">
		 <div class="widget-main">
			<h4 class="header lighter bigger text-error" id="to-recover"></h4>
			
			<div class="space-6"></div>
			
			<form>
				 <fieldset>
					<label>
						<span class="block input-icon input-icon-right">
							<input type="text" class="span12" placeholder="请输入用户名" id="loginname"/>
							<i class="icon-user"></i>
						</span>
					</label>
					<label>
						<span class="block input-icon input-icon-right">
							<input type="password" class="span12" placeholder="请输入登录密码" id="password"/>
							<i class="icon-lock"></i>
						</span>
					</label>
					<label>
						<span class="block input-icon input-icon-right">
							<input type="text" class="span12" id="code" class="login_code" placeholder="请输入验证码"/>
							<i class="icon-retweet"><img style="height:22px;" id="codeImg" alt="点击更换"
								title="点击更换" src="" /></i>
							
							
						</span>
					</label>
					<div class="row-fluid">
						<label class="span8" for="remember">
							<input type="checkbox" id="remember" onClick="savePaw();"><span class="lbl"> 记住密码</span>
						</label>
					</div>
					<div class="row-fluid">
						<button type="reset" class="span6 btn btn-small"><i class="icon-refresh"></i> 重置</button>
						<button onClick="return severCheck();" class="span6 btn btn-small btn-success" id="user_login">登录 <i class="icon-arrow-right icon-on-right"></i></button>
					</div>
					
					
				  </fieldset>
			</form>
		 </div><!--/widget-main-->
		
		
		 <div class="toolbar clearfix">
			<div style="width:100%;">
				<!-- <a href="#" onclick="show_box('forgot-box'); return false;" class="forgot-password-link"><i class="icon-arrow-left"></i> </a> -->
				<span style="font-size: 14px;font-weight: bold;font-size: 20px;margin-left:4%;">带您走向成功，月入百万不是梦！</span>
			</div>
		 </div>
		</div><!--/widget-body-->
	</div><!--/login-box-->

</div>
</div>
					</div><!--/span-->
				</div><!--/row-->
			</div>
		</div><!--/.fluid-container-->
		<!-- basic scripts -->
	
		<!-- page specific plugin scripts -->
		
		<!-- inline scripts related to this page -->
		
		<script type="text/javascript">
		
		//服务器校验
		function severCheck(){
			if(check()){
				$("#to-recover").text('正在登录...');
				$("#user_login").attr('disabled',true);
				var loginname = $("#loginname").val();
				var password = $("#password").val();
				var valicode = $("#code").val();
				$.ajax({
					type: "POST",
					url: '<%=basePath%>login/reque',
			    	data: { username:loginname,
		    				password:password,
		    				validationCode:valicode
		    			},
					dataType:'json',
					cache: false,
					success: function(data){
						if("logsuccess" == data.result){
							$("#user_login").removeAttr('disabled');
							saveCookie();
							window.location.href="/manage/index.do";
						}else{
							$("#to-recover").text(data.msg);
							$("#user_login").removeAttr('disabled');
						    return false;
						}
					},
					error : function() {  
						$("#to-recover").text('网络异常，请稍后重试！');
						$("#user_login").removeAttr('disabled');
					}  
				});
			}
			return false;
		}
	
		$(document).ready(function() {
			changeCode();
			$("#codeImg").bind("click", changeCode);
		});

		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				$("#to-recover").trigger("click");
			}
		});

		function genTimestamp() {
			var time = new Date();
			return time.getTime();
		}

		function changeCode() {
			$("#codeImg").attr("src", "<%=basePath%>validation/pcrimg?t=" + genTimestamp());
		}

		//客户端校验
		function check() {
			if ($("#loginname").val() == "") {
				$("#to-recover").text('请输入用户名!');
				$("#loginname").focus();
				return false;
			} else {
				$("#loginname").val(jQuery.trim($('#loginname').val()));
			}

			if ($("#password").val() == "") {
				$("#to-recover").text('请输入登录密码!');
				$("#password").focus();
				return false;
			}
			if ($("#code").val() == "") {
				$("#to-recover").text('请输入验证码!');
				$("#code").focus();
				return false;
			}
			return true;
		}

		function savePaw() {
			if (!$("#remember").attr("checked")) {
				$.cookie('loginname', '', {
					expires : -1
				});
				$.cookie('password', '', {
					expires : -1
				});
				$("#loginname").val('');
				$("#password").val('');
			}
		}

		function saveCookie() {
			if ($("#remember").attr("checked")) {
				$.cookie('loginname', $("#loginname").val(), {
					expires : 7
				});
				$.cookie('password', $("#password").val(), {
					expires : 7
				});
			}
		}
		function quxiao() {
			$("#loginname").val('');
			$("#password").val('');
		}
		
		jQuery(function() {
			var loginname = $.cookie('loginname');
			var password = $.cookie('password');
			if (typeof(loginname) != "undefined"
					&& typeof(password) != "undefined") {
				$("#loginname").val(loginname);
				$("#password").val(password);
				$("#remember").attr("checked", true);
				$("#code").focus();
			}
		});
		//TOCMAT重启之后 点击左侧列表跳转登录首页 
		if (window != top) {
			top.location.href = location.href;
		}
	</script>
	<script src="<%=basePath%>static/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/js/jquery.cookie.js"></script>
	</body>

</html>