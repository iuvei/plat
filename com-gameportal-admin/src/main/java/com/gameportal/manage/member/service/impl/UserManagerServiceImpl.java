package com.gameportal.manage.member.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.UserManagerDao;
import com.gameportal.manage.member.model.UserManager;
import com.gameportal.manage.member.service.IUserManagerService;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.system.dao.SystemUserDao;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.xima.model.MemberXimaMain;

@SuppressWarnings("all")
@Service("userManagerService")
public class UserManagerServiceImpl implements IUserManagerService{

	@Resource(name = "userManagerDao")
	private UserManagerDao userManagerDao;
	
	@Resource(name="systemUserDao")
	private SystemUserDao systemUserDao;
	
	@Override
	public UserManager getObject(Map<String, Object> params) {
		return userManagerDao.getObject(params);
	}
	
	@Override
	public UserManager getUManager(Map<String, Object> params){
		return userManagerDao.getUManager(params);
	}

	@Override
	public boolean save(UserManager entity) {
		return userManagerDao.save(entity);
	}

	@Override
	public boolean update(UserManager entity) {
		return userManagerDao.update(entity);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		return userManagerDao.getRecordCount(params);
	}

	@Override
	public List<UserManager> getList(Map<String, Object> params, int thisPage,
			int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return userManagerDao.getList(params);
	}

	@Override
	public boolean delete(Long uiid) {
		return userManagerDao.delete(uiid);
	}

	@Override
	public List<Map<String, Object>> getDXList(Map<String, Object> params) {
		return userManagerDao.getDXList(params);
	}
	
	@Override
	public List<PayOrder> getPayOrderList(Map<String, Object> params, int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return userManagerDao.getDXPayOrderList(params);
	}
	
	@Override
	public Long getPayOrderCount(Map<String, Object> params) {
		return userManagerDao.getDXPayOrderCount(params);
	}
	
	
	@Override
	public List<MemberXimaMain> getXimaList(Map<String, Object> params, int thisPage, int pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(thisPage))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			thisPage = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return userManagerDao.getDXMemeberXimaList(params);
	}
	
	@Override
	public Long getXimaCount(Map<String, Object> params) {
		return userManagerDao.getDXMemeberXimaCount(params);
	}

	@Override
	public boolean saveOrUpdateSystemUser(SystemUser systemUser) {
		return systemUserDao.saveOrUpdate(systemUser);
	}
}
