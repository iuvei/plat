package com.gameportal.pay.dao;

import org.springframework.stereotype.Component;

import com.gameportal.pay.model.PayPlatform;

@Component
public class PayPlatformDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return PayPlatform.class;
	}

	public void saveOrUpdate(PayPlatform entity) {
		if (entity.getPpid() == null)
			save(entity);
		else
			update(entity);
	}

}
