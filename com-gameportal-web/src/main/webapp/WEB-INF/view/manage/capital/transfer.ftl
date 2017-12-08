<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "${action_path}common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--我要转账</title>
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="${zy_path}static/css/user/user.css?v=2" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/capital/deposit.css" />
    <script type="text/javascript" src="${zy_path}static/lib/jquery.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/public/utils.js"></script>
    <script type="text/javascript" src="${zy_path}static/js/user/user.js"></script>
    <script type="text/javascript" src="${zy_path}js/transfer.js?v=${version}"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script type="text/javascript" >
    	var accountMoneyCount = ${accountMoneyCount!};
	    $(function(){
	    	$("#transferOut").closest(".ui-ddl").find("ul").delegate("li","click",function(e){
				var data = $(this).attr("data");
				var inul = $("#transferIn").closest(".ui-ddl").find("ul");
				inul.empty();
				if(data=="AA"){
					<#list listGame as game>
						inul.append('<li data="${game.gpname}">${game.fullname}</li>');
			        </#list>
			        //$("#transferIn").attr("data",'${listGame[0].gpname}');
			        //$("#transferIn").text('${listGame[0].fullname}');
			        $("#transferIn").attr("data",'');
			        $("#transferIn").text('请选择转入方');
				}else{
					inul.append('<li data="AA">中心钱包</li>');
					$("#transferIn").attr("data","AA");
					$("#transferIn").text("中心钱包");
				}
				if(data == 'BBIN'){
					if($("#bbin").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#bbin").html());
					}
				}else if(data == 'AA'){
					$("#transferNum").val(accountMoneyCount);
				}else if(data == 'MG'){
					if($("#mgin").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#mgin").html());
					}
				}else if(data == 'AG'){
					if($("#ag").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#ag").html());
					}
				}else if(data == 'AGIN'){
					if($("#agin").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#agin").html());
					}
				}else if(data == 'PT'){
					if($("#pt").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#pt").html());
					}
				}else if(data == 'SA'){
					if($("#sa").html().indexOf("<")>-1){
						$.msg("数据加载异常，请稍候再试！",2);
						$("#transferNum").val(0);
						return;
					}else{
						$("#transferNum").val($("#sa").html());
					}
				}
			});
		});
		
		function activeGame(type,target){
		    if($("#"+target).hasClass("_click")) return;
		    $("#"+target).addClass("_click");
		    $("#"+target).text('正在激活...');
			$.ajax({
					type : "POST",
					url : actionPath+"game/activeGame.do?timeStamp="+new Date().getTime(),
					data : {
						gameType : type
					},
					dataType : "json",
					async : true,
					success : function(data) {
						if (data.code == "1") {
							$.msg(type+"平台账号激活成功，祝您游戏愉快！",1);
							$("#"+type).html('<font color="green">已激活</font>');
						} else if (data.code == "0") {
							$("#"+target).removeClass("_click");
							$("#"+target).text('激活 '+type);
							$.msg(type+"平台账号激活失败，请稍候重试！",2);
						}else if (data.code == "2") {
							$("#"+target).removeClass("_click");
							$("#"+target).text('激活 '+type);
							$.msg(data.msg,2);
						}
					},
					error: function(){
						$.msg('网络异常，请稍后再试！',2);
						$("#"+target).removeClass("_click");
						$("#"+target).text('激活 '+type);
					}
				});
		}
    </script>
</head>

<body>
    <div class="title size-bigger">我要转账</div>
    <div class="main">
        <div class="user-layer ui-mt30" id="transfer_sq">
            <table class="ui-table ui-alignCenter">
                <colgroup>
                    <col width="50%" />
                    <col />
                </colgroup>
                <tr>
                    <th>游戏平台</th>
                    <th>钱包余额</th>
                    <th>登录前缀</th>
                    <th>激活帐号</th>
                </tr>
                <#if gameAccountList??>
              	<#list gameAccountList as game>
	              <#assign gameId="ag" />
	              <#if game.uname =='AG'>
	              	<#assign gameId="ag" />
	              <#elseif game.uname =='AGIN'>
	             	 <#assign gameId="agin" />
	              <#elseif game.uname =='MG'>
	              	<#assign gameId="mgin" />
	              <#elseif game.uname =='BBIN'>
	              	<#assign gameId="bbin" />
	              <#elseif game.uname =='SA'>
	              	<#assign gameId="sa" />
	              <#else>
	              	<#assign gameId="pt" />
	              </#if>
		            <tr >
		              <td >${game.fullname}<#if gameId =='agin'><font color='red'>(AG捕鱼)</font></#if></td>
		              <td id="${gameId}"><img src="${zy_path}images/s-loading.gif" /></td>
		              <#if gameId =='pt'>
		              	<td><font color='red'>QB7</font></td>
		              	<td id="PT"><#if transferUser.platforms?lower_case?index_of('pt') !=-1><font color='green'>已激活</font><#else><a href="javascript:activeGame('PT','ptbtn');" id="ptbtn">激活 PT</a></#if></td>
		              <#elseif gameId =='sa'>
		              	<td><font color='red'>无需添加</font></td>
		              	<td id="SA"><#if transferUser.platforms?lower_case?index_of('sa') !=-1><font color='green'>已激活</font><#else><a href="javascript:activeGame('SA','sabtn');" id="sabtn">激活 SA</a></#if></td>
		              <#elseif gameId =='bbin'>
		              	<td><font color='red'>qb7</font></td>
		              	<td id="BBIN"><#if transferUser.platforms?lower_case?index_of('bbin') !=-1><font color='green'>已激活</font><#else><a href="javascript:activeGame('BBIN','bbinbtn');" id="bbinbtn">激活 BBIN</a></#if></td>
		              <#elseif gameId =='mgin'>
		              	<td><font color='red'>QB7</font></td>
		              	<td id="MG"><#if transferUser.platforms?lower_case?index_of('mg') !=-1><font color='green'>已激活</font><#else><a href="javascript:activeGame('MG','mgbtn');" id="mgbtn">激活 MG</a></#if></td>
		              <#elseif gameId =='agin'>
		              	<td><font color='red'>QB</font></td>
		              	<td id="AGIN"><#if transferUser.platforms?lower_case?index_of('agin') !=-1><font color='green'>已激活</font><#else><a href="javascript:activeGame('AGIN','agbtn');" id="agbtn">激活 AG</a></#if></td>
		              </#if>
		            </tr>
	             </#list>
              <#else>
              	<tr>
              		<td colspan="2">您暂时还没有游戏记录!</td>
            	</tr>
              </#if>
            </table>

            <form class="ui-mt20" action="javascript:;">
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 转出：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="transferOut">请选择转出方</div>
                            <span class="fl"></span>
                            <ul class="ui-radius">
                            	<li data="">请选择转出方</li>
                                <li data="AA">中心钱包</li>
				                <#list listGame as game>
				                	<li data="${game.gpname}">${game.fullname}</li>
				                </#list>
                            </ul>
                        </div><span class="size-small">&emsp;转出的账户。</span>
                    </label>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 转入：</span>
                        <div class="ui-ddl fl size-small color-gray ui-radius">
                            <div class="input fl ui-hand ui-textOverflow" data="" id="transferIn">请选择转入方</div>
                            <span class="fl"></span>
                            <ul class="ui-radius">
                            	<li data="">请选择转入方</li>
                            	<li data="AA">中心钱包</li>
                                <#list listGame as game>
	                                <li data="${game.gpname}">${game.fullname}</li>
				                </#list>
                            </ul>
                        </div><span class="size-small">&emsp;转入账户。</span>
                    </label>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 钱包余额：</span>
                        <span >${accountMoneyCount!}</span>
                    </label>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 转出金额：</span>
                        <input class="fl size-small short" type="text" value="${accountMoneyCount!}" id="transferNum" maxlength="10" placeholder="请输入转出金额" /><span class="size-small">请输入转出金额</span>
                    </label>
                    <p id="transferNum_tip" class="size-tiny error ui-none short">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input id="vcode" maxlength="4" class="verifyCode fl size-small short" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode2();"><img class="ui-block" src="${action_path}validationCode/agentcode.do" id="imgCodeAgent" /></a><span class="size-small">请输入验证码。</span>
                    </label>
                    <p id="vcode_tip" class="size-tiny error ui-none short">验证通过</p>
                </div>
                <input type="hidden" id="agBill" value="${agBill}" />
                <input type="hidden" id="aginBill" value="${aginBill}" />
                <input type="hidden" id="bbinBill" value="${bbinBill}" />
                <input type="hidden" id="ptBill" value="${ptBill}" />
                <input type="hidden" id="mgBill" value="${mgBill}" />
                <input type="hidden" id="saBill" value="${saBill}" />
                <input type="button" class="submit ui-btn ui-block size-normal ui-radius color-white bg-orange bg-brownHover ui-transition short" id="transfer_sub" value="确认转账" />
            </form>
            
        </div>
       
    </div>

</body>

</html>