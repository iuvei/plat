package com.gameportal.pay.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
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
import com.gameportal.pay.model.fcs.request.FCSOpenApiAliPayRequest;
import com.gameportal.pay.model.fcs.response.FCSOpenApiResponse;
import com.gameportal.pay.util.WebConst;
import com.gameportal.pay.utils.CoderUtil;
import com.gameportal.pay.utils.DefaultFCSOpenApiClient;
import com.gameportal.pay.utils.RSACoderUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 支付宝扫码支付。
 * 
 * @author sum
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class AlipayPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(AlipayPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 支付宝扫码支付。
	 * 
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/alipay/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "channel",required=false) String channel,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
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
				if(StringUtils.isNotEmpty(channel)){
					params.put("channel", channel);
				}
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				FCSOpenApiAliPayRequest jf = payPlatformService.jfaliPayment(userInfo, params, payPlat);
				FCSOpenApiResponse apiResponse = new DefaultFCSOpenApiClient(WebConst.PUBLIC_KEY, WebConst.PRIVATE_KEY,
						"UTF-8").excute(jf, payPlat.getDomainname() + "/gateway");
				if ("T".equals(apiResponse.getIs_succ())) {
					if (StringUtils.isNotEmpty(apiResponse.getResponse())) {
						String responseCharset = apiResponse.getCharset();
						byte[] byte64 = CoderUtil.decodeStr(apiResponse.getResponse().getBytes("UTF-8"));

						String responseResult = new String(
								RSACoderUtil.decryptByPrivateKey(byte64, WebConst.PRIVATE_KEY), responseCharset);
						boolean verify = RSACoderUtil.verify(responseResult.getBytes(responseCharset),
								WebConst.PUBLIC_KEY, apiResponse.getSign());
						logger.info("responseResult"+responseResult);
						if (verify) {
							JSONObject json = JSONObject.parseObject(responseResult);
							if(StringUtils.isNotEmpty(channel)){
								redirect(json.getString("ali_pay_sm_url"),response.getWriter());
								return null;
							}
							request.setAttribute("jf", jf);
							if(!json.containsKey("base64QRCode")){
								request.setAttribute("imageqr",json.get("ali_pay_sm_url"));
							}else{
								request.setAttribute("imageqr", json.get("base64QRCode"));
							}
						} else {
							throw new Exception("ERROR ## doSign by fcsPublicKey has an error");
						}
					} else {
						System.out.println("errorCode = " + apiResponse.getFault_code() + ";errorMessage = "
								+ apiResponse.getFault_reason());
					}
				}
			}
		}
		return "/payment/alipayJump";
	}
	
	private void redirect(String url,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+url +"' method=\"post\" id=\"frm7\"><br>";
		out +="<script language=\"javascript\">";
		out +="document.getElementById(\"frm7\").submit();";
		out +="</script>";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		writer.write(out);
	}

	/**
	 * 中联支付回调接口
	 * 
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/alipay/callback")
	@ResponseBody
	public synchronized void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		try {
			String content= request.getParameter("content");
			String sign = request.getParameter("sign");
			String sign_type = request.getParameter("sign_type");
			String input_charset = request.getParameter("input_charset");
			String request_time = request.getParameter("request_time");
			String out_trade_no = request.getParameter("out_trade_no");
			String status = request.getParameter("status");
			logger.info("支付宝回调信息：content=" + content+",sign="+sign+",sign_type="+sign_type+",input_charset="+input_charset+",request_time="+request_time+",out_trade_no="+out_trade_no);
			if("1".equals(status)){
				String param = new String(RSACoderUtil.decryptByPrivateKey(CoderUtil.decodeStr(URLDecoder.decode(content, "UTF-8").getBytes("UTF-8")), WebConst.PRIVATE_KEY), "UTF-8");
				JSONObject json = JSONObject.parseObject(param);
				boolean flag = RSACoderUtil.verify(param.getBytes("UTF-8"), WebConst.PUBLIC_KEY,URLDecoder.decode(sign,"UTF-8"));
				System.out.println("回调response解密："+param+" 验证结果："+flag);
				// 1.判断订单是否已经支付成功。
				PayOrder order = payPlatformService.queryPayOrderId(json.getString("out_trade_no"), null, null, "0");
				if (order == null) {
					out.write("fail");
					logger.info("订单号:"+json.getString("out_trade_no")+"不存在!");
					return;
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.write("success");
					return;
				}
				if (order != null) {
					// 2.验签
					if (flag) {
						if (order.getAmount().intValue() !=Double.valueOf(json.getString("amount_str")).intValue()) {
							logger.info("支付宝扫码订单金额不匹配。");
							out.write("fail");
							return;
						}
						// 3.修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(json.getString("trade_id"));
						order.setPaystatus(2);
						order.setStatus(3);

						// 4.用户上分
						payPlatformService.jfpPay(order);
						out.write("success");
						return;
					} else {
						logger.info("签名验证失败。");
						out.write("fail");
					}
				}
			}
		} catch (Exception e) {
			logger.error("支付宝回调失败。" + e);
			out.write("fail");
		}
	}
}
