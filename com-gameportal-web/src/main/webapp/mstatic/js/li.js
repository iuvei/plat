//rem自适应效果   js
document.getElementsByTagName('html')[0].style.fontSize = $(window).width()*100/750+'px';
$(window).resize(function(){
	document.getElementsByTagName('html')[0].style.fontSize = $(window).width()*100/750+'px';
});

//返回页面顶部
(function() {
    var $backToTopTxt = " ", $backToTopEle = $('<div class="backToTop"></div>').appendTo($("body"))
        .text($backToTopTxt).attr("title", $backToTopTxt).click(function() {
            $("html, body").animate({ scrollTop: 0 }, 120);
    }), $backToTopFun = function() {
        var st = $(document).scrollTop(), winh = $(window).height();
        (st > 0)? $backToTopEle.show(): $backToTopEle.hide();
        //IE6下的定位
        if (!window.XMLHttpRequest) {
            $backToTopEle.css("top", st + winh - 166);
        }
    };
    $(window).bind("scroll", $backToTopFun);
    $(function() { $backToTopFun(); });
})();
















