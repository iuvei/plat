package com.gameportal.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.domain.GamePlatform;
import com.gameportal.domain.UserInfo;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IGameServiceHandler;
import com.gameportal.util.Blowfish;
import com.gameportal.util.HttpUtil;
import com.gameportal.util.MD5Util;
import com.gameportal.util.RandomUtil;
import com.gameportal.util.WebConstants;



@Service("mgGameServiceHandlerImpl")
public class MGGameServiceHandlerImpl implements IGameServiceHandler {
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	private static Logger logger = Logger
			.getLogger(MGGameServiceHandlerImpl.class);

	public MGGameServiceHandlerImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		String sessionGuid = (String) getSessionGUID(userInfo, gamePlatform,
				paramMap, true);
		if (!StringUtils.isNotBlank(sessionGuid)) {
			sessionGuid = (String) getSessionGUID(userInfo, gamePlatform,
					paramMap, false);
		}
		String url = gamePlatform.getDomainname() + "/api/mg/register.ashx";
		String salt = new RandomUtil().getRandomCode(5);
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		if("lx6688".equals(userName) || "wei888".equals(userName) || "yy9999".equals(userName) || "cxy1212".equals(userName)){
			userName = "dx"+userName;
		}
		String phone = "18688888888";
		String email = "123456@qq.com";
		// 明文密码
		String platformpassword = bf.decryptString(userInfo.getApipassword());
		String md5Code = salt
				+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
						+ gamePlatform.getApiaccount() + userName
						+ platformpassword + phone
						+ email + salt);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("apiAccount", gamePlatform.getApiaccount());
		data.put("userName", userName);
		data.put("password", platformpassword);
		data.put("mobilePhone", phone);
		data.put("email", email);
		data.put("sessionGUID", sessionGuid);
		data.put("code", md5Code);
		String result = HttpUtil.doPost(url, data);
		if(null == result || "".equals(result)){
			return "HTTP Request ERROR.";
		}
		JSONObject resultCode = JSONObject.fromObject(result);
		String numberCode = resultCode.getString("Code");
		logger.info("MGGameServiceHandlerImpl--createAccount" + result);
		return StringUtils.isNotBlank(numberCode) ? numberCode.trim() : "-1";
		// return "-2";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		String pageSite = StringUtils.isNotBlank(ObjectUtils.toString(paramMap
				.get("pageSite"))) ? paramMap.get("pageSite").toString()
				: "live";
		String lang = StringUtils.isNotBlank(ObjectUtils.toString(paramMap
				.get("lang"))) ? paramMap.get("lang").toString() : "zh-CN";
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		if("lx6688".equals(userName) || "wei888".equals(userName) || "yy9999".equals(userName) || "cxy1212".equals(userName)){
			userName = "dx"+userName;
		}
		// 明文密码
		String platformpassword = bf.decryptString(userInfo.getApipassword());
		if (StringUtils.isNotBlank(pageSite) && "live".equals(pageSite)) {
			String url = gamePlatform.getDomainname()
					+ "/api/mg/loginlive.ashx";
			String salt = new RandomUtil().getRandomCode(5);
			String md5Code = salt
					+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
							+ gamePlatform.getApiaccount()
							+ userName + platformpassword + lang
							+ salt);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiAccount", gamePlatform.getApiaccount());
			data.put("userName", userName);
			data.put("password", platformpassword);
			data.put("lang", lang);
			data.put("code", md5Code);
			String result = HttpUtil.doPost(url, data);
			JSONObject resultCode = JSONObject.fromObject(result);
			String numberCode = resultCode.getString("Code");
			if (StringUtils.isNotBlank(numberCode)
					&& "0".equals(numberCode.trim())) {
				return resultCode.getString("Data");
			}
		} else if (StringUtils.isNotBlank(pageSite)
				&& "electronic".equals(pageSite)) {
			String gameName = paramMap.get("gameName").toString();
			String url = gamePlatform.getDomainname()
					+ "/api/mg/loginelectronic.ashx";
			String salt = new RandomUtil().getRandomCode(5);
			String md5Code = salt
					+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
							+ gamePlatform.getApiaccount()
							+ userName + platformpassword
							+ gameName + lang + salt);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiAccount", gamePlatform.getApiaccount());
			data.put("userName", userName);
			data.put("password", platformpassword);
			data.put("gameName", gameName);
			data.put("lang", lang);
			data.put("code", md5Code);
			String result = HttpUtil.doPost(url, data);
			JSONObject resultCode = JSONObject.fromObject(result);
			String numberCode = resultCode.getString("Code");
			if (StringUtils.isNotBlank(numberCode)
					&& "0".equals(numberCode.trim())) {
				return resultCode.getString("Data");
			}
		} else if (StringUtils.isNotBlank(pageSite)
				&& "playcheckurl".equals(pageSite)) {
			String sessionGid = (String) getSessionGUID(userInfo, gamePlatform,
					paramMap, true);
			String url = gamePlatform.getDomainname()
					+ "/api/mg/playcheckurl.ashx";
			String salt = new RandomUtil().getRandomCode(5);
			String md5Code = salt
					+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
							+ gamePlatform.getApiaccount()
							+ userName + platformpassword + lang
							+ sessionGid + salt);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiAccount", gamePlatform.getApiaccount());
			data.put("userName", userName);
			data.put("password", platformpassword);
			data.put("lang", lang);
			data.put("sessionGUID", sessionGid);
			data.put("code", md5Code);
			String result = HttpUtil.doPost(url, data);
			JSONObject resultCode = JSONObject.fromObject(result);
			String numberCode = resultCode.getString("Code");
			if (StringUtils.isNotBlank(numberCode)
					&& "0".equals(numberCode.trim())) {
				return resultCode.getString("Data");
			}

		}
		return null;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform,
			String amount, Map paramMap) {
		// TODO Auto-generated method stub
		String sessionGid = (String) getSessionGUID(userInfo, gamePlatform,
				paramMap, true);
		String url = gamePlatform.getDomainname() + "/api/mg/deposit.ashx";
		String salt = new RandomUtil().getRandomCode(5);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		if("lx6688".equals(userName) || "wei888".equals(userName) || "yy9999".equals(userName) || "cxy1212".equals(userName)){
			userName = "dx"+userName;
		}
		String md5Code = salt
				+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
						+ gamePlatform.getApiaccount() + userName
						+ amount + salt);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("apiAccount", gamePlatform.getApiaccount());
		data.put("userName", userName);
		data.put("amount",amount);
		data.put("sessionGUID", sessionGid);
		data.put("code", md5Code);
		String result = HttpUtil.doPost(url, data);
		JSONObject resultCode = JSONObject.fromObject(result);
		String numberCode = resultCode.getString("Code");
		logger.info("bBINGameServiceHandler--deposit" + result);
		if (StringUtils.isNotBlank(numberCode) && "0".equals(numberCode)) {
			// 成功存款
			return true;
		}
		return false;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		String sessionGid = (String) getSessionGUID(userInfo, gamePlatform,
				paramMap, false);
		String url = gamePlatform.getDomainname() + "/api/mg/balance.ashx";
		String salt = new RandomUtil().getRandomCode(5);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		if("lx6688".equals(userName) || "wei888".equals(userName) || "yy9999".equals(userName) || "cxy1212".equals(userName)){
			userName = "dx"+userName;
		}
		String md5Code = salt
				+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
						+ gamePlatform.getApiaccount() + userName + salt);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("apiAccount", gamePlatform.getApiaccount());
		data.put("userName", userName);
		data.put("sessionGUID", sessionGid);
		data.put("code", md5Code);
		String result = HttpUtil.doPost(url, data);
		JSONObject resultCode = JSONObject.fromObject(result);
		String numberCode = resultCode.getString("Code");
		if (StringUtils.isNotBlank(numberCode) && "0".equals(numberCode.trim())) {
			return resultCode.getString("Data");
		}
		return null;
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform,
			String amount, Map paramMap) {
		// TODO Auto-generated method stub
		String sessionGid = (String) getSessionGUID(userInfo, gamePlatform,
				paramMap, false);
		String url = gamePlatform.getDomainname() + "/api/mg/withdrawal.ashx";
		String salt = new RandomUtil().getRandomCode(5);
		String userName = WebConstants.API_PREFIX+userInfo.getAccount();
		if("lx6688".equals(userName) || "wei888".equals(userName) || "yy9999".equals(userName) || "cxy1212".equals(userName)){
			userName = "dx"+userName;
		}
		String md5Code = salt
				+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
						+ gamePlatform.getApiaccount() + userName
						+ amount + salt);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("apiAccount", gamePlatform.getApiaccount());
		data.put("userName", userName);
		data.put("amount", amount);
		data.put("sessionGUID", sessionGid);
		data.put("code", md5Code);
		String result = HttpUtil.doPost(url, data);
		JSONObject resultCode = JSONObject.fromObject(result);
		String numberCode = resultCode.getString("Code");
		if (StringUtils.isNotBlank(numberCode) && "0".equals(numberCode)) {
			// 成功提款
			return true;
		}
		return false;
	}

	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object edit(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object resetloginattempts(UserInfo userInfo,
			GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getSessionGUID(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap, boolean isCache) {
		String key = iRedisService.getStringFromRedis(userInfo.getUiid()
				+ "SESSIONGUID");
		if (isCache == true && StringUtils.isNotBlank(key)) {
			return key;
		} else {
			String url = gamePlatform.getDomainname()
					+ "/api/mg/sessionguid.ashx";
			String salt = new RandomUtil().getRandomCode(5);
			String md5Code = salt
					+ MD5Util.getMD5Encode(gamePlatform.getDeskey()
							+ gamePlatform.getApiaccount() + salt);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("apiAccount", gamePlatform.getApiaccount());
			data.put("code", md5Code);
			String result = HttpUtil.doPost(url, data);
			logger.info("MGGameServiceHandlerImpl--logoutGame" + result);
			JSONObject resultCode = JSONObject.fromObject(result);
			String numberCode = resultCode.getString("Code");
			if (StringUtils.isNotBlank(numberCode) && "0".equals(numberCode)) {
				String codes = resultCode.getString("Data");
				iRedisService.setObjectFromRedis(userInfo.getUiid()
						+ "SESSIONGUID", codes);
				return codes;
			} else {
				return null;
			}
		}
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo,
			GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo,
			GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
