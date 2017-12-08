package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.member.model.GameTransferLog;


@Component
public class GameTransferLogDao extends BaseIbatisDAO{

	public Class<GameTransferLog> getEntityClass() {
		return GameTransferLog.class;
	}
	
	public boolean saveOrUpdate(GameTransferLog entity) {
		if(entity.getLid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<GameTransferLog> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
