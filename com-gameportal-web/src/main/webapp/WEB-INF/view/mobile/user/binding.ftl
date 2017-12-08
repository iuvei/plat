<!DOCTYPE html>
<html>
<head>
	<#include "${action_path}common/config.ftl">
	${meta}
    <title>${title}--绑卡</title>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0" />
	<meta name="format-detection" content="telephone=no" />
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/public/base.css" />
	<script type="text/javascript" src="${zy_path}mstatic/js/public/base.min.js"></script>
</head>
<body class="bg-main ft-normal">
	<header id="header"></header>
    <div class="box-wrap-bg-white ft-30">
        <a class="ui-item" href="${action_path}mobile/bindCardmp.html">
            <span>银行卡</span> <span><i class="icon-31fanhui2 ft-36"></i></span>
        </a>
        <a class="ui-item" href="${action_path}mobile/bindalipaymp.html">
            <span>支付宝</span> <span><i class="icon-31fanhui2 ft-36"></i></span>
        </a>
    </div>
</body>
</html>