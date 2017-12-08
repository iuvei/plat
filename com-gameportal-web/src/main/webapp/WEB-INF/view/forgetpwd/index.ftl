<#include "common/config.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--找回密码</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/sign.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/public/utils.js"></script>
    <script src="${zy_path}js/main.js"></script>
    <script type="text/javascript" src="${zy_path}js/forgetpwd.js"></script>
</head>

<body>
    <div class="wrapper ui-header border-bottom-brown" id="ui-header">
        <div class="inner pr">
            <span class="pa slash-left-orange"></span>
            <a href="${zy_path}index.html"><img class="logo-website" src="${zy_path}static/images/public/logo.png" alt="钱宝娱乐 logo" title="钱宝娱乐" /></a>
            <ul class="helper">
                <li>
                    <a class="ui-block color-light-gray color-orange-hover" href="${zy_path}">首页</a>
                </li>
                <li>
                    <a class="ui-block color-light-gray color-orange-hover" href="/xsbz/about.html">帮助中心</a>
                </li>
                <li>
                    <a class="ui-block color-light-gray color-orange-hover" href="javascript:;" onclick="joinFavorite(window.location.href,document.title);">加入收藏</a>
                </li>
                <li>
                    <a class="ui-block color-light-gray color-orange-hover" href="/lxwm/about.html">联系我们</a>
                </li>
            </ul>
            <span class="pa slash-rightOrange"></span>
        </div>
    </div>

    <div class="wrapper content border-bottomBrownLight">
        <div class="inner">
            <div class="sign-box login pa  color-dark">
                <h1 class="size-big ui-mt5">找回密码
                <p class="fr size-normal">使用其他帐号 
                        <a class="color-orange color-brownHover" href="${zy_path}login.html">登陆</a>
                    </p></h1>
                <form action="javascript:;">
                    <div class="ui-inputBox withIcon username">
                        <label><span class="pa icon"></span>
                            <input class="ui-block" name="txtAccount" type="text" id="account" maxlength="15" value="" placeholder="您的用户名" autofocus />
                        </label>
                        <p class="size-small error ui-none" id="account_tip">6-8个字符,含有字母和数字的组合</p>
                    </div>
                    <div class="ui-inputBox withIcon phone">
                        <label><span class="pa icon"></span>
                            <input class="ui-block" name="telphone" id="telphone" type="tel" maxlength="11" placeholder="您的手机号码" />
                        </label>
                        <p class="size-small error ui-none" id="telphone_tip">验证通过</p>
                    </div>
                    <div class="ui-inputBox withIcon verifyCode">
                        <label><span class="pa icon"></span>
                            <a class="fr" href="javascript:imgCode2();"><img src="${action_path}validationCode/agentcode.do" id="imgCodeAgent"/></a>
                            <input class="ui-block"  name="txtCaptcha" type="text" maxlength="4" id="vcode" value="" placeholder="验证码" />
                        </label>
                        <p class="size-small error ui-none" id="vcode_tip">验证通过</p>
                    </div>
                    <input type="button" id="LbtnSubmit" onclick="getPwd()" class="submit ui-btnHigh ui-block ui-radius color-white bg-orange bg-brownHover ui-transition" value="找回密码" />
                    <p class="ui-mt20 size-small color-red" id="_tip">温馨提示：新密码将以短信方式发送给您，如果您的预留手机号无法接收短信，请联系在线客服！</p>
                </form>
            </div>
        </div>
    </div>

    <#include "${zy_path}common/footer.ftl" />
	
</body>

</html>