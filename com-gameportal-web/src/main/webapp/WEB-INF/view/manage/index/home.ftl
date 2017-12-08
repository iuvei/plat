<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    ${meta}
    <title>${title}-用户中心</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css">
    <link type="text/css" rel="stylesheet" href="/static/css/user/index.css?v=2">

    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css">
    <link type="text/css" rel="stylesheet" href="/static/css/user/capital/deposit.css">

</head>
<body class="bg-gray">
<div class="wrapper ui-header" id="ui-header">
    <!--头部-->
    <#include "${action_path}common/top.ftl">
    <!--导航-->
    <#include "${action_path}common/header.ftl">
</div>
<a name="#5F"></a> 
<div class="user-wrapper main">
    <div class="menu pa" id="menu">
        <div class="menuTree">
            <ul class="active">
                <li>
                    <h2 class="size-big"><i class="iconfont icon-user-2"></i> 账户管理</h2>
                </li>
                <li>
                    <a class="" href="/manage/accountCenter.html" target="subPage" treeabbr="accIndex">个人中心</a>
                </li>
                <li>
                    <a class="" href="/manage/userdata.html" target="subPage" treeabbr="accPersonal">修改资料</a>
                </li>
                <li>
                    <a class="" href="/manage/user/changePwd.html" target="subPage" treeabbr="accChangePwd">修改密码</a>
                </li>
                <li>
                    <a class="" href="/manage/user/userBindBank.html" target="subPage" treeabbr="accBinding">绑定银行卡</a>
                </li>
                <li>
                    <a class="" href="/manage/user/verfityInfo.html" target="subPage" treeabbr="verfityInfo">手机/邮箱验证&nbsp;<img src="/images/hot.gif" /></a>
                </li>
            </ul>

            <ul>
                <li>
                    <h2 class="size-big"><i class="iconfont icon-up"></i>钱包管理</h2>
                </li>
                <li>
                    <a class="color-white active" href="/manage/capital/userDeposit.html" target="subPage" treeabbr="capDeposit">我要存款</a>
                </li>
                <li>
                    <a href="/manage/capital/userCaseout.html" target="subPage" treeabbr="capWithdraw">我要提款</a>
                </li>
                <li>
                    <a href="/manage/capital/userTransfer.html" target="subPage" treeabbr="capTransfer">我要转账</a>
                </li>
                <li>
                    <a href="/manage/reportQuery/outinRecord.html" target="subPage" treeabbr="cancelDW">撤销提款 <img src="/images/hot.gif" /></a>
                </li>
                <#--
                <li>
                    <a href="/manage/capital/loanmoney.html" target="subPage" treeabbr="loan">我要贷款 <i class="iconfont icon-hot"></i></a>
                </li>
                -->
            </ul>

            <ul>
                <li>
                    <h2 class="size-big"><i class="iconfont icon-form-2"></i>优惠领取</h2>
                </li>
                <li>
                   <a href="/manage/firstWithdrawPrize.html" target="subPage" treeabbr="proFDP">8-88元彩金 <img src="/images/hot.gif" /></a>
                </li>
                <li>
                    <a href="/manage/signIn.html" target="subPage" treeAbbr="proSignIn">钱宝今日签到<img src="/images/hot.gif" /></a>
                </li>
                <li>
                    <a href="/manage/gotoRedReward.html" target="subPage" treeabbr="proHB">钱宝抢红包 <img src="/images/hot.gif" /></a>
                </li>
                <li>
                    <a href="/manage/points.html" target="subPage" treeAbbr="points">钱宝积分兑换<img src="/images/hot.gif" /></a>
                </li>
                <li>
                    <a href="/manage/1/getHelpMoney.html" target="subPage" treeabbr="proSM">自助救援金 <img src="/images/hot.gif" /></a>
                </li>
                <li>
                    <a href="javascript:;" id="rateback">自动返水</i></a>
                </li>
            </ul>

            <ul>
                <li>
                    <h2 class="size-big"><i class="iconfont icon-bell"></i>报表查询</h2>
                </li>
                <li>
                    <a href="/manage/reportQuery/betRecord.html" target="subPage" treeabbr="selState">自助查流水</a>
                </li>
				
                <li>
                    <a href="/manage/reportQuery/outinRecord.html" target="subPage" treeabbr="rptEAE">存提款明细</a>
                </li>
                <li>
                    <a href="/manage/reportQuery/transferRecord.html" target="subPage" treeabbr="rptTransfer">转账记录查询</a>
                </li>
            </ul>
        </div>
    </div>

    <iframe class="subPage" id="subPage" name="subPage" frameborder="0"></iframe>
</div>
<!--客服-->
<#include "${action_path}common/rightSlider.ftl">
<!--底部-->
<#include "${action_path}common/footer.ftl">
</body>
</html>
<script src="${action_path}js/jquery.cookie.js"></script>
<script src="${action_path}static/js/public/utils.js"></script>
<script src="${action_path}static/js/public/common.js"></script>
<script>
    $(function () {
        var $menu = $("#menu"),
        $links = $menu.find("a[href]");
        
        //按需加载子页面
        var _abbr = $.cookie("treeAbbr") || "accIndex";
        var _target = $links.removeClass("active")
            .filter("[treeAbbr=" + _abbr + "]").addClass("active");
        var _src = _target.attr("href");
        _target.closest("ul").addClass("active");
        $("#subPage").attr("src", _src);
        
        //关联导航状态
        var $navs = $("#nav").find("a").removeClass("active");
        $navs.filter("[treeAbbr=" + _abbr + "]").addClass("active");
            
        //菜单点击事件
        $links.click(function () {
            if ($(this).hasClass("active")) {
                return false;
            }

            $links.filter(".active").removeClass("active");
            $menu.find("ul").removeClass("active");
            $(this).addClass("active").closest("ul").addClass("active");
            var treeAbbr = $(this).attr("treeAbbr");
            $.cookie('treeAbbr', treeAbbr, {
                path: "/"
            });
             pageScroll();
            //关联导航状态
            var $navs = $("#nav").find("a").removeClass("active");
            $navs.filter("[treeAbbr=" + treeAbbr + "]").addClass("active");
        });
        
        $("#rateback").click(function(){
        	$.msg('游戏返水将于每日下午2点自动派发到您的钱包账户,无需人工申请!',3);
        });
    });
    
    function pageScroll(){
	    //把内容滚动指定的像素数（第一个参数是向右滚动的像素数，第二个参数是向下滚动的像素数）
	    window.scrollBy(0,-100);
	    //延时递归调用，模拟滚动向上效果
	    scrolldelay = setTimeout('pageScroll()',100);
	    //获取scrollTop值，声明了DTD的标准网页取document.documentElement.scrollTop，否则取document.body.scrollTop；因为二者只有一个会生效，另一个就恒为0，所以取和值可以得到网页的真正的scrollTop值
	    var sTop=document.documentElement.scrollTop+document.body.scrollTop;
	    //判断当页面到达顶部，取消延时代码（否则页面滚动到顶部会无法再向下正常浏览页面）
	    if(sTop==0) clearTimeout(scrolldelay);
	}
</script>