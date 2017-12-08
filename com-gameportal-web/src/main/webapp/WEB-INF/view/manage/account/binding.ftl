<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--绑定银行卡</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css?v=2" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script src="${zy_path}js/jsAddress.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#bindingType").find("li").click(function () {
                if ($(this).hasClass("active")) return false;

                $(this).addClass("active").siblings("li").removeClass("active");
                var _index = $(this).index();
                $("#bindingTypeLayer").children().eq(_index).removeClass("ui-none").siblings().addClass("ui-none");
            });
            
            <#if cardPackage??>
				<#if cardPackage.cardnumber??>
				<#else>
					//省市联动
					addressInit('province', 'city');
				</#if>
	        <#else>
	        	//省市联动
				addressInit('province', 'city');
	        </#if>
	        
	        $("#submitBtn").click(function(){
				var bank = $("#bank_show").text();
				var province = $("#provinceName").text();
				var city = $("#cityName").text();
				var bankDeposit = $("#bankDeposit");
				var bankPerson = $("#bankPerson");
				var cardNum = $("#cardNum");
				var cardNumOK = $("#cardNumOK");
				var vcode = $("#vcode");
				
				if(bankDeposit.val() == '' || bankDeposit.val().length == 0){
					$("#bankDeposit_tip").html('请输入开户支行。');
					$("#bankDeposit_tip").removeClass("ui-none");
					$("#bankDeposit").focus();
					return;
				}else{
					$("#bankDeposit_tip").addClass("ui-none");
				}
				
				if(bankPerson.val() == '' || bankPerson.val().length == 0){
					$("#bankPerson_tip").html('请输入开户姓名。');
					$("#bankPerson_tip").removeClass("ui-none");
					$("#bankPerson").focus();
					return;
				}else{
					$("#bankPerson_tip").addClass("ui-none");
				}
				
				if(cardNum.val() == '' || cardNum.val().length == 0){
					$("#cardNum_tip").html('请输入银行卡号。');
					$("#cardNum_tip").removeClass("ui-none");
					$("#cardNum").focus();
					return;
				}else{
					$("#cardNum_tip").addClass("ui-none");
				}
				
				if(cardNumOK.val() == '' || cardNumOK.val().length == 0){
					$("#cardNumOK_tip").html('请确认银行卡号。');
					$("#cardNumOK_tip").removeClass("ui-none");
					$("#cardNumOK").focus();
					return;
				}else{
					$("#cardNumOK_tip").addClass("ui-none");
				}
				
				if(cardNumOK.val() != cardNum.val()){
					$("#cardNumOK_tip").html('两次卡号不一致，请检查后重新输入。');
					$("#cardNumOK_tip").removeClass("ui-none");
					$("#cardNumOK").val("");
					$("#cardNumOK").focus();
					return;
				}else{
					$("#cardNumOK_tip").addClass("ui-none");
				}
				
				if(vcode.val() == '' || vcode.val().length == 0){
					$("#vcode_tip").html('请输入验证码。');
					$("#vcode_tip").removeClass("ui-none");
					$("#vcode").focus();
					return;
				}else{
					$("#vcode_tip").addClass("ui-none");
				}
				
				$("#submitBtn").val('正在提交...');
				$("#submitBtn").attr('disabled',true);
			
				$.ajax({
					type : "POST",
					url : "${action_path}manage/user/saveCardPackage.do",
					data : {
						bank : bank,
						province : province,
						city : city,
						bankDeposit : bankDeposit.val(),
						bankPerson : bankPerson.val(),
						cardNum : cardNum.val(),
						code : vcode.val()
					},
					dataType : "json",
					async : true,
					success : function(data) {
						if (data.code == "1") {
							$.remind('恭喜，您的银行卡绑定成功！',success_callback);
						} else if (data.code == "0") {
							$.msg(data.info,3);
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
						}else if (data.code == "9") {
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
							$("#vcode_tip").html(data.info);
							$("#vcode_tip").removeClass("ui-none");
						} else {
							$.msg('网络异常，请稍后再试！',3);
							$("#submitBtn").val('提 交');
							$("#submitBtn").attr('disabled',false);
						}
					}
				})
			});
			
			function success_callback(){
				window.location.reload();
			}
	        
	        
	        $("#submit_zfb").click(function(){
				var alipayname = $("#alipayname");
				var alipay =$("#alipay");
				var vAlipyCode =$("#vAlipyCode"); 
				
				if(alipayname.val() ==''){
					$("#alipayname_tip").html('请输入支付宝真实姓名。');
					$("#alipayname_tip").removeClass("ui-none");
					$("#alipayname").focus();
					return;
				}else{
					$("#alipayname_tip").addClass("ui-none");
				}
				
				if(alipay.val() ==''){
					$("#alipay_tip").html('请输入支付宝账号。');
					$("#alipay_tip").removeClass("ui-none");
					$("#alipay").focus();
					return;
				}else{
					$("#alipay_tip").addClass("ui-none");
				}
				
				if(vAlipyCode.val() ==''){
					$("#vAlipyCode_tip").html('请输入验证码。');
					$("#vAlipyCode_tip").removeClass("ui-none");
					$("#vAlipyCode").focus();
					return;
				}else{
					$("#vAlipyCode_tip").addClass("ui-none");
				}
				
				$("#submit_zfb").val('正在提交...');
				$("#submit_zfb").attr('disabled',true);
			
				$.ajax({
					type : "POST",
					url : "${action_path}manage/user/saveCardPackage.do",
					data : {
						alipayname : alipayname.val(),
						alipay : alipay.val(),
						code : vAlipyCode.val()
					},
					dataType : "json",
					async : true,
					success : function(data) {
						if (data.code == "1") {
							$.remind('恭喜，您的支付宝绑定成功！',success_callback);
						} else if (data.code == "0") {
							$.msg(data.info,3);
							$("#submit_zfb").val('提 交');
							$("#submit_zfb").attr('disabled',false);
						}else if (data.code == "9") {
							$("#submit_zfb").val('提 交');
							$("#submit_zfb").attr('disabled',false);
							$("#vAlipyCode_tip").html(data.info);
							$("#vAlipyCode_tip").removeClass("ui-none");
						} else {
							$.msg('网络异常，请稍后再试！',3);
							$("#submit_zfb").val('提 交');
							$("#submit_zfb").attr('disabled',false);
						}
					}
				})
			});
			
        });
    </script>
</head>

<body>
    <div class="title size-bigger">
        <ul class="ui-alignCenter" id="bindingType">
            <li class="fl active">绑定银行卡</li>
            <!--
            <li class="fl">绑定支付宝</li>
            -->
        </ul>
    </div>

    <div class="main">
        <div class="user-layer ui-mt30" id="bindingTypeLayer">
            <div>
            	<#if cardPackage?? && cardPackage.cardnumber??>
            		<ul class="user-list">
	                    <li>
	                        <label class="short">存款银行：</label><span>${cardPackage.bankname}</span></li>
	                    <li>
	                        <label class="short">开户城市：</label><span>${cardPackage.province!}${cardPackage.city!}</span></li>
	                    <li>
	                        <label class="short">开户支行：</label><span>${cardPackage.openingbank!}</span></li>
	                    <li>
	                        <label class="short">开户姓名：</label><span>${cardPackage.accountname!}</span></li>
	                    <li>
	                        <label class="short">银行账号：</label><span>${cardPackage.cardnumber?substring(0,2)}****${cardPackage.cardnumber?substring(6)}</span></li>
	                    <li>
	                        <span class="color-brown">您已经绑定了银行卡信息，如果需要更换请联系客服。</span></li>
	                </ul>
                <#else>
	                <form action="javascript:;">
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 选择银行：</span>
                            	<div class="ui-ddl fl color-gray ui-radius long">
	                                <div class="input fl ui-hand ui-textOverflow" id="bank_show">中国工商银行</div>
	                                <span class="fl"></span>
	                                <ul class="ui-radius" id="card_bank">
	                                    <li>中国工商银行</li>
					                    <li>中国建设银行</li>
					                    <li>中国银行</li>
					                    <li>中国农业银行</li>
					                    <li>交通银行</li>
					                    <li>招商银行</li>
					                    <li>中国邮政储蓄银行</li>
					                    <li>中信银行</li>
					                    <li>光大银行</li>
					                    <li>民生银行</li>
					                    <li>兴业银行</li>
					                    <li>华夏银行</li>
					                    <li>上海浦东发展银行</li>
					                    <!--<li value="深圳发展银行">深圳发展银行</li>-->
					                    <li>广东发展银行</li>
					                    <!--<li value="上海银行">上海银行</li>-->
					                    <li>平安银行</li>
					                    <!--<li>北京银行</li>
					                	<li>南京银行</li>
					                    <li>宁波银行</li>
					                    <li>江苏银行</li>
					                    <li>浙商银行</li>
					                    <li>渤海银行</li>
					                    <li>恒丰银行</li>
					                    <li>昆仑银行</li>
					                    <li>大连银行</li>-->
					                    <li>农村信用社</li>
					                	<li>国家开发银行</li>
					                    <li>中国进出口银行</li>
					                    <li>中国农业发展银行</li>
	                                </ul>
                            	</div>
	                        </label>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 选择城市：</span>
	                            <div class="ui-ddl fl color-gray ui-radius short">
	                                <div class="input fl ui-hand ui-textOverflow" id="provinceName">北京</div>
	                                <span class="fl"></span>
	                                <ul class="ui-radius" id="province">
	                                </ul>
	                            </div>
	                            <div class="ui-ddl fl color-gray ui-radius short">
	                                <div class="input fl ui-hand ui-textOverflow" id="cityName">市辖区</div>
	                                <span class="fl"></span>
	                                <ul class="ui-radius" id="city">
	                                </ul>
	                            </div>
	                        </label>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 开户支行：</span>
                            	<input class="fl" name="bankDeposit" id="bankDeposit" maxlength="50" type="text" value="" placeholder="请输入开户行" />
	                        </label>
	                        <p id="bankDeposit_tip" class="size-tiny short error ui-none">验证通过</p>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 开户姓名：</span>
								<#if userInfo ? exists && userInfo.uname??>
		                            <input class="fl" name="bankPerson" id="bankPerson" maxlength="50" type="text" value="${userInfo.uname!}" readonly placeholder="请输入开户姓名" />
								<#else>
		                            <input class="fl" name="bankPerson" id="bankPerson" maxlength="50" type="text" value="" placeholder="请输入开户姓名" />
								</#if>
	                        </label>
	                        <p id="bankPerson_tip" class="size-tiny short error ui-none">验证通过</p>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 银行账号：</span>
	                            <input class="fl" name="cardNum" id="cardNum" maxlength="50" type="text" value="" placeholder="请输入银行帐号" />
	                        </label>
	                        <p id="cardNum_tip" class="size-tiny short error ui-none">验证通过</p>
	                    </div>
	                    
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 确认账号：</span>
	                            <input class="fl" name="cardNumOK" id="cardNumOK" maxlength="50" type="text" value="" placeholder="请确认银行账号" />请确认银行账号
	                        </label>
	                        <p id="cardNumOK_tip" class="size-tiny short error ui-none">验证通过</p>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
	                            <input class="verifyCode fl short" name="vcode" id="vcode" maxlength="4" type="text" value="" placeholder="请输入验证码" />
	                            <a class="verifyCode fl" href="javascript:imgCode2();"><img class="ui-block" id="imgCodeAgent" src="${action_path}validationCode/agentcode.do" /></a>请输入验证码。
	                        </label>
	                        <p id="vcode_tip" class="size-tiny short error ui-none">验证通过</p>
	                    </div>
	                    <input type="button" id="submitBtn" class="submit ui-btn ui-block ui-radius color-white bg-orange bg-brownHover ui-transition short" value="提交" />
	                </form>
                </#if>
            </div>

            <div class="ui-none">
				<#if cardPackage?? && cardPackage.alipay??>
	                <ul class="user-list ">
	                    <li>
	                        <label class="long">支付宝真实姓名：</label><span>${cardPackage.alipayname!}</span></li>
	                    <li>
	                        <label class="long">支付宝账号：</label><span>${cardPackage.alipay!}</span></li>
	
	                    <li>
	                        <span class="color-brown">支付宝绑定成功，如果需要更换请联系在线客服。</span></li>
	                </ul>
				<#else>
					<form action="javascript:;">
	                    <div class="ui-inputBox">
	                        <label><span class="fl long"><font class="color-orange">*</font> 支付宝真实姓名：</span>
	                        	<#if userInfo ? exists && userInfo.uname??>
	                            	<input class="fl" name="alipayname" id="alipayname" type="text" value="${userInfo.uname!}" readonly />请填写支付宝真实姓名。
								<#else>
	                            	<input class="fl" name="alipayname" id="alipayname" maxlength="50" type="text" value="" placeholder="请填写支付宝真实姓名" />请填写支付宝真实姓名。
	                            </#if>
	                        </label>
	                        <p id="alipayname_tip" class="size-tiny long error ui-none">验证通过</p>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl long"><font class="color-orange">*</font> * 支付宝账号：</span>
	                            <input class="fl" name="alipay" id="alipay" maxlength="50" type="text" value="" placeholder="请填写支付宝账号" />请填写支付宝账号。
	                        </label>
	                        <p id="alipay_tip" class="size-tiny long error ui-none">验证通过</p>
	                    </div>
	                    <div class="ui-inputBox">
	                        <label><span class="fl long"><font class="color-orange">*</font> 验证码：</span>
	                            <input class="vAlipyCode fl" name="vAlipyCode" maxlength="4" id="vAlipyCode" type="text" value="" placeholder="请输入验证码" />
	                            <a class="verifyCode fl" href="javascript:imgCode();"><img class="ui-block" id="imgCode" src="${action_path}validationCode/pcrimg.do" /></a>请输入验证码。
	                        </label>
	                        <p id="vAlipyCode_tip" class="size-tiny long error ui-none">验证通过</p>
	                    </div>
	                    <input type="button" class="submit ui-btn ui-block ui-radius color-white bg-orange bg-brownHover ui-transition long" id="submit_zfb" value="提交" />
	                    <p class="color-brown">温馨提示：请填写正确的支付宝信息，若由于填写错误的信息造成您的资金流失，我公司不负任何责任。</p>
	                </form>
				</#if>
            </div>
        </div>
    </div>

</body>

</html>