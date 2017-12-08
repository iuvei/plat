package com.gameportal.web.adver.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.adver.dao.BulletinDao;
import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.service.IBulletinService;
/**
 * 公告业务类。
 * @author sum
 *
 */
@Service("bulletinServiceImpl")
@SuppressWarnings("all")
public class BulletinServiceImpl implements IBulletinService {
	@Resource(name="bulletinDao")
	private BulletinDao bulletinDao;

	@Override
	public List<Bulletin> queryAllBulletin(Map<String, Object> values) {
		return bulletinDao.queryForList(values);
	}
	
	@Override
	public void saveBulletin(Bulletin bulletin) {
		bulletinDao.saveBulletin(bulletin);
	}
	
	@Override
	public void updateBulletin(Bulletin bulletin) {
		bulletinDao.updateBulletin(bulletin);
	}
}
