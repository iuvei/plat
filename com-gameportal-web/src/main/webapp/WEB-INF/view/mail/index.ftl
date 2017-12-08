<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${title} - 电子邮箱认证</title>
	${meta}
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <style type="text/css">
        .ui-contents {
            height: 510px;
        }
        
        .main {
            width: 900px;
            height: 350px;
            margin: 40px 0 0 50px;
        }
        
        .main h1 {
            height: 60px;
            line-height: 60px;
            font-weight: normal;
            font-size: 24px;
        }
        
        .main img {
            margin: 70px 0 0 80px;
        }
        
        .main p {
            width: 600px;
            margin: 120px 0 0 250px;
            font-size: 20px;
        }
    </style>
	<script>
		function goToPage(param) {
		    $.cookie('treeAbbr', param, {
		        path: "/"
		    });
	        top.window.location = "/manage/index.do";
		}
	</script>
</head>

<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <!--头部-->
    <#include "${action_path}common/top.ftl">
    <!--导航-->
    <#include "${action_path}common/header.ftl">
</div>

    <div class="wrapper border-bottomBrownLight ui-transition" id="container" style="background:none">
        <div class="inner">
            <div class="main pa bg-white" id="main" style="border: 1px solid #3071E7">
                <h1 class="bg-orange ui-alignCenter">电子邮箱认证</h1>
                <#if successMsg??>
	                <img class="pa" src="/static/images/public/success.png">
	                <#if activty38??>
		                <p class="pa color-green">${successMsg}</p>
        			</#if>
                </#if>
                
                <#if errorMsg??>
                	<img class="pa" src="/static/images/public/failure.png">
	                <p class="pa color-green">${errorMsg}</p>
                </#if>
            </div>
        </div>
    </div>
<!--客服-->
<#include "${action_path}common/rightSlider.ftl">
</body>

</html>