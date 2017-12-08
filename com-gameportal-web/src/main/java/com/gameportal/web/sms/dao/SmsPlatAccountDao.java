package com.gameportal.web.sms.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.sms.model.SmsPlatAccount;
import com.gameportal.web.user.dao.BaseIbatisDAO;

/**
 * 短信平台持久层。
 * @author sum
 *
 */
@Repository
@SuppressWarnings("all")
public class SmsPlatAccountDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return SmsPlatAccount.class;
	}
	
	public SmsPlatAccount getSmsPlatAccount(){
		Map values = new HashMap();
		values.put("status", 1);
		return (SmsPlatAccount)queryForObject(getSelectQuery(), values);
	}
}
