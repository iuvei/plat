package com.gameportal.web.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.order.model.CompanyCard;
import com.gameportal.web.user.dao.BaseIbatisDAO;

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
	
	/**
	 * 
	 * @param ccid
	 * @return
	 */
	public CompanyCard getCompanyCard(long ccid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ccid", ccid);
		return (CompanyCard)getSqlMapClientTemplate().queryForObject(getSimpleName()+".getById", params);
	}
	
	public List<CompanyCard> queryCompanyCard(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageCompanyCard", params);
	}
}
