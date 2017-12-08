<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../config.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保存结果</title>
<base href="<%=basePath%>">
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<script type="text/javascript" src="<%=basePath%>static/js/jquery-1.7.2.js"></script>

</head>
<body>
	<div id="zhongxin"></div>
	<script type="text/javascript">
		var msg = "${msg}";
		var status = "${status}";
		if(status=="success" || status==""){
			document.getElementById('zhongxin').style.display = 'none';
			top.alertBox({type:true,message:msg});
			top.Dialog.close();
		}else{
			top.alertBox({type:false,message:msg});
			top.Dialog.close();
		}
	</script>
</body>
</html>