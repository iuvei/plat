<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "${action_path}common/config.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${title} - 游戏维护页面</title>
${meta}
<style type="text/css">
        body, table, font {
            font-size: 12px;
            line-height: 20px;
            font-family: 'Microsoft YaHei','Hiragino Sans GB',Helvetica,Arial,'Lucida Grande',sans-serif;
            -webkit-text-size-adjust: none;
            _font-family: 'Microsoft YaHei','Hiragino Sans GB',Helvetica,Arial,'Lucida Grande',sans-serif;
        }
        body {
            background: #cccccc;
        }
        .div-wh {
            width: 650px;
            height: 350px;
            margin: 0 auto;
            background: url(../images/div-wh-bg.jpg) no-repeat;
            margin-top: 40px;
            padding: 200px 0 0 350px;
        }

        .wh-word {
            width: 650px;
            text-shadow: 0 1px 1px #fff;
        }
		strong, b {
    		font-weight: bold;
		}
        .wh-word .p1 {
            color: #ae0541;
            font-size: 28px;
            line-height: 0;
        }

        .wh-word .s1 {
            font-weight: 600;
            width:580px;
            color: #9e9e9e;
        }

        .wh-word .btn-wh1 {
            width: 120px;
            height: 40px;
            background: url(../images/btn-wh1.jpg);
            display: inline-block;
            vertical-align: middle;
            margin: 0 20px;
            text-align: center;
            line-height: 40px;
            font-size: 16px;
            color: white;
            text-shadow: none;
            text-decoration: none;
        }
        .wh-word .btn-wh2 {
            width: 120px;
            height: 40px;
            background: url(../images/btn-wh2.jpg);
            display: inline-block;
            vertical-align: middle;
            margin: 0 20px;
        }
    </style>
</head>
<body>
    <div class="div-wh">
        <div class="wh-word">
            <p class="p1"><strong><#if gameName=='AGIN'>AG国际厅<#elseif gameName=='BBIN'>BBIN<#elseif gameName == 'MG'>MG娱乐城<#elseif gameName =='PT'>PT电子<#elseif gameName =='NMG'>MG电子<#elseif gameName =='SA'>SA电子</#if>游戏正在维护中，造成不便，敬请谅解！</strong></p>
            <div class="s1">尊敬的玩家您好，<#if gameName=='AGIN'>AG国际厅<#elseif gameName=='BBIN'>BBIN<#elseif gameName == 'MG'>MG娱乐城<#elseif gameName =='PT'>PT电子<#elseif gameName =='NMG'>MG电子<#elseif gameName =='SA'>SA电子</#if>游戏平台进行临时维护，届时您可以去玩平台其它游戏。</div>
            <p style="padding-left:10px;">您可以选择其它平台：
            	<select id="Plat" name="Plat">
                <option value="">==请选择==</option>
				<option value="BBIN">BBIN游戏</option>
               	<option value="AGIN">AG国际厅游戏</option>
               	<option value="MG">MG娱乐城游戏</option>
                <option value="PT">PT电子游艺</option>
                <option value="NMG">MG电子游艺</option>
                <option value="SA">SA电子游艺</option>
				</select>
			<a class="btn-wh1" href="javascript:;">进入游戏</a>或者<a class="btn-wh2" href="${zy_path}index.do"></a></p>
        </div>
    </div>
</body>
</html>
<script type="text/javascript" src="${zy_path}js/jquery.js"></script>
<script>
  $(function(){
  	$("#Plat option[value='${gameName}']").attr("disabled",true);
  	if("${gameName}" == 'MG'){
  		$("#Plat option[value='NMG']").attr("disabled",true);
  	}
  	if("${gameName}" == 'NMG'){
  		$("#Plat option[value='MG']").attr("disabled",true);
  	}
  	
  	$(".btn-wh1").click(function(){
  		var sel = $("#Plat").val();
  		if(sel == ''){
  			warn("请选择要进入的游戏平台。");
  			return;
  		}
  		
  		if(sel =='BBIN'){
  			window.open('${zy_path}game/loginGameBBIN.html?ps=1');
  		}else if(sel =='AG'){
  			window.open('${zy_path}game/loginGameAG/AG.do');
  		}else if(sel =='AGIN'){
  			window.open('${zy_path}game/loginGameAG/AGIN.do');
  		}else if(sel =='MG'){
  			window.open('${zy_path}game/loginGameMG/0.do');
  		}else if(sel =='NMG'){
  			window.open('${zy_path}electronic/agElectronic.html')
  		}else if(sel =='PT'){
  			window.open('${zy_path}electronic/index.html');
  		}else if(sel =='SA'){
  			window.open('${zy_path}electronic/saElecGame.html');
  		}
  	});
  });
</script>