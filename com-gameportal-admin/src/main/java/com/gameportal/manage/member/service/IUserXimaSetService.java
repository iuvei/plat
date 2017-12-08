package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.UserXimaSet;

public interface IUserXimaSetService {

	public boolean update(UserXimaSet entity);
	
	public boolean save(UserXimaSet entity);
	
	public List<UserXimaSet> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	public Long count(Map<String, Object> params);
	
	public UserXimaSet getObject(Map<String, Object> params);
	
	/**
	 * 删除数据
	 * @param xmid
	 * @return
	 */
	public boolean delete(Long xmid);
}
