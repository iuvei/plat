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
 * 优付支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class YPPZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(YPPZFBPayController.class);
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
	@RequestMapping("/yppzfb/{type}")
	@ResponseBody
	public void prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			@RequestParam(value = "requestHost") String requestHost, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String key = vuid + "GAMEPORTAL_USER";
		Class<Object> c = null;
		UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		PrintWriter writer = response.getWriter();
		if (StringUtils.isNotBlank(vuid)) {
			if (userInfo != null) {
				String result = webValidate(userInfo, hd, request);
				if (hd != null && !"".equals(hd)) {
					wapValidate(userInfo, hd, requestHost, request, response);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				YPayment yp = payPlatformService.ypPayment(userInfo, params, payPlat, null);
				System.out.println("优付请求参数"+JSON.toJSONString(yp));
				result = new String(HttpUtil.doPost(payPlat.getDomainname(),yp.getMap(payPlat.getPlatformkey())),"utf-8");
				System.out.println("优付支付宝返回結果"+result);
				JSONObject json =JSON.parseObject(result);
				try {
					redirect(yp, json.getString("msg"), writer);
//					response.addHeader("refresh", "5;url=http://" + json.getString("msg"));
					writer.flush();
					response.flushBuffer();
					writer.close();
					return;
			} catch (Exception e) {
				logger.error("掌智wap获取二维码失败");
				response.addHeader("refresh", "5;url=http://" + requestHost);
				writer.write("发生错误，即将返回...");
				writer.flush();
				response.flushBuffer();
				writer.close();
				e.printStackTrace();
			}
			}
		}				
	}
	
	private void redirect(YPayment yp, String code_img_url, PrintWriter writer) {
		String out = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		out += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out += "<head>";
		out += "<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out += "<title>支付宝扫码支付</title>";
		out += "<link href=\"/css/zf_style.css\" type=\"text/css\" rel=\"stylesheet\" />";
		out += "<link href=\"/css/wechat_pay.css\" rel=\"stylesheet\" />";
		out += "</head>";
		out += "<div class=\"body\">";
		out += "<h1 class=\"mod-title\">";
		out += "<span class=\"ico-wechat\"></span><span class=\"text\">支付宝支付</span>";
		out += "</h1>";
		out += "<div class=\"mod-ct\">";
		out += "<div class=\"order\">";
		out += "</div>";
		out += "<div class=\"amount\">";
		out += "<span>￥</span>" + (Integer.valueOf(yp.getFee())/100) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src='"+code_img_url+"' width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + yp.getOrderid() + "</dd>";
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
		writer.write(out);
		writer.flush();
		writer.close();
	}

	/**
	 * 优付支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/yppzfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		//orderid=2017051320101404&result=1&fee=1000&paytype=31&tradetime=1494677415&cpparam=&sign=cf52700ae020729f028cf031abd12d76
		String message =request.getQueryString();
		System.out.println("优付web支付宝异步回调参数："+message);
		try {
			Map<String, String> map = URLUtils.getMap(message);
			PayPlatform payPlat = payPlatformService.queryPayPlatform(223L);
			PayOrder order = payPlatformService.queryPayOrderId(map.get("orderid"), null, null, "0");
			if (map.get("result").equals("1")) {
				// 1.判断订单是否已经支付成功。
				if (order == null) {
					logger.info("支付订单不存在。");
					out.write("支付订单不存在!");
					out.flush();
					out.close();
					return; // 支付订单不存在
				}
				if (order.getStatus() == 3) {
					// 返回支付成功
					out.print("OK");
					out.flush();
					out.close();
					return;
				}
				// 2.验签
				String bigStr = MD5Util.signByMD5(map.get("orderid")+map.get("result")+map.get("fee")+map.get("tradetime"), payPlat.getPlatformkey()).toLowerCase();
				logger.info("优付web后台支付宝回调参数：" + bigStr);
				if (bigStr.equals(map.get("sign"))) {
					if (order.getAmount().intValue() !=Double.valueOf(map.get("fee")).intValue()/100) { //单位分
						logger.info("优付web支付宝订单金额不匹配。");
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
					logger.info("优付web支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
					out.print("OK");
					return;
				} else {
					logger.info("优付web支付宝签名验证失败。");
					out.print("fail");
					return;
				}
			}
		} catch (Exception e) {
			logger.error("优付web支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("OK");
		return;
	}
}
