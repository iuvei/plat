package com.gameportal.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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
import com.gameportal.pay.model.MoBoPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 手机移动扫码支付
 * @author add by sum 2016.09.06
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class MBPPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(MBPPayController.class);
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
	@RequestMapping("/mbp/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost,
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
				params.put("customerIP", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				MoBoPayment mb = payPlatformService.mbPayment(userInfo, params, payPlat);
				logger.info("手机摩宝请求参数>>>>:"+JSON.toJSONString(mb));
				try {
					redirect(mb, payPlat, writer);
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
	
	private void redirect(MoBoPayment mb,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+payPlat.getDomainname() +"' method=\"post\" id=\"frm7\"><br>";
		out +="<input name='apiName' type='hidden' value='"+mb.getApiName()+"' /><br>";
		out +="<input name='apiVersion' type='hidden' value='"+mb.getApiVersion()+"'/><br>";
		out +="<input name='platformID' type='hidden' value='"+mb.getPlatformID()+"'/><br>";
		out +="<input name='merchNo' type='hidden' value='"+mb.getMerchNo()+"'/><br>";
		out +="<input name='orderNo' type='hidden' value='"+mb.getOrderNo()+"'/><br>";
		out +="<input name='tradeDate' type='hidden' value='"+mb.getTradeDate()+"' /><br>";
		out +="<input name='amt' type='hidden' value='"+mb.getAmt()+"' /><br>";
		out +="<input name='merchUrl' type='hidden' value='"+mb.getMerchUrl()+"'/><br>";
		out +="<input name='tradeSummary' type='hidden' value='"+mb.getTradeSummary()+"'/><br>";
		out +="<input name='merchParam' type='hidden' value='"+mb.getMerchParam()+"'/><br>";
		out +="<input name='choosePayType' type='hidden' value='"+mb.getChoosePayType()+"'/><br>";
		out +="<input name='signMsg' type='hidden' value='"+mb.getSignMsg()+"'/><br>";
		out +="<script language=\"javascript\">";
		out +="document.getElementById(\"frm7\").submit();";
		out +="</script>";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		System.out.println(">>>>>>>>>>"+out);
		writer.write(out);
	}

	/**
	 * 微信支付回调接口
	 * 
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mbp/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PrintWriter out = response.getWriter();
		try {
			String apiName = request.getParameter("apiName");
			String notifyTime = request.getParameter("notifyTime");
			String tradeAmt = request.getParameter("tradeAmt");
			String merchNo = request.getParameter("merchNo");
			String merchParam = request.getParameter("merchParam");
			String orderNo = request.getParameter("orderNo");
			String tradeDate = request.getParameter("tradeDate");
			String accNo = request.getParameter("accNo");
			String accDate = request.getParameter("accDate");
			String orderStatus = request.getParameter("orderStatus"); // 0 未支付，1 成功，2失败
			String signMsg = request.getParameter("signMsg").replaceAll(" ", "\\+");
			String notifyType = request.getParameter("notifyType");
			String srcMsg = String.format(
					"apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
					apiName, notifyTime, tradeAmt, merchNo, merchParam, orderNo, tradeDate, accNo, accDate,
					orderStatus);
			logger.info("手机摩宝支付后台回调信息：" + srcMsg+" notifyType="+notifyType);
			PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
			if (notifyType.equals("0")) { // 0-页面跳转通知，不处理业务 1-服务器异步通知
				view(order.getPlatformorders(), merchParam, request, response);
				return;
			}
			if (orderStatus.equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("支付订单不存在!即將返回...");
					out.write("FAIL");
					out.flush();
					out.close();
					return;
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("订单已经支付成功!即將返回...");
					out.write("SUCCESS");
					out.flush();
					out.close();
					return;
				}else{
					// 2.验签
					PayPlatform payPlat = payPlatformService.queryPayPlatform(21L);
					if (signMsg.equalsIgnoreCase(MD5Util.signByMD5(srcMsg, payPlat.getPlatformkey()))) {
						DecimalFormat numberFormat = new DecimalFormat("#.00");
						if (!numberFormat.format(order.getAmount()).equals(tradeAmt)) {
							logger.info("摩宝扫码订单金额不匹配。");
							response.addHeader("refresh", "3;url=http://" + merchParam);
							out.write("订单金额不匹配!即將返回...");
							out.write("FAIL");
							out.flush();
							out.close();
							return;
						}
						// 3.修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(accNo);
						order.setPaystatus(2);
						order.setStatus(3);
						
						// 4.用户上分
						payPlatformService.mbPay(order);
						logger.info("WAP摩宝充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
						response.addHeader("refresh", "3;url=http://" + merchParam);
						out.write("充值成功!即將返回...");
						out.write("SUCCESS");
						out.flush();
						out.close();
						return;
					} else {
						response.addHeader("refresh", "3;url=http://" + merchParam);
						out.write("签名验证失败!即將返回...");
						out.write("FAIL");
						out.flush();
						out.close();
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.error("摩宝扫码支付后台回调失败。", e);
			response.addHeader("refresh", "3;url=http://" + request.getParameter("merchParam"));
			out.write("第三方后台回调失败!即將返回...");
			out.write("FAIL");
			out.flush();
			out.close();
			return;
		}
	}

	public void view(String billno,String backUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PayOrder order = payPlatformService.queryPayOrderId(billno, null, null, "0");
		request.setAttribute("order", order);
		PrintWriter writer = response.getWriter();
		if (order != null && order.getStatus() == 3) {
			response.addHeader("refresh", "3;url=http://" + backUrl);
			writer.write("充值成功!即將返回...");
			writer.flush();
			writer.close();
		}
		response.addHeader("refresh", "3;url=http://" + backUrl);
		writer.write("充值失败!即將返回...");
		writer.flush();
		writer.close();
	}
}
