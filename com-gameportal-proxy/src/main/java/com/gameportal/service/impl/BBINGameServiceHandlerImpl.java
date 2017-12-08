package com.gameportal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gameportal.api.AGApiResponse;
import com.gameportal.api.BBINApiResponse;
import com.gameportal.api.BBINBalance;
import com.gameportal.domain.GamePlatform;
import com.gameportal.domain.UserInfo;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IGameServiceHandler;
import com.gameportal.util.Blowfish;
import com.gameportal.util.EncryptDecryt;
import com.gameportal.util.HttpUtil;
import com.gameportal.util.IdGenerator;
import com.gameportal.util.JsonUtils;
import com.gameportal.util.MD5Util;
import com.gameportal.util.RandomUtil;
import com.gameportal.util.WebConstants;
import com.gameportal.util.XstreamUtil;;



@Service("bBINGameServiceHandlerImpl")
@SuppressWarnings("all")
public class BBINGameServiceHandlerImpl implements IGameServiceHandler {

	private static Logger logger = Logger
			.getLogger(BBINGameServiceHandlerImpl.class);

	public BBINGameServiceHandlerImpl() {
		super();
	}
	
	private static final String BBIN_API_USERNAME_PREFIX = "dxy0";

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/bbin/register";
		String username = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + username + gamePlatform.getCiphercode());
		Map data = new HashMap();
		data.put("agent", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("key", key);
		logger.info("request url:" + url + "?agent=" + gamePlatform.getDeskey() + "&username=" + username + "&key=" + key);
		logger.info("bBINGameServiceHandler--createAccount.data:userName=" + username + ",agent=" + gamePlatform.getDeskey() + ",key="
				+ key);
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		BBINApiResponse response = JsonUtils.toBean(result, BBINApiResponse.class);
		return ("21001".equals(response.getCode()) || "21100".equals(response.getCode())) ? "0" : "-1"; // 21100注册用户成功或者21001用户重复
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/bbin/login";
		String userName = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + userName + gamePlatform.getCiphercode());
		Map data = new HashMap();
		data.put("agent", gamePlatform.getDeskey());
		data.put("username", userName);
		data.put("key", key);
		data.put("page_site", paramMap.get("pageSite"));
		logger.info("bBINGameServiceHandler--loginGame.data:userName=" + userName + ",agent=" + gamePlatform.getDeskey() + ",key=" + key
				+ ",page_site=" + paramMap.get("pageSite"));
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		BBINApiResponse response = JsonUtils.toBean(result, BBINApiResponse.class);
		return response.getUrl();
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/api/bbin/logout.ashx";
		String salt = new RandomUtil().getRandomCode(5);
		String userName = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String md5Code = salt + MD5Util.getMD5Encode(gamePlatform.getDeskey() + gamePlatform.getApiaccount() + userName + salt);
		Map data = new HashMap();
		data.put("apiAccount", gamePlatform.getApiaccount());
		data.put("userName", userName);
		data.put("code", md5Code);
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		JSONObject resultCode = JSONObject.fromObject(result);
		String numberCode = resultCode.getString("Code");
		return null;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		// 转账额度只接受整数
		String actionType = "IN";
		// 长度为16位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		String url = gamePlatform.getDomainname() + "/bbin/trans";
		String userName = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + userName + billNo + actionType + amount + gamePlatform.getCiphercode());
		logger.info("bBINGameServiceHandler--deposit.url:" + url + "?agent=" + gamePlatform.getDeskey() + "&username=" + userName + "&billno="
				+ billNo + "&action_type=IN&amount=" + amount + "&key=" + key);
		Map data = new HashMap();
		data.put("agent", gamePlatform.getDeskey());
		data.put("billno", billNo);
		data.put("username", userName);
		data.put("action_type", actionType); // 从网站账号转款到游戏账号
		data.put("amount", amount);
		data.put("key", key);
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		BBINApiResponse response = JsonUtils.toBean(result, BBINApiResponse.class);
		return response.getCode().equals("11100") ? true : false;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/bbin/balance";
		String userName = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + userName + gamePlatform.getCiphercode());
		Map data = new HashMap();
		data.put("agent", gamePlatform.getDeskey());
		data.put("username", userName);
		data.put("key", key);
		logger.info("bBINGameServiceHandler--queryBalance.data:userName=" + userName + ",agent=" + gamePlatform.getDeskey() + ",key=" + key);
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		JSONObject json = JSONObject.fromObject(result);
		String balanceData = json.getString("data");
		if (StringUtils.isNotEmpty(balanceData)) {
			return String.valueOf(JsonUtils.toCollection(balanceData, new TypeReference<ArrayList<BBINBalance>>() {
			}).get(0).getBalance());
		}
		return "0.00";
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String actionType = "OUT";
		// 长度为16位订单号
		String billNo = String.valueOf(paramMap.get("billno"));
		String url = gamePlatform.getDomainname() + "/bbin/trans";
		String userName = BBIN_API_USERNAME_PREFIX+userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + userName + billNo + actionType + amount + gamePlatform.getCiphercode());
		logger.info("bBINGameServiceHandler--withdrawal.url:" + url + "?agent=" + gamePlatform.getDeskey() + "&username=" + userName + "&billno="
				+ billNo + "&action_type=OUT&amount=" + amount + "&key=" + key);
		Map data = new HashMap();
		data.put("agent", gamePlatform.getDeskey());
		data.put("billno", billNo);
		data.put("username", userName);
		data.put("action_type", actionType); // 从游戏账号转款到网站账号
		data.put("amount", amount);
		data.put("key", key);
		String result = HttpUtil.doPost(url, data);
		logger.info("response result:" + result);
		BBINApiResponse response = JsonUtils.toBean(result, BBINApiResponse.class);
		return response.getCode().equals("11100") ? true : false;
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
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
