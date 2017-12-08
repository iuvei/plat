(function(){

})();

$(function(){
	
	//vip_page切换效果
	var vip_pages = $(".vip_page");
	var vip_handle = $(".vib_links a");
	
	vip_handle.each(function(num){
		$(this).mouseover(function(){
			vip_pages.removeClass("show");
			$(vip_pages[num]).addClass("show");
			vip_handle.each(function(n){
				$(vip_handle[n]).removeClass("a"+(n+1)+"_hover");
			});	
			$(vip_handle[num]).addClass("a"+(num+1)+"_hover");
		});	
	});

	//pref_list的滑动效果
	var pref_ctrl = true;
	
	$(".pref_list .list .handle").mouseover(function(){

		var _par = $(this).parent();
		if(_par.hasClass("list_hover")){
			return;	
		}	
		
		if(!pref_ctrl){
			return;	
		}
		
		pref_ctrl =false;
		
		_par.animate({
			"width" : 653	
		},100,function(){
			$(this).addClass("list_hover");	
			pref_ctrl = true;	
		});
		
		$(".pref_list .list_hover").animate({
			"width" : 73	
		},100,function(){
			$(this).removeClass("list_hover");	
			pref_ctrl = true;	
		});
		
	});
	
});	