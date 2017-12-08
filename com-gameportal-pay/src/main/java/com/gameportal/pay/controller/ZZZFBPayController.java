package com.gameportal.pay.controller;

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
 * 掌智支付宝
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class ZZZFBPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(ZZZFBPayController.class);
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
	 * @throws Exception 
	 */
	@RequestMapping("/zzzfb/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
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
				ZZPayment zz = payPlatformService.zzPayment(userInfo, params, payPlat, null);
				XStream xstream = XstreamUtil.initXStream();
				xstream.processAnnotations(ZZPayment.class);
				String param =xstream.toXML(zz).replace("__", "_");
				System.out.println("掌智请求参数："+param);
				String data = HttpUtil.doPost(payPlat.getDomainname(), param, "UTF-8");
				data =data.replace("<![CDATA[", "").replace("]]>", "");
				System.out.println("掌智返回结果："+data);
				String imgUrl =data.substring(data.indexOf("<code_img_url>")+14,data.indexOf("</code_img_url>"));
				System.out.println(imgUrl);
				request.setAttribute("code_img_url",imgUrl);
				request.setAttribute("orderNO", zz.getOutTradeNo());
				request.setAttribute("amount", zz.getTotalFee()/100);
				//data.substring(data.indexOf("<code_url>")+10,data.indexOf("</code_url>"))
				response.sendRedirect(imgUrl);
//				return "/payment/zhifubao";
			}
		}
		return null;
	}
	

	/**
	 * 掌智支付宝回调接口
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/zzzfb/callback")
	@ResponseBody
	public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String resString = XmlUtils.parseRequst(request);
	    	System.out.println("掌智web支付宝异步回调参数："+resString);
	    	PayPlatform payPlat = payPlatformService.queryPayPlatform(207L);
	        if(resString != null && !"".equals(resString)){
	            Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
	            String res = XmlUtils.toXml(map);
	            System.out.println("通知内容：" + res);
	            if(map.containsKey("sign")){
	                if(!SignUtils.checkParam(map, payPlat.getPlatformkey())){
	                	out.print("fail");
						return; // 支付订单不存在
	                }else{
	                    String status = map.get("status");
	                    if(status != null && "0".equals(status)){
	                        String result_code = map.get("result_code");
	                        if(result_code != null && "0".equals(result_code)){
	                        	PayOrder order = payPlatformService.queryPayOrderId(map.get("out_trade_no"), null, null, "0");
	            				// 1.判断订单是否已经支付成功。
	            				if (order == null) {
	            					logger.info("支付订单不存在。");
	            					out.print("fail");
	            					return; // 支付订单不存在
	            				}
	            				if (order.getStatus() == 3) {
	            					// 返回支付成功
	            					out.print("success");
	            					return;
	            				}
	        					if (order.getAmount().intValue() !=Double.valueOf(map.get("total_fee")).intValue()/100) { //单位分
	        						logger.info("掌智web支付宝订单金额不匹配。");
	        						out.print("fail");
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
	        					return;
	                        } 
	                    } 
	                }
	            }
	        }
		} catch (Exception e) {
			logger.error("掌智web支付宝支付后台回调失败。", e);
			out.print("fail");
			return;
		}
		out.print("success");
		return;
	}
}
