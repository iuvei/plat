package com.gameportal.web.game.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.api.sa.LoginRequest;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.WebConst;

/**
 * SA游戏控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class SAGameController {
	private static final Logger logger = Logger.getLogger(SAGameController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService;
	
	/**
	 * SA登录验证地址
	 * @param checkkey
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/qbVerifySa")
	@ResponseBody
	public void verfify(String checkkey,HttpServletRequest request, HttpServletResponse response){
		Writer write =null;
		try {
		  write =response.getWriter();
		  if(checkkey.equals(WebConst.SA_CHECK_KEY)){
			  write.write("checkkeyok");
			  return;
		  }
		  write.write("checkkeyfailed");
		  return;
		} catch (Exception e) {
			logger.error("SA平台登录验证失败。");
		}finally {
			try {
				if(write !=null){
					write.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	
	/**
	 * 登录SA老虎机
	 * @param type
	 * @param gameCode
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginSa")
	@ResponseBody
	public String loginGameSG(String type,String gameCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.SA);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.SA);
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=" + WebConst.SA);
				return null;
			}
			if("0".equals(type)){ //免费试玩
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("gameCode", gameCode);
				String gameurl= (String) gameInstance.resetloginattempts(userInfo, gamePlatform, paramMap);
				response.sendRedirect(gameurl);
				return null;
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
				if (userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())) {
					// 游戏帐号不存在，程序自动创建
					String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
					if (!"0".equals(result)) {
						logger.info("登录SA游戏->创建账号CODE：" + userInfo.getAccount());
					} else {
						// 添加创建第三方账号标识
						userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
						userInfoService.updateUserInfo(userInfo);
					}
				}
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("gameCode", gameCode);
				String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.sendRedirect(gameurl);
				return null;
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		response.sendRedirect("/index.html");
		return null;
	}
	
	/**
	 * 登录SA真人
	 * @param type
	 * @param gameCode
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginSaLive")
	public String loginSaLive(String type,String gameCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.SA);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.SA);
			if (gamePlatform == null) {
				response.sendRedirect("/game/gamefixed.do?game=" + WebConst.SA);
				return null;
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				LoginRequest loginRequest = (LoginRequest) gameInstance.logoutGame(userInfo, gamePlatform, null);
				if("0".equals(loginRequest.getErrorMsgId())){
					if (userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())) {
						// 游戏帐号不存在，程序自动创建
						String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
						if (!"0".equals(result)) {
							logger.info("登录SA游戏->创建账号CODE：" + userInfo.getAccount());
						} else {
							// 添加创建第三方账号标识
							userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
							userInfoService.updateUserInfo(userInfo);
						}
					}
				}
				request.setAttribute("loginRequest", loginRequest);
				request.setAttribute("plat", gamePlatform);
				return "/home/salogin";
			}
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return "/";
	}
}
