package com.na.manager.facade;

import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserStatus;

/**
 * @author andy
 * @date 2017年7月3日 下午3:07:17
 * 
 */
public interface ILiveUserFacade {
	
	void modifyStatus(Long id, LiveUserType liveUserType, UserStatus userStatus);
	
	void modifyBetStatus(Long id, LiveUserType liveUserType, LiveUserIsBet isBet);
	
}
