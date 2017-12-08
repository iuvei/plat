<!DOCTYPE html>
<html>
	<head>
		<#include "${action_path}common/config.ftl">
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
		<title>${title}--首页</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
	<script type="text/javascript">
		$(function(){
			var hei=$(window).height();
			$(".dh .swiper-slide").height(hei);
			
			var a =0,b=0;
			setInterval(function(){
				 var text = $("#pt_client");
				 if (!a)
				 {
					text.css("color","red");
					a = 1;
				 }else{
					text.css("color","");
					a = 0;
				 }
			 },500);
			 
		 	 setInterval(function(){
		 		var text = $("#mg_client");
				 if (!b)
				 {
					text.css("color","red");
					b = 1;
				 }else{
					text.css("color","");
					b = 0;
				 }
		 	},500);
		 	
		 	$("#ptgame").attr("href","http://"+window.location.host+"/Client.apk")
		 	$("#pt_client").click(function(){
		 		$("#ptgame").click();
		 	});
		 	
		 	$("#mg_client").click(function(){
		 		$("#mggame").click();
		 	});
		})
		
    	$(function(){
			// 申请存款红包
			$("#act1").click(function(){
				<#if !userInfo??>
			    	window.location.href='${action_path}loginmp.html';
			    </#if>
				$(this).find(".f12").text("正在申请...");
				if($(this).hasClass("ui-disabled")){
				 return;
				}
				$(this).addClass("ui-disabled");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/getRedReward.do",
		            success: function (result){ 
		            	$("#act1").removeClass("ui-disabled");
		            	if(result.code =='1'){
		            		ui.msg(result.msg,3,success_callback);
		            	}else{
		            		ui.msg(result.msg,3);
		            		$("#act1").find(".f12").text("点击领取");
		            	}
		            }  
			   	});
			});
			
			// 申请老虎机救援金
			$("#act2").click(function(){
				<#if !userInfo??>
			    	window.location.href='${action_path}loginmp.html';
			    </#if>
				$(this).find(".f12").text("正在申请...");
				if($(this).hasClass("ui-disabled")){
				 return;
				}
				$(this).addClass("ui-disabled");
				$.ajax({  
			        type: "POST", 
			        dataType: "json",
			        data:{type:2},
			        url: "${action_path}manage/getHelp.do",
			        success: function (result){ 
			        	$("#act2").removeClass("ui-disabled");
		            	if(result.success){
		            		ui.msg(result.msg,1,success_callback);
		            	}else{
		            		ui.msg(result.msg,3);
		            		$("#act2").find(".f12").text("点击领取");
		            	}
		            }  
		   		});
			});
			
			// 申请注册彩金
			$("#act3").click(function(){
			    <#if !userInfo??>
			    	window.location.href='${action_path}loginmp.html';
			    </#if>
				$(this).find(".f12").text("正在申请...");
				if($(this).hasClass("ui-disabled")){
				 return;
				}
				$(this).addClass("ui-disabled");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/activityReward.do",
		            success: function (result){
		           		$("#act3").removeClass("ui-disabled");
		            	if(result.success){
		            		ui.msg(result.msg,3,success_callback);
		            	}else{
		            		ui.msg(result.msg,3);
		            		$("#act3").find(".f12").text("点击领取");
		            	}
		            }  
			   	});
			});
			
			// 申请签到彩金
			$("#act4").click(function(){
			    <#if !userInfo??>
			    	window.location.href='${action_path}loginmp.html';
			    </#if>
				$(this).find(".f12").text("正在申请...");
				if($(this).hasClass("ui-disabled")){
				 return;
				}
				$(this).addClass("ui-disabled");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}getSignReward.do",
		            success: function (result){
		           		$("#act4").removeClass("ui-disabled");
		            	if(result.success){
		            		ui.msg(result.msg,3,success_callback);
		            	}else{
		            		ui.msg(result.msg,3);
		            		$("#act4").find(".f12").text("点击领取");
		            	}
		            }  
			   	});
			});
			
			// 积分兑换
			$("#act5").click(function(){
			    <#if !userInfo??>
			    	window.location.href='${action_path}loginmp.html';
			    </#if>
				$(this).find(".f12").text("正在申请...");
				if($(this).hasClass("ui-disabled")){
				 return;
				}
				$(this).addClass("ui-disabled");
				$.ajax({  
		            type: "POST", 
		            dataType: "json",  
		            url: "${action_path}manage/getSlotsWeekRebate.do",
		            success: function (result){
		           		$("#act5").removeClass("ui-disabled");
		            	if(result.success){
		            		ui.msg(result.msg,3,success_callback);
		            	}else{
		            		ui.msg(result.msg,3);
		            		$("#act5").find(".f12").text("点击领取");
		            	}
		            }  
			   	});
			});
			
			function success_callback(){
				window.location.reload();
			}
			
		});
	</script>
	<body>
		<#include "${action_path}common/mheader.ftl">
		<!--头部结束-->
		<!--banner-->
		<section class="banner">
			 <!-- Swiper -->
		    <div class="swiper-container">
		        <div class="swiper-wrapper">
		            <div class="swiper-slide"><img src="${action_path}mstatic/images/banner/20170406_1.jpg"/></div>
		        	<div class="swiper-slide"><img src="${action_path}mstatic/images/banner/20170608_3.jpg"/></div>
		        	<div class="swiper-slide"><img src="${action_path}mstatic/images/banner/20170610_3.jpg"/></div>
		        	<div class="swiper-slide"><img src="${action_path}mstatic/images/banner/b1701125.jpg"/></div>
		            <div class="swiper-slide"><img src="${action_path}mstatic/images/banner/b1701124.jpg"/></div>
		        </div>
		        <!-- Add Pagination -->
		        <div class="swiper-pagination"></div>
		    </div>
		</section>
		<!--banner结束-->
		<!--重要通知-->
		<section class="tz">
			<div class="wrap_02 overflow">
				<img src="${action_path}mstatic/images/laba.png" class="laba fl"/>
					<marquee behavior='scroll'  direction='up' scrollamount='1' align='top' width='88%' scrolldelay=-1 vspace='6'  class='f14 grey_3 fr' style='height:.4rem;'>
					<#list bulletionList as adv>
						${adv.content}</br>
					</#list>
					</marquee>
				</script>
			</div>
		</section>
		<!--重要通知结束-->
		<!--游戏平台-->
		<section class="yx_pt">
			<div class="wrap_02">
				<h3 class="f16 grey_4">游戏平台&nbsp;&nbsp;&nbsp;&nbsp;
					<a id="pt_client" style="color:#23314c;cursor: pointer;" href="${action_path}Client.apk">PT客户端下载</a>&nbsp;&nbsp;
        			<a id="mg_client" style="color:#23314c;cursor: pointer;" href="http://resigner22.qfcontent.com/mobilecasino1/APK?btag2=23262" id="mggame">MG客户端下载</a>
				</h3> 
				
				<ul class="ul_yxpt">
					<li><a href="javascript:window.location.href='/game/samp.do'"><b class="icon_sa"></b><p class="f12">SA游戏厅</p></a></li>
					<li><a href="javascript:window.location.href='/game/ptmp.do'"><b class="icon_pt"></b><p class="f12">PT游戏厅</p></a></li>
					<li><a href="javascript:<#if userInfo??>window.location.href='/game/mgmp.do';<#else>window.location.href='/loginmp.html';</#if>"><b class="icon_mg"></b><p class="f12">MG游戏厅</p></a></li>
					<li><a href="javascript:<#if userInfo??>window.location.href='/game/loginGameAG/AGIN.do';<#else>window.location.href='/loginmp.html';</#if>"><b class="icon_aggj"></b><p class="f12">AG国际厅</p></a></li>
				</ul>
				<div style="color:red;">手机客户端登录时，登录前缀AG--大写QB，PT,MG--大写QB7，SA--不需要</div>
			</div>
		</section>
		<!--游戏平台结束-->
		<!--优惠领取-->
		<section class="yhlq" style="margin-bottom: 1rem;">
			<div class="wrap_02">
				<h3 class="f16 grey_4">优惠领取</h3>
				<ul>
					<li><a href="javascript:void(0)" id="act1"><img src="${action_path}mstatic/images/favo/yh_01.jpg"/><p class="f12">点击领取</p></a></li>
					<li><a href="javascript:void(0)" id="act2"><img src="${action_path}mstatic/images/favo/yh_02.jpg"/><p class="f12">点击领取</p></a></li>
					<li><a href="javascript:void(0)" id="act3"><img src="${action_path}mstatic/images/favo/yh_03.jpg"/><p class="f12">点击领取</p></a></li>
					<li><a href="javascript:void(0)" id="act4"><img src="${action_path}mstatic/images/favo/20170608_4.jpg"/><p class="f12">我要签到</p></a></li>
					<li><a href="javascript:void(0)" id="act5"><img src="${action_path}mstatic/images/favo/20170610_4.jpg"/><p class="f12"><font color="red">总积分:10分</font>&nbsp;我要兑换 </p></a></li>		
				</ul>
			</div>
		</section>
		<!--优惠领取 结束-->
		<#include '${action_path}common/mfooter.ftl' />
	</body>
	<script src="${action_path}mstatic/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${action_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>
	<script>
		$(".tiyan").click(function(){
			$(".dh").hide();
			$(".index_ct").show();
		})
		
	   var swiper = new Swiper('.swiper-container', {
	        pagination: '.swiper-pagination',
	        nextButton: '.swiper-button-next',
	        prevButton: '.swiper-button-prev',
	        paginationClickable: true,
	        centeredSlides: true,
	        autoplay: 2500,
	        autoplayDisableOnInteraction: false
	    });

		  $(".close").click(function(){
		  	$("#zhbcz").hide();
		  	$("#zhbcz").prev().hide();
		  	$("#shiwan").hide();
		  	$("#shiwan").prev().hide();
		  });
		  
		  $("#btn_mmzh").click(function(){
		  	$("#zhbcz").show();
		  	$("#zhbcz").prev().show();
		  });
		  $(".header_close").click(function(){
		  	$("#zhbcz").hide();
		  	$("#zhbcz").prev().hide();
		  });
		 $(".sw").click(function(){
		 	$("#shiwan").show();
		 	$("#shiwan").prev().show();
		 })
    </script>
</html>
