package com.na.manager.service;

import java.util.List;
import java.util.Map;

import com.na.manager.entity.Chip;
import com.na.manager.entity.LiveUser;


/**
 * @author andy
 * @date 2017年6月23日 上午10:15:59
 * 
 */
public interface IChipService {
	
	/**
	 * 获取用户限红
	 * @param user
	 * @return
	 */
	List<Chip> findUserChip(String chipIds);
	
	/**
	 * 获取所用限红
	 * @param user
	 * @return
	 */
	List<Chip> findAll();
	
	/**
	 * 获取前段展示用户限红
	 * @param user
	 * @return
	 */
	List<Map<String, Object>> findTreeChip(Long userId, String idName, String textName,String type);
	
}
