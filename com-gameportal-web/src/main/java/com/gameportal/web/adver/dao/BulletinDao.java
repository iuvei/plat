package com.gameportal.web.adver.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
@SuppressWarnings("all")
public class BulletinDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return Bulletin.class;
	}
	
	public List<Bulletin> queryAllBulletin(Map<String, Object> values){
		return (List<Bulletin>)queryForList(values);
	}
	
	public void saveBulletin(Bulletin entity){
		save(entity);
	}
	public void updateBulletin(Bulletin entity){
		update(entity);
	}
}
