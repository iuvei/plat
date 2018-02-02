package com.na.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.na.manager.cache.AppCache;
import com.na.manager.dao.IChipMapper;
import com.na.manager.entity.Chip;
import com.na.manager.entity.Game;
import com.na.manager.entity.LiveUser;
import com.na.manager.service.IChipService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.util.BeanUtil;

/**
 * @author andy
 * @date 2017年6月23日 上午10:19:22
 * 
 */
@Service
public class ChipServiceImpl implements IChipService {
	
	@Autowired
	private IChipMapper chipMapper;
	
	@Autowired
	private ILiveUserService liveUserService;

	@Override
	public List<Chip> findUserChip(String chipIds) {
		if(StringUtils.isEmpty(chipIds)) {
			return null;
		}
		String chipsStr = BeanUtil.cloneTo(chipIds);
		//去掉头尾逗号
		chipsStr = chipsStr.substring(1,chipsStr.length()-1);
		if(chipsStr.endsWith(",")) {
			chipsStr = chipsStr.substring(0, chipsStr.length() - 2);
		}
		return chipMapper.findById(chipsStr);
	}
	
	@Override
	public List<Chip> findAll() {
		return chipMapper.findAll();
	}

	@Override
	public List<Map<String, Object>> findTreeChip(Long userId, String idName, String textName,String type) {
		LiveUser user = liveUserService.findById(userId);
		List<Chip> chipList = findUserChip(user.getChips());
		List<Map<String, Object>> record = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> tree = new ArrayList<Map<String, Object>>();
		Game game =null;
		for(Chip item : chipList) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put(idName, item.getId());
    		game = AppCache.getGame(item.getGameId());
//    		String gameName = game.getName();
    		map.put(type, game.getId());
    		map.put("max", item.getMax());
    		map.put("min", item.getMin());
    		map.put("chips", item.getJtton());
//			map.put(textName, gameName + "最大" + " : " + item.getMax() + " " + "最小" + ": " + item.getMin() + "筹码" + ": " + item.getJtton());
    		record.add(map);
    	}
    	
    	Map<String, Object> resultMap = new HashMap<>();
    	resultMap.put(idName, 0);
    	resultMap.put(textName, "root");
    	resultMap.put("children", record);
    	tree.add(resultMap);
		return tree;
	}
	
	
	

}
