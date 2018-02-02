package com.na.baccarat.socketserver.command.request;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.TradeItemPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.TradeItemResponse;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  通过玩法ID,返回交易项列表
 */
@Cmd(name="获取交易项列表",paraCls=TradeItemPara.class)
@Component
public class TradeItemCommand implements ICommand {
	
	@Autowired
	private RoomCommand roomCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		TradeItemPara params = (TradeItemPara) commandReqestPara;
		TradeItemResponse tradeItemResponse = new TradeItemResponse();		
		Integer playId = params.getPlayId();
		if(playId != null){
			List<TradeItemResponse.TradeItemInfo> tradeItemInfoList = null;
			List<TradeItem> tradeItems = AppCache.getTradeItemListByPlayId(playId);
			tradeItemInfoList = new ArrayList<>();
			TradeItemResponse.TradeItemInfo  itemInfo = null;
			for (TradeItem item : tradeItems) {
				itemInfo = tradeItemResponse.new TradeItemInfo();
				itemInfo.id = item.getId();
				itemInfo.key = item.getKey();
				itemInfo.name = item.getName();
				itemInfo.rate = item.getRate().subtract(new BigDecimal(1)).doubleValue();
				tradeItemInfoList.add(itemInfo);
			}
			tradeItemResponse.setTradeItemList(tradeItemInfoList);
			roomCommand.send(client, RequestCommandEnum.COMMON_TRADEITEM, tradeItemResponse);
			return true;
		}
		
		//获取玩法列表
		List<TradeItemResponse.PlayInfo>  playInfoList = new ArrayList<>();
		TradeItemResponse.PlayInfo playInfo = null;
		List<TradeItemResponse.TradeItemInfo> tradeList = null;
		TradeItemResponse.TradeItemInfo itemInfo = null;
		List<Play> playList = AppCache.getPlayList();
		for(Play play : playList) {
			playInfo = tradeItemResponse.new PlayInfo();
			tradeList = new ArrayList<>();
			for(TradeItem item :play.getTradeList()) {
				itemInfo = tradeItemResponse.new TradeItemInfo();
				itemInfo.id = item.getId();
				itemInfo.key = item.getKey();
				itemInfo.name = item.getName();
				itemInfo.rate = item.getRate().subtract(new BigDecimal(1)).doubleValue();
				tradeList.add(itemInfo);
			}
			playInfo.tradeList = tradeList;
			playInfo.id = play.getId();
			playInfo.name = play.getName();
			playInfoList.add(playInfo);
		}
		tradeItemResponse.setPlayList(playInfoList);
		roomCommand.send(client, RequestCommandEnum.COMMON_TRADEITEM, tradeItemResponse);
		return true;
	}
	
}