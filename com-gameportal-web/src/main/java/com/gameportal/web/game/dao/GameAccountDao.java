package com.gameportal.web.game.dao;

import org.springframework.stereotype.Component;

import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.user.dao.BaseIbatisDAO;

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
