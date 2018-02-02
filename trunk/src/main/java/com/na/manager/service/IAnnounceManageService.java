package com.na.manager.service;

import java.util.Map;

import com.na.manager.bean.AnnounceSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.AnnounceListVO;
import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.User;
import com.na.manager.entity.UserAnnounce;

/**
 * @author andy
 * @date 2017年6月26日 下午6:37:02
 * 
 */
public interface IAnnounceManageService {
	Page<AnnounceListVO> queryUserAnnouneByPage(AnnounceSearchRequest searchRequest);

	UserAnnounce insertAnnounceConent(AnnounceContent announceContent);
	
	void delete(Long id);
	
	void checkSysUserExist(User user);
	
	void updateAnnounceContent(AnnounceContent announceContent);
	
	void updateUserAnnounce(UserAnnounce UserAnnounce);
	
	Map<String, String> dealUserNameList(String userList);
	
}
