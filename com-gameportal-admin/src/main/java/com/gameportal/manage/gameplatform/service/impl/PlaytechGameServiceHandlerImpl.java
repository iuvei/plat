package com.gameportal.manage.gameplatform.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.api.PlaytechApiResponse;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGameServiceHandler;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.HttpClientUtil;
import com.gameportal.manage.util.JsonUtils;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONObject;

/**
 * Playtech 游戏接入。
 * 
 * @author sunshine
 *
 */
@Service("playtechGameServiceHandlerImpl")
@SuppressWarnings("rawtypes")
public class PlaytechGameServiceHandlerImpl implements IGameServiceHandler {
	private static Logger logger = Logger.getLogger(PlaytechGameServiceHandlerImpl.class);

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		// 1.检测玩家是否存在
		String url = gamePlatform.getDomainname() + "/player/checkplayerexists/membercode/"
				+ userName;
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 2.创建新玩家，设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		String result = HttpClientUtil.doGet(url, headerMap);
		logger.info("创建玩家账号操作前，检测玩家账号返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
		if ("54".equals(response.getCode()) || "251".equals(response.getCode()) || "0".equals(response.getCode())) { // 玩家存在
			return "0";
		}
		url = gamePlatform.getDomainname() + "/player/createplayer";
		// 设置玩家账号、密码、货币单位。
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("membercode", userName);
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		jsonParam.put("password", bf.decryptString(userInfo.getPasswd()));
		jsonParam.put("currency", "CNY");

		result = HttpClientUtil.doPost(url, headerMap, jsonParam.toString());
		logger.info("调用Playtech创建玩家账号接口返回结果:" + result);
		response = JsonUtils.toBean(result, PlaytechApiResponse.class);
		// 54:玩家已存在/0:创建成功/200:该玩家存在, 请再次创建玩家
		return ("54".equals(response.getCode()) || "0".equals(response.getCode()) || "200".equals(response.getCode()))
				? "0" : "-1";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String requestUrl = "http://cache.download.banner.longsnake88.com/casinoclient.html?game="
				+ paramMap.get("gameName") + "&language=CH";
		logger.info("进入Playtech游戏地址:" + requestUrl);
		return requestUrl;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String url = gamePlatform.getDomainname() + "/player/killsession";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		// 设置玩家账号、类型。
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("membercode", userName);
		jsonParam.put("producttype", 0);

		String result = HttpClientUtil.doPut(url, headerMap, jsonParam.toString());
		logger.info("调用Playtech登出游戏返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
		return "0".equals(response.getCode()) ? "0" : "-1";
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String url = gamePlatform.getDomainname() + "/chip/createtransaction";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		// 设置玩家账号、类型。
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("membercode", userName);
		jsonParam.put("amount", amount);
		jsonParam.put("externaltransactionid", paramMap.get("externaltranid"));
		jsonParam.put("producttype", 0);

		String result = HttpClientUtil.doPost(url, headerMap, jsonParam.toString());
		logger.info("调用Playtech游戏存款接口返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
//		return "0".equals(response.getCode()) ? true : false;
		return response;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
			return "0";
		}
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String url = gamePlatform.getDomainname() + "/account/getbalance/membercode/"
				+ userName + "/producttype/0"; // 0=Playtech
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 2.创建新玩家，设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		String result = HttpClientUtil.doGet(url, headerMap);
		logger.info("调用Playtech查询玩家余额接口返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
		return "0".equals(response.getCode()) ? response.getBalance() : "0";
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String url = gamePlatform.getDomainname() + "/chip/createtransaction";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		// 设置玩家账号、类型。
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("membercode", userName);
		jsonParam.put("amount", "-" + amount);
		jsonParam.put("externaltransactionid", paramMap.get("externaltranid"));
		jsonParam.put("producttype", 0);

		String result = HttpClientUtil.doPost(url, headerMap, jsonParam.toString());
		logger.info("调用Playtech游戏提款接口返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
//		return "0".equals(response.getCode()) ? true : false;
		return response;
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String url = gamePlatform.getDomainname() + "/chip/checktransaction/membercode/" + userName
				+ "/externaltransactionid/" + paramMap.get("externaltranid") + "/producttype/0";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		// 创建新玩家，设置客户端名称、运营商代码。
		headerMap.put("merchantname", gamePlatform.getApiaccount());
		headerMap.put("merchantcode", gamePlatform.getDeskey());
		String result = HttpClientUtil.doGet(url, headerMap);
		logger.info("调用Playtech查询订单状态接口返回结果:" + result);
		PlaytechApiResponse response = JsonUtils.toBean(result, PlaytechApiResponse.class);
		return response;
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
}
