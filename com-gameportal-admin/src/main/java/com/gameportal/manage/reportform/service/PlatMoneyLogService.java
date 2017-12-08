package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.reportform.model.PlatMoneyLog;

/**
 * 平台余额记录业务接口
 * @author Administrator
 *
 */
public interface PlatMoneyLogService {

	/**
	 * 保存平台余额记录
	 * @param entity 平台余额实体
	 * @return
	 */
	public boolean save(PlatMoneyLog entity);
	
	/**
	 * 平台余额记录
	 * @param map
	 * @param startNo
	 * @param pagaSize
	 * @return
	 */
	public List<PlatMoneyLog> querPlatMoneyLog(Map<String, Object> map,Integer startNo, Integer pageSize);
	
	/**
	 * 平台余额记录总计
	 * @param map
	 * @return
	 */
	public Long queryPlatMoneyLogCount(Map<String,Object> map);
	
}
