package com.na.user.socketserver.service;

import java.util.List;
import java.util.Map;

import com.na.user.socketserver.entity.I18NPO;

/**
 * 国际化表
 * 
 * @author alan
 * @date 2017年6月22日 下午5:38:49
 */
public interface II18NService {
	
	List<I18NPO> getAll();
	
	Map<String, I18NPO> getAllMap();
	
}
