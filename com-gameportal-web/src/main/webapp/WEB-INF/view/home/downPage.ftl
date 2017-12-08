<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}--下载专区</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css">
    <link type="text/css" rel="stylesheet" href="${action_path}css/liveCasino.css">
   
</head>

<body class="bg-gray">
	<div class="wrapper ui-header" id="ui-header">
	    <#--头部-->
	    <#include "${action_path}common/top.ftl">
	    <#--导航-->
	    <#include "${action_path}common/header.ftl">
	</div>
	
	<div class="download-bg download-page">
    <div class="container">
        <ul class="tab-hd live-tab type2">
            <li class="active"><a href="#tab-pt" data-toggle="tab">PT平台</a></li>
            <li><a href="#tab-ag" data-toggle="tab">AG平台</a></li>
            <li><a href="#tab-mg" data-toggle="tab">MG平台</a></li>
            <li><a href="#tab-bbin" data-toggle="tab">BBIN平台</a></li>
            <li><a href="#tab-other" data-toggle="tab">其他工具</a></li>
        </ul>

        <div class="tab-bd">
            <div id="tab-pt" class="tab-panel active">
                <div class="live-section ui-row">
                    <div class="col-4">
                        <div class="item">
                            <h2>PT平台PC端</h2>
                            <p>1、版本号：1.0.0</p>
                            <p>2、发布时间：2015.11.06</p>
                            <p>3、大小：58M</p>
                            <p>4、版本说明：支持xp和win7系统</p>
                            <a href="http://cdn.vbet.club/happyslots/d/setupglx.exe" class="btn btn-warning">下载地址一</a>
                            <a href="http://link.vbet.club/happyslots" class="btn btn-warning">下载地址二</a>
                        </div>

                    </div>
                    <div class="col-4">
                        <div class="item">
                            <h2>安卓版本</h2>
                            <div>
                                <img class="fr" src="images/qr/pt-android.png" alt="" width="86" height="86">
                                <p>1、版本号：1.0.0</p>
                                <p>2、发布时间：2015.11.06</p>
                                <p>3、大小：15.8M</p>
                                <p>4.版本说明： 适用于android2.1以上版本</p>
                                <a href="http://m.ld176888.com/download.html" class="btn btn-warning">下载手机版</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="item">
                            <h2>登陆提示</h2>
                            <p><span class="c-blue">温馨提示：</span>PT电脑客户端登录需要在账号前加
                                QB7,并且账号中的字母需要大写
                                例如：您的游戏账号为abc123，如果用PT
                                电脑客户端登录,请输入QB7ABC123
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-ag" class="tab-panel">
                <div class="live-section ui-row">
                 	<div class="col-3">
                        <div class="item text-center">
                            <h2>捕鱼王</h2>
                            <img src="images/qr/qrcode.jpg" width="120" height="120" alt="">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="item text-center">
                            <h2>Ipad版本</h2>
                            <img src="images/qr/ag-1.png" width="120" height="120" alt="">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="item text-center">
                            <h2>苹果版本</h2>
                            <img src="images/qr/ag-1.png" width="120" height="120" alt="">
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="item text-center">
                            <h2>安卓版本</h2>
                            <img src="images/qr/ag-1.png" width="120" height="120" alt="">
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-bbin" class="tab-panel">
                <div class="live-section ui-row">
                   <div class="col-4">
                        <div class="item">
                             <span style="font-weight: bold;font-size: 24px;">BBIN平台官方不提供客户端。</span>
                             <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-mg" class="tab-panel">
                <div class="live-section ui-row">
                    <div class="col-4">
                        <div class="item">
                            <h2>MG平台PC端</h2>
                            <p>1、<font color="red">下载安装时请选择中文</font></p>
                            <p>2、版本号：1.0.0</p>
                            <p>3、发布时间：2015.11.06</p>
                            <p>4、大小：824KB4、版本说明：支持xp和win7系统</p>
                            <a href="http://gmaster888.com/download.php?file_name=Launch98.exe&ul=ZH-CN&btag2=23262&&btag3=gmaster" class="btn btn-warning">立即下载</a>
                        </div>

                    </div>
                    <div class="col-4">
                        <div class="item">
                            <h2>手机网页版</h2>
                            <img src="/static/images/topic/client/android_rng_apk.jpg"/>
                          	<p>1、版本号：1.0.0</p>
                            <p>2、发布时间：2015.11.06</p>
                            <p>4.版本说明： 适用于android2.1以上版本</p>
                            <a href="/electronic/mgWapWebDownload.do" class="btn btn-warning">下载手机版</a>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="item">
                            <h2>Android版本</h2>
                            <img src="/static/images/topic/client/android_rng_apk.jpg">
                            <p>1、版本号：1.0.0</p>
                            <p>2、发布时间：2015.11.06</p>
                            <p>4.版本说明： 适用于android2.1以上版本</p>
                            <a href="/electronic/mgWapWebDownload.do" class="btn btn-warning">下载Android版</a>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-other" class="tab-panel">
            	<div class="live-section ui-row">
            	
            		<div class="col-4">
                        <div class="item">
                            <h2>一键加速器</h2>
                            <p>1、版本号：1.0.1</p>
                            <p>2、发布时间：2017.01.05</p>
                            <p>3、大小：2.62 MB</p>
                            <a href="http://pan.baidu.com/s/1kU8NsPL" class="btn btn-warning">立即下载</a>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="item">
                            <h2>一键切换DNS</h2>
                            <p>1、版本号：1.0.0</p>
                            <p>2、发布时间：2015.11.06</p>
                            <p>3、大小：600KB</p>
                            <a href="http://pan.baidu.com/s/1bPLZ8i" class="btn btn-warning">立即下载</a>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
</div>
<#--客服-->
<#include "${action_path}common/rightSlider.ftl">
<script src="${action_path}js/jquery.js"></script>
<script src="${action_path}js/jquery.cookie.js"></script>
<script src="${action_path}static/js/public/utils.js"></script>
<script src="${action_path}static/js/public/common.js"></script>
<script src="${action_path}js/base.js"></script>
<script src="${action_path}js/login.js"></script>
<script type="text/javascript">
    $(function () {
        var $main = $("#main");
        $main.find(".nav").find("li").click(function () {
        	/**
            var _index = $(this).index();
            if(_index >1){
            	$.msg('敬请期待！',3);
            }
            */
            $(this).addClass("active").siblings(".active").removeClass("active");
            $main.find(".box").eq(_index).removeClass("ui-none").siblings(".box").addClass("ui-none");
        });
    });
</script>
</body>
</html>