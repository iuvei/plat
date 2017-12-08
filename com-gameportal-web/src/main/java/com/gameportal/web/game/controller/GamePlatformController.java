package com.gameportal.web.game.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/game")
@SuppressWarnings("all")
public class GamePlatformController{
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService;
//	@Resource(name = "mgGameAPI")
//	private MgGameAPI mgGameAPI;

	private static final Logger logger = Logger.getLogger(GamePlatformController.class);

	public GamePlatformController() {
		super();
	}

	/**
	 * 登录BBIN
	 * 
	 * @param gameType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginGameBBIN")
	@ResponseBody
	public String loginGameBBIN(@RequestParam(value = "ps", required = false) String gameType,
			HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get("BBIN");
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform("BBIN");
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=BBIN");
				return null;
			}
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
					// 游戏帐号不存在，程序自动创建
					String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
					if (!"0".equals(result)) {
						logger.info("登录BBIN游戏->创建账号CODE：" + userInfo.getAccount());
					}else{
						// 添加创建第三方账号标识
						userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
						userInfoService.updateUserInfo(userInfo);
						iRedisService.addToRedis(key, userInfo);
					}
				}
				
				Map paramMap = new HashMap();
				paramMap.put("page_site", gameType);
				String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().write(gameurl);
				return null;
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "home/loginGame";
	}

	@RequestMapping(value = "/loginGameAG/{gameType}/{actype}/{type}")
	@ResponseBody
	public String loginGameAG(@PathVariable String gameType,@PathVariable String actype,@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		Map paramMap = new HashMap();
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(gameType);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(gameType);
			if(gamePlatform == null){
				response.sendRedirect("/game/gamefixed.do?game="+gameType);
				return null;
			}
			/**
			 * 1:旗舰厅 2:国际厅 6:捕鱼王 8：电子游戏 10：彩票
			 */
			paramMap.put("gameType", type);
			paramMap.put("actype", actype);
			if("0".equals(actype)){ // 免费试玩
				Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
				userInfo = new UserInfo();
				userInfo.setAccount("qb"+new RandomUtil().getRandomCode(4));
				userInfo.setApipassword(bf.encryptString("123456"));
				paramMap.put("actype", actype);
				// 游戏帐号不存在，程序自动创建
				String result = (String) gameInstance.createAccount(userInfo, gamePlatform, paramMap);
				logger.info("系统创建AG试玩账号："+userInfo.getAccount());
				if ("0".equals(result)) {
					paramMap.put("dm", request.getServerName());
					String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
					response.sendRedirect(gameurl);
					return null;
				}
			}else{ //真钱账号
				userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
				String platforms =(userInfo.getPlatforms()==null?"":userInfo.getPlatforms());
				if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
					if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
						// 游戏帐号不存在，程序自动创建
						String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
						if (!"0".equals(result)) {
							logger.info("登录BBIN游戏->创建账号CODE：" + userInfo.getAccount());
						}else{
							// 添加创建第三方账号标识
							userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
							userInfoService.updateUserInfo(userInfo);
							iRedisService.addToRedis(key, userInfo);
						}
					}
					
					paramMap.put("dm", request.getServerName());
					String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
					response.sendRedirect(gameurl);
					return null;
				}
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "get login address error!";
	}
	
	/**
	 * AG/AGIN真人视讯游戏登录入口
	 * @param gameType 类型：AG/AGIN两种
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginGameAG/{gameType}")
	@ResponseBody
	public String loginGameAG(@PathVariable String gameType, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(gameType);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(gameType);
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=" + gameType);
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if (userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())) {
					// 游戏帐号不存在，程序自动创建
					String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
					if (!"0".equals(result)) {
						logger.info("登录AG游戏->创建账号CODE：" + userInfo.getAccount());
					} else {
						// 添加创建第三方账号标识
						userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
						userInfoService.updateUserInfo(userInfo);
					}
				}
				Map paramMap = new HashMap();
				paramMap.put("dm", request.getServerName());
				String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.sendRedirect(gameurl);
				return null;
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "home/loginGame";
	}

	/**
	 * 进入AGIN电子游戏
	 * 
	 * @param gameType
	 * @param gameCode
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginGameAGINElec/{gameType}/{gameCode}")
	@ResponseBody
	public String loginGameAGINElec(@PathVariable String gameType, @PathVariable String gameCode,
			HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(gameType);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(gameType);
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=" + gameType);
				return null;
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
					// 游戏帐号不存在，程序自动创建
					String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
					if (!"0".equals(result)) {
						logger.info("登录BBIN游戏->创建账号CODE：" + userInfo.getAccount());
					}else{
						// 添加创建第三方账号标识
						userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
						userInfoService.updateUserInfo(userInfo);
						iRedisService.addToRedis(key, userInfo);
					}
				}
				Map paramMap = new HashMap();
				paramMap.put("dm", request.getServerName());
				paramMap.put("gameType", gameCode);
				String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.sendRedirect(gameurl);
			}
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "home/loginGame";
	}

	/**
	 * 登录MG电子游戏
	 * 
	 * @param gameType
	 * @param flashId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginGameMG/{itemId}/{appId}/{demoFlag}")
	@ResponseBody
	public String loginGameMG(@PathVariable String itemId,@PathVariable String appId,@PathVariable String demoFlag, HttpServletRequest request,
			HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			UserInfo userInfo =null;
			if("0".equals(demoFlag)){
				userInfo = new UserInfo();
				userInfo.setAccount(new RandomUtil().getRandomCode(6));
				Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
				userInfo.setPasswd(bf.encryptString("123456"));
			}else{
				userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			}
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.MG);
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=" + WebConst.MG);
				return null;
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if("1".equals(demoFlag)){
					// 游戏帐号不存在，程序自动创建
					if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
						String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
						if ("-1".equals(result)) {
							logger.info("登录MG游戏->创建账号CODE：" + userInfo.getAccount());
						} else {
							// 添加创建第三方账号标识
							userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
							userInfo.setMgId(result);
							userInfoService.updateUserInfo(userInfo);
						}
					}
				}else{
					String result = gameInstance.createAccount(userInfo, gamePlatform, null)+"";
					if ("-1".equals(result)) {
						logger.info("登录MG游戏->创建试玩账号：" + userInfo.getAccount()+"失败。");
					} 
				}
				Map<String, Object> paramMap=new HashMap<>();
				paramMap.put("itemId", itemId);
				paramMap.put("appId", appId);
				paramMap.put("demoFlag", demoFlag);
				String url= (String)gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.sendRedirect(url);
				return null;
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 进入游戏维护页面。
	 * 
	 * @param game
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gamefixed")
	public String goMaintainPage(String game, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("gameName", game);
		return "home/gamefixed";
	}
	
	/**
	 * 激活游戏账号
	 * @param gameType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/activeGame")
	@ResponseBody
	public String activeGame(String gameType,HttpServletRequest request, HttpServletResponse response){
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		JSONObject json = new JSONObject();
		String result ="";
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(gameType);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if(userInfo ==null){
				return "redirect:/";
			}
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(gameType);
			json.put("code", "1");
			if (gamePlatform == null) {
				json.put("msg", "该游戏平台正处于维护中，请稍候重试，如有疑问，请联系在线客服！");
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
					if(gameType.equals(WebConst.MG)){
						result = (String)gameInstance.createAccount(userInfo, gamePlatform, null);
						if ("-1".equals(result)) {
							logger.info(userInfo.getAccount()+"激活"+gameType+"游戏失败！");
						}else if(!"-1".equals(result)){
							// 添加创建第三方账号标识
							userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
							userInfo.setMgId(result);
							userInfoService.updateUserInfo(userInfo);
							iRedisService.addToRedis(key, userInfo);
						}
					}else{
						result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
						if (!"0".equals(result)) {
							logger.info(userInfo.getAccount()+"激活"+gameType+"游戏失败！");
						}else{
							// 添加创建第三方账号标识
							userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
							userInfoService.updateUserInfo(userInfo);
							iRedisService.addToRedis(key, userInfo);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			json.put("code", "0");
		}
		return json.toString();
	}
}
