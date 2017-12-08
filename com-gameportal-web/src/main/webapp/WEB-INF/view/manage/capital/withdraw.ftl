<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--我要提款</title>
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/user.css?v=2" />
    <script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/public/utils.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/css/user/capital/deposit.css" />
    <script type="text/javascript" src="${zy_path}static/js/user/user.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script src="${zy_path}js/withdraw.js"></script>
</head>

<body>


    <div class="title size-bigger">我要提款</div>
    <div class="main">
    	<div class="user-layer ui-mt30" id="withdraw_content">
    		<#if cardPackage?? && !userInfo.atmpasswd??>
	            <form action="javascript:;">
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 设置提款密码：</span>
	                        <input class="fl short" id="atmpasswd" maxlength="50" type="password" value="" placeholder="请输入提款密码" />密码长度6-15个字符,必须含有字母和数字的组合。
	                    </label>
	                    <p id="atmpasswd_tip" class="size-tiny long error ui-none">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 确认提款密码：</span>
	                        <input class="fl short" id="atmpwdok" maxlength="50" type="password" value="" placeholder="请输入确认提款密码" />请再次输入您的提款密码。
	                    </label>
	                    <p id="atmpwdok_tip" class="size-tiny long error ui-none">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                        <input class="verifyCode fl short" name="vcode" maxlength="4" id="atmvcode" type="text" value="" placeholder="请输入验证码" />
	                        <a class="verifyCode fl" href="javascript:imgCode();"><img id="imgCode" class="ui-block" src="${action_path}validationCode/pcrimg.do" /></a>请输入验证码。
	                    </label>
	                    <p id="atmvcode_tip" class="size-tiny long error ui-none">验证通过</p>
	                </div>
	                <input type="button" id="setAtmPwd_sub" class="submit ui-btn ui-block ui-radius color-white bg-orange bg-brownHover ui-transition short" id="submit" value="提交" />
	            </form>
    		<#else>
    			<#if cardPackage?? && userInfo.atmpasswd??>
	                <div class="ui-inputBox ui-mt20 ui-none">
	                    <label><span class="fl long"><font class="color-orange">*</font> 提款到：</span>
	                        <div class="ui-ddl fl size-small color-gray ui-radius short">
	                        	<#if !cardPackage.cardnumber?? && cardPackage.alipay??> 
		                            <div class="input fl ui-hand ui-textOverflow" data="2" id="way">支付宝</div>
								<#else>
	                            	<div class="input fl ui-hand ui-textOverflow" data="1" id="way">银行卡</div>
								</#if>
	                            <span class="fl"></span>
	                            <ul class="ui-radius" >
	                                <li data="1" onclick="payTypeChange(1)">银行卡</li>
	                                <li data="2" onclick="payTypeChange(2)">支付宝</li>
	                            </ul>
	                        </div>
	                    </label>
	                </div>
		            
		            
		            <form action="javascript:;" <#if !cardPackage.cardnumber?? && cardPackage.alipay??> class="ui-none"</#if> >
		              	<div class="ui-inputBox">
		                    <label><span class="fl long">钱包余额：</span>
		                        ${myBalance}&nbsp;元
		                    </label>
		                </div>
		                <div class="ui-inputBox">
		                   <input class="fl size-small" type="hidden" value="${cardPackage.getBankname()!}" id="caseoutBankname" />
		                   <label><span class="fl long">提款到账银行：</span>
		                        ${cardPackage.getBankname()!}
		                    </label>
		                </div>
		                <div class="ui-inputBox">
		                   <input class="fl size-small" type="hidden" value="${cardPackage.getAccountname()!}" id="caseoutBankperson" />
		                   <label><span class="fl long">账户姓名：</span>
		                        ${cardPackage.getAccountname()!}
		                    </label>
		                </div>
		                <div class="ui-inputBox">
		                   <input class="fl size-small" type="hidden" value="${cardPackage.getCardnumber()!}" id="caseoutBankcard" />
		                   <label><span class="fl long">银行账户：</span>
		                        ${cardPackage.getCardnumber()!}
		                   </label>
		                </div>
		            </form>
		            
		            <form action="javascript:;" <#if cardPackage.cardnumber?? || !cardPackage.alipay??> class="ui-none"</#if> >
		                <div class="ui-inputBox">
		                    <label><span class="fl long"><font class="color-orange">*</font> 支付宝真实姓名：</span>
		                        <input class="fl size-small" type="text" id="alipayname" value="${cardPackage.alipayname!""}" placeholder="请输入支付宝真实姓名" readOnly/><span class="size-small">提款到账的支付宝真实姓名。</span>
		                    </label>
		                   <p id="alipayname_tip" class="size-tiny long error ui-none">验证通过</p>
		                </div>
		                <div class="ui-inputBox">
		                    <label><span class="fl long"><font class="color-orange">*</font> 支付宝账号名：</span>
		                        <input class="fl size-small" type="text" id="alipay" value="${cardPackage.alipay!""}" placeholder="请输入支付宝账号名" readOnly/><span class="size-small">提款到账支付宝账号。</span>
		                    </label>
		                     <p id="alipay_tip" class="size-tiny long error ui-none">验证通过</p>
		                </div>
		            </form>
		            
		            <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 提款金额：</span>
	                        <input class="fl size-small" type="text" value="" id="caseoutFigure" placeholder="请输入提款金额" /><span class="size-small">请填写提款金额。</span>
	                    </label>
	                     <p id="caseoutFigure_tip" class="size-tiny long error ui-none">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 提款密码：</span>
	                        <input class="fl size-small" type="password" value="" id="caseoutPwd" placeholder="请输入提款密码" /><span class="size-small">请填写提款密码。</span>
	                    </label>
	                     <p id="caseoutPwd_tip" class="size-tiny long error ui-none">验证通过</p>
	                </div>
	                
	                <#if userInfo.phonevalid == 0>
		                <div class="ui-inputBox">
		                    <label><span class="fl long"><font class="color-orange">*</font> 手机号码：</span>
		                        <input class="fl size-small" type="text" id="telphone" maxlength="11" placeholder="请输入手机号码" value="${userInfo.phone}"/><span class="size-small">手机号码。</span>
		                    </label>
		                     <p id="phone_tip" class="size-tiny long error ui-none">验证通过</p>
		                </div>
		                <div class="ui-inputBox">
		                    <label><span class="fl long"><font class="color-orange">*</font> 短信验证码：</span>
		                        <input id="vcode" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入短信验证码" />
		                        <a class="SMSCode size-small ui-alignCenter color-white bg-orange bg-brownHover ui-radius ui-transition" href="javascript:sendSms();" id="getSmsCode">发送手机验证码</a>
		                    </label>
		                     <p id="vcode_tip" class="size-tiny long error ui-none">验证通过</p>
		                </div>
		            <#else>
		            	<div class="ui-inputBox">
		                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
		                        <input class="verifyCode fl short" name="vcode" maxlength="4" id="vcode" type="text" value="" placeholder="请输入验证码" />
		                        <a class="verifyCode fl" href="javascript:imgCode2();"><img class="ui-block"  src="${action_path}validationCode/agentcode.do" id="imgCodeAgent" /></a>请输入验证码。
		                    </label>
		                    <p id="vcode_tip" class="size-tiny long error ui-none">验证通过</p>
		                </div>
		            
	                </#if>
	                <input type="button" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition long" id="caseout_sub" value="确认提款" />
	                <br/>
	                <p class="size-small">为了您的资金安全，请保证您的预留手机【
	                <#if userInfo.phone?length gt 10>
	          			${userInfo.phone?substring(0,3)}****${userInfo.phone?substring(7)}
	          		</#if>
	          		】畅通。</p>
	            <#else>
		                <p class="size-small color-brown">请您先<a href="${action_path}manage/user/userBindBank.do">绑定银行卡</a>后再申请提款，如有疑问请联系在线客服。</p>
	            </#if>
	            <#-- 
                	<p class="size-small">系统提示：您今日剩余提款次数&nbsp;<font class="color-orange size-big">${withdrawalcountLog.withdrawalcount}</font>&nbsp;次，单笔最高提款额度${withdrawalquota}元</p>
                -->
            </#if>
        </div>
        
        <#--微信支付结束-->
        <div class="user-layer ui-mt30 ui-none" id="showMwssage">
            <div class="bankCard">
                <div class="left fl">
                    <img class="ui-block" src="/static/images/user/capital/depositSucc.png" />
                </div>
                <div class="right fl ui-alignCenter">
                    <p class="size-big">您的提款申请已成功提交，汇款成功后我们会以短信方式通知您。</p>
                </div>
                <div class="clearfix"></div>
	                <p class="size-small">同行转帐：完成转帐后请于30分钟内查收您的会员账号余额，如未加上请联系在线客服。</p>
	                <p class="size-small">跨行转帐：银行不承诺跨行汇款到帐时间， 如您的款项在24小时内未加上， 请您联系在线客服为您提供帮助。</p>
	                <a href="/manage/capital/userCaseout.html" style="width: 100px;" class="ui-btn ui-block ui-mt20 size-normal ui-radius color-white bg-orange bg-brownHover ui-transition">返回</a>
	            </div>
	
	        </div>
	    </div>
    </div>

</body>

</html>