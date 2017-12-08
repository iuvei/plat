<!--头部-->
<header>
	<div class="header_left f18">
		<span onclick=window.location.href='${action_path}indexmp.html'>首  页</span>
	</div>
	<#if userInfo??>
		<div class="header_right f14">
			 ${userInfo.account} | <span id="layout" onclick="window.location.href='${action_path}loginOutmp.html'">退出</span>
		</div>
	<#else>
		<div class="header_right f14">
			 <span onclick="window.location.href='${action_path}loginmp.html'" >登录</span>  |  <span onclick="window.location.href='${action_path}registermp.html'">注册</span>  <!--|  <span class="sw">试玩--></span>
		</div>
	</#if>
</header>