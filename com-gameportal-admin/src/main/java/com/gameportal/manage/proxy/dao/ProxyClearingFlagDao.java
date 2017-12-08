package com.gameportal.manage.proxy.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyClearingEntity;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 代理结算标示数据访问类
 * @author Administrator
 *
 */
@Component("proxyClearingFlagDao")
public class ProxyClearingFlagDao extends BaseIbatisDAO{

	@Override
	public Class<ProxyClearingEntity> getEntityClass() {
		return ProxyClearingEntity.class;
	}
	
	public ProxyClearingEntity getObject(Map<String, Object> params){
		List<ProxyClearingEntity> list = getSqlMapClientTemplate().queryForList(getSimpleName()+".queryClearingFlag", params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<ProxyClearingEntity> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".queryClearingFlag", params);
	}
	
	public boolean savebool(ProxyClearingEntity entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}

}
