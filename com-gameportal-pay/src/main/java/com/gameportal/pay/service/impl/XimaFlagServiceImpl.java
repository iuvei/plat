package com.gameportal.pay.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.pay.dao.XimaFlagDao;
import com.gameportal.pay.service.IXimaFlagService;
import com.gameportal.web.user.model.XimaFlag;

@Service("ximaFlagService")
public class XimaFlagServiceImpl implements IXimaFlagService{

	@Resource(name = "ximaFlagDao")
	private XimaFlagDao ximaFlagDao;
	@Override
	public boolean update(XimaFlag entity) {
		return ximaFlagDao.update(entity);
	}

	@Override
	public boolean save(XimaFlag entity) {
		return ximaFlagDao.save(entity);
	}

	@Override
	public XimaFlag getObject(Map<String, Object> params) {
		return ximaFlagDao.getObject(params);
	}

	@Override
	public List<XimaFlag> getList(Map<String, Object> params,int thisPage,int pageSize) {
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return ximaFlagDao.getList(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		return ximaFlagDao.getRecordCount(params);
	}

}
