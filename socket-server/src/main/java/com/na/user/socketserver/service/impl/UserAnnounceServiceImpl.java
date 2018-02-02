package com.na.user.socketserver.service.impl;

import com.na.user.socketserver.dao.IAnnounceContentMapper;
import com.na.user.socketserver.dao.IUserAnnounceMapper;
import com.na.user.socketserver.entity.AnnounceContent;
import com.na.user.socketserver.entity.UserAnnounce;
import com.na.user.socketserver.service.IUserAnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏公告
 * @author Administrator
 *
 */
@Service
public class UserAnnounceServiceImpl implements IUserAnnounceService {

	@Autowired
    private IUserAnnounceMapper userAnnounceMapper;
	@Autowired
    private IAnnounceContentMapper announceContentMapper;
	
	@Override
	public UserAnnounce getUserAnnouceById(Long announceId) {
		UserAnnounce userAnnounce = userAnnounceMapper.getUserAnnouceById(announceId);
		if(userAnnounce != null && userAnnounce.getContentId() != null){
			AnnounceContent announceContent = announceContentMapper.getAnnounceContentByContentId(userAnnounce.getContentId());
			userAnnounce.setAnnounceContent(announceContent);
		}
		return userAnnounce;
	}

	@Override
	public UserAnnounce getNewAnnounce() {
		UserAnnounce userAnnounce = userAnnounceMapper.getNewAnnounce();
		if(userAnnounce != null && userAnnounce.getContentId() != null){
			AnnounceContent announceContent = announceContentMapper.getAnnounceContentByContentId(userAnnounce.getContentId());
			userAnnounce.setAnnounceContent(announceContent);
		}
		return userAnnounce;
	}

	@Override
	public List<UserAnnounce> getAllUserAnnouce() {
		List<UserAnnounce> userAnnounceList = userAnnounceMapper.getAllUserAnnouce();
		/*for (UserAnnounce userAnnounce:userAnnounceList) {
			if(userAnnounce != null && userAnnounce.getContentId() != null){
				AnnounceContent announceContent = announceContentMapper.getAnnounceContentByContentId(userAnnounce.getContentId());
				userAnnounce.setAnnounceContent(announceContent);
			}
		}*/
		return userAnnounceList;
	}

}
