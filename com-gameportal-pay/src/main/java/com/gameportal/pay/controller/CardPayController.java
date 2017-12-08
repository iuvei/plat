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
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.XTCPayment;
import com.gameportal.pay.util.MD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * 点卡支付
 * @author jade
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class CardPayController extends BasePayController{
	private static final Logger logger = Logger.getLogger(CardPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 点卡预支付。
	 *
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/xtcdk/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "cardNo") String cardNo,@RequestParam(value = "cardpwd") String cardpwd,
			@RequestParam(value = "cardType") String cardType,
			@RequestParam(value = "channel", required = false) String channel, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter writer = response.getWriter();
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
				params.put("cardType", cardType);
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				XTCPayment xtc = payPlatformService.xtcPayment(userInfo, params, payPlat, channel);
				xtc.setCardno(cardNo);
				xtc.setCardpwd(cardpwd);
				xtc.setCardtype(cardType);
				xtc.setSign(xtc.buildDKSignature(payPlat.getPlatformkey()));
				logger.info("新天诚WEB点卡请求参数:"+JSON.toJSONString(xtc));
				try {
					redirect(xtc, payPlat, writer);
					writer.flush();
					response.flushBuffer();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("msg", "发生错误，即将返回...!");
					return "/payment/error";
				}
			}
		}
		return null;
	}
	
	private void redirect(XTCPayment xtc, PayPlatform payPlat, PrintWriter writer) {
		String out = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out += "<head>";
		out += "<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out += "<title>跳转中......</title>";
		out += "</head>";
		out += "<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out += "<form action='" + payPlat.getDomainname() + "' method=\"GET\" id=\"frm7\"><br>";
		out += "<input name='partner' type='hidden' value='" + xtc.getParter() + "' /><br>";
		out += "<input name='cardtype' type='hidden' value='" + xtc.getCardtype() + "'/><br>";
		out += "<input name='cardno' type='hidden' value='" + xtc.getCardno() + "'/><br>";
		out += "<input name='cardpwd' type='hidden' value='" + xtc.getCardpwd() + "'/><br>";
		out += "<input name='paymoney' type='hidden' value='" + xtc.getPaymoney() + "'/><br>";
		out += "<input name='ordernumber' type='hidden' value='" + xtc.getOrdernumber() + "'/><br>";
		out += "<input name='callbackurl' type='hidden' value='" + xtc.getCallbackurl() + "'/><br>";
		out += "<input name='hrefbackurl' type='hidden' value='" + xtc.getHrefbackurl() + "' /><br>";
		out += "<input name='attach' type='hidden' value='" + xtc.getCardtype() + "' /><br>";
		out += "<input name='sign' type='hidden' value='" + xtc.getSign() + "' /><br>";
		out += "<script language=\"javascript\">";
		out += "document.getElementById(\"frm7\").submit();";
		out += "</script>";
		out += "</form>";
		out += "</body>";
		out += "</html>";
		writer.write(out);
		writer.flush();
		writer.close();
	}


	/**
	 * 点卡支付回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/xtcdk/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String partner = request.getParameter("partner");
			String ordernumber = request.getParameter("ordernumber");
			String orderstatus = request.getParameter("orderstatus");
			String paymoney = request.getParameter("paymoney");
			String sysnumber = request.getParameter("sysnumber");
			String sign = request.getParameter("sign");
			
			PayPlatform payPlat = payPlatformService.queryPayPlatform(100L);
			String signature = String.format("partner=%s&ordernumber=%s&orderstatus=%s&paymoney=%s", partner,
					ordernumber, orderstatus, paymoney + payPlat.getPlatformkey());

			logger.info("新天誠点卡支付后台回调信息：" + signature);
			signature = MD5Util.getMD5Encode(signature);
	     
			PayOrder order = payPlatformService.queryPayOrderId(ordernumber, null, null, "0");
			if (orderstatus.equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
                    out.write("fail");
                    out.flush();
                    out.close();
                    return;
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.write("ok");
					out.flush();
					out.close();
					logger.info("新天誠点卡支付后台回调信息：orderNo=" + ordernumber +",orderstatus=" + orderstatus);
					return;
				}
				// 2.验签
				if (signature.equalsIgnoreCase(sign)) {
					if (order.getAmount().intValue() != Double.valueOf(paymoney).intValue()) {
						logger.info("新天誠点卡支付扫码订单金额不匹配。");
                        out.write("fail");
                        out.flush();
                        out.close();
						return;
					}
					// 3.修改订单状态
					order.setUpdateDate(new Date());
					Timestamp date = new Timestamp(new Date().getTime());
					order.setUpdateDate(date);
					order.setRemarks(sysnumber);
					order.setPaystatus(2);

					// 4.用户上分
					payPlatformService.xtcPay(order);
					logger.info("新天誠点卡支付扫码充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					
					out.write("ok");
					out.flush();
					out.close();
					return;
				} else {
					logger.error("新天誠点卡支付扫码签名验证失败。");
					out.write("fail");
					out.flush();
					out.close();
					return;
				}
			}
		} catch (Exception e) {
			logger.error("新天誠点卡支付扫码后台回调失败。", e);
			out.print("fail");
			out.flush();
			out.close();
			return;
		}
		return;
	}
}
