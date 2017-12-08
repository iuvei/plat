package com.gameportal.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.RFPayment;
import com.gameportal.pay.util.DigestUtils;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 锐付wap支付宝扫码。
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class RfZFBWapPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(RfZFBWapPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 锐付wap支付。
	 * 
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/rfzfbwap/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,@RequestParam(value = "requestHost") String requestHost,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			if (userInfo != null) {
				if (hd != null && !"".equals(hd)) {
					wapValidate(userInfo, hd, requestHost, request, response);
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("requestHost", requestHost);
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				RFPayment pay = payPlatformService.rfPayment(userInfo, params, payPlat);
				logger.info("WAP锐付支付宝请求参数："+JSON.toJSONString(pay));
				try {
					redirect(pay, payPlat, writer);
					writer.flush();
					response.flushBuffer();
					writer.close();
				} catch (Exception e) {
					logger.error("锐付预支付异常：",e);
					response.addHeader("refresh", "5;url=http://" + requestHost);
					writer.write("发生错误，即将返回...");
					writer.flush();
					response.flushBuffer();
					writer.close();
					return;
				}
			}
		}
	}

	private void redirect(RFPayment pay,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+payPlat.getDomainname() +"' method=\"post\" id=\"frm7\"><br>";
		out +="<input type=\"hidden\" name=\"partyId\" id=\"partyId\" value='"+pay.getPartyId()+"'/><br />";
		out +="<input type=\"hidden\" name=\"accountId\" id=\"accountId\" value='"+pay.getAccountId()+"'/><br />";
		out +="<input type=\"hidden\" name=\"appType\" id=\"appType\" value='"+pay.getAppType()+"'/><br />";
		out +="<input type=\"hidden\" name=\"orderNo\" id=\"orderNo\" value='"+pay.getOrderNo()+"'/><br />";
		out +="<input type=\"hidden\" name=\"orderAmount\" id=\"orderAmount\" value='"+pay.getOrderAmount()+"'/><br />";
		out +="<input type=\"hidden\" name=\"goods\" id=\"goods\" value='"+pay.getGoods()+"'/><br />";
		out +="<input type=\"hidden\" name=\"returnUrl\" id=\"returnUrl\" value='"+pay.getReturnUrl()+"'/><br />";
		out +="<input type=\"hidden\" name=\"cardType\" id=\"cardType\" value='"+pay.getCardType()+"'/><br />";
		out +="<input type=\"hidden\" name=\"refCode\" id=\"refCode\" value='"+pay.getRefCode()+"'/><br />";
		out +="<input type=\"hidden\" name=\"bank\" id=\"bank\" value='"+pay.getBank()+"'/><br />";
		out +="<input type=\"hidden\" name=\"encodeType\" id=\"encodeType\" value='"+pay.getEncodeType()+"'/><br />";
		out +="<input type=\"hidden\" name=\"signMD5\" id=\"signMD5\" value='"+pay.getSignMD5()+"'/><br />";
	    out +="<script language=\"javascript\">";
		out +="document.getElementById(\"frm7\").submit();";
		out +="</script>";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		writer.write(out);
	}

	/**
	 * 支付回调接口
	 * 
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rfzfbwap/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PrintWriter out = response.getWriter();
		try {
			// 返回参数
			StringBuilder origSign = new StringBuilder("orderNo").append(request.getParameter("orderNo"));
			origSign.append("appType").append(request.getParameter("appType"));
			origSign.append("orderAmount").append(request.getParameter("orderAmount"));
			origSign.append("succ").append(request.getParameter("succ"));
			origSign.append("encodeType").append(request.getParameter("encodeType"));
			// 签名
			String sign = request.getParameter("signMD5");
			logger.info("锐付后台回调信息：" + origSign.toString() + ",返回信息:" + request.getParameter("succ")+",签名："+sign);
			if (request.getParameter("succ").equals("Y")) {
				// 1.判断订单是否已经支付成功。
				PayOrder order = payPlatformService.queryPayOrderId(request.getParameter("orderNo").substring(4), null, null,
						"0");
				if (order == null) {
					logger.info("订单号不存在。");
					out.write("checkfail");
				} else if (order.getStatus() == 3) {
					out.write("checkok");
				} else {
					// 2.验签
					PayPlatform payPlat = payPlatformService.queryPayPlatform(41L);
					String signStr = origSign.append(payPlat.getPlatformkey())
							.toString();
					if (DigestUtils.md5DigestAsHex(signStr.getBytes("UTF-8")).equalsIgnoreCase(sign)) {
						if (!order.getAmount().toString().equals(request.getParameter("orderAmount"))) {
							logger.info("订单号[" + order.getPlatformorders() + "]金额不匹配。");
							out.write("checkfail");
						} else {
							// 3.修改订单状态
							order.setUpdateDate(new Date());
							Timestamp date = new Timestamp(new Date().getTime());
							order.setUpdateDate(date);
							order.setRemarks(request.getParameter("tradeNo"));
							order.setPaystatus(2);
							order.setStatus(3);

							// 4.用户上分
							payPlatformService.rfPay(order);
							logger.info(
									"充值加款成功->平台订单编号：" + order.getPlatformorders() + "->第三方订单编号：" + order.getRemarks()+"金额："+order.getAmount());
							out.write("checkok");
						}
					} else {
						logger.info("锐付签名验证失败。");
						out.write("checkfail");
					}
				}
			}
		} catch (Exception e) {
			logger.error("锐付后台回调失败。" + e.getMessage());
			out.write("checkfail");
		}
	}
}
