package com.gameportal.manage.fcrecord.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.fcrecord.model.FcRecord;

/**
 * 首存记录业务类。
 * @author Administrator
 *
 */
public interface IFcRecordService {
	/**
	 * 新增首充记录。
	 * @param entity
	 * @return
	 */
	FcRecord insert(FcRecord entity);
	
	/**
	 * 分页查询首充记录。
	 * @param params
	 * @return
	 */
	List<FcRecord> pageFcVisitRecord(Map<String, Object> params);
	
	/**
	 * 查询单个首存记录。
	 * @param param
	 * @return
	 */
	FcRecord getObject(Map<String, Object> param);

	/**
	 * 更新首充记录。
	 * @param fcRecord
	 * @return
	 */
	boolean update(FcRecord fcRecord);
	
	/**
	 * 汇总首充金额。
	 * @param param
	 * @return
	 */
	long sumFcMoney(Map<String, Object> param);
	
	/**
	 * 首充记录数。
	 * @param param
	 * @return
	 */
	long countFcRecord(Map<String, Object> param);
}
