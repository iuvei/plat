package com.gameportal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.Page;
import com.gameportal.domain.PayOrder;
import com.gameportal.persistence.PayOrderMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IPayOrderService;

/**
 * 订单业务实现
 * @author leron
 *
 */
@SuppressWarnings("all")
@Service("payOrderServiceImpl")
public class PayOrderServiceImpl extends BaseService implements
		IPayOrderService {

	@Autowired
	private PayOrderMapper payOrdermapper;
	
	@Override
	public List<PayOrder> queryDepositOrderLog(Page page) {
		return payOrdermapper.findlistPageOrderLog(page);
	}
	
	@Override
	public String queryOrderLogTotal(Map<String, Object> params) {
		return payOrdermapper.selectProxyPayOrderTotal(params);
	}

	@Override
	public int insert(PayOrder log) {
		return payOrdermapper.insert(log);
	}
}
