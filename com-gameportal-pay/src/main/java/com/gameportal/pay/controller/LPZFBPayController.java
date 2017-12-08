package com.gameportal.pay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.model.LePayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.ZLPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.MyRSAUtils;
import com.gameportal.pay.util.SignatureUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * LePay 支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class LPZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(LPZFBPayController.class);
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
	@RequestMapping("/lpzfb/{type}")
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
				if(result !=null){
					return result;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				LePayment le = payPlatformService.lePayment(userInfo, params, payPlat, null);
				try {
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("LePay web支付宝提交表单失败。",e);
				}
				request.setAttribute("zl", le);
				request.setAttribute("amount", Double.valueOf(le.getAmount())/100);
				request.setAttribute("orderNO", le.getOutTradeNo());
				return "/payment/zlJump";
			}
		}
		return null;
	}

	/**
	 * LePay 支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/lpzfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			// 定义BufferedReader输入流来读取URL的响应
			InputStream inputStream = request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = "";
			String message = "";
			while ((line = bufferedReader.readLine()) != null) {
				message += line;
			}
			inputStream.close();
			inputStreamReader.close();
			System.out.println("LePay web支付宝异步回调参数："+message);
			JSONObject data = JSONObject.parseObject(message);
			PayOrder order = payPlatformService.queryPayOrderId(data.getString("orgOrderNo"), null, null, "0");
			if (data.getString("paySt").equals("2")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					json.put("success", "true");
					out.print(json.toJSONString());
					return;
				}
				// 2.验签
				Map<String, Object> params = new HashMap<>();
				if (StringUtils.isNotEmpty(data.getString("amount"))) {
					params.put("amount", data.getString("amount"));
				}
				if (StringUtils.isNotEmpty(data.getString("extra"))) {
					params.put("extra", data.getString("extra"));
				}
				if (StringUtils.isNotEmpty(data.getString("orderDt"))) {
					params.put("orderDt", data.getString("orderDt"));
				}
				if (StringUtils.isNotEmpty(data.getString("orderNo"))) {
					params.put("orderNo", data.getString("orderNo"));
				}
				if (StringUtils.isNotEmpty(data.getString("orgOrderNo"))) {
					params.put("orgOrderNo", data.getString("orgOrderNo"));
				}
				if (StringUtils.isNotEmpty(data.getString("body"))) {
					params.put("body", data.getString("body"));
				}
				if (StringUtils.isNotEmpty(data.getString("orgId"))) {
					params.put("orgId", data.getString("orgId"));
				}
				if (StringUtils.isNotEmpty(data.getString("paySt"))) {
					params.put("paySt", data.getString("paySt"));
				}
				if (StringUtils.isNotEmpty(data.getString("fee"))) {
					params.put("fee", data.getString("fee"));
				}
				if (StringUtils.isNotEmpty(data.getString("respMsg"))) {
					params.put("respMsg", data.getString("respMsg"));
				}
				if (StringUtils.isNotEmpty(data.getString("account"))) {
					params.put("account", data.getString("account"));
				}

				if (StringUtils.isNotEmpty(data.getString("respCode"))) {
					params.put("respCode", data.getString("respCode"));
				}
				if (StringUtils.isNotEmpty(data.getString("subject"))) {
					params.put("subject", data.getString("subject"));
				}
				if (StringUtils.isNotEmpty(data.getString("description"))) {
					params.put("description", data.getString("description"));
				}

				String bigStr = SignatureUtil.hex(params);
				logger.info("LePay web后台支付宝回调参数：" + bigStr);
				if (MyRSAUtils.verifySignature(ZLPayment.PUBLICKEY, data.getString("signature"), bigStr, MyRSAUtils.MD5_SIGN_ALGORITHM)) {
					if (order.getAmount().intValue() !=Double.valueOf(data.getString("amount")).intValue()/100) { //单位分
						logger.info("LePay web支付宝订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(data.getString("orderNo"));
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.lpPay(order);
					logger.info("LePay web支付宝充值加款成功->平台订单编号：" + order.getPlatformorders());
					json.put("success", "true");
					out.print(json.toJSONString());
					return;
				} else {
					logger.info("LePay web支付宝签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("LePay web支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		json.put("success", "true");
		out.print(json.toJSONString());
		return;
	}
}
