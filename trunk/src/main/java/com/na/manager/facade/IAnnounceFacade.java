package com.na.manager.facade;

import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.UserAnnounce;

/**
 * @author andy
 * @date 2017年7月3日 下午3:55:15
 * 
 */
public interface IAnnounceFacade {
	void insertAnnounceConent(AnnounceContent announceContent);
	
	void updateUserAnnounce(UserAnnounce userAnnounce);

    void delete(Long id);
}
