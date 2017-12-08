package com.gameportal.web.payPlat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.payPlat.model.PayPlatform;
import com.gameportal.web.user.dao.BaseIbatisDAO;
/**
 * 支付平台。
 * @author sum
 *
 */
@Repository
public class PayPlatformDao extends BaseIbatisDAO {

	@Override
	public Class<PayPlatform> getEntityClass() {
		return PayPlatform.class;
	}
	
	public List<PayPlatform> getPayPlatforms(Map<String, Object> param){
		return queryForList(param);
	}
}
