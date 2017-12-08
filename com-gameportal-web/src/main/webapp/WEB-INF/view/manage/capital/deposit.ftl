<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--存款</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css?v=2" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/capital/deposit.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js?v=10"></script>
    <script src="${zy_path}js/zclip.js"></script>
    <script type="text/javascript">
    	var vuid = '${vuid}';
    	function imgCode5(){
			$("#cimgCode_zxzfb").attr("src",actionPath+"validationCode/qrcode.do?r="+(new Date()).getTime());
		}
		function imgCode6(){
			$("#imgCodeqq").attr("src",actionPath+"validationCode/qrcode.do?r="+(new Date()).getTime());
		}
		function imgCode7(){
			$("#imgCodeQQonline").attr("src",actionPath+"validationCode/wxcode.do?r="+(new Date()).getTime());
		}
		function imgCode8(){
			$("#imgCodewxs").attr("src",actionPath+"validationCode/wxcode.do?r="+(new Date()).getTime());
		}
    </script>
    <script src="/js/deposit.js?v=4.3"></script>
</head>

<body>
   <div class="title size-bigger">
        <ul class="ui-alignCenter" id="depositWay">
            <li class="fl active">网银转账</li>
            <#if payPlats?? && payPlats?size gt 0>
            <li class="fl">在线支付</li>
            </#if>
            <#if apliyAccount?exists || hasalipay>
            	<li class="fl">支付宝扫码</li>
            </#if>
            <#if wechatAccount?exists || haswx>
            	<li class="fl">微信扫码</li>
            </#if>
            <#if companyAliapy??>
            <li class="fl">支付宝转账</li>
            </#if>
            <#if qqAccount??>
            <li class="fl">QQ钱包扫码</li>
            </#if>
            <#if qqonlineAlipays??>
            <li class="fl">QQ扫码支付 <img src="/images/hot.gif" style="margin-bottom:25px"/></li>
            </#if>
            <#if dxAlipays??>
            <li class="fl">点卡支付</li>
            </#if>
        </ul>
    </div>
    
    <div class="main">
        <div class="user-layer ui-mt30" id="depositWayLayer">
            <form action="javascript:;">
                <p class="size-small color-brown">网银存款，系统将自动补偿0.5%的转账手续费到您的钱包。</p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl long"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long"  style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="tbk_company_memo">请选择您要的优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">请选择您要的优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}" >${activityl.hdtext}</li>
					              	</#list>
					            </#if>
                            </ul>
                        </div>&emsp;<span class="size-small"></span>
                    </label>
                </div>
                <p class="marginLeftLong size-small">请确认您：
                    <label class="ui-hand">
                        <input type="checkbox" id="company_memocheck" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl long"><font class="color-orange">*</font> 存款银行：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="depositBankname">请选择银行</div>
                            <span class="fl"></span>
                            <ul class="ui-radius" id="banks">
                                <li data="">请选择银行</li>
				            	<#if companyCardMap?exists> 
					                <#list companyCardMap?keys as k>
		                                <li data="${companyCardMap[k].getCcno()}::${companyCardMap[k].getCcholder()}::${companyCardMap[k].getBankaddr()}">${k}</li>
					                </#list>
				                </#if>
                            </ul>
                        </div>&emsp;<span class="size-small">请选择银行</span>
                    </label>
                    <p id="depositBankname_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 银行帐号：</span>
                        <input class="fl size-small" id="cardNum" type="text" value="" placeholder="请输入银行帐号" readonly/>
                        <a class="copy size-small color-white bg-orange bg-brownHover ui-radius ui-transition" id="clipinner1" href="javascript:;">复制银行帐号</a>
                    </label>
                    <p id="cardNum_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 开户人姓名：</span>
                        <input class="fl size-small" id="bankPerson" type="text" value="" placeholder="请输入开户人姓名" readonly/>
                        <a class="copy size-small color-white bg-orange bg-brownHover ui-radius ui-transition" id="clipinner2" href="javascript:;">复制开户姓名</a>
                    </label>
                    <p id="bankPerson_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 开户分行：</span>
                        <input class="fl size-small" id="depositOpenbank" type="text" value="" placeholder="请输入开户分行" readonly/>
                    </label>
                    <p id="depositOpenbank_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
               <div class="ui-inputBox ui-none" id="nickNameDiv">
                    <label><span class="fl long"><font class="color-orange">*</font> 支付宝昵称：</span>
                        <input class="fl size-small" id="nickName" type="text" value="" placeholder="请输入付款支付宝昵称" /><span class="size-small">提高存款到账时间</span>
                    </label>
                    <p id="nickName_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 存款金额：</span>
                        <input class="fl size-small" id="depositFigure" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款<font id="officeMinAmountFont">10</font>元</span>
                    </label>
                    <p id="depositFigure_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 转账附言：</span>
                        <input class="fl size-small" id="ordernumber" type="text" value="${orderNumber}" placeholder="请输入银行转账附言" readonly/>
                        <a class="copy size-small color-white bg-orange bg-brownHover ui-radius ui-transition" id="clipinner3" href="javascript:;">复制附言</a><span class="size-small color-brown">&emsp;请务必将转账附言复制到您银行汇款备注栏</span>
                    </label>
                    <p id="ordernumber_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="vcode" maxlength=4 type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode2();"><img class="ui-block" id="imgCodeAgent" src="${action_path}validationCode/agentcode.do" /></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="vcode_tip" class="size-tiny error long ui-none">验证通过</p>
                </div>
                <input type="hidden" disabled id="showBankCard"/>
                <input type="button" class="submit ui-btn ui-block size-normal ui-mt5 ui-radius color-white bg-orange bg-brownHover ui-transition long" id="depositATM_sub" value="确认存款" />
            </form>
			
			<#--在线支付开始-->
			<#if payPlats?? && payPlats?size gt 0>
            <form class="ui-none" action="javascript:;">
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="tbk_memo">请选择您要的优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">请选择您要的优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
					              	</#list>
					              </#if>
                            </ul>
                        </div>&emsp;<span class="size-small"></span>
                    </label>
                </div>
                <p class="marginLeftShort size-small">请确认您：
                    <label class="ui-hand">
                        <input type="checkbox" id="memocheck" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 银行代码：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="bank_code">请选择银行代码</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">请选择银行代码</li>
            					<li data="ABC">农业银行</li>
            					<li data="BOC">中国银行</li>
            					<li data="CCB">建设银行</li>
            					<li data="BOCOM">交通银行</li>
            					<li data="CMB">招商银行</li>
            					<li data="ICBC">中国工商银行</li>
            					<li data="CMBC">民生银行</li>
            					<li data="HXB">华夏银行</li>
            					<li data="CIB">兴业银行</li>
            					<li data="CEB">光大银行</li>
            					<li data="CNCB">中信银行</li>
            					<li data="PAB">平安银行</li>
            					<li data="SPDB">浦发银行</li>
            					<li data="PSBC">邮政储蓄银行</li>
            					<li data="CGB">广发银行</li>
                            </ul>
                        </div>
                    </label>
                    <p id="bankCode_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 存款金额：</span>
                        <input id="otherFigure" maxlength="50" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
                    </label>
                    <p id="otherFigure_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="otherCode" type="text" maxlength=4 value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode();"><img id="imgCode" class="ui-block" src="${action_path}validationCode/pcrimg.do" /></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="otherCode_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <p class="size-small color-brown ui-mt5">支付如不成功，请尝试其他存款通道。 </p>
                <ul class="payWay clearfix" id="payWay">
                	<#assign inx =0 />
                    <#list payPlats as payPlat>
                		<#assign inx = inx+1 />
	                    <li class="fl border-orange ui-radius ui-hand"><span class="fl ui-radio <#if inx==1 >checked</#if>" data="${payPlat.getPpid()}" name="payType"></span><img class="fl" src="/static/images/user/capital/bank.png">存款通道${inx}</li>
                    </#list>
                </ul>
                <input type="button" id="depositOther_sub" class="submit ui-btn ui-block size-normal ui-mt5 ui-radius color-white bg-orange bg-brownHover ui-transition short" id="submit" value="确认存款" />
                <p class="size-small">在线支付<span class="color-brown">及时到帐</span>。如果您存款中遇到问题请及时联系客服。</p>
            </form>
            </#if>
			<#--在线支付结束-->
			
			<#--支付宝扫码开始 -->
			<#if apliyAccount?exists>
	            <form class="ui-none" action="javascript:;">
	                <div class="QRCode pa"><img class="ui-block border-orange" src="${zy_path}images/${apliyAccount.remarks}" />
	                    <p class="color-brown ui-mt5 ui-alignCenter">支付宝账号：<span>${apliyAccount.ccno}</span>&emsp;用户名：<span>${apliyAccount.ccholder}</span></p>
	                </div>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 最新优惠：</span>
	                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
	                            <div class="input fl ui-hand ui-textOverflow" data="" id="tbk_memoqr">请选择您要的优惠</div>
	                            <span class="fl"></span>
	                            <ul class="ui-radius ul-data">
	                                <li data="">请选择您要的优惠</li>
					              	 <#if activitylist?exists> 
					              	 	<#list activitylist as activityl>
			                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
						              	</#list>
						              </#if>
	                            </ul>
	                        </div>&emsp;<span class="size-small"></span>
	                    </label>
	                </div>
	                <p class="marginLeftShort size-small">请确认您：
	                    <label class="ui-hand">
	                        <input type="checkbox" id="memocheckqr" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
	                </p>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 存款金额：</span>
	                        <input id="otherFigureqr" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
	                    </label>
	                    <p id="otherFigureqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 支付宝昵称：</span>
	                        <input id="nickNameqr" class="fl size-small short" type="text" value="" placeholder="请输入付款支付宝昵称" /><span class="size-small">提高充值到账时间。</span>
	                    </label>
	                    <p id="nickNameqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                        <input id="otherCodeqr" maxlength="4" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入验证码" />
	                        <a class="verifyCode fl" href="javascript:imgCode3();"><img class="ui-block" src="${action_path}validationCode/qrcode.do" id="imgCodeQr" /></a><span class="size-small">请输入验证码。</span>
	                    </label>
	                    <p id="otherCodeqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <input type="hidden" id="ccid" value="${apliyAccount.ccid}"/>
	                <input type="submit" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="depositOther_subqr" value="确认存款" />
	                <p class="size-small">温馨提示：<span class="color-brown">1. 支付宝存款支付成功后请立即联系在线客服进行对账。</span></p>
	            </form>
            </#if>
            <#--支付宝扫码结束-->
            <#--第三方支付宝开始-->
            <#if hasalipay && onlineAlipays??>
            <form class="ui-none" action="javascript:;">
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="onlineAlipay_memo">不申请优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">不申请优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
					              	</#list>
					              </#if>
                            </ul>
                        </div><span class="size-small color-brown">&emsp;有新的优惠活动可以参加</span>
                    </label>
                </div>
                <p class="marginLeftShort size-small">请确认您：
                    <label class="ui-hand">
                        <input type="checkbox" id="onlineAlipaycheckwx" name="memocheck" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 存款金额：</span>
                        <input class="fl size-small short" id="onlineAlipayFigure" maxlength="50" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
                    </label>
                    <p id="onlineAlipayFigure_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="onlineAlipayCode" maxlength="4" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode4();"><img class="ui-block" src="${action_path}validationCode/wxcode.do" id="imgCodewx"/></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="onlineAlipayCode_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <p class="size-small color-brown ui-mt5">支付如不成功，请尝试其他存款通道。 </p>
                <ul class="payWay clearfix" id="alipayWay">
                	<#assign inx =0 />
                    <#list onlineAlipays as payPlat>
                		<#assign inx = inx+1 />
	                    <li class="fl border-orange ui-radius ui-hand"><span class="fl ui-radio <#if inx==1 >checked</#if>" data="${payPlat.getPpid()}" name="payType"></span><img class="fl" src="/static/images/user/capital/bank.png">存款通道${inx}</li>
                    </#list>
                </ul>
                <input type="button" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="depositOther_onlineAlipay" value="确认存款" />
                <p class="size-small">支付宝扫码支付成功后，<span class="color-brown">请勿直接关闭页面，点击页面"跳转商户页面"链接，</span>否则不能及时到账。</p>
                <p><span class="color-red">在线支付宝存款需要承担1%的手续费，收费标准根据第三方通道为准，手续费由第三方收取。</span></p>
                <p class="size-small">如果您存款中遇到问题请及时联系客服。</p>
            </form>
            </#if>
            <#--第三方支付宝结束-->
            
			<#--微信支付开始-->
			<#if wechatAccount?exists>
	            <form class="ui-none" action="javascript:;">
	                <div class="QRCode pa"><img class="ui-block border-orange" src="${zy_path}images/${wechatAccount.remarks}" />
	                    <p class="color-brown ui-mt5 ui-alignCenter">微信账号：<span>${wechatAccount.ccholder}</span></p>
	                </div>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 最新优惠：</span>
	                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
	                            <div class="input fl ui-hand ui-textOverflow" data="" id="ctbk_memoqr">请选择您要的优惠</div>
	                            <span class="fl"></span>
	                            <ul class="ui-radius ul-data">
	                                <li data="">请选择您要的优惠</li>
					              	 <#if activitylist?exists> 
					              	 	<#list activitylist as activityl>
			                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
						              	</#list>
						              </#if>
	                            </ul>
	                        </div>&emsp;<span class="size-small"></span>
	                    </label>
	                </div>
	                <p class="marginLeftShort size-small">请确认您：
	                    <label class="ui-hand">
	                        <input type="checkbox" id="cmemocheckqr" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
	                </p>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 存款金额：</span>
	                        <input id="cotherFigureqr" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
	                    </label>
	                    <p id="cotherFigureqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 微信昵称：</span>
	                        <input id="cnickNameqr" class="fl size-small short" type="text" value="" placeholder="请输入付款微信昵称" /><span class="size-small">提高充值到账时间。</span>
	                    </label>
	                    <p id="cnickNameqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                        <input id="cotherCodeqr" maxlength="4" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入验证码" />
	                        <a class="verifyCode fl" href="javascript:imgCode3();"><img class="ui-block" src="${action_path}validationCode/qrcode.do" id="cimgCodeQr" /></a><span class="size-small">请输入验证码。</span>
	                    </label>
	                    <p id="cotherCodeqr_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <input type="hidden" id="cccid" value="${wechatAccount.ccid}"/>
	                <input type="submit" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="cdepositOther_subqr" value="确认存款" />
	                <p class="size-small">温馨提示：<span class="color-brown">微信存款支付成功后请立即联系在线客服进行对账。</span></p>
	            </form>
            </#if>
			<#if haswx && wxPlats??>
            <form class="ui-none" action="javascript:;">
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="wx_memo">不申请优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">不申请优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
					              	</#list>
					              </#if>
                            </ul>
                        </div><span class="size-small color-brown">&emsp;有新的优惠活动可以参加</span>
                    </label>
                </div>
                <p class="marginLeftShort size-small">请确认您：
                    <label class="ui-hand">
                        <input type="checkbox" id="memocheckwx" name="memocheck" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 存款金额：</span>
                        <input class="fl size-small short" id="wxFigure" maxlength="50" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
                    </label>
                    <p id="wxFigure_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="wxCode" maxlength="4" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode8();"><img class="ui-block" src="${action_path}validationCode/wxcode.do" id="imgCodewxs"/></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="wxCode_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <p class="size-small color-brown ui-mt5">支付如不成功，请尝试其他微信扫码通道。 </p>
                <ul class="payWay clearfix" id="wxpayWay">
                	<#assign inx =0 />
                    <#list wxPlats as payPlat>
                    		<#assign inx = inx+1 />
		                    <li class="fl border-orange ui-radius ui-hand"><span class="fl ui-radio <#if inx==1 >checked</#if>" data="${payPlat.getPpid()}" name="wxpayType"></span><img class="fl" src="/static/images/user/capital/bank.png">扫码通道${inx}</li>
                     </#list>
                </ul>
                
                <input type="button" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="depositOther_wx" value="确认存款" />
                <p class="size-small">微信扫码支付成功后，<span class="color-brown">请勿直接关闭页面，点击页面"跳转商户页面"链接，</span>否则不能及时到账。</p>
                <p><span class="color-red">在线微信存款需要承担1%的手续费，收费标准根据第三方通道为准，手续费由第三方收取。</span></p>
                <p class="size-small">如果您存款中遇到问题请及时联系客服。</p>
            </form>
            </#if>
            <#--微信支付结束-->
            <#--在线支付宝转账开始-->
            <#if companyAliapy??>
            <form class="ui-none" action="javascript:;">
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 最新优惠：</span>
	                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
	                            <div class="input fl ui-hand ui-textOverflow" data="" id="ctbk_memo_zxzfb">请选择您要的优惠</div>
	                            <span class="fl"></span>
	                            <ul class="ui-radius ul-data">
	                                <li data="">请选择您要的优惠</li>
					              	 <#if activitylist?exists> 
					              	 	<#list activitylist as activityl>
			                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
						              	</#list>
						              </#if>
	                            </ul>
	                        </div>&emsp;<span class="size-small"></span>
	                    </label>
	                </div>
	                <p class="marginLeftShort size-small">请确认您：
	                    <label class="ui-hand">
	                        <input type="checkbox" id="cmemocheck_zxzfb" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
	                </p>
	                <br/>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 收款姓名：</span><!---TRIEUTHIXUYEN-->
	                    	<span style="font-weight: bold;">${companyAliapy.ccholder}</span>&nbsp;&nbsp;<font color="red" style="font-weight: bold;">由于到账时间教慢，请截图给客服立刻上分!</font>
	                    </label>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 收款账号：</span><!--农业银行：6228 4850 1880 2931 177-->
	                     	<input class="fl size-small" type="text" value="${companyAliapy.bankaddr}：${companyAliapy.ccno}" readonly style="color:red;"/>
	                    </label>
	                </div>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 付款支付宝姓名：</span>
	                        <input id="payName_zxzfb" class="fl size-small short" type="text" value="" placeholder="请输入付款支付宝姓名" /><span class="size-small">请填写正确的信息，便于快速充值到帐。</span>
	                    </label>
	                    <p id="payName_zxzfb_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 存款金额：</span>
	                        <input id="cotherFigure_zxzfb" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
	                    </label>
	                    <p id="cotherFigure_zxzfb_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                        <input id="cotherCode_zxzfb" maxlength="4" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入验证码" />
	                        <a class="verifyCode fl" href="javascript:imgCode5();"><img class="ui-block" src="${action_path}validationCode/qrcode.do" id="cimgCode_zxzfb" /></a><span class="size-small">请输入验证码。</span>
	                    </label>
	                    <p id="cotherCode_zxzfb_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <input type="hidden" id="cccid_zxzfb" value="${companyAliapy.ccid}"/><!--62-->
	                <input type="submit" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="cdepositOther_zxzfb" value="确认存款" />
	            </form>
	         </#if>
            <#--在线支付宝转账结束-->
            
            <#--QQ扫码开始 -->
			<#if qqAccount?exists>
	            <form class="ui-none" action="javascript:;">
	                <div class="QRCode pa"><img class="ui-block border-orange" src="${zy_path}images/${qqAccount.remarks}" />
	                    <p class="color-brown ui-mt5 ui-alignCenter">支付宝账号：<span>${qqAccount.ccno}</span>&emsp;用户名：<span>${qqAccount.ccholder}</span></p>
	                </div>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 最新优惠：</span>
	                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
	                            <div class="input fl ui-hand ui-textOverflow" data="" id="tbk_memoqq">请选择您要的优惠</div>
	                            <span class="fl"></span>
	                            <ul class="ui-radius ul-data">
	                                <li data="">请选择您要的优惠</li>
					              	 <#if activitylist?exists> 
					              	 	<#list activitylist as activityl>
			                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
						              	</#list>
						              </#if>
	                            </ul>
	                        </div>&emsp;<span class="size-small"></span>
	                    </label>
	                </div>
	                <p class="marginLeftShort size-small">请确认您：
	                    <label class="ui-hand">
	                        <input type="checkbox" id="memocheckqq" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
	                </p>
	                <div class="ui-inputBox ui-mt20">
	                    <label><span class="fl long"><font class="color-orange">*</font> 存款金额：</span>
	                        <input id="otherFigureqq" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
	                    </label>
	                    <p id="otherFigureqq_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> QQ钱包昵称：</span>
	                        <input id="nickNameqq" class="fl size-small short" type="text" value="" placeholder="请输入付款QQ钱包昵称" /><span class="size-small">提高充值到账时间。</span>
	                    </label>
	                    <p id="nickNameqq_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <div class="ui-inputBox">
	                    <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                        <input id="otherCodeqq" maxlength="4" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入验证码" />
	                        <a class="verifyCode fl" href="javascript:imgCode6();"><img class="ui-block" src="${action_path}validationCode/qrcode.do" id="imgCodeqq" /></a><span class="size-small">请输入验证码。</span>
	                    </label>
	                    <p id="otherCodeqq_tip" class="size-tiny error ui-none long">验证通过</p>
	                </div>
	                <input type="hidden" id="ccidqq" value="${qqAccount.ccid}"/>
	                <input type="submit" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="depositOther_subqq" value="确认存款" />
	                <p class="size-small">温馨提示：<span class="color-brown">支付宝存款支付成功后请立即联系在线客服进行对账。</span></p>
	            </form>
            </#if>
            <#--QQ扫码结束-->
            
             <#--第三方QQ扫码开始-->
            <#if qqonlineAlipays??>
            <form class="ui-none" action="javascript:;">
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="onlineQQ_memo">不申请优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">不申请优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
					              	</#list>
					              </#if>
                            </ul>
                        </div><span class="size-small color-brown">&emsp;有新的优惠活动可以参加</span>
                    </label>
                </div>
                <p class="marginLeftShort size-small">请确认您：
                    <label class="ui-hand">
                        <input type="checkbox" id="onlineQQcheckwx" name="memocheck" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 存款金额：</span>
                        <input class="fl size-small short" id="onlineQQFigure" maxlength="50" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款10元。</span>
                    </label>
                    <p id="onlineQQFigure_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="onlineQQCode" maxlength="4" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode7();"><img class="ui-block" src="${action_path}validationCode/wxcode.do" id="imgCodeQQonline"/></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="onlineQQCode_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <p class="size-small color-brown ui-mt5">支付如不成功，请尝试其他存款通道。 </p>
                <ul class="payWay clearfix" id="qqWay">
                	<#assign inx =0 />
                    <#list qqonlineAlipays as payPlat>
                		<#assign inx = inx+1 />
	                    <li class="fl border-orange ui-radius ui-hand"><span class="fl ui-radio <#if inx==1 >checked</#if>" data="${payPlat.getPpid()}" name="payType"></span><img class="fl" src="/static/images/user/capital/bank.png">存款通道${inx}</li>
                    </#list>
                </ul>
                <input type="button" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="depositOther_onlineQQ" value="确认存款" />
                <p class="size-small">QQ扫码支付成功后，<span class="color-brown">请勿直接关闭页面，点击页面"跳转商户页面"链接，</span>否则不能及时到账。</p>
                <p class="size-small">如果您存款中遇到问题请及时联系客服。</p>
            </form>
            </#if>
            <#--第三方QQ扫码结束-->
            
            
           	<#--点卡支付开始-->
           	<#if dxAlipays??>
            <form class="ui-none" action="javascript:;">
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 最新优惠：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius long" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="tbk_memodk">请选择您要的优惠</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">请选择您要的优惠</li>
                                <#if activitylist?exists> 
				              	 	<#list activitylist as activityl>
		                                <li data="${activityl.hdnumber}">${activityl.hdtext}</li>
					              	</#list>
					              </#if>
                            </ul>
                        </div>&emsp;<span class="size-small"></span>
                    </label>
                </div>
                <p class="marginLeftShort size-small">请确认您：
                    <label class="ui-hand">
                    <input type="checkbox" id="memocheckdk" /> 同意</label><a class="color-orange color-brownHover " href="/favo.html" target="_blank">《优惠协议》</a>
                </p>
                <div class="ui-inputBox ui-mt20">
                    <label><span class="fl short"><font class="color-orange">*</font> 支付点卡：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius" style="width:237px;">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="dk_type">请选择支付点卡</div>
                            <span class="fl"></span>
                            <ul class="ui-radius ul-data">
                            	<li data="">请选择支付点卡</li>
            					<li data="SZX">神州行充值卡</li>
            					<li data="UNICOM">联通充值卡</li>
            					<li data="TELECOM">电信充值卡</li>
            					<li data="SFTCARD">盛付通卡</li>
            					<li data="JUNNET">骏网一卡通</li>
            					<li data="WANMEI">完美一卡通</li>
            					<li data="NETEASE">网易一卡通</li>
            					<li data="ZHENGTU">征途一卡通</li>
            					<li data="JIUYOU">久游一卡通</li>
            					<li data="QQCARD">QQ币卡</li>
            					<li data="ZONGYOU">纵游一卡通</li>
            					<li data="TIANHONG">天宏一卡通</li>
            					<li data="SOHU">搜狐一卡通</li>
                            </ul>
                        </div>
                    </label>
                    <p id="dk_type_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 存款金额：</span>
                        <input id="otherFiguredk" maxlength="50" class="fl size-small short" type="text" value="" placeholder="请输入存款金额" /><span class="size-small">最低存款20元。</span>
                    </label>
                    <p id="otherFiguredk_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 点卡卡号：</span>
                     <input id="card_no" maxlength="50" class="fl size-small short" type="text" value="" placeholder="请输入点卡卡号" />
                    <p id="card_no_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                 <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 点卡密码：</span>
                        <input id="card_password" maxlength="50" class="fl size-small short" type="password" value="" placeholder="请输入点卡密码" />
                    <p id="card_password_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl size-small short" id="otherCodedk" type="text" maxlength=4 value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode5();"><img id="imgdk" class="ui-block" src="${action_path}validationCode/wxcode.do" /></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="otherCodedk_tip" class="size-tiny error short ui-none">验证通过</p>
                </div>
                <p class="size-small color-brown ui-mt5">支付如不成功，请尝试其他存款通道。 </p>
                <input type="button" id="depositOther_dk" class="submit ui-btn ui-block size-normal ui-mt5 ui-radius color-white bg-orange bg-brownHover ui-transition short" value="确认存款" />
                <p class="size-small">建议您使用网银转账，因为使用一卡通，充值卡，游戏点卡充值到账的金额与您实际存款的金额是有一个额度差的。<br/>到账金额比实际存款金额少。到账比例 请查看费率表。</p>
                <table class="table2" border="1" cellpadding="5" cellspacing="0" style="margin:20px;width:90%">
                    <tbody>
                    <tr>    
						 <th class="td20" colspan="6">点卡支付业务费率</th>												      								                                 
                    </tr>
                   <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">移动神州行</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.05</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">联通充值卡</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.05</td>  
                   </tr>
                   <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">电信充值卡</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.05</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">盛付通卡</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.14</td>  
                   </tr> 
                    <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">骏网一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.16</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">完美一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.14</td>  
                   </tr> 
                   <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">网易一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.14</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">征途一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.15</td>  
                   </tr>
                    <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">久游一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.18</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">QQ币卡</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.14</td>  
                   </tr>
                   <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">纵游一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.16</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">天宏一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.16</td>  
                   </tr>
                    <tr>
                     <td class="td20 fb" style="width:10%;font-weight:700;">卡类型：</td>
                     <td class="td20" style="width:15%;">搜狐一卡通</td>
					 <td class="td20 fb" style="width:10%;color:red;">费率：0.14</td>
					 <td class="td20 fb" style="width:10%;font-weight:700;"></td>
                     <td class="td20" style="width:15%;"></td>
					 <td class="td20 fb" style="width:10%;color:red;"></td>  
                   </tr>                             
				   </tbody>
			</table>
            </form>
            </#if>
			<#--点卡支付结束-->
	        <form>
		        <div class="user-layer ui-mt30 ui-none" id="showMwssage">
		            <div class="bankCard ui-none">
		                <div class="left fl">
		                    <img class="ui-block" src="/static/images/user/capital/depositSucc.png" />
		                    <p class="color-green size-small ui-alignCenter">你的存款申请已成功提交。</p>
		                </div>
		                <div class="right fl ui-alignCenter">
		                    <p class="size-big">如果您使用银行卡付款，请点击链接进入银行官网</p>
		                    <#if companyCardMap?exists> 
				                <#list companyCardMap?keys as k>
				                	<#if companyCardMap[k].getBankurl()?? && !companyCardMap[k].getBankurl()?contains("www.alipay.com")>
					                    <a class="color-orange color-brownHover" href="${companyCardMap[k].getBankurl()}">${k}</a>
				                	</#if>
				                </#list>
			                </#if>
		                </div>
		                <div class="clearfix"></div>
		                <p class="size-small">同行转帐：完成转帐后请于30分钟内查收您的会员账号余额，如未加上请联系在线客服。</p>
		                <p class="size-small">跨行转帐：银行不承诺跨行汇款到帐时间， 如您的款项在24小时内未加上， 请您联系在线客服为您提供帮助。</p>
		                <a href="/manage/capital/userDeposit.html" style="width: 100px;" class="ui-btn ui-block ui-mt20 size-normal ui-radius color-white bg-orange bg-brownHover ui-transition">返回</a>
		            </div>
		
		            <div class="alipay">
		                <p class="color-green">您的存款申请已提交成功，请扫码二维码进行付款!</p>
		                <#if apliyAccount??>
		                <img class="ui-block ui-mt5" src="/images/${apliyAccount.remarks}" style="width: 200px;"/>
		                <p >支付宝账号：<span>${apliyAccount.ccno}</span>&emsp;用户名：<span>${apliyAccount.ccholder}</span></p>
		                <a href="/manage/capital/userDeposit.html" class="ui-btn ui-block ui-mt20 size-normal ui-radius color-white bg-orange bg-brownHover ui-transition">返回</a>
		                </#if>
		            </div>
		            
		            <div class="wechat">
		                <p class="color-green">您的存款申请已提交成功，请扫码二维码进行付款!</p>
		                <img class="ui-block ui-mt5" src="/static/images/user/capital/qbwx.jpg" style="width: 200px;"/>
		                <p>微信账号：<span>钱宝在线</span></p>
		               	<a href="/manage/capital/userDeposit.html" class="ui-btn ui-block ui-mt20 size-normal ui-radius color-white bg-orange bg-brownHover ui-transition" style="width:195px;">返回</a>
		            </div>
		        </div>
		    </div>
	    </form>
        </div>
        
</body>

</html>