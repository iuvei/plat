package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.MemberXimaDetail;
@Component
@SuppressWarnings("all")
public class MemberXimaDetailDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return MemberXimaDetail.class;
	}
	
	public boolean saveOrUpdate(MemberXimaDetail entity) {
		if (entity.getMxdid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	public List<MemberXimaDetail> findMemberXimaDetailList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelectDetail",params);
	}

}
