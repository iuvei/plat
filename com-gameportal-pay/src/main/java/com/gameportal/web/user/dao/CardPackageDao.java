package com.gameportal.web.user.dao;

import org.springframework.stereotype.Component;

import com.gameportal.pay.dao.BaseIbatisDAO;
import com.gameportal.web.user.model.CardPackage;

@Component
public class CardPackageDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return CardPackage.class;
	}

	public void saveOrUpdate(CardPackage entity) {
		if (entity.getCpid() == null)
			save(entity);
		else
			update(entity);
	}

}
