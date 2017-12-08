<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<#include "common/config.ftl">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>游戏开启中</title>
</head>
<body>

    <script type="text/javascript" src="http://cache.download.banner.greatfortune88.com/integrationjs.php"></script>
    <script type="text/javascript">
        iapiSetCallout('Login', calloutLogin);
        iapiSetCallout('Logout', calloutLogout);
        function login(realMode) {
            iapiLogin('${uaccount}', '${upwd}', realMode, "ch");
        }
        function logout(allSessions, realMode) {  
		  	iapiLogout(allSessions, realMode);
		}
        function calloutLogin(response) {
          // errorCode=6 错误在登录时可能会发生，但游戏可以正常进入。 可以忽略
		  var code = response.errorCode;
          if (code && code != 6) {
                alert("Login failed, " + code);
                window.close();
            } else {
                window.location.href="${action_path}electronic/GamePlay.html?name=${gameName}";
            }
        }
        login(1);
        function calloutLogout(response) {
		 if (response.errorCode) {
		 	alert("Logout failed, " + response.errorText);
		 }
		 else {
		 	alert("Logout OK"); 
		 }
		}
    </script>
    游戏加载中，请稍后...
</body>
</html>
