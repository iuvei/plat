package com.na.manager.service;

import com.na.manager.bean.DealerClassRecordSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.entity.DealerClassRecord;

/**
* @author Andy
* @version 创建时间：2017年10月14日 下午3:53:42
*/
public interface IDealerClassRecordService {
	Page<DealerClassRecord> queryRecordByPage(DealerClassRecordSearchRequest request);
}
