<!doctype html>
<html lang="zh-cn">
<head>
	<#include "${action_path}common/config.ftl">
   	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}注册</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="${action_path}static/css/sign.css" />
    <link type="text/css" rel="stylesheet" href="${action_path}css/register.css">
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
</head>
<body class="bg-gray">
<div class="ui-none">
 <!--头部-->
<#include "${action_path}common/top.ftl">
</div>
<header id="header" class="header">
    <!--head-top{-->
    <div class="logo container">
        <a href="/" class="logo-pic fl"><img src="${action_path}images/logo.png" alt=""></a>
        <span class="link-login fr">欢迎注册钱宝娱乐 已注册？<a href="/">请登录</a></span>
    </div>
    <!--}head-top-->
</header>
<div class="reg-wp">
    <div class="container reg-inner-wp">
        <form id="registrationtable" name="registrationtable" action="" method="post" class="form-reg">
            <div class="reg-tit">填写注册信息</div>
            <div class="ipt-group">
                <label for="" class="label rq-value"><font color="red">*</font>&nbsp;用户名：</label>
                <input class="ipt-txt" type="text" maxlength="20" name="username" id="account" data-rule-register-username="true"  placeholder="6-11个字符,含有字母和数字的组合"/>
                    <span class="ipt-tip" id="accountTip">
                      	用户名由6-11个数字或英文字母组成
                    </span>
            </div>
            <div class="ipt-group">
                <label for="" class="label rq-value"><font color="red">*</font>&nbsp;登录密码：</label>
                <input type="password" style="display: none;"/>
                <input class="ipt-txt"   type="password" maxlength="20" name="password" id="passwd" placeholder="请输入您的登录密码" />
                    <span class="ipt-tip" id="passwdTip">
                     	 登录密码由6-12数字和英文字母组成
                    </span>
            </div>
            <div class="ipt-group">
                <label for="" class="label rq-value"><font color="red">*</font>&nbsp;确认密码：</label>
                <input class="ipt-txt"  type="password" name="confirm_password" maxlength="20" id="surepasswd" placeholder="请再次输入登录密码" />
                    <span class="ipt-tip" id="surePasswdTip">
                      	请再次确认登录密码
                    </span>
            </div>

            <div class="ipt-group">
                <label for="" class="label rq-value"><font color="red">*</font>&nbsp;联系电话：</label>
                <input class="ipt-txt" type="text" name="phone" id="phone" maxlength="11"  placeholder="请输入您的联系电话"/>
                    <span class="ipt-tip" id="phoneTip">
                     	 若申请优惠，我们须和你本人进行电话核实
                    </span>
            </div>
            <div class="ipt-group">
                <label for="" class="label rq-value"><font color="red">*</font>&nbsp;验证码：</label>
                <input style="width: 274px;" class="ipt-txt" type="text" name="validateCode" id="vcode" placeholder="请输入您的验证码" />

            <span class="am-u-md-6">
              <img id="imgCodeAgent" width="60" height="36" src="${action_path}validationCode/agentcode.do" title="如果看不清验证码，请点图片刷新"/>
            </span>
            <span class="ipt-tip" id="vcodeTip">
            	  请输入左侧验证码
            </span>
            </div>
            <div class="ipt-group pl">
                <label for="" class="">
                    	点击“立即注册”，即表示您同意并愿意遵守钱宝娱乐城
                    <a target="_blank" href="javascript:;" class="link">《用户协议》</a>和
                    <a target="_blank" href="javascript:;" class="link">《隐私条款》</a>
                </label>
            </div>
            <div class="ipt-group pl">
                <input type="button" class="btn btn-primary btn-reg" value="立即注册" id="register_sub" />
            </div>
        </form>
        <img class="reg-pic" src="${action_path}images/reg-promotion.jpg" alt="">
    </div>

<div class="footer-wp-2">
    友情提示：博彩有风险，量力乐其中 Copyright © 2015 钱宝 All Rights Reserved
</div>
</div>
<#--底部-->
<#include "${action_path}common/footer.ftl">
<script src="${action_path}js/jquery.js"></script>
<script src="${action_path}js/countUp.js"></script>
<script src="${action_path}static/js/public/utils.js"></script>
<script src="${action_path}static/js/public/common.js"></script>
<script src="${action_path}js/register.js?v=4"></script>
</body>
</html>