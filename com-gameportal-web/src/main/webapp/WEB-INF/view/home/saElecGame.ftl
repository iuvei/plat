<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${title} - 电子游艺</title>
	${meta}
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="${action_path}css/slote.css">
    <link type="text/css" rel="stylesheet" href="${action_path}static/css/topic/egames.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <#-- 弹出框-->
	<link href="${zy_path}plugins/alert/ArtDialog/skins/sixwing.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDrag.js"></script>
	<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDialog.js"></script>
	<script type="text/javascript" src="${zy_path}plugins/alert/ArtDialog/artDialog.js"></script>
	<script type="text/javascript" src="${zy_path}plugins/alert/WebDialog.js"></script>
    <script type="text/javascript">
        $(function () {
          //电子游艺导航
          $(".navlist").find("li").click(function () {
            var check = $(this).attr("data");
            if($(this).attr('id') =='jackpot'){
	           var a= confirm('领取任何优惠，返水，均不能投注本栏目游戏,确定要打开吗？');
	           if(!a){
	               return;
	           }
            }
            $("#type").val(check);
            $("#pageNo").val(1);
          	$("#mgPage").submit();
        	});
            
            $("#gotoPT").click(function(){
           		window.location.href="${action_path}electronic/index.html";
        	});
        	
        	$("#gotoMg").click(function(){
	           window.location.href="${action_path}electronic/agElectronic.html";
	        });
        

            //分页
            var settings = {
                currentPage: ${pageNo}, //当前页
                totalRecoreds: ${total}, //总计录数
                recordsPerPage: 16, //每页X条
                dataText: true, //是否显示统计数据
                startEnd: true, //是否显示第一页最后一页
                prevNext: true, //是否显示上一页下一页
                pageSkip: true, //跳转至X页
                callback: cb, //回调方法
                align: "right" //对齐方式
            };

            function cb() {
            	$("#pageNo").val($(this).text());
                $("#saPage").submit();
            }
            $("#pages").paging(settings);
            
            var a =0,b=0;
			setInterval(function(){
			 var text = $("#gotoMg");
			 if (!a)
			 {
				text.css("background-color","#1e86cb");
				a = 1;
			 }else{
				text.css("background-color","#e7a300");
				a = 0;
			 }
		 },500);
		 
	 	setInterval(function(){
	 		var text = $("#gotoPT");
			 if (!b)
			 {
				text.css("background-color","#e7a300");
				b = 1;
			 }else{
				text.css("background-color","#1e86cb");
				b = 0;
			 }
	 	},500);
            
        });
		
        var iWidth=window.screen.availWidth; //弹出窗口的宽度;
		var iHeight=window.screen.availHeight-60; //弹出窗口的高度;
		var iTop = (window.screen.availHeight-60-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;

		function gotoSAGame(gameCode){
			window.open('${action_path}loginSa.do?gameCode='+gameCode+'&type=1',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
		}
		
		function gotoFreeSAGame(gameCode){
			window.open('${action_path}loginSa.do?gameCode='+gameCode+'&type=0',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
		}

    </script>
</head>

<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <#--头部-->
    <#include "${action_path}common/top.ftl">
    <#--导航-->
    <#include "${action_path}common/header.ftl">
</div>
<div class="bg-pt">
    <div class="container cfx game-wrapper">
    
        <div class="game-tip">
            <marquee id="j-top-scroll" data-marquee="" class="game-tip-marquee" behavior="scroll" direction="left" loop="infinite" scrollamount="3" width="1024" height="63">
                <div class="marquee-item"></div>
            </marquee>
        </div>
        <div class="jackpot"></div>
        <div class="gb-sidenav type-game" style="border-bottom:0px;">
        	<div style="margin-bottom:5px;text-align:center;"><input type="button" value="进入PT老虎机" class="btn btn-login" id="gotoPT" style="background-color: #1e86cb;border-radius: 5px;"></div>
        	<div style="margin-bottom:5px;text-align:center;"><input type="button" value="进入MG老虎机" class="btn btn-login" id="gotoMg" style="background-color: #1e86cb;border-radius: 5px;"></div>
        </div>
        <div class="gb-main-r tab-bd">
            <ul id="tab-hotGames" class="tab-panel gamelist fade active in">
           		<#list listElectronic as electronic>
                    <li class="game-info">
	                    <div class="game-pic">
	                        <img src="${zy_path}images/sagame/${electronic.imageFileName}" height="230" style="border-radius:6px;">
	                    </div>
	                    <div class="name"><h4><#if electronic.gameNameCN??> ${electronic.gameNameCN}<#else>${electronic.gameEnName}</#if></h4></div>
	                    <div class="game-play">
	                        <a href="javascript:;" target="_blank" class="btn-game play" href="javascript:;" onclick="<#if userInfo??>gotoSAGame('${electronic.gameId}');<#else>alert('请先登录或是注册新会员。');</#if>">进入游戏</a>
	                        <a href="javascript:;" target="_blank" class="btn-game demo" href="javascript:;" onclick="gotoFreeSAGame('${electronic.gameId}');">免费试玩</a>
	                    </div>
                	</li>
                 </#list>
            </ul>
        </div>
        <div class="pages" id="pages"></div>
    	<form action="${action_path}electronic/saElecGame.html" method="post" style="display:none;" name="gameParam" id="saPage">
			<input type="hidden" name="type" id="type" value="${type}"/>
			<input type="hidden" name="pageNo" id="pageNo" value="${pageNo}"/>
		</form>
    </div>
</div>
<#--客服-->
<#include "${action_path}common/rightSlider.ftl">
<#--底部-->
<#include "${action_path}common/footer.ftl" />
<script src="${action_path}js/countUp.js"></script>
<script src="${action_path}js/jquery.cookie.js"></script>
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="${zy_path}plugins/attention/zDialog/zDialog.js"></script>
<script type="text/javascript" src="${zy_path}plugins/alert/ArtDialog/artDialog.js"></script>
<script type="text/javascript" src="${zy_path}plugins/alert/WebDialog.js"></script>

</body>
</html>