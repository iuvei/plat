package com.gameportal.manage.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.model.UserInfoRemark;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.dao.CardPackageDao;
import com.gameportal.manage.user.dao.UserInfoDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.CardPackage;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.PropertyContext;
import com.gameportal.manage.util.WebConstants;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements IUserInfoService {
	private static final Logger logger = Logger
			.getLogger(UserInfoServiceImpl.class);
	@Resource
	private UserInfoDao userInfoDao = null;
	@Resource
	private CardPackageDao cardPackageDao = null;
	
	@Resource
	private AccountMoneyDao accountMoneyDao = null;

	public UserInfoServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean saveOrUpdateUserInfo(UserInfo userInfo) {
		return userInfoDao.saveOrUpdate(userInfo);
	}

	@Override
	public boolean deleteUserInfo(Long uiid) {
		return userInfoDao.delete(uiid);
	}

	@Override
	public boolean updateStatus(Long uiid, Integer status) {
		Map params = new HashMap();
		if (StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
			params.put("uiid", uiid);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			params.put("status", status);
		}
		return userInfoDao.updateStatus(params);
	}

	@Override
	public boolean resetPwd(Integer type, Long userId) {
		String defaultPassword = PropertyContext.PropertyContextFactory(
				"qiantai.properties").getValue("userinfo.default.password");
		if (StringUtils.isBlank(defaultPassword)) {
			defaultPassword = "888888";
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		UserInfo userInfo = (UserInfo) userInfoDao.findById(userId);
		if (null != userInfo) {
			if (1 == type) {
				userInfo.setPasswd(bf.encryptString(defaultPassword));
			} else if (2 == type) {
				userInfo.setAtmpasswd(bf.encryptString(defaultPassword));
			}
			userInfo.setUpdateDate(new Date());
			return userInfoDao.update(userInfo);
		}
		return false;
	}

	@Override
	public Long queryMemberCount(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(account))) {
			params.put("account", account);
			// params.put("uname", key);
			// params.put("identitycard", key);
			// params.put("phone", key);
			// params.put("email", key);
			// params.put("qq", key);
		}
		params.put("accounttype", 0); // 0 普通用户 1代理用户
		return userInfoDao.getRecordCount(params);
	}

	@Override
	public List<UserInfo> queryMember(String account, Integer startNo,
			Integer pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(ObjectUtils.toString(account))) {
			params.put("account", account);
			// params.put("uname", key);
			// params.put("identitycard", key);
			// params.put("phone", key);
			// params.put("email", key);
			// params.put("qq", key);
		}
		params.put("accounttype", 0); // 0 普通用户 1代理用户
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return userInfoDao.queryForPager(params, startNo, pageSize);
	}

	@Override
	public Long queryUserinfoCount(Map<String, Object> params) {
		return userInfoDao.getRecordCount(params);
	}

	@Override
	public List<UserInfo> queryUserinfo(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		return userInfoDao.queryForPager(params, startNo, pageSize);
	}

	@Override
	public Long queryAboveCount(Map<String, Object> params) {
		return userInfoDao.getRecordCount(params);
	}

	@Override
	public List<UserInfo> queryAbove(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		return userInfoDao.queryAbove(params, startNo, pageSize);
	}

	@Override
	public UserInfo queryById(Long uiid) {
		return (UserInfo) userInfoDao.findById(uiid);
	}

	@Override
	public CardPackage queryCardPackage(Long uiid, Integer status) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != uiid) {
			params.put("uiid", uiid);
		}
		if (null != status) {
			params.put("status", status);
		}
		List<CardPackage> cpList = cardPackageDao.queryForPager(params, 0, 0);
		if (null != cpList && cpList.size() > 0) {
			return cpList.get(0);
		}
		return null;
	}

	@Override
	public UserInfo queryUserInfo2(Map<String, Object> params) {
		List<UserInfo> list = userInfoDao.queryForPager(params, 0, 1);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveRemark(UserInfoRemark remark) {
		userInfoDao.saveRemark(remark);
	}

	@Override
	public List<UserInfoRemark> getRemarkList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		return userInfoDao.getRemarkList(map, startNo, pageSize);
	}

	@Override
	public boolean updateTotalamount(AccountMoney am) {
		return accountMoneyDao.updateTotalamount(am);
	}
}
