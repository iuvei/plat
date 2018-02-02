package com.na.user.socketserver.command.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.na.user.socketserver.command.flow.user.User;
import com.na.user.socketserver.common.enums.DealerUserTypeEnum;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.LiveUserTypeEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;

@Component
public class UserClassHandler {
	
	@Autowired
	private User playerUser;
	@Autowired
	private User dealerUser;
	@Autowired
	private User dealerManagerUser;
	
	public User user(UserPO userPO) {
		if(userPO == null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		if(UserTypeEnum.REAL == userPO.getUserTypeEnum()) {
			LiveUserPO liveUserPO = (LiveUserPO)userPO;
			if(LiveUserTypeEnum.PLAYER == liveUserPO.getTypeEnum()) {
				return playerUser;
			} else {
				throw SocketException.createError("user.type.not.allow.login");
			}
		} else if (UserTypeEnum.DEALER == userPO.getUserTypeEnum()) {
			DealerUserPO dealerUserPO = (DealerUserPO)userPO;
			if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
				return dealerUser;
			} else if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
				return dealerManagerUser;
			} else {
				throw SocketException.createError("user.type.not.allow.login");
			}
		} else {
			throw SocketException.createError("user.type.not.allow.login");
		}
	}
}
