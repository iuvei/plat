package com.na.baccarat.socketserver.command.flow.table.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.requestpara.JoinRoomPara;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserService;

public abstract class RoomAction {
	
	private Logger log = LoggerFactory.getLogger(RoomAction.class);
	
	@Autowired
	RoomCommand roomCommand;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	SocketIoConfig socketIoConfig;
	
	public abstract void join(JoinRoomPara params, SocketIOClient client, JoinRoomResponse response);
	
	public abstract void selectRoom(SelectRoomPara params, SocketIOClient client, SelectRoomResponse response);
	
	/**
	 * 选择房间根据页数过滤数据
	 * @param virtualTableList
	 * @param pageNumber
	 */
	protected void getVirtualTablePage(List<VirtualGameTable> virtualTableList, Integer pageNumber,Integer pagerSize, Integer vTableId) {
		
		List<VirtualGameTable> copyVirtualTableList;
		if(vTableId != null) {
			//搜索
			copyVirtualTableList = virtualTableList.parallelStream().filter(item -> {
				return item.getVirtualGameTablePO().getId().compareTo(vTableId) == 0;
			}).collect(Collectors.toList());
		} else {
	        int pager = 0;
	        
	        if(pageNumber == null || pageNumber.compareTo(1) <= 0) {
	        	pager = 1;
	        } else {
	        	pager = pageNumber;
	        }
	        
	        int beginIndex = (pager - 1) * pagerSize + 1;
	        int endIndex = pager * pagerSize;
			
	        log.debug("【selectRoom】选择房间Index :" + beginIndex + "~~~" + endIndex);
			if(virtualTableList.size() < beginIndex) {
				throw SocketException.createError("select.room.no.data");
			} else {
				if(virtualTableList.size() < endIndex) {
					endIndex = virtualTableList.size();
				}
				copyVirtualTableList = virtualTableList.subList(beginIndex - 1, endIndex);
			}
		}
		copyVirtualTableList = new ArrayList<>(copyVirtualTableList);
		virtualTableList.clear();
		virtualTableList.addAll(copyVirtualTableList);
	}
	
	public void getResponseData(JoinRoomResponse response,Map<Integer, Long> userMap,  List<UserBet> userBetInfos, Long userId) {
		//获取该桌用户信息
		List<JoinRoomResponse.UserInfo> seatList = new ArrayList<>();
		Set<User> userList = BaccaratCache.getUserList(userMap.values());
		userList.forEach( value -> {
			JoinRoomResponse.UserInfo userInfo = response.new UserInfo();
			userInfo.userId = value.getUserPO().getId();
			userInfo.seat = value.getSeat();
			userInfo.countryCode = value.getCountryCode();
			userInfo.nickName = value.getUserPO().getNickName();
			userInfo.balance = value.getUserPO().getBalance().doubleValue();
			userInfo.userPicture = value.getUserPO().getHeadPic();
			seatList.add(userInfo);
		});
		response.setSeatList(seatList);
		
		//获取用户已下注数量
		List<JoinRoomResponse.UserBetInfo> userBetList = new ArrayList<>();
		userBetInfos.forEach( item -> {
			JoinRoomResponse.UserBetInfo userBetInfo = userBetList.stream().filter( item2 -> {
				return item2.userId.compareTo(item.getUserId()) == 0;
			}).findFirst().orElse(null);
			List<JoinRoomResponse.UserBetDetailInfo> userBetDetailInfoList;
			
			if(userBetInfo == null) {
				userBetInfo = response.new UserBetInfo();
				userBetInfo.userId = item.getUserId();
				userBetDetailInfoList = new ArrayList<>();
				userBetInfo.betList = userBetDetailInfoList;
				userBetList.add(userBetInfo);
			} else {
				userBetDetailInfoList = userBetInfo.betList;
			}
			
			JoinRoomResponse.UserBetDetailInfo userBetDetailInfo = response.new UserBetDetailInfo();
			userBetDetailInfo.tradeId = item.getTradeId();
			userBetDetailInfo.number = item.getAmount();
			userBetDetailInfoList.add(userBetDetailInfo);
		});
		response.setUserBetList(userBetList);
	}
}
