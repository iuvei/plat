<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>${title}--个人中心</title>
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/user.css?v=2" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/account/index.css" />
    <script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
    <script type="text/javascript" src="${zy_path}static/lib/highcharts.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/jquery.cookie.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}static/js/public/utils.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/userInfo.js"></script>
	<script type="text/javascript" src="${zy_path}static/js/public/common.js"></script>
	<style>
	.mt10 {margin-top: 10px; }
	.user-box{ height:115px;border-radius: 0 0 10px 10px;font-size:16px;color:#e7c16a;}
	.user-box .user-level{width:120px; float:left; margin-left:33px; text-align:center;}
	.user-box .user-level p{ line-height:27px; margin-top:10px;}
	.user-level-info{ width:530px; margin-left:160px; float:left; position:relative;}
	.user-level-info h3{font:400 16px/35px "Microsoft Yahei";}
	.user-level-info h3 span{ color:#f3ee79;}
	.user-level-info .level-name li{ margin-right:38px; float:left; font-size:14px;margin-left：10px;}
	.user-level-info .level-name li a{color: #212223;font-weight:700; }
	.user-level-info .level{ width:452px; height:23px;margin-left:10px; background:url(../images/level.png) no-repeat;animation: loader0 10s linear;-moz-animation: loader0 10s;-webkit-animation: loader0 10s;-o-animation: loader0 10s;}
	.user-level-info .level0{ background-position: 0 0;}
	.user-level-info .level1{ background-position: 0 -23px;}
	.user-level-info .level2{ background-position: 0 -46px;}
	.user-level-info .level3{ background-position: 0 -69px;}
	.user-level-info .level4{ background-position: 0 -92px;}
	.user-level-info .level5{ background-position: 0 -115px;}
	.user-btn{width:120px !important;margin-left:40px !important;}
	</style>
    <script type="text/javascript">
        $(function () {
        	var safe_exponent = ${exponent};
            var settings = {
                a: safe_exponent,
                b: 100 - safe_exponent
            };
            highcharts(settings);

            //饼状图
            function highcharts(settings) {
                var default_settings = {
                    chart: {
                        type: 'pie',
                        backgroundColor: 'rgba(0,0,0,0)'
                    },
                    title: {
                        text: null
                    },
                    tooltip: { //鼠标移动到每个饼图显示的内容
                        enabled: false,
                        formatter: function () {
                            return this.point.name + this.point.y + "%";
                        }
                    },
                    plotOptions: {
                        pie: {
                            cursor: 'pointer',
                            startAngle: -180,
                            endAngle: 180
                        },
                        series: {
                            states: {
                                hover: {
                                    enabled: true,
                                    halo: {
                                        size: 5 //阴影带宽度为5px
                                    }
                                }
                            }
                        }
                    }
                };

                var arr_colors = ['#8cccfd', '#c0e4ff'],
                    _settings = {
                        colors: arr_colors,
                        plotOptions: {
                            pie: {
                                size: 100,
                                innerSize: 90,
                                dataLabels: {
                                    enabled: false
                                }
                            }
                        },
                        series: [{
                            data: [{
                                name: "已完成",
                                y: parseFloat(settings.a)
                                }, {
                                name: "未完成",
                                y: parseFloat(settings.b)
                            }]
                        }]
                    };
                var $target = $('#HC');

                //绘制图形
                $target.highcharts($.extend(true, {}, default_settings, _settings));
            } //highcharts
            
            // 生日礼金
			$("#birthdayMoney").click(function(){
				$(this).val("正在申请...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/getBirthDayMoney.do",
		            success: function (result){ 
		            	$("#birthdayMoney").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.success){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#birthdayMoney").val("生日礼金");
		            	}
		            }  
			   	});
			});
			
			  // 晋级礼金
			$("#upGrade").click(function(){
				$(this).val("正在申请...");
				$(this).addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/upGrade.do",
		            success: function (result){ 
		            	$("#upGrade").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.code =='1'){
		            		$.msg(result.msg,3,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#upGrade").val("我要晋级");
		            	}
		            }  
			   	});
			});
			
        });
    </script>
</head>

<body>
    <div class="title size-bigger">个人中心</div>
    <div class="main">
        <div class="user-info user-layer clearfix">
            <div class="safe-level fl">
                <div class="HC pa" id="HC"></div>
                <div class="note pa ui-alignCenter">
                <span class="size-big ui-block">个人中心</span><span class="size-small ui-block">安全指数</span><span class="size-small color-orange ui-block">${exponent}%</span>
                </div>
            </div>
            <div class="right fr">
                <h1 class="size-big">欢迎您，<span>${userInfo.uname}</span>账户余额：<span>${accountMoneyCount}元</span>&nbsp;总积分：<span>${integral}分</span></h1>
                <ul>
                    <li><span class="icon icon0"></span>真实姓名：<span>${userInfo.uname}</span></li>
                    <li><span class="icon icon1"></span>登陆次数：<span>${userInfo.logincount}次</span></li>
                    <li><span class="icon icon2"></span>会员等级：<span>
						<#if userInfo.grade == 1>
							新会员
						</#if>
						<#if userInfo.grade == 2>
							星级(VIP)
						</#if>
						<#if userInfo.grade == 3>
							银卡(VIP)
						</#if>
						<#if userInfo.grade == 4>
							金卡(VIP)
						</#if>
						<#if userInfo.grade == 5>
							钻石(VIP)
						</#if>
						<#if userInfo.grade == 6>
							黑卡(VIP)
						</#if>
					</span></li>
                    <li style="width: 250px"><span class="icon icon3"></span>上次登录：<span>${userInfo.updateDate?string("yyyy-MM-dd HH:mm:ss")}</span></li>
                </ul>
            </div>
        </div>
        
        <div class="more-info user-layer" style="height:160px;">
              <div class="user-level-info mt10">
              <h3>"钱宝"会员晋级</h3>
                <ol style="overflow: hidden;" class="mt10 level-name">
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 1><font color="red">新会员</font><#else>新会员</#if></a></li>
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 2><font color="red">星级VIP</font><#else>星级VIP</#if></a></li>
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 3><font color="red">银卡VIP</font><#else>银卡VIP</#if></a></li>
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 4><font color="red">金卡VIP</font><#else>金卡VIP</#if></a></li>
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 5><font color="red">钻石VIP</font><#else>钻石VIP</#if></a></li>
                    <li><a href="javascript:void(0);" style="cursor: pointer;" onclick="javascript:;"><#if userInfo.grade == 6><font color="red">黑卡VIP</font><#else>黑卡VIP</#if></a></li>
                </ol>
                <div class="level level${userInfo.grade-1}"></div>
      			<input type="button" class="submit ui-btn size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short user-btn" id="upGrade" value="我要晋级" />
      			<#if userInfo.grade !=1 && birthdayFlag>
      				<input type="button" class="submit ui-btn size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short user-btn" id="birthdayMoney" value="生日礼金" />
      			</#if>
            </div>
        </div>
        
        <div class="account-info user-layer" style="height:150px;">
            <ul class="fr">
                <li><span class="icon icon0"></span>返水比例：
                	<#if userInfo.grade == 1>
						【电子游艺：<span>0.6%</span>&emsp;真人娱乐：<span>0.4%</span>&emsp;体育：<span>0.4%</span>&emsp;彩票：<span>0.4%</span>】
					</#if>
					<#if userInfo.grade == 2>
						【电子游艺：<span>0.8%</span>&emsp;真人娱乐：<span>0.6%</span>&emsp;体育：<span>0.45%</span>&emsp;彩票：<span>0.45%</span>】
					</#if>
					<#if userInfo.grade == 3>
						【电子游艺：<span>0.9%</span>&emsp;真人娱乐：<span>0.8%</span>&emsp;体育：<span>0.5%</span>&emsp;彩票：<span>0.5%</span>】
					</#if>
					<#if userInfo.grade == 4>
						【电子游艺：<span>1.0%</span>&emsp;真人娱乐：<span>0.9%</span>&emsp;体育：<span>0.55%</span>&emsp;彩票：<span>0.55%</span>】
					</#if>
					<#if userInfo.grade == 5>
						【电子游艺：<span>1.1%</span>&emsp;真人娱乐：<span>1.0%</span>&emsp;体育：<span>0.6%</span>&emsp;彩票：<span>0.6%</span>】
					</#if>
                </li>
                <li><span class="icon icon1"></span>手机号码：<span><#if userInfo.phone?if_exists>${userInfo.phone?substring(0,3)}****${userInfo.phone?substring(7)}</#if></span>&emsp;
	                <#if userInfo.phonevalid ==1>
	            		<span class="color-green" href="javascript:;">已认证</span>
		    		<#else>
	               		<span class="color-orange" href="javascript:;">未认证</span>
		    		</#if>
                </li>
                <li><span class="icon icon2"></span>注册邮箱：
                	<span><#if userInfo.email?if_exists>***${userInfo.email?substring(3)}</#if></span>&emsp;
	                <#if userInfo.emailvalid ==1>
	            		<span class="color-green" href="javascript:;">已认证</span>
		    		<#else>
	               		<span class="color-orange" href="javascript:;">未认证</span>
		    		</#if>
                </li>
            </ul>
        </div>

        <div class="more-info user-layer">
            <ul>
                <li>1、绑定银行卡并设置您的取款密码您才能进行提款，并保障您的账户资金安全。&emsp; 
	                <#if cardPackage??>
		    			<span class="color-green">已绑定</span>
		    		<#else>
		    			<span class="color-brown">未绑定</span> &emsp;<a class="color-orange color-brownHover" href="javascript:forwardTo('accBinding')">立即绑定</a>
		    		</#if>
	    		</li>
                <li>2、完善个人资料方便我们即时联系您，如有优惠活动我们会第一时间告知。&emsp;
                	<#if udateil??>
		    			<span class="color-green">已完善</span>
		    		<#else>
		    			<span class="color-brown">未完善</span> &emsp;<a class="color-orange color-brownHover" href="javascript:forwardTo('accPersonal')">立即完善</a>
		    		</#if>
	    		</li>
            </ul>
        </div>

    </div>
	
</body>

</html>