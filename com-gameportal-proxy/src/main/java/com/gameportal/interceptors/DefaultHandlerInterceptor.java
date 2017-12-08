package com.gameportal.interceptors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.gameportal.domain.MemberInfo;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.util.CookieUtil;
import com.gameportal.util.WebConstants;


/**
 * 默认拦截器
 * @author brooke
 *
 */
public class DefaultHandlerInterceptor extends BaseInterceptor{

	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	/**
	 * preHandle()方法在业务处理器处理请求之前被调用 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo member = (MemberInfo) iRedisService.getRedisResult(key, c);
		if (member != null) {
			return true;
		} else {
			//登陆页面请求
			response.sendRedirect(request.getContextPath()+"/login/view.do");
			return false;
		}
	}

	/**
	 * postHandle()方法在业务处理器处理请求之后被调用  
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/**
	 * afterCompletion()方法在DispatcherServlet完全处理完请求后被调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
