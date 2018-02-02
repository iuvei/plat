package com.na.baccarat.socketserver.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.table.BeingMiTable;
import com.na.baccarat.socketserver.command.flow.table.NormalTable;
import com.na.baccarat.socketserver.command.flow.table.room.RoomAction;
import com.na.baccarat.socketserver.command.flow.user.BaccaratPlayerUser;
import com.na.baccarat.socketserver.command.request.TradeItemCommand;
import com.na.baccarat.socketserver.command.requestpara.TradeItemPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.JoinRoomResponse;
import com.na.baccarat.socketserver.common.enums.BetOrderSourceEnum;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.UserBet;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.event.ReconnectEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 百家乐重连监听
 * 
 * @author alan
 * @date 2017年5月24日 下午12:06:45
 */
@Component("baccaratReconnectEventListener")
public class ReconnectEventListener implements ApplicationListener<ReconnectEvent> {
	
    private final static Logger log = LoggerFactory.getLogger(ReconnectEventListener.class);

    @Autowired
    private SocketIOServer socketIOServer;
    
    @Autowired
    private TableCommand tableCommand;
    
    @Autowired
    private RoomCommand roomCommand;
    
    @Autowired
    private NormalTable normalTable;
    
    @Autowired
    private BeingMiTable beingMiTable;
    
    @Autowired
    private RoomAction normalRoom;
    
    @Autowired
    private BaccaratPlayerUser baccaratPlayerUser;
    
    @Autowired
	private TradeItemCommand tradeItemCommand;

    @Override
    public void onApplicationEvent(ReconnectEvent reconnectEvent) {
    	SocketIOClient client = reconnectEvent.getSource();
        if(client == null) {
        	log.error("客户端已经断开");
            return ;
        }
        
        if(GameEnum.BACCARAT != GameEnum.get(client.get(SocketClientStoreEnum.GAME_CODE.get()))) {
            return ;
        }
    	
        UserPO userPO = AppCache.getUserByClient(client);
        if(userPO == null ) {
        	throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
        }
        
        //用户掉线重连机制
  		User leaveSeatUser = BaccaratCache.getLoginUser(userPO.getId());
  		if (leaveSeatUser!=null && leaveSeatUser.getUserPO().isInTable()) {
  			GameTable table = BaccaratCache.getGameTableById(leaveSeatUser.getUserPO().getTableId());
  			
  			client.set(SocketClientStoreEnum.TABLE_ID.get(),leaveSeatUser.getUserPO().getTableId() + "");
  			JoinRoomResponse response = new JoinRoomResponse();
  			response.setSourceEnum(leaveSeatUser.getSource());
  			if(BetOrderSourceEnum.MANY_TYPE == leaveSeatUser.getSource()) {
  				response.setTableType(9);
  				
  				List<UserBet> userBetInfos = new ArrayList<>();
  				for(GameTable item2 : BaccaratCache.getMultipleTableList()) {
  					userBetInfos.addAll(item2.getRound().getUserBetedInfos().stream().filter( item -> {
  						return item.getUserId().compareTo(userPO.getId()) == 0;
  					}).collect(Collectors.toList()));
  				}
  				
  				baccaratPlayerUser.getResponseData(response, userBetInfos);
  				
  				roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
  				
  				TradeItemPara tradeItemPara = new TradeItemPara();
  				tradeItemPara.setPlayId(1);
  				tradeItemCommand.exec(client, tradeItemPara);
  				//发送多台桌子信息
  				BaccaratCache.getMultipleTableList().forEach( item -> {
  					tableCommand.sendTableStatus(client, item.getGameTablePO().getId());
  				});
  			} else {
  				
  				if(table.getGameTablePO().isBeingTable()) {
  					List<UserBet> userBetInfos = table.getRound().getUserBetedInfos().stream().filter( item -> {
  	  					return item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(userPO.getId()) == 0;
  	  				}).collect(Collectors.toList());
  					
  	  				BeingMiGameTable beingMiGameTable = (BeingMiGameTable) table;
  	  				beingMiTable.getResponseData(response, beingMiGameTable.getPlayers(), userBetInfos, table, userPO.getId());
  	  			} else {
  	  				VirtualGameTable vtable = BaccaratCache.getVirtualTableById(leaveSeatUser.getVirtualTableId());
  	  				
  	  				client.set(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get(),leaveSeatUser.getVirtualTableId() + "");
  	  				
  	  				response.setVirtualTableId(leaveSeatUser.getVirtualTableId());
  	  				response.setVirtualTableTypeEnum(vtable.getVirtualGameTablePO().getTypeEnum());
  	  				normalTable.getResponseData(response, table);
  	  				
	  	  			List<UserBet> userBetInfos = table.getRound().getUserBetedInfos().stream().filter( item -> {
		  	  			if(item.getVirtualTableId().compareTo(leaveSeatUser.getVirtualTableId()) == 0) {
		  	  				return item.getSource() == BetOrderSourceEnum.SEAT || item.getUserId().compareTo(userPO.getId()) == 0;
		  	  			}
		  	  			return false;
		  	  		}).collect(Collectors.toList());
  	  				normalRoom.getResponseData(response, BaccaratCache.getVirtualTableSeatUser(vtable.getVirtualGameTablePO().getGameTableId(), 
  	  						vtable.getVirtualGameTablePO().getId()), userBetInfos, userPO.getId());
  	  			}
  				if(BetOrderSourceEnum.SEAT == leaveSeatUser.getSource()) {
  					response.setSeatNum(leaveSeatUser.getSeat());
  				}
  				response.setTableId(leaveSeatUser.getUserPO().getTableId());
  				response.setTableType(table.getGameTablePO().getType());
  				response.setChipId(leaveSeatUser.getChipId());
  				response.setNickName(leaveSeatUser.getUserPO().getNickName());
  	  			roomCommand.send(client, RequestCommandEnum.COMMON_JOIN_ROOM, response);
  	  			//发送桌子信息
  				tableCommand.sendTableStatus(client);
  			}
  		}
    }
}
