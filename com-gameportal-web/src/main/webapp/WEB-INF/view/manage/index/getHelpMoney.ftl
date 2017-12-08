<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--自助救援金</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <style>.slot_live{float:left;margin-right:50px;}</style>
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/public/common.js"></script>
    <script type="text/javascript" >
    $(function(){
		// 申请老虎机救援金
		$("#act4").click(function(){
			var helpScale = $("#helpScale").attr("data");
			if(helpScale ==''){
				$.msg('请选择领取比例。',3);
				return;
			}
			var multiple = "";
			if(helpScale =='1'){
				multiple = "您选择了领取比例5%，需要投注10倍流水才能提款哦！";
			}else if(helpScale =='2'){
				multiple = "您选择了领取比例10%，需要投注12倍流水才能提款哦！";
			}else{
				multiple = "您选择了领取比例20%，需要投注20倍流水才能提款哦！";
			}
			$.confirm(multiple,cb_ok);
			
			function cb_ok(){
				$("#act4").val("正在提交申请...");
				$("#act4").addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
			        type: "POST", 
			        dataType: "json",
			        data:{type:helpScale},
			        url: "${action_path}manage/getHelp.do",
			        success: function (result){ 
		            	$("#act4").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.success){
		            		$.msg(result.msg,1,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act4").val("马上领取");
		            	}
		            }  
		   		});
			}
			
		});
		
		// 申请百家乐救援金
		$("#act5").click(function(){
			var helpScale = $("#liveScale").attr("data");
			if(helpScale ==''){
				$.msg('请选择领取比例。',3);
				return;
			}
	   		var multiple = "";
			if(helpScale =='1'){
				multiple = "您选择了领取比例3%，需要投注8倍流水才能提款哦！";
			}else if(helpScale =='2'){
				multiple = "您选择了领取比例4%，需要投注8倍流水才能提款哦！";
			}else{
				multiple = "您选择了领取比例5%，需要投注8倍流水才能提款哦！";
			}
			$.confirm(multiple,cb_ok);
			
			function cb_ok(){
				$("#act5").val("正在提交申请...");
				$("#act5").addClass("ui-disabled").attr("disabled", "true");
				$.ajax({  
			        type: "POST", 
			        dataType: "json",
			        data:{type:helpScale},
			        url: "${action_path}manage/getLiveHelp.do",
			        success: function (result){ 
		            	$("#act5").removeClass("ui-disabled").removeAttr("disabled");
		            	if(result.success){
		            		$.msg(result.msg,1,success_callback);
		            	}else{
		            		$.msg(result.msg,3);
		            		$("#act5").val("马上领取");
		            	}
		            }  
		   		});
			}
		});
		
		function success_callback(){
			window.location.reload();
		}
    })
    </script>
</head>

<body>
    <div class="title size-bigger">自助救援金领取</div>
    <div class="main">
        <div class="user-layer ui-mt30">
	            <div class="user-box ui-alignCenter ui-radius slot_live">
	                <img src="/static/images/user/promotion/jyj_slot.jpg">
	                <h2 class="size-big ui-strong" style="font-weight:bold;">老虎机救援金</h2>
	                <div class="ui-ddl fl size-normal ui-radius">
	                    <div class="input fl ui-hand ui-textOverflow" data="1" id="helpScale">领取比例5%</div>
	                    <span class="fl"></span>
	                    <ul class="ui-radius">
	                        <!--<li data="">选择领取比例</li>-->
	                        <li data="1">领取比例5%</li>
	                        <li data="2">领取比例10%</li>
	                        <#if userInfo.grade &gt; 3>
	                        	<li data="3">领取比例20%</li>
	                        </#if>
	                    </ul>
	                </div>
	                <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" id="act4" value="马上领取" />
	            </div>
	            
	             <div class="user-box ui-alignCenter ui-radius slot_live">
	                <img src="/static/images/user/promotion/jyj_live.jpg">
	                <h2 class="size-big ui-strong" style="font-weight:bold;">百家乐救援金</h2>
	                <div class="ui-ddl fl size-normal ui-radius">
	                    <div class="input fl ui-hand ui-textOverflow" data="1" id="liveScale">领取比例3%</div>
	                    <span class="fl"></span>
	                    <ul class="ui-radius">
	                        <li data="1">领取比例3%</li>
	                        <li data="2">领取比例4%</li>
	                        <li data="3">领取比例5%</li>
	                    </ul>
	                </div>
	                <input type="button" class="get ui-btn ui-block size-big ui-radius color-white bg-orange bg-brownHover ui-transition" id="act5" value="马上领取" />
	            </div>
	            
        </div>
    </div>

</body>

</html>