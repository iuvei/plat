package com.gameportal.web.home.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.util.VerifyCode;
import com.gameportal.web.util.WebConst;
import com.google.code.kaptcha.Producer;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/validationCode")
public class ValidationCodeController{
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "captchaProducer")
    private Producer captchaProducer = null;
	
	 private int width = 90;//验证码宽度
	    private int height = 40;//验证码高度
	    private int codeCount = 4;//验证码个数
	    private int lineCount = 2;//混淆线个数

	    char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public ValidationCodeController() {
		super();
	}

	@RequestMapping("/pcrimg")
	public void crimg(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		setResponseHeaders(response);
        session.setAttribute(WebConst.TOKEN_CODE,VerifyCode.getImageCode(response));
	}
	
	/**
	 * 代理验证码。
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/agentcode")
	public void agentcode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		setResponseHeaders(response);
        session.setAttribute(WebConst.AGENT_TOKEN_CODE,VerifyCode.getImageCode(response));
	}
	
	/**
	 * 二维码验证码。
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/qrcode")
	public void qrcode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		setResponseHeaders(response);
        session.setAttribute(WebConst.QR_TOKEN_CODE, VerifyCode.getImageCode(response));  	
       
	}
	
	/**
	 * 微信二维码验证码。
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/wxcode")
	public void wxcode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		setResponseHeaders(response);
        session.setAttribute(WebConst.WX_TOKEN_CODE, VerifyCode.getImageCode(response));  	
      
	}
	
	protected void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

	@RequestMapping(value = "/validform")
	public @ResponseBody
	String validform(
			@RequestParam(value = "param", required = false) String code,
			@RequestParam(value = "isLogin", required = false) boolean isLogin,
			HttpServletRequest request, HttpServletResponse response) {
		String cacheCode;
		if(isLogin){
			cacheCode = (String)request.getSession().getAttribute(WebConst.TOKEN_CODE);
		}else{
			cacheCode = (String)request.getSession().getAttribute(WebConst.AGENT_TOKEN_CODE);
		}
		JSONObject json = new JSONObject();
		if (cacheCode.equals(code.toUpperCase())) {
			json.put("status", "y");
			json.put("info", "验证通过！");
		} else {
			json.put("status", "n");
			json.put("info", "验证码错误！");
		}
		return json.toString();
	}
}
