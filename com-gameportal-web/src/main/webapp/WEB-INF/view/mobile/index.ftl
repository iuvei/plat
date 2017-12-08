<!DOCTYPE html>
<html>
	<head>
		<#include "${action_path}common/config.ftl">
		<meta charset="UTF-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	    <meta name="keywords" content="">
	    <meta name="description" content="">
	    <meta name="format-detection" content="telephone=yes">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<link rel="Bookmark" href="${zy_path}favicon.ico">
		<title>${title}</title>
	</head>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${zy_path}mstatic/css/li.css"/>
	<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$(function(){
			var hei=$(window).height();
			$(".swiper-slide").height(hei);
		})
		
	</script>
	<body>
		<!-- 启动动画 -->
		<div class="dh">
			<div class="swiper-container">
		        <div class="swiper-wrapper">
		            <div class="swiper-slide" style="background: url(${action_path}mstatic/images/phone_01.jpg) no-repeat;width:100%;height:auto;background-size: cover;">
		            	<a href="${action_path}indexmp.html" class="btn f14 tiyan" style="display: block;">立即体验</a>
		            </div>
		            <div class="swiper-slide" style="background: url(${action_path}mstatic/images/phone_02.jpg) no-repeat;width:100%;height:auto;background-size: cover;">
			            <a href="${action_path}indexmp.html" class="btn f14 tiyan" style="display: block;">立即体验</a>
			            <p class="p_fonts f12">Copyright@2016 Qian Bao .All Rights Reserved.</p>
		            </div>
		        </div>
		        <div class="swiper-pagination"></div>
		    </div>
		</div>
		<!--启动动画结束-->
	
	</body>
	<script src="${action_path}mstatic/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${action_path}mstatic/js/li.js" type="text/javascript" charset="utf-8"></script>

	<script>
	   var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        spaceBetween: 30,
    });
    </script>
</html>