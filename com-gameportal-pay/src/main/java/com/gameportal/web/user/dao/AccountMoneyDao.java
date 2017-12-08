package com.gameportal.web.user.dao;

import org.springframework.stereotype.Component;

import com.gameportal.pay.dao.BaseIbatisDAO;
import com.gameportal.web.user.model.AccountMoney;

@Component
public class AccountMoneyDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return AccountMoney.class;
	}

	public void saveOrUpdate(AccountMoney entity) {
		if (entity.getAmid() == null)
			save(entity);
		else
			update(entity);
	}
	
	public boolean updateTotalAmount(AccountMoney entity){
		return getSqlMapClientTemplate().update(getSimpleName()+".updateRow", entity) ==1;
	}

}
