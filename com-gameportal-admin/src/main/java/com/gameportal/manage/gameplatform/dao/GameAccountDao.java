package com.gameportal.manage.gameplatform.dao;

import org.springframework.stereotype.Component;

import com.gameportal.manage.gameplatform.model.GameAccount;
import com.gameportal.manage.system.dao.BaseIbatisDAO;



@Component
public class GameAccountDao extends BaseIbatisDAO {

	public Class<GameAccount> getEntityClass() {
		return GameAccount.class;
	}

	public void saveOrUpdate(GameAccount entity) {
		if (entity.getGaid() == null)
			save(entity);
		else
			update(entity);
	}

}
