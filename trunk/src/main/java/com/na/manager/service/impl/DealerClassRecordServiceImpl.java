package com.na.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.manager.bean.DealerClassRecordSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.dao.IDealerClassRecordMapper;
import com.na.manager.entity.DealerClassRecord;
import com.na.manager.service.IDealerClassRecordService;

/**
* @author Andy
* @version 创建时间：2017年10月14日 下午3:54:32
*/
@Service
public class DealerClassRecordServiceImpl implements IDealerClassRecordService {

	@Autowired
	private IDealerClassRecordMapper dealerClassRecordMapper;

	@Override
	public Page<DealerClassRecord> queryRecordByPage(DealerClassRecordSearchRequest request) {
		Page<DealerClassRecord> page = new Page<>(request);
		page.setData(dealerClassRecordMapper.queryRecordByPage(request));
		page.setTotal(dealerClassRecordMapper.getRecordCount(request));
		return page;
	}
}
