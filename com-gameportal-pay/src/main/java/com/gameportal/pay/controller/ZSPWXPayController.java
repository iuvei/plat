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
 * 泽圣wap微信扫码。
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class ZSPWXPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ZSPWXPayController.class);
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
	@RequestMapping("/zspwx/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost,
			@RequestParam(value = "channel", required = false) String channel, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
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
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				ZSPayment zs = payPlatformService.zsPayment(userInfo, params, payPlat, null);
				zs.setExt(requestHost);
				try {
					String[] signFields = { "merchantCode", "outOrderId", "amount", "orderCreateTime", "noticeUrl",
							"isSupportCredit" };
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
					System.out.println("泽圣WAP微信请求报文:" + json.toString());
					// 报文提交
					String retStr = HttpUtilKeyVal.doPost(payPlat.getDomainname(), json);
					System.out.println("泽圣WAP微信请求应答报文:" + retStr);
					JSONObject retJson = JSONObject.parseObject(retStr);
					JSONObject data = retJson.getJSONObject("data");
					String url = data.getString("url");
					try {
						redirect(zs, url, writer);
						writer.flush();
						response.flushBuffer();
						writer.close();
					} catch (Exception e) {
						e.printStackTrace();
						response.addHeader("refresh", "5;url=http://" + requestHost);
						writer.write("发生错误，即将返回...");
						writer.flush();
						response.flushBuffer();
						writer.close();
						return;
					}
				} catch (Exception e) {
					logger.error("泽圣WAP微信提交訂單失敗。", e);
				}
			}
		}
	}

	private void redirect(ZSPayment zs, String code_img_url, PrintWriter writer) {
		String out = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		out += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out += "<head>";
		out += "<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out += "<title>微信扫码支付</title>";
		out += "<link href=\"/css/zf_style.css\" type=\"text/css\" rel=\"stylesheet\" />";
		out += "<link href=\"/css/wechat_pay.css\" rel=\"stylesheet\" />";
		out += "<script type=\"text/javascript\" src=\"/js/jquery-1.8.0.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/jquery.qrcode.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/utf.js\"></script>";
		out += "</head>";
		out += "<div class=\"body\">";
		out += "<h1 class=\"mod-title\">";
		out += "<span class=\"ico-wechat\"></span><span class=\"text\">微信支付</span>";
		out += "</h1>";
		out += "<div class=\"mod-ct\">";
		out += "<div class=\"order\">";
		out += "</div>";
		out += "<div class=\"amount\">";
		out += "<span>￥</span>" + (zs.getAmount() / 100) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src=\"\" width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + zs.getOutOrderId() + "</dd>";
		out += "</dl>";
		out += "</div>";
		out += "<div class=\"tip\">";
		out += "	<span class=\"dec dec-left\"></span>";
		out += "	<span class=\"dec dec-right\"></span>";
		out += "	<div class=\"ico-scan\">";
		out += "	</div>";
		out += "	<div class=\"tip-text\">";
		out += "	<p>";
		out += "		请使用微信扫一扫";
		out += "	</p>";
		out += "<p>";
		out += "	扫描二维码完成支付";
		out += "</p>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "<div class=\"foot\">";
		out += "	<div class=\"inner\">";
		out += "	<p> ";
		out += "Copyright @ 工业和信息化部备案号: 黔ICP备16006408号-1 增值电信： 黔B2-20160068";
		out += "</p>";
		out += "</div>";
		out += "</div>";
		out += "</div>";
		out += "</body>";
		out += "</html>";
		out += "<script>";
		out += "$(function(){";
		out += "sQrcode('" + code_img_url + "');";
		out += "});";
		out += "function sQrcode(qdata){";
		out += "	$(\"#showqrcode\").empty().qrcode({";
		out += "			render : \"canvas\",    			";
		out += "			text : qdata,    				";
		out += "			width : \"200\",              	";
		out += "			height : \"200\",             	";
		out += "			background : \"#ffffff\",     	";
		out += "			foreground : \"#000000\",  ";
		out += "			src: \"\"";
		out += "});	";
		out += "}";
		out += "</script>";
		writer.write(out);
		writer.flush();
		writer.close();
	}

	/**
	 * 泽圣WAP微信回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zspwx/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
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
			System.out.println("泽圣WAP微信返回：" + message);
			Map<String, String> map = URLUtils.getMap(message);
			PayOrder order = payPlatformService.queryPayOrderId(map.get("outOrderId"), null, null, "0");
			PayPlatform payPlat = payPlatformService.queryPayPlatform(233L);
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
						logger.info("泽圣WAP微信订单金额不匹配。");
						out.print("{'code':'-1'}");
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
					payPlatformService.shbPay(order);
					logger.info("泽圣WAP微信充值加款成功->平台订单编号：" + order.getPlatformorders() + " 金额：" + order.getAmount());
					out.print("{'code':'00'}");
					return;
				} else {
					logger.info("泽圣wap微信签名验证失败。");
					out.print("{'code':'-1'}");
					return;
				}
		} catch (Exception e) {
			logger.error("泽圣WAP微信支付后台回调失败。", e);
			out.print("{'code':'-1'}");
			return;
		}
	}
}
