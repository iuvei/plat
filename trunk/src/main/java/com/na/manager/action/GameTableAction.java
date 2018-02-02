package com.na.manager.action;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.na.manager.bean.NaResponse;
import com.na.manager.cache.AppCache;
import com.na.manager.common.ActiveMQUtil;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.GameTable;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.remote.IGameRemote;
import com.na.manager.service.IGameService;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.IVirtualGameTableService;

/**
 * game table
 * @author Administrator
 *
 */
@RestController
@RequestMapping("gametable")
@Auth("GameTableManager")
public class GameTableAction {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IGameTableService gameTableService;
	@Autowired
	private IGameService gameService;

	@Autowired
	private IVirtualGameTableService virtualGameTableService;
	
	@Autowired
	private ActiveMQUtil activeMQUtil;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private IGameRemote gameRemote;
	
	/**
	 * update gametablestatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/updateTableStatus")
	public NaResponse<Object> updateTableStatus(Integer tid,Integer status){
		try {
			Preconditions.checkNotNull(tid,"param.null");
			Preconditions.checkNotNull(status,"param.null");
			Boolean isUpdate = gameTableService.updateTableStatus(tid,status);
			Preconditions.checkArgument(isUpdate,"update.error");
			// 更新游戏桌缓存
			AppCache.GAMETABLEMAP.clear();
			AppCache.initGameTable(gameTableService.listAllTable());
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
		
	}

	@PostMapping("addCommonBatch")
	public NaResponse addCommonBatch(Integer gameTableId,int size){
		virtualGameTableService.addCommonBatch(gameTableId,size);
		return NaResponse.createSuccess();
	}
	
	/**
	 * create gametable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/create")
	public NaResponse<Object> saveGameTable(Integer gameId,String name,
			Integer status,Integer type,Integer countDownSeconds,Integer isMany,
			Integer miCountdownSeconds,Integer min,Integer max){
		try {
			Preconditions.checkNotNull(gameId,"param.null");
			if(Strings.isNullOrEmpty(name))	return NaResponse.createError("param.null");
			Preconditions.checkNotNull(status,"param.null");
			Preconditions.checkNotNull(type,"param.null");
			Preconditions.checkNotNull(countDownSeconds,"param.null");
			Preconditions.checkNotNull(isMany,"param.null");
			Preconditions.checkNotNull(miCountdownSeconds,"param.null");
			Preconditions.checkNotNull(min,"param.null");
			Preconditions.checkNotNull(max,"param.null");
			Boolean isSave = gameTableService.saveGameTable(gameId,name,status,type,countDownSeconds,isMany,miCountdownSeconds,min,max);
			Preconditions.checkArgument(isSave,"create.error");
			// 更新游戏桌缓存
			AppCache.GAMETABLEMAP.clear();
			AppCache.initGameTable(gameTableService.listAllTable());
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
		
	}
	
	/**
	 * modify gametable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/modify")
	public NaResponse<Object> updateGameTable(Integer id,Integer gameId,String name,
			Integer status,Integer type,Integer countDownSeconds,Integer isMany,
			Integer miCountdownSeconds,Integer min,Integer max){
		try {
			Preconditions.checkNotNull(gameId,"param.null");
			if(Strings.isNullOrEmpty(name))	return NaResponse.createError("param.null");
			Preconditions.checkNotNull(status,"param.null");
			Preconditions.checkNotNull(type,"param.null");
			Preconditions.checkNotNull(countDownSeconds,"param.null");
			Preconditions.checkNotNull(isMany,"param.null");
			Preconditions.checkNotNull(miCountdownSeconds,"param.null");
			Preconditions.checkNotNull(min,"param.null");
			Preconditions.checkNotNull(max,"param.null");
			Boolean isModify = gameTableService.updateGameTable(id,gameId,name,status,type,countDownSeconds,isMany,miCountdownSeconds,min,max);
			Preconditions.checkArgument(isModify,"create.error");
			// 更新游戏桌缓存
			AppCache.GAMETABLEMAP.clear();
			AppCache.initGameTable(gameTableService.listAllTable());
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
		
	}
	
	/**
	 * search gametableList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/search")
	public NaResponse<Object> searchGameTables(Integer gameId,String name,
			Integer status,Integer type,Integer page,Integer rows){
		try {
			Integer pageNumber = page <= 1 ? 0 : page;
			Integer pageSize = rows < 10 ? 20 : rows;
			List<GameTable> listGameTables = gameTableService.listGameTables(gameId,name,status,type,pageNumber,pageSize);
			Integer count = gameTableService.countGameTables(gameId,name,status,type);
			LinkedHashMap<String, Object> datamap = Maps.newLinkedHashMapWithExpectedSize(2);
			datamap.put("total", count);
			datamap.put("rows", listGameTables);
			return NaResponse.createSuccess(datamap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
		
	}
	/**
	 * gametableList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getGameType")
	public NaResponse<Object> findGameType(){
		try {
			return NaResponse.createSuccess(gameService.listGameTypes());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError();
		}
	}
	
	@RequestMapping("/clearOnlineUser")
	public NaResponse<Object> clearOnlineUser() {
		try {
			String flag = stringRedisTemplate.opsForValue().get(RedisKeyEnum.PLATFORM_GAME_MAINTAIN_TOKEN.get());
			// 0 正常 1 维护中
			if(StringUtils.isEmpty(flag) || "0".equals(flag)){
				return NaResponse.createError("当前游戏平台为运营中，不能踢出在线玩家！");
			}else{
				gameRemote.clearOnlineUser();
				return NaResponse.createSuccess();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError("踢出在线用户失败！");
		}
	}
	
	@RequestMapping("/getPlatFlag")
	public NaResponse<Object> search() {
		String flag = stringRedisTemplate.opsForValue().get(RedisKeyEnum.PLATFORM_GAME_MAINTAIN_TOKEN.get());
		if(StringUtils.isEmpty(flag)){
			flag="0";
		}
		return NaResponse.createSuccess(flag);
	}
	
	@PostMapping("/platMaintenance")
	public NaResponse<Object> systemMaintenance() {
		try {
			String flag = stringRedisTemplate.opsForValue().get(RedisKeyEnum.PLATFORM_GAME_MAINTAIN_TOKEN.get());
			// 0 正常 1 维护中
			flag = (StringUtils.isEmpty(flag) || "0".equals(flag)) ? "1" : "0";
			stringRedisTemplate.opsForValue().set(RedisKeyEnum.PLATFORM_GAME_MAINTAIN_TOKEN.get(), flag);
			activeMQUtil.send(Integer.valueOf(flag), null);
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError();
		}
	}
}
