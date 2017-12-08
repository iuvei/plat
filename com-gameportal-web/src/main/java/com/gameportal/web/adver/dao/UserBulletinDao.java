package com.gameportal.web.adver.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.adver.model.UserBulletin;
import com.gameportal.web.user.dao.BaseIbatisDAO;
/**
 * 公告浏览记录。
 * @author Administrator
 *
 */
@Repository
@SuppressWarnings("unchecked")
public class UserBulletinDao extends BaseIbatisDAO{

	@Override
	public Class<UserBulletin> getEntityClass() {
		return UserBulletin.class;
	}
	
	public List<UserBulletin> getAll(Map<String, Object> map){
		return queryForList(map);
	}
}
