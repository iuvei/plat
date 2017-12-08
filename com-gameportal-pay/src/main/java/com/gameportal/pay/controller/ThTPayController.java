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

import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.ThPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.KeyValue;
import com.gameportal.pay.util.KeyValues;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 通汇支付控制器。
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class ThTPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ThTPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 通汇预支付。
	 * 
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/tht/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "bankCode",required=false) String bankCode,
			HttpServletRequest request,HttpServletResponse response) {
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
				ThPayment pay = payPlatformService.thPayment(userInfo, params, payPlat);
				if(StringUtils.isNotEmpty(bankCode)){
					pay.setBankCode(bankCode);
				}
				pay.setSign(pay.bulidSignature(payPlat.getPlatformkey()));
				request.setAttribute("pay", pay);
				request.setAttribute("submitUrl", payPlat.getDomainname());
				return "/payment/thJump";
			}
		}
		return null;
	}

	/**
	 * 通汇回调接口
	 * 
	 * @param amount
	 * @param request
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tht/callback")
	public void callBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		PrintWriter out = resp.getWriter();
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
			PayPlatform payPlat = payPlatformService.queryPayPlatform(11L);

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
//			logger.info("充值金额：" + orderAmount + "通汇后台回调信息：" + thizSign);
			if (thizSign.equalsIgnoreCase(sign)) {
				PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
				if (order == null) {
					logger.info("订单号不存在。");
					out.write("fail");
				} else if (order.getStatus() == 3) {
					out.write("success");
				} else {
					DecimalFormat numberFormat = new DecimalFormat("#0.0");
					if (!numberFormat.format(order.getAmount()).equals(orderAmount)) {
						logger.info("订单号[" + order.getPlatformorders() + "]金额不匹配。");
						out.write("fail");
					} else if ("success".equals(tradeStatus)) { // tradeStatus ="success"代表成功
						// 修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(tradeNo);
						order.setPaystatus(2);
						// 用户上分
						payPlatformService.thPay(order);
						logger.info("通汇充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
						out.write("success");
					} else {
						logger.error("通汇后台回调失败。");
						out.write("fail");
					}
				}
			} else {
				logger.info("通汇签名验证失败。");
				out.print("fail");
			}
		} catch (Exception e) {
			logger.error("通汇后台回调失败。" + e.getMessage());
			out.write("fail");
		}
	}

	@RequestMapping("/tht/view")
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
			PayPlatform payPlat = payPlatformService.queryPayPlatform(11L);

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
			logger.info("充值金额：" + orderAmount + "通汇后台回调信息：" + thizSign);
			if (thizSign.equalsIgnoreCase(sign)) {
				PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
				req.setAttribute("order", order);
				if (order == null) {
					logger.info("订单号不存在。");
					return "/payment/payFail";
				} else if (order.getStatus() == 3) {
					logger.info("充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
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
						payPlatformService.thPay(order);
						logger.info("充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
						return "/payment/payOk";
					} else {
						logger.error("通汇前台回调失败。");
						return "/payment/payFail";
					}
				}
			} else {
				logger.info("通汇签名验证失败。");
				return "/payment/payFail";
			}
		} catch (Exception e) {
			logger.error("通汇前台回调失败。" + e.getMessage());
			return "/payment/payFail";
		}
	}
}
