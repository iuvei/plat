package com.na.baccarat.socketserver.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.config.BaccaratConfig;
import com.na.baccarat.socketserver.entity.BeingMiGameTable;
import com.na.baccarat.socketserver.entity.Game;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.CacheContainsException;
import com.na.user.socketserver.exception.SocketException;

/**
 * 系统缓存。
 * Created by Sunny on 2017/4/27 0027.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BaccaratCache {
	
	private static Logger log = LoggerFactory.getLogger(BaccaratCache.class);
	
	// 当前登录map的锁
	private static ReadWriteLock LOCK_CURRENT_LOGIN = new ReentrantReadWriteLock();
	
	public static RedisTemplate getRedisTemplate(){
		return (RedisTemplate)SpringContextUtil.getBean("redisTemplate");
	}
	
	private static Game game;
	
	/**
     * 登录用户缓存。
     * key: 用户ID, val : 用户对象。
     */
    private static Map<Long,User> currentLoginUserMap = new ConcurrentHashMap<>();
    
    /**
	 * 存放所有游戏下的桌子
	 * key: 游戏en, val : tid,gameTable实体map。
	 */
    private static Map<Integer, GameTable> gameTableMap;
    
    /**
     *  显示的好路 k:tid v:好路类型
     */
 	private static ConcurrentHashMap<Integer, Integer> viewRoads = new ConcurrentHashMap<Integer, Integer>();

 	/**
 	 *  用户保存的好路  k:loginId v :list tid
 	 */
 	private static ConcurrentHashMap<Long, List<Integer>> userSaveTid = new ConcurrentHashMap<Long, List<Integer>>();
    
 	/**
 	 * 多台用户
 	 */
 	private static Map<Long,User> multipleUserMap;
 	
 	public static BoundHashOperations tableIdTovirtualTableId() {
		return getRedisTemplate().boundHashOps("tableIdTovirtualTableId");
	}
 	
 	public static BoundHashOperations virtualTableType() {
		return getRedisTemplate().boundHashOps("virtualTableType");
	}
	
	public static BoundHashOperations virtualGametable() {
		return getRedisTemplate().boundHashOps("virtual_gametable");
	}
	
	public static BoundZSetOperations virtualGametableSeat() {
		return getRedisTemplate().boundZSetOps("table.virtualtable.seat");
	}

 	public synchronized static void addLoginUser(User user){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		try{
			if(currentLoginUserMap.containsKey(user.getUserPO().getId())) {
				throw CacheContainsException.createError("user.already.in.game");
			}
			currentLoginUserMap.put(user.getUserPO().getId(), user);
		}finally {
			LOCK_CURRENT_LOGIN.writeLock().unlock();
		}
	}
 	
 	public static User removeUser(Long userId){
		LOCK_CURRENT_LOGIN.writeLock().lock();
		try{
			log.info("移除百家乐用户" + userId + "");
        	return currentLoginUserMap.remove(userId);
		}finally {
			LOCK_CURRENT_LOGIN.writeLock().unlock();
		}
    }
    
    public static User getLoginUser(Long uid){
        return currentLoginUserMap.get(uid);
    }
    
    public static boolean isLoginByUserId(Long uid){
        return currentLoginUserMap.containsKey(uid);
    }
    
    public static Set<User> getUserList(Collection<Long> uid) {
    	Set<User> userList = new HashSet<>();
    	for(Long item : uid) {
    		User user = currentLoginUserMap.get(item);
    		if(user != null) {
    			userList.add(user);
    		}
    	}
        return userList;
    }
 	
    public static ConcurrentHashMap<Integer, Integer> getViewRoads() {
		return viewRoads;
	}

	public static void setViewRoads(ConcurrentHashMap<Integer, Integer> viewRoads) {
		BaccaratCache.viewRoads = viewRoads;
	}

	public static ConcurrentHashMap<Long, List<Integer>> getUserSaveTid() {
		return userSaveTid;
	}

	public static void setUserSaveTid(
			ConcurrentHashMap<Long, List<Integer>> userSaveTid) {
		BaccaratCache.userSaveTid = userSaveTid;
	}

	/**
     * 获取相关桌子的用户信息
     * @param tid
     * @return
     */
    public static Set<User> getTableUserList(Integer tid){
    	Set<User> userList = new HashSet<>();
    	
    	GameTable gameTable = gameTableMap.get(tid);
    	
    	if(gameTable.getGameTablePO().getIsMany().compareTo(1) == 0) {
    		userList.addAll(BaccaratCache.multipleUserMap.values());
    	}
    	
    	if(gameTable.getGameTablePO().isBeingTable()) {
    		BeingMiGameTable table = (BeingMiGameTable) gameTable;
    		userList.addAll(BaccaratCache.getUserList(table.getPlayers().values()));
    		userList.addAll(BaccaratCache.getUserList(table.getBesideUser()));
    	} else {
    		Set<JSONObject> seatUsers = BaccaratCache.getVirtualTableSeats(tid);
    		Collection<Long> userIds = new ArrayList<>();
    		seatUsers.forEach( item -> {
    			userIds.add(item.getLong("userId"));
    		});
    		userList.addAll(BaccaratCache.getUserList(userIds));
    	}
    	
    	return userList;
    }

	public static Map<Integer, GameTable> getGameTableMap() {
		if(gameTableMap == null) {
			gameTableMap = new HashMap<Integer, GameTable>();
		}
		return gameTableMap;
	}

	public static void setGameTableMap(
			Map<Integer, GameTable> gameTableMap) {
		BaccaratCache.gameTableMap = gameTableMap;
	}
	
	public static void clearRedisData() {
		List<String> keys = new ArrayList<>();
		keys.add("tableIdTovirtualTableId");
		keys.add("virtual_gametable");
		keys.add("virtualTableType");
		keys.add("table.virtualtable.seat");
		getRedisTemplate().delete(keys);
	}
	
	/**
	 * 设置实体桌和虚拟桌的Id关系
	 * @param table2VirtualMap
	 */
	public static void setTableId2VirtualId(Map<Integer,List<Integer>> table2VirtualMap) {
		tableIdTovirtualTableId().putAll(table2VirtualMap);
	}
	
	/**
	 * 获取实体桌和虚拟桌的Id关系
	 * @param gameTableId
	 */
	public static List<Integer> getVirtualIdByTableId(Integer gameTableId) {
		return (List<Integer>) tableIdTovirtualTableId().get(gameTableId);
	}
	
	public static List<Integer> getVirtualIdByType(VirtualGameTableType type) {
		return getVirtualIdByType(type.get());
	}
	
	public static List<Integer> getVirtualIdByType(Integer type) {
		return (List<Integer>) virtualTableType().get(type);
	}
	
	public static void setVirtualTableType(Map<Integer,List<Integer>> virtualTypeMap) {
		virtualTableType().putAll(virtualTypeMap);
	}
	
	public static void setVirtualTableMap(Map<Integer, VirtualGameTable> virtualTableMap) {
		virtualGametable().putAll(virtualTableMap);
	}
	
	public static void removeVirtualTableIdByTableId(Integer tableId, Integer virtualTableId) {
		List<Integer> idList = getVirtualIdByTableId(tableId);
		idList.remove(virtualTableId);
		tableIdTovirtualTableId().put(tableId, idList);
	}
	
	public static void freshVirtualTableIdByTableId(Integer tableId, Integer virtualTableId) {
		List<Integer> idList = getVirtualIdByTableId(tableId);
		idList.add(virtualTableId);
		tableIdTovirtualTableId().put(tableId, idList);
	}
	
	public static void freshVirtualTableByType(Integer type, Integer virtualTableId) {
		List<Integer> typeList = getVirtualIdByType(type);
		typeList.add(virtualTableId);
		virtualTableType().put(type, typeList);
	}
	
	public static void freshVirtualTable(VirtualGameTable virtualTable) {
		VirtualGameTablePO virtualGameTablePO = virtualTable.getVirtualGameTablePO();
		VirtualGameTable is = getVirtualTableById(virtualGameTablePO.getId());
		if(is == null) {
			freshVirtualTableIdByTableId(virtualGameTablePO.getGameTableId(), virtualGameTablePO.getId());
			freshVirtualTableByType(virtualGameTablePO.getType(), virtualGameTablePO.getId());
			//初始化虚拟桌座位信息
			BaccaratCache.setVirtualTableSeat(virtualGameTablePO.getGameTableId(), virtualGameTablePO.getId());
		}
		virtualGametable().put(virtualGameTablePO.getId(), virtualTable);
		
	}
	
	public static void removeVirtualTable(Integer virtualTableId) {
		virtualGametable().delete(virtualTableId);
	}
	
	public static void freshVirtualTableList(List<VirtualGameTable> vTableList) {
		Map<Integer,VirtualGameTable> freshData = new HashMap<>();
		for(VirtualGameTable item : vTableList) {
			freshData.put(item.getVirtualGameTablePO().getId(), item);
		}
		virtualGametable().putAll(freshData);
	}
	
	public static VirtualGameTable getVirtualTableById(Integer virtualTableId) {
		if(null == virtualTableId)
    		return null;
		return (VirtualGameTable) virtualGametable().get(virtualTableId);
	}
	
	/**
	 * 根据虚拟桌类型 获取虚拟桌
	 * @param type
	 * @return
	 */
	public static List<VirtualGameTable> getVirtualTableByType(Integer type) {
		List<Integer> virtualIdList = getVirtualIdByType(type);
		return (List<VirtualGameTable>) virtualGametable().multiGet(virtualIdList);
	}
	
	/**
	 * 获取对应实体桌对应的有人座位
	 */
	public static Set<JSONObject> getVirtualTableSeats(Integer tableId) {
		Set<JSONObject> seatSet = virtualGametableSeat().reverseRangeByScore((tableId * 100000), ((long)(tableId + 1) * 100000) - 1);
		return seatSet;
	}
	
	/**
	 * 获取虚拟桌对应的座位用户
	 */
	public static Map<Integer, Long> getVirtualTableSeatUser(Integer tableId, Integer virtualTableId) {
		Set<JSONObject> seatSet = virtualGametableSeat().reverseRangeByScore((tableId * 100000 + virtualTableId * 10), ((long)tableId * 100000 + virtualTableId * 10) + 9);
		Map<Integer, Long> result = new HashMap<>();
		for(JSONObject json : seatSet) {
			result.put(json.getInteger("seatNumber"), json.getLong("userId"));
		}
		
		return result;
	}
	
	public static Set<User> getVirtualTableUser(Integer tableId, Integer virtualTableId) {
		Set<User> userSet = new HashSet<>();
		Collection<Long> userIds = getVirtualTableSeatUser(tableId, virtualTableId).values();
		if(userIds != null && !userIds.isEmpty()) {
			userSet.addAll(BaccaratCache.getUserList(userIds));
		}
		Collection<Long> sideUserIds = BaccaratCache.getVirtualTableById(virtualTableId).getBesideUser();
		if(sideUserIds != null && !sideUserIds.isEmpty()) {
			userSet.addAll(BaccaratCache.getUserList(sideUserIds));
		}
		return userSet;
	}
	
	/**
	 * 用户加入虚拟桌
	 */
	public synchronized static Map<String, Object> joinVirtualTable(Integer tableId, Long userId) {
		UserPO loginUserPO = AppCache.getLoginUser(userId);
		if(loginUserPO.isInTable()) {
			throw SocketException.createError("user.exist.in.table");
		}
		Map<String, Object> result = new HashMap<>();
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	Set<JSONObject> seatSet = virtualGametableSeat().reverseRangeByScore(((long)(tableId + 1) * -100000) + 1, tableId * 100000 * -1);
		    	
		    	JSONObject jSONObject = null;
		    	if(seatSet!= null && !seatSet.isEmpty()) {
		    		jSONObject = seatSet.iterator().next();
				} else {
					throw SocketException.createError("table.have.full.person");
				}
		    	
		    	Integer virtualTableId = jSONObject.getInteger("virtualTableId");
		    	Integer seatNumber = jSONObject.getInteger("seatNumber");
		    	
				long score = ((long)tableId * 100000 + virtualTableId * 10) + seatNumber;
				
				connection.zRem("table.virtualtable.seat".getBytes(), JSONObject.toJSONBytes(jSONObject));
				jSONObject.put("userId", userId);
				connection.zAdd("table.virtualtable.seat".getBytes(), score, JSONObject.toJSONBytes(jSONObject));
				
				result.put("tableId", tableId);
				result.put("virtualTableId", virtualTableId);
		    	result.put("seatNumber", seatNumber);
		    	result.put("userId", userId);
				
				return null;
		    }
		});
		return result;
	}
	
	/**
	 * 用户加入虚拟桌
	 */
	public synchronized static Map<String, Object> joinVirtualTable(Integer tableId, Integer virtualTableId, Long userId) {
		UserPO loginUserPO = AppCache.getLoginUser(userId);
		if(loginUserPO.isInTable()) {
			throw SocketException.createError("user.exist.in.table");
		}
		Map<String, Object> result = new HashMap<>();
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	Set<JSONObject> seatSet = virtualGametableSeat().reverseRangeByScore((tableId * 100000 + virtualTableId * 10 + 9) * -1 , (tableId * 100000 + virtualTableId * 10) * -1);
		    	
		    	JSONObject jSONObject = null;
		    	if(seatSet!= null && !seatSet.isEmpty()) {
		    		jSONObject = seatSet.iterator().next();
				} else {
					throw SocketException.createError("table.have.full.person");
				}
		    	
		    	Integer virtualId = jSONObject.getInteger("virtualTableId");
		    	Integer seatNumber = jSONObject.getInteger("seatNumber");
		    	
				long score = ((long)tableId * 100000 + virtualId * 10) + seatNumber;
				
				connection.zRem("table.virtualtable.seat".getBytes(), JSONObject.toJSONBytes(jSONObject));
				jSONObject.put("userId", userId);
				connection.zAdd("table.virtualtable.seat".getBytes(), score, JSONObject.toJSONBytes(jSONObject));
				
				result.put("tableId", tableId);
				result.put("virtualTableId", virtualId);
		    	result.put("seatNumber", seatNumber);
		    	result.put("userId", userId);
				
				return null;
		    }
		});
		return result;
	}
	
	/**
	 * 当用户加入或离开房间  更新对应Redis的虚拟桌座位信息
	 * @param tableId
	 * @param virtualTableId
	 * @param seatNumber
	 * @param userId
	 */
	public synchronized static void leaveVirtualTableSeat(Integer tableId, Integer virtualTableId, Integer seatNumber, Long userId) {
		if(tableId == null) throw SocketException.createError("桌子ID不能空");
		
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	Map<String,Object> keyMap = new HashMap<>();
				keyMap.put("tableId", tableId);
				keyMap.put("virtualTableId", virtualTableId);
				keyMap.put("seatNumber", seatNumber);
				keyMap.put("userId", userId);
				long score = ((long)tableId * 100000 + virtualTableId * 10) + seatNumber;
				
				connection.zRem("table.virtualtable.seat".getBytes(), JSONObject.toJSONString(keyMap).getBytes());
				keyMap.remove("userId");
				score = score * -1;
				connection.zAdd("table.virtualtable.seat".getBytes(), score, JSONObject.toJSONString(keyMap).getBytes());
	    		
				return null;
		    }
		});
	}
	
	/**
	 * 获取虚拟桌对应的座位用户数量
	 */
	public static Integer getVirtualTableSeatUserCount(Integer tableId, Integer virtualTableId) {
		Long count = virtualGametableSeat().count((tableId * 100000 + virtualTableId * 10), ((long)tableId * 100000 + virtualTableId * 10) + 9);
		return count.intValue();
	}
	
	/**
	 * 初始化虚拟桌座位Redis存储关系
	 * @param virtualTableSeatMap
	 */
	public static void setVirtualTableSeat(Map<String,Long> virtualTableSeatMap) {
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	virtualTableSeatMap.forEach( (k,v) -> {
		    		connection.zAdd("table.virtualtable.seat".getBytes(), v, k.getBytes());
				});
		    return null;
		    }
		});
	}
	
	/**
	 * 初始化虚拟桌座位Redis存储关系
	 * @param virtualTableSeatMap
	 */
	public static void setVirtualTableSeat(Integer gameTableId, Integer vTalbeId) {
		//初始化虚拟桌座位信息
		Map<String,Long> virtualTableSeatMap = new HashMap<>();
		for(int i : BaccaratConfig.seatNums) {
			Map<String,Object> keyMap = new HashMap<>();
			keyMap.put("tableId", gameTableId);
			keyMap.put("virtualTableId", vTalbeId);
			keyMap.put("seatNumber", i);
			long score = gameTableId * 100000 + vTalbeId * 10 + i;
			virtualTableSeatMap.put(JSONObject.toJSONString(keyMap), score * -1);
		}
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	virtualTableSeatMap.forEach( (k,v) -> {
		    		connection.zAdd("table.virtualtable.seat".getBytes(), v, k.getBytes());
				});
		    return null;
		    }
		});
	}
	
	/**
	 * 初始化虚拟桌座位Redis存储关系
	 * @param virtualTableSeatMap
	 */
	public static void deleteVirtualTableSeat(Integer gameTableId, Integer vTalbeId) {
		//初始化虚拟桌座位信息
		List<String> virtualTableSeatList = new ArrayList<>();
		for(int i : BaccaratConfig.seatNums) {
			Map<String,Object> keyMap = new HashMap<>();
			keyMap.put("tableId", gameTableId);
			keyMap.put("virtualTableId", vTalbeId);
			keyMap.put("seatNumber", i);
			virtualTableSeatList.add(JSONObject.toJSONString(keyMap));
		}
		getRedisTemplate().executePipelined(new RedisCallback<Object>() {
		    public Object doInRedis(RedisConnection connection) throws DataAccessException {
		    	virtualTableSeatList.forEach( k -> {
		    		connection.zRem("table.virtualtable.seat".getBytes(), k.getBytes());
				});
		    return null;
		    }
		});
	}
	
	public static List<VirtualGameTable> getVirtualTableByTableId(Integer gameTableId) {
		List<Integer> virtualIdList = getVirtualIdByTableId(gameTableId);
		return (List<VirtualGameTable>) virtualGametable().multiGet(virtualIdList);
	}
	
	/**
	 * 获取所有的虚拟桌Map
	 * @return
	 */
	public static Map<Integer, VirtualGameTable> getAllVirtualTable() {
		return virtualGametable().entries();
    }
	
	/**
	 * 返回所有实桌列表。
	 * @return
	 */
	public static List<GameTable> getAllGameTable(){
		List<GameTable> gameTables = new ArrayList<>();
		gameTableMap.forEach((key,item)->{
			gameTables.add(item);
		});
		return gameTables;
	}

	/**
	 * 获取指定tableId的GameTable实体。
	 * @param tableId
	 * @return
	 */
	public static GameTable getGameTableById(Integer tableId){
		return gameTableMap.get(tableId);
	}
    
    /**
	 * 获取既定socketIOClient
	 * @param client
	 * @return
	 */
	public static User getUserByClient(SocketIOClient client){
		if (client==null) return null;
		String userIdStr = client.get(SocketClientStoreEnum.USER_ID.get());
		if(userIdStr==null) return null;
		Long userId = Long.valueOf(userIdStr);
		User user = currentLoginUserMap.get(userId);
		if(user == null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		return user;
	}

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		BaccaratCache.game = game;
	}
	
	public static User removeMultipleUser(Long userId) {
		return multipleUserMap.remove(userId);
	}
	
	public static User getMultipleUserMap(Long userId) {
		return multipleUserMap.get(userId);
	}
	
	public static boolean containMultipleUserMap(Long userId) {
		return multipleUserMap.containsKey(userId);
	}
	
	public static Map<Long, User> getMultipleUserMap() {
		if(multipleUserMap == null) {
			multipleUserMap = new ConcurrentHashMap<>();
		}
		return multipleUserMap;
	}

	public static void setMultipleUserMap(Map<Long, User> multipleUserMap) {
		BaccaratCache.multipleUserMap = multipleUserMap;
	}
	
	public static List<GameTable> getMultipleTableList() {
		return BaccaratCache.gameTableMap.values().stream().filter( item -> {
			if(item.getGameTablePO().getIsMany().compareTo(1) == 0) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	public static boolean isMultipleTalbe(Integer tableId) {
		GameTable table = BaccaratCache.getGameTableById(tableId);
		if(null == table)
			return false;
		return true;
	}

	public static Map<Long, User> getCurrentLoginUserMap() {
		return currentLoginUserMap;
	}
	
	
}
