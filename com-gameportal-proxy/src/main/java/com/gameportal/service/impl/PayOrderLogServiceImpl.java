package com.gameportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.PayOrderLog;
import com.gameportal.persistence.PayOrderLogMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IPayOrderLogService;

@Service("payOrderLogService")
public class PayOrderLogServiceImpl extends BaseService implements IPayOrderLogService {
	@Autowired
	private PayOrderLogMapper payOrderLogMapper;

	@Override
	public int insert(PayOrderLog log) {
		return payOrderLogMapper.insert(log);
	}

}
