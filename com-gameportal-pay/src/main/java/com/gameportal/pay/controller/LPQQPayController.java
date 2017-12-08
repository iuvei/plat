package com.gameportal.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.model.LePayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.ExcuteRequestUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.lechinepay.channel.lepay.client.apppay.AppPay;

/**
 * LePay QQ扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class LPQQPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(LPQQPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * QQ扫码预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/lpqq/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
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
				LePayment lp = payPlatformService.lePayment(userInfo, params, payPlat, null);

				Map<String, Object> requestMap = new HashMap<String, Object>();
				requestMap.put("version", lp.getVersion());
				requestMap.put("encoding", lp.getEncoding());
				requestMap.put("signature", lp.getSignature());
//				requestMap.put("reqReserved", lp.getReqReserved());
				requestMap.put("mchId", lp.getMchId());
				requestMap.put("cmpAppId", lp.getCmpAppId());
				requestMap.put("payTypeCode", lp.getPayTypeCode());
				requestMap.put("outTradeNo", lp.getOutTradeNo());
				requestMap.put("tradeTime", lp.getTradeTime());
				requestMap.put("amount", lp.getAmount());
				requestMap.put("summary", lp.getSummary());
				requestMap.put("summaryDetail", lp.getSummary());
				requestMap.put("deviceIp", lp.getDeviceIp());
				requestMap.put("returnUrl", lp.getReturnUrl());

				System.out.println("lepay QQ扫码请求参数：" + requestMap.toString());
				try {
					LePayment.init();
					Map<String, Object> responseMap = new ExcuteRequestUtil(payPlat.getDomainname(), "/order/add").execute(requestMap);
					//response.sendRedirect(responseMap.get("webOrderInfo").toString());
					System.out.println(">>>>>>>>>>>>"+requestMap);
					JSONObject json = JSONObject.parseObject(responseMap.get("reponseData").toString());
					request.setAttribute("code_img_url", json.get("qrPath"));
					request.setAttribute("orderNO", lp.getOutTradeNo());
					request.setAttribute("amount", Double.valueOf(lp.getAmount())/100);
					return "/payment/zlqqJump";
				} catch (Exception e) {
					e.getMessage();
					logger.error("lepay wab QQ扫码提交失败。", e);
				}
			}
		}
		return null;
	}

	@RequestMapping("/lpqq/view")
	@ResponseBody
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("QQ扫码支付成功，请稍候查看您的钱包余额。");
			writer.flush();
		} catch (Exception e) {
			logger.error("LePay web QQ扫码支付同步回调失败。", e);
		}
	}
}
