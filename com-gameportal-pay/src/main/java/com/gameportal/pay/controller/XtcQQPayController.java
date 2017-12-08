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
import com.gameportal.pay.model.XTCPayment;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * PC新天誠QQ扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class XtcQQPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(XtcQQPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * QQ预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/xtcqq/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "channel", required = false) String channel, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				XTCPayment xtc = payPlatformService.xtcPayment(userInfo, params, payPlat, channel);
				logger.info("新天誠QQ请求参数" + JSON.toJSON(xtc));
				request.setAttribute("pay", xtc);
				request.setAttribute("submitUrl", payPlat.getDomainname());
			}
		}
		return "/payment/xtcJump";
	}

	/**
	 * QQ支付回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/xtcqq/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String partner = request.getParameter("partner");
			String ordernumber = request.getParameter("ordernumber");
			String orderstatus = request.getParameter("orderstatus");
			String paymoney = request.getParameter("paymoney");
			String sysnumber = request.getParameter("sysnumber");
			String sign = request.getParameter("sign");

			PayPlatform payPlat = payPlatformService.queryPayPlatform(205L);
			String signature = String.format("partner=%s&ordernumber=%s&orderstatus=%s&paymoney=%s", partner,
					ordernumber, orderstatus, paymoney + payPlat.getPlatformkey());

			logger.info("新天誠QQ后台回调信息：" + signature);
			signature = MD5Util.getMD5Encode(signature);
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
					return;
				}
				// 2.验签

				if (signature.equalsIgnoreCase(sign)) {
					if (order.getAmount().intValue() != Double.valueOf(paymoney).intValue()) {
						logger.info("新天誠QQ扫码订单金额不匹配。");
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
					payPlatformService.xtcPay(order);
					logger.info("新天誠QQ扫码充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					out.print("ok");
					return;
				} else {
					logger.info("新天誠QQ扫码签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("新天誠QQ扫码后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("ok");
		return;
	}
}
