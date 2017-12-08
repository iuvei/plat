package com.gameportal.web.order.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.order.model.CCAndGroup;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Component
public class CCAndGroupDao extends BaseIbatisDAO {

	public Class<CCAndGroup> getEntityClass() {
		return CCAndGroup.class;
	}

	public boolean saveOrUpdate(CCAndGroup entity) {
		if (entity.getId() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public void deleteByCcgid(Long ccgid) {
		getSqlMapClientTemplate().delete(getSimpleName() + ".deleteByCcgid",
				ccgid);
	}

}
