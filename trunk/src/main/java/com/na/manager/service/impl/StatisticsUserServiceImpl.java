package com.na.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.na.manager.bean.vo.BetRankVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IStatisticsUserMapper;
import com.na.manager.service.IStatisticsUserService;

/**
 * 
 * 
 * @author alan
 * @date 2017年12月19日 下午4:49:48
 */
@Service
public class StatisticsUserServiceImpl implements IStatisticsUserService {
	
	private Logger logger =LoggerFactory.getLogger(StatisticsUserServiceImpl.class);
	
	@Autowired
	private IStatisticsUserMapper statisticsUserMapper;

	@Override
	public void add(String beginTime, String endTime) {
		statisticsUserMapper.add(beginTime, endTime);
	}

	@Override
	public Long selectOnlineNumberNow() {
		return statisticsUserMapper.selectOnlineNumberNow(AppCache.getCurrentUser().getId());
	}

	@Override
	public List<?> selectRegisterNumberByDay() {
		List<?> dataList= statisticsUserMapper.selectRegisterNumberByDay(AppCache.getCurrentUser().getId());
		logger.info("注册人数："+JSONObject.toJSON(dataList).toString());
		return dataList;
	}

	@Override
	public List<?> selectBetNumberByDay() {
		List<?> dataList= statisticsUserMapper.selectBetNumberByDay(AppCache.getCurrentUser().getId());
		logger.info("投注人数："+JSONObject.toJSON(dataList).toString());
		return dataList;
	}

	@Override
	public List<?> selectBetTotalByDay() {
		List<?> dataList= statisticsUserMapper.selectBetTotalByDay(AppCache.getCurrentUser().getId());
		logger.info("投注总量："+JSONObject.toJSON(dataList).toString());
		return dataList;
	}

	@Override
	public Map<Object, Object> selectBetTotalRankByDay() {
		Map<Object, Object> dataMap =new HashMap<>();
		Map<Object, Map<Object, Object>> complexMap = new HashMap<>();
		List<?> list =statisticsUserMapper.selectBetTotalRankByDay(AppCache.getCurrentUser().getId());
		JSONArray jsonArray =(JSONArray)JSON.toJSON(list);
		JSONObject jsonObject =null;
		Map<Object, Object> map =null;
		Set<Object> mainChartLabels =new LinkedHashSet<>();
		List<Object> data=new ArrayList<>();
		List<BetRankVO> tempMap = new ArrayList<>();
		for(Object obj :jsonArray){
			jsonObject=(JSONObject)obj;
			if(complexMap.containsKey(jsonObject.get("agentName"))){
				complexMap.get(jsonObject.get("agentName")).put(jsonObject.get("staDate"), jsonObject.get("betTotal"));
			}else{
				map =new HashMap<>();
				map.put(jsonObject.get("staDate"), jsonObject.get("betTotal"));
				complexMap.put(jsonObject.get("agentName"),map);
			}
			mainChartLabels.add(jsonObject.get("staDate"));
		}
		Iterator<Entry<Object, Map<Object, Object>>> iterator = complexMap.entrySet().iterator();
		Entry<Object, Map<Object, Object>> next =null;
		while(iterator.hasNext()){
			next = iterator.next();
			data =new ArrayList<>();
			for (Entry<Object, Object> temp : next.getValue().entrySet()) {  
				data.add(temp.getValue());
			}
			tempMap.add(new BetRankVO(data, (String)next.getKey()));
		}
		logger.info("投注纵轴："+JSONObject.toJSON(tempMap).toString());
		logger.info("投注横轴："+JSONObject.toJSON(mainChartLabels).toString());
		dataMap.put("mainChartData", tempMap);
		dataMap.put("mainChartLabels", mainChartLabels);
		return dataMap;
	}
	
}
