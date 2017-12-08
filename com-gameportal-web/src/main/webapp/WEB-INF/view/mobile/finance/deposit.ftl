<!DOCTYPE html>
<html>
<head>
    <#include "/common/config.ftl">
	${meta}
    <title>${title}--我要存款</title>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, maximum-scale=1.0, minimum-scale=1.0" />
    <meta name="format-detection" content="telephone=no" />
   	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
</head>
<body style="background-color: #fff;">
	<!--后台头部-->
	<header class="ht_header">
		<div class="header_left f18">
			<span onclick="history.go(-1);"><img src="${zy_path}mstatic/images/ck_04.png" style="width:.23rem;position: relative;top:.05rem"/> 存款</span>
		</div>
		<div class="header_right f14">
			 ${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
		</div>
	</header>
	<!--后台头部结束-->
	<section class="ck_box">
		<div class="wrap_01" id="zfpt">
			<b class="b1"></b><span class="grey f16">选择支付平台</span><i class="zs"></i>
		</div>
		<input type="hidden" id="ccid" />
	</section>
	<section class="ck_box">
		<div class="wrap_01" id="yh_zx">
			<b class="b2"></b><span class="grey f16">最新优惠</span><i class="zs"></i>
			<input type="hidden" id="benefit" value=""/>
		</div>
	</section>
	<div class="modal" style="top:0;position: relative;z-index: 0;">
		<div class="input_box" style="width:100%" id="alipayName" style="display:none;">
			<input type="text" value="" id="alipayName1" readonly/>
			<b class="icon_user">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款姓名：</b>
			<p class="f12">&nbsp;</p>
		</div>
		<div class="input_box" style="width:100%" id="alipayNumber" style="display:none;">
			<input type="text" value="" id="alipayNumber1" readonly/>
			<b class="icon_money">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收款账号：</b>
			<p class="f12">&nbsp;</p>
		</div>
		<div class="input_box" style="width:100%">
			<input type="text" placeholder="" id="depositFigure"/>
			<b class="icon_money">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;充值金额：</b>
			<p class="f12">&nbsp;</p>
		</div>
		<div class="input_box" style="width:100%">
			<input type="text" placeholder="" id="nickName"/>
			<b class="icon_user">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;支付昵称：</b>
			<p class="f12">&nbsp;</p>
		</div>
		<input id="ordernumber" type="hidden" value="${orderNumber}" readonly/>
		<div class="input_box" style="width:100%;margin:.8rem 0 2rem 0;">
			<input type="button" id="depositATM_sub"  value="确定存款"  style="background-color: #417be7;color:#fff;width:100%;padding-left: 0;font-family: 'microsoft yahei';"/>
		</div>
	</div>
	
	<#include '${action_path}common/mfooter.ftl' />
	
	<!--弹出层  支付方式-->
	<div class="grey_div  undis"></div>
	<div id="zfpt_tan" class="undis modal" style="position: absolute;">
		<h1 class="f18">请选择支付方式</h1>
		<a class="close">&#215;</a>
		<#if wechatChannel?if_exists>
			<#list wechatChannel as weChat>
				<section class="ck_box" style="margin:0 auto .3rem">
					<div class="wrap_01">
						<a class="grey f16 wxchat" href="javascript:void(0)" style="width:100%" id="${weChat.ppid}">${weChat.pname}</a>
					</div>
				</section>
			</#list>
		</#if>
		<#if ailpayChannel?if_exists>
			<#list ailpayChannel as onlineAlipy>
				<section class="ck_box" style="margin:0 auto .3rem">
					<div class="wrap_01">
						<a class="grey f16 onlineAlipay" href="javascript:void(0)" style="width:100%" id="${onlineAlipy.ppid}">${onlineAlipy.pname}</a>
					</div>
				</section>
			</#list>
		</#if>
		<#if onlineQQChannel?if_exists>
			<#list onlineQQChannel as onlineQQ>
				<section class="ck_box" style="margin:0 auto .3rem">
					<div class="wrap_01">
						<a class="grey f16 onlineQQ" href="javascript:void(0)" style="width:100%" id="${onlineQQ.ppid}">QQ钱包扫码</a>
					</div>
				</section>
			</#list>
		</#if>
		<#if apliyAccount?if_exists>
		<section class="ck_box" style="margin:0 auto .3rem">
			<div class="wrap_01">
				<a class="grey f16 alipay" href="javascript:void(0)" style="width:100%" id="${apliyAccount.ccid}">官网支付宝扫码</a>
			</div>
		</section>
		</#if>
		<#if companyWX?if_exists>
		<section class="ck_box" style="margin:0 auto .3rem" id="my_wechat">
			<div class="wrap_01">
				<a class="grey f16 mywechat" href="javascript:void(0)" style="width:100%" id="${companyWX.ccid}">官网微信扫码</a>
			</div>
		</section>
		</#if>
		<#if qqAccount?if_exists>
		<section class="ck_box" style="margin:0 auto .3rem" id="my_qq">
			<div class="wrap_01">
				<a class="grey f16 mywechat" href="javascript:void(0)" style="width:100%" id="${qqAccount.ccid}">官网QQ扫码</a>
			</div>
		</section>
		</#if>
		<#if companyAliapy?if_exists>
		<section class="ck_box" style="margin:0 auto .3rem" id="my_companyAliapy">
			<div class="wrap_01">
				<a class="grey f16 myAlipay" href="javascript:void(0)" style="width:100%" id="${companyAliapy.ccid}" data="${companyAliapy.ccno}" data2="${companyAliapy.ccholder}-${companyAliapy.bankaddr}">在线支付宝转账</a>
			</div>
		</section>
		</#if>
		<div class="input_box" style="width:100%;margin:0 auto 2rem ;text-align: center;">
			<input class="grey_2 quxiao" href="ht_ckc.html" type="button"  value="取 消"  style="width:20%;font-family: 'microsoft yahei';border:1px solid #ccc;padding:.2rem;background-color: transparent;"/>
		</div>
	</div>
	<!--弹出层  选择优惠-->
	<div class="grey_div  undis"></div>
	<div id="yh_tan" class="undis modal" style="position: absolute;top:5%;">
		<h1 class="f18">请您选择您要的优惠</h1>
		<a class="close">&#215;</a>
		<section class="ck_box" style="margin:0 auto .3rem">
			<div class="wrap_01">
				<a class="grey f16"  style="width:100%">不申请优惠</a>
			</div>
		</section>
		<#list activitylist as activity>
		<section class="ck_box" style="margin:0 auto .3rem">
			<div class="wrap_01">
				<a class="grey f16" style="width:100%" id="${activity.aid}">${activity.hdtext}</a>
			</div>
		</section>
		</#list>
		<div class="input_box" style="width:100%;margin:0 auto 2rem ;text-align: center;">
			<input class="grey_2" type="button"  value="取 消" onclick="window.location.href='ht_cka.html'" style="width:20%;font-family: 'microsoft yahei';border:1px solid #ccc;padding:.2rem;background-color: transparent;"/>
		</div>
	</div>
</body>
<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${zy_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
<script type="text/javascript">
	var connection = navigator.connection||navigator.mozConnection||navigator.webkitConnection||{tyep:'unknown'};
	var type_text = ['unknown','ethernet','wifi','2g','3g','4g','none'];
    
	$(function(){
	   //var type=navigator.connection.type
	   /**
	   if(get_status() !='wifi'){
	   		$("#my_wechat").show();
	   }else{
	   		$("#my_wechat").hide();
	   }
	   */
	});
	
	function get_status(){
		if(typeof(connection.type) == "number"){
			connection.type_text = type_text[connection.type];
		}else{
			connection.type_text = connection.type;
		}
		if(typeof(connection.bandwidth) == "number"){
			if(connection.bandwidth > 10){
				return 'wifi';
			}else if(connection.bandwidth > 2){
				return '3g';
			}else if(connection.bandwidth > 0){
				return '2g';
			}else if(connection.bandwidth == 0){
				return 'none';
			}else{
				return 'unknown';
			}
		}
		return 'unknown';
	}
	
	$("#zfpt").click(function(){
		$("#zfpt_tan").show();
		$("#zfpt_tan").prev().show();
	});
	$(".close").click(function(){
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
		$("#yh_tan").hide();
		$("#yh_tan").prev().hide();
	});
	$(".quxiao").click(function(){
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
		$("#yh_tan").hide();
		$("#yh_tan").prev().hide();
	});
	$(".alipay").click(function(){
		var ali = $(this).html();
		
		$("#alipayName").hide();
		$("#alipayNumber").hide();
		
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$(".onlineAlipay").click(function(){
		var ali = $(this).html();
		
		$("#alipayName").hide();
		$("#alipayNumber").hide();
		
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$(".onlineQQ").click(function(){
		var ali = $(this).html();
		
		$("#alipayName").hide();
		$("#alipayNumber").hide();
		
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$(".wxchat").click(function(){
		var ali = $(this).html();
		
		$("#alipayName").hide();
		$("#alipayNumber").hide();
		
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$(".mywechat").click(function(){
		var ali = $(this).html();
		
		$("#alipayName").hide();
		$("#alipayNumber").hide();
		
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$(".myAlipay").click(function(){
		var ali = $(this).html();
		$("#zfpt").find("span").html(ali);
		$("#ccid").val($(this).attr("id"));
		
		$("#alipayName").show();
		$("#alipayNumber").show();
		$("#alipayName1").val($(this).attr('data2'));
		$("#alipayNumber1").val($(this).attr('data'));
		
		$("#zfpt").find(".zs").hide();
		$("#zfpt_tan").hide();
		$("#zfpt_tan").prev().hide();
	});
	
	$("#yh_zx").click(function(){
		$("#yh_tan").show();
		$("#yh_tan").prev().show();
	});
	$("#yh_tan .ck_box a").click(function(){
		var ct = $(this).html();
		$("#yh_zx").find("span").html(ct);
		$("#yh_zx").find(".zs").hide();
		$("#yh_tan").hide();
		$("#benefit").val($(this).attr('id'));
		$("#yh_tan").prev().hide();
	})
	
	$(function(){
		$("#depositATM_sub").click(function(){
	    	var paymethod = $("#ccid").val();
	    	var nickName =$("#nickName");
	    	var depositFigure =$("#depositFigure");
			if(paymethod ==''){
				ui.msg('请选择支付方式!',3);
				return;
			}
			var tbk_company_memo=$("#benefit").val();
			if(depositFigure.val() == ''){
				depositFigure.parent().find(".f12").html("* 请输入您要充值的金额!");
				depositFigure.focus();
				return;
			}else if(!/^[1-9]+[0-9]*]*$/.test(depositFigure.val())){
				depositFigure.parent().find(".f12").html("* 存款金额只允许输入整数!");
				depositFigure.focus();
				return;
			}else if(parseInt(depositFigure.val())<10){
				depositFigure.parent().find(".f12").html("* 公司入款最低金额10元!");
				depositFigure.focus();
				return;
			}else if(tbk_company_memo =='1' && parseInt(depositFigure.val())<100){
				ui.msg('选择首存优惠，需要最低存款100元哦。',3);
				depositFigure.focus();
				return;
			}else if(tbk_company_memo=='2' && parseInt(depositFigure.val())<50){
				ui.msg('选择首存优惠，需要最低存款50元哦。',3);
				depositFigure.focus();
				return;
			}else if((tbk_company_memo =='3' || tbk_company_memo=='4') && parseInt(depositFigure.val())<10){
				ui.msg('选择首存或次存优惠活动，需要最低存款10元哦。',3);
				$("#depositFigure").focus();
				return;
			}else if((tbk_company_memo =='5' || tbk_company_memo=='6') && parseInt(depositFigure.val())<10){
				ui.msg('您选择了参加PT笔笔存优惠活动，需要最低存款10元哦。',3);
				depositFigure.focus();
				return;
			}else if(tbk_company_memo =='8' && parseInt(depositFigure.val())<200){
				ui.msg('您选择了参加的优惠活动，需要最低存款200元哦。',3);
				depositFigure.focus();
				return;
			}else if(tbk_company_memo =='9' && parseInt(depositFigure.val())<500){
				ui.msg('您选择了参加的优惠活动，需要最低存款500元哦。',3);
				depositFigure.focus();
				return;
			}else if((tbk_company_memo =='11' || tbk_company_memo =='12' || tbk_company_memo =='13') && parseInt(depositFigure.val())<100){
				ui.msg('您选择了参加的优惠活动，需要最低存款100元哦。',3);
				depositFigure.focus();
				return;
        	}else if(tbk_company_memo =='15' && parseInt(depositFigure.val()) < 1000){
				ui.msg('您选择了参加的优惠活动，需要存款1000元哦。',3);
				depositFigure.focus();
				return;
        	}else if(tbk_company_memo =='16' && parseInt(depositFigure.val()) < 2000){
				ui.msg('您选择了参加的优惠活动，需要存款2000元哦。',3);
				depositFigure.focus();
				return;
        	}else if(tbk_company_memo =='17' && parseInt(depositFigure.val()) < 50){
				ui.msg('您选择了参加的优惠活动，需要存款50元哦。',3);
				depositFigure.focus();
				return;
        	}else{
				depositFigure.parent().find(".f12").html("&nbsp;");
			}
			
			if(nickName.val() ==''){
				nickName.parent().find(".f12").html("* 请输入您的付款账号昵称!");
				nickName.focus();
				return;
			}else{
				nickName.parent().find(".f12").html("&nbsp;");
			}
			
			if($("#depositalipay_sub").hasClass("disabled")){
				return;
			}
			$("#depositATM_sub").val('正在提交...');
			$("#depositATM_sub").addClass('disabled');
			if(paymethod ==1){
				var submitUrl ="http://app.qianbaobet.com/pay/alipay/1.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host+"&channel=1";
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==24){
				var submitUrl ="http://app.hhh656.pw/pay/zfp/24.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==41){
				var submitUrl ="http://app.qianbaobet.com/pay/rfzfbwap/41.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==45){
				var submitUrl ="http://app.qianbaobet.com/pay/gstpzfb/45.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==236){ //泽圣微信
				var submitUrl ="http://app.qianbaobet.com/pay/zspwx/236.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==234){  //泽圣支付宝
				var submitUrl ="http://app.qianbaobet.com/pay/zspzfb/234.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==21){
				var submitUrl ="http://app.qianbaobet.com/pay/mbp/21.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==21){
				var submitUrl ="http://app.qianbaobet.com/pay/mbp/21.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==43){
				var submitUrl ="http://app.qianbaobet.com/pay/gstpwx/43.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==52){
				var submitUrl ="http://app.qianbaobet.com/pay/xtcpwx/52.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==53){
				var submitUrl ="http://app.qianbaobet.com/pay/xtcpzfb/53.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==56){
				var submitUrl ="http://app.qianbaobet.com/pay/zlpzfb/56.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==58){
				var submitUrl ="http://app.qianbaobet.com/pay/zlpwx/58.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==202){
				var submitUrl ="http://app.qianbaobet.com/pay/lppwx/202.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==232){
				var submitUrl ="http://app.qianbaobet.com/pay/lppqq/232.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==203){
				var submitUrl ="http://app.qianbaobet.com/pay/lppzfb/203.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==206){
				var submitUrl ="http://app.qianbaobet.com/pay/xtcpqq/206.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==209){
				var submitUrl ="http://app.hhh656.pw/pay/zzpwx/209.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==210){
				var submitUrl ="http://app.hhh656.pw/pay/zzpzfb/210.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==212){
				var submitUrl ="http://app.hhh656.pw/pay/yppwx/212.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==222){
				var submitUrl ="http://app.tabaojj.top/pay/shbpwx/222.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==226){
				var submitUrl ="http://app.tabaojj.top/pay/shbpzfb/226.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==232){
				var submitUrl ="http://app.tabaojj.top/pay/shbpqq/232.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==228){
				var submitUrl ="http://app.qianbaobet.com/pay/hebaopzfb/228.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else if(paymethod ==224){
				var submitUrl ="http://app.hhh656.pw/pay/yppzfb/224.do?vuid=${vuid}&totalAmount=" + depositFigure.val()
				+ "&hd=" + tbk_company_memo + "&requestHost=" + window.location.host;
				setTimeout(function () {
					window.location = submitUrl;
				}, 0);
			}else{
				$.ajax({
					type: 'POST',
					url : actionPath+"mobile/finance/savedepositmp.do",
					data: {
						ccid : paymethod,
						depositFigure : depositFigure.val(),
						ordernumber:$("#ordernumber").val(),
						nickNme:nickName.val(),
						hd : tbk_company_memo
					},
					dataType : "JSON",
					async: true,
					success: function(data){
					 	if (data.code == "1") {
					 		if($("#zfpt").find("span").html()=='在线支付宝转账'){
						 		ui.alert({title:'温馨提示',content:data.info,icon:'ok',callback:function(){
						 			window.location.reload();
						 		}});
					 		}else{
						 		ui.alert({title:'温馨提示',content:data.info,icon:'ok',callback:function(){
						 			window.location.href="/mobile/showOrder.do?ccid="+paymethod;
						 		}});
					 		}
						} else if (data.code == "0") {
							$("#depositATM_sub").val('确认存款');
							$("#depositATM_sub").removeClass('disabled');
							ui.msg(data.info,3);
						} else if (data.code == "8" || data.code == "9") {
							$("#depositATM_sub").val('确认存款');
							$("#depositATM_sub").removeClass('disabled');
							ui.msg(data.info,3);
						}else {
							ui.msg('网络异常，请稍后再试！',2);
							$("#depositATM_sub").removeClass('disabled');
						}
					},
					error: function(e){
						$("#depositATM_sub").val('确认存款');
						$("#depositATM_sub").removeClass('disabled');
						ui.msg('网络异常，请稍后再试！',2);
					}	
				});	
			}
				
		});
	})
</script>
</html>