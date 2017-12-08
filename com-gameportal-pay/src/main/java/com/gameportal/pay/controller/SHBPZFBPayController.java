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

import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.SHBPayment;
import com.gameportal.pay.model.shb.Dinpay;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.HttpClientUtil;
import com.gameportal.pay.util.XstreamUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.itrus.util.sign.RSAWithSoftware;

/**
 * 速汇宝WAP支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class SHBPZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(SHBPZFBPayController.class);
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
	@RequestMapping("/shbpzfb/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			 @RequestParam(value = "requestHost") String requestHost,
			@RequestParam(value = "channel", required = false) String channel, HttpServletRequest request, HttpServletResponse response)
					throws IOException {
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
				SHBPayment zf = payPlatformService.shbPayment(userInfo, params, payPlat, channel);
				zf.setProductName(requestHost);
				try {
					Map<String, String> reqMap = new HashMap<String, String>();
					reqMap.put("merchant_code", zf.getMerchantCode());
					reqMap.put("service_type", zf.getServiceType());
					reqMap.put("notify_url", zf.getNotifyUrl());
					reqMap.put("interface_version", zf.getInterfaceVersion());
					reqMap.put("client_ip", zf.getClientIp());
					reqMap.put("sign_type", zf.getSignType());
					reqMap.put("order_no", zf.getOrderNo());
					reqMap.put("order_time", zf.getOrderTime());
					reqMap.put("order_amount", zf.getOrderAmount());
					reqMap.put("product_name", requestHost);
					reqMap.put("sign", zf.genSignStr());
					logger.info("速汇宝WAP支付宝提交参数："+reqMap.toString());
					String result = new HttpClientUtil().doPost(payPlat.getDomainname(), reqMap, "utf-8");
					logger.info("速汇宝WAP支付宝提交返回:"+result);
					Dinpay dinpay =XstreamUtil.toBean(result, Dinpay.class);
					try {
						String extra = dinpay.getResponse().getQrCode();
						redirect(zf, extra, writer);
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
					logger.error("速汇宝WAP支付宝提交訂單失敗。", e);
				}
			}
		}
	}
	
	private void redirect(SHBPayment zf, String code_img_url, PrintWriter writer) {
		String out = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		out += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out += "<head>";
		out += "<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out += "<title>支付宝扫码支付</title>";
		out += "<link href=\"/css/zf_style.css\" type=\"text/css\" rel=\"stylesheet\" />";
		out += "<link href=\"/css/wechat_pay.css\" rel=\"stylesheet\" />";
		out += "<script type=\"text/javascript\" src=\"/js/jquery-1.8.0.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/jquery.qrcode.js\"></script>";
		out += "<script type=\"text/javascript\" src=\"/js/utf.js\"></script>";
		out += "</head>";
		out += "<div class=\"body\">";
		out += "<h1 class=\"mod-title\">";
		out += "<span class=\"ico-wechat\"></span><span class=\"text\">支付宝支付</span>";
		out += "</h1>";
		out += "<div class=\"mod-ct\">";
		out += "<div class=\"order\">";
		out += "</div>";
		out += "<div class=\"amount\">";
		out += "<span>￥</span>" + Double.valueOf(zf.getOrderAmount()) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src=\"\" width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + zf.getOrderNo() + "</dd>";
		out += "</dl>";
		out += "</div>";
		out += "<div class=\"tip\">";
		out += "	<span class=\"dec dec-left\"></span>";
		out += "	<span class=\"dec dec-right\"></span>";
		out += "	<div class=\"ico-scan\">";
		out += "	</div>";
		out += "	<div class=\"tip-text\">";
		out += "	<p>";
		out += "		请使用支付宝扫一扫";
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
	 * 速汇宝WAP支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shbpzfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
			String interface_version = (String) request.getParameter("interface_version");
			String merchant_code = (String) request.getParameter("merchant_code");
			String notify_type = (String) request.getParameter("notify_type");
			String notify_id = (String) request.getParameter("notify_id");
			String sign_type = (String) request.getParameter("sign_type");
			String dinpaySign = (String) request.getParameter("sign");
			String order_no = (String) request.getParameter("order_no");
			String order_time = (String) request.getParameter("order_time");
			String order_amount = (String) request.getParameter("order_amount");
			String extra_return_param = (String) request.getParameter("extra_return_param");
			String trade_no = (String) request.getParameter("trade_no");
			String trade_time = (String) request.getParameter("trade_time");
			String trade_status = (String) request.getParameter("trade_status");
			String bank_seq_no = (String) request.getParameter("bank_seq_no");

			System.out.println("interface_version = " + interface_version + "\n" + "merchant_code = " + merchant_code
					+ "\n" + "notify_type = " + notify_type + "\n" + "notify_id = " + notify_id + "\n" + "sign_type = "
					+ sign_type + "\n" + "dinpaySign = " + dinpaySign + "\n" + "order_no = " + order_no + "\n"
					+ "order_time = " + order_time + "\n" + "order_amount = " + order_amount + "\n"
					+ "extra_return_param = " + extra_return_param + "\n" + "trade_no = " + trade_no + "\n"
					+ "trade_time = " + trade_time + "\n" + "trade_status = " + trade_status + "\n" + "bank_seq_no = "
					+ bank_seq_no + "\n");

			PayOrder order = payPlatformService.queryPayOrderId(order_no, null, null, "0");

			StringBuilder signStr = new StringBuilder();
			if (StringUtils.isNotEmpty(bank_seq_no)) {
				signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
			}
			if (StringUtils.isNotEmpty(extra_return_param)) {
				signStr.append("extra_return_param=").append(extra_return_param).append("&");
			}
			signStr.append("interface_version=").append(interface_version).append("&");
			signStr.append("merchant_code=").append(merchant_code).append("&");
			signStr.append("notify_id=").append(notify_id).append("&");
			signStr.append("notify_type=").append(notify_type).append("&");
			signStr.append("order_amount=").append(order_amount).append("&");
			signStr.append("order_no=").append(order_no).append("&");
			signStr.append("order_time=").append(order_time).append("&");
			signStr.append("trade_no=").append(trade_no).append("&");
			signStr.append("trade_status=").append(trade_status).append("&");
			signStr.append("trade_time=").append(trade_time);
			String signInfo = signStr.toString();
			if ("SUCCESS".equals(trade_status)) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.print("fail");
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("SUCCESS");
					// logger.info("订单已经支付成功，不能重复支付。");
					return;
				}
				// 2.验签
				boolean result = RSAWithSoftware.validateSignByPublicKey(signInfo, SHBPayment.PUBLICKEY, dinpaySign);
				if (result) {
					if (order.getAmount().intValue() != Double.valueOf(order_amount).intValue()) {
						logger.info("速汇宝支付宝订单金额不匹配。");
						out.print("fail");
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(trade_no);
					order.setPaystatus(2);
					order.setStatus(3);

					// 4.用户上分
					payPlatformService.shbPay(order);
					logger.info("速汇宝支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+" 金额："+order.getAmount());
					out.print("SUCCESS");
					return;
				} else {
					logger.info("速汇宝支付宝签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("速汇宝支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("SUCCESS");
		return;
	}
}
