<!doctype html>
<html lang="zh-cn">
<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}-电子游艺</title>
    <link type="text/css" rel="stylesheet" href="${action_path}css/base.css">
    <link type="text/css" rel="stylesheet" href="${action_path}css/slote.css">
    <link type="text/css" rel="stylesheet" href="${action_path}static/css/topic/egames.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
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
            <marquee id="j-top-scroll" data-marquee="" class="game-tip-marquee" behavior="scroll" direction="left" loop="infinite" scrollamount="3" width="660" height="63">
                <div class="marquee-item">温馨提示：PT电脑客户端、PT老虎机安卓手机客户端、PT真人安卓手机客户端登录，需要在账号前加 QB7,并且账号中的字母需要大写 例如：您的游戏账号为abc123，若用以上设备登录游戏,请输入QB7ABC123。</div>
            </marquee>
        </div>
        <div class="jackpot">
            JACKPOT累积奖池 <span id="j-game-jackpot">398,243.38</span>
        </div>
        <div class="gb-sidenav type-game">
        	<div style="margin-bottom:5px;text-align:center;"><input type="button" value="进入SA老虎机" class="btn btn-login" id="gotoSa" style="background-color: #1e86cb;border-radius: 5px;"></div>
        	<div style="margin-bottom:5px;text-align:center;"><input type="button" value="进入MG老虎机" class="btn btn-login" id="gotoMg" style="background-color: #1e86cb;border-radius: 5px;"></div>
            <ul class="navlist">
                <li <#if type == 1> class="active" </#if> data="1"><a data-toggle="tab">热门游戏</a></li>
                <li <#if type == 8> class="active" </#if> data="8"><a data-toggle="tab">新款游戏推荐&nbsp;&nbsp;<img src="/images/hot.gif" /></a></li>
                <li <#if type == 2> class="active" </#if> data="2"><a data-toggle="tab">经典老虎机</a></li>
                <li <#if type == 3> class="active" </#if> data="3"><a data-toggle="tab">电动角子老虎</a></li>
                <li  <#if type == 4> class="active" </#if> data="4" class="j-sub-nav dropdown-toggle"><a data-toggle="tab">桌面&扑克游戏</a>
                    <div class="submenu" style="display: none">
                        <a data-toggle="tab" href="#tab-miracle">【奇迹大奖】</a>
                        <a data-toggle="tab" href="#tab-5line">【5-10条线】</a>
                        <a data-toggle="tab" href="#tab-15line">【15-20条线】</a>
                        <a data-toggle="tab" href="#tab-25line">【25+条线】</a>
                        <a data-toggle="tab" href="#tab-otherLine">【多旋转游戏】</a>
                    </div>
                </li>
                <li <#if type == 5> class="active" </#if> data="5"><a data-toggle="tab">街机游戏</a></li>
                <li <#if type == 6> class="active" </#if> data="6" id="jackpot"><a data-toggle="tab">积累游戏jackpot</a></li>
                <li <#if type == 7> class="active" </#if> data="7"><a data-toggle="tab">乐透刮刮乐</a></li>
            </ul>
        </div>
        <div class="gb-main-r tab-bd">
            <ul id="tab-hotGames" class="tab-panel gamelist fade active in">
           		<#list listElectronic as electronic>
                    <li class="game-info">
	                    <div class="game-pic">
	                        <img src="${zy_path}images/ptgame/${electronic.imageFileName}" height="230" style="border-radius:6px;">
	                    </div>
	                    <div class="name"><h4><#if electronic.gameNameCN??> ${electronic.gameNameCN}<#else>${electronic.gameEnName}</#if></h4></div>
	
	                    <div class="game-play">
	                        <a href="javascript:;" target="_blank" class="btn-game play" href="javascript:;" onclick="<#if userInfo??>gotoPtGame('${electronic.gameName}');<#else>alert('请先登录或是注册新会员。');</#if>">进入游戏</a>
	                        <a href="javascript:;" target="_blank" class="btn-game demo" href="javascript:;" onclick="gotoFreePTGame('${electronic.gameName}');">免费试玩</a>
	                    </div>
                	</li>
                 </#list>
            </ul>
        </div>
        <div class="pages" id="pages"></div>
    	<form action="${action_path}electronic/index.html" method="post" style="display:none;" name="gameParam" id="ptPage">
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
<script>
    $(function () {
        getJackpot();
        
        var a =0,b=0;
		setInterval(function(){
			 var text = $("#gotoSa");
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
	 		var text = $("#gotoMg");
			 if (!b)
			 {
				text.css("background-color","#e7a300");
				b = 1;
			 }else{
				text.css("background-color","#1e86cb");
				b = 0;
			 }
	 	},500);
        
        //奖池数据
        function getJackpot() {
            var options = {
                useEasing: true,
                useGrouping: true,
                separator: ',',
                decimal: '.',
                prefix: '',
                suffix: ''
            };
            var demo = new CountUp("j-game-jackpot", 39824338, 99824338, 2, 3000000000, options);
            demo.start();
        }
        
        $("#gotoMg").click(function(){
           window.location.href="${action_path}electronic/agElectronic.html";
        });
        
        $("#gotoSa").click(function(){
           window.location.href="${action_path}electronic/saElecGame.html";
        });
        
         //电子游艺导航
          $(".navlist").find("li").click(function () {
            var check = $(this).attr("data");
            $("#type").val(check);
            $("#pageNo").val(1);
            $("#ptPage").submit();
        });
    
    	//电子游艺导航
         $(".navlist").find("li").click(function () {
	         if($(this).attr('id') =='jackpot'){
		           var a= confirm('领取任何优惠，返水，均不能投注本栏目游戏,确定要打开吗？');
		           if(!a){
		               return;
		           }
	            }
                if ($(this).hasClass("active")) return false;
                var type = $(this).attr("data");
                $("#type").val(type);
                $("#pageNo").val(1);
                $("#ptPage").submit();
        });

            //分页
            var settings = {
                currentPage: ${pageNo}, //当前页
                totalRecoreds: ${total}, //总计录数
                recordsPerPage: 16, //每页X条
                dataText: false, //是否显示统计数据
                startEnd: false, //是否显示第一页最后一页
                prevNext: false, //是否显示上一页下一页
                pageSkip: false, //跳转至X页
                callback: cb, //回调方法
                align: "center" //对齐方式
            };

            function cb() {
                $("#pageNo").val($(this).text());
                $("#ptPage").submit();
            }
            $("#pages").paging(settings);
        });
		var iWidth=window.screen.availWidth; //弹出窗口的宽度;
		var iHeight=window.screen.availHeight-60; //弹出窗口的高度;
		var iTop = (window.screen.availHeight-60-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
        
        function gotoPtGame(gameName){
			 ptOut();
			 window.open('http://slot.qianbaobet.com/electronic/GameHall.html?gameName='+gameName+'&vuid=${vuid}',"slot","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
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
		
		function ptOut(){
			$.ajax({
			    type : "POST",
			    url : '${action_path}electronic/logoutPTGame.do',
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
</script>
</body>
</html>