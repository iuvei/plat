package com.gameportal.manage.fcrecord.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.manage.fcrecord.model.FcRecord;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 首充回访记录。
 * @author sum
 *
 */
@Repository
public class FcRecordDao extends BaseIbatisDAO{

	@Override
	public Class<FcRecord> getEntityClass() {
		return FcRecord.class;
	}

	public List<FcRecord> pageFcVisitRecord(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public FcRecord getObject(Map<String, Object> param){
		return (FcRecord)queryForObject(getSimpleName()+".getObject",param);
	}
	
	public long countFcRecord(Map<String, Object> param){
		return (Long)queryForObject(getSimpleName()+".count",param);
	}
	
	public long sumFcMoney(Map<String, Object> param){
		return (Long)queryForObject(getSimpleName()+".sumFcMoney",param);
	}
}
