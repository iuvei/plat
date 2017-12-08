package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.XimaFlag;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component("ximaFlagDao")
public class XimaFlagDao extends BaseIbatisDAO{

	@Override
	public Class<XimaFlag> getEntityClass() {
		// TODO Auto-generated method stub
		return XimaFlag.class;
	}
	
	public boolean save(XimaFlag entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true
				: false;
	}
	
	public List<XimaFlag> getList(Map<String, Object> parmas){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", parmas);
	}
	
	public XimaFlag getObject(Map<String, Object> params){
		List<XimaFlag> list = this.getList(params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
