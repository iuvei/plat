package com.gameportal.manage.redis.service;

import redis.clients.jedis.ShardedJedis;

/**
 * redis管理接口
 * @author YTO_CS
 *
 */
public interface IRedisClientResMgr {
	
	public ShardedJedis getCurShardedJedis();
	
	/**
	 * create by  
	 * @return
	 */
	public ShardedJedis getShardedJedis();
	
	/**
	 * create by  
	 * @return
	 */
	public void returnShardedJedis(ShardedJedis value, boolean isSucc);

}
