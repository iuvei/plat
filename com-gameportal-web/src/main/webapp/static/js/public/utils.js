$.fn.extend({
    /*日历表
        $.fn.calendar({
        myDate: '2015-2-8',//默认为当天
        monthLoad : monthLoad,//收益列表回调函数
        dateClick : dateClick//收益详情回调函数
        });
    */
    calendar: function (settings) {
        settings = $.extend({}, $(this).data("settings"), settings) || {};

        //获取要绘制月份的信息
        var $this = $(this),
            myDate = settings.myDate && $.date(settings.myDate) || new Date(),
            objDate = $.parseDate(myDate),
            monthDates = [new Date(objDate.y, objDate.m, 0).getDate(), new Date(objDate.y, objDate.m + 1, 0).getDate()];
        $this.data("settings", $.extend({}, settings, {
            objDate: objDate
        }));

        //输出绘制信息
        var _days,
            str = "";
        if (!$this.data("$e")) { //绘制基本框架结构
            var $e = $('<div class="head">' +
                '<ul class="years">' +
                '<li><font>2013</font>年</li>' +
                '<li><font>2014</font>年</li>' +
                '<li><font>2015</font>年</li>' +
                '<li><font>2016</font>年</li>' +
                '<li><font>2017</font>年</li>' +
                '<li><font>2018</font>年</li>' +
                '<li><font>2019</font>年</li>' +
                '<li><font>2020</font>年</li>' +
                '<li><font>2021</font>年</li>' +
                '<li><font>2022</font>年</li>' +
                '<li><font>2023</font>年</li>' +
                '<li><font>2024</font>年</li></ul>' +
                '<ul class="months">' +
                '<li><font>1</font>月</li>' +
                '<li><font>2</font>月</li>' +
                '<li><font>3</font>月</li>' +
                '<li><font>4</font>月</li>' +
                '<li><font>5</font>月</li>' +
                '<li><font>6</font>月</li>' +
                '<li><font>7</font>月</li>' +
                '<li><font>8</font>月</li>' +
                '<li><font>9</font>月</li>' +
                '<li><font>10</font>月</li>' +
                '<li><font>11</font>月</li>' +
                '<li><font>12</font>月</li></ul>' +
                '<span class="yearMinus"></span>' +
                '<span class="monthMinus"></span>' +
                '<span class="year"><font>8888</font>年</span>' +
                '<span class="month"><font>88</font>月</span>' +
                '<span class="monthAdd"></span>' +
                '<span class="yearAdd"></span>' +
                '</div>' +
                '<ul class="days">' +
                '<li>日</li>' +
                '<li>一</li>' +
                '<li>二</li>' +
                '<li>三</li>' +
                '<li>四</li>' +
                '<li>五</li>' +
                '<li>六</li>' +
                '</ul>' +
                '<ul class="dates"></ul>');
            $this.data("$e", $e.appendTo($this));

            //关闭年份日期选择窗口
            $this.click(function () {
                $(this).find(".years, .months").hide();
            });

            //年份减1
            $this.find(".yearMinus").click(function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date(objDate.y - 1, objDate.m, 1)
                });
            });

            //月份减1
            $this.find(".monthMinus").click(function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date(objDate.y, objDate.m - 1, 1)
                });
            });

            //打开年份选择窗口
            $this.find(".year").click(function (e) {
                e.stopPropagation();
                $this.find(".years").show().next(".months").hide();
            });

            //打开月份选择窗口
            $this.find(".month").click(function (e) {
                e.stopPropagation();
                $this.find(".months").show().prev(".years").hide();
            });

            //选择年份
            $this.find(".years").delegate("li", "click", function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date($(this).find("font").text(), objDate.m, 1)
                });
            });

            //选择月份
            $this.find(".months").delegate("li", "click", function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date(objDate.y, parseInt($(this).find("font").text()) - 1, 1)
                });
            });

            //月份加1
            $this.find(".monthAdd").click(function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date(objDate.y, objDate.m + 1, 1)
                });
            });

            //年份加1
            $this.find(".yearAdd").click(function () {
                var objDate = $this.data("settings").objDate;
                $this.calendar({
                    myDate: new Date(objDate.y + 1, objDate.m, 1)
                });
            });

            //刷新右侧收益详情
            $this.find(".dates").delegate("li", "click", function () {
                if (!$(this).hasClass("gray") && !$(this).hasClass("current")) {
                    $(this).addClass("current").siblings(".current").removeClass("current");
                    if (typeof settings.dateClick === "function") settings.dateClick.call($this.find(".current"));
                }
            });
        } //绘制基本框架结构
        $this.find(".year").find("font").html(objDate.y);
        $this.find(".month").find("font").html(objDate.m + 1);

        //月初日期补齐天数
        _days = new Date(objDate.y, objDate.m, 1).getDay();
        while (_days > 0) {
            str += "<li class=\"gray\">" + (monthDates[0] - _days + 1) + "</li>";
            _days--;
        }

        //当月天数
        var dataMonth = objDate.y + "-" + (objDate.m + 1) + "-";
        for (var i = 1; i <= monthDates[1]; i++) {
            if (i === objDate.d) {
                str += "<li data-date=\"" + dataMonth + i + "\" class=\"current\">" + i + "</li>";
            } else {
                str += "<li data-date=\"" + dataMonth + i + "\">" + i + "</li>";
            }
        }

        //月未日期补齐天数
        _days = 7 - new Date(objDate.y, objDate.m + 1, 1).getDay();
        if (_days < 7) {
            for (var i = 1; i <= _days; i++) {
                str += "<li class=\"gray\">" + i + "</li>";
            }
        }

        $this.find(".dates").html(str);

        //设置今天的背景色
        var today = new Date();
        var strToday = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();
        $this.find(".dates").find("[data-date=" + strToday + "]").addClass("today").attr("title", "今天");

        if (typeof settings.monthLoad === "function") settings.monthLoad.call(objDate);
        if (typeof settings.dateClick === "function") settings.dateClick.call($this.find(".current"));

        return $this;
    }, //calendar

    /*可拖拽层
        $.fn.drag(selector);
        selector为拖拽位置，为空则层的任意点都可以拖拽
    */
    drag: function (selector) {
        var $win = $(top.window),
            $doc = $(top.document),
            $drag = $(this).eq(0),
            $dragElem = $();

        if ($(this).find(selector).length === 0) {
            $dragElem = $drag;
        } else {
            $dragElem = $drag.find(selector).eq(0);
        }
        $drag.css("position") === "static" && $drag.css({
            "position": "fixed"
        });

        $dragElem.mousedown(function (e) {
            var _x = e.clientX,
                _y = e.clientY,
                _w_drag = $(this).innerWidth(),
                _h_drag = $(this).innerHeight(),
                _w_window = $win.width(),
                _h_window = $win.height(),
                _scrollTop = $win.scrollTop(),
                _scrollLeft = $win.scrollLeft(),
                _top,
                _left;
            var $clone = $("<div />").addClass("clone").css({
                width: $drag.width(),
                height: $drag.height()
            }).prependTo($drag);

            //移动
            $doc.mousemove(function (e) {
                var _diffX = e.clientX - _x,
                    _diffY = e.clientY - _y,
                    _pos = $drag.position();

                _x = e.clientX;
                _y = e.clientY;
                _top = _pos.top - _scrollTop + _diffY;
                _left = _pos.left - _scrollLeft + _diffX;

                //边界检测
                if (_top < 0) {
                    _top = 0;
                } else if (_left < 0) {
                    _left = 0;
                } else if (_top + _h_drag > _h_window) {
                    _top = _h_window - _h_drag;
                } else if (_left + _w_drag > _w_window) {
                    _left = _w_window - _w_drag;
                }

                $drag.css({
                    "top": _top,
                    "left": _left
                });
                return false;
            }).one("mouseup mouseleave", function () {
                $clone.remove();
                $(this).unbind("mousemove");
            });

            $drag.find("iframe").contents().one("mousemove", function () {
                $doc.unbind("mousemove");
            });

            return false;
        }); //$dragElem.mousedown

        return $drag;
    }, //drag


    /*分页控件
        var settings={
            totalRecoreds:40,//总计录数
            recordsPerPage:5,//每页X条
            dataText:true,//是否显示统计数据
            startEnd:false,//是否显示第一页最后一页
            prevNext:true,//是否显示上一页下一页
            pageSkip:false//跳转至X页
        };
        $("#pages").pages(settings);
    */
    paging: function (settings) {
        var defaultSettings = {
            currentPage: 1, //当前页
            totalRecoreds: 100, //总计录数
            recordsPerPpage: 10, //每页X条
            width: 35, //页码宽
            maxPages: 4, //最多显示页码数量
            dataText: true, //是否显示统计数据
            startEnd: true, //是否显示第一页最后一页
            prevNext: true, //是否显示上一页下一页
            pageSkip: true, //跳转至X页
            callback: null, //异步刷新表格
            align: "right"
        };
        var settings = $.extend({}, defaultSettings, $(this).data("settings"), settings);
        $(this).data("settings", settings);

        var totalPages = Math.floor((settings.totalRecoreds - 1) / settings.recordsPerPage) + 1;
        var $e = $();

        //生成页码
        var pagesNums = settings.maxPages > totalPages ? totalPages : settings.maxPages,
            $pagesWarpper = $("<div />").css({
                "width": settings.width * (pagesNums - 1) + 25
            }),
            $lis = $(),
            _$li = $(),
            _$current = $();
        for (var i = 1; i <= totalPages; i++) {
            _$li = $("<li />").addClass("ui-radius ui-transition").text(i).click(function () {
                if ($(this).hasClass("current")) {
                    return false;
                }

                $(this).addClass("current").siblings(".current").removeClass("current");
                setPosition(this);
                typeof settings.callback === 'function' && settings.callback.call(this);
            });
            if (i === parseInt(settings.currentPage)) {
                _$current = _$li.addClass("current");
            }
            $lis = $lis.add(_$li);
        }
        $e = $pagesWarpper.append($e.add($("<ul />").css({
            "width": settings.width * totalPages
        }).append($lis)));

        //生成上一页下一页功能按钮
        if (settings.prevNext) {
            $e = $("<span />").addClass("goPrev ui-radius ui-transition ui-hand").click(function () {
                    $e.find(".current").prev().click();
                }).add($e)
                .add($("<span />").addClass("goNext ui-radius ui-transition ui-hand").click(function () {
                    $e.find(".current").next().click();
                }));
        }

        //生成首页尾页功能按钮
        if (settings.startEnd) {
            $e = $("<span />").addClass("goFirst ui-radius ui-transition ui-hand").click(function () {
                    $e.find("li").eq(0).click();
                }).add($e)
                .add($("<span />").addClass("goLast ui-radius ui-transition ui-hand").click(function () {
                    $e.find("li").eq(-1).click();
                }));
        }

        //生成统计数据
        if (settings.dataText) {
            var str = "第<em class=\"currentPage\">" + settings.currentPage + "</em>页/共<em class=\"totalPges\">" + totalPages + "</em>页 总<em>" + settings.totalRecoreds + "<em>条记录";
            $e = $("<label />").html(str).add($e);
        }

        //生成跳转按钮
        if (settings.pageSkip) {
            $e = $e.add($("<input type=input />").addClass("ui-radius").keyup(function (e) {
                if (e.which === 13) {
                    $(this).next().click();
                }
            })).add($("<input type=button />").addClass("ui-radius ui-transition").val('确定').click(function () {
                var page = parseInt($(this).prev().val());
                if (page > 0 && page <= totalPages) {
                    $e.find("li").eq(page - 1).click();
                } else if (page > totalPages) {
                    $e.find("li").eq(-1).click();
                }
            }));
        }

        //注入当前元素
        $(this).removeAttr("style").addClass("ui-paging").append($e);
        $(this).find(".currentPage").width($(this).find(".totalPages").width());
        $(this).css({
                width: function () {
                    return $(this).outerWidth()
                }
            })
            .find("li").eq(settings.currentPage - 1).click();
        setPosition(_$current, 1);

        if (settings.align == "left") {
            $(this).css({
                float: "left"
            });
        } else if (settings.align == "center") {
            $(this).css({
                float: "none"
            });
        } else {
            $(this).css({
                float: "right"
            });
        }

        function setPosition(elem, duration) {
            var pos = parseInt($(elem).text()) || 0,
                middle = Math.round((pagesNums + 1) / 2),
                ml = 0;

            if (middle >= pos) {
                ml = 0;
            } else if (pos - middle > totalPages - pagesNums) {
                ml = (pagesNums - totalPages) * settings.width;
            } else {
                ml = (middle - pos) * settings.width;
            }
            $e.find(".currentPage").text(pos).end()
                .find("ul").stop(true, true).animate({
                    "margin-left": ml
                }, duration || 600);
        }

        return $e;
    }, //paging

    /*tips泡泡提示框
        bugs:
        1.IE8-无圆角
        2.IE9-无动画
        3.页面出现横向滚动条时冒泡提示框定位异常

        $.fn.tips([a,b,c],true);					//初始化
        $(selector).trigger("close"); 				//关
        $(selector).trigger("open",["aaaaaaa"]); 	//开启

        1、数组元素默认为false
        2、数组元素为fasle或函数元素返回false时对应提示框不显示
        3、数组元素为true或函数元素返回true时对应提示框显示selectoer的placehoder值
        4、数组元素为字符串或函数元素返回字符串时对应提示框显示该字符串
        5、其它情况转字符串处理
    */
    tips: function (arr) {
            var isDemo = false;
            if (typeof arr === "boolean") {
                isDemo = arr;
                arr = [];
            }
            arr = arr || [];
            var $es = $();

            $(this).each(function (index) {
                if ($(this).is(":hidden") || $(this).data("$e")) return true;

                var $e = $("<div />").addClass("ui-tips ui-transitionLazy").css({
                        "opacity": 0,
                        "z-index": -1
                    })
                    .append($("<span />").addClass("ui-arrow"))
                    .append($("<span />").addClass("ui-content ui-radius"));
                var _fixX = $(this).attr("fix-x") ? parseInt($(this).attr("fix-x")) : 0;
                var _fixY = $(this).attr("fix-y") ? parseInt($(this).attr("fix-y")) : 0;
                var text = "";

                $e.insertAfter(this);
                var marginLeft = $(this).position().left + $(this).outerWidth(true) - $e.position().left + _fixX + 11,
                    marginTop = $(this).position().top - $e.position().top + _fixY;
                $e.css({
                    "margin-left": marginLeft + 10,
                    "margin-top": marginTop
                });

                $(this).blur(function () {
                    if (isDemo) {
                        text = true;
                    } else if (typeof arr[index] === "string") {
                        text = arr[index];
                    } else if (typeof arr[index] === "function") {
                        text = arr[index].apply(this);
                    } else {
                        text = false;
                    }

                    //获取值为false时直接返回
                    if (typeof text === "boolean" && text == false) {
                        $(this).trigger("close");
                        return false;
                    }

                    $(this).trigger("open", [text]);
                }).on("open", function (e, text) {
                    if (typeof text === "boolean" && text == true) {
                        text = this.placeholder || "请输入！";
                    }

                    $(this).addClass("error");
                    var marginLeft = $(this).position().left + $(this).outerWidth(true) - $e.position().left + _fixX + 11,
                        marginLop = $(this).position().top - $e.position().top + _fixY;
                    $e.css({
                        "margin-left": marginLeft - 10,
                        "margin-top": marginTop,
                        "opacity": 1,
                        "z-index": 9999
                    }).find(".ui-content").html(text.toString());
                }).on("focus close", function () {
                    $e.css({
                        "margin-left": "+=20px",
                        "opacity": 0,
                        "z-index": -1
                    });
                    $(this).removeClass("error");
                }).data("$e", $e);

                $es = $es.add($e);
            });

            return $es;
        } //tips
});

$.extend({
    /*
        @msg:要显示的信息
        @cb_ok:第一个按钮的回调
        @btn_ok:第一个按钮的文本
        demo:
        $.remind("操作成功！",function(){console.log("ok")});
    */
    remind: function (msg, cb_ok, btn_ok) {
        var WIN = top.window, //放到顶层窗口
            $remind = $(WIN.document.body).find(".ui-remind").prev(".ui-mask").andSelf();

        $remind.remove();
        $remind = $("<div />").attr("id", "remind").addClass("ui-remind bg-white ui-radius").css({
                "z-index": 1101
            })
            .append("<h1 class=\"tilte size-big bg-orange color-white ui-song\">提示</h1>")
            .append($("<p />").addClass("text color-dark size-big ui-mt20"))
            .append($("<a />").attr("href", "javascript:void(0);")
                .addClass("btn btn-ok ui-block color-white bg-orange bg-brownHover ui-radius ui-transition").text(btn_ok || "确定"));
        $(WIN.document.body).append($("<div />").addClass("ui-mask").css({
            "z-index": 1100
        }).add($remind));
        $remind.find('.text').text(msg);
        $remind.css({
            marginLeft: -parseInt($remind.outerWidth()) / 2,
            marginTop: -parseInt($remind.outerHeight()) / 2
        }).prev(".ui-mask").css({"opacity":0,"background-color":"#000"}).fadeTo(600,.5);

        //关闭确认框
        $remind.find(".btn").click(function () {
            $remind.fadeOut().prev(".ui-mask").fadeOut(function () {
                $remind.add(this).remove();
                typeof cb_ok === "function" && cb_ok();
            });
        });

        return $remind;
    }, //remind

    /*
	    @msg:要显示的信息
	    @cb_ok:第一个按钮的回调
	    @cb_cancel:第二个取消按钮的回调
	    @btn_ok:第一个按钮的文本
	    @btn_cancel:第二个按钮的文本
	    @flag:cb_ok是否淡出后调用，默认淡出后调用，为true时淡出前调用
	    demo:
	    $.confirm("你确认要删除吗?",function(){console.log("ok")},function(){console.log("cancel")});
	*/
    confirm: function (msg, cb_ok, cb_cancel, btn_ok, btn_cancel, flag) {
        var WIN = top.window, //放到顶层窗口
            $confirm = $(WIN.document.body).find(".ui-confirm").prev(".ui-mask").andSelf();

        $confirm.remove();
        $confirm = $("<div />").attr("id", "confirm").addClass("ui-confirm bg-white ui-radius").css({
                "z-index": 1101
            })
            .append("<h1 class=\"tilte size-big bg-orange color-white ui-song\">提示<span class=\"close fr color-orange color-brown-hover ui-alignCenter bg-white ui-yahei ui-radius ui-hand\">&times</span></h1>")
            .append($("<p />").addClass("text color-dark size-big ui-mt20"))
            .append($("<a />").attr("href", "javascript:void(0);")
                .addClass("btn btn-cancel fl color-white bg-orange bg-brownHover ui-radius ui-transition").text(btn_cancel || "取消"))
            .append($("<a />").attr("href", "javascript:void(0);")
                .addClass("btn btn-ok fr color-white bg-orange bg-brownHover ui-radius ui-transition").text(btn_ok || "确定"));
        $(WIN.document.body).append($("<div />").addClass("ui-mask").css({
            "z-index": 1100
        }).add($confirm));
        $confirm.find('.text').text(msg);
        $confirm.css({
            marginLeft: -parseInt($confirm.outerWidth()) / 2,
            marginTop: -parseInt($confirm.outerHeight()) / 2
        }).prev(".ui-mask").css({
            "opacity": 0,
            "background-color": "#000"
        }).fadeTo(600, .5);

        //关闭确认框
        $confirm.find(".close, .btn").click(function () {
            var $this = $(this);
            $confirm.fadeOut();
            $confirm.prev(".ui-mask").fadeOut(function () {
                $confirm.add(this).remove();

                if ($this.hasClass("btn-ok")&&!flag) {
                    typeof cb_ok === "function" && cb_ok();
                } else if ($this.hasClass("btn-cancel")) {
                    typeof cb_cancel === "function" && cb_cancel();
                }
            });
            if ($this.hasClass("btn-ok")&&flag) {
                typeof cb_ok === "function" && cb_ok();
            }
        });

        return $confirm;
    }, //confirm

    /** 
        新建一个cookie 包括有效期 路径 域名等
        example $.cookie('name', 'lance', {expires: 7, path: '/', domain: 'baidu.com', secure: true});
        删除一个cookie
        example $.cookie('name', null);
        取一个cookie(url)值给myvar
        var url= $.cookie('name');
    **/
    cookie: function (name, value, settings) {
        if (typeof value != 'undefined') { //写cookie
            settings = settings || {};
            if (value === null) {
                value = '';
                settings.expires = -1;
            }

            var expires = '';
            if (settings.expires && (typeof settings.expires == 'number' || settings.expires.toUTCString)) {
                var date;
                if (typeof settings.expires == 'number') {
                    date = new Date();
                    date.setTime(date.getTime() + (settings.expires * 1000));
                } else {
                    date = settings.expires;
                }
                expires = '; expires=' + date.toUTCString();
            }

            var path = settings.path ? '; path=' + settings.path : '';
            var domain = settings.domain ? '; domain=' + settings.domain : '';
            var secure = settings.secure ? '; secure' : '';
            document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
        } else { //读cookie
            var cookieValue = null;
            if (document.cookie && document.cookie != '') {
                var cookies = document.cookie.split(';');
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = jQuery.trim(cookies[i]);
                    if (cookie.substring(0, name.length + 1) == (name + '=')) {
                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                        break;
                    }
                }
            }
            return cookieValue;
        }
    }, //cookie

    //将字符串解析为日期
    date: function (date) {
        if (typeof date === "string") {
            var arrDate = date.split("-");
            date = new Date(parseInt(arrDate[0]), parseInt(arrDate[1]) - 1, parseInt(arrDate[2]));
        }
        return date;
    }, //date

    ddl: function (func) {
        var $ddl = $(".ui-ddl");
        $(document).click(function () {
            $ddl.find("ul").hide();
        });
        $ddl.click(function (e) {
            e.stopPropagation();
            $ddl.find("ul").hide();
            $(this).find("ul").show();

            //增加title属性
            var $li = $(this).find("li").eq(0);
            if (!$li.data("format")) {
                var w_li = $li.width(),
                    fontSize = parseInt($li.css("font-size")),
                    maxLen = Math.floor(w_li / fontSize);
                $(this).find("li").each(function () {
                    if ($(this).text().length > maxLen) {
                        $(this).attr("title", $(this).text());
                    };
                });
                $li.data("format", true);
            }
        }).find("ul").each(function () {
            $(this).width($(this).closest(".ui-ddl").innerWidth());
        }).delegate("li", "click", function (e) {
            e.stopPropagation();
            $(this).closest(".ui-ddl").find("input").val($(this).text());
            $(this).closest(".ui-ddl").find(".input").text($(this).text());
            $(this).closest(".ui-ddl").find(".input").attr("data", $(this).attr("data"));
            $(this).closest("ul").hide();
            typeof func === 'function' && func.call(this);
        });
    }, //ddl

    /*
        @msg:要显示的信息
        @type:图标类型，0：正在加载，1：成功，2：失败、错误，3：警告
        @cb:回调方法
        @autoClose:是否自动关闭
        z-index:1100+;
        demo:
        3秒后自动关
        $.msg("我可以3秒后自动关",1,"",true);
        点击背景关闭
        $.msg("我需要点击确定关",1);
        立即关闭
        $.msg("close");
    */
    msg: function (msg, type, cb, autoClose) {
        var WIN = top.window, //放到顶层窗口
            type = type || 0,
            msg = msg || "正在加载中,请稍候...",
            typeArr = ["ui-msgLoading", "ui-msgSuccess", "ui-msgError", "ui-msgWarning"],
            $MSG = $(WIN.document.body).find(".ui-msg"),
            ct;

        if (msg === 'close') {
            close();
            return false;
        }

        //移除旧的，重新生成一份新的
        $MSG.prev(".ui-mask").andSelf().remove();
        $MSG = $("<div />").attr("id", "ui-msg").addClass("ui-msg bg-orange ui-radius").css({
                "z-index": 1101
            })
            .append($("<span />").addClass("icon fmsg " + typeArr[type]))
            .append($("<span />").addClass("text fmsg").text(msg));
        $(WIN.document.body).append($("<div />").addClass("ui-mask").css({
            "z-index": 1100
        }).add($MSG));

        if (type === 0) { //加载提示，不响应用户操作
            $MSG.siblings(".ui-mask").css({
                opacity: 0.1
            });
            setPos();
            ct = setTimeout(function () {
                $MSG.addClass("ui-transitionLazy").find('.text').text('您的请求仍需一点时间，请耐心等候...');
                setPos();
                ct = setTimeout(function () {
                    $MSG.fadeOut(3000, function () {
                        close();
                    }).find('.text').text("操作超时，请重试!", 3);
                    setPos();
                }, 15000);
            }, 3000);
        } else { //其它提示，点击背景关闭
            setPos();
            if (autoClose) {
                ct = setTimeout(function () {
                    $MSG.prev(".ui-mask").click();
                }, 3000);
            }

            $MSG.prev(".ui-mask").click(function () {
                close();
            }).addClass("ui-hand").css({"opacity":0,"background-color":"#000"}).fadeTo(600,.5);
        }

        //关闭提示框
        function close() {
            clearTimeout(ct);
            $MSG.prev(".ui-mask").fadeOut();
            $MSG.fadeOut(function () {
                $MSG.prev(".ui-mask").andSelf().remove();
                typeof cb === "function" && cb();
            });
        }

        function setPos() {
            $MSG.css({
                marginLeft: -parseInt($MSG.outerWidth()) / 2,
                marginTop: -parseInt($MSG.outerHeight()) / 2
            });
        }

        return $MSG;
    }, //msg

    parseDate: function (sDate) {
            var myDate;
            if (typeof sDate === "string") {
                var arrDate = sDate.split("-");
                var myDate = sDate && new Date(arrDate[0], arrDate[1] - 1, arrDate[2]) || new Date();
            } else if (typeof sDate === "object") {
                myDate = new Date(sDate);
            } else {
                myDate = new Date();
            }

            return {
                g: -myDate.getTimezoneOffset() / 60,
                y: myDate.getFullYear(),
                m: myDate.getMonth(),
                d: myDate.getDate(),
                w: myDate.getDay(),
                h: myDate.getHours(),
                min: myDate.getMinutes(),
                s: myDate.getSeconds()
            }
        } //parseDate
});