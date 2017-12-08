package com.gameportal.manage.xima.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaDetail;

@Component
public class MemberXimaDetailDao extends BaseIbatisDAO {

	public Class<MemberXimaDetail> getEntityClass() {
		return MemberXimaDetail.class;
	}

	public boolean saveOrUpdate(MemberXimaDetail entity) {
		if (entity.getMxdid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<String> selectGpid(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectGpids",params);
	}

}
