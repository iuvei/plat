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
import com.gameportal.pay.model.YBPayment;
import com.gameportal.pay.util.SFMD5Util;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

/**
 * mobile支付宝扫码
 *
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/pay")
public class ZFBPPayController extends BasePayController {
    private static final Logger logger = Logger.getLogger(ZFBPPayController.class);
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService;

    /**
     * 微信支付。
     *
     * @param type
     * @param vuid
     * @param totalAmount
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/zfp/{type}")
    @ResponseBody
    public void prePayment(@PathVariable String type,
                           @RequestParam(value = "hd") String hd,
                           @RequestParam(value = "vuid") String vuid,
                           @RequestParam(value = "totalAmount") String totalAmount,
                           @RequestParam(value = "requestHost") String requestHost,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {
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
				YBPayment yb = payPlatformService.ybPayment(userInfo, params, payPlat);
				yb.setAttach(requestHost);
                try {
                    redirect(yb, payPlat, writer);
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
    }

    private void redirect(YBPayment yb,PayPlatform payPlat,PrintWriter writer) {
		String out ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		out +="<head>";
		out +="<meta http-equiv=\"Content-Type\" content=\"hidden/html; charset=utf-8\" />";
		out +="<title>跳转中......</title>";
		out +="</head>";
		out +="<body style=\"background:url(/images/pay_loading.gif) no-repeat fixed center;\">";
		out +="<form action='"+payPlat.getDomainname() +"' method=\"GET\" id=\"frm7\"><br>";
		out +="<input name='partner' type='hidden' value='"+yb.getPartner()+"' /><br>";
		out +="<input name='banktype' type='hidden' value='"+yb.getBanktype()+"'/><br>";
		out +="<input name='paymoney' type='hidden' value='"+yb.getPaymoney()+"'/><br>";
		out +="<input name='ordernumber' type='hidden' value='"+yb.getOrdernumber()+"'/><br>";
		out +="<input name='callbackurl' type='hidden' value='"+yb.getCallbackurl()+"'/><br>";
		out +="<input name='hrefbackurl' type='hidden' value='"+yb.getHrefbackurl()+"' /><br>";
		out +="<input name='attach' type='hidden' value='"+yb.getAttach()+"' /><br>";
		out +="<input name='sign' type='hidden' value='"+yb.getSign()+"'/><br>";
		out +="<script language=\"javascript\">";
		out +="document.getElementById(\"frm7\").submit();";
		out +="</script>";
		out +="</form>";
		out +="</body>";
		out +="</html>";
		writer.write(out);
		writer.flush();
		writer.close();
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
    @RequestMapping("/zfp/callback")
    @ResponseBody
    public void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=GBK");
        response.setCharacterEncoding("GBK");
        PrintWriter out = response.getWriter();
        try {
        	String partner = request.getParameter("partner");
			String ordernumber = request.getParameter("ordernumber");
			String orderstatus = request.getParameter("orderstatus"); //1:支付成功，非1为支付失败
			String paymoney = request.getParameter("paymoney");
			String sysnumber = request.getParameter("sysnumber");
			String sign = request.getParameter("sign");
			String merchParam = request.getParameter("attach");
			String srcMsg = String.format(
					"partner=%s&ordernumber=%s&orderstatus=%s&paymoney=%s",
					partner, ordernumber, orderstatus, paymoney);
			PayOrder order = payPlatformService.queryPayOrderId(ordernumber, null, null, "0");
			if (orderstatus.equals("1")) {
                // 1.判断订单是否已经支付成功。
                if (order == null) {
                    logger.info("支付订单不存在。");
                    response.addHeader("refresh", "3;url=http://" + merchParam);
                    out.write("支付订单不存在!即將返回...");
                    out.flush();
                    out.close();
                    return;
                }
                if (order.getStatus() == 3) {
                	// 返回支付成功
                	logger.info("wap支付宝后台回调信息：" + srcMsg);
					response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("订单已经支付成功!即將返回...");
					out.flush();
					out.close();
					return;
                }
                PayPlatform payPlat = payPlatformService.queryPayPlatform(24L);
				if (sign.equalsIgnoreCase(SFMD5Util.encryption(srcMsg+payPlat.getPlatformkey()))) {
					if (order.getAmount().intValue() != Double.valueOf(paymoney).intValue()) {
                        logger.info("wap支付宝订单金额不匹配。");
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
					order.setRemarks(sysnumber);
					order.setPaystatus(2);

                    // 4.用户上分
                    payPlatformService.ybPay(order);
                    logger.info("wap支付宝扫码充值加款成功->平台订单编号：" + order.getPlatformorders()+"金额："+order.getAmount());
                	response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("充值成功!即將返回...");
					out.flush();
					out.close();
					return;
                } else {
                	response.addHeader("refresh", "3;url=http://" + merchParam);
					out.write("签名验证失败!即將返回...");
					out.flush();
					out.close();
					return;
                }
            }
        } catch (Exception e) {
            logger.error("wap支付宝回调失败。", e);
            response.addHeader("refresh", "3;url=http://" + request.getParameter("merchParam"));
			out.write("第三方后台回调失败!即將返回...");
			out.flush();
			out.close();
			return;
        }
    }

    @RequestMapping("/zfp/view")
    public void view(String ordernumber, String attach, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=GBK");
        response.setCharacterEncoding("GBK");
        PayOrder order = payPlatformService.queryPayOrderId(ordernumber, null, null, "0");
        logger.info("billno=" + ordernumber + ",backUrl=" + attach);
        PrintWriter writer = response.getWriter();
        if (order != null && order.getStatus() == 3) {
            response.addHeader("refresh", "3;url=http://" + attach);
            writer.write("充值成功!即將返回...");
            writer.flush();
            writer.close();
        }
        response.addHeader("refresh", "3;url=http://" + attach);
        writer.write("充值失败!即將返回...");
        writer.flush();
        writer.close();
    }
}
