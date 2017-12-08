package com.gameportal.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CookieUtil {
	private static final Logger log = Logger.getLogger(CookieUtil.class);

	/**
	 * 存值进Cookie带VUID的值
	 * 
	 * @param key
	 * @param value
	 * @return true设置成功， false设置失败
	 */
	public static boolean setCookies(HttpServletResponse response, String key,
			String value) {
		try {
			Cookie cookie = new Cookie(key, value);
			cookie.setPath("/");
			cookie.setMaxAge(-1);// 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
			response.addCookie(cookie);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * key来得到Cookie
	 * 
	 * @param key
	 * @return Cookie
	 */
	public static Cookie getCookieViaValue(String key,
			HttpServletRequest request) {
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals(key)) {
						return cookies[i];
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * key 清除Cookie
	 * 
	 * @param key
	 *            return true设置成功， false设置失败
	 */
	public static boolean clearCookie(String key, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie cookie = getCookieViaValue(key, request);
			if (cookie != null) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setValue(null);
				response.addCookie(cookie);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 得到客户端IP
	 * 
	 * @return String
	 */
	public static String getIpAddr(HttpServletRequest request,
			HttpServletResponse response) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(0, ip.indexOf(","));
		}
		// System.out.println("[getIpAddr() ---- 订购次数限制作为key值ip为---]" + ip);
		return ip;
	}

	/**
	 * 根据key获取cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request,
			HttpServletResponse response, String key) {
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(key)) {
						return cookie;
					}
				}
			}
			try {
				Cookie keyInRequest = (Cookie) request.getAttribute(key);
				return keyInRequest;
			} catch (Exception e) {
			}
			return null;
		} catch (Exception e) {
			log.error("[getCookie() ---- Exception return " + e.getMessage()
					+ "]");
			return null;
		}
	}

	/**
	 * 根据key获取cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,
			HttpServletResponse response, String key) {
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(key)) {
						return cookie.getValue();
					}
				}
			}
			return null;
		} catch (Exception e) {
			log.error("[getCookie() ---- Exception return " + e.getMessage()
					+ "]");
			return null;
		}
	}

	/**
	 * 设置cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 * @return true设置成功， false设置失败
	 */
	public synchronized static boolean setCookie(HttpServletRequest request,
			HttpServletResponse response, String key, String value) {
		try {
			Cookie cookie = new Cookie(key, value);
			cookie.setPath("/");
			cookie.setMaxAge(-1);// 不设置的话，则cookies不写入硬盘,而是写在内存,只在当前页面有用,以秒为单位
			response.addCookie(cookie);
			request.setAttribute(key, cookie);
			return true;
		} catch (Exception e) {
			log.error("[setCookie() ---- Exception return " + e.getMessage()
					+ "]");
			return false;
		}
	}

	/**
	 * 从cookie中获取用户的vuid,如果vuid为null,返回为空,key为"VUID"
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getDefaultVuid(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie cookie = getCookie(request, response, "VUID");
			return null == cookie ? null : cookie.getValue();
		} catch (Exception e) {
			log.error("[getDefaultVUID() ---- Exception return "
					+ e.getMessage() + "]");
			return null;
		}
	}

	/**
	 * 获取vuid,如果为空，则新生成一个
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public synchronized static String getOrCreateVuid(
			HttpServletRequest request, HttpServletResponse response) {
		String vuid = request.getSession(true).getId();
		//String vuid = getDefaultVuid(request, response);
//		if (vuid == null || "".equals(vuid)) {
//			vuid = VuidUtil.createVuid();
//			setCookie(request, response, "VUID", vuid);
//		}
		return vuid;
	}

	public static void setCookieLogin(HttpServletRequest request,
			HttpServletResponse response, String value, String cookieName) {
		try {
			value = URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		Cookie c = getCookie(request, response, cookieName);
		if (c == null) {
			c = new Cookie(cookieName, value);
			c.setPath("/");
			c.setMaxAge(-1);
			response.addCookie(c);
		} else {
			if (!c.getValue().equals(value)) {
				c.setValue(value);
				c.setPath("/");
				c.setMaxAge(-1);
				response.addCookie(c);
			}
		}
	}

	public static String getCookieLoginURL(HttpServletRequest request,
			HttpServletResponse response, String Key) {
		Cookie c = getCookie(request, response, Key);
		if (c == null)
			return null;
		try {
			return URLDecoder.decode(c.getValue(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String key) {
		try {
			Cookie cookie = getCookie(request, response, key);
			if (cookie != null) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				cookie.setValue(null);
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
