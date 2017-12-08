package com.gameportal.web.home.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserLoginLog;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.ClientIP;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.MobileLocationUtil;
import com.gameportal.web.util.WebConst;
import com.gameportal.web.util.ip.IPSeeker;

import net.sf.json.JSONObject;

@Controller
public class LoginController{
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    private static final Logger logger = Logger
            .getLogger(LoginController.class);

    public LoginController() {
        super();
    }

    @RequestMapping(value="/login")
    public String login(){
        return "home/main";
    }


    @RequestMapping(value = "/loginSubmit")
    public @ResponseBody
    String loginSubmit(
            @RequestParam(value = "username", required = false) String account,
            @RequestParam(value = "password", required = false) String passwd,
            @RequestParam(value = "validationCode", required = false) String code,
            @RequestParam(value = "rememberme", required = false) boolean rememberme,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String cacheCode = (String)request.getSession().getAttribute(WebConst.TOKEN_CODE);
        if(cacheCode == null || "".equals(cacheCode)){
            json.put("code", "4");
            json.put("info", "请刷新验证码重新输入。");
            return json.toString();
        }
        if (!cacheCode.equals(code)) {//校验验证码是否正确
            json.put("code", "4");
            json.put("info", "验证码输入错误，请检查后重新输入。");
            return json.toString();
        }
        try {
            Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
            UserInfo userInfo = userInfoService.queryUserInfo(account, null);
            if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
                if (userInfo.getStatus() == 0) {
                    json.put("code", "2");
                    json.put("info", "您的帐号已经禁用，请稍后联系客服!");
                } else {
                	System.out.println(userInfo.getPasswd());
                    if (bf.decryptString(userInfo.getPasswd()).equals(passwd.trim())) {
                        Date now = DateUtil.getDateByStr(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        String key = vuid + "GAMEPORTAL_USER";
                        String referer = request.getHeader("Referer");
                        if(!StringUtils.isNotBlank(referer)){
                            referer = ClientIP.getInstance().getURL(request);
                        }
                        userInfo.setLoginip(ClientIP.getInstance().getIpAddr(request));
                        userInfo.setLoginClient(request.getHeader("User-Agent"));
                        userInfo.setLoginurl(referer);
                        userInfo.setKey(key);
                        userInfo.setLogincount(userInfo.getLogincount()+1);
                        //userInfo.setUpdateDate(now);
                        iRedisService.addToRedis(key,userInfo);
                        UserInfo upUser = new UserInfo();
                        upUser.setUiid(userInfo.getUiid());
                        //upUser.setLoginip(ClientIP.getInstance().getIpAddr(request));
                        upUser.setLogincount(1);
                        upUser.setUpdateDate(now);
//                        boolean flag = true;
                        // 判断关联账号
//                        Map<String, Object> params = new HashMap<String, Object>();
//                        if(flag){
//                            params.put("uiid", userInfo.getUiid());
//                            String ips = "";
//                            List<String> ipList = userInfoService.selectUserLoginIps(params);
//                            if (CollectionUtils.isNotEmpty(ipList)) {
//                                for (String ip : ipList) {
//                                    if (ips.length() > 0) {
//                                        ips += ",";
//                                    }
//                                    ips += "'" + ip + "'";
//                                }
//                            }
//                            logger.info(">>>>>>>>>>>>>>>>>关联IP："+ips);
//                            params.put("loginips", ips);
//                            List<String> logs = userInfoService.selectUserLoginLog(params);
//                            String relaAccount = "";
//                            if (CollectionUtils.isNotEmpty(logs)) {
//                                for (String ul : logs) {
//                                    if (relaAccount.length() > 0) {
//                                        relaAccount += ",";
//                                    }
//                                    relaAccount += ul;
//                                }
//                            }
//                            logger.info(">>>>>>>>>>>>>>>>>关联账号："+relaAccount);
//                            if (StringUtils.isNotEmpty(relaAccount)) {
//                                upUser.setRelaflag(1);
//                                upUser.setRelaaccount(relaAccount); // 设置关联账号
//                            }
//                        }
                        if(StringUtils.isEmpty(userInfo.getIparea())){
                            upUser.setIparea(IPSeeker.getInstance().getAddress(userInfo.getLoginip()));
                        }
                        if(StringUtils.isEmpty(userInfo.getPhonearea())){
                            upUser.setPhonearea(MobileLocationUtil.getMobileLocation(userInfo.getPhone()));
                        }
                        if(StringUtils.isEmpty(userInfo.getRegdevice())){
                            upUser.setRegdevice(request.getHeader("User-Agent"));
                        }
                        if(userInfo.getStatus()==1){
                            upUser.setStatus(2);
                        }
                        userInfoService.updateLogin(upUser);
                        //添加登录日志
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
                        iRedisService.addToRedis(key,userInfo);
                        iRedisService.setObjectFromRedis(userInfo.getAccount()+"_"+userInfo.getUiid(), key);
                        json.put("code", "1");
                        json.put("info", "登录成功!");
                        if(rememberme){
                            Cookie cookie = new Cookie("username", account);
                            response.addCookie(cookie);
                        }else{
                            Cookie cookie = new Cookie("username", account);
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    } else {
                        json.put("code", "3");
                        json.put("info", "密码不正确!");
                    }
                }
            }else{
                json.put("code", "2");
                json.put("info", "您输入的账号不存在，请检查后重新输入。");
            }
        } catch (Exception e) {
            logger.error("登录异常", e);
            json.put("code", "5");
            json.put("info", "网络异常，请稍后再试！");
        }
        return json.toString();
    }

    @RequestMapping(value = "/signOut")
    public String signOut(HttpServletRequest request,
            HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        CookieUtil.removeCookie(request, response, "VUID");
        iRedisService.delete(vuid + "GAMEPORTAL_USER");
        return "redirect:/";
    }

    @RequestMapping(value = "/redsss")
    public String redsss(HttpServletRequest request,
            HttpServletResponse response) {
        List<String> redisList = iRedisService.getKeys("*GAMEPORTAL_USER");
        for(String str : redisList){
            System.out.println("用户登录key:"+str);
        }
        return null;
    }

    @RequestMapping(value = "/delrk")
    public String delrk(HttpServletRequest request,
            HttpServletResponse response) {
        List<String> redisList = iRedisService.getKeys("*GAMEPORTAL_USER");
        for(String str : redisList){
            iRedisService.delete(str);
        }
        return null;
    }

    /**
     * 下载专区。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/downClient")
    public String downloadPage(HttpServletRequest request,
            HttpServletResponse response){
        return "/home/downClient";
    }

    /**
     * 下载专区。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/downPage")
    public String gotoDownloadPage(HttpServletRequest request,
            HttpServletResponse response){
        return "/home/downPage";
    }

    public static void main(String[] args) {
        Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
        System.out.println(bf.decryptString("efc97d36b10b9d4bc180dc726a405b7bafc8ad67ede70526268315d355c03f7a"));
        System.out.println(bf.decryptString("66832070e2f53a478c24cc5bec4ba33fb6419078d0247a8b238f8f599710f157"));
    }
}
