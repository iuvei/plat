package com.gameportal.redis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.gameportal.redis.service.IRedisClientResMgr;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/**
 * 配置连接redis服务器
 * @author YTO_ZJ
 *
 */
public class RedisClientResMgr implements IRedisClientResMgr{
	
	private static final Logger logger = Logger.getLogger(RedisClientResMgr.class);
	/**
	 * redis服务器ip列表，注入
	 */
	private List<String> redisHosts;
	/**
	 * redis服务器端口列表，注入
	 */
	private List<Integer> redisPorts;

	/**
	 * pool的设置
	 */
	private Map<String, Integer> poolConfig;

	private int timeOut;

	/**
	 * 到redis服务器的连接池，为了实现key的固定hash，自己初始化
	 */
	private ShardedJedisPool pool = null;

	

	private ThreadLocal<ShardedJedis> shardedJedises = new ThreadLocal<ShardedJedis>(){
	/*	public ShardedJedis initialValue(){
			return pool.getResource();
		}*/
	};

	@Override
	public ShardedJedis getCurShardedJedis() {
		return shardedJedises.get();
	}

	@Override
	public ShardedJedis getShardedJedis() {
		return pool.getResource();
	}

	@Override
	public void returnShardedJedis(ShardedJedis value, boolean isSucc) {
		if(value != null){
			if(isSucc){
				pool.returnResource(value);
			}else{
				pool.returnBrokenResource(value);
			}
		}
	}
	
	/**
	 * AOP处理
	 * @param joinpoint
	 * @return
	 */
	public Object preAndRelResource(ProceedingJoinPoint joinpoint){
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = pool.getResource();
			if(shardedJedis == null){
				logger.warn("Get redis resource is null");
				return null;
			}
			shardedJedises.set(shardedJedis);
			Object returnValue = null;
			returnValue = joinpoint.proceed();
			if (null != shardedJedis){
				pool.returnResource(shardedJedis);
			}
			return returnValue;
		} catch (Throwable e) {
			logger.error("Access cache error:",e);
			if(e instanceof JedisConnectionException){
				if (null != shardedJedis)
					pool.returnBrokenResource(shardedJedis);
			}else{
				if (null != shardedJedis)
					pool.returnResource(shardedJedis);
			}
			return null;
		}finally{
			shardedJedises.remove();
		}
	}
	
	/**
	 * spring配置初始化函数
	 */
	public void init(){
		if (null == redisHosts || null == redisPorts || 0 == redisHosts.size()
				|| 0 == redisPorts.size()) {
			logger.warn("init error, no hosts or ports data");
			return;
		}
		if (redisHosts.size() != redisPorts.size()) {
			logger.warn("init error, hosts number not match with ports number");
			return;
		}
		initJedisPool();
	}
	
	/**
	 * 初始化jedis池
	 */
	private void initJedisPool() {
		logger.info("Prepare to do initJedisPool");
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		
		for (int i = 0; i < redisHosts.size(); i++) {
			String host = redisHosts.get(i);
			if(host.equals("")){
				logger.warn("finished initJedisPool");
			}
			Integer port = redisPorts.get(i);
			if(timeOut > 0) {
				JedisShardInfo si = new JedisShardInfo(host, port.intValue(), timeOut);
				shards.add(si);
			} else {
				JedisShardInfo si = new JedisShardInfo(host, port.intValue());
				shards.add(si);
			}
		}
		pool = new ShardedJedisPool(getJedisPoolConfig(), shards,
				Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
		logger.warn("finished initJedisPool");
		
	}
	
	/**
	 * 从配置文件中取Pool配置
	 * @return JedisPoolConfig
	 */
	private JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig jConfg = new JedisPoolConfig();
		jConfg.setMaxTotal(poolConfig.get("maxActive"));
		//jConfg.setMaxActive(poolConfig.get("maxActive"));
		jConfg.setMaxIdle(poolConfig.get("maxIdle"));
		jConfg.setMaxWaitMillis(poolConfig.get("maxWait"));
		jConfg.setTestOnBorrow(poolConfig.get("testOnBorrow") > 0 ? true : false);
		return jConfg;
	}

	public void destroy() {
		logger.info("Prepare to do destroy");
		if (null != pool) {
			pool.destroy();
		}
		logger.info("finished destroy");
	}

	public List<String> getRedisHosts() {
		return redisHosts;
	}

	public void setRedisHosts(List<String> redisHosts) {
		this.redisHosts = redisHosts;
	}

	public List<Integer> getRedisPorts() {
		return redisPorts;
	}

	public void setRedisPorts(List<Integer> redisPorts) {
		this.redisPorts = redisPorts;
	}

	public Map<String, Integer> getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(Map<String, Integer> poolConfig) {
		this.poolConfig = poolConfig;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public ShardedJedisPool getPool() {
		return pool;
	}

	public void setPool(ShardedJedisPool pool) {
		this.pool = pool;
	}

	public ThreadLocal<ShardedJedis> getShardedJedises() {
		return shardedJedises;
	}

	public void setShardedJedises(ThreadLocal<ShardedJedis> shardedJedises) {
		this.shardedJedises = shardedJedises;
	}
}
