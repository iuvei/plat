package com.gameportal.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.api.AGApiResponse;
import com.gameportal.domain.GamePlatform;
import com.gameportal.domain.UserInfo;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IGameServiceHandler;
import com.gameportal.util.Blowfish;
import com.gameportal.util.EncryptDecryt;
import com.gameportal.util.HttpUtil;
import com.gameportal.util.IdGenerator;
import com.gameportal.util.MD5Util;
import com.gameportal.util.RandomUtil;
import com.gameportal.util.WebConstants;
import com.gameportal.util.XstreamUtil;


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
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + userInfo.getAccount()
				+ WebConstants.API_URL_PARAM_SPLIT;
		params += "method=lg" + WebConstants.API_URL_PARAM_SPLIT + "actype=1" + WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword())
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--createAccount" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? "0" : "-1";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String sid = gamePlatform.getCiphercode() + new RandomUtil().getRandomNumber(16);
		// 请求地址
		String url = gamePlatform.getDomainname() + "/forwardGame.do";
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "loginname=" + userName
				+ WebConstants.API_URL_PARAM_SPLIT;
		params += "actype=1" + WebConstants.API_URL_PARAM_SPLIT + "password=" + bf.decryptString(userInfo.getApipassword()) + WebConstants.API_URL_PARAM_SPLIT + "dm=www.16898.com"
				+ WebConstants.API_URL_PARAM_SPLIT + "sid=" + sid + WebConstants.API_URL_PARAM_SPLIT;
		params += "lang=1" + WebConstants.API_URL_PARAM_SPLIT + "gameType=1" + WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
		logger.info("request param-->" + params);
		EncryptDecryt ende = new EncryptDecryt(gamePlatform.getDeskey());
		params = ende.encrypt(params);
		String key = MD5Util.getMD5Encode(params + gamePlatform.getApiaccount());
		url = url + "?params=" + params + "&key=" + key;
		logger.info("AGameServiceHandlerImpl--loginGame" + url);
		return url;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		// 转账额度
		BigDecimal credit = new BigDecimal(amount);
		// 长度为15位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--prepareTransferCredit" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? true : false;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--queryBalance" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? response.getInfo() : "0.00";
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		// 转账额度
		BigDecimal credit = new BigDecimal(amount);
		// 长度为15位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--prepareTransferCredit" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? true : false;
	}

	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object edit(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object resetloginattempts(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSessionGUID(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap, boolean isCache) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform,Map paramMap) {
		String billNo = String.valueOf(paramMap.get("billno"));
		BigDecimal credit = new BigDecimal(paramMap.get("credit")+"");
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--transferCreditConfirm" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? true : false;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 长度为15位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		// 请求地址
		String url = gamePlatform.getDomainname() + "/doBusiness.do";
		String params = "cagent=" + gamePlatform.getCiphercode() + WebConstants.API_URL_PARAM_SPLIT + "billno=" + billNo + WebConstants.API_URL_PARAM_SPLIT;
		params += "method=qos" + WebConstants.API_URL_PARAM_SPLIT + "actype=1" + WebConstants.API_URL_PARAM_SPLIT + "cur=CNY";
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
		String result = HttpUtil.doPost(url, data);
		logger.info("AGameServiceHandlerImpl--queryOrderStatus" + result);
		AGApiResponse response = (AGApiResponse) XstreamUtil.toBean(result, AGApiResponse.class);
		return StringUtils.isEmpty(response.getMsg()) ? "0" : "-1";
	}

}
