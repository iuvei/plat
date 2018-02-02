package com.na.user.socketserver.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.II18NMapper;
import com.na.user.socketserver.entity.I18NPO;
import com.na.user.socketserver.service.II18NService;

@Service
public class I18NServiceImpl implements II18NService {
	
	@Autowired
	private II18NMapper i18NMapper;
	
	@Override
	public List<I18NPO> getAll() {
		return i18NMapper.getAll();
	}
	
	@Override
	public Map<String, I18NPO> getAllMap() {
		List<I18NPO> i18nList = i18NMapper.getAll();
		Map<String, I18NPO> resultMap = new ConcurrentHashMap<>();
		i18nList.forEach( item -> {
			resultMap.put(item.getKey(), item);
		});
		return resultMap;
	}

}
