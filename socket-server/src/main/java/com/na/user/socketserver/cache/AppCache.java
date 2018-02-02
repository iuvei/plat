package com.na.user.socketserver.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.I18NPO;
import com.na.user.socketserver.entity.IpBlackWhiteAddr;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.util.Md5Util;

/**
 * 系统缓存。
 * Created by Sunny on 2017/4/27 0027.
 */
public class AppCache {

	// 当前登录map的锁
	private static ReadWriteLock LOCK_CURRENT_LOGIN = new ReentrantReadWriteLock ();
	// 断线用户的锁
	private static ReadWriteLock  LOCK_DIS_CONNECT_USER = new ReentrantReadWriteLock ();
	// 断线用户socketClient的锁
	private static ReadWriteLock  LOCK_DIS_CONNECT_CLIENT = new ReentrantReadWriteLock ();

    /**
     * 游戏缓存。
     * key: en,val: game实体.
     */
    private static Map<GameEnum,GamePO> gameMap = new HashMap<>();
    
    /**
     * 登录用户缓存。
     * key: 用户ID, val : 用户对象。
     */
    private static Map<Long,UserPO> currentLoginUserMap = new ConcurrentHashMap<>();
    
 	/**
 	 * 玩法列表
 	 */
 	private static List<Play> playList;
 	
 	/**
     * 掉线用户缓存。
     * key: 用户ID, val : 用户对象。
     */
    private static Map<Long,UserPO> disConnectUserMap = new ConcurrentHashMap<>();
    private static Map<Long,SocketIOClient> disConnectClientMap = new ConcurrentHashMap<>();
    
    private static Map<String,I18NPO> I18NMap = new ConcurrentHashMap<>();
    
    /**
	 * 系统黑白名单 
	 */
	private static Map<String, List<IpBlackWhiteAddr>> blackWhiteIPMap = new HashMap<>();

	public static void initGame(List<GamePO> gameList){
        gameList.forEach(item->{
            gameMap.put(item.getGameEnum(),item);
        });
    }
    
    public static Map<GameEnum,GamePO> getGame(){
        return gameMap;
    }
    
    public static GamePO getGame(GameEnum gameCode){
		return gameMap.get(gameCode);
    }

	public static GamePO getGame(Integer gameId){
		for(Map.Entry<GameEnum,GamePO> item : gameMap.entrySet()){
			if(item.getValue().getId().equals(gameId)){
				return item.getValue();
			}
		}
		return null;
	}

	public static Map<GameEnum, GamePO> getGameMap() {
		return gameMap;
	}

    public static void addLoginUser(UserPO user){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		try {
        	currentLoginUserMap.put(user.getId(),user);
		}finally {
			LOCK_CURRENT_LOGIN.writeLock().unlock();
		}
    }
    
    public static UserPO removeLoginUser(UserPO user){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		try {
			return currentLoginUserMap.remove(user.getId());
		}finally {
			LOCK_CURRENT_LOGIN.writeLock().unlock();
		}
    }
    
    public static UserPO getLoginUser(Long uid){
        return currentLoginUserMap.get(uid);
    }
    
    public static Map<Long, UserPO> getCurrentLoginUserMap() {
		return currentLoginUserMap;
	}

	public static List<UserPO> getLoginUserList(Collection<Long> uids) {
    	LOCK_CURRENT_LOGIN.readLock().lock();
    	try {
			return currentLoginUserMap.values().stream().filter(item -> {
				return uids.contains(item.getId());
			}).collect(Collectors.toList());
		}finally {
			LOCK_CURRENT_LOGIN.readLock().unlock();
		}
	}
    
	public static List<Play> getPlayList() {
		return playList;
	}

	public static void setPlayList(List<Play> playList) {
		AppCache.playList = playList;
	}
    
	public static Map<Long, UserPO> getDisConnectUserMap() {
		return disConnectUserMap;
	}
	
	public static UserPO getDisConnectUser(Long uid) {
		return disConnectUserMap.get(uid);
	}
	
	public static UserPO removeDisConnectUser(Long userId) {
		LOCK_DIS_CONNECT_USER.writeLock().lock();
		try {
			return disConnectUserMap.remove(userId);
		}finally {
			LOCK_DIS_CONNECT_USER.writeLock().unlock();
		}
	}

	public static void addDisConnectUserMap(UserPO disConnectUserMap) {
		LOCK_DIS_CONNECT_USER.writeLock().lock();
		try {
			AppCache.disConnectUserMap.put(disConnectUserMap.getId(), disConnectUserMap);
		}finally {
			LOCK_DIS_CONNECT_USER.writeLock().unlock();
		}
	}

	/**
	 * 获取既定socketIOClient
	 * @param client
	 * @return
	 */
	public static UserPO getUserByClient(SocketIOClient client){
		if (client==null) return null;
		String userIdStr = client.get(SocketClientStoreEnum.USER_ID.get());
		if(userIdStr==null) return null;
		Long userId = Long.valueOf(userIdStr);
		return currentLoginUserMap.get(userId);
	}

	/**
	 * 查询指定交易项.
	 * @param tradeItemId
	 * @return
	 */
	public static TradeItem getTradeItem(Integer tradeItemId){
		for(Play play : playList){
			if(play.getTradeList()!=null){
				Optional<TradeItem> tradeItemOptional = play.getTradeList().stream().filter(tradeItem -> tradeItem.getId().equals(tradeItemId)).findFirst();
				if(tradeItemOptional.isPresent())return tradeItemOptional.get();
			}
		}
		return null;
	}

	/**
	 * 查询指定玩法的所有交易项.
	 * @param playId
	 * @return
	 */
	public static List<TradeItem> getTradeItemListByPlayId(Integer playId){
		if(playList==null) return null;

		Optional<Play> playOptional = playList.stream()
				.filter(play->play.getId().equals(playId))
				.findFirst();

		return playOptional.isPresent() ? playOptional.get().getTradeList() : new ArrayList<TradeItem>();
	}

	/**
	 * 返回指定玩法。
	 * @param playId
	 * @return
	 */
	public static Play getPlay(Integer playId){
		return playList.stream().filter(play->play.getId().equals(playId)).findFirst().orElseGet(null);
	}

	public static Map<Long, SocketIOClient> getDisConnectClientMap() {
		return disConnectClientMap;
	}
	
	public static SocketIOClient getDisConnectClientMap(Long userId) {
		return disConnectClientMap.get(userId);
	}
	
	public static SocketIOClient removeDisConnectClient(Long userId) {
		LOCK_DIS_CONNECT_CLIENT.writeLock().lock();
		try {
			return disConnectClientMap.remove(userId);
		}finally {
			LOCK_DIS_CONNECT_CLIENT.writeLock().unlock();
		}

	}
	
	public static void addDisConnectClient(Long userId, SocketIOClient client) {
		LOCK_DIS_CONNECT_CLIENT.writeLock().lock();
		try {
			disConnectClientMap.put(userId, client);
		}finally {
			LOCK_DIS_CONNECT_CLIENT.writeLock().unlock();
		}

	}

	public static void setDisConnectClientMap(
			Map<Long, SocketIOClient> disConnectClientMap) {
		AppCache.disConnectClientMap = disConnectClientMap;
	}

	public static Map<String, I18NPO> getI18NMap() {
		return I18NMap;
	}
	
	public static I18NPO getI18NMap(String key) {
		return I18NMap.get(key);
	}

	public static void setI18NMap(Map<String, I18NPO> i18nMap) {
		I18NMap = i18nMap;
	}
	
	public static void initBlackWhiteIpMap(List<IpBlackWhiteAddr> ipList){
		blackWhiteIPMap.clear();
		List<IpBlackWhiteAddr> list = null;
		for(IpBlackWhiteAddr ipAddr : ipList){
			if(blackWhiteIPMap.containsKey(ipAddr.getKey())){
				blackWhiteIPMap.get(ipAddr.getKey()).add(ipAddr);
			}else{
				list = new ArrayList<>();
				list.add(ipAddr);
				blackWhiteIPMap.put(ipAddr.getKey(), list);
			}
		}
	}
	
	public static Map<String, List<IpBlackWhiteAddr>> getBlackWhiteIpMap(){
		return blackWhiteIPMap;
	}

}
