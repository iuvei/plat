package com.gameportal.manage.gameplatform.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGameServiceHandler;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.HttpClientUtil;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * MG游戏平台
 * 
 * @author Administrator
 *
 */
@Service("mgGameServiceHandlerImpl")
@SuppressWarnings("all")
public class MGGameServiceHandlerImpl implements IGameServiceHandler {
	private static Logger logger = Logger.getLogger(MGGameServiceHandlerImpl.class);

	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	private Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);

	public MGGameServiceHandlerImpl() {
		super();
	}

	public static Map<String, Object> getHeaderMap(){
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("Authorization", "Basic R2FtaW5nTWFzdGVyMUNOWV9hdXRoOjlGSE9SUWRHVFp3cURYRkBeaVpeS1JNZ1U=");
		headerMap.put("X-DAS-TZ", "UTC+8");
		headerMap.put("ContentType", "application/x-www-form-urlencoded;charset=utf-8");
		headerMap.put("X-DAS-CURRENCY", "CNY");
		headerMap.put("X-DAS-TX-ID", "TEXT-TX-ID");
		headerMap.put("X-DAS-LANG", "zh-CN");
		return headerMap;
	}
	
	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
		body.put("parent_id", gamePlatform.getDomainip());
	    String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		body.put("username", account); //用户名
		body.put("password", bf.decryptString(userInfo.getPasswd()));  //密码
		body.put("ext_ref", account); //用户名
		System.out.println(account+"创建MG账号请求参数："+body.toString());
		String result=HttpClientUtil.doPost(gamePlatform.getDomainname()+"/v1/account/member", headerMap, body.toString());
		System.out.println(account+"创建MG账号返回结果："+result);
		JSONObject json =JSONObject.fromObject(result);
		if(json.containsKey("data")){ // 账号创建成功
			return JSONObject.fromObject(json.get("data")).get("id")+"";
		}else if(json.containsKey("error")){
			json =JSONObject.fromObject(json.get("error"));
			if(json.getInt("code") ==409){ //玩家账号已存在
				String message =json.getString("message").split(":")[2];
				message = message.substring(0, message.lastIndexOf(","));
				return message.replace(",", "").trim();
			}
		}
		return "-1";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		System.out.println(headerMap.toString());
		JSONObject body = new JSONObject();
	    String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		body.put("account_id", userInfo.getMgId());
		body.put("category", "TRANSFER");
		body.put("balance_type", "CREDIT_BALANCE");
		body.put("type", "CREDIT");
		body.put("amount", Double.valueOf(amount));
		body.put("external_ref", paramMap.get("externaltranid"));
		List<Object> data = new ArrayList<>();
		data.add(body);
		System.out.println(account+"向MG存款请求参数："+data.toString());
		String result =HttpClientUtil.doPost(gamePlatform.getDomainname()+"/v1/transaction", headerMap,data.toString());
		System.out.println(account+"向MG存款返回结果："+result);
		return result;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 如果用户不存在，直接返回0；
		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
			return "0";
		}
//				String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//				headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		String result =HttpClientUtil.doGet(gamePlatform.getDomainname()+"/v1/wallet?account_id="+userInfo.getMgId(), headerMap);
		System.out.println(account+"查询MG余额返回结果："+result);
		JSONObject json = JSONObject.fromObject(result);
		if(json.containsKey("error")){
			return "0";
		}
		return JSONArray.fromObject(json.getString("data")).getJSONObject(0).getString("credit_balance");
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
	    String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		body.put("account_id", userInfo.getMgId());
		body.put("category", "TRANSFER");
		body.put("balance_type", "CREDIT_BALANCE");
		body.put("type", "DEBIT");
		body.put("amount", Double.valueOf(amount));
		body.put("external_ref", paramMap.get("externaltranid"));
		List<Object> data = new ArrayList<>();
		data.add(body);
		System.out.println(account+"从MG提款请求参数："+body.toString());
		String result =HttpClientUtil.doPost(gamePlatform.getDomainname()+"/v1/transaction", headerMap,data.toString());
		System.out.println(account+"从MG提款返回结果："+result);
		return result;
	}

	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object edit(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
		body.put("account_id", userInfo.getMgId());
		body.put("password", bf.decryptString(userInfo.getPasswd()));  //密码

		String result =HttpClientUtil.doPut(gamePlatform.getDomainname()+"/v1/account/member/password", headerMap, body.toString());
		String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		System.out.println(account+"修改密码返回结果："+result);
		JSONObject json  = JSONObject.fromObject(result);
		if(!json.containsKey("error")){
			return "0";
		}
		return "-1";
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
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConstants.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		String account = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
		String result =HttpClientUtil.doGet(gamePlatform.getDomainname()+"/v1/transaction?account_id="+userInfo.getMgId()+"&ext_ref="+paramMap.get("externaltranid"), headerMap);
		
//		String result =new String(HttpUtil.doGet(gamePlatform.getDomainname()+"/v1/transaction?account_id="+userInfo.getMgId()+"&ext_ref="+paramMap.get("externaltranid"), headerMap,null,0));
		System.out.println(account+"查询MG订单【"+paramMap.get("externaltranid")+"】状态返回结果："+result);
		JSONObject json  = JSONObject.fromObject(result);
		if(!json.containsKey("error")){
			return "0";
		}
		return "-1";
	}
}
