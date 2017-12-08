package com.gameportal.manage.handlerinterceptor;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.manage.listener.SystemFieldsCache;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.redis.service.IRedisService;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.system.service.ISystemService;
import com.gameportal.manage.util.CookieUtil;
import com.gameportal.manage.util.WebConstants;

/**
 * @ClassName: ManageHandlerInterceptor
 * @Description: TODO(后台管理拦截器)
 * @author shejia@gz-mstc.com
 * @date 2014-4-16 下午2:47:38
 */
public class ProxyManageHandlerInterceptor implements HandlerInterceptor {
	@Resource(name = "systemServiceImpl")
	private ISystemService iSystemService = null;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;

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
		// 请求处理之前进行调用
		// Map<String, String> map = iSystemService.qeuryAllFields();
		request.setAttribute("fields", SystemFieldsCache.fields);
		String url = request.getServletPath();
		// 1、请求到登录页面 放行
		if (url.startsWith("index.html")
				|| url.startsWith("/manage/activity/fileUpload.do")
				|| url.startsWith("/manage/weixin/fileupload.do")
				|| url.startsWith("/manage/website/fileupload.do")) {
			return true;
		}

		String vuid = CookieUtil.getOrCreateVuid(request, response);
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo memberInfo = (MemberInfo) iRedisService.getRedisResult(key,c);
		if (StringUtils.isNotBlank(ObjectUtils.toString(memberInfo))) {
			return true;
		} else {
			String requestedWith = request.getHeader("x-requested-with");
			// ajax请求
			if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {
				response.setHeader("session-status", "timeout");
				response.getWriter().print(WebConstants.TIME_OUT);
			} else {
				// 普通页面请求
				response.sendRedirect(request.getContextPath() + "/proxy/index.html");
			}
			// String action =
			// "<script type='text/javascript'>top.window.location.href ='"
			// + "http://"
			// + request.getServerName()
			// + ":"
			// + request.getServerPort() + "/index.html';</script>";
			// response.getWriter().print(action);
			return false;
		}

	}

}
