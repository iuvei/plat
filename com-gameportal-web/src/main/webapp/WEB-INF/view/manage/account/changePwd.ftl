<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<#include "common/config.ftl">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>钱宝娱乐--帐户中心--修改密码</title>
    <link type="text/css" rel="stylesheet" href="/static/css/public/base.css" />
    <link type="text/css" rel="stylesheet" href="/static/css/user/user.css" />
    <script type="text/javascript" src="/static/lib/jquery.js"></script>
    <script type="text/javascript" src="/static/js/public/utils.js"></script>
    <script type="text/javascript" src="/static/js/user/user.js"></script>
    <script type="text/javascript" language="javascript" src="${zy_path}js/main.js"></script>
    <script src="${zy_path}js/base.js"></script>
</head>

<body>
    <div class="title size-bigger">修改密码</div>
    
    <div class="main">
        <div class="user-layer ui-mt30">
            <form action="javascript:;">
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 密码类型：</span>
                        <div class="ui-ddl fl color-gray ui-radius">
                            <div class="input fl ui-hand ui-textOverflow">登陆密码</div>
                            <span class="fl"></span>
                            <ul class="ui-radius" >
                                <li onclick="convertType(1)" >登陆密码</li>
                                <li onclick="convertType(2)">提款密码</li>
                            </ul>
                        </div>
                    </label>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 旧密码：</span>
                        <input class="fl short" name="oldPwd" id="oldPwd" maxlength="10" type="password" value="" placeholder="请输入当前密码" />请输入当前【6-8】位的密码，只能是英文字母加数字组合
                    </label>
                    <p id="oldPwd_tip" class="size-tiny short error ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 新密码：</span>
                        <input class="fl short" name="newPwd" id="newPwd" maxlength="10" type="password" value="" placeholder="请输入新密码" />请输入【6-8】位的新设密码，只能是英文字母加数字组合
                    </label>
                    <p id="newPwd_tip" class="size-tiny short error ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 确认密码：</span>
                        <input class="fl short" name="newpwdok" id="newpwdok" maxlength="10" type="password" value="" placeholder="请再次输入新密码" />请再次输入【6-8】位的新设密码，只能是英文字母加数字组合
                    </label>
                    <p id="newpwdok_tip" class="size-tiny short error ui-none">验证通过</p>
                </div>
                <div class="ui-inputBox">
                    <label><span class="fl short"><font class="color-orange">*</font> 验证码：</span>
                        <input class="verifyCode fl short" name="vcode" id="vcode" maxlength="4" type="text" value="" placeholder="请输入验证码" />
                        <a class="verifyCode fl" href="javascript:imgCode2();"><img id="imgCodeAgent" class="ui-block" src="${action_path}validationCode/agentcode.do" /></a>请输入验证码。
                    </label>
                    <p id="vcode_tip" class="size-tiny short error ui-none">验证通过</p>
                </div>
                <input type="button" id="submitBtn" class="submit ui-btn ui-block ui-radius color-white bg-orange bg-brownHover ui-transition short" id="submit" value="提交" />
            </form>
        </div>
    </div>

</body>
<script type="text/javascript" >
	var type = 1;
	function convertType(val){
		type = val;
	}
	$(function(){
	
		$("#submitBtn").click(function(){
			var oldPwd = $("#oldPwd").val();
			if(base.valid.isPassWord(oldPwd) == false){
				$("#oldPwd_tip").html('您输入的旧密码格式不正确：【6-8】位的密码。');
				$("#oldPwd_tip").removeClass("ui-none");
				$("#oldPwd").focus();
				return;
			}else{
				$("#oldPwd_tip").addClass("ui-none");
			}
			
			var newPwd = $("#newPwd").val();
			if(base.valid.isPassWord(newPwd) == false){
				$("#newPwd_tip").html('密码格式不正确：【6-8】位的密码。');
				$("#newPwd_tip").removeClass("ui-none");
				$("#newPwd").focus();
				return;
			}else{
				$("#newPwd_tip").addClass("ui-none");
			}
			var newpwdok = $("#newpwdok").val();
			if(base.valid.isPassWord(newpwdok) == false){
				$("#newpwdok_tip").html('确认密码格式不正确：【6-8】位的密码。');
				$("#newpwdok_tip").removeClass("ui-none");
				$("#newpwdok").focus();
				return;
			}else{
				$("#newpwdok_tip").addClass("ui-none");
			}
			if(newPwd != newpwdok){
				$("#newpwdok_tip").html('确认密码与新密码不一致。');
				$("#newpwdok_tip").removeClass("ui-none");
				$("#newpwdok").val("");
				$("#newpwdok").focus();
				return;
			}else{
				$("#newpwdok_tip").addClass("ui-none");
			}
			var vcode = $("#vcode").val();
			if(vcode.length != 4){
				$("#vcode_tip").html('请输入4位数字验证码。');
				$("#vcode_tip").removeClass("ui-none");
				$("#vcode").focus();
				return;
			}else{
				$("#vcode_tip").addClass("ui-none");
			}
			
			$("#submitBtn").val('正在提交...');
			$("#submitBtn").attr('disabled',true);
		
			$.ajax({
				type : "POST",
				url : "${action_path}manage/user/modifyPasswd.do",
				data : {
					oldPwd : oldPwd,
					newPwd : newPwd,
					type : type,
					code : vcode
				},
				dataType : "json",
				async : true,
				success : function(data) {
					if (data.code == "1") {
						$.remind('您的密码已经修改成功！',success_callback);
					} else if (data.code == "0") {
						$.msg(data.info,3);
						$("#submitBtn").val('提 交');
						$("#submitBtn").attr('disabled',false);
					}else if (data.code == "9") {
						$("#submitBtn").val('提 交');
						$("#submitBtn").attr('disabled',false);
						$("#vcode_tip").html(data.info);
						$("#vcode_tip").removeClass("ui-none");
					} else {
						$.msg('网络异常，请稍后再试！',3);
						$("#submitBtn").val('提 交');
						$("#submitBtn").attr('disabled',false);
					}
				}
			})
		});
		
		function success_callback(){
			window.location.reload();
		}
	});
</script>
</html>