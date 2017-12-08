package com.gameportal.pay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gameportal.pay.model.Activity;
import com.gameportal.pay.model.ActivityFlag;
import com.gameportal.pay.model.BaoFooPayment;
import com.gameportal.pay.model.CHPayment;
import com.gameportal.pay.model.GstPayment;
import com.gameportal.pay.model.IPSPayment;
import com.gameportal.pay.model.LePayment;
import com.gameportal.pay.model.MoBoPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayPlatform;
import com.gameportal.pay.model.RFPayment;
import com.gameportal.pay.model.SHBPayment;
import com.gameportal.pay.model.ThPayment;
import com.gameportal.pay.model.UnitedPayment;
import com.gameportal.pay.model.UserManager;
import com.gameportal.pay.model.XTCPayment;
import com.gameportal.pay.model.YBPayment;
import com.gameportal.pay.model.YPayment;
import com.gameportal.pay.model.ZLPayment;
import com.gameportal.pay.model.ZSPayment;
import com.gameportal.pay.model.ZZPayment;
import com.gameportal.pay.model.fcs.request.FCSOpenApiAliPayRequest;
import com.gameportal.pay.model.fcs.request.FCSOpenApiWxPayRequest;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.UserInfo;

/**
 * @ClassName: IPayPlatformService
 * @Description: (支付平台配置接口)
 * @date 2015年4月12日 下午1:16:38
 */
public interface IPayPlatformService {
	public abstract PayPlatform queryPayPlatform(Long ppid);

	public abstract PayPlatform queryPayPlatform(Long ppid, Integer status);

	public abstract PayOrder queryPayOrderId(String orderId, Integer paystatus,
			Integer status,String paytyple);
	
	public abstract PayOrder queryPayOrder(Map<String, Object> param);

	public abstract PayOrder savePayOrder(PayOrder payOrder) throws Exception;

	public abstract boolean prepaidMoney(String orderId, String code)
			throws Exception;

	public AccountMoney queryAccountMoney(Long uuid, Integer status);

	public AccountMoney saveAccountMoney(AccountMoney accountMoney);

	public boolean accumulationAccountMoney(Long uuid, BigDecimal money);

	public abstract boolean modifyPayOrder(PayOrder payOrder) throws Exception;
	
	public Long getCount(Map<String, Object> params);
	
	/**
	 * 环迅预支付。
	 * 
	 * @param user
	 *            下单用户
	 * @param params
	 * 			  其他参数
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	IPSPayment hxPrePayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 中联预支付。
	 * 
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	UnitedPayment unitedPrePayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 快捷支付宝预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	FCSOpenApiAliPayRequest jfaliPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 快捷支付宝微信预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	FCSOpenApiWxPayRequest jfwxPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 畅汇预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	CHPayment chPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 宝付预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	BaoFooPayment baoFooPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 通汇预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	ThPayment thPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 摩宝微信扫码支付
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	MoBoPayment mbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	
	/**
	 * 银宝微信扫码支付
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	YBPayment ybPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 锐付微信预支付。
	 * @param user
	 *            下单用户
	 * @param amount
	 *            金额
	 * @param payPlat
	 *            支付平台
	 * @return 第三方订单。
	 */
	RFPayment rfPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat);
	
	/**
	 * 国盛通
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	GstPayment gstPayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	/**
	 * 新天诚
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	XTCPayment xtcPayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	
	/**
	 * 掌灵
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	ZLPayment zlPayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	/**
	 * lepay
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	LePayment lePayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	/**
	 * 掌智
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	ZZPayment zzPayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	
	/**
	 * 优付
	 * @param user
	 * @param params
	 * @param payPlat
	 * @return
	 */
	YPayment ypPayment(UserInfo user,Map<String,Object> params,PayPlatform payPlat,String channel);
	
	
	/**
	 * 速汇宝
	 * @param user
	 * @param params
	 * @param payPlat
	 * @param channel
	 * @return
	 */
	SHBPayment shbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel);
	
	/**
	 * 速汇宝
	 * @param user
	 * @param params
	 * @param payPlat
	 * @param channel
	 * @return
	 */
	ThPayment hbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel);
	
	/**
	 * 泽圣
	 * @param user
	 * @param params
	 * @param payPlat
	 * @param channel
	 * @return
	 */
	ZSPayment zsPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel);
	
	/**
	 * 第三方支付。
	 * 
	 * @param order
	 */
	void otherPay(PayOrder order);
	
	void thwPay(PayOrder order);
	
	void thPay(PayOrder order);
	
	void mbPay(PayOrder order);
	
	void mbwPay(PayOrder order);
	
	void chPay(PayOrder order);
	
	void jfpPay(PayOrder order);
	
	void ybPay(PayOrder order);
	
	void zfbPay(PayOrder order);
	
	void rfPay(PayOrder order);
	
	void gstPay(PayOrder order);
	
	void xtcPay(PayOrder order);
	
	void zlPay(PayOrder order);
	
	void lpPay(PayOrder order);
	
	void zzPay(PayOrder order);
	
	void ypPay(PayOrder order);
	
	void shbPay(PayOrder order);
	
	void hbPay(PayOrder order);
	
	void zsPay(PayOrder order);
		
	public boolean updateUManager(UserManager entity);
	
	public UserManager getUserManager(Map<String, Object> params);
	
	
	/**
	 * 查询活动
	 * @param params
	 * @return
	 */
	public List<Activity> queryActivity(Map<String, Object> params);
	
	/**
	 * 查询活动标识
	 * @param params
	 * @return
	 */
	public List<ActivityFlag> queryActivityFlag(Map<String, Object> params);
}
