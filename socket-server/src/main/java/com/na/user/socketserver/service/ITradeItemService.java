package com.na.user.socketserver.service;

import java.util.List;

import com.na.baccarat.socketserver.entity.TradeItem;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface ITradeItemService {
	
    /**
     * 获取交易项
     * @return
     */
	public TradeItem getTradeItemById(Integer id);
	
	
	/**
     * 根据玩法获取交易项
     * @return
     */
	public List<TradeItem> getByPlayId(Integer playId);
}
