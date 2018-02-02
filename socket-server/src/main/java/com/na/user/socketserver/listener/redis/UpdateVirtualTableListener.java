package com.na.user.socketserver.listener.redis;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.request.QuickChangeRoomCommand;
import com.na.baccarat.socketserver.command.requestpara.QuickChangeRoomPara;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 更新VIP包房
 * 
 * @author alan
 * @date 2017年8月4日 上午10:19:08
 */
@Component
public class UpdateVirtualTableListener {
	
	private Logger log = LoggerFactory.getLogger(UpdateVirtualTableListener.class);
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
	private QuickChangeRoomCommand quickChangeRoomCommand;
	
	@Autowired
	private SocketIOServer socketIOServer;
	
    public void onMessage(Object message) {
    	Integer params = (Integer) message;
    	
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		JSONObject jSONObject = (com.alibaba.fastjson.JSONObject) redisTemplate.boundHashOps("virtual.gametable.vip.room").get(params);
		if(jSONObject == null) {
			throw SocketException.createError("virtual.table.update.failure");
		}
		log.debug("【虚拟桌热更新】 参数：" + params + ",redis数据：" + jSONObject.toJSONString());
		VirtualGameTablePO virtualGameTablePO = jSONObject.toJavaObject(VirtualGameTablePO.class);
		if(virtualGameTablePO == null) {
			throw SocketException.createError("virtual.table.update.failure");
		}
		
		VirtualGameTable virtualGameTable = BaccaratCache.getVirtualTableById(virtualGameTablePO.getId());
		if(virtualGameTable == null) {
			virtualGameTable = new VirtualGameTable();
		}
		VirtualGameTablePO oldVirtualGameTablePO = virtualGameTable.getVirtualGameTablePO();
		virtualGameTable.setVirtualGameTablePO(virtualGameTablePO);
		BaccaratCache.freshVirtualTable(virtualGameTable);
		
		vipRoomChangeTable(oldVirtualGameTablePO, virtualGameTable);
    }
    
    private void vipRoomChangeTable(VirtualGameTablePO oldVirtualGameTablePO, VirtualGameTable virtualGameTable) {
    	VirtualGameTablePO virtualGameTablePO = virtualGameTable.getVirtualGameTablePO();
    	if(virtualGameTablePO.getTypeEnum() == VirtualGameTableType.AGENT_VIP) {
    		if(oldVirtualGameTablePO.getGameTableId().compareTo(virtualGameTablePO.getGameTableId()) != 0) {
    			log.debug("【虚拟桌热更新】 实体桌由：" + oldVirtualGameTablePO.getGameTableId() + ",改为：" + virtualGameTablePO.getGameTableId());
    			//删掉老的座位信息
    			BaccaratCache.deleteVirtualTableSeat(oldVirtualGameTablePO.getGameTableId(), oldVirtualGameTablePO.getId());
    			//初始化虚拟桌座位信息
    			BaccaratCache.setVirtualTableSeat(virtualGameTablePO.getGameTableId(), virtualGameTablePO.getId());
    			//更新实体桌虚拟桌对应列表
    			BaccaratCache.removeVirtualTableIdByTableId(oldVirtualGameTablePO.getGameTableId(), virtualGameTablePO.getId());
    			BaccaratCache.freshVirtualTableIdByTableId(virtualGameTablePO.getGameTableId(), virtualGameTablePO.getId());
    			
    			Set<Long> userSet = new HashSet<>(virtualGameTable.getBesideUser());
    			userSet.addAll(BaccaratCache.getVirtualTableSeatUser(oldVirtualGameTablePO.getGameTableId(), virtualGameTablePO.getId()).values());
    			
    			if(userSet != null && !userSet.isEmpty()) {
    				userSet.stream().forEach( item -> {
    					SocketIOClient client = SocketUtil.getClientByUser(socketIOServer, item);
    					User user = BaccaratCache.getLoginUser(item);
    					QuickChangeRoomPara quickChangeRoomPara = new QuickChangeRoomPara();
    					quickChangeRoomPara.setPreGameId(BaccaratCache.getGame().getGamePO().getId());
    					quickChangeRoomPara.setGameId(BaccaratCache.getGame().getGamePO().getId());
    					quickChangeRoomPara.setSource(user.getSource().get());
    					quickChangeRoomPara.setChipId(user.getChipId());
    					quickChangeRoomPara.setPreTableId(oldVirtualGameTablePO.getGameTableId());
    					quickChangeRoomPara.setTableId(virtualGameTablePO.getGameTableId());
    					quickChangeRoomPara.setPreVirtualTableId(oldVirtualGameTablePO.getId());
    					quickChangeRoomPara.setVirtualTableId(virtualGameTablePO.getId());
    					quickChangeRoomCommand.exec(client, quickChangeRoomPara);
    				});
    			}
    			
    		}
		}
    }
}
