package com.gameportal.web.handlerinterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.CookieUtil;

/**
 * @ClassName: MobileHandlerInterceptor
 * @Description: TODO(手机端拦截器)
 * @date 2014-4-16 下午2:47:38
 */
public class MobileHandlerInterceptor implements HandlerInterceptor {
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;

    @Override
    public void afterCompletion(HttpServletRequest arg0,
            HttpServletResponse arg1, Object arg2, Exception arg3)
                    throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
            Object arg2, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object arg2) throws Exception {
        String url = request.getServletPath();
        
        System.out.println("url:"+url);
        // 1、请求到登录页面 放行
        if (url.startsWith("indexmp.html")) {
            return true;
        }

        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        UserInfo systemUser = (UserInfo) iRedisService.getRedisResult(key, c);
        if (StringUtils.isNotBlank(ObjectUtils.toString(systemUser))) {
            return true;
        } else {
            String requestedWith = request.getHeader("x-requested-with");
            // ajax请求
            if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {
                response.setHeader("session-status", "timeout");
                if(StringUtils.isNotBlank(ObjectUtils.toString(systemUser)) && !iRedisService.getStringFromRedis(systemUser.getAccount()+"_"+systemUser.getUiid()).equals(key) && url.startsWith("/manage/capital/gameTransfer")){
                	// 普通页面请求
//                    response.getWriter().print("<script type='text/javascript'>window.location.href = '"+request.getContextPath() + "/loginmp.html'</script>");
                	response.sendRedirect("/loginmp.html");
                }else{
                	response.getWriter().print("{\"error\":true,\"msg\":\"登录超时,请重新登录！\"}");
                }
            } else {
                // 普通页面请求
//                response.getWriter().print("<script type='text/javascript'>window.location.href = '"+request.getContextPath() + "/loginmp.do'</script>");
            	response.sendRedirect("/loginmp.html");
            }
            return false;
        }
    }
}
