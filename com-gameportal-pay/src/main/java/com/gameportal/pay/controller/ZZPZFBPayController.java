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
import com.gameportal.pay.model.ZZPayment;
import com.gameportal.pay.util.ClientUtills;
import com.gameportal.pay.util.HttpUtil;
import com.gameportal.pay.util.SignUtils;
import com.gameportal.pay.util.XmlUtils;
import com.gameportal.pay.util.XstreamUtil;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.thoughtworks.xstream.XStream;

/**
 * 掌灵wap支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class ZZPZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ZZPZFBPayController.class);
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
	@RequestMapping("/zzpzfb/{type}")
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
				params.put("clientIp", ClientUtills.getInstance().getIpAddr(request));
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				ZZPayment zz = payPlatformService.zzPayment(userInfo, params, payPlat, null);
				zz.setAttach(requestHost);
				zz.setSign(zz.buildSignatrue(payPlat.getPlatformkey()));
				
				logger.info("wap掌智支付宝表单参数：" + JSON.toJSONString(zz));
				try {
					XStream xstream = XstreamUtil.initXStream();
					xstream.processAnnotations(ZZPayment.class);
					String param =xstream.toXML(zz).replace("__", "_");
					System.out.println("wap掌智支付宝请求参数："+param);
					String data = HttpUtil.doPost(payPlat.getDomainname(), param, "UTF-8");
					data =data.replace("<![CDATA[", "").replace("]]>", "");
					System.out.println("wap掌智支付宝返回结果："+data);
					try {
//						String extra = data.substring(data.indexOf("<code_img_url>")+14,data.indexOf("</code_img_url>"));
//						redirect(zz, extra, writer);
						//response.addHeader("refresh", "5;url=" + data.substring(data.indexOf("<code_url>")+10,data.indexOf("</code_url>")));
						response.addHeader("refresh", "5;url=" + data.substring(data.indexOf("<code_img_url>")+14,data.indexOf("</code_img_url>")));
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
					logger.error("掌智wap获取二维码失败");
					e.printStackTrace();
				}
			}
		}
	}

	private void redirect(ZZPayment zz, String code_img_url, PrintWriter writer) {
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
		out += "<span>￥</span>" + (Double.valueOf(zz.getTotalFee()) / 100) + "</div>";
		out += "<div class=\"qr-image\" id=\"showqrcode\">";
		out += "<img  src='"+code_img_url+"' width=\"280px\" height=\"260px\" style=\"margin-left:10px; margin-top:20px\" />";
		out += "</div>";
		out += "<div class=\"detail detail-open\" id=\"orderDetail\">";
		out += "<dl class=\"detail-ct\" style=\"display: block;\">";
		out += "<dt>交易单号</dt>";
		out += "<dd id=\"billId\">" + zz.getOutTradeNo() + "</dd>";
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
	 * 掌灵支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zzpzfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			String resString = XmlUtils.parseRequst(request);
	    	System.out.println("掌智web支付宝异步回调参数："+resString);
	    	PayPlatform payPlat = payPlatformService.queryPayPlatform(207L);
	        if(resString != null && !"".equals(resString)){
	            Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
	            String res = XmlUtils.toXml(map);
	            System.out.println("通知内容：" + res);
	            String merchParam = map.get("attach");
	            if(map.containsKey("sign")){
	                if(!SignUtils.checkParam(map, payPlat.getPlatformkey())){
	                	logger.info("掌智支付宝签名验证失败。");
						response.addHeader("refresh", "3;url=http://" + merchParam);
						out.write("签名验证失败!即將返回...");
						out.flush();
						out.close();
						return;
	                }else{
	                    String status = map.get("status");
	                    if(status != null && "0".equals(status)){
	                        String result_code = map.get("result_code");
	                        if(result_code != null && "0".equals(result_code)){
	                        	PayOrder order = payPlatformService.queryPayOrderId(map.get("out_trade_no"), null, null, "0");
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
	        					if (order.getAmount().intValue() !=Double.valueOf(map.get("total_fee")).intValue()/100) { //单位分
	        						logger.info("掌智wap支付宝订单金额不匹配。");
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
	        					order.setRemarks(map.get("out_transaction_id"));
	        					order.setPaystatus(2);
	
	        					// 4.用户上分
	        					payPlatformService.zzPay(order);
	        					logger.info("掌智web支付宝充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
	        					out.print("success");
	        					response.addHeader("refresh", "3;url=http://" + merchParam);
	        					out.flush();
	        					out.close();
	        					return;
	                        } 
	                    } 
	                }
	            }
	        }
		} catch (Exception e) {
			logger.error("掌智wap支付宝支付后台回调失败。", e);
			out.print("fail");
			out.flush();
			out.close();
			return;
		}
		out.print("success");
		return;
	}
	
	public static void main(String[] args) throws Exception {
		String str="<xml><attach><![CDATA[slot.qianbaobet.com]]></attach>"
+"<bank_type><![CDATA[]]></bank_type>"
+"<charset><![CDATA[UTF-8]]></charset>"
+"<fee_type><![CDATA[CNY]]></fee_type>"
+"<mch_id><![CDATA[841705959888]]></mch_id>"
+"<nonce_str><![CDATA[20306385141186802514]]></nonce_str>"
+"<openid><![CDATA[openidnull]]></openid>"
+"<out_trade_no><![CDATA[2017051817505944]]></out_trade_no>"
+"<out_transaction_id><![CDATA[20170518175118667595962748030]]></out_transaction_id>"
+"<pay_result><![CDATA[0]]></pay_result>"
+"<result_code><![CDATA[0]]></result_code>"
+"<sign><![CDATA[96DC81C12D0BB901FD4FE9CD2C6AA736]]></sign>"
+"<sign_type><![CDATA[MD5]]></sign_type>"
+"<status><![CDATA[0]]></status>"
+"<time_end><![CDATA[1495101175]]></time_end>"
+"<total_fee><![CDATA[20000]]></total_fee>"
+"<trade_type><![CDATA[pay.alipay.native]]></trade_type>"
+"<transaction_id><![CDATA[20170518175118667595962748030]]></transaction_id>"
+"<version><![CDATA[2.0]]></version>"
+"</xml>";
		
		 Map<String,String> map = XmlUtils.toMap(str.getBytes(), "utf-8");
         String res = XmlUtils.toXml(map);
         System.out.println(map);
		 System.out.println(SignUtils.checkParam(map, "5f9e87996cef4ff0a5b0e46383dd197b"));
		
	}
}
