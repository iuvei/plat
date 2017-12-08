<!DOCTYPE html>
<html>
	<head>
		<#include "${action_path}common/config.ftl">
		<style type="text/css">
			@charset "UTF-8";
			[ng\:cloak],
			[ng-cloak],
			[data-ng-cloak],
			[x-ng-cloak],
			.ng-cloak,
			.x-ng-cloak,
			.ng-hide:not(.ng-hide-animate) {
				display: none !important;
			}
			
			ng\:form {
				display: block;
			}
			
			.ng-animate-shim {
				visibility: hidden;
			}
			
			.ng-anchor {
				position: absolute;
			}
		</style>
		<meta charset="utf-8">
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
		<meta name="format-detection" content="telephone=no">
		<meta name="screen-orientation" content="portrait">
		<meta name="x5-fullscreen" content="true">
		<meta name="x5-orientation" content="portrait">
		<title id="apptitle">PT电子游戏</title>
		<link rel="stylesheet" href="${zy_path}mstatic/css/main.pt.css?v=1.1">
		<script src="${zy_path}mstatic/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="${zy_path}mstatic/js/base.min.js"></script>
		<style type="text/css">
			.incap_from-left .incap_shadow,
			.incap_from-right .incap_shadow {
				display: none!important
			}
			
			.incap_from-right .incap_btn-area {
				float: left!important
			}
			
			.incap_page-tooltip {
				position: fixed
			}
			
			.incap_from-right.incap_bottom {
				top: 90%
			}
			
			.incap_from-left,
			.incap_from-right {
				top: 50%;
				left: 0;
				height: 49px;
				margin: -25px 0 0
			}
			
			.incap_from-right {
				left: auto;
				right: 0
			}
			
			.incap_from-right .incap_btn-holder {
				float: left!important;
				background-position: -143px 0
			}
			
			.incap_from-bottom1 {
				bottom: 0;
				left: 0;
				width: 52px
			}
			
			.incap_from-bottom2 {
				bottom: 0;
				left: 50%;
				width: 52px;
				margin: 0 0 0 -27px
			}
			
			*+html .incap_from-bottom2 {
				width: 234px;
				margin: 0 0 0 -183px
			}
			
			.incap_from-bottom3 {
				bottom: 0;
				right: 0;
				width: 52px
			}
			
			.incap_from-bottom2.active {
				margin: 0 0 0 -209px
			}
			
			.incap_page-tooltip .incap_btn-area {
				overflow: hidden
			}
			
			.incap_page-tooltip .incap_btn-holder {
				float: right;
				width: 49px;
				height: 57px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/sprite.png) no-repeat -143px 0*/
			}
			
			.incap_page-tooltip.active .incap_btn-holder {
				background-position: 0 0
			}
			
			.incap_page-tooltip .incap_mask {
				overflow: hidden;
				float: left
			}
			
			.incap_from-bottom1 .incap_mask,
			.incap_from-bottom2 .incap_mask,
			.incap_from-bottom3 .incap_mask {
				float: none;
				width: 234px
			}
			
			.incap_from-bottom1 .incap_btn-holder,
			.incap_from-bottom2 .incap_btn-holder,
			.incap_from-bottom3 .incap_btn-holder {
				float: right;
				width: 57px;
				background-position: -349px 0;
				height: 58px
			}
			
			.incap_from-bottom1 .incap_btn-holder {
				float: left;
				background-position: -439px 0
			}
			
			.incap_from-bottom1.active .incap_btn-holder,
			.incap_from-bottom2.active .incap_btn-holder,
			.incap_from-bottom3.active .incap_btn-holder {
				background-position: -274px 0
			}
			
			.incap_from-bottom1.active .incap_btn-holder {
				background-position: -514px 0
			}
			
			.incap_from-right.active .incap_btn-holder {
				background-position: -209px 0
			}
			
			.incap_logo-image {
				float: left;
				width: 36px;
				height: 42px;
				overflow: hidden;
				text-indent: -9999px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/logo-image.png) no-repeat;*/
				margin: 5px 0 0 5px
			}
			
			.incap_logo-image:hover {
				background-position: 0 -58px
			}
			
			.incap_from-bottom1 .incap_logo-image,
			.incap_from-bottom2 .incap_logo-image,
			.incap_from-bottom3 .incap_logo-image {
				margin: 14px 0 0 15px
			}
			
			.incap_from-bottom1 .incap_logo-image {
				margin: 14px 0 0 6px
			}
			
			.incap_shadow {
				float: left;
				width: 177px;
				height: 34px;
				font-size: 0;
				line-height: 0;
				text-indent: -9999px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/sprite.png) no-repeat -637px 0;*/
				display: none;
				margin: 24px 0 0
			}
			
			.incap_page-tooltip.active .incap_shadow {
				display: block
			}
			
			.incap_from-bottom1 .incap_shadow {
				float: right;
				background-position: -870px 0
			}
			
			.incap_tooltip-box {
				float: left;
				width: 227px
			}
			
			.incap_page-tooltip .incap_c {
				overflow: hidden;
				width: 227px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/sprite.png) no-repeat -1163px 0*/
			}
			
			.incap_from-right .incap_c {
				background-position: -1407px 0
			}
			
			.incap_from-bottom1 .incap_c,
			.incap_from-bottom2 .incap_c,
			.incap_from-bottom3 .incap_c {
				width: 234px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/sprite.png) repeat-y -1661px 0*/
			}
			
			.incap_from-bottom1 .incap_c {
				background-position: -1936px 0
			}
			
			.incap_page-tooltip .incap_b {
				overflow: hidden;
				width: 227px;
				height: 8px;
				font-size: 0;
				line-height: 0;
				text-indent: -9999px;
				/*background: url(/_Incapsula_Resource?NWFURVBO=images/sprite.png) no-repeat 0 -311px*/
			}
			
			.incap_from-right .incap_b {
				background-position: -253px -311px
			}
			
			.incap_from-bottom1 .incap_b,
			.incap_from-bottom2 .incap_b,
			.incap_from-bottom3 .incap_b {
				width: 234px;
				background-position: -529px -311px
			}
			
			.incap_from-bottom1 .incap_b {
				background-position: -866px -311px
			}
			
			.incap_page-tooltip .incap_text {
				text-align: center;
				overflow: hidden;
				color: #757679;
				font: 11px/14px Arial, Helvetica, sans-serif;
				padding: 14px 8px 8px 18px
			}
			
			.incap_page-tooltip .incap_text p {
				margin: 0
			}
			
			.incap_page-tooltip .incap_heading {
				display: block;
				color: #050708;
				font: 18px/19px Arial, Helvetica, sans-serif;
				margin: 0 0 13px
			}
			
			.incap_page-tooltip .incap_heading a {
				font-weight: 700;
				font-style: normal;
				color: #25a8e0!important
			}
			
			.incap_from-left .incap_btn-area,
			.incap_from-right .incap_mask {
				float: right!important
			}
			
			*+html .incap_from-bottom3,
			.incap_from-bottom2.active,
			.incap_from-bottom3.active,
			.incap_from-bottom1.active {
				width: 234px
			}
			
			.incap_from-right .incap_text,
			.incap_from-bottom2 .incap_text,
			.incap_from-bottom3 .incap_text {
				padding-left: 20px
			}
			
			#qbyx{width:40%;position: relative;}
			#qbyx label{width:100%}
			#qbyx .xiala_bg{display:none;background-color: #ccc;opacity: 1.5;filter:Alpha(opacity=50);position: absolute;top:1rem;left:0;width:3rem;height:6.3rem;z-index: 9999;}
			#qbyx .xiala{display:none;position: absolute;top:1rem;left:0;z-index: 9999;}
			#qbyx .xiala_box{width:3rem;text-align: center;color:#333;line-height:1.5rem;border-bottom: 1px solid #999;}
			#qbyx .xiala_box:last-child{border-bottom: 0;}
		</style>
		<script type="text/javascript">
			function xiala(){
				$('.xiala').slideDown();
				$(".xiala_bg").slideDown();
			}
			
			function xiala_up(obj){
				var ct=$(obj).html();
				$('.xiala').slideUp();
				$(".xiala_bg").slideUp();
				$(obj).parent().siblings(".ng-binding").html(ct);
				return false;
			}
			
			function getBalance(){
				window.location.reload();
			}
			
			function search(){
				$("#gamename").val(encodeURI($("#gameName").val()));
				ptmobile.submit();
			}
		</script>
	</head>

	<body>
		<!-- uiView:  -->
		<div ui-view="" class="app ng-scope">
			<div class="navbar navbar-app navbar-absolute-top ng-scope">
				<div class="navbar-brand navbar-brand-center ng-binding" ui-yield-to="title">PT 电子</div>
				<div class="btn-group pull-left">
					<!-- ngIf: currentUser.TestState==0 -->
					<a ng-if="currentUser.TestState==0" class="button button-icon ion-home ng-scope" href="/indexmp.html"></a>
					<!-- end ngIf: currentUser.TestState==0 -->
					<!-- ngIf: currentUser.TestState!=0 -->
				</div>
				<!-- ngIf: currentUser.TestState==0 -->
				<div class="btn-group pull-right ng-scope" ng-if="currentUser.TestState==0">
					<a href="/mobile/transfermp.html" class="button ng-binding">转账</a>
				</div>
				<!-- end ngIf: currentUser.TestState==0 -->
			</div>
			<div class="app-body ng-scope">
				<div class="app-content">
					<div class="top">
						<!-- ngIf: balanceLoaded && currentUser.TestState!=2 --><span ng-if="balanceLoaded &amp;&amp; currentUser.TestState!=2" class="ng-scope"><img ng-src="${zy_path}mstatic/images/reload.png" src="${zy_path}mstatic/images/reload.png" onclick="getBalance()"><span class="ng-binding">${accountMoneyCount!}</span></span>
						<!-- end ngIf: balanceLoaded && currentUser.TestState!=2 -->
						<!-- ngIf: !balanceLoaded && currentUser.TestState!=2 -->
					</div>
					<div class="game-form">
						<!--
						<div id="qbyx" >
							<label ui-turn-on="typeSideUp" ui-state="typeSideUp" class="ng-binding" onclick=xiala()>全部游戏<em></em></label>
							<div class="xiala_bg"></div>
							<div class="xiala" >
								<div class="xiala_box" onclick=xiala_up(this)>全部游戏</div>
								<div class="xiala_box" onclick=xiala_up(this)>热门游戏</div>
								<div class="xiala_box" onclick=xiala_up(this)>金典游戏</div>
								<div class="xiala_box" onclick=xiala_up(this)>其他游戏</div>
							</div>
						</div>
						-->
						<input type="text" name="gameName" id="gameName" value="${gamename}"  placeholder="搜索游戏" class="ng-pristine ng-untouched ng-valid needsclick"> <button class="btn" onclick="search()"><i class="ion-search"></i></button></div>
					<div class="scrollable-index">
						<div class="scrollable-con" afkl-image-container="">
							<!-- ngIf: mgList.length==0&&!mgListLoaded -->
							<!-- ngIf: mgList.length==0&&mgListLoaded -->
							<!-- ngIf: mgList.length>0 -->
							<div class="game-content ng-scope" ng-if="mgList.length>0">
								<!-- ngRepeat: item in mgList -->
								<#list listElectronic as electronic>
								<div class="game-item ng-scope" onclick="javascript:<#if open>window.location.href='http://slot.qianbaobet.com/mobile/launchptmp.html?gameName=${electronic.gameName}&vuid=${vuid}';<#else>ui.msg('游戏维护中,请稍后!',3)</#if>"" ng-repeat="item in mgList"><span afkl-lazy-image="${zy_path}images/pt/ano.png" afkl-lazy-image-options="{&quot;offset&quot;: 0}" class="" afkl-lazy-image-loaded="done"><img src="${zy_path}mstatic/images/mpt/${electronic.imageFileName}"></span>
									<p class="ng-binding">${electronic.gameNameCN}</p>
								</div>
								</#list>
								<!-- end ngRepeat: item in mgList -->
								
							</div>
							<!-- end ngIf: mgList.length>0 -->
						</div>
					</div>
				</div>
			</div>
			<side-up-menu sideup-value="1" sideup-trigger="typeSideUp" sideup-select="typeSelect(value)" class="ng-scope ng-isolate-scope">
				<!-- uiIf: {{sideupTrigger}} -->
			</side-up-menu>
		</div>
		
		<div class="incap_page-tooltip incap_from-right incap_bottom">
			<div class="incap_btn-area"> <span class="incap_shadow">&nbsp;</span>
				<div class="incap_btn-holder">
					<a href="#" class="incap_logo-image">Incapsula</a>
				</div>
			</div>
			<div class="incap_mask">
				<div class="incap_tooltip-box" style="margin-right: -227px;">
					<div class="incap_c">
						<div class="incap_text"> <strong class="incap_heading" style="font-weight:normal !important">Protected &amp; Accelerated<br> by <a style="font-size: 18px">Incapsula</a></strong>
							<p> <label id="incap_threat_count">10,961,454</label> threats blocked this month </p>
						</div>
					</div>
					<div class="incap_b"> &nbsp; </div>
				</div>
			</div>
		</div>
		<form action="/game/ptmp.do" name="ptmobile" style="display:none;">
			<input name="gamename" id="gamename" />
			<input name="type" id="type" />
		</form>
	</body>

</html>