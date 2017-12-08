package com.gameportal.web.payPlat.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.payPlat.model.PayPlatform;

public interface IPayPlatformService {

	/**
	 * 获取启用的支付平台信息
	 * @param param
	 * @return
	 */
	List<PayPlatform> getPayPlatform(Map<String, Object> param);
}
