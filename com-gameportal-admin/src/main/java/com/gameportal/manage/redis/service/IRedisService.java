package com.gameportal.manage.redis.service;

import java.io.Serializable;
import java.util.List;

import redis.clients.jedis.ShardedJedis;

/**
 * redis服务
 * 
 * @author shejia
 * 
 */
public abstract interface IRedisService {
	/**
	 * 获得redis链接实例
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<
	 * |>异常码<|>备用参数<|>
	 * 
	 * @return ShardedJedis
	 */
	public abstract ShardedJedis getShardedJedis();

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
	public abstract boolean setObjectFromRedis(String key, String value);

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
	public abstract boolean setObjectFromRedis(String key, String value,
			int seconds);

	/**
	 * 按键获得当前的值
	 * 
	 * @param key
	 *            键
	 * @return String 值
	 */
	public abstract String get(String key);

	/**
	 * 按键获得当前的值
	 * 
	 * @param key
	 *            键
	 * @return Long 值
	 */
	public abstract Long getLong(String key);

	/**
	 * 取缓存数据
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<|>异常码<
	 * |>备用参数<|>
	 * 
	 * @param key
	 *            键
	 * @return 取得缓存数据值 或 null
	 */
	public abstract String getStringFromRedis(String key);

	/**
	 * 判断key值是否存在
	 * 日志格式：<|>功能代码<|>子功能代码<|>功能描述<|>子功能描述<|>参数（多个以/分划）<|>传入参数<|>返回码<|
	 * >异常码<|>备用参数<|>
	 * 
	 * @param key
	 *            键
	 * @return 存在true
	 */
	public abstract boolean keyExists(String key);

	/**
	 * 设置KEY的过期时间
	 * 
	 * @param key
	 *            键值
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return Long
	 */
	public abstract Long expire(String key, int seconds);

	/**
	 * KEY的累加
	 * 
	 * @param key
	 *            键值
	 * @return Long
	 */
	public abstract Long inc(String key);

	/**
	 * 按key累加
	 * 
	 * @param key
	 *            键
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return 累加次数
	 */
	public abstract Long inc(String key, int seconds);

	/**
	 * 按key自减 默认是1个小时数据过期
	 * 
	 * @param key
	 *            键
	 * @return 自减次数
	 */
	public abstract Long dinc(String key);

	/**
	 * 按key自减
	 * 
	 * @param key
	 *            键
	 * @param seconds
	 *            过期时间以秒为单位
	 * @return 自减次数
	 */
	public abstract Long dinc(String key, int seconds);
	
	/**
	 * 
	* @Title: delete
	* @Description: TODO(清除key的内容)
	* @param @param key   键
	* @param @return
	* @return boolean    返回类型
	* @date 2013-1-18 下午03:27:58 
	* @throws
	 */
	public abstract boolean delete(String key);
	
	/**
	 * 
	* @Title: addToRedis
	* @Description: TODO(把对象写入redis缓存中默认是1个小时数据过期)
	* @param @param key
	* @param @param servializable
	* @param @return
	* @return boolean    返回类型
	* @date 2013-1-31 下午08:32:36 
	* @throws
	 */
	public abstract boolean addToRedis(String key, Serializable servializable);
	
	/**
	 * 
	* @Title: addToRedis
	* @Description: TODO(把对象写入redis缓存中)
	* @param @param key
	* @param @param servializable
	* @param @param seconds    过期时间以秒为单位
	* @param @return
	* @return boolean    返回类型
	* @date 2013-1-31 下午08:32:58 
	* @throws
	 */
	public abstract boolean addToRedis(String key, Serializable servializable,int seconds);
	
	/**
	 * 
	* @Title: getRedisResult
	* @Description: TODO(根据key查询redis的缓存结果)
	* @param @param <T>   实体类
	* @param @param key
	* @param @param objClass
	* @param @return
	* @return T    返回类型
	* @date 2013-1-21 下午12:57:15 
	* @throws
	 */
	public abstract <T> T getRedisResult(String key, Class<T> objClass);
	
	/**
	 * 
	* @Title: serializeObj
	* @Description: TODO(把对象序列化为btye[])
	* @param @param servializable
	* @param @return
	* @return byte[]    返回类型
	* @date 2013-1-21 下午12:57:47 
	* @throws
	 */
	public abstract byte[] serializeObj(Serializable servializable);
	
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
	* @return T    返回类型
	* @date 2013-1-21 下午12:58:25 
	* @throws
	 */
	public <T> T reSerializeObj(byte[] bytes, Class<T> objClass);
	
	/**
	 * 从list中获取一个范围的string
	 * 
	 * @param key
	 * @param beginIndex 0
	 * @param endIndex -1 表示取全部
	 * @return
	 */
	public List<String> getRangeStringFromList(String key, long beginIndex,
			long endIndex);
	
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
	public List<String> getKeys(String key);
}
