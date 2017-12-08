<div id="footer">
 <div class="f-item one" style="height:40px;">
            <a href="${action_path}lxwm/about.html">关于我们</a>
            <a href="${action_path}lxwm/about.html">公司优势</a>
            <a href="${action_path}lxwm/about.html">用户协议</a>
            <a href="${action_path}lxwm/about.html">理性博彩</a>
            <a href="${action_path}lxwm/about.html">隐私保护</a>
            <a href="${action_path}lxwm/about.html">免责申明</a>
        </div>
    <div class="f-content container">
        <div class="ui-row">
            <div class="col-6">
                <div class="f-item first">
                    <h2>推荐浏览器</h2>
                    <div class="bower-link">
                        <a target="_blank" href="https://www.google.com/chrome/browser/desktop/index.html" title="谷歌浏览器"><i class="iconfont icon-chrome"></i></a>
                        <a target="_blank" href="http://www.firefox.com.cn" title="火狐浏览器"><i class="iconfont icon-firefox"></i></a>
                        <a target="_blank" href="http://windows.microsoft.com/zh-CN/internet-explorer/download-ie" title="ie"><i class="iconfont icon-ie"></i></a>
                        <a target="_blank" href="http://www.apple.com/safari" title="safari"><i class="iconfont icon-safari"></i></a>
                    </div>

                </div>
            </div>
            <div class="col-6">
                <div class="f-item">
                    <h2>合作平台</h2>
                    <a href="javascript:;">
                        <img src="/images/partner.png" alt="">
                    </a></div>
            </div>
        </div>
    </div>
    <br/>
    <div class="f-sub">
        	所属产品和服务获得菲律宾政府监管并颁发博彩牌照NO.CF-188
    </div>
</div>
<!--忘记密码-->
<div id="j-modal-forgetpwd" class="modal in" style="display: none;">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-hd">
                <h4 class="modal-title">找回密码</h4>
                <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <div class="modal-bd ui-form">
                <div class="ui-form-item">
                    <label class="ui-label">帐号：</label>
                    <input class="ui-ipt" name="username" type="text" id="account" maxlength="8">
                    <br>
                    <label class="size-small" id="account_tip"></label>
                </div>
                <div class="ui-form-item">
                    <label class="ui-label">手机号码：</label>
                    <input class="ui-ipt" maxlength="11" name="phone" type="text" id="telphone">
                      <br>
                    <label class="size-small" id="telphone_tip"></label>
                </div>
                <div class="ui-form-item" style="width:180px;">
                    <label class="ui-label">验证码：</label>
                    <input class="ui-ipt" maxlength="4" name="phone" type="text" id="vcode" style="width: 120px;position: relative">
                    <a href="javascript:imgCode2();"><img src="/validationCode/agentcode.do" id="imgCodeAgent"  width="46px" height="30px" style="float:right;"></a>
                    <br>
                    <label class="size-small" id="vcode_tip"></label>
                </div>
                <div class="ui-form-item">
                    <input type="button" class="btn btn-primary" value="找回密码" id="LbtnSubmit" onclick="getPwd()">
                </div>
            </div>
        </div>
    </div>
</div>
<script language="javascript" type="text/javascript" src="http://js.users.51.la/18897728.js"></script>
<script>
/**
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-77353446-1', 'auto');
  ga('send', 'pageview');
 */
//禁用右键
function stopFuntion(){
    return false;
}
//document.oncontextmenu=stopFuntion;
</script>