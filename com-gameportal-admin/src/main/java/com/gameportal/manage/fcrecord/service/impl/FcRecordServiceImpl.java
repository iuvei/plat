package com.gameportal.manage.fcrecord.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.fcrecord.dao.FcRecordDao;
import com.gameportal.manage.fcrecord.model.FcRecord;
import com.gameportal.manage.fcrecord.service.IFcRecordService;
/**
 * 
 * @author 首存记录业务类。
 *
 */
@Service("fcRecordService")
public class FcRecordServiceImpl implements IFcRecordService {

	@Resource(name="fcRecordDao")
	private FcRecordDao fcRecordDao;
	@Override
	public FcRecord insert(FcRecord entity) {
		return (FcRecord)fcRecordDao.save(entity);
	}

	@Override
	public List<FcRecord> pageFcVisitRecord(Map<String, Object> params) {
		return fcRecordDao.pageFcVisitRecord(params);
	}

	@Override
	public FcRecord getObject(Map<String, Object> param) {
		return fcRecordDao.getObject(param);
	}

	@Override
	public boolean update(FcRecord fcRecord) {
		return fcRecordDao.update(fcRecord);
	}

	@Override
	public long sumFcMoney(Map<String, Object> param) {
		return fcRecordDao.sumFcMoney(param);
	}

	@Override
	public long countFcRecord(Map<String, Object> param) {
		return fcRecordDao.getRecordCount(param);
	}
}
