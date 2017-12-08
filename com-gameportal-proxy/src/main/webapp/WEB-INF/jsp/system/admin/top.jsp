<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<div class="navbar navbar-inverse">
  <div class="navbar-inner">
   <div class="container-fluid">
	  <a class="brand" href="#"><small><i class="icon-leaf"></i> 代理管理后台</small> </a>
	  <a class="brand" href="#"><small id="timer"></small> </a>
	  <ul class="nav ace-nav pull-right">
			<li class="light-blue user-profile">
				<a class="user-menu dropdown-toggle" href="#" data-toggle="dropdown">
					<span id="user_info">
						<small>Welcome,</small> ${memberinfo.account}
					</span>
					<i class="icon-caret-down"></i>
				</a>
				<ul id="user_menu" class="pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
					<!-- <li><a href="#"><i class="icon-cog"></i> Settings</a></li>
					<li><a href="#"><i class="icon-user"></i> Profile</a></li>
					<li class="divider"></li> -->
					<li><a href="${ctx }login/logout.do"><i class="icon-off"></i>退出登录</a></li>
				</ul>
			</li>
	  </ul><!--/.ace-nav-->
   </div><!--/.container-fluid-->
  </div><!--/.navbar-inner-->
</div><!--/.navbar-->
<script src="/static/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	setInterval("getCurentTime()",1000); 
});

function getCurentTime()
{ 
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();          //分
   
    var clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
   
    if(hh < 10)
        clock += "0";
       
    clock += hh + ":";
    if (mm < 10) clock += '0'; 
    clock += mm+":"; 
    if (ss < 10) clock += '0'; 
    clock += ss;
    $("#timer").html(clock);
} 
</script>