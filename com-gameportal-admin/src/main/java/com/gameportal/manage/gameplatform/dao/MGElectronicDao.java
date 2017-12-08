package com.gameportal.manage.gameplatform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.gameplatform.model.MGElectronic;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 电子游戏实体类
 * @author Administrator
 *
 */
@Component("mgElectronicDao")
public class MGElectronicDao extends BaseIbatisDAO{

	@Override
	public Class<MGElectronic> getEntityClass() {
		return MGElectronic.class;
	}
	
	public MGElectronic getObject(Map<String, Object> params){
		List<MGElectronic> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public List<MGElectronic> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
}
