package com.gameportal.web.user.dao;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.AccountMoney;

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
		int cnt = getSqlMapClientTemplate().update(getSimpleName() + ".updateTotalamount", am);
		return cnt == 1;
	}
}
