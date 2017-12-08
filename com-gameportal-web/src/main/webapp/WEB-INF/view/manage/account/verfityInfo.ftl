<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>${title}--手机/邮箱认证</title>
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/user.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/account/index.css" />
    <style type="text/css">
    .myInfo{background-color:#fff!important;}	
    </style>
    <script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/jquery.cookie.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}static/js/public/utils.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/userInfo.js"></script>
	<script type="text/javascript" src="${zy_path}static/js/public/common.js"></script>
      
</head>

<body>
    <div class="title size-bigger">手机/邮箱认证</div>

    <div class="main">
        <div class="user-layer clearfix">
          <div class="auth-info user-layer myInfo">
            <ul>
            	<#if userInfo.phonevalid ==0> 
	                <li>
	                    <label>验证手机号码：</label>
	                    <input class="phone ui-radius" type="tel" value ="${userInfo.phone}" type="text" id="telphone" maxlength="11"  placeholder="验证手机号码" />
	                    <a class="btn-phone size-small ui-alignCenter ui-radius color-white bg-orange bg-brownHover ui-transition" href="javascript:sendSms();" id="getSmsCode">发送手机验证码</a> 
	                    <input class="v-code ui-radius" type="text" id="vcode" maxlength="6" placeholder="手机验证码" />
	                    <a class="color-orange color-brownHover" href="javascript:verificationPhone();" id="verifyPhone">立即验证</a>
	                </li>
	            <#else>
	            	<li>
	                    <label>验证手机号码：</label>
	                    <label>${userInfo.phone?substring(0,3)}****${userInfo.phone?substring(7)}</label>
	                   	<span class="color-green">已认证</span>
	                </li>
                </#if>
                <#if userInfo.emailvalid ==0>
	                <li>
	                    <label>E-mail邮箱：</label>
	                    <input class="email ui-radius" id="email" name="email" type="text" value="${userInfo.email}" placeholder="E-mail邮箱" maxlength="60" />
	                    <a class="btn-email size-small ui-alignCenter ui-radius color-white bg-orange bg-brownHover ui-transition" id="verifyEmail" href="javascript:;">立即申请邮箱认证</a>
	                </li>
	            </#if>
	            <#if userInfo.emailvalid ==1>
	                <li>
	                    <label>E-mail邮箱：</label>
	                    <label style="width:150px;">***${userInfo.email?substring(3)}</label>
	                   	<span class="color-green" style="margin-left:10px;">已认证</span>
	                </li>
	            </#if>
	            <#if userInfo.emailvalid ==2>
	          		<li>邮箱认证申请已经提交,请稍后打开您的邮箱进行认证！</li>
	    		</#if>
	    		<#--
	            <#if userInfo.emailvalid !=1 && activty_38>
	                <li>本时间段信息认证送礼金剩余<span class="total color-orange">${places}</span> 个名额 <a class="color-orange color-brownHover" href="javascript:;" id="refresh">刷新</a></li>
	    		</#if>
	    		-- >
            </ul>
        </div>
    </div>
	
</body>
</html>