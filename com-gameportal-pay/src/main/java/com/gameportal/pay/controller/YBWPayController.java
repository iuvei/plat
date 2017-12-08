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

import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.YBPayment;
import com.gameportal.pay.util.SFMD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 银宝
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class YBWPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(YBWPayController.class);
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
	@RequestMapping("/ybw/{type}")
	@ResponseBody
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (userInfo != null) {
				if (hd != null && !"".equals(hd)) {
					String result = webValidate(userInfo, hd, request);
					if(result !=null){
						return result;
					}
                }
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				YBPayment mb = payPlatformService.ybPayment(userInfo, params, payPlat);
				response.setContentType("text/html;charset=GBK");
				response.setCharacterEncoding("GBK");
				PrintWriter writer = response.getWriter();
				redirect(mb, payPlat, writer);
			}
		}
		return null;
	}

	/**
	 * 微信支付回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ybw/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String partner = request.getParameter("partner");
			String ordernumber = request.getParameter("ordernumber");
			String orderstatus = request.getParameter("orderstatus"); //1:支付成功，非1为支付失败
			String paymoney = request.getParameter("paymoney");
			String sysnumber = request.getParameter("sysnumber");
			String sign = request.getParameter("sign");
			String srcMsg = String.format(
					"partner=%s&ordernumber=%s&orderstatus=%s&paymoney=%s",
					partner, ordernumber, orderstatus, paymoney);
			logger.info("银宝支付后台回调信息：" + srcMsg);
			PayOrder order = payPlatformService.queryPayOrderId(ordernumber, null, null, "0");
			if (orderstatus.equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("ok");
//					logger.info("订单已经支付成功，不能重复支付。");
					return;
				}
				// 2.验签
				PayPlatform payPlat = payPlatformService.queryPayPlatform(7L);
				if (sign.equalsIgnoreCase(SFMD5Util.encryption(srcMsg+payPlat.getPlatformkey()))) {
					if (order.getAmount().intValue() != Double.valueOf(paymoney).intValue()) {
						logger.info("银宝扫码订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(sysnumber);
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.ybPay(order);
					logger.info("银宝扫码充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					out.print("ok");
					return; 
				} else {
					logger.info("银宝扫码签名验证失败。");
					out.print("fail");
					return; 
				}
			}
		} catch (Exception e) {
			logger.error("银宝扫码支付后台回调失败。", e);
			out.print("fail");
			return; 
		}
		out.print("ok");
		return;
	}

	@RequestMapping("/ybw/view")
	public String view(String ordernumber, HttpServletRequest request, HttpServletResponse response) {
		PayOrder order = payPlatformService.queryPayOrderId(ordernumber, null, null, "0");
		request.setAttribute("order", order);
		if (order != null && order.getStatus() == 3) {
			return "/payment/payOk";
		}
		return "/payment/payFail";
	}
	
	private void redirect(YBPayment mb,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+payPlat.getDomainname() +"' method=\"GET\" id=\"frm7\"><br>";
		out +="<input name='partner' type='hidden' value='"+mb.getPartner()+"' /><br>";
		out +="<input name='banktype' type='hidden' value='"+mb.getBanktype()+"'/><br>";
		out +="<input name='paymoney' type='hidden' value='"+mb.getPaymoney()+"'/><br>";
		out +="<input name='ordernumber' type='hidden' value='"+mb.getOrdernumber()+"'/><br>";
		out +="<input name='callbackurl' type='hidden' value='"+mb.getCallbackurl()+"'/><br>";
		out +="<input name='hrefbackurl' type='hidden' value='"+mb.getHrefbackurl()+"' /><br>";
		out +="<input name='attach' type='hidden' value='abc' /><br>";
		out +="<input name='sign' type='hidden' value='"+mb.getSign()+"'/><br>";
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
}
