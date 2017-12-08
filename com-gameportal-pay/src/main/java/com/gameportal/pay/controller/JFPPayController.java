package com.gameportal.pay.controller;

import java.io.IOException;
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
import com.gameportal.pay.model.fcs.request.FCSOpenApiWxPayRequest;
import com.gameportal.pay.model.fcs.response.FCSOpenApiResponse;
import com.gameportal.pay.util.WebConst;
import com.gameportal.pay.utils.CoderUtil;
import com.gameportal.pay.utils.DefaultFCSOpenApiClient;
import com.gameportal.pay.utils.RSACoderUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 捷付手机支付支付。
 * 
 * @author sum
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class JFPPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(JFPPayController.class);
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
	@RequestMapping("/jfp/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost,
			HttpServletRequest request,HttpServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
			if (userInfo != null) {
				if (hd != null && !"".equals(hd)) {
					wapValidate(userInfo, hd, requestHost, request, response);
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				FCSOpenApiWxPayRequest jf = payPlatformService.jfwxPayment(userInfo, params, payPlat);
				try {
					FCSOpenApiResponse apiResponse = new DefaultFCSOpenApiClient(WebConst.PUBLIC_KEY, WebConst.PRIVATE_KEY,
							"UTF-8").excute(jf, payPlat.getDomainname() + "/gateway");
				   if("T".equals(apiResponse.getIs_succ())){
					   if (StringUtils.isNotEmpty(apiResponse.getResponse())) {
							String responseCharset = apiResponse.getCharset();
							byte[] byte64 = CoderUtil.decodeStr(apiResponse.getResponse().getBytes("UTF-8"));
							String responseResult = new String(
									RSACoderUtil.decryptByPrivateKey(byte64, WebConst.PRIVATE_KEY), responseCharset);
							boolean verify = RSACoderUtil.verify(responseResult.getBytes(responseCharset),
									WebConst.PUBLIC_KEY, apiResponse.getSign());
							if (verify) {
								JSONObject json = JSONObject.parseObject(responseResult);
								if(!json.containsKey("base64QRCode")){
									redirect(json.getString("wx_pay_sm_url"), payPlat, writer);
								}else{
									redirect(json.getString("base64QRCode"), payPlat, writer);
								}
							} else {
								throw new Exception("ERROR ## doSign by fcsPublicKey has an error");
							}
						} else {
							System.out.println("errorCode = " + apiResponse.getFault_code() + ";errorMessage = "
									+ apiResponse.getFault_reason());
						}
			        }else {
			            System.out.println("errorCode = " + apiResponse.getFault_code() + ";errorMessage = "
			                    + apiResponse.getFault_reason());
			        }
					writer.flush();
					response.flushBuffer();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
					response.addHeader("refresh", "5;url=http://" + requestHost);
					writer.write("发生错误，返回首页...");
					writer.flush();
					response.flushBuffer();
					writer.close();
					return;
				}
			}
		}
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
	@RequestMapping("/jfp/callback")
	@ResponseBody
	public synchronized void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PrintWriter out = response.getWriter();
		// 返回参数 
		String content= request.getParameter("content");
		String sign = request.getParameter("sign");
		String sign_type = request.getParameter("sign_type");
		String input_charset = request.getParameter("input_charset");
		String request_time = request.getParameter("request_time");
		String out_trade_no = request.getParameter("out_trade_no");
		String status = request.getParameter("status");
		try {
			if ("1".equals(status)) {
				String param = new String(RSACoderUtil.decryptByPrivateKey(CoderUtil.decodeStr(URLDecoder.decode(content, "UTF-8").getBytes("UTF-8")), WebConst.PRIVATE_KEY), "UTF-8");
				JSONObject json = JSONObject.parseObject(param);
				boolean flag = RSACoderUtil.verify(param.getBytes("UTF-8"), WebConst.PUBLIC_KEY,URLDecoder.decode(sign,"UTF-8"));
				System.out.println("回调response解密："+param+" 验证结果："+flag);
				// 1.判断订单是否已经支付成功。
				PayOrder order = payPlatformService.queryPayOrderId(json.getString("out_trade_no"), null, null,
						"0");
				if (order == null) {
					logger.info("支付订单不存在。");
					out.write("支付订单不存在!请返回首页。");
					out.flush();
					out.close();
					return;
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					logger.info("支付宝回调信息：content=" + content+",sign="+sign+",sign_type="+sign_type+",input_charset="+input_charset+",request_time="+request_time+",out_trade_no="+out_trade_no);
					out.write("success");
					out.flush();
					out.close();
					return;
				}
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
					logger.info("微信充值加款成功->平台订单编号：" + order.getPlatformorders() + "->第三方订单编号：" + order.getRemarks()+"金额："+order.getAmount());
					out.write("充值成功!返回首页。");
					out.flush();
					out.close();
				} else {
					out.write("签名验证失败。");
					out.flush();
					out.close();
					return;
				}
			}
		} catch (Exception e) {
			out.write("签名验证失败!返回首页");
			out.flush();
			out.close();
			return;
		}
	}

	public void view(String billno,String remark, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PayOrder order = payPlatformService.queryPayOrderId(billno, null, null, "0");
		request.setAttribute("order", order);
		PrintWriter writer = response.getWriter();
		if (order != null && order.getStatus() == 3) {
			response.addHeader("refresh", "3;url=http://" + remark);
			writer.write("充值成功!返回首页...");
			writer.flush();
			writer.close();
		}
		response.addHeader("refresh", "3;url=http://" + remark);
		writer.write("充值失败!返回首页...");
		writer.flush();
		writer.close();
	}
	
	private void redirect(String url,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body>";
		out +="<div>请长按下面的二维码截图保存至手机，进行扫码识别。</div>";
		out +="<img src='"+url+"' />";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		writer.write(out);
	}
}
