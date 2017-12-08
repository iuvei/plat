package com.gameportal.manage.betlog.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;


import com.gameportal.manage.system.dao.BaseIbatisDAO;

import com.gameportal.manage.betlog.model.GameInfo;


@Component
public class GameInfoDao extends BaseIbatisDAO{

	public Class<GameInfo> getEntityClass() {
		return GameInfo.class;
	}
	
	public boolean saveOrUpdate(GameInfo entity) {
		if(entity.getGid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

}
