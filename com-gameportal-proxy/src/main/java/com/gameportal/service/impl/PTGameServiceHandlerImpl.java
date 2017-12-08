package com.gameportal.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.domain.GamePlatform;
import com.gameportal.domain.UserInfo;
import com.gameportal.service.IGameServiceHandler;
import com.gameportal.util.Blowfish;
import com.gameportal.util.HttpsUtil;
import com.gameportal.util.WebConstants;

/**
 * PT电子游戏接入。
 * 
 * @author sum	
 *
 */
@Service("ptGameServiceHandlerImpl")
@SuppressWarnings("all")
public class PTGameServiceHandlerImpl implements IGameServiceHandler {
	private static Logger logger = Logger.getLogger(PTGameServiceHandlerImpl.class);

	public PTGameServiceHandlerImpl() {
		super();
	}

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
		String requestUrl = gamePlatform.getDomainname() + "/create/playername/" + userName + "/adminname/" + gamePlatform.getApiaccount()
				+ "/kioskname/" + gamePlatform.getCiphercode()+"/password/"+bf.decryptString(userInfo.getApipassword())+"/custom02/"+gamePlatform.getCiphercode(); // custom02参数表示注册会员账号
		logger.info("PTGameServiceHandlerImpl--->createAccount--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT接口注册账号返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		return (json.get("errorcode") == null || String.valueOf(json.get("errorcode")).equals("19"))? "0" : "-1"; //注册成功或者用户已经注册
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String requestUrl = "http://cache.download.banner.drunkenmonkey88.com/flash/28/casino_drunkenmonkey88/launchcasino.html?language=zh-cn&nolobby=1&game=" + paramMap.get("gameName");
		logger.info("PTGameServiceHandlerImpl--->loginGame--->requestUrl:" + requestUrl);
		return requestUrl;
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String requestUrl = gamePlatform.getDomainname() + "/logout/playername/" + userName;
		logger.info("PTGameServiceHandlerImpl--->logoutGame--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT存款接口返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		return json.get("errorcode") == null ? "0" : "-1";
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String requestUrl = gamePlatform.getDomainname() + "/deposit/playername/" + userName + "/amount/" + amount + "/adminname/"
				+ gamePlatform.getApiaccount() + "/externaltranid/" + paramMap.get("externaltranid");
		logger.info("PTGameServiceHandlerImpl--->deposit--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT存款接口返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		return json.get("errorcode") == null ? true : false;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String requestUrl = gamePlatform.getDomainname() + "/info/playername/" + userName + "/";
		logger.info("PTGameServiceHandlerImpl--->queryBalance--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT查询用户余额接口返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		if(json.get("errorcode") != null){
			return "0";
		}
		result = json.getString("result");
		json = JSONObject.fromObject(result);
		return json.getString("BALANCE");
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = WebConstants.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase();
		String requestUrl = gamePlatform.getDomainname() + "/withdraw/playername/" + userName + "/amount/" + amount + "/adminname/"
				+ gamePlatform.getApiaccount() + "/externaltranid/" + paramMap.get("externaltranid")+"/isForce/1"; //isForce=1表示随时可提款
		logger.info("PTGameServiceHandlerImpl--->withdrawal--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT取款接口返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		return json.get("errorcode") == null ? true : false;
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String requestUrl = gamePlatform.getDomainname() + "/player/checktransaction/externaltransactionid/" + paramMap.get("externaltranid");
		logger.info("PTGameServiceHandlerImpl--->queryOrderStatus--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("调用PT存取款交易状态接口返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		return json.get("errorcode") == null ? "0" : "-1";
	}

	/**
	 * reportname 报表名称参数
	 * PlayerGames 游戏报表
	 * PlayersOnline 在线玩家
	 * PlayerActivity 玩家活动
	 * KioskTransaction
	 */
	@Override
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String reportname = paramMap.get("reportname").toString();
		String requestUrl = gamePlatform.getDomainname() + "/customreport/getdata/reportname/"+paramMap.get("reportname");
		
		if("PlayerGames".equals(reportname)){//游戏报表
			requestUrl +="/startdate/"+paramMap.get("startdate")+"/enddate/"+paramMap.get("enddate")+"/frozen/all/timeperiod/specify/sortby/playername/page/"+paramMap.get("page")+"/perPage/"+paramMap.get("pageSize");
			//requestUrl +="/kioskname/"+paramMap.get("kioskname");
		}
		//requestUrl = "https://kioskpublicapi.mightypanda88.com/customreport/getdata/reportname/PlayerGames/startdate/2015-08-05 13:05:49/enddate/2015-08-06 13:05:59/frozen/yes/timeperiod/specify/sortby/playername";
		System.out.println("requestUrl:"+requestUrl);
		logger.info("PTGameServiceHandlerImpl--->betRecord--->requestUrl:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		JSONObject jsonobj = JSONObject.fromObject(result);
		if(jsonobj.containsKey("error")){
			logger.error("PT数据抓取错误信息："+jsonobj.getString("error")+"->"+jsonobj.getString("errorcode"));
			return null;
		}
		System.out.println(result);
		return result;
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
