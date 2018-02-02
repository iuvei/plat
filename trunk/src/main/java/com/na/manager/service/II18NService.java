package com.na.manager.service;

import java.util.List;
import java.util.Map;

import com.na.manager.entity.I18N;

/**
 * 国际化表
 * 
 * @author alan
 * @date 2017年6月22日 下午5:38:49
 */
public interface II18NService {
	
	List<I18N> getAll();
	
	Map<String, I18N> getAllMap();
	
}
