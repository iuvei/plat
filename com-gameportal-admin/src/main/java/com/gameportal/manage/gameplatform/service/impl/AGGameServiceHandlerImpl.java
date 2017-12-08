package com.gameportal.manage.gameplatform.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.api.AGApiResponse;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGameServiceHandler;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.EncryptDecryt;
import com.gameportal.manage.util.HttpUtil;
import com.gameportal.manage.util.MD5Util;
import com.gameportal.manage.util.RandomUtil;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.util.XstreamUtil;

/**
 * AG游戏平台接入。
 * @author Administrator
 *
 */
@Service("agGameServiceHandlerImpl")
@SuppressWarnings("all")
public class AGGameServiceHandlerImpl implements IGameServiceHandler {
	private static Logger logger = Logger.getLogger(AGGameServiceHandlerImpl.class);

	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	public AGGameServiceHandlerImpl() {
		super();
	}

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String actype="1"; //0:试玩账号 1：真钱账号
		if(paramMap!=null && paramMap.containsKey("actype")){
			actype = String.valueOf(paramMap.get("actype"));
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String account = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			account = userInfo.getAccount();
		}
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + account
				+ WebConstants.API_URL_PARAM_SPLIT;
		params += "method=lg" + WebConstants.API_URL_PARAM_SPLIT + "actype="+actype+ WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword())
				+ WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userInfo.getAccount()+">创建账号请求地址："+ url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userInfo.getAccount()+">创建账号返回结果：" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? "0" : "-1";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String sid = gamePlatform.getCiphercode() + new RandomUtil().getRandomNumber(16);
		gamePlatform.setDomainname(gamePlatform.getDomainip());
		String url = gamePlatform.getDomainname() + "/forwardGame.do";
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String userName = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			userName = userInfo.getAccount();
		}
		String gameType =String.valueOf(paramMap.get("gameType"));
		String actype="1"; //0:试玩账号 1：真钱账号
		if(paramMap != null && paramMap.containsKey("actype")){
			actype = String.valueOf(paramMap.get("actype"));
		}
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + userName
				+ WebConstants.API_URL_PARAM_SPLIT;
		params += "actype="+actype+ WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword()) + WebConstants.API_URL_PARAM_SPLIT + "dm="+paramMap.get("dm")
				+ WebConstants.API_URL_PARAM_SPLIT + "sid=" + sid + WebConstants.API_URL_PARAM_SPLIT;
		params += "lang=1" + WebConstants.API_URL_PARAM_SPLIT + "gameType=" +gameType + WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param-->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		url = url + "?params=" + params + "&key=" + key;
		logger.info("<"+userName+">进入AG游戏：" + url);
		return url;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		BigDecimal credit = new BigDecimal(amount);
		String billNo = String.valueOf(paramMap.get("billno"));
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			userName = userInfo.getAccount();
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "method=tc" + WebConstants.API_URL_PARAM_SPLIT
				+ "loginname=" + userName + WebConstants.API_URL_PARAM_SPLIT + "billno=" + billNo + WebConstants.API_URL_PARAM_SPLIT;
		params += "type=IN" + WebConstants.API_URL_PARAM_SPLIT + "credit=" + credit + WebConstants.API_URL_PARAM_SPLIT + "actype=1"
				+ WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword()) + WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userName+">存款AG请求地址：" + url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userName+">存款AG返回结果："+ result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		//return StringUtils.isEmpty(response.getMsg()) ? true : false;
		return response;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 如果用户不存在，直接返回0；
		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
			return "0";
		}
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			userName = userInfo.getAccount();
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + userName
				+ WebConstants.API_URL_PARAM_SPLIT;
		params += "method=gb" + WebConstants.API_URL_PARAM_SPLIT + "actype=1" + WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword())
				+ WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userName+">查询AG余额请求地址：" + url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userName+">查询AG余额返回结果：" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? response.getInfo() : "0";
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		// 转账额度
		BigDecimal credit = new BigDecimal(amount);
		// 长度为15位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			userName = userInfo.getAccount();
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "method=tc" + WebConstants.API_URL_PARAM_SPLIT
				+ "loginname=" + userName + WebConstants.API_URL_PARAM_SPLIT + "billno=" + billNo + WebConstants.API_URL_PARAM_SPLIT;
		params += "type=OUT" + WebConstants.API_URL_PARAM_SPLIT + "credit=" + credit + WebConstants.API_URL_PARAM_SPLIT + "actype=1"
				+ WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword()) + WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userName+">从AG提款请求地址：" + url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userName+">从AG提款返回结果：" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		//return StringUtils.isEmpty(response.getMsg()) ? true : false;
		return response;
	}

	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object edit(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object resetloginattempts(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object getSessionGUID(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap, boolean isCache) {
		return null;
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform,Map paramMap) {
		String billNo = String.valueOf(paramMap.get("billno"));
		BigDecimal credit = new BigDecimal(paramMap.get("credit").toString());
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.AG_API_PREFIX+userInfo.getAccount();
		if(userInfo.getAccount().startsWith("QB77")){
			userName = userInfo.getAccount();
		}
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + userName
				+ WebConstants.API_URL_PARAM_SPLIT + "method=tcc" + WebConstants.API_URL_PARAM_SPLIT + "billno=" + billNo + WebConstants.API_URL_PARAM_SPLIT;
		params += "type="+paramMap.get("type") + WebConstants.API_URL_PARAM_SPLIT + "credit=" + credit + WebConstants.API_URL_PARAM_SPLIT + "actype=1"
				+ WebConstants.API_URL_PARAM_SPLIT + "flag=1" + WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword())
				+ WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userName+">AG确认转账请求地址：" + url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userName+">AG确认转账返回结果：" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		//return StringUtils.isEmpty(response.getMsg()) ? true : false;
		return response;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 长度为15位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "billno=" + billNo + WebConstants.API_URL_PARAM_SPLIT;
		params += "method=qos" + WebConstants.API_URL_PARAM_SPLIT + "actype=1"+ WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param--->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		Map headerMap = new HashMap();
		// 消息头参数
		headerMap.put("User-Agent", "WEB_LIB_GI_" + gamePlatform.getCiphercode());
		Map data = new HashMap();
		// 请求参数
		data.put("params", params);
		data.put("key", key);
		logger.info("<"+userInfo.getAccount()+">查询AG订单状态请求地址："+ url + "?params=" + params + "&key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("<"+userInfo.getAccount()+">查询AG订单状态返回结果：" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
//		return StringUtils.isEmpty(response.getMsg()) ? "0" : "-1";
		return response;
	}
}
