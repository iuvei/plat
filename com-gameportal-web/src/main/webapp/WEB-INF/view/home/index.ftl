<#include "${action_path}common/config.ftl">
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}--首页</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css?v=1.3">
    <link type="text/css" rel="stylesheet" href="${action_path}css/index.css?v=1.0">
    <style>
    .game-lobby {
      margin:12px 10px;
	  position: relative;
	}
	.game-lobby img {
	  position: absolute;
	  z-index: 1;
	}
	.game-lobby button {
	  position: relative;
	  display: block;
	  margin: 0 auto 30px;
	  top: 100px;
      left: -130px;
	  cursor: pointer;
	}
    </style>
    <link type="text/css" rel="stylesheet" href="${action_path}static/css/public/base.css?v=1.0" />
</head>
<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <!--头部-->
    <#include "${action_path}common/top.ftl">
    <!--导航-->
    <#include "${action_path}common/header.ftl">
</div>

<div id="index-banner" style="margin-bottom: -250px;" class="carousel slide" data-ride="carousel" data-interval="3000">
    <ol class="carousel-indicators" style="bottom: 260px;">
    	<#list adverList as adv>
    		<li data-target="#index-banner" data-slide-to="${adv_index}" <#if adv_index ==0>class="active"</#if>></li>
        </#list>
    </ol>

    <div class="carousel-inner">
    	<#list adverList as ad>
    		<div class="item <#if ad_index==0>active</#if>">
    			<a target="_blank" href="${ad.href}"><img src="/static/images/banner/${ad.images}" width="1920" height="940" alt="${ad.title}"> </a>
        	</div>
        </#list>
    </div>
</div>

<div class="container">
    <div style="margin-bottom: 74px;" class="section-hd box">
        <div class="notice">
            <span class="fl">最新玩家资讯:</span>
            <marquee id="j-top-scroll" data-marquee="" class="top-scroll" behavior="scroll" direction="left" loop="infinite" scrollamount="3" width="980" height="40">
                <div class="marquee-item">
                    <span class="c-blue">会员WL***00</span>成功存款<span class="c-blue">1000</span>元！
                </div>
                <div class="marquee-item">
                    <span class="c-blue">会员xwi***su</span>成功存款<span class="c-yellow">5000</span>元！
                </div>
                <div class="marquee-item">
                    <span class="c-blue">会员d5y**2</span>成功在 游戏中赢得<span class="c-yellow">8883.1</span>元奖励！
                </div>
                <div class="marquee-item">
                    <span class="c-blue">会员rt**8</span>成功存款<span class="c-yellow">1000</span>元奖励！
                </div>
                 <div class="marquee-item">
                    <span class="c-blue">会员139**8</span>成功在 游戏中赢得<span class="c-yellow">8000</span>元奖励！
                </div>
            </marquee>

        </div>
        <div class="ui-row">
            <div class="col-4">
                <a class="inner" href="#">
                    <h2>体验金8-88元</h2>
                    <div class="sub-tit">新注册会员自助领取优惠</div>
                    <!--
                    <h2> 老虎机救援金</h2>
                    <div class="sub-tit">最高赠送5888元自助领取优惠</div>
                    -->
                </a>
            </div>
            <div class="col-4">
                <a class="inner" href="http://cdn.vbet.club/happyslots/d/setupglx.exe">
                    <h2>老虎机下载中心</h2>
                    <div class="sub-tit">电脑客户端，手机端</div>
                    <button class="btn btn-warning">立即下载</button>
                </a>
            </div>
            <div class="col-4 pr">
                <div class="jackpot" id="j-jackpot">2,982,433,88</div>
            </div>
        </div>
    </div>
    <div class="section-main box cfx">
        <div class="aside-bg fl">
            <img id="j-superman" class="superman" src="images/man.png" alt="" width="284" height="442">
        </div>
        <div class="game-box fl">
            <ul class="game-tab clearfix">
                <li class="active"><a data-toggle="tab" href="#tab-sa-game">SA老虎机</a></li>
                <li><a data-toggle="tab" href="#tab-pt-game">PT老虎机</a></li>
                <li><a data-toggle="tab" href="#tab-mg-game">MG老虎机</a></li>
                <li><a data-toggle="tab" href="#tab-ag-game">AG捕鱼游戏&nbsp;<img src="/images/hot.gif" style="margin-bottom:25px"/></a></li>
            </ul>
            <div class="tab-bd">
                <div id="tab-pt-game" class="tab-panel">
                    <ul class="clearfix gamelist">
                        <li>
                            <div class="game-name">古怪猴子</div>
                            <div class="game-pic">
                                <img src="/images/fm.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoPtGame('fm');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('fm');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">五虎将</div>
                            <div class="game-pic">
                                <img src="/images/ptgame/ftg.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoPtGame('ftg');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('ftg');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">黄金之旅</div>
                            <div class="game-pic">
                                <img src="/images/ptgame/goldentour.jpg">
                            </div>
                          	<a class="i-btn" href="javascript:;" onclick="gotoPtGame('gos');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('gos');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">蜘蛛侠</div>
                            <div class="game-pic">
                                <img src="/images/ptgame/spidergreen.jpg">
                            </div>
                          	<a class="i-btn" href="javascript:;" onclick="gotoPtGame('spidc');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('spidc');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">公路王国</div>
                            <div class="game-pic">
                                <img src="/images/ptgame/high_way.jpg">
                            </div>
                           	<a class="i-btn" href="javascript:;" onclick="gotoPtGame('hk');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('hk');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">船长的宝藏</div>
                            <div class="game-pic">
                                <img src="/images/ptgame/captains-treasure.jpg">
                            </div>
                           	<a class="i-btn" href="javascript:;" onclick="gotoPtGame('ct');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreePTGame('ct');">免费试玩</a>
                        </li>

                    </ul>
                </div>
                <div id="tab-mg-game" class="tab-panel">
                    <ul class="clearfix gamelist mg">
                         <li>
                            <div class="game-name">超级厨王</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/newmg/BTN_Belissimo17.png">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1367','1001');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1367','1001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">水果老虎机</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/newmg/BTN_FruitSlots1.png">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1285','1001');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1285','1001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">新年快乐</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/newmg/BTN_HappyNewYear3.png">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1145','1001');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1145','1001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">黄金海岸</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/mggame/BTN_GoldCoast3.png">
                            </div>
                             <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1066','1001');">进入游戏</a>
                             <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1066','1001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">黄金龙</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/newmg/BTN_GoldenDragon6.png">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1138','1001');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1138','1001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name"> 财富之轮</div>
                            <div class="mg-game-pic" style="width: 133px;">
                                <img src="/images/newmg/BTN_SpectacularWheelOfWealth1.png">
                            </div>
                             <a class="i-btn" href="javascript:;" onclick="gotoMgGame('1031','1001');">进入游戏</a>
                             <a class="i-btn demo" href="javascript:;" onclick="gotoFreeMgGame('1031','1001');">免费试玩</a>
                        </li>
                    </ul>
                </div>
                
                <div id="tab-sa-game" class="tab-panel active">
                    <ul class="clearfix gamelist">
                        <li>
                            <div class="game-name">张保仔</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-A018.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-A018');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-A018');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">发大财</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-051.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-051');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-051');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">过大年</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-A001.jpg">
                            </div>
                          	<a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-A001');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-A001');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">三星报囍</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-A002.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-A002');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-A002');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">梦幻女神</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-A005.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-A005');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-A005');">免费试玩</a>
                        </li>
                        <li>
                            <div class="game-name">黄飞鸿</div>
                            <div class="game-pic">
                                <img src="/images/sagame/EG-SLOT-S003.jpg">
                            </div>
                            <a class="i-btn" href="javascript:;" onclick="gotoSaGame('EG-SLOT-S003');">进入游戏</a>
                            <a class="i-btn demo" href="javascript:;" onclick="gotoFreeSaGame('EG-SLOT-S003');">免费试玩</a>
                        </li>

                    </ul>
                </div>
                
                <div id="tab-ag-game" class="tab-panel">
                	<div class="game-lobby">
                    	<img src="/images/agby01.jpg"  title="AG扑鱼游戏"/>
                    	<button class="btn btn-join" onclick="javascript:<#if userInfo??>window.open('/game/loginGameAG/AGIN/1/6.do')<#else>alert('请登录后再进入游戏!')</#if>;"><img src="/images/agby02.png" /></button>
                    	<button class="btn btn-free" onclick="javascript:window.open('/game/loginGameAG/AGIN/0/6.do');"><img src="/images/agby01.png" /></button>
                    </div>
                </div>

            </div>
        </div>
        <div class="winner-box fr">
            <h2>玩家中奖信息</h2>
            <div class="winner-wrapper" id="j-marquee-aside">
                <ul class="winner-list" >
                    <#if winRecords??>
			    		<#list winRecords as content>
			    			<li data-marquee-item=\"\" class=\"marquee -item\" style="padding:10px 14px 10px 18px;border-bottom: 1px solid #ebebeb;"><a>
			    				${content}
			    			</a></li>
			    		</#list>
					</#if>
                </ul>

            </div>
        </div>
        <a href="/electronic/index.html" class="more-game">更多推荐游戏</a>
    </div>
    <div class="section-ft box ui-row">
        <div class="col-4 item">
            <i class="fl iconfont icon-person2"></i>
            <h2>免费代理加盟</h2>
            <p class="sub-tit">零投资 高回报 实现土豪梦</p>
        </div>
        <div class="col-4 item">
            <i class="fl iconfont icon-diamond"></i>
            <h2>VIP俱乐部</h2>
            <p class="sub-tit">全新优惠汹涌来袭</p>
        </div>
        <div class="col-4 item">
            <i class="fl iconfont icon-fourm"></i>
            <h2>优质客服为你解答</h2>
            <p class="sub-tit">7x24 小时在线 一对一为您服务</p>
        </div>
		<input type="hidden" id ="vuid" value="${vuid}" />
    </div>
</div>

<!--底部-->
<#include "${action_path}common/footer.ftl">
<!--客服-->
<#include "${action_path}common/rightSlider.ftl">

<!--首页弹框
<#if !userInfo??>
<div class="modal fade" id="modal-index" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display: none;">
    <div class="modal-dialog" role="document">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&nbsp;</span></button>
        <div class="notice-wp">
            <div>
                <h1 class="notice-tit">号外号外！</h1>

                <p class="notice-cnt">由于支付宝，微信受到严厉的监管，导致支付成本大幅上升，为了更好地为亲们提供服务，从即日起，支付宝，微信扫码入款将加收1%手续费，QQ钱包扫码入款继续免费！</p>
            </div>
        </div>
    </div>
</div>
</#if>
-->
<#--订阅-->
<div id="aside-ad">
    <button class="close" data-dismiss="alert">&nbsp;</button>
    <a href="/favo.html" target="_blank"> &nbsp; </a>
</div>

</body>
</html>
<script src="${action_path}js/countUp.js"></script>
<script src="${action_path}js/index.js?v=1.1"></script>
<#-- 弹出框-->
<link href="${zy_path}plugins/alert/ArtDialog/skins/sixwing.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDialog.js"></script>
<script type="text/javascript" src="${zy_path}plugins/alert/ArtDialog/artDialog.js"></script>
<script type="text/javascript" src="${zy_path}plugins/alert/WebDialog.js"></script>
<script type="text/javascript">
	var iWidth=window.screen.availWidth; //弹出窗口的宽度;
	var iHeight=window.screen.availHeight-60; //弹出窗口的高度;
	var iTop = (window.screen.availHeight-60-iHeight)/2; //获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	
	function gotoPtGame(gameName){
 		 if("${userInfo}" ==''){
 		 	alert('请登录后再进入游戏!');
 		 	return;
 		 }
		 ptOut();
		 window.open('http://cache.download.banner.greatfortune88.com/casinoclient.html?language=zh-cn&game='+gameName+'&mode=offline&currency=CNY',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
		  
		 /**
		 diag.Drag=true;
		 diag.Title ="游戏开启中";
		 diag.URL = 'http://slot.qianbaobet.com/electronic/GameHall.html?gameName='+gameName+'&vuid=${vuid}';
		 diag.Width = 960;
		 diag.Height = 750;
		 diag.CancelEvent = function(){ //关闭事件
			ptOut();
			diag.close();
		 };
		 diag.show();
		 */
	}
	
	function gotoFreePTGame(gameName){
		window.open('http://cache.download.banner.greatfortune88.com/casinoclient.html?language=zh-cn&game='+gameName+'&mode=offline&currency=CNY',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
	}
	
	
	function gotoSaGame(gameName){
 		 if("${userInfo}" ==''){
 		 	alert('请登录后再进入游戏!');
 		 	return;
 		 }
		 window.open('/loginSa.html?gameCode='+gameName+'&type=1',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
	}
	
	function gotoFreeSaGame(gameName){
		window.open('/loginSa.html?gameCode='+gameName+'&type=0',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
	}
	
	function ptOut(){
		$.ajax({
		    type : "POST",
		    url : '/electronic/logoutPTGame.do',
		    dataType : "json",
		    async : true,
		    success : function(data) {},
		    error: function(){}
	    }); 
	}
	
	//var diag = new Dialog();    
	window.onbeforeunload = function(event) { 
		ptOut();
	}
	
	function gotoMgGame(itemId,appId){
		if("${userInfo}" ==''){
	 		 alert('请登录后再进入游戏!');
	 		 return;
 		 }
		 window.open('${action_path}game/loginGameMG/'+itemId+'/'+appId+'/1.html',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
	}
	
	function gotoFreeMgGame(itemId,appId){
		 window.open('${action_path}game/loginGameMG/'+itemId+'/'+appId+'/0.html',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
	}
</script>