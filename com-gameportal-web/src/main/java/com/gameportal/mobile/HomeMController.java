package com.gameportal.mobile;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gameportal.web.adver.service.IAdverService;
import com.gameportal.web.agent.service.IProxyWebSitePvService;
import com.gameportal.web.game.model.AGElectronic;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.MGElectronic;
import com.gameportal.web.game.model.SAElectronic;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.CheckMobile;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.WebConst;

/**
 * 手机版
 * @author add by sum 2016.08.09
 *
 */
@Controller
public class HomeMController {
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService;
    @Resource(name="adverServiceImpl")
    private IAdverService adverService;
    @Resource(name="proxyWebSitePvService")
    private IProxyWebSitePvService proxyWebSitePvService;
    @Resource(name = "gamePlatformServiceImpl")
    private IGamePlatformService gamePlatformService;

    private static final Logger logger = Logger.getLogger(HomeMController.class);

    public HomeMController() {
        super();
    }

    /**
     * 进入主页。
     * @param adLocation
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/indexmp")
    public String index(String adLocation,HttpServletRequest request, HttpServletResponse response) {
        // 加载首页广告
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("status", 1);
//        request.setAttribute("adverList",adverService.queryAllAdver(paramMap));
    	String userAgent =request.getHeader( "USER-AGENT" ).toLowerCase();
    	if(CheckMobile.check(userAgent)){
    		return "/mobile/main";
    	}
    	return "/home/index";
    }
    
    @RequestMapping(value = "/intrmp")
    public String intrmp(HttpServletRequest request, HttpServletResponse response) {
    	request.setAttribute("intrid", request.getParameter("aff"));
        return "/mobile/register";
    }
    
    /**
     * 注册
     * @param adLocation
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/registermp")
    public String register(HttpServletRequest request, HttpServletResponse response) {
    	String userAgent =request.getHeader( "USER-AGENT" ).toLowerCase();
    	if(CheckMobile.check(userAgent)){
    		return "/mobile/register";
    	}
    	return "/register/index";
    }
    /**
     * 登录
     * @param adLocation
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/loginmp")
    public String login(HttpServletRequest request, HttpServletResponse response) {
    	String userAgent =request.getHeader( "USER-AGENT" ).toLowerCase();
    	if(CheckMobile.check(userAgent)){
    		return "/mobile/login";
    	}
		return "home/index";
    }
    
    
    /**
     * 手机版真人页面
     * @param adLocation
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/mainmp")
    public String live(HttpServletRequest request, HttpServletResponse response) {
        return "/mobile/main";
    }

    /**
     * PT游戏列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/game/ptmp")
    public String gameList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "gamename", required = false) String gamename,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
      String vuid = CookieUtil.getOrCreateVuid(request, response);
      request.setAttribute("vuid", vuid);
      if(StringUtils.isEmpty(type)){
          type ="0";
      }
      Map<String, Object> params = new HashMap<String, Object>();
      if(StringUtils.isNotEmpty(gamename)){
      	gamename= URLDecoder.decode(gamename,"utf8");
      	params.put("gamename", gamename);
      }else{
      	params.put("categoryID", "0".equals(type)?"":type);
      }
      params.put("sortColumns", "Sequence ASC");
      params.put("status", 0);
      List<MGElectronic> listElectronic = gamePlatformService.queryMPT(params);
      request.setAttribute("listElectronic", listElectronic);
      request.setAttribute("type", type);
      request.setAttribute("gamename", gamename==null?"":gamename);
      GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
      if(gamePlatform == null){
    	  request.setAttribute("open", false);
      }else{
    	  request.setAttribute("open", true);
      }
      return "/mobile/ptmobile";
    }
    
    /**
     * SA游戏列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/game/samp")
    public String saList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "gamename", required = false) String gamename,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
      String vuid = CookieUtil.getOrCreateVuid(request, response);
      request.setAttribute("vuid", vuid);
      if(StringUtils.isEmpty(type)){
          type ="0";
      }
      Map<String, Object> params = new HashMap<String, Object>();
      if(StringUtils.isNotEmpty(gamename)){
      	gamename= URLDecoder.decode(gamename,"utf8");
      	params.put("gamename", gamename);
      }else{
      	params.put("categoryID", "0".equals(type)?"":type);
      }
      params.put("sortColumns", "Sequence ASC");
      params.put("status", 1);
      List<SAElectronic> listElectronic = gamePlatformService.querySAElec(params);
      request.setAttribute("listElectronic", listElectronic);
      request.setAttribute("type", type);
      request.setAttribute("gamename", gamename==null?"":gamename);
      GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.SA);
      if(gamePlatform == null){
    	  request.setAttribute("open", false);
      }else{
    	  request.setAttribute("open", true);
      }
      return "/mobile/samobile";
    }
    
    @RequestMapping(value = "/game/mgmp")
    public String mgList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "gamename", required = false) String gamename,
            HttpServletRequest request,HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        request.setAttribute("vuid", vuid);
        Map<String, Object> params = new HashMap<String, Object>();
        //type = StringUtils.isEmpty(type) ? "Classic Slot" : type;
       // params.put("category", type);
        params.put("sortColumns", "electronicid ASC");
        params.put("status", 1);
        params.put("gamename", gamename);
        params.put("clientID", 1002);
        List<AGElectronic> listElectronic = gamePlatformService.queryAGElectronic(params);
        request.setAttribute("listElectronic", listElectronic);
        request.setAttribute("type", type);
        GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
        if(gamePlatform == null){
      	  request.setAttribute("open", false);
        }else{
      	  request.setAttribute("open", true);
        }
        return "/mobile/mgmobile";
    }
    
	@RequestMapping(value = "/loginOutmp")
	public String signOut(HttpServletRequest request, HttpServletResponse response) {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		CookieUtil.removeCookie(request, response, "VUID");
		iRedisService.delete(vuid + "GAMEPORTAL_USER");
		logger.info("{"+vuid+"}退出成功。");
		return "redirect:/indexmp.html";
	}
	
	/**
	 * 进入手机活动页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/favomp")
	public String favo(HttpServletRequest request, HttpServletResponse response) {
		return "/mobile/favo/activity";
	}
}
