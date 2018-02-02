package com.na.manager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.manager.dao.II18NMapper;
import com.na.manager.entity.I18N;
import com.na.manager.service.II18NService;

@Service
public class I18NServiceImpl implements II18NService {
	
	@Autowired
	private II18NMapper i18NMapper;
	
	@Override
	public List<I18N> getAll() {
		return i18NMapper.getAll();
	}
	
	@Override
	public Map<String, I18N> getAllMap() {
		List<I18N> i18nList = i18NMapper.getAll();
		Map<String, I18N> resultMap = new ConcurrentHashMap<>();
		i18nList.forEach( item -> {
			resultMap.put(item.getKey(), item);
		});
		return resultMap;
	}

}
