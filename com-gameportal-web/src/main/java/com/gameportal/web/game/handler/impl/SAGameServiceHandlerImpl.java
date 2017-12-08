package com.gameportal.web.game.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.api.sa.CheckOrderId;
import com.gameportal.web.api.sa.CreditBalanceDV;
import com.gameportal.web.api.sa.DebitBalanceDV;
import com.gameportal.web.api.sa.EGameLoginResponse;
import com.gameportal.web.api.sa.GetUserStatusDV;
import com.gameportal.web.api.sa.LoginRequest;
import com.gameportal.web.api.sa.LoginRequestTryToPlay;
import com.gameportal.web.api.sa.UserInfoResponse;
import com.gameportal.web.api.sa.VerifyUsername;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.HttpUtil;
import com.gameportal.web.util.MD5Util;
import com.gameportal.web.util.SADES;
import com.gameportal.web.util.WebConst;
import com.gameportal.web.util.XstreamUtil;
/**
 * SA老虎机
 * @author Administrator
 *
 */
@Service("saGameServiceHandlerImpl")
public class SAGameServiceHandlerImpl implements IGameServiceHandler{
	private  static Logger logger = Logger.getLogger(SAGameServiceHandlerImpl.class);
		
	private static String key ="g9G16nTs";

	@Override
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=VerifyUsername&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName;
		logger.info("qs:"+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"验证SA账号是否存在返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		VerifyUsername verify =XstreamUtil.toBean(response.replace(filterStr, ""), VerifyUsername.class);
		if(verify.getExist()){
			return "0";
		}
		time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		qs ="method=RegUserInfo&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&CurrencyType=CNY";
		q = SADES.encrypt(key,qs);
		s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"创建SA账号请求参数："+param.toString());
		response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"创建SA账号返回报文："+response);
		filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		UserInfoResponse user =XstreamUtil.toBean(response.replace(filterStr, ""), UserInfoResponse.class);
		if("0".equals(user.getErrorMsgId())){
			return "0";
		}
		return "-1";
	}

	@Override
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=EGameLoginRequest&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&CurrencyType=CNY&GameCode="+paramMap.get("gameCode");
		logger.info("SA登入游戏参数："+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"登入SA请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"登入SA返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		EGameLoginResponse login =XstreamUtil.toBean(response.replace(filterStr, ""), EGameLoginResponse.class);
		if(paramMap.containsKey("mobile")){
			return login.getGameURL()+"?token="+login.getToken()+"&name="+login.getDisplayName()+"&language=1&mobile=true";
		}
		return login.getGameURL()+"?token="+login.getToken()+"&name="+login.getDisplayName()+"&language=1";
	}

	@Override
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=LoginRequest&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&CurrencyType=CNY";
		logger.info("SA登入游戏参数："+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"登入SA游戏大厅请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"登入SA游戏大厅返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		LoginRequest login =XstreamUtil.toBean(response.replace(filterStr, ""), LoginRequest.class);
		return login;
	}

	@Override
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=CreditBalanceDV&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&OrderId="+paramMap.get("billno")+"&CreditAmount="+amount;
		logger.info("SA转入请求参数："+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"转入SA请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"转入SA返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		CreditBalanceDV creditBalance =XstreamUtil.toBean(response.replace(filterStr, ""), CreditBalanceDV.class);
		return creditBalance;
	}

	@Override
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// 如果用户不存在，直接返回0；
		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
			return "0";
		}
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=GetUserStatusDV&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName;
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"查询SA余额请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"查询SA余额返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		GetUserStatusDV userStatus =XstreamUtil.toBean(response.replace(filterStr, ""), GetUserStatusDV.class);
		if("0".equals(userStatus.getErrorMsgId())){
			return userStatus.getBalance();
		}
		return "0";
	}

	@Override
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform, String amount, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=DebitBalanceDV&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&OrderId="+paramMap.get("billno")+"&DebitAmount="+amount;
		logger.info("SA转出请求参数："+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"转出SA请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"转出SA返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		DebitBalanceDV debitBalance =XstreamUtil.toBean(response.replace(filterStr, ""), DebitBalanceDV.class);
		return debitBalance;
	}

	@Override
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap) {
		String userName = userInfo.getAccount().toLowerCase();
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=CheckOrderId&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Username="+userName+"&Checkkey="+WebConst.SA_CHECK_KEY+"&OrderId="+paramMap.get("billno");
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		logger.info(userName+"查询SA订单"+paramMap.get("billno")+"请求参数："+param.toString());
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		logger.info(userName+"查询SA订单"+paramMap.get("billno")+"返回报文："+response);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		CheckOrderId status =XstreamUtil.toBean(response.replace(filterStr, ""), CheckOrderId.class);
		return status;
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
		String time =DateUtil.getStrByDate(new Date(),DateUtil.TIME_FORMAT_MEDIA);
		String qs ="method=LoginRequestTryToPlayDV&Key="+gamePlatform.getDeskey()+"&Time="+time+"&Checkkey="+WebConst.SA_CHECK_KEY+"&CurrencyType=CNY&SlotGameCode="+paramMap.get("gameCode")+"&Amount=100";
		logger.info("SA试玩参数："+qs);
		String q = SADES.encrypt(key,qs);
		String s =MD5Util.getMD5Encode(qs+gamePlatform.getApiaccount()+time+gamePlatform.getDeskey());
		Map<String, Object> param = new HashMap<>();
		param.put("q", q);
		param.put("s", s);
		String response =HttpUtil.doPost(gamePlatform.getDomainname(), param);
		String filterStr = response.substring(response.indexOf("xmlns"),response.indexOf("instance\">")+9);
		LoginRequestTryToPlay login =XstreamUtil.toBean(response.replace(filterStr, ""), LoginRequestTryToPlay.class);
		return login.getSlotGameURL()+"?token="+login.getToken()+"&name="+login.getDisplayName()+"&language=1";
	}

	@Override
	public Object getSessionGUID(UserInfo userInfo, GamePlatform gamePlatform, Map paramMap, boolean isCache) {
		// TODO Auto-generated method stub
		return null;
	}
}
