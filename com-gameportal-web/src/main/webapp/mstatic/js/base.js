/**
 * 公用js文件
 */

function Base() {
	this.init();
};
Base.prototype = {
	init : function() {
		if ($('#download').length > 0) {
			var type = 'show';
			$('#downloadIcon').click(function() {
				if (type === 'show') {
					$('#download').animate({
						right : '-100px'
					}, 250);
					type = 'hide';
				} else {
					$('#download').animate({
						right : '0'
					}, 250);
					type = 'show';
				}
			});
		}
		// 加入收藏
		$('#addFavorites')
				.click(
						function(e) {
							e.preventDefault();
							try {
								window.external.addFavorite(
										window.location.href, '棋牌游戏');
							} catch (e) {
								try {
									window.sidebar.addPanel('棋牌游戏',
											window.location.href, "");
								} catch (e) {
									base
											.alert("抱歉，您当前使用的浏览器暂不支持一键收藏！\n\n请使用Ctrl+D直接添加，或使用浏览器菜单手动收藏！");
								}
							}
						});
		// 设为首页
		$('#setHome')
				.click(
						function(e) {
							e.preventDefault();
							var obj = $(this)[0];
							try {
								obj.style.behavior = 'url(#default#homepage)';
								obj.setHomePage(window.location.href);
							} catch (e) {
								if (window.netscape) {
									try {
										netscape.security.PrivilegeManager
												.enablePrivilege("UniversalXPConnect");
									} catch (e) {
										base
												.alert("抱歉，您当前使用的浏览器暂不支持一键添加！\n\n请通过浏览器菜单，手动将网站设为首页！");
									}
								} else {
									base
											.alert("抱歉，您当前使用的浏览器暂不支持一键添加！\n\n请通过浏览器菜单，手动将网站设为首页！");
								}
							}
						});
	},
	/**
	 * 表单验证valid
	 */
	valid : {
		// 验证纯数字
		isNum : function(str) {
			return /^\d+$/.test(str);
		},
		// 验证QQ
		isQQ : function(str) {
			return /^[1-9]\d{4,9}$/.test(str);
		},
		// 验证手机号
		isMobile : function(str) {
			return /^1\d{10}$/.test(str);
		},
		// 验证固话号或小灵通
		isTel : function(str) {
			return /^0(([1-9]\d)|([3-9]\d{2}))(\d{7}|\d{8})$/.test(str);
		},
		// 验证E-mail
		isEmail : function(str) {
			return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+\.([a-zA-Z])+$/.test(str);
		},
		//验证密码
		isPassWord : function(str){
			return /^[0-9a-zA-Z]{6,12}$/.test(str);
		},
		// 验证大陆身份证号
		isIDcard : function(str) {
			var _tag = (/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/
					.test(str))
					|| (/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/
							.test(str))
			return _tag;
		},
		// 验证中国邮编
		isPcode : function(str) {
			return /^[1-9]\d{5}$/.test(str);
		},
		// 验证姓名
		isNanme : function(str) {
			return /^[\u4e00-\u9fa5]+$/.test(str);
			/**
			var regex = /\d/;
			if (str.search(regex) != -1 || str.length < 2) {
				return false;
			} else {
				return true;
			}
			*/
		},
		// 验证非法特殊字符
		checkInput : function(inputId) {
			var tag = /\||\&|\;|\$|\%|\'|\"|\<|\>|\+|\=|\\/.test($(
					'#' + inputId).val());
			if (tag) {
				alert("禁止输入非法字符！");
				return false;
			} else {
				return true;
			}
		},
		checkInputVal : function(str) {
			var tag = /\||\&|\;|\$|\%|\'|\"|\<|\>|\+|\=|\\/.test(str);
			if (tag) {
				alert("禁止输入非法字符！");
				return false;
			} else {
				return true;
			}
		}
	},
	// 文本框聚焦的时候清空文本
	txtFocus : function(e) {
		if (e.value == e.defaultValue) {
			e.value = '';
		}
	},
	// 文本框失焦的时候设置默认值
	txtBlur : function(e) {
		if (e.value == '') {
			e.value = e.defaultValue;
		}
	},
	// 文本框提示错误
	tip : function(id) {
		var obj = $('#' + id), no = 1, bg = '#fa9760', interval = setInterval(
				function() {
					obj.css('background', bg);
					no++;
					bg = bg === '#fa9760' ? '' : '#fa9760';
					if (no === 7) {
						clearInterval(interval);
					}
				}, 100);
	},
	// fancybox 不绑定按钮直接弹开窗口 id：弹框id type：是否iframe width：窗口宽度 height：窗口高度
	// onClosed：关闭之后的回调函数
	showBox : function(href, type, width, height, onClosed) {
		var boxType = arguments[1] ? arguments[1] : 'inline', boxWidth = arguments[2] ? arguments[2]
				: 'auto', boxHeight = arguments[3] ? arguments[3] : 'auto', callback = arguments[4] ? arguments[4]
				: function() {
				};
		$.fancybox({
			'width' : boxWidth,
			'height' : boxHeight,
			'padding' : 0,
			'transitionIn' : 'none',
			'transitionOut' : 'none',
			'enableEscapeButton' : true,
			'titleShow' : false,
			'href' : href,
			'type' : boxType,
			'onClosed' : callback
		});
	},
	alert : function(content, title, surefun, time, callback) {
		$(document).off('click', '#baseAlert .btnC').on('click',
				'#baseAlert .btnC', surefun ? surefun : $.fancybox.close);
		title = title ? title : '温馨提示';
		if ($('#baseAlert').length <= 0) {
			var boxobj = '<div class="none"><div class="box" id="baseAlert"><h1 class="box_h" id="baseAlert_h">'
					+ title
					+ '</h1><p class="baseBoxCon" id="baseAlert_p">'
					+ content
					+ '</p><div class="box_btn"><a href="javascript:;" title="确 认" class="btnC">确 认</a></div></div></div>';
			$('body').append(boxobj);
		} else {
			$('#baseAlert_h').html(title);
			$('#baseAlert_p').html(content);
		}
		$.fancybox({
			'padding' : 0,
			'transitionIn' : 'none',
			'transitionOut' : 'none',
			'enableEscapeButton' : true,
			'titleShow' : false,
			'href' : '#baseAlert',
			'type' : 'inline',
			'onClosed' : callback || function() {
			}
		});
		if (time)
			setTimeout($.fancybox.close, time);
	},
	/**
	 * @param formId
	 *            需要提交表单的id
	 * @param btnId
	 *            提交按钮的id
	 * @param passFun
	 *            所有验证通过之后的执行函数，可以是ajax方法提交，如果未传该参数则默认为form表单方式提交
	 */
	validform : function(formId, btnId, passFun) {
		$('#' + formId).Validform(
				{
					btnSubmit : "#" + btnId,
					tiptype : function(msg, o, cssctl) {
						if (!o.obj.is("form")) {
							var objtip = o.obj.parents('.formli').find(
									".Validform_checktip");
							cssctl(objtip, o.type);
							objtip.text(msg);

							var infoObj = o.obj.parents('.formli')
									.find(".info");
							if (o.type == 2) {
								infoObj.fadeOut(200);
							} else {
								if (infoObj.is(":visible")) {
									return;
								}
								var left = o.obj.offset().left, top = o.obj
										.offset().top;

								infoObj.css({
									left : left + 170,
									top : top - 45
								}).show().animate({
									top : top - 35
								}, 200);
							}

						}
					},
					beforeSubmit : function(curform) {
						if (passFun) {
							passFun(); // 自定义提交方法
							return false;
						} else {
							$(formId).submit(); // form表单提交的方式提交
						}
					}
				});
	},
	// 客户端二维码弹窗
	appCodeShow : function() {
		if ($('#appCode').length <= 0) {
			var boxobj = '<div class="none"><div class="box" id="appCode"><h1 class="box_h">欢迎您加入AA棋牌娱乐场！</h1><p class="tip">下载AA棋牌客户端，随时随地想玩就玩，扫二维码安装。</p><div class="appCode"><img src="tem/appCode.jpg" alt="客户端二维码" width="165" height="165" /></div><div class="box_btn"><a href="javascript:jQuery.fancybox.close();" title="确 认" class="btnC">确 认</a></div></div></div>';
			$('body').append(boxobj);
		}
		this.showBox('#appCode');
	},
	setCurr : function() {
		nav_a = $('.nav a');
		nav_a.removeClass('curr');
		var url = top.location.href;
		if (url.indexOf("/index.") > 0) {
			nav_a.eq(0).addClass('curr');
		}else if (url.indexOf("/qipaiGame.") > 0) {
			nav_a.eq(1).addClass('curr');
		}else if (url.indexOf("/sportsEvent.") > 0) {
			nav_a.eq(2).addClass('curr');
		}else if (url.indexOf("/liveCasino.") > 0) {
			nav_a.eq(3).addClass('curr');
		}else if (url.indexOf("/electronicGame.") > 0) {
			nav_a.eq(4).addClass('curr');
		}else if (url.indexOf("/lottery.") > 0) {
			nav_a.eq(5).addClass('curr');
		}else if (url.indexOf("/coumonActivity.") > 0) {
			nav_a.eq(6).addClass('curr');
		}else{
			//nav_a.eq(7).addClass('curr');
		}
	}
}

$(document).ready(function() {
	base = new Base();
	//base.setCurr();
});

Function.prototype.method = function(name, func) {
	this.prototype[name] = func;
	return this;
};
if(!String.prototype.trim){ //判断下浏览器是否自带有trim()方法
	String.method('trim', function() {
		return this.replace(/^s+|s+$/g, '');
	});
	String.method('ltrim', function() {
		return this.replace(/^s+/g, '');
	});
	String.method('rtrim', function() {
		return this.replace(/s+$/g, '');
	});
} 