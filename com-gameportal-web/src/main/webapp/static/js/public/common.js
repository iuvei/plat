var website_flag = true; //false：静态站点， true: 动态站点

$(function () {
    //静态站点执行
    website_flag || $.get("/static/tpl/tpl.htm", function (tpl) {
        var $tpl = $(tpl);

        //注入头部
        $("#insert-header").replaceWith($tpl.find("#ui-header"));

        //注入公告
        var $notices = $tpl.find("#ui-notices").replaceAll("#insert-notices");
        $notices.length && init_notices();

        //注入底部
        $("#insert-footer").replaceWith($tpl.find("#ui-footer"));

        //注入侧边栏
        var $sliderBar = $tpl.find("#ui-sliderBar").replaceAll("#insert-sliderBar");
        $sliderBar.length && init_sliderBar();

        //是否已登陆&&退出登陆
        var $header = $("#ui-header"),
            $signs = $header.find(".nav-user .signs"),
            $user = $header.find(".nav-user .user");
        if ($.cookie("login")) {
            $signs.addClass("ui-none").next().removeClass("ui-none");
        }
        $user.find(".exit").click(function () {
            $.cookie("login", null, {
                "path": "/"
            });
        });

        //只在用户中心生效
        if ($("body").data("userIndex") === "") {
            //关联导航状态
            var treeAbbr = window.location.hash.substring(1) || $.cookie("treeAbbr") || "accIndex";
            var $navs = $("#nav").find("a").removeClass("active");
            $navs.filter("[href=\\/user\\/index\\.htm#" + treeAbbr + "]").addClass("active");


            //导航点击事件
            var $nav = $("#nav"),
                $nav_links = $nav.find("a");
            $nav_links.click(function () {
                if ($(this).hasClass("active")) {
                    return false;
                }

                $nav_links.filter(".active").removeClass("active");
                $(this).addClass("active");

                //关联右侧菜单
                var treeAbbr = $(this).attr("href").split("#")[1];
                var $target = $("#menu").find("a").filter("[treeAbbr=" + treeAbbr + "]");
                var _src = $target.attr("href");
                $target.triggerHandler("click");
                $("#subPage").attr("src", _src);
            });
        }

        //只在关于我们生效
        if ($("body").data("userAbout") === "") {
            var $topSources = $("#ui-header").find(".helper").find("a"),
                $bottomSources = $("#nav-footer").find("a"),
                $target = $("#menu").find("a"),
                $goup = $("#goup");

            $topSources.eq(0).click(function () {
                $target.eq(7).click();
            });
            $topSources.eq(2).click(function () {
                $target.eq(5).click();
            });

            $bottomSources.click(function () {
                $target.eq($(this).parent().index() + 1).click();
                $goup.click();
            });
        }

    });

    //动态站点执行
    website_flag && (function () {
    	//头部时间读秒
    	initHeadTime();
    	//高亮导航菜单
    	//hightLightNav();
        
        $("#ui-notices").length && init_notices();

        $("#goup").length && init_sliderBar();
    })();
    
    //高亮导航菜单
    function hightLightNav(){
    	var $header = $("#ui-header");
        var $websiteNavs = $header.find(".nav-website").find("a"),
            _path = window.location.pathname;
        if (_path === "/index.html" || _path === "/index.do") _path = "/";
        var _s = "[href='" + _path + "']";
        $websiteNavs.filter(_s)
            .addClass("color-orange").removeAttr("href");
    }
    
    function initHeadTime(){
    	//头部时间读秒
        var $tc = $("#timerCount");
        timerCount();
        setInterval(timerCount, 1000);
        function timerCount() {
            var d = $.parseDate();
            //var s = "GMT+8&emsp;2015-12-30&emsp;12:12:12";
            var s = "GMT";
            s += d.g > 0 ? "+" + d.g : d.g;
            s += "&emsp;" + d.y + "-" + f(d.m + 1) + "-" + f(d.d) + "&emsp;";
            s += f(d.h) + ":" + f(d.min) + ":" + f(d.s);
            $tc.html(s);

            function f(n) {
                return n > 9 ? n : "0" + n;
            }
        }
    }

    //公告动画效果
    function init_notices() {
        var $notices = $("#ui-notices");

        //状态开关标志
        var $swith_flag = $notices.find(".swithFlag");

        var $links = $notices.find(".wrap a"),
            $details = $notices.find(".detail");
        $links.each(function (index) {
            $(this).click(function (e) {
                e.stopPropagation();
                $notices.find(".detail").eq(index).slideDown().siblings(".detail").slideUp();
                $swith_flag.addClass("up");
            });
        });

        //关闭详情
        //$(document).click(function () {
        //    $notices.find(".detail").slideUp();
        //    $swith_flag.removeClass("up");
        //});
        $notices.find(".detail").click(function (e) {
            e.stopPropagation();
        }).parent().mouseleave(function () {
            $(this).find(".detail").slideUp();
            $swith_flag.removeClass("up");
        });

        //与状态开关进行联动
        $swith_flag.click(function (e) {
            e.stopPropagation();

            if ($(this).hasClass("up")) {
                $notices.find(".detail").slideUp();
                $swith_flag.removeClass("up");
            } else {
                $notices.find("a").eq(0).click();
            }
        });

        //动画
        var $items = $notices.find(".wrap ul");
        var length = $links.length;
        if (length < 2) {
            return false;
        }
        var ci = setInterval(run, 3000);
        $items.hover(function () {
            clearInterval(ci);
        }, function () {
            ci = setInterval(run, 3000);
        });

        function run() {
            $items.animate({
                "margin-top": "-30px"
            }, 500, function () {
                $items.find("li:eq(0)").appendTo($items.css({
                    "margin-top": "0"
                }));
            });
        }
    }

    //侧边栏动画
    function init_sliderBar() {
        var $up = $("#goup");
        var $scroll_box = $("html,body");
        var $sliderBar = $("#ui-sliderBar");
        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                $up.fadeIn(1000);
                $sliderBar.height(302);
            } else {
                $up.fadeOut(1000);
                $sliderBar.height(242);
            }
        });
        $up.click(function () {
            $scroll_box.animate({
                "scrollTop": 0
            }, 400, function () {
                $sliderBar.height(242);
            });
        });
    }
});

//定义加入收藏夹函数
function joinFavorite(sTitle, sURL) {
    try {
        window.external.addFavorite(sURL, sTitle);
    } catch (e) {
        try {
            window.sidebar.addPanel(sTitle, sURL, "");
        } catch (e) {
            alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
}

function forwardTo(param) {
    $.cookie('treeAbbr', param, {
        path: "/"
    });
    var href = window.location.href;
    if (href.indexOf("/manage/index.html") > -1) { //只需局部刷新
        //关联导航状态
        var $navs = $("#nav").find("a").removeClass("active");
        $navs.filter("[treeAbbr=" + param + "]").addClass("active");

        var $menu = $("#menu"),
            $links = $menu.find("a[href]");

        //按需加载子页面
        $menu.find("ul").removeClass("active");
        var _target = $links.removeClass("active")
            .filter("[treeAbbr=" + param + "]").addClass("active");
        var _src = _target.attr("href");
        _target.closest("ul").addClass("active");
        $("#subPage").attr("src", _src);
    } else {
        top.window.location = "/manage/index.html";
    }

}