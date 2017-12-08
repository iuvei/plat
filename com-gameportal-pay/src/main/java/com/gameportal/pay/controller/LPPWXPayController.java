package com.gameportal.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.model.LePayment;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.ExcuteRequestUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * LePay 微信
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class LPPWXPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(LPPWXPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 微信预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/lppwx/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
	     request.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html;charset=GBK");
         response.setCharacterEncoding("GBK");
         PrintWriter writer = response.getWriter();
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (userInfo != null) {
				if (hd != null && !"".equals(hd)) {
            		wapValidate(userInfo, hd, requestHost, request, response);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				LePayment lp = payPlatformService.lePayment(userInfo, params, payPlat, null);

				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("version", lp.getVersion());
				requestMap.put("encoding", lp.getEncoding());
				requestMap.put("signature", lp.getSignature());
//				requestMap.put("reqReserved", lp.getReqReserved());
				requestMap.put("mchId", lp.getMchId());
				requestMap.put("cmpAppId", lp.getCmpAppId());
				requestMap.put("payTypeCode", lp.getPayTypeCode());
				requestMap.put("outTradeNo", lp.getOutTradeNo());
				requestMap.put("tradeTime", lp.getTradeTime());
				requestMap.put("amount", lp.getAmount());
				requestMap.put("summary", lp.getSummary());
				requestMap.put("summaryDetail", lp.getSummary());
				requestMap.put("deviceIp", lp.getDeviceIp());
				requestMap.put("returnUrl", lp.getReturnUrl());

				System.out.println("lepay微信请求参数：" + requestMap.toString());
				try {
					LePayment.init();
					Map<String, Object> responseMap = new ExcuteRequestUtil(payPlat.getDomainname(), "/order/add").execute(requestMap);
					//response.sendRedirect(responseMap.get("webOrderInfo").toString());
					System.out.println(">>>>>>>>>>>>"+requestMap);
					JSONObject json = JSONObject.parseObject(responseMap.get("reponseData").toString());
					redirect(lp, json.get("qrPath")+"", writer);
					//Map<String, Object> responseMap = ExcuteRequestUtil.excute(payPlat.getDomainname(), "/order/add",
					//		requestMap);
					//response.sendRedirect(responseMap.get("webOrderInfo").toString());
					//response.addHeader("refresh", "0;url=" + responseMap.get("webOrderInfo").toString());
					writer.flush();
					response.flushBuffer();
					writer.close();
				} catch (Exception e) {
					e.getMessage();
					logger.error("lepay wab微信提交失败。", e);
				}
			}
		}
	}
	
	private void redirect(LePayment lp, String code_img_url, PrintWriter writer) {
		String out = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		out += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out += "<head>";
		out += "<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out += "<title>微信扫码支付</title>";
		out += "<link href=\"/css/zf_style.css\" type=\"text/css\" rel=\"stylesheet\" />";
		out += "<link href=\"/css/wechat_pay.css\" rel=\"stylesheet\" />";
		out += "<script type=\"text/javascript\" src=\"/js/jquery-1.8.0.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/jquery.qrcode.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/utf.js\"></script>";
		out += "</head>";
		out += "<div class=\"body\">";
		out += "<h1 class=\"mod-title\">";
		out += "<span class=\"ico-wechat\"></span><span class=\"text\">微信支付</span>";
		out += "</h1>";
		out += "<div class=\"mod-ct\">";
		out += "<div class=\"order\">";
		out += "</div>";
		out += "<div class=\"amount\">";
		out += "<span>￥</span>" + (Double.valueOf(lp.getAmount()) / 100) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src=\"\" width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + lp.getOutTradeNo() + "</dd>";
		out += "</dl>";
		out += "</div>";
		out += "<div class=\"tip\">";
		out += "	<span class=\"dec dec-left\"></span>";
		out += "	<span class=\"dec dec-right\"></span>";
		out += "	<div class=\"ico-scan\">";
		out += "	</div>";
		out += "	<div class=\"tip-text\">";
		out += "	<p style=\"color:red;\">";
		out += "		请使用另一部手机登录微信扫一扫";
		out += "	</p>";
		out += "<p>";
		out += "	扫描二维码完成支付";
		out += "</p>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"foot\">";
		out += "	<div class=\"inner\">";
		out += "	<p> ";
		out += "Copyright @ 工业和信息化部备案号: 黔ICP备16006408号-1 增值电信： 黔B2-20160068";
		out += "</p>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "</body>";
		out += "</html>";
		out += "<script>";
		out += "$(function(){";
		out += "sQrcode('" + code_img_url + "');";
		out += "});";
		out += "function sQrcode(qdata){";
		out += "	$(\"#showqrcode\").empty().qrcode({";
		out += "			render : \"canvas\",    			";
		out += "			text : qdata,    				";
		out += "			width : \"200\",              	";
		out += "			height : \"200\",             	";
		out += "			background : \"#ffffff\",     	";
		out += "			foreground : \"#000000\",  ";
		out += "			src: \"\"";
		out += "});	";
		out += "}";
		out += "</script>";
		writer.write(out);
		writer.flush();
		writer.close();
	}

	@RequestMapping("/lppwx/view")
	@ResponseBody
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("扫码支付成功，请稍候查看您的钱包余额。");
			writer.flush();
		} catch (Exception e) {
			logger.error("LePay web微信支付同步回调失败。", e);
		}
	}
}
