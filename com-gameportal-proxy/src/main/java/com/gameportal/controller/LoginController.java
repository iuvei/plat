package com.gameportal.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.comms.Tools;
import com.gameportal.comms.WebConst;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.PageData;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.service.IMemberInfoService;
import com.gameportal.util.Blowfish;
import com.gameportal.util.CookieUtil;
import com.gameportal.util.WebConstants;

import net.sf.json.JSONObject;

/**
 * 
 * @author brooke
 *
 */
@Controller
@RequestMapping(value="/login")
public class LoginController extends BaseAction{
	
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	
	@Resource(name = "memberInfoServiceImpl")
	private IMemberInfoService memberInfoService = null;
	/**
	 * 访问登录页
	 * @return
	 */
	@RequestMapping(value="/view")
	public ModelAndView toLogin()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("SYSNAME", Tools.readTxtFile(WebConst.SYSNAME)); //读取系统名称
		mv.setViewName("system/admin/login");
		mv.addObject("pd",pd);
		return mv;
	}
	
	/**
	 * 验证用户登录
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	@RequestMapping(value="/reque")
	@ResponseBody
	public Object login(
			@RequestParam(value = "username", required = false) String account,
			@RequestParam(value = "password", required = false) String passwd,
			@RequestParam(value = "validationCode", required = false) String code,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		JSONObject json = new JSONObject();
		try {
			String cacheCode = (String)getSession().getAttribute(WebConst.SESSION_SECURITY_CODE);
			if(cacheCode == null || "".equals(cacheCode)){
				json.put("result", "flushValicode");
				json.put("msg", "请刷新验证码重新输入!");
				return json.toString();
			}
			if (!cacheCode.equals(code)) {//校验验证码是否正确
				json.put("result", "valicodearror");
				json.put("msg", "验证码输入错误!");
				return json.toString();
			}
			if (StringUtils.isBlank(account)) {
				json.put("result", "unameemtry");
				json.put("msg", "请输入您的登录账号!");
				return json.toString();
			}
			if (StringUtils.isBlank(passwd)) {
				json.put("result", "pwdemtry");
				json.put("msg", "请输入您的登录密码!");
				return json.toString();
			}
			Map<String,  Object> params = new HashMap<String, Object>();
			params.put("account", account.trim());
			MemberInfo member =memberInfoService.queryMemberInfo(params);
			if(null == member){
				json.put("result", "unamenotfound");
				json.put("msg", "您输入的账号不存在!");
				return json.toString();
			}
			Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
			if (!bf.decryptString(member.getPasswd()).equals(passwd.trim())) {
				json.put("result", "pwderror");
				json.put("msg", "您的登录密码输入有误!");
				return json.toString();
			}
			if(member.getStatus() == 0){
				json.put("result", "uaccountlock");
				json.put("msg", "您的账号已被禁用!");
				return json.toString();
			}
			if(member.getAccounttype() == 0){
				json.put("result", "uaccounterror");
				json.put("msg", "您的用户账号或密码错误!");
				return json.toString();
			}
			if(StringUtils.isEmpty(member.getUname())){
				json.put("result", "unamerror");
				json.put("msg", "您的代理账号姓名不能为空");
				return json.toString();
			}
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
			CookieUtil.setCookie(request, response, "PROXY_USERNAME",
					URLEncoder.encode(member.getUname(), "UTF-8"));
			iRedisService.addToRedis(key, member);//保持1小时
			getSession().setAttribute("loginUser",member);
			json.put("result", "logsuccess");
			json.put("msg", "登录成功");
			return json.toString();
		}catch (Exception e) {
			logger.error("Exception: ", e);
			json.put("result", "error");
			json.put("msg", "网络异常，请稍后再试！");
			return json.toString();
		}
	}
	
	/**
	 * 注销。
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String vuid = CookieUtil.getOrCreateVuid(request,response);
		String key = vuid + WebConstants.FRAMEWORK_PROXY_USER;
		Class<Object> c = null;
		MemberInfo member = (MemberInfo) iRedisService.getRedisResult(key, c);
		if(member !=null){
			iRedisService.delete(key);
		}
		getSession().removeAttribute("loginUser");
		response.sendRedirect(request.getContextPath()+"/");
		return null;
	}
	
	/**
	 * 进入tab标签
	 * @return
	 */
	@RequestMapping(value="/tab")
	public String tab(){
		return "system/admin/tab";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 */
	@RequestMapping(value="/default")
	public String defaultPage(){
		return "system/admin/default";
	}
	
}
