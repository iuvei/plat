package com.na.gate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.gate.dao.ISyncBetOrderFailRecordMapper;
import com.na.gate.entity.SyncBetOrderFailRecord;
import com.na.gate.service.ISyncBetOrderFailRecordService;

/**
* @author Andy
* @version 创建时间：2017年11月10日 下午1:53:02
*/
@Service	
public class SyncBetOrderFailRecordServiceImpl implements ISyncBetOrderFailRecordService{

	@Autowired
	private ISyncBetOrderFailRecordMapper syncBetOrderFailRecordMapper;
	
	@Override
	public List<SyncBetOrderFailRecord> findAll() {
		return syncBetOrderFailRecordMapper.findAll();
	}
	
	@Override
	public void add(Long roundId) {
		syncBetOrderFailRecordMapper.add(roundId);		
	}

	@Override
	public void delete(Long roundId) {
		syncBetOrderFailRecordMapper.delete(roundId);
	}
}
