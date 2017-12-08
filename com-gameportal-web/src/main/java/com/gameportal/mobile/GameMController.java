package com.gameportal.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.order.service.ICompanyCardService;
import com.gameportal.web.payPlat.service.IPayPlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.sms.service.ISmsLogService;
import com.gameportal.web.sms.service.ISmsService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.ClientIP;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.HttpsUtil;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.RandomUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

/**
 * 手机版
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mobile")
public class GameMController {
	private static final Logger logger = Logger.getLogger(GameMController.class);
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService;
	@Resource(name = "userPropertyService")
	private IUserPropertyService userPropertyService;
	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService;
	@Resource(name = "companyCardServiceImpl")
	private ICompanyCardService companyCardService;
	@Resource(name = "activityFlagService")
	private IActivityFlagService activityFlagService;
	@Resource(name = "smsService")
	private ISmsService smsService;
	@Resource(name = "smsLogService")
	private ISmsLogService smsLogService;
	@Resource(name = "payPlatformService")
	private IPayPlatformService PayPlatformService;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
//	@Resource(name = "mgGameAPI")
//	private MgGameAPI mgGameAPI;

	/**
	 * 手机版转账页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfermp")
	public String gameTransfer(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		Class<Object> c = null;
		String key = vuid + "GAMEPORTAL_USER";
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
			List<GameAccount> gameAccountList = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 1);
			List<GamePlatform> listGame = gamePlatformService.queryGame(params);
			if (null != listGame && listGame.size() > 0) {
				gameAccountList = new ArrayList<GameAccount>();
				GameAccount gameAccount = null;
				for (GamePlatform game : listGame) {
					gameAccount = new GameAccount();
					gameAccount.setMoney(0);
					gameAccount.setFullname(game.getFullname());
					gameAccount.setUname(game.getGpname());
					gameAccountList.add(gameAccount);
				}
			}
			// 生成各平台交易号
			request.setAttribute("agBill", "J25_AG" + new RandomUtil().getRandomNumber(15));
			request.setAttribute("aginBill", "J25_AG" + new RandomUtil().getRandomNumber(15));
			request.setAttribute("bbinBill", IdGenerator.getRandomNumber(17));
			request.setAttribute("ptBill", new RandomUtil().getRandomCode(9));
//			request.setAttribute("mgBill", UUID.randomUUID().toString().toUpperCase());
			 request.setAttribute("mgBill",new RandomUtil().getRandomCode(16));
			request.setAttribute("saBill", DateUtil.getStrByDate(new Date(), "yyyMMddHHmmss")+userInfo.getAccount());
			request.setAttribute("gameAccountList", gameAccountList);
			request.setAttribute("listGame", listGame);
		}
		return "/mobile/finance/transfer";
	}

	/**
	 * 登录wap pt游戏
	 * 
	 * @param vuid
	 * @param gameName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/launchptmp")
	public String gameHall(@RequestParam(value = "vuid", required = false) String vuid,
			@RequestParam(value = "gameName", required = false) String gameName, HttpServletRequest request,
			HttpServletResponse response) {
		if (!StringUtils.isNotBlank(vuid)) {
			vuid = CookieUtil.getOrCreateVuid(request, response);
		}
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			// 获取用户最新信息。
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
				if (gamePlatform == null) {
					logger.info("手机端进入游戏，PT平台维护。");
					return "redirect:/indexmp.html";
				}
				IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
				if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
					// 游戏帐号不存在，程序自动创建
					String code = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
					if (!"0".equals(code)) {
						logger.info("手机端进入游戏，创建PT账号失败。");
						return "redirect:/indexmp.html";
					}
				}
				Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
				request.setAttribute("uaccount", WebConst.PT_API_USERNAME_PREFIX + userInfo.getAccount().toUpperCase());
				request.setAttribute("upwd", bf.decryptString(userInfo.getPasswd()));
				request.setAttribute("gameName", gameName);
				return "/mobile/hall";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手机登录PT游戏失败。",e);
		}
		logger.info("手机端进入PT，没有登陆。");
		return "redirect:/indexmp.html";
	}

	/**
	 * 退出PT游戏。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logoutptmp")
	public String logoutPTGame(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("username");
		GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
		String requestUrl = gamePlatform.getDomainname() + "/logout/playername/" + request.getParameter("username");
		logger.info("{" + userName + "}登出PT游戏请求地址:" + requestUrl);
		String result = HttpsUtil.processRequst(requestUrl, gamePlatform.getDeskey());
		logger.info("{" + userName + "}登出PT游戏返回结果:" + result);
		JSONObject json = JSONObject.fromObject(result);
		if (json.get("errorcode") == null) {
			logger.info("登出" + userName + "成功。");
		}
		return "redirect:/indexmp.html";
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
	@RequestMapping(value = "/loginMGmp/{itemId}/{appId}")
	@ResponseBody
	public String loginMGmp(@PathVariable String itemId,@PathVariable String appId, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = null;
		GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
		IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.MG);
		try {
			userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			if (gamePlatform == null) {
				return "redirect:/indexmp.html";
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
					// 游戏帐号不存在，程序自动创建
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
				Map<String, Object> paramMap=new HashMap<>();
				paramMap.put("itemId", itemId);
				paramMap.put("appId", appId);
				String gameurl= (String)gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.addHeader("refresh", "1;url=" + gameurl);
				logger.info("手机登录MG游戏地址："+gameurl);
				return null;
			}
			String requestURL = ClientIP.getInstance().getURL(request);
			if (StringUtils.isNotBlank(requestURL) && requestURL.indexOf("/loginMGmp.html") > 0) {
				response.setHeader("session-status", "timeout");
				response.getWriter().print("Login Timeout!");
			} else {
				//response.sendRedirect("/indexmp.html");
				response.addHeader("refresh", "1;url=/indexmp.html");
			}
		} catch (Exception e) {
			logger.error("LOGIN MG ERROR", e);
			// 登录失败，可能是密码不对，修改用户信息。
			gameInstance.edit(userInfo, gamePlatform, null);
		}
		return null;
	}
	
	@RequestMapping(value = "/loginSamp")
	@ResponseBody
	public String loginGameAG(String type,String gameCode, HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		try {
			IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.SA);
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.SA);
			if (gamePlatform == null) {
				return "redirect:/indexmp.html";
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
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
				paramMap.put("mobile", true);
				String gameurl = (String) gameInstance.loginGame(userInfo, gamePlatform, paramMap);
				response.addHeader("refresh", "1;url="+gameurl);
				return null;
			}
			response.sendRedirect("/index.html");
		} catch (Exception e) {
			logger.error("LOGIN ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
