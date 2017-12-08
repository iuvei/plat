package com.gameportal.web.payPlat.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.payPlat.dao.PayPlatformDao;
import com.gameportal.web.payPlat.model.PayPlatform;
import com.gameportal.web.payPlat.service.IPayPlatformService;

@Service("payPlatformService")
public class PayPlatformServiceImpl implements IPayPlatformService{

	@Resource(name="payPlatformDao")
	private PayPlatformDao payPlatformDao;
	
	@Override
	public List<PayPlatform> getPayPlatform(Map<String, Object> param) {
		return payPlatformDao.getPayPlatforms(param);
	}

}
