<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "${action_path}common/config.ftl">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${title} - SA真人视讯</title>
	${meta}
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<body onLoad="document.loginform.submit();">

<form method='POST' name='loginform' id='loginform' action='${plat.domainip}'> 
	<input type='hidden' name='username' id='username' value='${loginRequest.displayName}'/> 
	<input type='hidden' name='token' id='token' value='${loginRequest.token}'/>
 	<input type='hidden' name='lobby' id='lobby' value='${plat.ciphercode}' />
 	<input type='hidden' name='lang' id='lang' value='zh_CN'/>
</form>
 </script>
</body>
</html>