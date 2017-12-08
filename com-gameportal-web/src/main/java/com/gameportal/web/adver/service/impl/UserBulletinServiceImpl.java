package com.gameportal.web.adver.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.adver.dao.UserBulletinDao;
import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.model.UserBulletin;
import com.gameportal.web.adver.service.IUserBulletinService;

@Service("userBulletinService")
public class UserBulletinServiceImpl implements IUserBulletinService{
	
	@Resource(name="userBulletinDao")
	private UserBulletinDao userBulletinDao;

	@Override
	public void save(UserBulletin entity) {
		userBulletinDao.save(entity);
	}
	
	@Override
	public void update(UserBulletin entity) {
		userBulletinDao.update(entity);
	}

	@Override
	public List<UserBulletin> getAll(Map<String, Object> map) {
		return userBulletinDao.getAll(map);
	}

	@Override
	public boolean isRead(Bulletin curBulletin, UserBulletin bulletin) {
		if(bulletin.getBid().indexOf(String.valueOf(curBulletin.getId()))!=-1){
			return true;
		}
		return false;
	}
}
