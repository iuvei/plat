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

import com.gameportal.pay.model.MoBoPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 摩宝-微信扫码
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class MBBlankPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(MBBlankPayController.class);
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
	 */
	@RequestMapping("/mbob/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request,
			HttpServletResponse response) {
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
				params.put("customerIP", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				MoBoPayment mb = payPlatformService.mbPayment(userInfo, params, payPlat);
				request.setAttribute("pay", mb);
				request.setAttribute("submitUrl", payPlat.getDomainname());
				return "/payment/mbJump";
			}
		}
		return null;
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
	@RequestMapping("/mbob/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
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
			logger.info("摩宝支付后台回调信息：" + srcMsg);
			PayOrder order = payPlatformService.queryPayOrderId(orderNo, null, null, "0");
			if (notifyType.equals("0")) { // 0-页面跳转通知，不处理业务 1-服务器异步通知
				response.sendRedirect("/pay/mbo/view.do?billno=" + order.getPlatformorders());
			}
			if (orderStatus.equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.write("Fail");
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.write("SUCCESS");
				}else{
					// 2.验签
					PayPlatform payPlat = payPlatformService.queryPayPlatform(10L);
					if (signMsg.equalsIgnoreCase(MD5Util.signByMD5(srcMsg, payPlat.getPlatformkey()))) {
						DecimalFormat numberFormat = new DecimalFormat("#.00");
						if (!numberFormat.format(order.getAmount()).equals(tradeAmt)) {
							logger.info("摩宝扫码订单金额不匹配。");
							out.write("Fail");
						}
						// 3.修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(accNo);
						order.setPaystatus(2);
						
						// 4.用户上分
						payPlatformService.mbwPay(order);
						logger.info("摩宝充值加款成功->平台订单编号：" + order.getPlatformorders() + "->第三方订单编号：" + order.getRemarks());
						out.write("SUCCESS");
					} else {
						logger.info("摩宝扫码签名验证失败。");
						out.write("Fail");
					}
				}
			}
		} catch (Exception e) {
			logger.error("摩宝扫码支付后台回调失败。", e);
			out.write("Fail");
		}
		out.write("SUCCESS");
		out.flush();
		out.close();
	}

	@RequestMapping("/mbob/view")
	public String view(String billno, HttpServletRequest request, HttpServletResponse response) {
		PayOrder order = payPlatformService.queryPayOrderId(billno, null, null, "0");
		request.setAttribute("order", order);
		if (order != null && order.getStatus() == 3) {
			return "/payment/payOk";
		}
		return "/payment/payFail";
	}
}
