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

import com.gameportal.pay.model.GstPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.KeyValue;
import com.gameportal.pay.util.KeyValues;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * WAP国盛通微信扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class GstPWXPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(GstPWXPayController.class);
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
	@RequestMapping("/gstpwx/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost", required = false) String requestHost,
			@RequestParam(value = "channel", required = false) String channel, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				GstPayment gst = payPlatformService.gstPayment(userInfo, params, payPlat,channel);
				gst.setCustomerIp(ClientUtills.getInstance().getIpAddr(request));
				gst.setReturnParams(requestHost);
				gst.setSign(gst.buildSignature(payPlat.getPlatformkey()));
				try {
					redirect(gst, payPlat, writer);
					writer.flush();
					response.flushBuffer();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
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
	
	private void redirect(GstPayment gst,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+payPlat.getDomainname() +"' method=\"GET\" id=\"frm7\"><br>";
		out +="<input name='input_charset' type='hidden' value='"+gst.getInputCharset()+"' /><br>";
		out +="<input name='inform_url' type='hidden' value='"+gst.getInformUrl()+"'/><br>";
		out +="<input name='return_url' type='hidden' value='"+gst.getReturnUrl()+"'/><br>";
		out +="<input name='pay_type' type='hidden' value='"+gst.getPayType()+"'/><br>";
		out +="<input name='bank_code' type='hidden' value='"+gst.getBankCode()+"'/><br>";
		out +="<input name='merchant_code' type='hidden' value='"+gst.getMerchantCode()+"' /><br>";
		out +="<input name='order_no' type='hidden' value='"+gst.getOrderNo()+"' /><br>";
		out +="<input name='order_amount' type='hidden' value='"+gst.getOrderAmount()+"'/><br>";
		out +="<input name='order_time' type='hidden' value='"+gst.getOrderTime()+"'/><br>";
		out +="<input name='req_referer' type='hidden' value='"+gst.getReqReferer()+"'/><br>";
		out +="<input name='customer_ip' type='hidden' value='"+gst.getCustomerIp()+"'/><br>";
		out +="<input name='return_params' type='hidden' value='"+gst.getReturnParams()+"'/><br>";
		out +="<input name='sign' type='hidden' value='"+gst.getSign()+"'/><br>";
		out +="<script language=\"javascript\">";
		out +="document.getElementById(\"frm7\").submit();";
		out +="</script>";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		writer.write(out);
		writer.flush();
		writer.close();
	}

	/**
	 * 微信支付回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/gstpwx/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String merchantCode = request.getParameter("merchant_code");
//	        String notifyType = request.getParameter("notify_type");
	        String orderNo = request.getParameter("order_no");
	        String orderAmount = request.getParameter("order_amount");
	        String orderTime = request.getParameter("order_time");
	        String returnParams = request.getParameter("return_params");
	        String tradeNo = request.getParameter("trade_no");
	        String tradeTime = request.getParameter("trade_time");
	        String tradeStatus = request.getParameter("trade_status");
	        String sign = request.getParameter("sign");
	        KeyValues kvs = new KeyValues();
	
	        kvs.add(new KeyValue("merchant_code", merchantCode));
//	        kvs.add(new KeyValue("notify_type", notifyType));
	        kvs.add(new KeyValue("order_no", orderNo));
	        kvs.add(new KeyValue("order_amount", orderAmount));
	        kvs.add(new KeyValue("order_time", orderTime));
	        kvs.add(new KeyValue("return_params", returnParams));
	        kvs.add(new KeyValue("trade_no", tradeNo));
	        kvs.add(new KeyValue("trade_time", tradeTime));
	        kvs.add(new KeyValue("trade_status", tradeStatus)); //success 交易成功 | failed 交易失败 | paying 交易中 
	     
			PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
			if (tradeStatus.equals("success")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					response.addHeader("refresh", "3;url=http://" + returnParams);
                    out.write("支付订单不存在!即將返回...");
                    out.write("fail");
                    out.flush();
                    out.close();
                    return;
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					response.addHeader("refresh", "3;url=http://" + returnParams);
					out.write("success");
					out.write("订单已经支付成功!即將返回...");
					out.flush();
					out.close();
					logger.info("国盛通支付宝后台回调信息：orderNo=" + orderNo + ",orderNo=" + orderNo + ",tradeStatus=" + tradeStatus);
					return;
				}
				// 2.验签
				PayPlatform payPlat = payPlatformService.queryPayPlatform(43L);
				String thizSign = kvs.sign(payPlat.getPlatformkey(), "UTF-8");
				if (thizSign.equalsIgnoreCase(sign)) {
					if (order.getAmount().intValue() != Double.valueOf(orderAmount).intValue()) {
						logger.info("国盛通支付宝扫码订单金额不匹配。");
						response.addHeader("refresh", "3;url=http://" + returnParams);
                        out.write("订单金额不匹配!即將返回...");
                        out.write("fail");
                        out.flush();
                        out.close();
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(tradeNo);
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.gstPay(order);
					logger.info("国盛通WAP支付宝扫码充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					
					response.addHeader("refresh", "3;url=http://" + returnParams);
					out.write("success");
					out.write("充值成功!即將返回...");
					out.flush();
					out.close();
					return;
				} else {
					logger.error("国盛通支付宝扫码签名验证失败。");
					response.addHeader("refresh", "3;url=http://" + returnParams);
					out.write("fail");
					out.write("签名验证失败!即將返回...");
					out.flush();
					out.close();
					return;
				}
			}
		} catch (Exception e) {
			logger.error("国盛通支付宝扫码后台回调失败。", e);
			response.addHeader("refresh", "3;url=http://" + request.getParameter("merchParam"));
			out.write("第三方后台回调失败!即將返回...");
			out.print("fail");
			out.flush();
			out.close();
			return;
		}
		return;
	}
}
