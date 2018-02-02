package com.na.user.socketserver.service.impl;


import java.util.List;

import com.na.baccarat.socketserver.entity.Play;
import com.na.user.socketserver.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.dao.ITradeItemMapper;
import com.na.user.socketserver.service.ITradeItemService;


/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class TradeItemServiceImpl implements ITradeItemService {
	
    @Autowired
    private ITradeItemMapper tradeItemMapper;

	@Override
	public TradeItem getTradeItemById(Integer id) {
		TradeItem tradeItem = AppCache.getTradeItem(id);
		if(tradeItem!=null) {
			return tradeItem;
		}

		return tradeItemMapper.getTradeItemById(id);
	}

	@Override
	public List<TradeItem> getByPlayId(Integer playId) {
		List<TradeItem> tradeItemList = AppCache.getTradeItemListByPlayId(playId);
		if(tradeItemList!=null && tradeItemList.size()>0){
			return tradeItemList;
		}

		return tradeItemMapper.getByPlay(playId);
	}
    
    

}
