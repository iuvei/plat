// JavaScript Document
!(function($, window, document, undefined){
	var center = {
		init: function(){
			$(".centerIfm").Validform({
				tiptype:function(msg,o,cssctl){
					if(!o.obj.is("form")){
						var objtip=o.obj.parents('.formli').find(".Validform_checktip");
						cssctl(objtip,o.type);
						objtip.text(msg);
						
						var infoObj=o.obj.parents('.formli').find(".info");
						if(o.type==2){
							infoObj.fadeOut(200);
						}else{
							if(infoObj.is(":visible")){return;}
							var left=o.obj.offset().left,
								top=o.obj.offset().top;
			
							infoObj.css({
								left:left+170,
								top:top-45
							}).show().animate({
								top:top-35	
							},200);
						}
						
					}	
				}
			});
		}
	}
	
	window.center = center;
	
})(jQuery, window, document);

$(function(){
	center.init();	
});