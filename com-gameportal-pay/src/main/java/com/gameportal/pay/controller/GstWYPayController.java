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
 * PC国盛通网银扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class GstWYPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(GstWYPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 网银预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/gstwy/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "channel", required = false) String channel,
			@RequestParam(value = "bankCode", required = false) String bankCode,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
				GstPayment gst = payPlatformService.gstPayment(userInfo, params, payPlat,channel);
				gst.setCustomerIp(ClientUtills.getInstance().getIpAddr(request));
				gst.setBankCode(bankCode);
				gst.setSign(gst.buildSignature(payPlat.getPlatformkey()));
				request.setAttribute("pay", gst);
				request.setAttribute("submitUrl", payPlat.getDomainname());
				return "/payment/gstJump";
			}
		}
		return null;
	}

	/**
	 * 网银支付回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/gstwy/callback")
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
			if(StringUtils.isEmpty(tradeStatus)){
				out.print("fail");
				return;
			}
			if (tradeStatus.equals("success")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					logger.info("国盛通网银后台回调信息：orderNo=" + orderNo + ",orderNo=" + orderNo + ",tradeStatus=" +tradeStatus);
					out.print("success");
					return;
				}
				// 2.验签
				PayPlatform payPlat = payPlatformService.queryPayPlatform(44L);
				String thizSign = kvs.sign(payPlat.getPlatformkey(), "UTF-8");
				if (thizSign.equalsIgnoreCase(sign)) {
					if (order.getAmount().intValue() != Double.valueOf(orderAmount).intValue()) {
						logger.info("国盛通网银订单金额不匹配。");
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
					payPlatformService.gstPay(order);
					logger.info("国盛通网银充值加款成功->平台订单编号：" + order.getPlatformorders());
					out.print("success");
					return;
				} else {
					logger.info("国盛通网银签名验证失败。");
					out.print("fail");
					return;
				}
			}
			out.print("fail");
			return;
		} catch (Exception e) {
			logger.error("国盛通网银后台回调失败。", e);
			out.print("fail");
			return;
		}
	}
}
