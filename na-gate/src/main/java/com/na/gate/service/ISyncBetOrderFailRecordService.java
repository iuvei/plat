package com.na.gate.service;

import java.util.List;

import com.na.gate.entity.SyncBetOrderFailRecord;

/**
* @author Andy
* @version 创建时间：2017年11月10日 下午1:52:25
*/
public interface ISyncBetOrderFailRecordService {
	
	List<SyncBetOrderFailRecord> findAll();
	
	void add(Long roundId);

	void delete(Long roundId);
}
