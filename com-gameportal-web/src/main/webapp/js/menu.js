(function(){
	

	
//清除所有input的value
	function ClearValue(forms){
		this.forms = forms;
		this.load();	
	}
	
	ClearValue.prototype = {
		constructor : this,
		load : function(){
			var _this = this;			
			this.forms.each(function(){
				_this.clearValue($(this));	
			});
		},
		clearValue : function(fm){			
			this.inputs = $("input.text,input.keyword",fm);
			this.textarea = $("textarea",fm);
			var _this = this;
			var dValues = [];	
			var aValues = [];		
			this.inputs.each(function(n){
				dValues[n] = $(_this.inputs[n]).val();
			});
			this.textarea.each(function(n){
				aValues[n] = $(_this.textarea[n]).html();
			});
						
			this.inputs.each(function(n){
				$(this).focus(function(){
					if($(this).val() == dValues[n]){
						$(this).val("");	
					}
				});	
				$(this).blur(function(){
					if($(this).val() == ""){
						$(this).val(dValues[n]);	
					}	
				});
			});	
			this.textarea.each(function(n){
				$(this).focus(function(){
					if($(this).html() == aValues[n]){
						$(this).html("");	
					}
				});	
				$(this).blur(function(){
					if($(this).html() == ""){
						$(this).html(aValues[n]);	
					}	
				});	
			});
		}	
	};
	
	window.ClearValue = ClearValue;
	//清除所有input的value
	
//顶端adver渐隐
	
function FadeAdver(args){
	for(var i in args){
			this[i] = args[i];	
		}	
		this.speed = args.speed ? args.speed : 3000;	//间隔时间默认3秒
		this.sTime = args.sTime ? args.sTime : 500;	//渐进时间，默认1秒
		this.load();
		this.start();
}

FadeAdver.prototype ={
	constructor : this,
	load : function(){
		var _this = this;
		this.num = 0;	//计时器
		this.mNum = this.num+1;	//轮播计时
		this.len = this.divs.length;					
		
		//所有div设置absolute并排好index
		this.divs.each(function(num){
			var z_index = 500-num;
			$(this).css({
				"position" : "absolute",
				"left" : 0,
				"top" : 0,
				"z-index" : z_index,
				"display" : "none"	
			})
		});
		
		$(this.divs[0]).show();
		
		//所有div设置absolute并排好index
		
			
		this.btns.each(function(num){
			$(this).mouseover(function(){
				_this.show(num);	
				_this.stop();				
			}).mouseout(function(){
				_this.start();	
			});	
		});
		
		//左右按钮的使用
		if(!!this.preBtn && !!this.nextBtn){
			this.preBtn.css("z-index",1000);
			this.preBtn.click(function(){
				var num = _this.num - 1;
				if(num < 0){
					num = _this.len-1;		
				}	
				_this.show(num);
			});	
			this.nextBtn.css("z-index",1000);
			this.nextBtn.click(function(){
				var num = _this.num + 1;
				if(num >= _this.len){
					num = 0;		
				}	
				_this.show(num);
			});	
		}
		
		this.divs.each(function(num){
			$(this).mouseover(function(){					
				_this.stop();				
			}).mouseout(function(){
				_this.start();	
			});	
		});
	},
	show : function(num){
		if(num == this.num) return;	//同一个返回
		
		
		var _this = this;
		this.flag  = false;	//关闭控制开关
		this.btns.each(function(i){
			if(i == num){
				$(this).addClass("hover");	
			}else{
				$(this).removeClass("hover");	
			}				
		});
				
		$(this.divs[this.num]).fadeOut(this.sTime);	//旧的淡出
						
		$(this.divs[num]).fadeIn(_this.sTime);		//新的淡入
		_this.num = num;	
		_this.mNum = num+1;			
				
	},
	start : function(){
		var _this = this;					
		this.interval = setInterval(function(){					
			if(_this.mNum >= _this.len){
				_this.mNum = 0;
			}						
			_this.show(_this.mNum);								
		},this.speed);
	},
	stop : function(){
		clearInterval(this.interval);
	}	
};

window.FadeAdver = FadeAdver;
//顶端adver	

//ChangeDiv切换效果
function ChangeDiv(args){
	for(var i in args){
		this[i] = args[i];	
	}	
	this.type = this.type ? this.type : "click"; // mouseover 改为click
	this.load();
}

ChangeDiv.prototype = {
	constructor : this,
	load : function(){
		var _this = this;
		this.btns.each(function(num){
			if(_this.type == "click"){
				$(this).click(function(){
					_this.change(num)	
				});		
			}else{
				$(this).mouseover(function(){
					_this.change(num)	
				});		
			}			
		});	
	},
	change : function(num){
		var _this = this;
		
		this.btns.each(function(n){
			if(n ==num){
				$(this).addClass("hover");		
			}else{
				$(this).removeClass("hover");		
			}				
		});
		
		this.divs.each(function(n){
			if(n ==num){
				$(this).addClass("show");		
			}else{
				$(this).removeClass("show");		
			}				
		});
	}	
};

window.ChangeDiv = ChangeDiv;
//ChangeDiv切换效果

//select 替换类
function SameSelect(obj){
	this.obj = obj;
	this.opts = $("option",obj);
	this.top = $(".top",obj);
	this.btn = $(".btn",obj);
	this.lis = $("li",obj);		
	this.load();	
}

SameSelect.prototype = {
	constructor : this,
	load : function(){
		var _this = this;
		
		this.btn.click(function(){
			if(_this.obj.hasClass("select_hover")){
				_this.hide();	
			}else{
				_this.show();		
			}			
		});
		this.lis.mouseover(function(){
			_this.lis.removeClass("hover");
			$(this).addClass("hover");
		});
		this.lis.each(function(num){
			$(this).click(function(){
				_this.set(num);	
			});	
		});
		this.obj.mouseout(function(){
			_this.wait = setTimeout(function(){
					_this.hide();	
			},200);			
		});
		$("*",this.obj).mouseover(function(){
			if(!!_this.wait){
				clearTimeout(_this.wait);		
			}			
		});		
	},
	show : function(){
		var _this = this;
		//和top相同的li隐藏一下
		this.lis.show();		
		this.lis.each(function(){
			if($(this).html() == _this.top.html()){
				$(this).hide();	
			}	
		});
		
		this.obj.addClass("select_hover");	
	},
	hide : function(){
		this.obj.removeClass("select_hover");	
	},
	set : function(num){
		var _this = this;
		this.hide();
		this.top.html($(this.lis[num]).html());	
		this.opts.removeAttr("selected");
		$(this.opts[num]).attr("selected","selected");
	}
};

window.SameSelect = SameSelect;
//select 替换类

})();

$(function(){

	new ClearValue($("form"));	//清除默认文字
	
	//tabs统一调用
	$(".tabs").each(function(){
		new ChangeDiv({
			btns : $(".tabs_handle .h",this),
			divs : $(".con",this)	
		});	
	});
	
	//select美化的统一调用
	$(".select").each(function(){
		new SameSelect($(this));	
	});
	
	//返回顶部
	$(".flog_left .go_top,.flog_right .go_top").click(function(e){
		$(window).scrollTop(0);	
		if(e == null) var e = window.event; 
		e.cancelBubble = true;
		if (e.stopPropagation) e.stopPropagation();
	});
	
	//top_login密码切换
	
	$(".top_login .ps1").focus(function(){
		$(this).hide();
		$(".top_login .ps2").show().focus();	
	});
	$(".top_login .ps2").blur(function(){
		if($(this).val() == ""){
			$(this).hide();
			$(".top_login .ps1").show();
		}	
	});
	
	//nav下拉菜单特效
	if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) { 		
		$(".nav_slide").css("width",$(window).width());	
		$(".nav_slide").css("background-image","url(images/nav_slide.png)");	
	} 
	
	//站内信超过9条显示n
	if($(".afi_user a").html()*1 > 9){
		$(".afi_user a").html("N");	
	}
	
	
	$(".nav_in").hover(function(){		
		$(this).addClass("nav_in_hover");	
		$(".nav_slide",this).fadeIn(200);
	},function(){	
		$(".nav_slide",this).fadeOut(200);			
		$(this).removeClass("nav_in_hover");		
	});
	
	
	
	$(".nav_slide1 .list").hover(function(){
		$(this).addClass("list_hover");	
	},function(){
		$(this).removeClass("list_hover");	
	});

});	