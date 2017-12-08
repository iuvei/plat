package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.XimaFlag;

public interface IXimaFlagService {

	public boolean update(XimaFlag entity);
	public boolean save(XimaFlag entity);
	public XimaFlag getObject(Map<String, Object> params);
	public List<XimaFlag> getList(Map<String, Object> params,int thisPage,int pageSize);
	public Long getCount(Map<String, Object> params);
	
	/**
	 * 查询最新洗码状态。
	 * @param uiid
	 * @return
	 */
	public XimaFlag getNewestXimaFlag(long uiid);
}
