(function(jQuery) {
	 $ =  jQuery;
	 var currentindex=1,
	 speed = 5000,
	 timerID=undefined,
	 //win = $(window),
	 changeflash = function(v){
		 currentindex = v;
		 try{
			 var adcount = $(".control_list").find("li").length;
			 for (var i=1;i<=adcount;i++){
				 if(i == v){
					 $("#flash"+i).fadeIn("normal");
					 $("#flash"+i).css("display","block");
					 $("#index_focus_"+i).attr("class","");
					 $("#index_focus_"+i).addClass("current");
					 $("#flashbg").css("background-color",$("#flash"+i).attr("data-color"));
				 }else{
					 $("#flash"+i).css("display","none");
					 $("#index_focus_"+i).attr("class","");
				 }
			 }
			 
		 }catch (e) {
			 window.console.log("->"+e);
		}
	 },
	 timerTick = function(){//定时任务
		 var adcount = $(".control_list").find("li").length;
		 currentindex = currentindex>=adcount ? 1 : currentindex+1;
		 
		 changeflash(currentindex);
	 },
	 startTick = function(){
		 timerID = setInterval(timerTick,speed);
	 },
	 stopTick = function(){
		clearInterval(timerID);
	 };
	 $(".control_list li").mouseover(function(){
		 stopTick();
		 var index = $(this).attr("data-role");
		 changeflash(parseInt(index));
		 
	 }).mouseout(function(){
		 startTick();
	 });
	 $(".banner_control a").click(function(){
		 stopTick();
		 var adcount = $(".control_list").find("li").length;
		 var p = $(this).attr("data-role");
		 if(p != undefined){
			 if(p == 'prev'){//向前
				 currentindex = currentindex==1 ? adcount : currentindex-1;
			 }
			 if(p == 'next'){//向后
				 currentindex = currentindex>=adcount ? 1 : currentindex+1;
			 }
			 changeflash(currentindex);
		 }
		 startTick();
	 });
	 startTick();
})(jQuery);