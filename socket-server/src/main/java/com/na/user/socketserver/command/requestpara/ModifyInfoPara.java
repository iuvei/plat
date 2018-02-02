package com.na.user.socketserver.command.requestpara;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 修改资料请求参数
 * 
 * @author alan
 * @date 2017年6月5日 上午11:22:52
 */
public class ModifyInfoPara extends CommandReqestPara {
	
	/**
	 * 昵称
	 */
    @JSONField(name = "nickName")
    private String nickName;
    
	/**
	 * 旧密码
	 */
    @JSONField(name = "oldPassword")
    private String oldPassword;
    
    /**
	 * 新密码
	 */
    @JSONField(name = "newPassword")
    private String newPassword;
    
    /**
	 * 头像
	 */
    @JSONField(name = "headPic")
    private String headPic;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
    
    
}
