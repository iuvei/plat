package com.gameportal.web.home.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.model.UserBulletin;
import com.gameportal.web.adver.service.IBulletinService;
import com.gameportal.web.adver.service.IUserBulletinService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.util.ClientIP;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;

/**
 * 其他页面跳转控制器，例如：用户协议等等
 * @author Administrator
 *
 */
@Controller
public class OtherController{
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService;
    @Resource(name="bulletinServiceImpl")
    private IBulletinService bulletinService;
    @Resource(name="userBulletinService")
    private IUserBulletinService userBulletinService;
    /**
     * 用户协议
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/agreement")
    public String agreement(HttpServletRequest request, HttpServletResponse response) {
        return "/other/agreement";
    }

    /**
     * 关于钱宝娱乐
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/{hash}/about", method = RequestMethod.GET)
    public String about(@PathVariable String hash, HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("hash", hash);
        return "/other/about";
    }

    /**
     * 博彩责任
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/gaming")
    public String gaming(HttpServletRequest request, HttpServletResponse response){
        return "/other/gaming";
    }

    /**
     * 免责条款
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/disclaimer")
    public String disclaimer(HttpServletRequest request, HttpServletResponse response){
        return "/other/disclaimer";
    }

    /**
     * 联系我们
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/contact")
    public String contact(HttpServletRequest request, HttpServletResponse response){
        return "/other/contact";
    }

    /**
     * 隐私保护
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/privacy")
    public String privacy(HttpServletRequest request, HttpServletResponse response){
        return "/other/privacy";
    }

    /**
     * 新手帮助
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/help")
    public String help(HttpServletRequest request, HttpServletResponse response){
        return "/other/help";
    }

    /**
     * 阅读站内信。
     * @param bid
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/readMsg")
    public String readMsg(Long bid,HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", bid);
        List<Bulletin> bulletins =bulletinService.queryAllBulletin(map);
        Bulletin curBulletin = bulletins.get(0);
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        UserInfo loginUser = (UserInfo) iRedisService.getRedisResult(key, c);
        if(loginUser !=null){
            map.put("userid", loginUser.getUiid());
            List<UserBulletin> userBulletins =userBulletinService.getAll(map);
            UserBulletin userBulletin =null;
            if(CollectionUtils.isEmpty(userBulletins)){
                userBulletin = new UserBulletin(); // 新增
                userBulletin.setBid(String.valueOf(curBulletin.getId()));
                userBulletin.setUserid(loginUser.getUiid());
                userBulletinService.save(userBulletin);
            }else{
                userBulletin = userBulletins.get(0); //修改
                if (!userBulletinService.isRead(curBulletin,userBulletin)) {
                    userBulletin.setBid(userBulletin.getBid()+","+curBulletin.getId());
                    userBulletinService.update(userBulletin);
                }
            }
        }
        request.setAttribute("curBulletin", curBulletin);
        return "/other/readMsg";
    }


    /**
     * 网站公告
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/notice")
    public String notice(HttpServletRequest request, HttpServletResponse response){
        //		String vuid = CookieUtil.getOrCreateVuid(request, response);
        //		String key = vuid + "GAMEPORTAL_USER";
        //		Class<Object> c = null;
        //		UserInfo loginUser = (UserInfo) iRedisService.getRedisResult(key, c);
        //		if(loginUser ==null){
        //			return "redirect:/index.do";
        //		}
        return "/other/notice";
    }

    /**
     * VIP介绍
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/vip")
    public String vip(HttpServletRequest request, HttpServletResponse response){
        return "/vip/index";
    }


    /**
     * 代理专区
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/agent")
    public String agent(HttpServletRequest request, HttpServletResponse response){
        return "/agent/index";
    }

    @RequestMapping(value="/checkClient")
    public String checkClient(HttpServletRequest request, HttpServletResponse response){
        String date_str = DateUtil.getStrByDate(new Date(), "yyyy年MM月dd日 HH:mm:ss");
        String client_ip = ClientIP.getInstance().getIpAddr(request);
        request.setAttribute("clientIp", client_ip);
        request.setAttribute("date", date_str);
        return "/common/check";
    }
}
