package com.na.gate.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.proto.bean.PengMerchantQueueResponse;
import com.na.gate.proto.bean.PlayerData;

/**
 * @author Andy
 * @version 创建时间：2017年9月26日 下午4:46:39
 */
public class GateCache {

	public static Cache<String, PengMerchantQueueResponse> pendingMerchantCache = CacheBuilder.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES).build();

	public static Cache<String, List<PlayerData>> pindingPlayerCache = CacheBuilder.newBuilder()
			.expireAfterWrite(20, TimeUnit.MINUTES).build();

	/**
	 * 等待退出的玩家集合：异常退出存在未结算
	 */
	public static Cache<Long, PlatformUserAdapter> WAIT_LOGOUT_USER_MAP = CacheBuilder.newBuilder().build();

	/**
	 * 异常流水账单局号
	 */
	public static Set<Long> ROUNDSET = new LinkedHashSet<>();
	
	/**
	 * key 真人用户
	 * 
	 */
	public static Map<Long, PlatformUserAdapter> PLATFORM_USER_ADAPTER_MAP =new HashMap<>();
	
	/**
	 * 平台维护 0 正常  1维护中
	 */
	public static String platMaintenance="0";

}
