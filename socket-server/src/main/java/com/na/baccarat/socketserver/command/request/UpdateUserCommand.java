package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.UpdateUserResponse;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.AppException;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 用户余额变动推送
 * 
 * @author Administrator
 */
@Cmd(paraCls = CommandReqestPara.class, name = "用户余额变动推送")
@Component
public class UpdateUserCommand implements ICommand {

	@Autowired
	private IUserService userService;

	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		return exe(client, commandReqestPara);
	}

	private boolean exe(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		//从currentLoginUserMap中获取用户信息
		User user = BaccaratCache.getUserByClient(client);
		if (user == null)
			throw AppException.createError("user.not.exist");
		
		UserPO newSqlUser = userService.getUserById(user.getUserPO().getId());
		
		//更新AppCache中的用户数据
		user.getUserPO().setBalance(newSqlUser.getBalance());
		
		UpdateUserResponse _response = new UpdateUserResponse();
		CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_UPDATE_USER_INFO, _response);
		SocketUtil.send(client, ResponseCommandEnum.OK,response);
		
		return true;
	}

}
