package com.gameportal.manage.gameplatform.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.api.AGApiResponse;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGameServiceHandler;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.HttpUtil;
import com.gameportal.manage.util.JsonUtils;
import com.gameportal.manage.util.MD5Util;
import com.gameportal.manage.util.RandomUtil;
import com.gameportal.manage.util.WebConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * BBIN波音游戏平台
 * 
 * @author Administrator
 *
 */
@Service("bBINGameServiceHandlerImpl")
@SuppressWarnings("all")
public class BBINGameServiceHandlerImpl implements IGameServiceHandler {

	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	private static Logger logger = Logger.getLogger(BBINGameServiceHandlerImpl.class);

	public BBINGameServiceHandlerImpl() {
		super();
	}

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/CreateMember";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("uppername", gamePlatform.getApiaccount());
		data.put("password", bf.decryptString(userInfo.getApipassword()));
		data.put("key", getKey(gamePlatform.getDeskey(), username, "U3ZalK43", "", 8, 9));
		logger.info("<" + username + ">创建BBIN账号请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + ">创建BBIN账号返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		json = JSONObject.fromObject(json.get("data"));
		return ("21001".equals(json.getString("Code")) || "21100".equals(json.getString("Code"))) ? "0" : "-1"; // 21100注册用户成功或者21001用户重复
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = "http://888.apibox.info/app/WebService/JSON/display.php/Login";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String gameType = paramMap.get("page_site").toString();
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("uppername", gamePlatform.getApiaccount());
		data.put("password", bf.decryptString(userInfo.getApipassword()));
		data.put("key", getKey(gamePlatform.getDeskey(), username, "j30Ak0dY", "", 9, 6));
		data.put("lang", "zh-cn");
		data.put("page_site", gameType);
		logger.info("<" + username + ">登入BBIN游戏请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + ">登入BBIN游戏返回结果：" + result);
		if("ltlottery".equals(gameType)){
			result =result.replaceAll("</form>", "<input name=page_site type='hidden' value='ltlottery'></form>");
		}
		return result;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/Logout";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("key", getKey(gamePlatform.getDeskey(), username, "j5As2zh", "", 7, 8));
		logger.info("<" + username + ">登出BBIN游戏请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + ">登出BBIN游戏返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		json = JSONObject.fromObject(json.get("data"));
		return ("22000".equals(json.getString("Code")) || "22001".equals(json.getString("Code"))) ? "0" : "-1"; // 22000使用者未登入或者22001使用者登出
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/Transfer";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("uppername", gamePlatform.getApiaccount());
		data.put("key",
				getKey(gamePlatform.getDeskey(), username, "n1TBaber84", paramMap.get("billno").toString(), 3, 4));
		data.put("action", "IN");
		data.put("remit", amount);
		data.put("remitno", paramMap.get("billno"));
		logger.info("<" + username + ">转入BBIN请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + "转入BBIN返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		boolean r = json.getBoolean("result");
		AGApiResponse response = new AGApiResponse();
		if (r) {
			response.setInfo("0");
		} else {
			json = JSONObject.fromObject(json.getString("data"));
			response.setInfo(json.getString("Code"));
			response.setMsg(json.getString("Message"));
		}
		System.out.println(JsonUtils.toJson(response).toString());
		return response;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 如果用户不存在，直接返回0；
		if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
			return "0";
		}
		String url = gamePlatform.getDomainname() + "/CheckUsrBalance";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		String key = MD5Util.getMD5Encode(gamePlatform.getDeskey() + username + gamePlatform.getCiphercode());
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("uppername", gamePlatform.getApiaccount());
		data.put("key", getKey(gamePlatform.getDeskey(), username, "4xZ5474fQ", "", 9, 2));
		logger.info("<" + username + ">查询BBIN余额请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + "查询BBIN余额返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		json = JSONObject.fromObject(JSONArray.fromObject(json.get("data")).get(0));
		return json.getString("Balance");
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/Transfer";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("username", username);
		data.put("uppername", gamePlatform.getApiaccount());
		data.put("key",
				getKey(gamePlatform.getDeskey(), username, "n1TBaber84", paramMap.get("billno").toString(), 3, 4));
		data.put("action", "OUT");
		data.put("remit", amount);
		data.put("remitno", paramMap.get("billno"));
		logger.info("<" + username + ">转出BBIN余额请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + "转出BBIN余额返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		boolean r = json.getBoolean("result");
		AGApiResponse response = new AGApiResponse();
		if (r) {
			response.setInfo("0");
		} else {
			json = JSONObject.fromObject(json.getString("data"));
			response.setInfo(json.getString("Code"));
			response.setMsg(json.getString("Message"));
		}
		System.out.println(JsonUtils.toJson(response).toString());
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
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String url = gamePlatform.getDomainname() + "/CheckTransfer";
		String username = WebConstants.AG_BBIN_USERNAME_PREFIX + userInfo.getAccount();
		Map data = new HashMap();
		data.put("website", gamePlatform.getDeskey());
		data.put("key", getKey(gamePlatform.getDeskey(), "", "Pt9E7JI7Bg", "", 3, 4));
		data.put("transid", paramMap.get("billno"));
		logger.info("<" + username + ">查询BBIN订单状态请求参数：" + data.toString());
		String result = HttpUtil.doPost(url, data);
		logger.info("<" + username + "查询BBIN订单状态返回结果：" + result);
		JSONObject json = JSONObject.fromObject(result);
		json = JSONObject.fromObject(json.get("data"));
		return json.getString("Status");
	}

	/**
	 * 生成key
	 * 
	 * @param webSite
	 * @param userName
	 * @param keyB
	 * @return
	 */
	protected String getKey(String webSite, String userName, String keyB, String remitno, int al, int cl) {
		String a = new RandomUtil().getRandomCode(al);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String b = MD5Util.getMD5Encode(webSite + userName + remitno + keyB + sdf.format(new Date()));
		String c = new RandomUtil().getRandomCode(cl);
		String key = (a + b + c).toLowerCase();
		logger.info("key=[" + key + "]");
		return key;
	}
}
