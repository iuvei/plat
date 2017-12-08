<!DOCTYPE html>
<html>
<head>
	<#include "${action_path}common/config.ftl">
	${meta}
    <title>${title}--绑定支付宝</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta name="format-detection" content="telephone=no"/>
    <link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/public/base.css"/>
    <script type="text/javascript" src="${zy_path}mstatic/js/public/base.min.js"></script>
    <script  type="text/javascript" src="${zy_path}mstatic/js/public/code.js"></script>
    <script src="${zy_path}mstatic/js/public/base.js"></script>
    <style>
        .form-alipay input{
            padding-left: 2.3rem;
    		padding-right: 1rem;
        }
        .form-alipay .focus-bg{
            left:2.3rem;
        }
        p{
            line-height:0.36rem;
        }
    </style>
    
   <script>
    $(function () {
    	$("#imgCode").click(function(){
    		imgCode();
    	});
    	
    	$("#submit_zfb").click(function(){
			var alipayname = $("#alipayname");
			var alipay = $("#alipay");
			var vAlipyCode =$("#vAlipyCode"); 
			
			if(alipayname.val() ==''){
				$(".error").html('请输入支付宝真实姓名！').addClass("show");
				$("#alipayname").focus();
				return;
			}
			if(base.valid.isNanme(alipayname.val()) == false){
				$(".error").html('支付宝真实姓名输入不正确！').addClass("show");
				alipayname.focus();
				return;
			}
			
			if(alipay.val() ==''){
				$(".error").html('请输入支付宝账号！').addClass("show");
				$("#alipay").focus();
				return;
			}
			
			if(vAlipyCode.val() ==''){
				$(".error").html('请输入验证码！').addClass("show");
				$("#vAlipyCode").focus();
				return;
			}
			
			if($("#submit_zfb").hasClass('_click')){
				return;
			}
			$("#submit_zfb").text('正在提交...');
			$("#submit_zfb").addClass('_click');
			$.ajax({
				type : "POST",
				url : "${action_path}mobile/saveCardPackage.do",
				data : {
					alipayname : encodeURI(alipayname.val()),
					alipay : encodeURI(alipay.val()),
					code : vAlipyCode.val()
				},
				dataType : "JSON",
				async : true,
				success : function(data) {
				 	 data = eval('('+data+')');
					if (data.code == "1") {
						ui.alert({title:'温馨提示',content:'恭喜，您的支付宝绑定成功！',icon:'ok',callback:function(){
				 			window.location.reload();
				 		}});
					} else if (data.code == "0") {
						ui.msg(data.info,3);
						$("#submit_zfb").text('提 交');
						$("#submit_zfb").removeClass('_click');
					}else if (data.code == "9") {
						$("#submit_zfb").text('提 交');
						$("#submit_zfb").removeClass('_click');
						$(".error").html(data.info).addClass("show");
						$("#vAlipyCode").focus();
					} else {
						ui.msg('网络异常，请稍后再试！',3);
						$("#submit_zfb").text('提 交');
						$("#submit_zfb").removeClass('_click');
					}
				}
			})
		});
    });
    </script>
</head>
<body class="bg-main ft-normal">
    <header id="header" class="ui-header bg-dark-black"></header>
    <p class="error _pl30 color-white"></p>
	    <#if myAlipay?? && myAlipay.alipay?if_exists>
	    	<div class="box-wrap-list">
		        <div class="box-wrap-bg-white ft-30">
		            <div class="ui-form-item form-alipay">
		                <label>支付宝真实姓名:</label> <input value="<#if myAlipay.alipayname?if_exists> ${myAlipay.alipayname?substring(0,1)}**</#if>" type="text" readonly/>
		                <span class="focus-bg"></span>
		            </div>
		            <div class="ui-form-item form-alipay">
		                <label>支付宝账号:</label> <input type="text" value="<#if myAlipay.alipay?if_exists>***${myAlipay.alipay?substring(3)}</#if>" readonly/>
		                <span class="focus-bg"></span>
		            </div>
		        </div>
		    </div>
	    <#else>
		  <div class="box-wrap-list">
	        <div class="box-wrap-bg ft-30">
	            <div class="ui-form-item form-alipay">
	                <label>支付宝姓名:</label> 
	                <#if userInfo?? && userInfo.uname?if_exists>
                    	<input name="alipayname" id="alipayname" type="hidden" value="${userInfo.uname!}" readonly />
                    	<input type="text" value="${userInfo.uname?substring(0,1)}**" readonly />
					<#else>
                    	<input name="alipayname" id="alipayname" maxlength="50" type="text" value="" placeholder="请填写支付宝真实姓名" />
                    </#if>
	                <span class="focus-bg"></span>
	            </div>
	            <div class="ui-form-item form-alipay">
	                <label>支付宝账号:</label> <input type="text" id="alipay" maxlength="50" placeholder="请输入支付宝账号"/>
	                <span class="focus-bg"></span>
	            </div>
	            <div class="ui-form-item ui-form-item-r">
	                <input type="text" placeholder="请输入验证码" name="vAlipyCode" maxlength="4" id="vAlipyCode"/>
	                <span class="focus-bg"></span>
	                <div class="vcode"><img id="imgCode" src="${action_path}validationCode/pcrimg.do"></div>
	            </div>
	        </div>
	    </div>
	    <div class="btn-wrap _plr30 _mt90">
	        <a class="btn-lg btn-orange" href="javascript:;" id="submit_zfb">提交</a>
	    </div>
    </#if>
    <p class="_plr30 color-999 ft-26 _mt20 _mb40">温馨提示：请填写正确的支付宝信息，若由于填写错误的信息造成您的资金流失，我公司不负任何责任。</p>
</body>
</html>