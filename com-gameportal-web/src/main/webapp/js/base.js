/* ========================================================================
 * Bootstrap: transition.js v3.3.5
 * http://getbootstrap.com/javascript/#transitions
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
	'use strict';

	// CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
	// ============================================================

	function transitionEnd() {
		var el = document.createElement('bootstrap')

		var transEndEventNames = {
			WebkitTransition : 'webkitTransitionEnd',
			MozTransition    : 'transitionend',
			OTransition      : 'oTransitionEnd otransitionend',
			transition       : 'transitionend'
		}

		for (var name in transEndEventNames) {
			if (el.style[name] !== undefined) {
				return { end: transEndEventNames[name] }
			}
		}

		return false // explicit for ie8 (  ._.)
	}

	// http://blog.alexmaccaw.com/css-transitions
	$.fn.emulateTransitionEnd = function (duration) {
		var called = false
		var $el = this
		$(this).one('bsTransitionEnd', function () { called = true })
		var callback = function () { if (!called) $($el).trigger($.support.transition.end) }
		setTimeout(callback, duration)
		return this
	}

	$(function () {
		$.support.transition = transitionEnd()

		if (!$.support.transition) return

		$.event.special.bsTransitionEnd = {
			bindType: $.support.transition.end,
			delegateType: $.support.transition.end,
			handle: function (e) {
				if ($(e.target).is(this)) return e.handleObj.handler.apply(this, arguments)
			}
		}
	})

}(jQuery);


/* ========================================================================
 * Bootstrap: tab.js v3.3.5
 * http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
	'use strict';

	// TAB CLASS DEFINITION
	// ====================

	var Tab = function (element) {
		// jscs:disable requireDollarBeforejQueryAssignment
		this.element = $(element)
		// jscs:enable requireDollarBeforejQueryAssignment
	}

	Tab.VERSION = '3.3.5'

	Tab.TRANSITION_DURATION = 150

	Tab.prototype.show = function () {
		var $this    = this.element
		var $ul      = $this.closest('ul:not(.dropdown-menu)')
		var selector = $this.data('target')

		if (!selector) {
			selector = $this.attr('href')
			selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
		}

		if ($this.parent('li').hasClass('active')) return

		var $previous = $ul.find('.active:last a')
		var hideEvent = $.Event('hide.bs.tab', {
			relatedTarget: $this[0]
		})
		var showEvent = $.Event('show.bs.tab', {
			relatedTarget: $previous[0]
		})

		$previous.trigger(hideEvent)
		$this.trigger(showEvent)

		if (showEvent.isDefaultPrevented() || hideEvent.isDefaultPrevented()) return

		var $target = $(selector)

		this.activate($this.closest('li'), $ul)
		this.activate($target, $target.parent(), function () {
			$previous.trigger({
				type: 'hidden.bs.tab',
				relatedTarget: $this[0]
			})
			$this.trigger({
				type: 'shown.bs.tab',
				relatedTarget: $previous[0]
			})
		})
	}

	Tab.prototype.activate = function (element, container, callback) {
		var $active    = container.find('> .active')
		var transition = callback
			&& $.support.transition
			&& ($active.length && $active.hasClass('fade') || !!container.find('> .fade').length)

		function next() {
			$active
				.removeClass('active')
				.find('> .dropdown-menu > .active')
				.removeClass('active')
				.end()
				.find('[data-toggle="tab"]')
				.attr('aria-expanded', false)

			element
				.addClass('active')
				.find('[data-toggle="tab"]')
				.attr('aria-expanded', true)

			if (transition) {
				element[0].offsetWidth // reflow for transition
				element.addClass('in')
			} else {
				element.removeClass('fade')
			}

			if (element.parent('.dropdown-menu').length) {
				element
					.closest('li.dropdown')
					.addClass('active')
					.end()
					.find('[data-toggle="tab"]')
					.attr('aria-expanded', true)
			}

			callback && callback()
		}

		$active.length && transition ?
			$active
				.one('bsTransitionEnd', next)
				.emulateTransitionEnd(Tab.TRANSITION_DURATION) :
			next()

		$active.removeClass('in')
	}


	// TAB PLUGIN DEFINITION
	// =====================

	function Plugin(option) {
		return this.each(function () {
			var $this = $(this)
			var data  = $this.data('bs.tab')

			if (!data) $this.data('bs.tab', (data = new Tab(this)))
			if (typeof option == 'string') data[option]()
		})
	}

	var old = $.fn.tab

	$.fn.tab             = Plugin
	$.fn.tab.Constructor = Tab


	// TAB NO CONFLICT
	// ===============

	$.fn.tab.noConflict = function () {
		$.fn.tab = old
		return this
	}


	// TAB DATA-API
	// ============

	var clickHandler = function (e) {
		e.preventDefault()
		Plugin.call($(this), 'show')
	}

	$(document)
		.on('click.bs.tab.data-api', '[data-toggle="tab"]', clickHandler)
		.on('click.bs.tab.data-api', '[data-toggle="pill"]', clickHandler)

}(jQuery);


/* ========================================================================
 * Bootstrap: carousel.js v3.3.5
 * http://getbootstrap.com/javascript/#carousel
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
	'use strict';

	// CAROUSEL CLASS DEFINITION
	// =========================

	var Carousel = function (element, options) {
		this.$element    = $(element)
		this.$indicators = this.$element.find('.carousel-indicators')
		this.options     = options
		this.paused      = null
		this.sliding     = null
		this.interval    = null
		this.$active     = null
		this.$items      = null

		this.options.keyboard && this.$element.on('keydown.bs.carousel', $.proxy(this.keydown, this))

		this.options.pause == 'hover' && !('ontouchstart' in document.documentElement) && this.$element
			.on('mouseenter.bs.carousel', $.proxy(this.pause, this))
			.on('mouseleave.bs.carousel', $.proxy(this.cycle, this))
	}

	Carousel.VERSION  = '3.3.5'

	Carousel.TRANSITION_DURATION = 600

	Carousel.DEFAULTS = {
		interval: 5000,
		pause: 'hover',
		wrap: true,
		keyboard: true
	}

	Carousel.prototype.keydown = function (e) {
		if (/input|textarea/i.test(e.target.tagName)) return
		switch (e.which) {
			case 37: this.prev(); break
			case 39: this.next(); break
			default: return
		}

		e.preventDefault()
	}

	Carousel.prototype.cycle = function (e) {
		e || (this.paused = false)

		this.interval && clearInterval(this.interval)

		this.options.interval
		&& !this.paused
		&& (this.interval = setInterval($.proxy(this.next, this), this.options.interval))

		return this
	}

	Carousel.prototype.getItemIndex = function (item) {
		this.$items = item.parent().children('.item')
		return this.$items.index(item || this.$active)
	}

	Carousel.prototype.getItemForDirection = function (direction, active) {
		var activeIndex = this.getItemIndex(active)
		var willWrap = (direction == 'prev' && activeIndex === 0)
			|| (direction == 'next' && activeIndex == (this.$items.length - 1))
		if (willWrap && !this.options.wrap) return active
		var delta = direction == 'prev' ? -1 : 1
		var itemIndex = (activeIndex + delta) % this.$items.length
		return this.$items.eq(itemIndex)
	}

	Carousel.prototype.to = function (pos) {
		var that        = this
		var activeIndex = this.getItemIndex(this.$active = this.$element.find('.item.active'))

		if (pos > (this.$items.length - 1) || pos < 0) return

		if (this.sliding)       return this.$element.one('slid.bs.carousel', function () { that.to(pos) }) // yes, "slid"
		if (activeIndex == pos) return this.pause().cycle()

		return this.slide(pos > activeIndex ? 'next' : 'prev', this.$items.eq(pos))
	}

	Carousel.prototype.pause = function (e) {
		e || (this.paused = true)

		if (this.$element.find('.next, .prev').length && $.support.transition) {
			this.$element.trigger($.support.transition.end)
			this.cycle(true)
		}

		this.interval = clearInterval(this.interval)

		return this
	}

	Carousel.prototype.next = function () {
		if (this.sliding) return
		return this.slide('next')
	}

	Carousel.prototype.prev = function () {
		if (this.sliding) return
		return this.slide('prev')
	}

	Carousel.prototype.slide = function (type, next) {
		var $active   = this.$element.find('.item.active')
		var $next     = next || this.getItemForDirection(type, $active)
		var isCycling = this.interval
		var direction = type == 'next' ? 'left' : 'right'
		var that      = this

		if ($next.hasClass('active')) return (this.sliding = false)

		var relatedTarget = $next[0]
		var slideEvent = $.Event('slide.bs.carousel', {
			relatedTarget: relatedTarget,
			direction: direction
		})
		this.$element.trigger(slideEvent)
		if (slideEvent.isDefaultPrevented()) return

		this.sliding = true

		isCycling && this.pause()

		if (this.$indicators.length) {
			this.$indicators.find('.active').removeClass('active')
			var $nextIndicator = $(this.$indicators.children()[this.getItemIndex($next)])
			$nextIndicator && $nextIndicator.addClass('active')
		}

		var slidEvent = $.Event('slid.bs.carousel', { relatedTarget: relatedTarget, direction: direction }) // yes, "slid"
		if ($.support.transition && this.$element.hasClass('slide')) {
			$next.addClass(type)
			$next[0].offsetWidth // force reflow
			$active.addClass(direction)
			$next.addClass(direction)
			$active
				.one('bsTransitionEnd', function () {
					$next.removeClass([type, direction].join(' ')).addClass('active')
					$active.removeClass(['active', direction].join(' '))
					that.sliding = false
					setTimeout(function () {
						that.$element.trigger(slidEvent)
					}, 0)
				})
				.emulateTransitionEnd(Carousel.TRANSITION_DURATION)
		} else {
			$active.removeClass('active')
			$next.addClass('active')
			this.sliding = false
			this.$element.trigger(slidEvent)
		}

		isCycling && this.cycle()

		return this
	}


	// CAROUSEL PLUGIN DEFINITION
	// ==========================

	function Plugin(option) {
		return this.each(function () {
			var $this   = $(this)
			var data    = $this.data('bs.carousel')
			var options = $.extend({}, Carousel.DEFAULTS, $this.data(), typeof option == 'object' && option)
			var action  = typeof option == 'string' ? option : options.slide

			if (!data) $this.data('bs.carousel', (data = new Carousel(this, options)))
			if (typeof option == 'number') data.to(option)
			else if (action) data[action]()
			else if (options.interval) data.pause().cycle()
		})
	}

	var old = $.fn.carousel

	$.fn.carousel             = Plugin
	$.fn.carousel.Constructor = Carousel


	// CAROUSEL NO CONFLICT
	// ====================

	$.fn.carousel.noConflict = function () {
		$.fn.carousel = old
		return this
	}


	// CAROUSEL DATA-API
	// =================

	var clickHandler = function (e) {
		var href
		var $this   = $(this)
		var $target = $($this.attr('data-target') || (href = $this.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '')) // strip for ie7
		if (!$target.hasClass('carousel')) return
		var options = $.extend({}, $target.data(), $this.data())
		var slideIndex = $this.attr('data-slide-to')
		if (slideIndex) options.interval = false

		Plugin.call($target, options)

		if (slideIndex) {
			$target.data('bs.carousel').to(slideIndex)
		}

		e.preventDefault()
	}

	$(document)
		.on('click.bs.carousel.data-api', '[data-slide]', clickHandler)
		.on('click.bs.carousel.data-api', '[data-slide-to]', clickHandler)

	$(window).on('load', function () {
		$('[data-ride="carousel"]').each(function () {
			var $carousel = $(this)
			Plugin.call($carousel, $carousel.data())
		})
	})

}(jQuery);

/* ========================================================================
 * Bootstrap: modal.js v3.3.5
 * http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2011-2015 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
	'use strict';

	// MODAL CLASS DEFINITION
	// ======================

	var Modal = function (element, options) {
		this.options             = options
		this.$body               = $(document.body)
		this.$element            = $(element)
		this.$dialog             = this.$element.find('.modal-dialog')
		this.$backdrop           = null
		this.isShown             = null
		this.originalBodyPad     = null
		this.scrollbarWidth      = 0
		this.ignoreBackdropClick = false

		if (this.options.remote) {
			this.$element
				.find('.modal-content')
				.load(this.options.remote, $.proxy(function () {
					this.$element.trigger('loaded.bs.modal')
				}, this))
		}
	}

	Modal.VERSION  = '3.3.5'

	Modal.TRANSITION_DURATION = 300
	Modal.BACKDROP_TRANSITION_DURATION = 150

	Modal.DEFAULTS = {
		backdrop: true,
		keyboard: true,
		show: true
	}

	Modal.prototype.toggle = function (_relatedTarget) {
		return this.isShown ? this.hide() : this.show(_relatedTarget)
	}

	Modal.prototype.show = function (_relatedTarget) {
		var that = this
		var e    = $.Event('show.bs.modal', { relatedTarget: _relatedTarget })

		this.$element.trigger(e)

		if (this.isShown || e.isDefaultPrevented()) return

		this.isShown = true

		this.checkScrollbar()
		this.setScrollbar()
		this.$body.addClass('modal-open')

		this.escape()
		this.resize()

		this.$element.on('click.dismiss.bs.modal', '[data-dismiss="modal"]', $.proxy(this.hide, this))

		this.$dialog.on('mousedown.dismiss.bs.modal', function () {
			that.$element.one('mouseup.dismiss.bs.modal', function (e) {
				if ($(e.target).is(that.$element)) that.ignoreBackdropClick = true
			})
		})

		this.backdrop(function () {
			var transition = $.support.transition && that.$element.hasClass('fade')

			if (!that.$element.parent().length) {
				that.$element.appendTo(that.$body) // don't move modals dom position
			}

			that.$element
				.show()
				.scrollTop(0)

			that.adjustDialog()

			if (transition) {
				that.$element[0].offsetWidth // force reflow
			}

			that.$element.addClass('in')

			that.enforceFocus()

			var e = $.Event('shown.bs.modal', { relatedTarget: _relatedTarget })

			transition ?
				that.$dialog // wait for modal to slide in
					.one('bsTransitionEnd', function () {
						that.$element.trigger('focus').trigger(e)
					})
					.emulateTransitionEnd(Modal.TRANSITION_DURATION) :
				that.$element.trigger('focus').trigger(e)
		})
	}

	Modal.prototype.hide = function (e) {
		if (e) e.preventDefault()

		e = $.Event('hide.bs.modal')

		this.$element.trigger(e)

		if (!this.isShown || e.isDefaultPrevented()) return

		this.isShown = false

		this.escape()
		this.resize()

		$(document).off('focusin.bs.modal')

		this.$element
			.removeClass('in')
			.off('click.dismiss.bs.modal')
			.off('mouseup.dismiss.bs.modal')

		this.$dialog.off('mousedown.dismiss.bs.modal')

		$.support.transition && this.$element.hasClass('fade') ?
			this.$element
				.one('bsTransitionEnd', $.proxy(this.hideModal, this))
				.emulateTransitionEnd(Modal.TRANSITION_DURATION) :
			this.hideModal()
	}

	Modal.prototype.enforceFocus = function () {
		$(document)
			.off('focusin.bs.modal') // guard against infinite focus loop
			.on('focusin.bs.modal', $.proxy(function (e) {
				if (this.$element[0] !== e.target && !this.$element.has(e.target).length) {
					this.$element.trigger('focus')
				}
			}, this))
	}

	Modal.prototype.escape = function () {
		if (this.isShown && this.options.keyboard) {
			this.$element.on('keydown.dismiss.bs.modal', $.proxy(function (e) {
				e.which == 27 && this.hide()
			}, this))
		} else if (!this.isShown) {
			this.$element.off('keydown.dismiss.bs.modal')
		}
	}

	Modal.prototype.resize = function () {
		if (this.isShown) {
			$(window).on('resize.bs.modal', $.proxy(this.handleUpdate, this))
		} else {
			$(window).off('resize.bs.modal')
		}
	}

	Modal.prototype.hideModal = function () {
		var that = this
		this.$element.hide()
		this.backdrop(function () {
			that.$body.removeClass('modal-open')
			that.resetAdjustments()
			that.resetScrollbar()
			that.$element.trigger('hidden.bs.modal')
		})
	}

	Modal.prototype.removeBackdrop = function () {
		this.$backdrop && this.$backdrop.remove()
		this.$backdrop = null
	}

	Modal.prototype.backdrop = function (callback) {
		var that = this
		var animate = this.$element.hasClass('fade') ? 'fade' : ''

		if (this.isShown && this.options.backdrop) {
			var doAnimate = $.support.transition && animate

			this.$backdrop = $(document.createElement('div'))
				.addClass('modal-backdrop ' + animate)
				.appendTo(this.$body)

			this.$element.on('click.dismiss.bs.modal', $.proxy(function (e) {
				if (this.ignoreBackdropClick) {
					this.ignoreBackdropClick = false
					return
				}
				if (e.target !== e.currentTarget) return
				this.options.backdrop == 'static'
					? this.$element[0].focus()
					: this.hide()
			}, this))

			if (doAnimate) this.$backdrop[0].offsetWidth // force reflow

			this.$backdrop.addClass('in')

			if (!callback) return

			doAnimate ?
				this.$backdrop
					.one('bsTransitionEnd', callback)
					.emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
				callback()

		} else if (!this.isShown && this.$backdrop) {
			this.$backdrop.removeClass('in')

			var callbackRemove = function () {
				that.removeBackdrop()
				callback && callback()
			}
			$.support.transition && this.$element.hasClass('fade') ?
				this.$backdrop
					.one('bsTransitionEnd', callbackRemove)
					.emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
				callbackRemove()

		} else if (callback) {
			callback()
		}
	}

	// these following methods are used to handle overflowing modals

	Modal.prototype.handleUpdate = function () {
		this.adjustDialog()
	}

	Modal.prototype.adjustDialog = function () {
		var modalIsOverflowing = this.$element[0].scrollHeight > document.documentElement.clientHeight

		this.$element.css({
			paddingLeft:  !this.bodyIsOverflowing && modalIsOverflowing ? this.scrollbarWidth : '',
			paddingRight: this.bodyIsOverflowing && !modalIsOverflowing ? this.scrollbarWidth : ''
		})
	}

	Modal.prototype.resetAdjustments = function () {
		this.$element.css({
			paddingLeft: '',
			paddingRight: ''
		})
	}

	Modal.prototype.checkScrollbar = function () {
		var fullWindowWidth = window.innerWidth
		if (!fullWindowWidth) { // workaround for missing window.innerWidth in IE8
			var documentElementRect = document.documentElement.getBoundingClientRect()
			fullWindowWidth = documentElementRect.right - Math.abs(documentElementRect.left)
		}
		this.bodyIsOverflowing = document.body.clientWidth < fullWindowWidth
		this.scrollbarWidth = this.measureScrollbar()
	}

	Modal.prototype.setScrollbar = function () {
		var bodyPad = parseInt((this.$body.css('padding-right') || 0), 10)
		this.originalBodyPad = document.body.style.paddingRight || ''
		if (this.bodyIsOverflowing) this.$body.css('padding-right', bodyPad + this.scrollbarWidth)
	}

	Modal.prototype.resetScrollbar = function () {
		this.$body.css('padding-right', this.originalBodyPad)
	}

	Modal.prototype.measureScrollbar = function () { // thx walsh
		var scrollDiv = document.createElement('div')
		scrollDiv.className = 'modal-scrollbar-measure'
		this.$body.append(scrollDiv)
		var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth
		this.$body[0].removeChild(scrollDiv)
		return scrollbarWidth
	}


	// MODAL PLUGIN DEFINITION
	// =======================

	function Plugin(option, _relatedTarget) {
		return this.each(function () {
			var $this   = $(this)
			var data    = $this.data('bs.modal')
			var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option == 'object' && option)

			if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
			if (typeof option == 'string') data[option](_relatedTarget)
			else if (options.show) data.show(_relatedTarget)
		})
	}

	var old = $.fn.modal

	$.fn.modal             = Plugin
	$.fn.modal.Constructor = Modal


	// MODAL NO CONFLICT
	// =================

	$.fn.modal.noConflict = function () {
		$.fn.modal = old
		return this
	}


	// MODAL DATA-API
	// ==============

	$(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
		var $this   = $(this)
		var href    = $this.attr('href')
		var $target = $($this.attr('data-target') || (href && href.replace(/.*(?=#[^\s]+$)/, ''))) // strip for ie7
		var option  = $target.data('bs.modal') ? 'toggle' : $.extend({ remote: !/#/.test(href) && href }, $target.data(), $this.data())

		if ($this.is('a')) e.preventDefault()

		$target.one('show.bs.modal', function (showEvent) {
			if (showEvent.isDefaultPrevented()) return // only register focus restorer if modal will actually get shown
			$target.one('hidden.bs.modal', function () {
				$this.is(':visible') && $this.trigger('focus')
			})
		})
		Plugin.call($target, option, this)
	})

}(jQuery);


/**
 * 公用js文件
 */

function Base() {
	this.init();
};
Base.prototype = {
	init : function() {
		if ($('#js-download').length > 0) {
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
		$('#js-addFavorites')
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
		$('#js-setHome')
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
