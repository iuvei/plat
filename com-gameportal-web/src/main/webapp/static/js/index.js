$(function () {
    //banner条
    init_pics("#banner", 11);

    //AD
    var josn_ad = [{
            "img": "/static/images/ad/ad0.jpg",
            "url": "/index.htm#ad0"
            },
        {
            "img": "/static/images/ad/ad1.jpg",
            "url": "/index.htm#ad1"
            },
        {
            "img": "/static/images/ad/ad2.jpg",
            "url": "/index.htm#ad2"
            },
        {
            "img": "/static/images/ad/ad3.jpg",
            "url": "/index.htm#ad3"
            },
        {
            "img": "/static/images/ad/ad4.jpg",
            "url": "/index.htm#ad4"
            },
        {
            "img": "/static/images/ad/ad5.jpg",
            "url": "/index.htm#ad5"
            }];
    init_pics("#ad", 10, josn_ad);

    //中奖喜讯
    init_bonusNews();

    //生成幻灯
    //@target 插入的目标元素
    //@w_li   控制列宽的一半
    //@json   
    function init_pics(target, w_li, json) {
        var default_json = [{
                "img": "/static/images/banner/demo0.jpg",
                "url": "/index.htm"
            },
            {
                "img": "/static/images/banner/demo1.jpg",
                "url": "/index.htm#1"
            },
            {
                "img": "/static/images/banner/demo2.jpg",
                "url": "/index.htm#2"
            },
            {
                "img": "/static/images/banner/demo3.jpg",
                "url": "/index.htm#3"
            },
            {
                "img": "/static/images/banner/demo4.jpg",
                "url": "/index.htm#4"
            },
            {
                "img": "/static/images/banner/demo5.jpg",
                "url": "/index.htm#5"
            }];
        json = json || default_json;

        var length = json.length;

        //插入图片并生成控制按钮
        var _$e = $(),
            $e = $();
        var $bar = $("<ul />").addClass("controlBar").css({
            "width": w_li * length * 2,
            "margin-left": -w_li * length
        });
        for (var i = 0; i < length; i++) {
            _$e = $("<div />").addClass("imgContainer")
                .css({
                    "background-image": "url(" + json[i].img + ")",
                    "z-index": 9 - i
                });
            (function (url) {
                _$e.click(function () {
                    //window.location.href = url;
                    var newWindow = window.open('', '_blank');
                    setTimeout(function () {
                        newWindow.location.href = url;
                    }, 0);
                });
            })(json[i].url)
            $e = $e.add(_$e);
            $bar.append("<li></li>");
        }
        $(target).prepend($bar).prepend($e);
        setTimeout(function () {
            $bar.find("li").eq(0).addClass("current");
        }, 50);

        //动画事件
        var ci = setInterval(bg_run, 6000);
        $bar.find("li").hover(function () {
            clearInterval(ci);
            var index = $(this).index();
            $(this).addClass("current").siblings(".current").removeClass("current");
            $e.eq(index).stop(true, true).fadeIn(1000).siblings(".imgContainer").fadeOut(1000);
        }, function () {
            ci = setInterval(bg_run, 6000);
        });

        function bg_run() {
            var index = $bar.find(".current").index();
            var next = (index + 1) % length;
            $bar.find("li").eq(next).addClass("current").siblings(".current").removeClass("current");
            $e.eq(next).fadeIn(1000).siblings(".imgContainer").fadeOut(1000);
        }

    } //init_pics

    //生成中奖喜讯
    function init_bonusNews(json) {
        var default_json = [{
            "text": "上海玩家 SD***5 在 年年有余 赢得 <b class=\"color-orange\">￥5</b>万元"
            }, {
            "text": "深圳玩家 ii***4 在 角斗士 赢得 <b class=\"color-orange\">￥8.5</b>万元"
            }, {
            "text": "重庆玩家 sd***4 在 角斗士 赢得 <b class=\"color-orange\">￥7</b>万元"
            }, {
            "text": "安徽玩家 df***5 在 角斗士 赢得 <b class=\"color-orange\">￥6.2</b>万元"
            }, {
            "text": "安徽玩家 df***5 在 角斗士 赢得 <b class=\"color-orange\">￥16.2</b>万元"
            }, {
            "text": "安徽玩家 df***5 在 角斗士 赢得 <b class=\"color-orange\">￥26.2</b>万元"
            }];
        json = json || default_json;
        if (json == null || json == "" || json == "undefined") {
            return false;
        }
        var length = json.length;

        //插入文本
        var $e = $(),
            $box = $("#bonusNews"),
            $items = $box.find("ul");

        for (var i = 0; i < length; i++) {
            $e = $e.add($("<li />").html(json[i].text));
        }
        $items.append($e);

        //动画，小于5条时无动画
        if (length < 5) {
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
                "margin-top": "-57px"
            }, 500, function () {
                $items.find("li:eq(0)").appendTo($items.css({
                    "margin-top": "0"
                }));
            });
        }

    } //init_bonusNews
});