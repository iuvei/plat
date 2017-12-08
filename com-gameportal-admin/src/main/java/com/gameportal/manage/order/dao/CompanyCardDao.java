package com.gameportal.manage.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.MemberUpgradeLog;
import com.gameportal.manage.order.model.CompanyCard;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class CompanyCardDao extends BaseIbatisDAO {

	public Class<CompanyCard> getEntityClass() {
		return CompanyCard.class;
	}

	public boolean saveOrUpdate(CompanyCard entity) {
		if (entity.getCcid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public boolean updateStatus(Map<String, Object> params) {
		int cnt = getSqlMapClientTemplate().update(
				getSimpleName() + ".updateStatus", params);
		return cnt == 1;
	}

	public List<CompanyCard> queryByCcgid(Long ccgid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ccgid", ccgid);
		return getSqlMapClientTemplate().queryForList(
				getSimpleName() + ".queryByCcgid", params);
	}

	public List<CompanyCard> queryByUiidInCcgx(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList(
				getSimpleName() + ".queryByUiidInCcgx", params);
	}

	public List<CompanyCard> queryByGrade(Integer grade) {
		return null;
	}
	

	public List<CompanyCard> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
