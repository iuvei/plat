package com.gameportal.manage.member.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.UserXimaSetDao;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.member.service.IUserXimaSetService;

/**
 * 
 * @author Administrator
 *
 */
@Service("userXimaSetService")
public class UserXimaSetServiceImpl implements IUserXimaSetService{
	
	@Resource(name = "userXimaSetDao")
	private UserXimaSetDao userXimaSetDao;

	@Override
	public boolean update(UserXimaSet entity) {
		return userXimaSetDao.update(entity);
	}

	@Override
	public boolean save(UserXimaSet entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(userXimaSetDao.save(entity))) ? true: false;
	}

	@Override
	public List<UserXimaSet> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return userXimaSetDao.getList(params);
	}

	@Override
	public Long count(Map<String, Object> params) {
		return userXimaSetDao.getRecordCount(params);
	}

	@Override
	public UserXimaSet getObject(Map<String, Object> params) {
		return userXimaSetDao.getObject(params);
	}

	@Override
	public boolean delete(Long xmid) {
		return userXimaSetDao.delete(xmid);
	}

}
