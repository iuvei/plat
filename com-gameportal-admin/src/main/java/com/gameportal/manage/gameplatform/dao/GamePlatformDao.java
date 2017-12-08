package com.gameportal.manage.gameplatform.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.sitesettings.model.SiteSettings;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class GamePlatformDao extends BaseIbatisDAO {

	public Class<GamePlatform> getEntityClass() {
		return GamePlatform.class;
	}

	public boolean saveOrUpdate(GamePlatform entity) {
		if (entity.getGpid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<GamePlatform> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
