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
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.ZSPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.URLUtils;
import com.gameportal.pay.util.sz.HttpUtilKeyVal;
import com.gameportal.pay.util.sz.Security;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;


/**
 * 泽圣WEB支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class ZSZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ZSZFBPayController.class);
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
	 * @throws IOException
	 */
	@RequestMapping("/zszfb/{type}")
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
				ZSPayment zs = payPlatformService.zsPayment(userInfo, params, payPlat, null);
				try {
					String[] signFields = { "merchantCode", "outOrderId", "amount",
							"orderCreateTime", "noticeUrl", "isSupportCredit" };
					JSONObject json = new JSONObject();
					json.put("merchantCode", zs.getMerchantCode());
					json.put("outOrderId", zs.getOutOrderId());
					json.put("amount", zs.getAmount());
					json.put("orderCreateTime", zs.getOrderCreateTime()); // 必填
					json.put("noticeUrl", zs.getNoticeUrl());
					json.put("isSupportCredit", zs.getIsSupportCredit());
					try {// 签名
						String sign = Security.countSignMd5(payPlat.getPlatformkey(), signFields, json);
						json.put("sign", sign);
						System.out.println("签名的sign:" + sign);
					} catch (Exception e) {
						System.out.println("签名失败");
					}
					json.put("goodsName", zs.getGoodsName());
					json.put("model", zs.getModel());
					json.put("deviceNo", zs.getDeviceNo());
					json.put("ip", zs.getIp());
					json.put("payChannel", zs.getPayChannel());
					System.out.println("泽圣WEB支付宝请求报文:" + json.toString());
					// 报文提交
					String retStr = HttpUtilKeyVal.doPost(payPlat.getDomainname(), json);
					System.out.println("泽圣WEB支付宝请求应答报文:" + retStr);
					JSONObject retJson = JSONObject.parseObject(retStr);
					JSONObject data = retJson.getJSONObject("data");
					String url = data.getString("url");
					request.setAttribute("code_img_url", url);
					request.setAttribute("amount", Double.valueOf(zs.getAmount()/100));
					request.setAttribute("orderNO", zs.getOutOrderId());
				} catch (Exception e) {
					logger.error("泽圣WEB支付宝提交訂單失敗。", e);
				}
			}
		}
		return "/payment/zlJump";
	}

	/**
	 * 泽圣支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zszfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
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
			System.out.println("泽圣WEB支付宝返回："+message);
			Map<String, String> map = URLUtils.getMap(message);
			PayOrder order = payPlatformService.queryPayOrderId(map.get("outOrderId"), null, null, "0");
			PayPlatform payPlat = payPlatformService.queryPayPlatform(233L);
//			if ("00".equals(json.getString("replyCode"))) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("{'code':'-1'}");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("{'code':'00'}");
					// logger.info("订单已经支付成功，不能重复支付。");
					return;
				}
				// 2.验签
				String[] signFields = { "merchantCode", "instructCode", "transType", "outOrderId","transTime","totalAmount" };
				JSONObject signJson = new JSONObject();
				signJson.put("merchantCode", map.get("merchantCode"));
				signJson.put("instructCode", map.get("instructCode"));
				signJson.put("transType", map.get("transType"));
				signJson.put("outOrderId", map.get("outOrderId"));
				signJson.put("transTime", map.get("transTime"));
				signJson.put("totalAmount",map.get("totalAmount"));
				signJson.put("sign", map.get("sign"));
				boolean flag = Security.verifySignMd5(payPlat.getPlatformkey(), signFields, signJson);
				if (flag) {
					if (order.getAmount().intValue() != Double.valueOf(map.get("totalAmount")).intValue() / 100) { // 单位分
						logger.info("泽圣WEB支付宝订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(map.get("instructCode"));
					order.setPaystatus(2);
					order.setStatus(3);

					// 4.用户上分
					payPlatformService.zsPay(order);
					logger.info("泽圣WEB支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
					out.print("{'code':'00'}");
					return;
				} else {
					logger.info("泽圣WEB支付宝签名验证失败。");
					out.print("{'code':'-1'}");
					return;
				}
//			}
		} catch (Exception e) {
			logger.error("泽圣WEB支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
	}
}
