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
import com.alibaba.fastjson.JSONObject;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.YPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.HttpUtil;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.pay.util.URLUtils;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 优付微信
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class YPWXPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(YPWXPayController.class);
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
	@RequestMapping("/ypwx/{type}")
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
				YPayment yp = payPlatformService.ypPayment(userInfo, params, payPlat, null);
				System.out.println("优付请求参数"+JSON.toJSONString(yp));
				result = new String(HttpUtil.doPost(payPlat.getDomainname(),yp.getMap(payPlat.getPlatformkey())),"utf-8");
				System.out.println("优付返回結果"+result);
				JSONObject json =JSON.parseObject(result);
				request.setAttribute("code_img_url", json.getString("msg"));
				request.setAttribute("orderNO", yp.getOrderid());
				request.setAttribute("amount", Integer.valueOf(yp.getFee())/100);
				return "/payment/ypwxJump";
//				response.sendRedirect(json.getString("msg"));
			}
		}
		return null;
	}

	/**
	 * 优付微信回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ypwx/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		//orderid=2017051320101404&result=1&fee=1000&paytype=31&tradetime=1494677415&cpparam=&sign=cf52700ae020729f028cf031abd12d76
		String message =request.getQueryString();
		System.out.println("优付web微信异步回调参数："+message);
		try {
			Map<String, String> map = URLUtils.getMap(message);
			PayPlatform payPlat = payPlatformService.queryPayPlatform(211L);
			PayOrder order = payPlatformService.queryPayOrderId(map.get("orderid"), null, null, "0");
			if (map.get("result").equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("OK");
					return;
				}
				// 2.验签
				String bigStr = MD5Util.signByMD5(map.get("orderid")+map.get("result")+map.get("fee")+map.get("tradetime"), payPlat.getPlatformkey()).toLowerCase();
				logger.info("优付web后台微信回调参数：" + bigStr);
				if (bigStr.equals(map.get("sign"))) {
					if (order.getAmount().intValue() !=Double.valueOf(map.get("fee")).intValue()/100) { //单位分
						logger.info("优付web微信订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.ypPay(order);
					logger.info("优付web微信充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					out.print("OK");
					return;
				} else {
					logger.info("优付web微信签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("优付web微信支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("OK");
		return;
	}
}
