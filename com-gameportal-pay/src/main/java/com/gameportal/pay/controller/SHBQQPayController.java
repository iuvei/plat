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
import com.gameportal.pay.model.SHBPayment;
import com.gameportal.pay.model.shb.Dinpay;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.HttpClientUtil;
import com.gameportal.pay.util.XstreamUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.itrus.util.sign.RSAWithSoftware;

/**
 * 速汇宝QQ扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class SHBQQPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(SHBQQPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 *  QQ扫码预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/shbqq/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (userInfo != null) {
				String result = webValidate(userInfo, hd, request);
				if (result != null) {
					return result;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				SHBPayment zf = payPlatformService.shbPayment(userInfo, params, payPlat, null);
				try {
					Map<String, String> reqMap = new HashMap<String, String>();
					reqMap.put("merchant_code", zf.getMerchantCode());
					reqMap.put("service_type", zf.getServiceType());
					reqMap.put("notify_url", zf.getNotifyUrl());
					reqMap.put("interface_version", zf.getInterfaceVersion());
					reqMap.put("client_ip", zf.getClientIp());
					reqMap.put("sign_type", zf.getSignType());
					reqMap.put("order_no", zf.getOrderNo());
					reqMap.put("order_time", zf.getOrderTime());
					reqMap.put("order_amount", zf.getOrderAmount());
					reqMap.put("product_name", zf.getProductName());
					reqMap.put("sign", zf.genSignStr());
					logger.info("速汇宝WEB QQ提交参数："+reqMap.toString());
					result = new HttpClientUtil().doPost(payPlat.getDomainname(), reqMap, "utf-8");
					logger.info("速汇宝WEB QQ提交返回:"+result);
					Dinpay dinpay =XstreamUtil.toBean(result, Dinpay.class);
					
					request.setAttribute("code_img_url", dinpay.getResponse().getQrCode());
					request.setAttribute("orderNO", zf.getOrderNo());
					request.setAttribute("amount", zf.getOrderAmount());
					
				} catch (Exception e) {
					logger.error("速汇宝提交訂單失敗。", e);
				}
			}
		}
		return "/payment/zlqqJump";
	}

	/**
	 * 速汇宝 QQ回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shbqq/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
			String interface_version = (String) request.getParameter("interface_version");
			String merchant_code = (String) request.getParameter("merchant_code");
			String notify_type = (String) request.getParameter("notify_type");
			String notify_id = (String) request.getParameter("notify_id");
			String sign_type = (String) request.getParameter("sign_type");
			String dinpaySign = (String) request.getParameter("sign");
			String order_no = (String) request.getParameter("order_no");
			String order_time = (String) request.getParameter("order_time");
			String order_amount = (String) request.getParameter("order_amount");
			String extra_return_param = (String) request.getParameter("extra_return_param");
			String trade_no = (String) request.getParameter("trade_no");
			String trade_time = (String) request.getParameter("trade_time");
			String trade_status = (String) request.getParameter("trade_status");
			String bank_seq_no = (String) request.getParameter("bank_seq_no");

			System.out.println("interface_version = " + interface_version + "\n" + "merchant_code = " + merchant_code
					+ "\n" + "notify_type = " + notify_type + "\n" + "notify_id = " + notify_id + "\n" + "sign_type = "
					+ sign_type + "\n" + "dinpaySign = " + dinpaySign + "\n" + "order_no = " + order_no + "\n"
					+ "order_time = " + order_time + "\n" + "order_amount = " + order_amount + "\n"
					+ "extra_return_param = " + extra_return_param + "\n" + "trade_no = " + trade_no + "\n"
					+ "trade_time = " + trade_time + "\n" + "trade_status = " + trade_status + "\n" + "bank_seq_no = "
					+ bank_seq_no + "\n");

			PayOrder order = payPlatformService.queryPayOrderId(order_no, null, null, "0");

			StringBuilder signStr = new StringBuilder();
			if (StringUtils.isNotEmpty(bank_seq_no)) {
				signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
			}
			if (StringUtils.isNotEmpty(extra_return_param)) {
				signStr.append("extra_return_param=").append(extra_return_param).append("&");
			}
			signStr.append("interface_version=").append(interface_version).append("&");
			signStr.append("merchant_code=").append(merchant_code).append("&");
			signStr.append("notify_id=").append(notify_id).append("&");
			signStr.append("notify_type=").append(notify_type).append("&");
			signStr.append("order_amount=").append(order_amount).append("&");
			signStr.append("order_no=").append(order_no).append("&");
			signStr.append("order_time=").append(order_time).append("&");
			signStr.append("trade_no=").append(trade_no).append("&");
			signStr.append("trade_status=").append(trade_status).append("&");
			signStr.append("trade_time=").append(trade_time);
			String signInfo = signStr.toString();
//			System.out.println("速汇宝 QQ返回的签名参数排序：" + signInfo.length() + " -->" + signInfo);
//			System.out.println("速汇宝 QQ返回的签名：" + dinpaySign.length() + " -->" + dinpaySign);
			if ("SUCCESS".equals(trade_status)) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("SUCCESS");
					// logger.info("订单已经支付成功，不能重复支付。");
					return;
				}
				// 2.验签
//				String result = RSACoderUtil.getParamByPublicKey(signInfo, WebConst.CHARSET,
//						ZFPayment.PUBLICKEY);
				boolean result = RSAWithSoftware.validateSignByPublicKey(signInfo, SHBPayment.PUBLICKEY, dinpaySign);
				if (result) {
					if (order.getAmount().intValue() != Double.valueOf(order_amount).intValue()) {
						logger.info("速汇宝 QQ订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(trade_no);
					order.setPaystatus(2);
					order.setStatus(3);

					// 4.用户上分
					payPlatformService.shbPay(order);
					logger.info("速汇宝 QQ充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
					out.print("SUCCESS");
					return;
				} else {
					logger.info("速汇宝 QQ签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("速汇宝 QQ支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("SUCCESS");
		return;
	}
}
