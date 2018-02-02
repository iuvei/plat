package com.na.baccarat.socketserver.command.flow.table.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.OtherJoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.common.enums.SelectRoomSearchType;
import com.na.baccarat.socketserver.config.BaccaratConfig;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableStatus;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.StringUtil;

@Component
public class NormalRoom extends RoomAction {
	
	private Logger log = LoggerFactory.getLogger(NormalRoom.class);
	private byte[] lock = new byte[0];
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client,
			JoinRoomResponse response) {
		User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		if(loginUser == null) {
			throw SocketException.createError("user.not.exist");
		}
		UserPO loginUserPO = loginUser.getUserPO();

		Integer tableId = params.getTableId();
		GameTable gameTable = BaccaratCache.getGameTableById(tableId);
		Integer virtualTableId = params.getVirtualTableId();
		VirtualGameTable virtualTable = null;
		Integer seatNum = null;
		
		if (StringUtils.isEmpty(virtualTableId)) {
			log.debug("用户" + loginUserPO.getLoginName() + "快速开始游戏");

			/**
			 * 快速开始
			 * 从对应的虚拟桌中筛选空座随机加入
			 */
			Map<String, Object> map = BaccaratCache.joinVirtualTable(tableId, loginUserPO.getId());
			
			virtualTableId = (Integer) map.get("virtualTableId");
			seatNum = (Integer) map.get("seatNumber");
			virtualTable = BaccaratCache.getVirtualTableById(virtualTableId);
			
		} else {
			//加入指定房间
			Map<String, Object> map = BaccaratCache.joinVirtualTable(tableId, virtualTableId, loginUserPO.getId());
			
			virtualTableId = (Integer) map.get("virtualTableId");
			seatNum = (Integer) map.get("seatNumber");
			virtualTable = BaccaratCache.getVirtualTableById(virtualTableId);
		}

		loginUser.setSeat(seatNum);
		loginUser.setSource(BetOrderSourceEnum.SEAT);
		loginUser.setVirtualTableId(virtualTable.getVirtualGameTablePO().getId());

		client.set(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get(), virtualTable.getVirtualGameTablePO().getId() + "");

		BaccaratCache.freshVirtualTable(virtualTable);
		
		response.setVirtualTableTypeEnum(VirtualGameTableType.COMMON);
		response.setSeatNum(loginUser.getSeat());
		response.setSourceEnum(BetOrderSourceEnum.SEAT);
		response.setVirtualTableId(loginUser.getVirtualTableId());
		
		List<UserBet> userBetInfos = new ArrayList<>();
		for(UserBet item : gameTable.getRound().getUserBetedInfos()) {
			if(item.getVirtualTableId() != null && item.getVirtualTableId().compareTo(virtualTableId) == 0) {
				if(item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(loginUserPO.getId()) == 0) {
					userBetInfos.add(item);
				}
			}
		}
		
		getResponseData(response, BaccaratCache.getVirtualTableSeatUser(virtualTable.getVirtualGameTablePO().getGameTableId(), 
				virtualTable.getVirtualGameTablePO().getId()), userBetInfos, loginUserPO.getId());
		
		Collection<SocketIOClient> clients = SocketUtil.getOtherVirtualTableClients(socketIOServer, client, virtualTableId);
		if(clients != null) {
        	clients.forEach( item -> {
        		Long userId = ConvertUtil.toLong(item.get(SocketClientStoreEnum.USER_ID.get()));
        		OtherJoinRoomResponse otherJoinRoomResponse = new OtherJoinRoomResponse();
        		otherJoinRoomResponse.setTableId(response.getTableId());
        		otherJoinRoomResponse.setVirtualTableId(response.getVirtualTableId());
//        		List<JoinRoomResponse.UserInfo> userInfoList = BeanUtil.cloneTo(response.getSeatList());
//        		userInfoList.forEach( value -> {
//        			if(userId.compareTo(value.userId) != 0) {
//        				value.nickName = StringUtil.hideNickName(value.nickName);
//        			}
//        		});
        		otherJoinRoomResponse.setSeatList(response.getSeatList());
        		roomCommand.send(item, RequestCommandEnum.COMMON_OTHER_JOIN_ROOM, otherJoinRoomResponse);
            });
        }
		
		log.debug("用户" + loginUserPO.getLoginName() + "加入房间" + virtualTableId + " : " + virtualTable.getVirtualGameTablePO().getId());
	}

	@Override
	public void selectRoom(SelectRoomPara params, SocketIOClient client,
			SelectRoomResponse response) {
		Integer tableId = params.getTableId();
		GameTable table = BaccaratCache.getGameTableById(tableId);
		if(table == null) {
			throw SocketException.createError("table.not.exist");
		}
		
		List<VirtualGameTable> virtualTableListTemp = BaccaratCache.getVirtualTableByTableId(tableId);
		List<VirtualGameTable> virtualTableList = virtualTableListTemp.stream().filter( item -> {
			return VirtualGameTableType.COMMON == item.getVirtualGameTablePO().getTypeEnum() && 
					item.getVirtualGameTablePO().getStatusEnum() == VirtualGameTableStatus.NORMAL;
		}).collect(Collectors.toList());
		
		if(params.getSearchFlagEnum() != null) {
			virtualTableList = virtualTableList.stream().filter(item -> {
				Integer size = BaccaratCache.getVirtualTableSeatUserCount(item.getVirtualGameTablePO().getGameTableId(), item.getVirtualGameTablePO().getId());
				if(SelectRoomSearchType.EMPTY == params.getSearchFlagEnum()) {
					return size == 0;
				} else if(SelectRoomSearchType.FULL == params.getSearchFlagEnum()) {
					return size == BaccaratConfig.seatNum;
				} else if(SelectRoomSearchType.LACK == params.getSearchFlagEnum()) {
					return size < BaccaratConfig.seatNum;
				}
				return false;
			}).collect(Collectors.toList());
		}
		int virtualTableSize = virtualTableList.size();
		getVirtualTablePage(virtualTableList, params.getPageNum(), params.getPageSize(), params.getvTableId());
		
        List<SelectRoomResponse.VirtualTableInfo> vTabelInfoList = new ArrayList<>();
        virtualTableList.forEach(virtualTable -> {
        	SelectRoomResponse.VirtualTableInfo  vTableInfo = response.new VirtualTableInfo();
            vTableInfo.tableId = virtualTable.getVirtualGameTablePO().getId();
            vTableInfo.name = virtualTable.getVirtualGameTablePO().getRoomName();
            vTableInfo.tid = virtualTable.getVirtualGameTablePO().getGameTableId();
            vTableInfo.minBalance = virtualTable.getVirtualGameTablePO().getMinBalance();
            vTableInfo.playerNumber = BaccaratCache.getVirtualTableSeatUserCount(virtualTable.getVirtualGameTablePO().getGameTableId(), virtualTable.getVirtualGameTablePO().getId());
            if (vTableInfo.playerNumber >= BaccaratConfig.seatNum) {
                vTableInfo.isFull = true;
            } else {
                vTableInfo.isFull = false;
            }
            vTabelInfoList.add(vTableInfo);
        });
        response.setTotal(virtualTableSize);
        response.setTableList(vTabelInfoList);
        response.setTid(tableId);
	}

}
