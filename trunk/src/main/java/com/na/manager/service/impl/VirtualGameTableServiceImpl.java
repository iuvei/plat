package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.na.manager.bean.Page;
import com.na.manager.bean.PercentConfigSearchRequest;
import com.na.manager.bean.VirtualGameTableSearchRequest;
import com.na.manager.cache.AppCache;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.dao.IPercentageConfigMapper;
import com.na.manager.dao.IVirtualGameTableMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.GameTable;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.PercentageConfig;
import com.na.manager.entity.User;
import com.na.manager.entity.VirtualGameTable;
import com.na.manager.enums.GameEnum;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.enums.UserType;
import com.na.manager.enums.VirtualGameTableStatus;
import com.na.manager.enums.VirtualGameTableType;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IVirtualGameTableService;

/**
 * 虚拟房间
 *
 * @create 2017-07
 */
@Service
@Transactional(propagation= Propagation.NESTED,rollbackFor=Exception.class)
public class VirtualGameTableServiceImpl implements IVirtualGameTableService {
	private Logger logger = LoggerFactory.getLogger(VirtualGameTableServiceImpl.class);
	@Autowired
	private IVirtualGameTableMapper virtualGameTableMapper;
	@Autowired
	private ILiveUserService liveUserService;
	@Autowired
	private IPercentageConfigMapper percentageConfigMapper;
	@Autowired
	private RedisClient redisClient;
	@Resource(name="myRedisTemplate")
    private RedisTemplate redisTemplate; //采用定制的redisTemplate
	
	@Override
	public VirtualGameTable add(VirtualGameTable virtualGameTable) {
		virtualGameTable.setCreateTime(new Date());
		User user = AppCache.getCurrentUser();
		if (user != null) {
			if (user.getUserTypeEnum() == UserType.SUB_ACCOUNT) { // 子账号
				ChildAccountUser childAccountUser = (ChildAccountUser)user;
				user =childAccountUser.getParentUser();
			}
		}
		LiveUser liveUser = liveUserService.findById(user.getId());
		Preconditions.checkArgument(liveUser.getRoomMember()>0, "virtual.gametable.room.member.zero");
		long count = virtualGameTableMapper.queryCountByOwnerId(user.getId());
		com.na.manager.common.Preconditions.checkArgument(liveUser.getRoomMember()>count, "virtual.gametable.room.member.full",new Object[]{liveUser.getRoomMember()});
		//判断包房名称是否存在
		VirtualGameTableSearchRequest searchRequest = new VirtualGameTableSearchRequest();
		searchRequest.setGameTableId(virtualGameTable.getGameTableId());
		searchRequest.setUserId(user.getId());
		searchRequest.setFullRoomName(virtualGameTable.getRoomName());
		List<VirtualGameTable> tableList = virtualGameTableMapper.queryVirtualGameTableByPage(searchRequest);
		
		Preconditions.checkArgument(CollectionUtils.isEmpty(tableList), "virtual.gametable.room.name.exist");
		Preconditions.checkArgument(
				(virtualGameTable.getMaxMembers() != null && virtualGameTable.getMaxMembers().intValue() >= 0),
				"virtual.gametable.room.maxMembers.error");
		Preconditions.checkArgument(
				(virtualGameTable.getMinBalance() != null && virtualGameTable.getMinBalance().intValue() > 0),
				"virtual.gametable.room.minBalance.error");
		if(virtualGameTable.getType()==2){
			Preconditions.checkArgument(StringUtils.isNotEmpty(virtualGameTable.getLid()),
					"virtual.gametable.room.config.missing");
		}
		
		virtualGameTable.setCreateUser(user.getLoginName());
		virtualGameTable.setOwnerId(user.getId());
		virtualGameTable.setGameId(GameEnum.BACCARAT.get());
		virtualGameTable.setStatus(VirtualGameTableStatus.NORMAL.get());
		virtualGameTableMapper.add(virtualGameTable);
		
		publishGameServer(virtualGameTable);
		
		return virtualGameTable;
	}

	@Override
	public void addCommonBatch(Integer gameTableId,int size){
		 GameTable gameTable = AppCache.getGameTable(gameTableId);
		 VirtualGameTable virtualGameTable = new VirtualGameTable();
		 virtualGameTable.setGameId(gameTable.getGameId());
		 virtualGameTable.setGameTableId(gameTableId);
		 virtualGameTable.setStatus(VirtualGameTableStatus.NORMAL.get());
		 virtualGameTable.setCreateUser(AppCache.getCurrentUser().getLoginName());
		 virtualGameTable.setType(VirtualGameTableType.COMMON.get());
		 for(int i=0;i<size;i++) {
			 virtualGameTableMapper.add(virtualGameTable);
		 }
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<VirtualGameTable> queryVIPRoomByPage(VirtualGameTableSearchRequest request) {
		User user = AppCache.getCurrentUser();
		if (user != null) {
			if (user.getUserTypeEnum() == UserType.SUB_ACCOUNT) { // 子账号
				ChildAccountUser childAccountUser = (ChildAccountUser)user;
				user =childAccountUser.getParentUser();
			}
		}
		if (user.getUserTypeEnum() == UserType.LIVE) {
			request.setUserId(user.getId());
		}
		Page<VirtualGameTable> page = new Page<>(request);
		request.setType(VirtualGameTableType.COMMON.get());
		List<VirtualGameTable> list = virtualGameTableMapper.queryVirtualGameTableByPage(request);
		list.forEach(item->{
			item.setHedgePercentage(item.getHedgePercentage()==null?BigDecimal.ZERO:item.getHedgePercentage().multiply(new BigDecimal(100)));
			item.setNoHedgePercentage(item.getNoHedgePercentage()==null?BigDecimal.ZERO:item.getNoHedgePercentage().multiply(new BigDecimal(100)));
			item.setWaterPercentage(item.getWaterPercentage()==null?BigDecimal.ZERO:item.getWaterPercentage().multiply(new BigDecimal(100)));
		});
		page.setData(list);
		page.setTotal(virtualGameTableMapper.getVirtualGameTableCount(request));
		return page;
	}

	@Override
	@Transactional(readOnly=true)
	public Page<VirtualGameTable> queryVirtualGameTableByPage(VirtualGameTableSearchRequest request) {
		request.setvType(VirtualGameTableType.COMMON.get());
		Page<VirtualGameTable> page = new Page<>(request);
		List<VirtualGameTable> list = virtualGameTableMapper.queryVirtualGameTableByPage(request);
		page.setData(list);
		page.setTotal(virtualGameTableMapper.getVirtualGameTableCount(request));
		return page;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PercentageConfig> getPercentageConfig(Long userId, String idName, String textName) {
		PercentConfigSearchRequest searchRequest = new PercentConfigSearchRequest();
		searchRequest.setUserId(userId);
		List<PercentageConfig> percentageConfigs = percentageConfigMapper.listPercentConfigByPage(searchRequest);
		return percentageConfigs;
	}
	

	@Override
	public void update(VirtualGameTable virtualGameTable) {
		VirtualGameTable original =findById(virtualGameTable.getId());
		boolean flag =true;
		//判断包房名称是否存在
		VirtualGameTableSearchRequest searchRequest = new VirtualGameTableSearchRequest();
		searchRequest.setGameTableId(original.getGameTableId());
		searchRequest.setUserId(original.getOwnerId());
		searchRequest.setFullRoomName(virtualGameTable.getRoomName());
		List<VirtualGameTable> tableList = virtualGameTableMapper.queryVirtualGameTableByPage(searchRequest);
		if(!CollectionUtils.isEmpty(tableList)){
			for (VirtualGameTable item : tableList) {
				if(!item.getId().equals(original.getId())){
					flag = false;
					break;
				}
			}
		}
		
		Preconditions.checkArgument(flag, "virtual.gametable.room.name.exist");
		Preconditions.checkArgument(
				(virtualGameTable.getMaxMembers() != null && virtualGameTable.getMaxMembers().intValue() >= 0),
				"virtual.gametable.room.maxMembers.error");
		Preconditions.checkArgument(
				(virtualGameTable.getMinBalance() != null && virtualGameTable.getMinBalance().intValue() > 0),
				"virtual.gametable.room.minBalance.error");
		Preconditions.checkArgument(StringUtils.isNotEmpty(virtualGameTable.getLid()),
				"virtual.gametable.room.config.missing");
		original.setGameTableId(virtualGameTable.getGameTableId());
		original.setRoomName(virtualGameTable.getRoomName());
		original.setMaxMembers(virtualGameTable.getMaxMembers());
		original.setMinBalance(virtualGameTable.getMinBalance());
//		original.setHedgePercentage(virtualGameTable.getHedgePercentage());
//		original.setNoHedgePercentage(virtualGameTable.getNoHedgePercentage());
//		original.setWaterPercentage(virtualGameTable.getWaterPercentage());
		virtualGameTableMapper.update(original);
		
		publishGameServer(original);
	}

	@Override
	public VirtualGameTable findById(Integer id) {
		return virtualGameTableMapper.findById(id);
	}

	@Override
	public void delete(Integer id) {
		VirtualGameTable virtualGameTable = findById(id);
		if(virtualGameTable !=null){
			if(virtualGameTable.getStatusEnum() == VirtualGameTableStatus.CANCEL){
				virtualGameTable.setStatus(VirtualGameTableStatus.NORMAL.get());
			}else{
				virtualGameTable.setStatus(VirtualGameTableStatus.CANCEL.get());
			}
			virtualGameTableMapper.update(virtualGameTable);
		}
		publishGameServer(virtualGameTable);
	}
	
	@Override
	public void delete(VirtualGameTable gameTable) {
		if(gameTable ==null || StringUtils.isEmpty(gameTable.getIds())){
			return;
		}
		for (String id:gameTable.getIds().split(",")) {
			VirtualGameTable virtualGameTable = findById(Integer.valueOf(id));
			if(virtualGameTable ==null || virtualGameTable.getStatus() ==gameTable.getStatus()){
				continue;
			}
			virtualGameTable.setStatus(gameTable.getStatus());
			virtualGameTableMapper.update(virtualGameTable);
			publishGameServer(virtualGameTable);
		}
	}

	@Override
	public void modifyPassword(VirtualGameTable virtualGameTable) {
		VirtualGameTable original =findById(virtualGameTable.getId());
		original.setPassword(virtualGameTable.getPassword());
		virtualGameTableMapper.update(original);
		
		publishGameServer(original);
	
	}
	
	/**
	 * 通知游戏服务端更新包房信息
	 * @param virtualGameTable
	 */
	private void publishGameServer(VirtualGameTable virtualGameTable){
		logger.info("VirtualGameTable数据："+JSONObject.toJSONString(virtualGameTable));
		redisTemplate.boundHashOps(RedisKeyEnum.VIRTUAL_GAMETABLE_VIP_ROOM.get()).put(virtualGameTable.getId(), virtualGameTable);
		RedisEventData redisEventData = new RedisEventData();
		redisEventData.setRedisEventType(RedisEventData.RedisEventType.UpdateVirtualGameTable.get());
		redisEventData.setRedisEventData(virtualGameTable.getId());
		redisClient.publishGameServer(JSON.toJSONString(redisEventData));
	}
}
