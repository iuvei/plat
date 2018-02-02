package com.na.user.socketserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.dao.IPlayMapper;
import com.na.user.socketserver.service.IPlayService;
import com.na.user.socketserver.service.ITradeItemService;


/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class PlayServiceImpl implements IPlayService {
	
    @Autowired
    private IPlayMapper playMapper;
    
    @Autowired
    private ITradeItemService tradeItemService;

	@Override
	public List<Play> getPlayByGame(Integer gameId) {
		List<Play> playList = playMapper.getPlayByGame(gameId);
		playList.forEach(item -> {
			List<TradeItem> tradeList = tradeItemService.getByPlayId(item.getId());
			item.setTradeList(tradeList);
		});
		return playList;
	}

	@Override
	public List<Play> getAll() {
		List<Play> playList = playMapper.getAll();
		playList.forEach(item -> {
			List<TradeItem> tradeList = tradeItemService.getByPlayId(item.getId());
			item.setTradeList(tradeList);
		});
		return playList;
	}
    

}
