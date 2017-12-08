<div class="nav-user">
    <div class="container">
        <div>
            <a class="logo" href="/"><img class="logo-website" src="${action_path}images/logo.gif"></a>
            <#if userInfo??>
             	<div class="user-top-info">
                    <p class="mb"><i class="iconfont icon-user-2"></i><em>用户:&nbsp;</em>${userInfo.account}</p>
					<p class="mb"><i class="iconfont icon-user-3"></i><em>余额:&nbsp;</em>${accountMoneyCount}元</p>
                    <!--<p class="mb"><i class="iconfont icon-up"></i> <em>等级:&nbsp;
                    	<#if userInfo.grade == 1>
							新会员
						</#if>
						<#if userInfo.grade == 2>
							星级(VIP)
						</#if>
						<#if userInfo.grade == 3>
							银卡(VIP)
						</#if>
						<#if userInfo.grade == 4>
							金卡(VIP)
						</#if>
						<#if userInfo.grade == 5>
							钻石(VIP)）
						</#if>
						<#if userInfo.grade == 6>
							黑卡(VIP)
						</#if>
                    	</em>
                    </p>-->
                </div>
            	 <div class="user-action">
                    <a href="/lxwm/about.html" treeabbr="accIndex">
                        <img class="" src="/images/index/lb.png" style="margin-bottom:8px;"/>
                        <span style="color: #fff;background: #a40000;border-radius: 20px;padding: 0 4px;">${unReadCount}</span>
                        <div class="u-tit">站内信</div>
                    </a>
                    <a href="javascript:forwardTo('capDeposit')" treeabbr="capDeposit">
                        <i class="iconfont icon-rmb"></i>
                        <div class="u-tit">存款</div>
                    </a>
                    <a href="javascript:forwardTo('capWithdraw')" treeabbr="capWithdraw">
                        <i class="iconfont icon-form-2"></i>
                        <div class="u-tit">提款</div>
                    </a>
                    <a href="javascript:forwardTo('capTransfer')" treeabbr="capTransfer">
                        <i class="iconfont icon-return"></i>
                        <div class="u-tit">转账</div>
                    </a>
                    <a href="javascript:forwardTo('accIndex')" treeabbr="accIndex">
                        <i class="iconfont icon-user"></i>
                        <div class="u-tit">账户管理</div>
                    </a>
                    <a href="/signOut.do">
                        <i class="iconfont icon-right-arrows"></i>
                        <div class="u-tit">安全退出</div>
                    </a>
                </div>
            <#else>
             <form class="fr top-reg">
                    <div class="i-item">
                        <i class="iconfont icon-user-2"></i>
                        <input type="text" name="loginname" id="loginname" maxlength="20" autocomplete="off" placeholder="用户名">
                    </div>
                    <div class="i-item">
                        <i class="iconfont icon-user-2 icon-password"></i>
                        <input type="password" name="password" id="password" value="" autocomplete="off"  maxlength="20" placeholder="请输入密码">
                    </div>
                    <div class="i-item two">
                        <input type="text" name="code" id="validationCode" maxlength="4" autocomplete="off" placeholder="验证码">
                         <a class="fr" href="javascript:imgCode();">
		               		<img id="imgCode" src="/validationCode/pcrimg.do" class="img-code">
		                </a>
                    </div>
                    <input type="button" value="登录" class="btn btn-login" id="loginBtn">
                    <input type="button" value="注册" class="btn btn-reg" id="regBtn" onclick="index.register();">
                    <a style="margin-top: 11px;float: left;margin-left: 5px;" class="c-blue" href="javascript:;" data-toggle="modal" data-target="#j-modal-forgetpwd" id="getpwd">忘记密码</a>
                </form>
            </#if>
        </div>
    </div>
</div>

 <#include "${action_path}common/nav.ftl" />
<script src="${action_path}js/jquery.js"></script>
<script src="${action_path}js/jquery.cookie.js"></script>
<script src="${action_path}js/login.js?v=1.1"></script>
<script src="${action_path}js/base.js"></script>
<script src="${action_path}js/main.js"></script>
<script src="${action_path}static/js/public/utils.js"></script>
<script src="${action_path}static/js/public/common.js"></script>
<script src="${action_path}js/forgetpwd.js"></script>