package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.DealerClassRecordSearchRequest;
import com.na.manager.entity.DealerClassRecord;

/**
* @author Andy
* @version 创建时间：2017年10月14日 下午3:41:47
*/
@Mapper
public interface IDealerClassRecordMapper {
	
	long getRecordCount(DealerClassRecordSearchRequest request);
	
	List<DealerClassRecord> queryRecordByPage(DealerClassRecordSearchRequest request);
}
