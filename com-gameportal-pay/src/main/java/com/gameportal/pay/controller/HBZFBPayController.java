package com.gameportal.pay.controller;

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
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.ThPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.KeyValue;
import com.gameportal.pay.util.KeyValues;
import com.gameportal.pay.util.URLUtils;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * hebao支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class HBZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(HBZFBPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 支付宝预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/hebaozfb/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
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
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				params.put("referer", request.getServerName());
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				ThPayment pay = payPlatformService.hbPayment(userInfo, params, payPlat,null);
				
				StringBuilder sb = new StringBuilder();
			    sb.append(payPlat.getDomainname());
			    URLUtils.appendParam(sb, "input_charset", pay.getInputCharset(), false);
			    URLUtils.appendParam(sb, "notify_url", pay.getNotifyUrl(), "UTF-8");
			    URLUtils.appendParam(sb, "return_url", pay.getReturnUrl(), "UTF-8");
			    URLUtils.appendParam(sb, "pay_type", pay.getPayType());
			    URLUtils.appendParam(sb, "bank_code", pay.getBankCode());
			    URLUtils.appendParam(sb, "merchant_code", pay.getMerchantCode());
			    URLUtils.appendParam(sb, "order_no", pay.getOrderNo());
			    URLUtils.appendParam(sb, "order_amount", pay.getOrderAmount());
			    URLUtils.appendParam(sb, "order_time", pay.getOrderTime());
			    URLUtils.appendParam(sb, "req_referer", pay.getReqReferer(), "UTF-8");
			    URLUtils.appendParam(sb, "customer_ip", pay.getCustomerIp());
			    URLUtils.appendParam(sb, "sign", pay.getSign());
//				request.setAttribute("pay", pay);
//				request.setAttribute("submitUrl", payPlat.getDomainname());
//				return "/payment/thJump";
			    response.sendRedirect(sb.toString());
			}
		}
		return null;
	}
	

	/**
	 * hebao支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/hebaozfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest req, HttpServletResponse response) throws Exception {
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			 String merchantCode = req.getParameter("merchant_code");
			    String notifyType = req.getParameter("notify_type");
			    String orderNo = req.getParameter("order_no");
			    String orderAmount = req.getParameter("order_amount");
			    String orderTime = req.getParameter("order_time");
			    String returnParams = req.getParameter("return_params");
			    String tradeNo = req.getParameter("trade_no");
			    String tradeTime = req.getParameter("trade_time");
			    String tradeStatus = req.getParameter("trade_status");
			    String sign = req.getParameter("sign");
			    KeyValues kvs = new KeyValues();

			    kvs.add(new KeyValue("merchant_code", merchantCode));
			    kvs.add(new KeyValue("notify_type", notifyType));
			    kvs.add(new KeyValue("order_no", orderNo));
			    kvs.add(new KeyValue("order_amount", orderAmount));
			    kvs.add(new KeyValue("order_time", orderTime));
			    kvs.add(new KeyValue("return_params", returnParams));
			    kvs.add(new KeyValue("trade_no", tradeNo));
			    kvs.add(new KeyValue("trade_time", tradeTime));
			    kvs.add(new KeyValue("trade_status", tradeStatus));

		    
	    	System.out.println("合付web支付宝异步回调参数："+JSON.toJSONString(kvs));
	    	PayPlatform payPlat = payPlatformService.queryPayPlatform(227L);
	    	String thizSign = kvs.sign(payPlat.getPlatformkey(), "UTF-8");
            if("success".equals(tradeStatus)){
                if(thizSign.equalsIgnoreCase(sign)){
                	PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
    				// 1.判断订单是否已经支付成功。
    				if (order == null) {
    					logger.info("支付订单不存在。");
    					out.print("fail");
    					return; // 支付订单不存在
    				}
    				if (order.getStatus() == 3) {
    					// 返回支付成功
    					out.print("success");
    					return;
    				}
					if (order.getAmount().intValue() !=Double.valueOf(orderAmount).intValue()) {
						logger.info("合付web支付宝订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(tradeNo);
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.zzPay(order);
					logger.info("合付web支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
					out.print("success");
					return;
                } 
            } 
		} catch (Exception e) {
			logger.error("合付web支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("success");
		return;
	}
	
	@RequestMapping("/hebaozfb/view")
	public String view(HttpServletRequest req, HttpServletResponse resp) {
		try {
			// 返回参数
			String merchantCode = req.getParameter("merchant_code");
			String notifyType = req.getParameter("notify_type");
			String orderNo = req.getParameter("order_no");
			String orderAmount = req.getParameter("order_amount");
			String orderTime = req.getParameter("order_time");
			String returnParams = req.getParameter("return_params");
			String tradeNo = req.getParameter("trade_no");
			String tradeTime = req.getParameter("trade_time");
			String tradeStatus = req.getParameter("trade_status");
			String sign = req.getParameter("sign");
			PayPlatform payPlat = payPlatformService.queryPayPlatform(227L);

			// 构造签名
			KeyValues kvs = new KeyValues();
			kvs.add(new KeyValue("merchant_code", merchantCode));
			kvs.add(new KeyValue("notify_type", notifyType));
			kvs.add(new KeyValue("order_no", orderNo));
			kvs.add(new KeyValue("order_amount", orderAmount));
			kvs.add(new KeyValue("order_time", orderTime));
			kvs.add(new KeyValue("return_params", returnParams));
			kvs.add(new KeyValue("trade_no", tradeNo));
			kvs.add(new KeyValue("trade_time", tradeTime));
			kvs.add(new KeyValue("trade_status", tradeStatus));
			String thizSign = kvs.sign(payPlat.getPlatformkey(), "UTF-8");
			logger.info("充值金额：" + orderAmount + "合付扫码支付后台回调信息：" + thizSign);
			if (thizSign.equalsIgnoreCase(sign)) {
				PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
				req.setAttribute("order", order);
				if (order == null) {
					logger.info("订单号不存在。");
					return "/payment/payFail";
				} else if (order.getStatus() == 3) {
					return "/payment/payOk";
				} else {
					DecimalFormat numberFormat = new DecimalFormat("#0.0");
					if (!numberFormat.format(order.getAmount()).equals(orderAmount)) {
						logger.info("订单号[" + order.getPlatformorders() + "]金额不匹配。");
						return "/payment/payFail";
					} else if ("success".equals(tradeStatus)) { // tradeStatus ="success"代表成功
						// 修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(tradeNo);
						order.setPaystatus(2);
						// 用户上分
						payPlatformService.thwPay(order);
						logger.info("合付web支付宝扫码支付充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
						return "/payment/payOk";
					} else {
						logger.error("合付web支付宝前台回调失败。");
						return "/payment/payFail";
					}
				}
			} else {
				logger.info("合付web支付宝扫码签名验证失败。");
				return "/payment/payFail";
			}
		} catch (Exception e) {
			logger.error("合付web支付宝前台回调失败。" + e.getMessage());
			return "/payment/payFail";
		}
	}
}
