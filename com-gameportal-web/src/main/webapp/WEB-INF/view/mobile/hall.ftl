<!DOCTYPE html>
<html>
<head>
	<#include "/common/config.ftl">
	${meta}
<title>游戏开启中</title>
</head>
<body>
<script type="text/javascript" src="https://login.luckydragon88.com/jswrapper/integration.js.php?casino=greatfortune88"></script>
<script type="text/javascript">
iapiSetCallout('Login', calloutLogin);
function login(realMode) {
	iapiSetClientPlatform("mobile&deliveryPlatform=HTML5");
	iapiLogin('${uaccount}', '${upwd}', realMode, "zh-cn");
}

function calloutLogin(response) {
    if (response.errorCode) {
          if(response.playerMessage) {
              alert("(1) 游戏开启失败\n" + response.playerMessage);
          } else {
              alert("(2) 游戏开启失败\n" + response.actions.PlayerActionShowMessage[0].message.replace(/\\r/g,''));
          }
          window.close();
      } else {
    	  iapiSetCallout('GetTemporaryAuthenticationToken', calloutGetTemporaryAuthenticationToken);
    	  iapiRequestTemporaryToken(1, '424', 'GamePlay');
    	
      }
  }


function calloutGetTemporaryAuthenticationToken(response) {
	if (response.errorCode) {
		alert("Token failed. " + response.playerMessage + " Error code: " + response.errorCode);
	}else{
  	  window.location.href = 'http://hub.ld176888.com/igaming/?gameId=${gameName}&real=1&username=${uaccount}&lang=zh-cn&tempToken='+response.sessionToken.sessionToken+'&lobby='+encodeURIComponent('http://'+window.location.host+'/gameListmp.html')+'&support=&logout='+encodeURIComponent('http://'+window.location.host+'/mobile/logoutptmp.html?username=${uaccount}');
	}
}
login(1);
</script>
    游戏开启中 ...
</body>
</html>