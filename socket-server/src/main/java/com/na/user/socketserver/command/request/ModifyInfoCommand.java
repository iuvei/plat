package com.na.user.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.ModifyInfoPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.Md5Util;

/**
 * 修改资料编辑
 */
@Cmd(paraCls = ModifyInfoPara.class,name = "修改资料编辑")
@Component
public class ModifyInfoCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(ModifyInfoCommand.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserCommand userCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		ModifyInfoPara param = (ModifyInfoPara) commandReqestPara;
		
		if(param == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		
		UserPO userPO = AppCache.getUserByClient(client);
		
		if(!StringUtils.isEmpty(param.getOldPassword())) {
			String oldPassword = Md5Util.digest("MD5", param.getOldPassword().getBytes(), userPO.getPasswordSalt().getBytes(), 3);
			if(oldPassword.equals(userPO.getPassword())) {
				if(!StringUtils.isEmpty(param.getNewPassword())) {
					if(param.getNewPassword().length() >= 6) {
						userPO.setPassword(Md5Util.digest("MD5", param.getNewPassword().getBytes(), userPO.getPasswordSalt().getBytes(), 3));
					} else {
						throw SocketException.createError("password.min.length");
					}
				} else {
					throw SocketException.createError("newpassword.not.allow.empty");
				}
			} else {
				throw SocketException.createError("oldpassword.error");
			}
		}
		
		if(!StringUtils.isEmpty(param.getNickName())) {
			if(param.getNickName().length() > 10) {
				userPO.setNickName(param.getNickName());
			} else {
				throw SocketException.createError("nickname.max.length");
			}
		}
		
		if(!StringUtils.isEmpty(param.getHeadPic())) {
			userPO.setHeadPic(param.getHeadPic());
		}
		
		try {
			userService.modifyUserInfo(userPO);
			
			userCommand.send(client, RequestCommandEnum.CLIENT_MODIFY_INFO, null);
			return true;
		} catch (Exception e) {
			log.error(null, e);
			throw SocketException.createError("user.modify.data.error");
		}
	}
	
}
