package com.gameportal.web.game.handler.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.HttpClientUtil;
import com.gameportal.web.util.HttpUtil;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

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
	private Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

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

	public static String getToken(GamePlatform gamePlatform) {
		// body = new JSONObject();
		 Map<String, Object> body = new HashMap<String, Object>();
		body.put("grant_type", gamePlatform.getDeskey());
		body.put("username", gamePlatform.getApiaccount());
		body.put("password", gamePlatform.getCiphercode());
		return HttpUtil.doPost(gamePlatform.getDomainname()+"/oauth/token", getHeaderMap(), body,"utf-8");
	}


	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		//{"meta":{"currency":"CNY","time_zone":"UTC","transaction_id":"TEXT-TX-ID","processing_time":389},"data":{"id":1092808,"my_path":"1090524,1092808","version":1,"user_id":1080826,"username":"QB7qb741:124","user_status":"ENABLED","parent_id":1090524,"name":"QB7qb741","type":"MEMBER","currency_unit":"CNY","status":"ENABLED","test":false,"ip_whitelist":false,"created_by":1078570,"created":"2017-08-03 06:56:35.356","updated_by":1078570,"updated":"2017-08-03 06:56:35.356"}}
		//{"meta":{"currency":"CNY","time_zone":"UTC","transaction_id":"TEXT-TX-ID","processing_time":33},"error":{"type":"HTTP_EXCEPTION","code":409,"message":"Account conflicts with id: 1,092,808, name: QB7qb741"}}
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
		body.put("parent_id", gamePlatform.getDomainip());
	    String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
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
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
	    String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
		if(!paramMap.containsKey("demo")){
			String demoFlag = (String)paramMap.get("demoFlag");
			if("0".equals(demoFlag)){
				body.put("demo", true);
			}else{
				body.put("account_id", userInfo.getMgId());
				body.put("demo", false);
			}
		}
		body.put("item_id", paramMap.get("itemId"));
		body.put("app_id",paramMap.get("appId"));
		JSONObject json = new JSONObject();
		json.put("lang", "zh_CN");
		body.put("login_context", json.toString());
		String result = HttpClientUtil.doPost(gamePlatform.getDomainname()+"/v1/launcher/item", headerMap, body.toString());
		return JSONObject.fromObject(result).get("data");
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}
	
	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 如果用户不存在，直接返回0；
		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
			return "0";
		}
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
		String result =HttpClientUtil.doGet(gamePlatform.getDomainname()+"/v1/wallet?account_id="+userInfo.getMgId(), headerMap);
		System.out.println(account+"查询MG余额返回结果："+result);
		JSONObject json = JSONObject.fromObject(result);
		if(json.containsKey("error")){
			return "0";
		}
		return JSONArray.fromObject(json.getString("data")).getJSONObject(0).getString("credit_balance");
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		System.out.println(headerMap.toString());
		JSONObject body = new JSONObject();
	    String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
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
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
	    String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
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
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
		String result =HttpClientUtil.doGet(gamePlatform.getDomainname()+"/v1/transaction?account_id="+userInfo.getMgId()+"&ext_ref="+paramMap.get("externaltranid"), headerMap);
		
//		String result =new String(HttpUtil.doGet(gamePlatform.getDomainname()+"/v1/transaction?account_id="+userInfo.getMgId()+"&ext_ref="+paramMap.get("externaltranid"), headerMap,null,0));
		System.out.println(account+"查询MG订单【"+paramMap.get("externaltranid")+"】状态返回结果："+result);
		JSONObject json  = JSONObject.fromObject(result);
		if(!json.containsKey("error")){
			return "0";
		}
		return "-1";
	}

	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {

		return null;
	}

	@Override
	public Object edit(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
//		String token = getToken(gamePlatform);
		String token=iRedisService.getRedisResult(WebConst.MG_TOKEN, null);
		Map<String, Object> headerMap =getHeaderMap();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("Authorization", "Bearer "+JSONObject.fromObject(token).getString("access_token"));
		headerMap.put("Authorization", "Bearer "+token);
		JSONObject body = new JSONObject();
		body.put("account_id", userInfo.getMgId());
		body.put("password", bf.decryptString(userInfo.getPasswd()));  //密码

		String result =HttpClientUtil.doPut(gamePlatform.getDomainname()+"/v1/account/member/password", headerMap, body.toString());
		String account = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
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

	public static void main(String[] args) {
		UserInfo userInfo = new UserInfo();
		userInfo.setMgId("1106546");
		userInfo.setAccount("qb742");
		userInfo.setPasswd("dbc354b27467d3f0d732edbbd7f1e3234c6eaae304470f2f");
		GamePlatform gamePlatform = new GamePlatform();
		gamePlatform.setDomainname("https://api.adminserv88.com");
		gamePlatform.setDeskey("password");
		gamePlatform.setApiaccount("ma08sa01test_API");
		gamePlatform.setCiphercode("11111111");
		gamePlatform.setDomainip("1090524");
		MGGameServiceHandlerImpl mg = new MGGameServiceHandlerImpl();
		System.out.println(mg.getToken(gamePlatform));
//		System.out.println(mg.createAccount(userInfo, gamePlatform, headerMap));
//		System.out.println(mg.edit(userInfo, gamePlatform, headerMap));
//		System.out.println(mg.queryBalance(userInfo, gamePlatform, headerMap));
		Map<String, Object> paramMap=new HashMap<>();
//		paramMap.put("externaltranid", UUID.randomUUID().toString().toUpperCase());
		paramMap.put("externaltranid", new RandomUtil().getRandomCode(16));
		System.out.println(paramMap.toString());
		System.out.println(mg.deposit(userInfo, gamePlatform, "1", paramMap));
		
//		System.out.println(mg.withdrawal(userInfo, gamePlatform, "1", paramMap));
		System.out.println(mg.queryOrderStatus(userInfo, gamePlatform, paramMap));
		System.out.println(mg.queryBalance(userInfo, gamePlatform, getHeaderMap()));
	}
}
