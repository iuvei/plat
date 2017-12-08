<!doctype html>
<html lang="zh-cn">
<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}-真人视讯</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css?v=1.1">
    <link type="text/css" rel="stylesheet" href="${action_path}css/liveCasino.css?v=1.1">
    <link type="text/css" rel="stylesheet" href="${action_path}static/css/public/base.css?v=1.1" />
  
</head>
<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <#--头部-->
    <#include "${action_path}common/top.ftl">
    <#--导航-->
    <#include "${action_path}common/header.ftl">
</div>

<ul class="tab-hd live-tab ui-row">
    <li class="active col-6"><a href="#tab-ag" data-toggle="tab">
        <i class="iconfont icon-pocker"></i>AG国际厅
    </a></li>
    <!--
    <li class="col-4"><a href="#tab-ag2" data-toggle="tab">
        <i class="iconfont icon-pocker"></i>AG旗舰厅
    </a></li>
    -->
    <li class="col-6"><a href="#tab-bbin" data-toggle="tab">
        <i class="iconfont icon-money"></i>BBIN真人视讯
    </a>
    </li>
</ul>

<div class="tab-bd">
    <div id="tab-ag" class="tab-panel  active" style="padding-top: 580px; background:#fff url('images/banner/live.jpg') 50% 0 no-repeat">
        <div class="container live-section ui-row">
            <div class="col-6">
                <div class="item">
                    <p>全球首创6张牌先发百家乐旗舰厅 ，华丽高质素特大视频，页面清晰。无需下载，更加快捷方便。 连环百家乐，同时观看下注多个游戏房间，不会错过任何好牌路。</p>
                     <a href="javascript:;" class="btn btn-warning" onclick="javascript:<#if userInfo??>window.open('/game/loginGameAG/AGIN/1/2.do')<#else>$.msg('请登录后再进入游戏!',3)</#if>;">进入游戏</a>
                <a href="javascript:;" onclick="javascript:window.open('/game/loginGameAG/AGIN/0/1.do');" class="btn btn-primary">免费试玩</a>
                </div>

            </div>
            <div class="col-6">
                <div class="item">
                    <img class="fl m10" src="images/qr/ag.png" width="130" height="130" alt="">
                    <p>
                       温馨提示：AG手机客户端登录需要在账号前加QB <br>
                    例如： <br>
                    您的游戏账号为abc12345，如果用AG手 机客户端登录，请输入QBabc12345
                    </p>
                    <a href="javascript:;" class="btn btn-primary">点击下载</a>
                </div>
            </div>
        </div>
    </div>
    <div id="tab-ag2" class="tab-panel" style="padding-top: 580px; background:#fff url('images/banner/live03.jpg') 50% 0 no-repeat">
        <div class="container live-section ui-row">
            <div class="col-6">
                <div class="item">
                    <p>全球首创6张牌先发百家乐旗舰厅 ，华丽高质素特大视频，页面清晰。无需下载，更加快捷方便。 连环百家乐，同时观看下注多个游戏房间，不会错过任何好牌路。</p>
                    <a href="javascript:;" class="btn btn-warning" onclick="javascript:<#if userInfo??>window.open('/game/loginGameAG/AG/1/1.do')<#else>$.msg('请登录后再进入游戏!',3)</#if>;">进入游戏</a>
                </div>

            </div>
            <div class="col-6">
                <div class="item">
                    <img class="fl m10" src="images/qr/ag.png" width="130" height="130" alt="">
                    <p>
                       温馨提示：AG手机客户端登录需要在账号前加QB <br>
                    	例如： <br>
                   		 您的游戏账号为abc12345，如果用AG手 机客户端登录，请输入QBabc12345
                    </p>
                    <a href="javascript:;" class="btn btn-primary">点击下载</a>
                </div>
            </div>
        </div>
    </div>
    <div id="tab-bbin" class="tab-panel" style="padding-top: 580px; background:#fff url('images/banner/live02.jpg') 50% 0 no-repeat">
        <div class="container live-section ui-row">
            <div class="item">
                <p>业界最早最著名的真人平台之一, 平台稳定细致, 娱乐产品多元化，秉持高品质的服务于玩家，游戏公平、公正、公开。玩儿法众多，趣味性强，投注金额低，适合大多数玩家尝试！</p>
                <div class="text-center">
                    <a href="javascript:;" class="btn btn-warning" onclick="javascript:<#if userInfo??>window.open('/game/loginGameBBIN.html?ps=live')<#else>$.msg('请登录后再进入游戏!',3)</#if>;">进入游戏</a>
                </div>
            </div>
        </div>
    </div>
<!--底部-->
<#include "${action_path}common/footer.ftl">
<!--客服-->
<#include "${action_path}common/rightSlider.ftl">
</body>
</html>