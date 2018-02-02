package com.na.baccarat.socketserver.command.send;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.command.sendpara.GoodRoadResponse;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.GoodRoadsEnum;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.Round;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.util.SocketUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GoodRoadsCommand {

	/**
	 * 检查好路
	 * 
	 * @param tid
	 *            桌子ID
	 * @param socketIOServer
	 */
	public void checkGoodRoads(Integer tid,SocketIOServer socketIOServer) {
		if (tid == null)
			return;
		GameTable gameTable = BaccaratCache.getGameTableById(tid);
		if(gameTable == null)
			return;
		int roadType = -1;
		Round round = gameTable.getRound();
		if(round == null)
			return;
		if (GameTableInstantStateEnum.get(round.getRoundPO().getStatus()) == GameTableInstantStateEnum.INACTIVE
				|| GameTableInstantStateEnum.get(round.getRoundPO().getStatus()) == GameTableInstantStateEnum.CLOSED) {
			roadType = -1;
		} else {
			List<Round> rds = gameTable.getRounds();
			if (rds != null && rds.size() > 0) {
				// 返回给前台的庄闲和为012
				int n = 0;
				int size = rds.size();
				// 判断最后一句是否结算 todo
				
				// 单挑长庄长闲
				if (rds.size() > (3 + n)) {
					if ("1".equals(getRs(rds.get(size - (n + 1))))
							&& "2".equals(getRs(rds.get(size - (n + 2))))
							&& "1".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))) {
						roadType = GoodRoadsEnum.SINGLEJUMPBANK.get();
					}
					if ("2".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "2".equals(getRs(rds.get(size - (n + 3))))
							&& "1".equals(getRs(rds.get(size - (n + 4))))) {
						roadType = GoodRoadsEnum.SINGLEJUMPPLAY.get();
					}
					if (roadType == -1) {
						if ("1".equals(getRs(rds.get(size - (n + 1))))
								&& "1".equals(getRs(rds.get(size - (n + 2))))
								&& "1".equals(getRs(rds.get(size - (n + 3))))
								&& "1".equals(getRs(rds.get(size - (n + 4))))) {
							roadType = GoodRoadsEnum.LONGBANK.get();
						}
						if ("2".equals(getRs(rds.get(size - (n + 1))))
								&& "2".equals(getRs(rds.get(size - (n + 2))))
								&& "2".equals(getRs(rds.get(size - (n + 3))))
								&& "2".equals(getRs(rds.get(size - (n + 4))))) {
							roadType = GoodRoadsEnum.LONGPLAY.get();
						}
					}
				}
				// 一厅两房
				if (rds.size() > (5 + n) && roadType == -1) {
					if ("1".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "2".equals(getRs(rds.get(size - (n + 3))))
							&& "1".equals(getRs(rds.get(size - (n + 4))))
							&& "1".equals(getRs(rds.get(size - (n + 5))))
							&& "2".equals(getRs(rds.get(size - (n + 6))))) {
						roadType = GoodRoadsEnum.ONETOWBANK.get();
					}
					if ("2".equals(getRs(rds.get(size - (n + 1))))
							&& "2".equals(getRs(rds.get(size - (n + 2))))
							&& "1".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))
							&& "2".equals(getRs(rds.get(size - (n + 5))))
							&& "1".equals(getRs(rds.get(size - (n + 6))))) {
						roadType = GoodRoadsEnum.ONETOWPLAY.get();
					}
				}
				// 逢庄离
				if (rds.size() > (7 + n) && roadType == -1) {
					if ("2".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "1".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))
							&& "1".equals(getRs(rds.get(size - (n + 5))))
							&& "1".equals(getRs(rds.get(size - (n + 6))))
							&& "1".equals(getRs(rds.get(size - (n + 7))))
							&& "1".equals(getRs(rds.get(size - (n + 8))))) {
						roadType = GoodRoadsEnum.LONGTOWBANK.get();
					}
				}
				// 逢闲离
				if (rds.size() > (8 + n) && roadType == -1) {
					if ("1".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "2".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))
							&& "2".equals(getRs(rds.get(size - (n + 5))))
							&& "2".equals(getRs(rds.get(size - (n + 6))))
							&& "1".equals(getRs(rds.get(size - (n + 7))))
							&& "2".equals(getRs(rds.get(size - (n + 8))))
							&& "2".equals(getRs(rds.get(size - (n + 9))))) {
						roadType = GoodRoadsEnum.LONGTOWPLAY.get();
					}
				}

				// 拍拍离
				if (rds.size() > (6 + n) && roadType == -1) {
					if ("1".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "2".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))
							&& "2".equals(getRs(rds.get(size - (n + 5))))
							&& "1".equals(getRs(rds.get(size - (n + 6))))
							&& "1".equals(getRs(rds.get(size - (n + 7))))) {
						roadType = GoodRoadsEnum.TOWLONGBANK.get();
					}
					if ("2".equals(getRs(rds.get(size - (n + 1))))
							&& "2".equals(getRs(rds.get(size - (n + 2))))
							&& "1".equals(getRs(rds.get(size - (n + 3))))
							&& "1".equals(getRs(rds.get(size - (n + 4))))
							&& "1".equals(getRs(rds.get(size - (n + 5))))
							&& "2".equals(getRs(rds.get(size - (n + 6))))
							&& "2".equals(getRs(rds.get(size - (n + 7))))) {
						roadType = GoodRoadsEnum.TOWLONGPLAY.get();
					}
				}
				// 隔离庄
				if (rds.size() > (10 + n) && roadType == -1) {
					if ("1".equals(getRs(rds.get(size - (n + 1))))
							&& "1".equals(getRs(rds.get(size - (n + 2))))
							&& "2".equals(getRs(rds.get(size - (n + 3))))
							&& "1".equals(getRs(rds.get(size - (n + 4))))
							&& "2".equals(getRs(rds.get(size - (n + 5))))
							&& "2".equals(getRs(rds.get(size - (n + 6))))
							&& "1".equals(getRs(rds.get(size - (n + 7))))
							&& "1".equals(getRs(rds.get(size - (n + 8))))
							&& "1".equals(getRs(rds.get(size - (n + 9))))
							&& "2".equals(getRs(rds.get(size - (n + 10))))
							&& "1".equals(getRs(rds.get(size - (n + 11))))) {
						roadType = GoodRoadsEnum.TWOTOWBANK.get();
					}
				}
				// 隔离闲
				if (rds.size() > (8 + n) && roadType == -1) {
					if ("2".equals(getRs(rds.get(size - (n + 1))))
							&& "2".equals(getRs(rds.get(size - (n + 2))))
							&& "1".equals(getRs(rds.get(size - (n + 3))))
							&& "2".equals(getRs(rds.get(size - (n + 4))))
							&& "1".equals(getRs(rds.get(size - (n + 5))))
							&& "2".equals(getRs(rds.get(size - (n + 6))))
							&& "2".equals(getRs(rds.get(size - (n + 7))))
							&& "1".equals(getRs(rds.get(size - (n + 8))))
							&& "2".equals(getRs(rds.get(size - (n + 9))))) {
						roadType = GoodRoadsEnum.TWOTOWPLAY.get();
					}
				}
			}
		}
		//if (roadType == -1) {
		if (roadType != -1) {
			GoodRoadResponse goodRoadResponse = new GoodRoadResponse();
			goodRoadResponse.setStu(gameTable.getRound().getRoundPO().getStatus());
			goodRoadResponse.setTableName(gameTable.getGameTablePO().getName());
			goodRoadResponse.setTid(tid);
			goodRoadResponse.setGtp(roadType);
			goodRoadResponse.setCountDownSeconds(gameTable.getGameTablePO().getCountDownSeconds());


			List<GoodRoadResponse.PlayInfo>  playInfoList = new ArrayList<>();
			GoodRoadResponse.PlayInfo playInfo = null;
			List<GoodRoadResponse.TradeItemInfo> tradeList = null;
			GoodRoadResponse.TradeItemInfo itemInfo = null;
			List<Play> playList = gameTable.getPlayList();
			for(Play play : playList) {
				playInfo = goodRoadResponse.new PlayInfo();
				tradeList = new ArrayList<>();
				for(TradeItem item :play.getTradeList()) {
					itemInfo = goodRoadResponse.new TradeItemInfo();
					itemInfo.id = item.getId();
					itemInfo.key = item.getKey();
					itemInfo.name = item.getName();
					itemInfo.rate = item.getRate().doubleValue();
					tradeList.add(itemInfo);
				}
				playInfo.tradeList = tradeList;
				playInfo.id = play.getId();
				playInfo.name = play.getName();
				playInfoList.add(playInfo);
			}
			goodRoadResponse.setPlayList(playInfoList);

			Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
			for (SocketIOClient socketIOClient : allClients) {
				UserPO user = AppCache.getUserByClient(socketIOClient);
				if(null == user || UserTypeEnum.DEALER == user.getUserTypeEnum() || !user.isInTable())
					continue;
				CommandResponse response = CommandResponse.createSuccess(RequestCommandEnum.COMMON_GOOD_ROADS_CHANGE,goodRoadResponse);
				SocketUtil.send(socketIOClient, ResponseCommandEnum.OK,response);
			}
		} 
	}


	public String getRs(Round round) {
		String rs = "";
		if (round != null ){
			String result = round.getRoundPO().getResult();
			if(result!= null && result.length()>2){
				rs = result.substring(0, 1);
			}
		}
			
		return rs;
	}

}
