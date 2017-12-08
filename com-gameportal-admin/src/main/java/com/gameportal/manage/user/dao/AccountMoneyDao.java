package com.gameportal.manage.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.AccountMoney;

@Component
public class AccountMoneyDao extends BaseIbatisDAO {

	public Class<AccountMoney> getEntityClass() {
		return AccountMoney.class;
	}

	public boolean saveOrUpdate(AccountMoney entity) {
		if (entity.getAmid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public boolean updateTotalamount(AccountMoney am) {
		int cnt = getSqlMapClientTemplate().update(
				getSimpleName() + ".updateTotalamount", am);
		return cnt == 1;
	}

	/**
	 * 根据用户id查询钱包
	 * @param uiid
	 * @return
	 */
	public AccountMoney getByUiid(Map<String, Object> params){
		return (AccountMoney)super.queryForObject(getSimpleName()+".getByAccount", params);
	}
	public boolean updateStatus(Map<String,Object> params) {
		int cnt = getSqlMapClientTemplate().update(
				getSimpleName() + ".updateStatus", params);
		return cnt == 1;
	}
	
	public AccountMoney getMoneyInfo(Map<String, Object> params){
		return (AccountMoney)super.queryForObject(getSimpleName()+".getMoneyInfo", params);
	}
	
	public List<AccountMoney> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
}
