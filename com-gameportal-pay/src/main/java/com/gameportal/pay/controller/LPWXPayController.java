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
 * LePay 微信
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class LPWXPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(LPWXPayController.class);
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
	@RequestMapping("/lpwx/{type}")
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

				System.out.println("lepay微信请求参数：" + requestMap.toString());
				try {
					LePayment.init();
					Map<String, Object> responseMap = new ExcuteRequestUtil(payPlat.getDomainname(), "/order/add").execute(requestMap);
					//response.sendRedirect(responseMap.get("webOrderInfo").toString());
					System.out.println(">>>>>>>>>>>>"+requestMap);
					JSONObject json = JSONObject.parseObject(responseMap.get("reponseData").toString());
					request.setAttribute("code_img_url", json.get("qrPath"));
					request.setAttribute("orderNO", lp.getOutTradeNo());
					request.setAttribute("amount", Double.valueOf(lp.getAmount())/100);
					return "/payment/zlwxJump";
				} catch (Exception e) {
					e.getMessage();
					logger.error("lepay wab微信提交失败。", e);
				}
			}
		}
		return null;
	}

	/**
	 * LePay 微信回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/lepay/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}

			logger.info("lepay 异步返回参数:");
			logger.info(params.toString());
			if(params ==null){
				out.print("fail");
				return; // 支付订单不存在
			}
			if (AppPay.verify(params)) {// 验证成功
				PayOrder order = payPlatformService.queryPayOrderId(params.get("outTradeNo").toString(), null, null,
						"0");
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.println("success");
					return;
				}
				if (order.getAmount().intValue() != Double.valueOf(params.get("amount").toString()).intValue() / 100) { // 单位分
					logger.info("LePay web微信订单金额不匹配。");
					out.print("fail");
					return;
				}
				// 3.修改订单状态
				order.setUpdateDate(new Date());
				Timestamp date = new Timestamp(new Date().getTime());
				order.setUpdateDate(date);
				order.setRemarks(params.get("tradeNo") + "");
				order.setPaystatus(2);

				// 4.用户上分
				payPlatformService.lpPay(order);
				logger.info("LePay web微信充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
				out.println("success"); // 请不要修改或删除
				return;
			} else {// 验证失败
				logger.info("LePay web微信签名验证失败。");
				out.println("fail");
			}
		} catch (Exception e) {
			logger.error("LePay web微信支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.println("success");
		out.print(json.toJSONString());
		return;
	}

	@RequestMapping("/lpwx/view")
	@ResponseBody
	public void view(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			writer.write("扫码支付成功，请稍候查看您的钱包余额。");
			writer.flush();
		} catch (Exception e) {
			logger.error("LePay web微信支付同步回调失败。", e);
		}
	}
}
