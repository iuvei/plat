package com.na.roulette.socketserver.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.roulette.socketserver.entity.RouletteGame;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.roulette.socketserver.entity.RouletteUser;
import com.na.roulette.socketserver.entity.RouletteUserBet;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;

/**
 * 轮盘系统缓存。 Created by Sunny on 2017/4/27 0027.
 */
public class RouletteCache {
	
	private static RouletteGame game;

	private static ReadWriteLock LOCK_CURRENT_LOGIN = new ReentrantReadWriteLock();
	
	/**
	 * 登录用户缓存。 key: 用户ID, val : 用户对象。
	 */
	private static Map<Long, RouletteUser> currentLoginUserMap = new ConcurrentHashMap<>();
	/**
	 * 存放所有游戏下的桌子
	 *  tid,gameTable实体map。
	 */
    private static Map<Integer, RouletteGameTable> gameTableMap = new ConcurrentHashMap<>();
    
    private static Map<Integer,List<TradeItem>> tradeItemMap = new ConcurrentHashMap<>(); 
    
    /**
 	 * 用户投注细节
 	 */
 	private static List<RouletteUserBet> userBetDetailList = new CopyOnWriteArrayList<>();
 	
 	
 	
 	/**
     * 获取相关桌子的用户信息
     * @param tid
     * @return
     */
    public static List<RouletteUser> getTableUserList(Integer tid){
    	List<RouletteUser> userList = new ArrayList<>();
    	
    	RouletteGameTable gameTable = gameTableMap.get(tid);
    	userList.addAll(gameTable.getPlayers().values());
    	
    	return userList;
    }

	/**
	 * 获取所有桌子的用户信息
	 * @return
	 */
	public static List<RouletteUser> getAllTableUserList(){
		List<RouletteUser> userList = new ArrayList<>();
		Map<Integer, RouletteGameTable> gameTableMap = getGameTableMap();
		if(gameTableMap == null) return userList;
		gameTableMap.forEach((key, value)->{
			userList.addAll(value.getPlayers().values());
		});
		return userList;
	}
 	
 	/**
 	 * 增加用户投注记录
 	 * @return
 	 */
 	public static void addUserBetDetail(RouletteUserBet userbet){
 		if(userbet == null ){
 			return;
 		}
 		boolean isAdd = false;
 		for (RouletteUserBet userBetDetail : userBetDetailList) {
 			if(userBetDetail != null && userBetDetail.getRoundId().longValue() == userbet.getRoundId().longValue()
 					&& userBetDetail.getTableId().intValue() == userbet.getTableId().intValue()
 					&& userBetDetail.getTradeId().intValue() == userbet.getTradeId().intValue()){
 				isAdd = true;
 				userBetDetail.setAmount(userBetDetail.getAmount().add(userbet.getAmount()));
 			}
		}
 		if(!isAdd){
 			userBetDetailList.add(userbet);
 		}
 	}
 	
 	/**
 	 * 获取对应座子和用户的投注记录
 	 * @param tableId 桌子id
 	 * @param uid 用户id
 	 * @return
 	 */
 	public static List<RouletteUserBet> getUserBetDetail(Integer tableId,Long uid){
 		if(tableId == null || uid == null){
 			return null;
 		}
 		return userBetDetailList.stream().filter(item -> item.getTableId().equals(tableId) && item.getUid().equals(uid)).collect(Collectors.toList());
 	}
 	
 	/**
 	 * 移除对应座子和用户的投注记录
 	 * @param tableId 桌子id
 	 * @param roundId 局id
 	 * @return
 	 */
 	public static void removeUserBetDetail(Integer tableId,Long roundId){
 		if(tableId == null || roundId == null){
 			return;
 		}
 		userBetDetailList = userBetDetailList.stream().filter(item -> !item.getTableId().equals(tableId) && !item.getRoundId().equals(roundId)).collect(Collectors.toCollection(CopyOnWriteArrayList::new));
 	}

    
    public static List<TradeItem> getTradeItemList(Integer playId){
    	return tradeItemMap.get(playId);
    }
    public static List<TradeItem> putTradeItemList(Integer playId,List<TradeItem> tradeItemList){
    	return tradeItemMap.get(playId);
    }
	/**
	 * 获取既定socketIOClient
	 * @param client
	 * @return
	 */
	public static RouletteUser getUserByClient(SocketIOClient client) {
		if (client == null)
			return null;
		String userIdStr = client.get(SocketClientStoreEnum.USER_ID.get());
		if (userIdStr == null)
			return null;
		Long userId = Long.valueOf(userIdStr);

		return getLoginUser(userId);
	}
	
	/**
	 * 获取指定tableId的GameTable实体。
	 * @param tableId
	 * @return
	 */
	public static RouletteGameTable getGameTableById(Integer tableId){
		return gameTableMap.get(tableId);
	}

	public static RouletteGame getGame() {
		return game;
	}
	public static void setGame(RouletteGame game) {
		RouletteCache.game = game;
	}
	
	public static Map<Long, RouletteUser> getCurrentLoginUserMap() {
		return currentLoginUserMap;
	}

	public static void setCurrentLoginUserMap(
			Map<Long, RouletteUser> currentLoginUserMap) {
		RouletteCache.currentLoginUserMap = currentLoginUserMap;
	}
	
	public static RouletteUser getLoginUser(Long uid){
		LOCK_CURRENT_LOGIN.readLock().lock();
		RouletteUser user = currentLoginUserMap.get(uid);
		LOCK_CURRENT_LOGIN.readLock().unlock();
        return user;
	}
	
	public static void addLoginUser(RouletteUser user){
		LOCK_CURRENT_LOGIN.writeLock().lock();
        currentLoginUserMap.put(user.getUserPO().getId(), user);
		LOCK_CURRENT_LOGIN.writeLock().unlock();
    }

	/**
	 * 将当前用户移除。和用户所有相关的内存信息都要移除掉。
	 * @param user
	 */
	public static void removeUser(RouletteUser user){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		getCurrentLoginUserMap().remove(user.getUserPO().getId());
		LOCK_CURRENT_LOGIN.writeLock().unlock();
	}
	
	public static void removeUser(Long userId){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		getCurrentLoginUserMap().remove(userId);
		LOCK_CURRENT_LOGIN.writeLock().unlock();
	}

	public static Map<Integer, RouletteGameTable> getGameTableMap() {
		return gameTableMap;
	}

	public static void setGameTableMap(Map<Integer, RouletteGameTable> gameTableMap) {
		RouletteCache.gameTableMap = gameTableMap;
	}
	
	

}
