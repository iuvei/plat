package com.gameportal.manage.xima.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.xima.model.ProxyXimaSet;


@Component
public class ProxyXimaSetDao extends BaseIbatisDAO{

	public Class<ProxyXimaSet> getEntityClass() {
		return ProxyXimaSet.class;
	}
	
	public boolean saveOrUpdate(ProxyXimaSet entity) {
		if(entity.getPxsid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}

	public ProxyXimaSet queryByUiid(Long uiid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", uiid);
		List<ProxyXimaSet> list = queryForPager(params, 0, 0);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<ProxyXimaSet> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
