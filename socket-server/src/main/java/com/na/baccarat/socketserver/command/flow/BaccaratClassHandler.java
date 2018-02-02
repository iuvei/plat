package com.na.baccarat.socketserver.command.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.flow.table.Table;
import com.na.baccarat.socketserver.command.flow.table.room.RoomAction;
import com.na.baccarat.socketserver.common.enums.GameTableTypeEnum;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.flow.game.GameAction;
import com.na.user.socketserver.common.enums.DealerUserTypeEnum;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.LiveUserTypeEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.ConvertUtil;

/**
 * 1.  根据用户判断类型   分别处理玩家  荷官 以及荷官主管
 * 2.  玩家 根据进入房间的不同  处理不同的逻辑   分为 多台 普通和竞咪
 * 用户： User    PlayerUser   Dealer  DealerManager 
 * 游戏： Game    BaccaratGame RouletteGame (暂不考虑)
 * 用户基本动作为  login logout reconnect 
 *  进入百家乐后   BaccaratAction  ->  BaccaratPlayerAction, BaccaratDealerAction,BaccaratDealerManagerAction
 * 桌子： Table   NormalTable  BeingMiTable  MultipleTable
 * 房间： Room    NormalRoom  AgentVipRoom
 * 
 * 用户登陆    根据账号获取数据    
 * 根据不同的玩家 走不同的login
 * 加入桌子   若发送join  进入百家乐处理逻辑
 * 根据参数进入不同的桌子  处理join流程 
 * 
 * 进入玩家相应的处理流程后    在根据不同的房间 走不同房间的流程
 */
@Component
public final class BaccaratClassHandler {
	
	@Autowired
	private GameAction baccaratPlayerUser;
	
	@Autowired
	private GameAction baccaratDealerUser;
	
	@Autowired
	private GameAction baccaratDealerManagerUser;
	
	@Autowired
	private Table normalTable;
	
	@Autowired
	private Table quickTable;
	
	@Autowired
	private Table beingMiTable;
	
	@Autowired
	private RoomAction normalRoom;
	
	@Autowired
	private RoomAction agentVipRoom;
	
	public GameAction baccaratAction(SocketIOClient client) {
		Long userId = ConvertUtil.toLong(client.get(SocketClientStoreEnum.USER_ID.get()));
		if(userId == null || userId == 0) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		return baccaratAction(AppCache.getLoginUser(userId));
	}
	
	public GameAction baccaratAction(UserPO loginUserPO) {
		if(loginUserPO == null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		
		if(UserTypeEnum.REAL == loginUserPO.getUserTypeEnum()) {
			LiveUserPO liveUserPO = (LiveUserPO) loginUserPO;
			if(LiveUserTypeEnum.PLAYER == liveUserPO.getTypeEnum()) {
				return baccaratPlayerUser;
			} else {
				throw SocketException.createError("user.type.error");
			}
		} else if (UserTypeEnum.DEALER == loginUserPO.getUserTypeEnum()) {
			DealerUserPO dealerUserPO = (DealerUserPO) loginUserPO;
			if(DealerUserTypeEnum.DEALER == dealerUserPO.getTypeEnum()) {
				return baccaratDealerUser;
			} else if (DealerUserTypeEnum.CHECKER == dealerUserPO.getTypeEnum()) {
				return baccaratDealerManagerUser;
			} else {
				throw SocketException.createError("user.type.error");
			}
		} else {
			throw SocketException.createError("user.type.error");
		}
	}
	
	public Table table(GameTable table) {
		if(table == null) {
			throw SocketException.createError("table.not.exist");
		}
		if(table.getGameTablePO().getTypeEnum() == GameTableTypeEnum.MI_BEING) {
			return beingMiTable;
		} else if(table.getGameTablePO().getTypeEnum() == GameTableTypeEnum.MI_NORMAL) {
			return normalTable;
		} else if(table.getGameTablePO().getTypeEnum() == GameTableTypeEnum.NOTMI_NORMAL) {
			return quickTable;
		} else {
			throw SocketException.createError("table.not.exist");
		}
	}
	
	public RoomAction roomAction(Integer virtualTableId) {
		if(StringUtils.isEmpty(virtualTableId)) {
			return normalRoom;
		} else {
			//加入指定房间
			VirtualGameTable virtualTable = BaccaratCache.getVirtualTableById(virtualTableId);
			VirtualGameTablePO virtualGameTablePO = virtualTable.getVirtualGameTablePO();
			return roomAction(virtualGameTablePO.getTypeEnum());
		}
	}
	
	public RoomAction roomAction(VirtualGameTableType virtualGameTableType) {
		if(VirtualGameTableType.COMMON == virtualGameTableType) {
			return normalRoom;
		}if(VirtualGameTableType.COMMON_VIP == virtualGameTableType) {
			return agentVipRoom;
		} else if(VirtualGameTableType.AGENT_VIP == virtualGameTableType) {
			return agentVipRoom;
		} else {
			throw SocketException.createError("");
		}
	}

}
