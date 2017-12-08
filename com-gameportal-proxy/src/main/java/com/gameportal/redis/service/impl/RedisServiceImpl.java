package com.gameportal.redis.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.gameportal.redis.service.IRedisClientResMgr;
import com.gameportal.redis.service.IRedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * @FileName: RedisTool.java
 * @Copyright: Copyright (c)　中讯爱乐
 * @Description: Redis 缓存工具类
 * @date 2013-1-18 下午02:50:56
 * @author by yangyan
 * @version V1.0
 */
public class RedisServiceImpl implements IRedisService {

	private static Logger logger = Logger.getLogger(RedisServiceImpl.class);
	private final int time = 60 * 60; // 默认过期时间

	private IRedisClientResMgr resMgr;

	public IRedisClientResMgr getResMgr() {
		return resMgr;
	}

	public void setResMgr(IRedisClientResMgr resMgr) {
		this.resMgr = resMgr;
	}
	
	public RedisServiceImpl() {
		super();
	}

	/**
	 * 获得redis链接实例
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<
	 * |>异常码<|>备用参数<|>
	 * 
	 * @return ShardedJedis
	 */
	public ShardedJedis getShardedJedis() {
		try {
			ShardedJedis jedis = resMgr.getCurShardedJedis();
//			logger.info("<|>2013011801<|><|>数据缓存工具类<|>getShardedJedis链接<|><|><|>成功<|><|>备用参数<|>");
			return jedis;
		} catch (Exception e) {
			logger.error(
					"<|>2013011801<|><|>数据缓存工具类<|>getShardedJedis链接<|><|><|>异常<|><|>备用参数<|>",
					e);
			return null;
		}
	}

	/**
	 * /** 数据放入缓存 默认1个小时过期
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数
	 * <|>返回码<|>异常码<|>备用参数<|>
	 * 
	 * @param key
	 * @param value
	 *            存放数据值
	 * @return code 状态
	 */
	public boolean setObjectFromRedis(String key, String value) {
		return this.setObjectFromRedis(key, value, time);
	}

	/**
	 * 数据放入缓存
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<|>异常码
	 * <|>备用参数<|>
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            存放数据值
	 * @param seconds
	 *            存放多久失效
	 * @return code 状态
	 */
	public boolean setObjectFromRedis(String key, String value, int seconds) {
		try {
			String code = this.getShardedJedis().set(key, value);
			Long c = this.getShardedJedis().expire(key, seconds);
			boolean flag = (StringUtils.isNotBlank(code) && "OK".equals(code
					.trim())) ? true : false;
//			logger.info("<|>2013011802<|>数据缓存工具类<|>setObjectFromRedis数据放入缓存<|>"
//					+ key + "/" + value + "/" + seconds + "<|>" + flag + "<|>"
//					+ c + "<|>备用参数<|>");
			return flag;
		} catch (Exception e) {
			logger.error(
					"<|>2013011802<|>数据缓存工具类<|>setObjectFromRedis数据放入缓存<|>"
							+ key + "/" + value + "/" + seconds + "<|>false<|>"
							+ e.getMessage() + "<|>备用参数<|>", e);
			return false;
		}
	}

	/**
	 * 按键获得当前的值
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public String get(String key) {
		try {
			String code = this.getShardedJedis().get(key);
//			logger.info("<|>2013011803<|>数据缓存工具类<|>get redis的数据<|>" + key
//					+ "<|>" + code + "<|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error("<|>2013011803<|>数据缓存工具类<|>get redis的数据<|>" + key
					+ "<|>异常<|>" + e.getMessage() + "<|>备用参数<|>", e);
			return null;
		}
	}

	/**
	 * 按键获得当前的值
	 * 
	 * @param key
	 *            键
	 * @return Long 值
	 */
	public Long getLong(String key) {
		String code = this.get(key);
		return StringUtils.isNotBlank(code) ? Long.parseLong(code) : null;
	}

	/**
	 * 取缓存数据
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<|>异常码<
	 * |>备用参数<|>
	 * 
	 * @param key
	 *            键
	 * @return 取得缓存数据值 或 null
	 */
	public String getStringFromRedis(String key) {
		try {
			String code = this.getShardedJedis().get(key);
//			logger.info("<|>2013011804<|>数据缓存工具类<|>getStringFromRedis缓存取数据<|>"
//					+ key + "<|>yes<|><|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error("<|>2013011804<|>数据缓存工具类<|>getStringFromRedis缓存取数据<|>"
					+ key + "<|>null<|>" + e.getMessage() + "<|>备用参数<|>", e);
			return null;
		}
	}

	/**
	 * 判断key值是否存在
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<|
	 * >异常码<|>备用参数<|>
	 * 
	 * @param key
	 *            键
	 * @return 存在true
	 */
	public boolean keyExists(String key) {
		try {
			boolean code = this.getShardedJedis().exists(key);
//			logger.info("<|>2013011805<|>数据缓存工具类<|>keyExists key值是否存在<|>" + key
//					+ "<|>" + code + "<|><|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error("<|>2013011805<|>数据缓存工具类<|>keyExists key值是否存在<|>"
					+ key + "<|>false<|>" + e.getMessage() + "<|>备用参数<|>", e);
			return false;
		}
	}

	/**
	 * 设置KEY的过期时间
	 * 
	 * @param key
	 *            键值
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return Long
	 */
	public Long expire(String key, int seconds) {
		Long code = null;
		try {
			code = this.getShardedJedis().expire(key, seconds);
//			logger.info("<|>2013011806<|>数据缓存工具类<|>expire 设置KEY的过期时间<|>" + key
//					+ "/" + seconds + "<|><|>" + code + "<|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error("<|>2013011806<|>数据缓存工具类<|>expire 设置KEY的过期时间<|>" + key
					+ "/" + seconds + "<|><|>" + e.getMessage() + "<|>备用参数<|>",
					e);
			return code;
		}
	}

	public Long inc(String key) {
		return this.inc(key, time);
	}

	/**
	 * 按key累加
	 * 
	 * @param key
	 *            键
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return 累加次数
	 */
	public Long inc(String key, int seconds) {
		Long code = null;
		try {
			code = this.getShardedJedis().incr(key);
			Long c = this.expire(key, seconds);
//			logger.info("<|>2013011807<|>数据缓存工具类<|>inc自增<|>" + key + "/"
//					+ seconds + "<|>" + code + "<|>" + c + "<|><|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error(
					"<|>2013011807<|>数据缓存工具类<|>inc自增<|>" + key + "/" + seconds
							+ "<|>null<|>" + e.getMessage() + "<|><|>备用参数<|>",
					e);
			return code;
		}
	}

	/**
	 * 按key自减 默认是1个小时数据过期
	 * 
	 * @param key
	 *            键
	 * @return 自减次数
	 */
	public Long dinc(String key) {
		return this.dinc(key, time);
	}

	/**
	 * 按key自减
	 * 
	 * @param key
	 *            键
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return 自减次数
	 */
	public Long dinc(String key, int seconds) {
		Long code = null;
		try {
			code = this.getShardedJedis().decr(key);
			Long c = this.expire(key, seconds);
//			logger.info("<|>2013011808<|>数据缓存工具类<|>dinc自减<|>" + key + "/"
//					+ seconds + "<|>" + code + "<|>" + c + "<|><|>备用参数<|>");
			return code;
		} catch (Exception e) {
			logger.error(
					"<|>2013011808<|>数据缓存工具类<|>dinc自减<|>" + key + "/" + seconds
							+ "<|>null<|>" + e.getMessage() + "<|><|>备用参数<|>",
					e);
			return code;
		}
	}

	/**
	 * 
	 * @Title: delete
	 * @Description: TODO(清除key的内容)
	 * @param @param key 键
	 * @param @return
	 * @return boolean 返回类型
	 * @date 2013-1-18 下午03:27:58
	 * @throws
	 */
	public boolean delete(String key) {
		try {
			Long c = this.getShardedJedis().del(key);
//			logger.info("<|>2013011809<|>数据缓存工具类<|>dinc自减<|>" + key + "/"
//					+ "<|>" + (c == 1 ? true : false) + "<|><|>备用参数<|>");
			return c == 1 ? true : false;
		} catch (Exception e) {
			logger.error("<|>2013011809<|>数据缓存工具类<|>dinc自减<|>" + key + "/"
					+ "<|>false<|><|>备用参数<|>", e);
			return false;
		}
	}

	/**
	 * 
	 * @Title: addToRedis
	 * @Description: TODO(把对象写入redis缓存中默认是1个小时数据过期)
	 * @param @param key
	 * @param @param servializable
	 * @param @return
	 * @return boolean 返回类型
	 * @date 2013-1-31 下午08:32:36
	 * @throws
	 */
	public boolean addToRedis(String key, Serializable servializable) {
		return this.addToRedis(key, servializable, time);
	}

	/**
	 * 
	 * @Title: addToRedis
	 * @Description: TODO(把对象写入redis缓存中)
	 * @param @param key
	 * @param @param servializable
	 * @param @param seconds 过期时间以秒为单位
	 * @param @return
	 * @return boolean 返回类型
	 * @date 2013-1-31 下午08:32:58
	 * @throws
	 */
	public boolean addToRedis(String key, Serializable servializable,
			int seconds) {
		byte[] bytes = serializeObj(servializable);
		ShardedJedis jedis = this.getShardedJedis();
		String result = "";
		try {
			if (jedis != null) {
				// result = jedis.set(key.getBytes(), bytes);
				result = jedis.setex(key.getBytes(), seconds, bytes);
			}
//			logger.info("<|>2013011810<|>数据缓存工具类<|>addToRedis把对象写入redis缓存中<|>"
//					+ key + "/" + servializable + "<|>" + result
//					+ "<|><|><|>备用参数<|>");
			return "OK".equals(result) ? true : false;
		} catch (Exception e) {
			// 当遇到set入redis不成功时(socket问题)，循环set5次。
			int count = 5;
			while (!"OK".equals(result) || count > 0) {
				if (jedis != null) {
					// result = jedis.set(key.getBytes(), bytes);
					result = jedis.setex(key.getBytes(), seconds, bytes);
				}
				count--;
			}
			logger.error("<|>2013011810<|>数据缓存工具类<|>addToRedis把对象写入redis缓存中<|>"
					+ key + "/" + servializable + "<|>" + result
					+ "<|><|><|>备用参数<|>", e);
			return "OK".equals(result) ? true : false;
		}
	}

	/**
	 * 
	 * @Title: getRedisResult
	 * @Description: TODO(根据key查询redis的缓存结果)
	 * @param @param <T> 实体类
	 * @param @param key
	 * @param @param objClass
	 * @param @return
	 * @return T 返回类型
	 * @date 2013-1-21 下午12:57:15
	 * @throws
	 */
	public <T> T getRedisResult(String key, Class<T> objClass) {
		byte[] bytes = null;
		ShardedJedis jedis = this.getShardedJedis();
		try {
			if (jedis != null) {
				bytes = jedis.get(key.getBytes());
			}
			Object obj = null;
			if (bytes != null && bytes.length > 0) {
				obj = reSerializeObj(bytes, objClass);
			}
//			logger.info("<|>2013011813<|>数据缓存工具类<|>getRedisResult把对象写入redis缓存中<|>"
//					+ key + "/" + objClass + "<|><|><|>备用参数<|>");
			return (T) obj;
		} catch (Exception e) {
			logger.error("<|>2013011813<|>数据缓存工具类<|>getRedisResult把对象写入redis缓存中<|>"
					+ key + "/" + objClass + "<|><|><|>备用参数<|>", e);
			return null;
		}
	}

	/**
	 * 
	 * @Title: serializeObj
	 * @Description: TODO(把对象序列化为btye[])
	 * @param @param servializable
	 * @param @return
	 * @return byte[] 返回类型
	 * @date 2013-1-21 下午12:57:47
	 * @throws
	 */
	public byte[] serializeObj(Serializable servializable) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] perBytes = null;
		try {
			oos = new ObjectOutputStream(out);
			oos.writeObject(servializable);
			if (out != null) {
				perBytes = out.toByteArray();
			}
//			logger.info("<|>2013011811<|>数据缓存工具类<|>serializeObj把对象序列化为btye[]<|>"
//					+ servializable + "<|><|><|>备用参数<|>");
			return perBytes;
		} catch (IOException e) {
			logger.error(
					"<|>2013011811<|>数据缓存工具类<|>serializeObj把对象序列化为btye[]<|>"
							+ servializable + "<|><|><|>备用参数<|>", e);
			return null;
		}

	}

	/**
	 * 
	 * @Title: reSerializeObj
	 * @Description: TODO(反序列化)
	 * @param @param <T>
	 * @param @param bytes
	 * @param @param objClass
	 * @param @return
	 * @param @throws IOException
	 * @param @throws ClassNotFoundException
	 * @return T 返回类型
	 * @date 2013-1-21 下午12:58:25
	 * @throws
	 */
	public <T> T reSerializeObj(byte[] bytes, Class<T> objClass) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(in);
			Object obj = ois.readObject();
//			logger.info("<|>2013011812<|>数据缓存工具类<|>reSerializeObj反序列化<|>"
//					+ objClass + "<|><|><|>备用参数<|>");
			return (T) obj;
		} catch (Exception e) {
			logger.error("<|>2013011812<|>数据缓存工具类<|>reSerializeObj反序列化<|>"
					+ objClass + "<|><|><|>备用参数<|>", e);
			return null;
		}
	}
	
	/**
	 * 从list中获取一个范围的string
	 * 
	 * @param key
	 * @param beginIndex 0
	 * @param endIndex -1 表示取全部
	 * @return
	 */
	public List<String> getRangeStringFromList(String key, long beginIndex,
			long endIndex) {
		ShardedJedis shardedJedis = this.getShardedJedis();
		return shardedJedis.lrange(key, beginIndex, endIndex);
	}
	
	/**
	 * <b>模糊查询所有匹配的key</b><br/>
	 * key模糊规则如下:<br/>
	 * key="*string"查询以string结尾的key<br/>
	 * key="string*"查询以string开头的key<br/>
	 * key="*string*"查询存在string的key<br/>
	 * key="string*string"查询以string为开头和结尾的key<br/>
	 * @param key
	 * @return List<String> keys
	 */
	public List<String> getKeys(String key) {
		ShardedJedis shardedJedis = this.getShardedJedis();
		Collection<Jedis> jedisCollection = shardedJedis.getAllShards();
		Iterator<Jedis> jedisIterator= jedisCollection.iterator();
		List<String> res = new ArrayList<String>();
		while(jedisIterator.hasNext()) {
			Jedis jedis = jedisIterator.next();
			Iterator<String> keys = jedis.keys(key).iterator();
			while(keys.hasNext()) {
				res.add(keys.next());
			}
		}
		return res;
	}

}
