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

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gameportal.pay.model.CHPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.util.DigestUtils;
import com.gameportal.pay.util.StringUtil;
import com.gameportal.pay.util.http.HttpClientUtils;
import com.gameportal.pay.util.http.MethodType;
import com.gameportal.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;

import net.sf.json.JSONObject;

/**
 * 畅汇支付。
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class CHQQPayController extends BasePayController {
	private static final Logger logger = Logger.getLogger(CHQQPayController.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;

	/**
	 * 畅汇预支付。
	 * 
	 * @param type
	 * @param vuid
	 * @param totalAmount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/chqq/{type}")
	public String prePayment(@PathVariable String type, @RequestParam(value = "hd") String hd,
			@RequestParam(value = "bankCode", required = false) String bankCode,
			@RequestParam(value = "vuid") String vuid, @RequestParam(value = "totalAmount") String totalAmount,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isNotBlank(vuid)) {
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			if (userInfo != null) {
				if (hd != null && !"".equals(hd)) {
					String result = webValidate(userInfo, hd, request);
					if (result != null) {
						return result;
					}
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("amount", totalAmount);
				params.put("hd", hd);
				params.put("pgBankCode", "");
				params.put("paFrpId", "QQ");
				PayPlatform payPlat = payPlatformService.queryPayPlatform(Long.valueOf(type), 1);
				CHPayment pay = payPlatformService.chPayment(userInfo, params, payPlat);
				Map<String, String> reParams =new HashMap<>();
				reParams.put("p0_Cmd", "Buy");
				reParams.put("p1_MerId", pay.getP1MerId());
				reParams.put("p2_Order", pay.getP2Order());
				reParams.put("p3_Cur", pay.getP3Cur());
				reParams.put("p4_Amt", ((int)pay.getP4Amt())+"");
				reParams.put("pa_FrpId", pay.getPaFrpId());
				reParams.put("p8_Url", pay.getP8Url());
				reParams.put("pi_Url", pay.getPiUrl());
				reParams.put("p9_MP", pay.getP9MP());
				reParams.put("hmac", pay.getHmac());
				HttpClientUtils httpClientUtils = new HttpClientUtils();
				String responseStr = httpClientUtils.doRequest(MethodType.POST, payPlat.getDomainname(),reParams, "UTF-8");
				logger.info("畅汇QQ请求参数："+JSONObject.fromObject(pay)+" \n"+responseStr);
				JSONObject json =JSONObject.fromObject(responseStr);
				if(json.containsKey("r3_PayInfo")){
					request.setAttribute("code_img_url", json.getString("r3_PayInfo"));
				}else{
					request.setAttribute("code_img_url", "");
				}
				request.setAttribute("amount", Double.valueOf(pay.getP4Amt()));
				request.setAttribute("orderNO", pay.getP2Order());
				return "/payment/zlqqJump";
			}
		}
		return null;
	}

	/**
	 * 畅汇回调接口
	 * 
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/chqq/callback")
	public synchronized void callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			String r0_Cmd = StringUtil.formatString(request.getParameter("r0_Cmd")); // 业务类型
			String r1_Code = StringUtil.formatString(request.getParameter("r1_Code"));// 支付结果
			String r2_TrxId = StringUtil.formatString(request.getParameter("r2_TrxId"));// 支付交易流水号
			String r3_Amt = StringUtil.formatString(request.getParameter("r3_Amt"));// 支付金额
			String r4_Cur = StringUtil.formatString(request.getParameter("r4_Cur"));// 交易币种
			String r6_Order = StringUtil.formatString(request.getParameter("r6_Order"));// 商户订单号
			String r9_BType = StringUtil.formatString(request.getParameter("r9_BType"));// 交易结果返回类型
			String ro_BankOrderId = StringUtil.formatString(request.getParameter("ro_BankOrderId")); // 银行订单号
			String rp_PayDate = StringUtil.formatString(request.getParameter("rp_PayDate")); // 支付成功时间
			String hmac = StringUtil.formatString(request.getParameter("hmac"));// 签名数据
			String result = String.format(
					"r0_Cmd=%s,r1_Code=%,sr2_TrxId=%s,r3_Amt=%s,r4_Cur=%s,r6_Order=%s,r6_Order=%s,ro_BankOrderId=%s,rp_PayDate=%s,hmac=%s",
					r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r6_Order, r9_BType, ro_BankOrderId, rp_PayDate, hmac);
			logger.info("返回参数：" + result);
			PayPlatform payPlat = payPlatformService.queryPayPlatform(1L);
			StringBuffer sValue = new StringBuffer();
			sValue.append(payPlat.getCiphercode());
			sValue.append(r0_Cmd);
			sValue.append(r1_Code);
			sValue.append(r2_TrxId);
			sValue.append(r3_Amt);
			sValue.append(r4_Cur);
			sValue.append(r6_Order);
			sValue.append(r9_BType);
			sValue.append(ro_BankOrderId);
			sValue.append(rp_PayDate);
			String sNewString = DigestUtils.hmacSign(sValue.toString(), payPlat.getPlatformkey());
			if (hmac.equals(sNewString)) {
				if (r1_Code.equals("1")) {
					PayOrder order = payPlatformService.queryPayOrderId(request.getParameter("merOrderNum"), null, null,
							"0");
					request.setAttribute("order", order);
					if (r9_BType.equals("2")) {
						if (order == null) {
							logger.info("支付订单不存在。");
							out.println("FAIL");
							out.flush();
							out.close();
							return;
						}
						if (order.getStatus() == 3) {
							out.println("SUCCESS");
							out.flush();
							out.close();
							return;
						}
						if (!order.getAmount().toString().equals(request.getParameter("r3_Amt"))) {
							logger.info("订单号[" + order.getPlatformorders() + "]金额不匹配。");
							out.println("FAIL");
							out.flush();
							out.close();
							return;
						}
						// 3.修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(r2_TrxId);
						order.setPaystatus(2);
						order.setStatus(3);
						// 4.用户上分
						payPlatformService.chPay(order);
						logger.info("充值加款成功->平台订单编号：" + order.getPlatformorders() + "->第三方订单编号：" + order.getRemarks());
						out.println("SUCCESS");
						out.flush();
						out.close();
						return;
					}
				}
			} else {
				logger.info("交易签名被篡改!");
				out.println("FAIL");
				out.flush();
				out.close();
				return;
			}
		} catch (Exception e) {
			logger.error("畅汇台回调失败。" + e.getMessage());
			out.println("FAIL");
			out.flush();
			out.close();
			return;
		}
	}

	@RequestMapping("/chqq/view")
	public String view(HttpServletRequest request, HttpServletResponse response) {
		try {
			String r0_Cmd = StringUtil.formatString(request.getParameter("r0_Cmd")); // 业务类型
			String r1_Code = StringUtil.formatString(request.getParameter("r1_Code"));// 支付结果
			String r2_TrxId = StringUtil.formatString(request.getParameter("r2_TrxId"));// 支付交易流水号
			String r3_Amt = StringUtil.formatString(request.getParameter("r3_Amt"));// 支付金额
			String r4_Cur = StringUtil.formatString(request.getParameter("r4_Cur"));// 交易币种
			String r6_Order = StringUtil.formatString(request.getParameter("r6_Order"));// 商户订单号
			String r9_BType = StringUtil.formatString(request.getParameter("r9_BType"));// 交易结果返回类型
			String ro_BankOrderId = StringUtil.formatString(request.getParameter("ro_BankOrderId")); // 银行订单号
			String rp_PayDate = StringUtil.formatString(request.getParameter("rp_PayDate")); // 支付成功时间
			String hmac = StringUtil.formatString(request.getParameter("hmac"));// 签名数据
			String result = String.format(
					"r0_Cmd=%s,r1_Code=%,sr2_TrxId=%s,r3_Amt=%s,r4_Cur=%s,r6_Order=%s,r6_Order=%s,ro_BankOrderId=%s,rp_PayDate=%s,hmac=%s",
					r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r6_Order, r9_BType, ro_BankOrderId, rp_PayDate, hmac);
			logger.info("返回参数：" + result);
			PayPlatform payPlat = payPlatformService.queryPayPlatform(1L);
			StringBuffer sValue = new StringBuffer();
			sValue.append(payPlat.getCiphercode());
			sValue.append(r0_Cmd);
			sValue.append(r1_Code);
			sValue.append(r2_TrxId);
			sValue.append(r3_Amt);
			sValue.append(r4_Cur);
			sValue.append(r6_Order);
			sValue.append(r9_BType);
			sValue.append(ro_BankOrderId);
			sValue.append(rp_PayDate);
			String sNewString = DigestUtils.hmacSign(sValue.toString(), payPlat.getPlatformkey());
			PrintWriter out = response.getWriter();
			if (hmac.equals(sNewString)) {
				if (r1_Code.equals("1")) {
					PayOrder order = payPlatformService.queryPayOrderId(request.getParameter("merOrderNum"), null, null,
							"0");
					request.setAttribute("order", order);
					if (r9_BType.equals("1")) {
						logger.info("callback方式:产品通用接口支付成功返回-浏览器重定向");
						return "/payment/payOk";
					} else if (r9_BType.equals("2")) {
						if (order == null) {
							logger.info("支付订单不存在。");
							return "/payment/payFail";
						}
						if (order.getStatus() == 3) {
							return "/payment/payOk";
						}
						if (!order.getAmount().toString().equals(request.getParameter("r3_Amt"))) {
							logger.info("订单号[" + order.getPlatformorders() + "]金额不匹配。");
							return "/payment/payFail";
						}
						// 3.修改订单状态
						order.setUpdateDate(new Date());
						Timestamp date = new Timestamp(new Date().getTime());
						order.setUpdateDate(date);
						order.setRemarks(r2_TrxId);
						order.setPaystatus(2);
						order.setStatus(3);
						// 4.用户上分
						payPlatformService.chPay(order);
						logger.info("充值加款成功->平台订单编号：" + order.getPlatformorders() + "->第三方订单编号：" + order.getRemarks());
						out.println("SUCCESS");
						out.flush();
						out.close();
					}
				}
			} else {
				logger.info("交易签名被篡改!");
				return "/payment/payFail";
			}
		} catch (Exception e) {
			logger.error("畅汇台回调失败。" + e.getMessage());
			return "/payment/payFail";
		}
		return "/payment/payOk";
	}
}
