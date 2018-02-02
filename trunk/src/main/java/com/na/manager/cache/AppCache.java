package com.na.manager.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.na.manager.entity.Dict;
import com.na.manager.entity.Game;
import com.na.manager.entity.GameTable;
import com.na.manager.entity.I18N;
import com.na.manager.entity.IpBlackWhiteAddr;
import com.na.manager.entity.Menu;
import com.na.manager.entity.Permission;
import com.na.manager.entity.User;
import com.na.monitor.core.NodeData;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public final class AppCache {
	public static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

	/**
	 * 一级菜单
	 */
	private static Map<Long, Menu> MENUMAP = new HashMap<>();

	/**
	 * 系统权限
	 */
	private static Map<String, Permission> PERMISSIONMAP = new HashMap<>();

	/**
	 * 游戏全局变量
	 */
	public static Map<Integer, Game> GAMEMAP = new HashMap<>();

	/**
	 * 游戏桌全局变量
	 */
	public static Map<Integer, GameTable> GAMETABLEMAP = new HashMap<>();

	private static Map<String,I18N> I18NMap = new ConcurrentHashMap<>();

	/**
	 * 字典内存数据。
	 */
	private static Multimap<String,Dict> dictMultimap = ArrayListMultimap.create();
	
	/**
	 * 系统黑白名单
	 */
	private static Map<String, List<IpBlackWhiteAddr>> BLACKWHITEIPMAP = new HashMap<>();
	
	/**
	 * 系统黑白名单
	 */
	private static Map<String, NodeData> SERVERLISTMAP = new HashMap<>();
	
	public static Locale getLocalLanguage(){
		return Locale.CHINESE;
	}
	public static synchronized void initGame(List<Game> games) {
		GAMEMAP.clear();
		for (Game game : games) {
			GAMEMAP.put(game.getId(), game);
		}
	}
	
	public static Collection<Game> getAllGame() {
		return GAMEMAP.values();
	}

	public static Game getGame(Integer gid) {
		return GAMEMAP.get(gid);
	}

	public static synchronized void initGameTable(List<GameTable> tables) {
		GAMETABLEMAP.clear();
		for (GameTable table : tables) {
			GAMETABLEMAP.put(table.getId(), table);
		}
	}

	public static GameTable getGameTable(Integer gameTableId) {
		return GAMETABLEMAP.get(gameTableId);
	}
	public static Collection<GameTable> getAllGameTable() {
		return GAMETABLEMAP.values();
	}
	
	public static Menu getMenuBy(Long groupId){
		return MENUMAP.get(groupId);
	}
	public static synchronized void initMenu(List<Menu> menus){
		menus.forEach(item->{
			MENUMAP.put(item.getId(),item);
		});
	}
	public static synchronized void initPermission(List<Permission> permissions){
		permissions.forEach(item->{
			PERMISSIONMAP.put(item.getPermissionID(), item);
		});
	}

	/**
	 * 返回当前登录用户
	 * @return
	 */
	public static User getCurrentUser(){
		return userThreadLocal.get();
	}

	public static void setCurrentUser(User user){
		userThreadLocal.set(user);
	}
	
	public static Map<String, I18N> getI18NMap() {
		return I18NMap;
	}
	
	public static I18N getI18NMap(String key) {
		return I18NMap.get(key);
	}

	public static void setI18NMap(Map<String, I18N> i18nMap) {
		I18NMap = i18nMap;
	}

	public static void initDictMap(List<Dict> dictList){
		dictList.forEach(item ->{
			dictMultimap.put(item.getType(),item);
		});

	}

	public static Multimap<String, Dict> getDictMultimap() {
		return dictMultimap;
	}
	
	public static void initBlackWhiteIpMap(List<IpBlackWhiteAddr> ipList){
		BLACKWHITEIPMAP.clear();
		List<IpBlackWhiteAddr> list = null;
		for(IpBlackWhiteAddr ipAddr : ipList){
			if(BLACKWHITEIPMAP.containsKey(ipAddr.getKey())){
				BLACKWHITEIPMAP.get(ipAddr.getKey()).add(ipAddr);
			}else{
				list = new ArrayList<>();
				list.add(ipAddr);
				BLACKWHITEIPMAP.put(ipAddr.getKey(), list);
			}
		}
	}
	
	public static Map<String, List<IpBlackWhiteAddr>> getBlackWhiteIpMap(){
		return BLACKWHITEIPMAP;
	}
	
	public static void initServerListMap(List<NodeData> list){
		SERVERLISTMAP.clear();
		list.forEach(item->{
			if(!SERVERLISTMAP.containsKey(item.getServerAddress()+":"+item.getServerPort())){
				SERVERLISTMAP.put(item.getServerAddress()+":"+item.getServerPort(), item);
			}
		});
	}
	
	public static Map<String,NodeData> getServerListMap(){
		return SERVERLISTMAP;
	}
}
