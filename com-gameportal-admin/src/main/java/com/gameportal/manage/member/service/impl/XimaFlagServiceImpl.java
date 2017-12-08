package com.gameportal.manage.member.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.XimaFlagDao;
import com.gameportal.manage.member.model.XimaFlag;
import com.gameportal.manage.member.service.IXimaFlagService;

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
	
	@Override
	public XimaFlag getNewestXimaFlag(long uiid) {
		Map<String, Object> map = new HashMap<>();
		map.put("flaguiid", uiid);
		map.put("sortColumns", "updatetime desc");
		List<XimaFlag> list =ximaFlagDao.getList(map);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

}
