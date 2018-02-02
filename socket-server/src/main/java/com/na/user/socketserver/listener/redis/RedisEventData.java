package com.na.user.socketserver.listener.redis;

import java.io.Serializable;

/**
 *redis事件接受的数据类型
 * @author Administrator
 *
 */
public class RedisEventData implements Serializable{
	
	private static final long serialVersionUID = -4324181598261098620L;
	//事件类型
	private Integer redisEventType;
	//事件数据
	private Object redisEventData;
			
	
	public Integer getRedisEventType() {
		return redisEventType;
	}
	
	public RedisEventType getRedisEventTypeEnum() {
		return RedisEventType.get(redisEventType);
	}

	public void setRedisEventType(Integer redisEventType) {
		this.redisEventType = redisEventType;
	}



	public Object getRedisEventData() {
		return redisEventData;
	}



	public void setRedisEventData(Object redisEventData) {
		this.redisEventData = redisEventData;
	}



	public enum RedisEventType{
		AbnormalGameResult(1, "异常游戏处理"),
		UpdateGameTable(2, "修改游戏桌"),
		UserAnnounce(3, "用户公告"),
		ReshAmount(4, "更新用户余额"),
		ResetPercentage(5, "更新洗码比"),
		AccountManage(6, "账户管理"),
		UpdateVirtualTable(7, "修改虚拟房间"),
		UpdateGameConfig(8, "修改游戏配置"),
		  ;
		Integer val;
		String desc;

		RedisEventType(Integer val, String desc) {
			this.val = val;
			this.desc = desc;
		}

		public Integer get() {
			return val;
		}
		
		public String getDesc() {
			return desc;
		}

		public static RedisEventType get(Integer status) {
			if(status != null){
				for(RedisEventType item : RedisEventType.values()){
					if(item.get().intValue() == status.intValue()){
						return item;
					}
				}
			}
			
			return null;
		}
	}
	
	
}

