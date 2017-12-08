package com.gameportal.web.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.order.dao.CCAndGroupDao;
import com.gameportal.web.order.dao.CCGroupxAndUserDao;
import com.gameportal.web.order.dao.CompanyCardDao;
import com.gameportal.web.order.model.CompanyCard;
import com.gameportal.web.order.service.ICompanyCardService;
import com.gameportal.web.user.dao.UserInfoDao;
import com.gameportal.web.user.model.UserInfo;

@Service("companyCardServiceImpl")
public class CompanyCardServiceImpl implements ICompanyCardService {
	private static final Logger logger = Logger.getLogger(CompanyCardServiceImpl.class);

	@Resource
	private CompanyCardDao companyCardDao = null;
	@Resource
	private CCAndGroupDao cCAndGroupDao = null;
	@Resource
	private CCGroupxAndUserDao cCGroupxAndUserDao = null;
	@Resource
	private UserInfoDao userInfoDao = null;

	public CompanyCardServiceImpl() {
		super();
	}

	@Override
	public Long queryCompanyCardCount(Map<String, Object> params) {
		return companyCardDao.getRecordCount(params);
	}

	@Override
	public List<CompanyCard> queryCompanyCard(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 0;
		}
		return companyCardDao.queryForPager(params, startNo, pageSize);

	}

	@Override
	public List<CompanyCard> queryByCcgid(Long ccgid) {
		return companyCardDao.queryByCcgid(ccgid);
	}

	@Override
	public List<CompanyCard> queryByUuid(Long uiid) {
		List<CompanyCard> ccList = null;
		// 优先查询自定义分组
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", uiid);
		ccList = companyCardDao.queryByUiidInCcgx(params);
		if (null == ccList || ccList.size() <= 0) {
			// 根据该会员的星级查询对应关联公司银行卡
			UserInfo user = (UserInfo) userInfoDao.findById(uiid);
			if (null != user && null != user.getGrade()) {
				ccList = companyCardDao.queryByGrade(user.getGrade());
			}
		}
		return ccList;
	}

	@Override
	public CompanyCard getCompanyCard(long ccid) {
		return companyCardDao.getCompanyCard(ccid);
	}

	@Override
	public List<CompanyCard> queryCompanyCard(Map<String, Object> params) {
		return companyCardDao.queryCompanyCard(params);
	}
}
