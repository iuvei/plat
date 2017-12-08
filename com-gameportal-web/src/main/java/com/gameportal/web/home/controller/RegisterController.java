package com.gameportal.web.home.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.gameportal.web.user.model.Electricpin;
import com.gameportal.web.user.model.ElectricpinList;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserLoginLog;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserManagerService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.ClientIP;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.WebConst;
import com.gameportal.web.util.XstreamUtil;
import com.gameportal.web.util.ip.IPSeeker;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/register")
public class RegisterController{
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap;
	@Resource(name="userManagerService")
	private IUserManagerService userManagerService = null;
	private static final Logger logger = Logger
			.getLogger(RegisterController.class);

	public RegisterController() {
		super();
	}

	@RequestMapping(value = "/validform/{authcode}")
	public @ResponseBody
	String validform(@PathVariable Integer authcode,
			@RequestParam(value = "param", required = false) String code,
			HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo systemUser = (UserInfo) iRedisService.getRedisResult(key, c);
		JSONObject json = new JSONObject();
		if (authcode.intValue() == 1) {
			UserInfo userInfo = userInfoService.queryUserInfo(code, null, null,
					null);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				json.put("status", "y");
				json.put("info", "验证通过！");
			} else {
				json.put("status", "n");
				json.put("info", "会员帐号已经注册，请重新输入！");
			}
		} else if (authcode.intValue() == 2) {
			UserInfo userInfo = userInfoService.queryUserInfo(null, code, null,
					null);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				json.put("status", "y");
				json.put("info", "验证通过！");
			} else {
				json.put("status", "n");
				json.put("info", "您的电话已经注册过，请重新输入！");
			}
		} else if (authcode.intValue() == 3) {
			UserInfo userInfo = userInfoService.queryUserInfo(null, code, null,
					null);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				json.put("status", "y");
				json.put("info", "验证通过！");
			} else {
				json.put("status", "n");
				json.put("info", "您的电话已经注册过，请重新输入！");
			}
		} else if (authcode.intValue() == 4) {
			UserInfo userInfo = userInfoService.queryUserInfo(null, null, code,
					null);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				json.put("status", "y");
				json.put("info", "验证通过！");
			} else {
				if (userInfo.getEmail().equals(systemUser.getEmail())) {
					json.put("status", "y");
					json.put("info", "验证通过！");
				} else {
					json.put("status", "n");
					json.put("info", "您的电话已经注册过，请重新输入！");
				}
			}
		} else if(authcode.intValue() == 5){
			String token = (String) request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
			if(StringUtils.isEmpty(code)){
				json.put("status", "n");
				json.put("info", "请填写	验证码！");
			}else if(!code.equals(token)){
				json.put("status", "n");
				json.put("info", "验证码填写错误！");
			}else{
				json.put("status", "y");
				json.put("info", "验证通过！");
			}
		}else {
			
			UserInfo userInfo = userInfoService.queryUserInfo(null, null, code,
					null);
			if (!StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
				json.put("status", "y");
				json.put("info", "验证通过！");
			} else {
				json.put("status", "n");
				json.put("info", "你的邮箱已经注册过，请重新输入！");
			}
		}
		return json.toString();
	}
	
	/**
	 * t
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/tes")
	public String test(HttpServletRequest request, HttpServletResponse response){
		System.out.println(ClientIP.getInstance().getURL(request));
		return null;
	}
	public static boolean checkAccount(String value) {
		String regex = "^[a-zA-z0-9]{6,15}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	@RequestMapping(value = "/index")
	public @ResponseBody
	String register(
			@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "passwd", required = false) String passwd,
			@RequestParam(value = "realname", required = false) String realname,
			//@RequestParam(value = "atmpasswd", required = false) String atmpasswd,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "identitycard", required = false) String identitycard,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "qq", required = false) String qq,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "domain", required = false) String domain,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		UserInfo userInfo = null;
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + "GAMEPORTAL_USER";
		try {
			String cacheCode = (String)request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
			if(!StringUtils.isNotBlank(code) || !code.equals(cacheCode)){
				json.put("code", "9");
				json.put("info", "验证码错误！");
				return json.toString();
			}
			if(StringUtils.isEmpty(account.trim())){
				json.put("code", "2");
				json.put("info", "请输入您要注册的会员账号！");
				return json.toString();
			}
			if(!checkAccount(account.trim())){
				json.put("code", "2");
				json.put("info", "用户账号格式不正确：账号长度6-15个字符,以及仅限含有字母和数字的组合！");
				return json.toString();
			}
			Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
			if (StringUtils.isNotEmpty(account)) {
				userInfo = userInfoService.queryUserInfo(account.trim(), null, null, null);
				if (userInfo != null) {
					json.put("code", "2");
					json.put("info", "您输入的帐号已经注册过，请重新输入！");
					return json.toString();
				}
			}
			if (StringUtils.isNotEmpty(phone)) {
				 Map<String, Object> param = new HashMap<String, Object>();
                 param.put("phone", phone);
                 long count = userInfoService.getUserInfoCount(param);
                 if (count > 0) {
                	json.put("code", "3");
 					json.put("info", "您输入的手机已经注册过，请重新输入！");
 					return json.toString();
                 }
			}
//			if (StringUtils.isNotEmpty(email)) {
//				userInfo = userInfoService.queryUserInfo(null, null, email.trim(), null);
//				if (userInfo != null) {
//					json.put("code", "4");
//					json.put("info", "您输入的邮箱已经注册过，请重新输入！");
//					return json.toString();
//				}
//			}
			String serverName = request.getServerName();
			String[] fg = serverName.split("\\.");
			if(fg.length == 2){//当用户用二级域名的时候如:16898.com
				serverName = "www."+fg[0]+"."+fg[1];
			}
			//代理验证
			String proxyid = userInfoService.queryProxyID(serverName);
			if(null == proxyid || "".equals(proxyid) || "0".equals(proxyid)){
				if(null != domain && !"".equals(domain)){
					if(WebConst.isInteger(domain) && WebConst.isNonnegative(domain)){
						UserInfo proxyUser = userInfoService.findProxyInfoId(Long.valueOf(domain), 0);
						if(null == proxyUser){
							domain = "0";
						}
						channel = proxyUser.getUlabel();
					}else{
						domain = "0";
					}
				}else{
					domain = "0";
				}
			}else{
				UserInfo proxyUser = userInfoService.findProxyInfoId(Long.valueOf(proxyid), 0);
				if(null != proxyUser){//设置渠道，注册用户选择代理渠道
					channel = proxyUser.getUlabel();
				}
				domain = proxyid;
			}
			
			userInfo = new UserInfo();
			userInfo.setAccount(account.trim());
			userInfo.setPasswd(bf.encryptString(passwd.trim()));
			userInfo.setApipassword(bf.encryptString(passwd.trim()));
			userInfo.setPhone(phone);
			if(StringUtils.isNotBlank(channel)){
				userInfo.setUlabel(channel);
			}else{
				userInfo.setUlabel("direct");
			}
			
			userInfo.setUrl(ClientIP.getInstance().getURL(request));
			userInfo.setRegip(ClientIP.getInstance().getIpAddr(request));
			Integer accounttype = 0;
			userInfo.setAccounttype(accounttype);
			userInfo.setGrade(1);
			userInfo.setPuiid(Long.valueOf(domain));
			userInfo.setStatus(1);
			Date date = new Date();
			userInfo.setLogincount(1);
			userInfo.setCreateDate(date);
			userInfo.setLoginip(ClientIP.getInstance().getIpAddr(request));
			userInfo.setUpdateDate(date);
			userInfo.setRegdevice(request.getHeader("User-Agent"));
			userInfo = userInfoService.saveUserInfo(userInfo);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo
					.getUiid()))) {
				Date now = DateUtil.getDateByStr(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				userInfo.setLoginClient(request.getHeader("User-Agent"));
				userInfo.setLoginurl(ClientIP.getInstance().getURL(request));
				userInfo.setKey(key);
				//更新用户登录
				UserInfo upUser = new UserInfo();
				upUser.setUiid(userInfo.getUiid());
				upUser.setLoginip(ClientIP.getInstance().getIpAddr(request));
				upUser.setLogincount(1);
				upUser.setUpdateDate(now);
				upUser.setIparea(IPSeeker.getInstance().getAddress(upUser.getLoginip()));
				iRedisService.addToRedis(vuid + "GAMEPORTAL_USER", userInfo);
				iRedisService.setObjectFromRedis(userInfo.getAccount()+"_"+userInfo.getUiid(), key);
				// 设置关联账号
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("loginip", upUser.getLoginip());
				List<String> logs = userInfoService.selectUserLoginLog(params);
				String relaAccount ="";
				if(CollectionUtils.isNotEmpty(logs)){
					for (String ul : logs) {
						if (relaAccount.length() > 0) {
							relaAccount += ",";
						}
						relaAccount += ul;
					}
				}
				if(StringUtils.isNotEmpty(relaAccount)){
					upUser.setRelaflag(1);
					upUser.setRelaaccount(relaAccount); // 设置关联账号
				}
				userInfoService.updateLogin(upUser);
				/*保持用户登录日志*/
				String referer = request.getHeader("Referer");
				if(!StringUtils.isNotBlank(referer)){
					referer = ClientIP.getInstance().getURL(request);
				}
				UserLoginLog loginlog = new UserLoginLog();
				loginlog.setUiid(userInfo.getUiid().intValue());
				loginlog.setAccount(userInfo.getAccount());
				loginlog.setTruename(userInfo.getUname());
				loginlog.setLogintime(DateUtil.getStrYMDHMSByDate(new Date()));
				loginlog.setLoginip(ClientIP.getInstance().getIpAddr(request));
				loginlog.setLoginsource(referer);
				loginlog.setLogindevice(request.getHeader("User-Agent"));
				loginlog.setIparea(IPSeeker.getInstance().getAddress(loginlog.getLoginip()));
				userInfoService.insertLog(loginlog);
				
				json.put("code", "1");
				json.put("info", "注册成功!");
				
//				List<Electricpin> elist = getE(request);
//				//随机分配给电销
//				UserManager umanager = new UserManager();
//				umanager.setUiid(userInfo.getUiid());
//				umanager.setBelongid(0L);
//				umanager.setPayertype("slot");
//				boolean isFP = true;
//				if(elist!=null && elist.size() > 0){
//					for(Electricpin object : elist){
//						if(serverName.equals(object.getDomain())){
//							umanager.setBelongid(Long.valueOf(object.getEid()));
//							isFP = false;
//							break;
//						}
//					}
//				}
//				if(isFP){
//					List<String> dxlist = userInfoService.getDXList();
//					if(dxlist.size() > 0){
//						int number = new Random().nextInt(dxlist.size());
//						umanager.setBelongid(Long.valueOf(dxlist.get(number)));//随机分配
//					}
//				}
//				userManagerService.insert(umanager);
			}
			
		} catch (Exception e) {
			logger.error("注册失败", e);
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
			return json.toString();
		}
		try {
			// 添加PT游戏账号
			GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
			if(gamePlatform != null){
				IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
				String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("会员注册时添加PT游戏账号失败。");
				}else{
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
					userInfoService.updateUserInfo(userInfo);
					iRedisService.addToRedis(key, userInfo);
				}
			}
		} catch (Exception e) {
			logger.error("注册PT账号异常", e);
		}
		return json.toString();
	}

	@RequestMapping(value = "/confirm")
	public @ResponseBody
	String confirm(
			@RequestParam(value = "uiid", required = false) Long uiid,
			@RequestParam(value = "realname", required = false) String realname,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "identitycard", required = false) String identitycard,
			@RequestParam(value = "email", required = false) String email,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		try {
			UserInfo userInfo = userInfoService.findUserInfoId(uiid);
			if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))
					&& userInfo.getStatus() == 1L) {
				userInfo.setUname(realname);
				userInfo.setPhone(phone);
				userInfo.setIdentitycard(identitycard);
				userInfo.setEmail(email);
				userInfo.setStatus(2);
				userInfo.setUpdateDate(new Date());
				boolean code = userInfoService.modifyUserInfo(userInfo);
				if (code == true) {
					iRedisService
							.addToRedis(vuid + "GAMEPORTAL_USER", userInfo);
					json.put("code", "1");
					json.put("info", "操作成功!");
				} else {
					json.put("code", "0");
					json.put("info", "操作失败，请稍后再试!");
				}
			} else {
				json.put("code", "0");
				json.put("info", "非法操作，请联系客服!");
			}
		} catch (Exception e) {
			json.put("code", "0");
			json.put("info", "网络异常，请稍后再试！");
		}
		return json.toString();
	}
	
	private List<Electricpin> getE(HttpServletRequest request){
		InputStream is = null;
		ByteArrayOutputStream out = null;
		List<Electricpin> list = null;
		try {
			String path = request.getRealPath("WEB-INF/electric-pin.xml");
			is = new FileInputStream(new File(path));
			out = new ByteArrayOutputStream();
			int i = 1;
			while ((i = is.read()) != -1) {
				out.write(i);
			}
			ElectricpinList electricpinList = XstreamUtil.toBean(new String(out.toByteArray(),"utf-8"), ElectricpinList.class);
			list = electricpinList.getElectricpin();
			logger.info("电销人员列表信息成功。");
			out.close();
			is.close();
		} catch (Exception e) {
			logger.error("电销人员列表信息失败。");
		}
		return list;
	}
}
