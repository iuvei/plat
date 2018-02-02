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
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableStatus;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.BeanUtil;
import com.na.user.socketserver.util.ConvertUtil;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.StringUtil;

@Component
public class AgentVipRoom extends RoomAction {
	
	private Logger log = LoggerFactory.getLogger(AgentVipRoom.class);
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	@Override
	public void join(JoinRoomPara params, SocketIOClient client,
			JoinRoomResponse response) {
		User loginUser = BaccaratCache.getLoginUser(ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get())));
		LiveUserPO liveUserPO = (LiveUserPO) loginUser.getUserPO();
		
		GameTable gameTable = BaccaratCache.getGameTableById(params.getTableId());
		Integer virtualTableId = params.getVirtualTableId();
		VirtualGameTable virtualTable = BaccaratCache.getVirtualTableById(virtualTableId);
		VirtualGameTablePO virtualGameTablePO = virtualTable.getVirtualGameTablePO();
		
		if(!params.isQuickChangeRoom()) {
			if(StringUtils.isEmpty(params.getPassword())) {
				throw SocketException.createError("login.params.password.null");
			}
			
			if(!virtualGameTablePO.getPassword().equals(params.getPassword())) {
				throw SocketException.createError("room.password.error");
			}
		}
		
		//试玩就算输入密码，也是不可以进入包房的
		//非直线会员就算输入密码，也是不可以进入包房的
		LiveUserPO demoAgent = userService.getLiveUserByLoginName(socketIoConfig.getDemoAgentName());
		LiveUserPO apiDemoAgent = userService.getLiveUserByLoginName(socketIoConfig.getApiDemoAgentName());
		if(virtualGameTablePO.getOwnerId().compareTo(liveUserPO.getParentId()) != 0 ||
				liveUserPO.getParentId().compareTo(demoAgent.getId()) == 0 ||
				liveUserPO.getParentId().compareTo(apiDemoAgent.getId()) == 0) {
			throw SocketException.createError("agentviproom.not.allow.enter");
		}
		
		if(virtualGameTablePO.getStatusEnum() != VirtualGameTableStatus.NORMAL) {
			throw SocketException.createError("room.already.disable");
		}
		
		if(virtualGameTablePO.getMinBalance().compareTo(loginUser.getUserPO().getBalance()) > 0) {
			throw SocketException.createError("current.balance.low.limit.balance");
		}
		
		if(virtualGameTablePO.getMaxMembers().compareTo(0) != 0) {
			Integer playerNumber = BaccaratCache.getVirtualTableSeatUserCount(virtualTable.getVirtualGameTablePO().getGameTableId(), virtualTable.getVirtualGameTablePO().getId()) + virtualTable.getBesideUser().size();
			if(virtualGameTablePO.getMaxMembers().compareTo(playerNumber) <= 0) {
				throw SocketException.createError("table.person.enough");
			}
		}
		
		Integer seatNum = null;
		try {
			Map<String, Object> map = BaccaratCache.joinVirtualTable(virtualGameTablePO.getGameTableId(), virtualGameTablePO.getId(), liveUserPO.getId());
			seatNum = (Integer) map.get("seatNumber");
			virtualTable = BaccaratCache.getVirtualTableById(virtualTableId);
			
			if(seatNum == null) {
				throw SocketException.createError("table.person.enough");
			}
			
			loginUser.setSeat(seatNum);
			loginUser.setSource(BetOrderSourceEnum.SEAT);

			response.setSeatNum(seatNum);
			response.setSourceEnum(BetOrderSourceEnum.SEAT);
		} catch(SocketException e) {
			virtualTable.getBesideUser().add(loginUser.getUserPO().getId());
			
			loginUser.setSource(BetOrderSourceEnum.SIDENOTE);
			
			response.setSourceEnum(BetOrderSourceEnum.SIDENOTE);
		}
		
		//用于不下注踢出用户  第三局警告  第五局强制踢出
		loginUser.setVirtualTableId(virtualTable.getVirtualGameTablePO().getId());
		client.set(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get(), virtualTable.getVirtualGameTablePO().getId() + "");
		
		BaccaratCache.freshVirtualTable(virtualTable);
		
		List<UserBet> userBetInfos = gameTable.getRound().getUserBetedInfos().stream().filter( item -> {
			if(item.getVirtualTableId() != null && item.getVirtualTableId().compareTo(virtualTableId) == 0) {
				return item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(liveUserPO.getId()) == 0;
			}
			return false;
		}).collect(Collectors.toList());
		
		response.setVirtualTableId(loginUser.getVirtualTableId());
		response.setVirtualTableTypeEnum(virtualGameTablePO.getTypeEnum());
		getResponseData(response, BaccaratCache.getVirtualTableSeatUser(virtualTable.getVirtualGameTablePO().getGameTableId(), 
				virtualTable.getVirtualGameTablePO().getId()), userBetInfos, liveUserPO.getId());
		
		if(BetOrderSourceEnum.SEAT == loginUser.getSource()) {
			Collection<SocketIOClient> clients = SocketUtil.getOtherVirtualTableClients(socketIOServer, client, virtualTableId);
			if(clients != null) {
	        	clients.forEach( item -> {
	        		Long userId = ConvertUtil.toLong(item.get(SocketClientStoreEnum.USER_ID.get()));
	        		OtherJoinRoomResponse otherJoinRoomResponse = new OtherJoinRoomResponse();
	        		otherJoinRoomResponse.setTableId(response.getTableId());
	        		otherJoinRoomResponse.setVirtualTableId(response.getVirtualTableId());
//	        		List<JoinRoomResponse.UserInfo> userInfoList = BeanUtil.cloneTo(response.getSeatList());
//	        		userInfoList.forEach( value -> {
//	        			if(userId.compareTo(value.userId) != 0) {
//	        				value.nickName = StringUtil.hideNickName(value.nickName);
//	        			}
//	        		});
	        		otherJoinRoomResponse.setSeatList(response.getSeatList());
	        		roomCommand.send(item, RequestCommandEnum.COMMON_OTHER_JOIN_ROOM, otherJoinRoomResponse);
	            });
	        }
		}
		
		log.debug("用户" + loginUser.getUserPO().getLoginName() + "加入房间" + virtualTableId + " : " + virtualTable.getVirtualGameTablePO().getId());
	}

	@Override
	public void selectRoom(SelectRoomPara params, SocketIOClient client,
			SelectRoomResponse response) {
		List<VirtualGameTable> virtualTableList = BaccaratCache.getVirtualTableByType(params.getType());
		//加入普通VIP房搜索
		virtualTableList.addAll(BaccaratCache.getVirtualTableByType(VirtualGameTableType.COMMON_VIP.get()));
		
		virtualTableList = new ArrayList<>(virtualTableList.stream().filter( item -> {
			return item.getVirtualGameTablePO().getStatusEnum() == VirtualGameTableStatus.NORMAL && 
					item.getVirtualGameTablePO().getStatusEnum() == VirtualGameTableStatus.NORMAL;
		}).collect(Collectors.toList()));
		
		int total = virtualTableList.size();
		int totalPlayer = virtualTableList.stream().mapToInt( item -> {
			return BaccaratCache.getVirtualTableSeatUserCount(item.getVirtualGameTablePO().getGameTableId(), item.getVirtualGameTablePO().getId()) + item.getBesideUser().size();
		}).sum();
		
        getVirtualTablePage(virtualTableList, params.getPageNum(),params.getPageSize(), params.getvTableId());
        
        List<SelectRoomResponse.VirtualTableInfo> vTabelInfoList = new ArrayList<>();
        virtualTableList.forEach(virtualTable -> {
        	SelectRoomResponse.VirtualTableInfo vTableInfo = response.new VirtualTableInfo();
            vTableInfo.tableId = virtualTable.getVirtualGameTablePO().getId();
            vTableInfo.name = virtualTable.getVirtualGameTablePO().getRoomName();
            vTableInfo.tid = virtualTable.getVirtualGameTablePO().getGameTableId();
            vTableInfo.minBalance = virtualTable.getVirtualGameTablePO().getMinBalance();
            
            int playerNum = BaccaratCache.getVirtualTableSeatUserCount(virtualTable.getVirtualGameTablePO().getGameTableId(), virtualTable.getVirtualGameTablePO().getId());
            int besideNum = virtualTable.getBesideUser() == null ? 0 : virtualTable.getBesideUser().size();
            vTableInfo.playerNumber = playerNum + besideNum;
            if (virtualTable.getVirtualGameTablePO().getMaxMembers().compareTo(vTableInfo.playerNumber) >= 0) {
                vTableInfo.isFull = true;
            } else {
                vTableInfo.isFull = false;
            }
            
            vTabelInfoList.add(vTableInfo);
        });
        
        response.setTotalPlayer(totalPlayer);
        response.setTotal(total);
        response.setTableList(vTabelInfoList);
	}

}
