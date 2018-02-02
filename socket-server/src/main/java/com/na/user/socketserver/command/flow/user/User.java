package com.na.user.socketserver.command.flow.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.LoginInfoPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.des.EncryptKit;

/**
 * 用户处理流程抽象类
 * 
 * @author alan
 * @date 2017年7月31日 下午5:53:16
 */
public abstract class User {
	
	@Autowired
	protected IUserService userService;
	
	@Autowired
	protected BaccaratClassHandler baccaratClassHandler;
	
	@Autowired
	protected UserCommand userCommand;
	
	public abstract void login(LoginInfoPara params, SocketIOClient client, LoginInfoResponse response);
	
	public abstract void logout(CommandReqestPara commandReqestPara, SocketIOClient client);
	
	public abstract void reconnect(SocketIOClient client);
	
	/**
	 * 生成Token
	 * @return
	 */
	protected String generateToken(Long userId) {
		StringBuffer src = new StringBuffer("@@");
		src.append(userId + "@");
		String token = EncryptKit.encode(src.toString());
		return token;
	}
	
}
