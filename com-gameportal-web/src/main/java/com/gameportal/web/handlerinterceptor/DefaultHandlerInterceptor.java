package com.gameportal.web.handlerinterceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.model.UserBulletin;
import com.gameportal.web.adver.service.IBulletinService;
import com.gameportal.web.adver.service.IUserBulletinService;
import com.gameportal.web.agent.model.ProxyWebSitePv;
import com.gameportal.web.agent.service.IProxyWebSitePvService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;

/**
 * 默认拦截器
 * @author Administrator
 *
 */
public class DefaultHandlerInterceptor implements HandlerInterceptor{
    private static final Logger logger = Logger.getLogger(DefaultHandlerInterceptor.class);

    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    @Resource(name="bulletinServiceImpl")
    private IBulletinService bulletinService;
    @Resource(name="userBulletinService")
    private IUserBulletinService userBulletinService;
    @Resource(name="proxyWebSitePvService")
    private IProxyWebSitePvService proxyWebSitePvService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        UserInfo systemUser = (UserInfo) iRedisService.getRedisResult(key, c);
        if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
            Long count = userInfoService.getAccountMoneyCount(
                    systemUser.getUiid(), 1);
            AccountMoney am=userInfoService.getAccountMoneyObj(systemUser.getUiid(), null);
            request.setAttribute("accountMoneyCount", am.getTotalamount().intValue());
            request.setAttribute("integral", am.getIntegral());
            request.setAttribute("moneyCount", count);
            request.setAttribute("userInfo", systemUser);
        }
        /**
         * 访问页面名称
         */
        /*String  path  =  request.getContextPath()+request.getServletPath();
			if(path.indexOf("pcrimg") < 0 && path.indexOf("agentcode") < 0){
				//页面后缀是.html
				String requestedWith = request.getHeader("x-requested-with");
				if(!StringUtils.isNotBlank(requestedWith)){
					String referer = request.getHeader("Referer");//获取请求来源
					if(StringUtils.isNotBlank(referer)){
						System.out.println("===============referer:"+referer);
						//非Ajax请求
						String refererParams = "";
						if(referer.indexOf("?") > 0){//验证源地址有参数
							refererParams = referer.substring(referer.indexOf("?"), referer.length());
						}
						if(StringUtils.isNotBlank(refererParams)){
							String  realPath  =  request.getScheme()+"://"+request.getServerName()+request.getContextPath()+request.getServletPath();
						    realPath +=refererParams;
							response.sendRedirect(realPath);
						}
					}
				}
			}*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        UserInfo loginUser = (UserInfo) iRedisService.getRedisResult(key, c);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", 1);
        List<Bulletin> bulletionList = bulletinService.queryAllBulletin(paramMap);
        if(loginUser != null){
            // 公告未读数
            Integer unReadCount = 0;
            paramMap.put("userid", loginUser.getUiid());
            List<UserBulletin> userBulletins = userBulletinService.getAll(paramMap);
            if (CollectionUtils.isEmpty(userBulletins)) {
                request.getSession().setAttribute("unReadCount",
                        CollectionUtils.isEmpty(bulletionList) == true ? "0" : bulletionList.size());
                request.getSession().removeAttribute("userBulletin");
            } else {
                UserBulletin uBulletin = userBulletins.get(0);
                for (Bulletin bt : bulletionList) {
                    if (uBulletin.getBid().indexOf(String.valueOf(bt.getId())) == -1) {
                        unReadCount++;
                    }
                }
                request.getSession().setAttribute("unReadCount", unReadCount);
                request.getSession().setAttribute("userBulletin", uBulletin);
            }
        }
        String url = request.getServletPath();
        if (url.startsWith("/index.html") || url.startsWith("/manage/index.html")
                || url.startsWith("/liveCasino.html") || url.startsWith("/electronic/index.html")
                || url.startsWith("/downPage.html") || url.startsWith("/favo.html") || url.startsWith("/forgetpwd.html")
                || url.startsWith("/electronic/agElectronic.html") || url.contains("/about.html")
                || url.startsWith("/gaming.html") || url.startsWith("/register.html") || url.startsWith("/contact.html")
                || url.startsWith("/disclaimer.html") || url.startsWith("/help.html")
                || url.startsWith("/privacy.html")) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("proxyurl", request.getServerName());
            List<Map<String, Object>> list = userInfoService.selectProxyUrl(map);
            if(CollectionUtils.isNotEmpty(list)){
                // logger.info("网站访问量统计。");
                map = new HashMap<String, Object>();
                Map<String, Object> proxyUrl = list.get(0);
                map.put("proxydomain", proxyUrl.get("proxyurl"));
                map.put("proxyid", proxyUrl.get("proxyuid"));
                map.put("createDate", DateUtil.FormatDate(new Date()));
                ProxyWebSitePv proxyWebSitePv = proxyWebSitePvService.getProxyWebSitePv(map);
                if(proxyWebSitePv == null){ // 本日首次访问
                    proxyWebSitePv = new ProxyWebSitePv();
                    proxyWebSitePv.setProxyid(Long.valueOf(proxyUrl.get("proxyuid").toString()));
                    proxyWebSitePv.setProxydomain(proxyUrl.get("proxyurl").toString());
                    proxyWebSitePv.setCreateDate(new Date());
                    proxyWebSitePv.setNumber(1);
                    proxyWebSitePvService.save(proxyWebSitePv);
                }else{ // 访问量+1
                    proxyWebSitePv.setNumber(proxyWebSitePv.getNumber()+1);
                    proxyWebSitePvService.update(proxyWebSitePv);
                }
            }
        }
        request.getSession().setAttribute("bulletionList", bulletionList);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
                    throws Exception {

    }
}
