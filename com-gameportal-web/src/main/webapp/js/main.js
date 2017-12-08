var actionPath = "/";
// 设置为主页 
function SetHome(obj,vrl){ 
try{ obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);} 
catch(e){ if(window.netscape) { try {netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect"); } 
catch (e){ alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。"); } 
var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch); 
prefs.setCharPref('browser.startup.homepage',vrl); }
else{alert("您的浏览器不支持，请按照下面步骤操作：1.打开浏览器设置。2.点击设置网页。3.输入："+vrl+"点击确定。"); } 
} 
} 

// 加入收藏 兼容360和IE6 
function shoucang(sTitle,sURL) 
{ try {window.external.addFavorite(sURL, sTitle);} 

  catch (e) { try {window.sidebar.addPanel(sTitle, sURL, "");} 
  catch (e) { alert("加入收藏失败，请使用Ctrl+D进行添加");} 
  
} 
}

//获取参数getQueryString("参数名3")
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
}


//网站公告
xw=function(A,B,C,D,E,F){ 
var $=function (id){return document.getElementById(id)},Y=+!!F; 
(A=$(A)).appendChild((B=$(B)).cloneNode(true)); 
(function (){ 
var m=A.scrollTop%C?(E||0):D; 
A.scrollTop=[0,B.offsetHeight][Y]==A.scrollTop?[B.offsetHeight-1,1][Y]:(A.scrollTop+[-1,1][Y]); 
setTimeout(arguments.callee,m); 
})()
return arguments.callee; 
}


//tab选项JS
function switchmodtab(modtab, modcontent, modk)
{ 
  for (var i = 1; i <= 5; i++)
  
  {
	try {
		  if (i == modk) {
	         document.getElementById(modtab + i).className = "quesitonon"; 
	         document.getElementById(modcontent + i).className = "list";
	     }else {
	         document.getElementById(modtab + i).className = "quesitonno"; 
	         document.getElementById(modcontent + i).className = "list_none";
	     }
	} catch (e) {
		
	}
     
  }
}

//tab选项JS
function switchmodtab2(modtab, modcontent, modk)
{ 
  for (var i = 1; i <= 5; i++)
  
  {
	try {
		  if (i == modk) {
	         document.getElementById(modtab + i).className = "usertitleover"; 
	         document.getElementById(modcontent + i).className = "list";
	     }else {
	         document.getElementById(modtab + i).className = "usertitle"; 
	         document.getElementById(modcontent + i).className = "list_none";
	     }
	} catch (e) {
		
	}
     
  }
}



//显示/隐藏内容
function switchDiv(id){
var theDiv = document.getElementById(id);
if(theDiv.style.display==''){theDiv.style.display="none";}
else{theDiv.style.display="";}}

//刷新验证码
function imgCode(){
	$("#imgCode").attr("src",actionPath+"validationCode/pcrimg.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode2(){
	$("#imgCodeAgent").attr("src",actionPath+"validationCode/agentcode.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode3(){
	$("#imgCodeQr").attr("src",actionPath+"validationCode/qrcode.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode4(){
	$("#imgCodewx").attr("src",actionPath+"validationCode/wxcode.do?r="+(new Date()).getTime());
}

//刷新验证码
function imgCode5(){
	$("#imgdk").attr("src",actionPath+"validationCode/wxcode.do?r="+(new Date()).getTime());
}

//选择
function setCurr() {
	var url = top.location.href;
	if (url.indexOf("/agreement.html") > 0 || url.indexOf("/agreement.do") > 0) {
		$('#td_agreement').attr('onMouseOver','');
		$('#td_agreement').attr('onMouseOut','');
		$('#td_agreement').removeClass('menulistno');
		$('#td_agreement').addClass('menuliston');
	}else if (url.indexOf("/about.html") > 0 || url.indexOf("/about.do") >0) {
		$('#td_about').attr('onMouseOver','');
		$('#td_about').attr('onMouseOut','');
		$('#td_about').removeClass('menulistno');
		$('#td_about').addClass('menuliston');
	}else if (url.indexOf("/gaming.html") > 0 || url.indexOf("/gaming.do") > 0) {
		$('#td_gaming').attr('onMouseOver','');
		$('#td_gaming').attr('onMouseOut','');
		$('#td_gaming').removeClass('menulistno');
		$('#td_gaming').addClass('menuliston');
	}else if (url.indexOf("/disclaimer.html") > 0 || url.indexOf("/disclaimer.do") > 0) {
		$('#td_disclaimer').attr('onMouseOver','');
		$('#td_disclaimer').attr('onMouseOut','');
		$('#td_disclaimer').removeClass('menulistno');
		$('#td_disclaimer').addClass('menuliston');
	}else if (url.indexOf("/contact.html") > 0 || url.indexOf("/contact.do") > 0) {
		$('#td_contact').attr('onMouseOver','');
		$('#td_contact').attr('onMouseOut','');
		$('#td_contact').removeClass('menulistno');
		$('#td_contact').addClass('menuliston');
	}else if (url.indexOf("/privacy.html") > 0 || url.indexOf("/privacy.do") > 0) {
		$('#td_privacy').attr('onMouseOver','');
		$('#td_privacy').attr('onMouseOut','');
		$('#td_privacy').removeClass('menulistno');
		$('#td_privacy').addClass('menuliston');
	}else if (url.indexOf("/help.html") > 0 || url.indexOf("/help.do") > 0) {
		$('#td_help').attr('onMouseOver','');
		$('#td_help').attr('onMouseOut','');
		$('#td_help').removeClass('menulistno');
		$('#td_help').addClass('menuliston');
	}else if (url.indexOf("/notice.html") > 0 || url.indexOf("/notice.do") >0) {
		$('#td_notice').attr('onMouseOver','');
		$('#td_notice').attr('onMouseOut','');
		$('#td_notice').removeClass('menulistno');
		$('#td_notice').addClass('menuliston');
	}else if (url.indexOf("/autoReward.html") >0 || url.indexOf("/autoReward.do") >0){
		$('#td_autoReward').attr('onMouseOver','');
		$('#td_autoReward').attr('onMouseOut','');
		$('#td_autoReward').removeClass('menulistno');
		$('#td_autoReward').addClass('menuliston');
	}
}

function memberSetCurr(){
	var url = top.location.href;
	if (url.indexOf("manage/index.html") > 0 || url.indexOf("manage/index.do") > 0) {
		$('#zhzx').attr('onMouseOver','');
		$('#zhzx').attr('onMouseOut','');
		$("#zhzx").removeClass("menutitle");
		$('#zhzx').addClass('menutitleover');
	}else if(url.indexOf("manage/userdata.html") > 0 || url.indexOf("manage/user/changePwd.do") > 0 || url.indexOf("manage/user/userBindBank.do") > 0){
		$('#grzl').attr('onMouseOver','');
		$('#grzl').attr('onMouseOut','');
		$("#grzl").removeClass("menutitle");
		$('#grzl').addClass('menutitleover');
	}else if(url.indexOf("manage/capital/userDeposit.do") > 0 || url.indexOf("manage/capital/userCaseout.do") > 0 || url.indexOf("manage/capital/userTransfer.do") > 0){
		$('#zjgl').attr('onMouseOver','');
		$('#zjgl').attr('onMouseOut','');
		$("#zjgl").removeClass("menutitle");
		$('#zjgl').addClass('menutitleover');
	}else if(url.indexOf("manage/reportQuery/outinRecord.do") > 0 || url.indexOf("manage/reportQuery/limitRecord.do") > 0){
		$('#bbcx').attr('onMouseOver','');
		$('#bbcx').attr('onMouseOut','');
		$("#bbcx").removeClass("menutitle");
		$('#bbcx').addClass('menutitleover');
	}else if(url.indexOf("/autoReward.html") >0 || url.indexOf("/autoReward.do") >0){
		$('#yhlq').attr('onMouseOver','');
		$('#yhlq').attr('onMouseOut','');
		$("#yhlq").removeClass("menutitle");
		$('#yhlq').addClass('menutitleover');
	}else if(url.indexOf("/queryMemberXimaMain.html") >0 || url.indexOf("/queryMemberXimaMain.do") >0 || url.indexOf("manage/reportQuery/betRecord.do") > 0){
		$('#zzzx').attr('onMouseOver','');
		$('#zzzx').attr('onMouseOut','');
		$("#zzzx").removeClass("menutitle");
		$('#zzzx').addClass('menutitleover');
	}
	
}