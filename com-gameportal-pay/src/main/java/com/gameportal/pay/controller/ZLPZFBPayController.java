package com.gameportal.pay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.alibaba.fastjson.TypeReference;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.ZLPayment;
import com.gameportal.pay.util.HttpClientHelper;
import com.gameportal.pay.util.HttpResponse;
import com.gameportal.pay.util.MyRSAUtils;
import com.gameportal.pay.util.SignatureUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 掌灵wap支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class ZLPZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ZLPZFBPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 掌灵wap预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zlpzfb/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost, HttpServletRequest request,
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
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				ZLPayment zl = payPlatformService.zlPayment(userInfo, params, payPlat, null);
				zl.setExtra(requestHost);
				logger.info("掌灵表单参数：" + JSON.toJSONString(zl));
				try {
					Map<String, Object> dataMap = zl.fqrpay(payPlat.getPlatformkey());
					logger.info("掌灵提交参数：" + dataMap.toString());
					String postData = JSON.toJSONString(dataMap);
					List<String[]> headers = new ArrayList<>();
					headers.add(new String[] { "Content-Type", "application/json" });
					HttpResponse resData = HttpClientHelper.doHttp(payPlat.getDomainname(), HttpClientHelper.POST,
							headers, "utf-8", postData, "60000");
					logger.info("掌灵wap支付宝返回参数:" + JSON.toJSONString(resData));
					if (StringUtils.isNotEmpty(resData.getRspStr())) {
						Map<String, String> retMap = JSON.parseObject(resData.getRspStr(),
								new TypeReference<Map<String, String>>() {
								});
						if ("200".equals(retMap.get("respCode"))) {
							try {
								String extra = retMap.get("qrcode");
								redirect(zl, extra, writer);
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
						}
					}
				} catch (Exception e) {
					logger.error("掌灵wap获取二维码失败");
					e.printStackTrace();
				}
			}
		}
	}

	private void redirect(ZLPayment zl, String code_img_url, PrintWriter writer) {
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
		out += "<span>￥</span>" + (Double.valueOf(zl.getAmount()) / 100) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src=\"\" width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + zl.getOrgOrderNo() + "</dd>";
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
	 * 掌灵支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zlpzfb/callback")
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
			System.out.println("掌灵wap支付宝异步回调参数：" + message);
			JSONObject data = JSONObject.parseObject(message);
			String merchParam = data.getString("extra");
			PayOrder order = payPlatformService.queryPayOrderId(data.getString("orgOrderNo"), null, null, "0");
			if (data.getString("paySt").equals("2")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("支付订单不存在!即將返回...");
					out.flush();
					out.close();
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					json.put("success", "true");
					out.print(json.toJSONString());
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.flush();
					out.close();
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
				logger.info("掌灵wap后台支付宝回调参数：" + bigStr);
				if (MyRSAUtils.verifySignature(ZLPayment.PUBLICKEY, data.getString("signature"), bigStr,
						MyRSAUtils.MD5_SIGN_ALGORITHM)) {
					if (order.getAmount().intValue() != Double.valueOf(data.getString("amount")).intValue() / 100) { // 单位分
						logger.info("掌灵wap支付宝订单金额不匹配。");
						response.addHeader("refresh", "3;url=http://" + merchParam);
						out.write("订单金额不匹配!即將返回...");
						out.flush();
						out.close();
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(data.getString("orderNo"));
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.zlPay(order);
					logger.info("掌灵wap支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					json.put("success", "true");
					out.print(json.toJSONString());
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.flush();
					out.close();
					out.print(json.toJSONString());
					return;
				} else {
					logger.info("掌灵支付宝签名验证失败。");
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("签名验证失败!即將返回...");
					out.flush();
					out.close();
					return;
				}
			}
		} catch (Exception e) {
			logger.error("掌灵wap支付宝支付后台回调失败。", e);
			response.addHeader("refresh", "3;url=http://" + request.getParameter("merchParam"));
			out.write("第三方后台回调失败!即將返回...");
			out.flush();
			out.close();
			return;
		}
		json.put("success", "true");
		out.print(json.toJSONString());
		return;
	}
}
