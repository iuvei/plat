package com.gameportal.pay.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.experimental.theories.PotentialAssignment;
import org.springframework.stereotype.Service;

import com.gameportal.pay.dao.ActivityDao;
import com.gameportal.pay.dao.PayOrderDao;
import com.gameportal.pay.dao.PayPlatformDao;
import com.gameportal.pay.dao.XimaFlagDao;
import com.gameportal.pay.model.Activity;
import com.gameportal.pay.model.ActivityFlag;
import com.gameportal.pay.model.BaoFooPayment;
import com.gameportal.pay.model.CHPayment;
import com.gameportal.pay.model.GoPayment;
import com.gameportal.pay.model.GstPayment;
import com.gameportal.pay.model.IPSPayment;
import com.gameportal.pay.model.LePayment;
import com.gameportal.pay.model.MoBoPayment;
import com.gameportal.pay.model.PayOrder;
import com.gameportal.pay.model.PayOrderLog;
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
import com.gameportal.pay.service.IPayPlatformService;
import com.gameportal.pay.util.AES;
import com.gameportal.pay.util.DateUtil;
import com.gameportal.pay.util.IdGenerator;
import com.gameportal.pay.util.RandomUtil;
import com.gameportal.web.user.dao.AccountMoneyDao;
import com.gameportal.web.user.dao.UserInfoDao;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.XimaFlag;

@Service("payPlatformServiceImpl")
public class PayPlatformServiceImpl implements IPayPlatformService {
	private final static Logger logger = Logger.getLogger(PayPlatformServiceImpl.class);
	@Resource(name = "ximaFlagDao")
	private XimaFlagDao ximaFlagDao;
	@Resource(name = "payPlatformDao")
	private PayPlatformDao payPlatformDao = null;
	@Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao = null;
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao = null;
	@Resource(name = "activityDao")
	private ActivityDao activityDao;
	@Resource(name="userInfoDao")
	private UserInfoDao userInfoDao;

	public PayPlatformServiceImpl() {
		super();
	}

	@Override
	public PayPlatform queryPayPlatform(Long ppid) {
		return this.queryPayPlatform(ppid, 1);
	}

	@Override
	public PayPlatform queryPayPlatform(Long ppid, Integer status) {
		Map param = new HashMap(2);
		param.put("ppid", ppid);
//		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
//			param.put("status", status);
//		}
		PayPlatform payPlatform = (PayPlatform) payPlatformDao.queryForObject(payPlatformDao.getSelectQuery(), param);
		return StringUtils.isNotBlank(ObjectUtils.toString(payPlatform)) ? payPlatform : null;
	}

	@Override
	public PayOrder savePayOrder(PayOrder payOrder) throws Exception {
		payOrder = (PayOrder) payOrderDao.save(payOrder);
		return StringUtils.isNotBlank(ObjectUtils.toString(payOrder.getPoid())) ? payOrder : null;
	}

	@Override
	public PayOrder queryPayOrderId(String orderId, Integer paystatus, Integer status, String paytyple) {
		Map param = new HashMap();
		param.put("platformorders", orderId);
		if (StringUtils.isNotBlank(ObjectUtils.toString(paystatus))) {
			param.put("paystatus", paystatus);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			param.put("status", status);
		}
		if (StringUtils.isNotEmpty(paytyple)) {
			param.put("paytyple", paytyple);
		}
		List<PayOrder> payOrders = payOrderDao.queryForPager(payOrderDao.getSelectQuery(), param, 0, 0);
		if (CollectionUtils.isNotEmpty(payOrders)) {
			return payOrders.get(0);
		}
		return null;
	}

	@Override
	public PayOrder queryPayOrder(Map<String, Object> param) {
		List<PayOrder> payOrders = payOrderDao.queryForPager(payOrderDao.getSelectQuery(), param, 0, 0);
		if (CollectionUtils.isNotEmpty(payOrders)) {
			return payOrders.get(0);
		}
		return null;
	}

	@Override
	public boolean prepaidMoney(String orderId, String code) throws Exception {
		PayOrder payOrder = queryPayOrderId(orderId, null, null, "0");
		return false;
	}

	@Override
	public boolean accumulationAccountMoney(Long uuid, BigDecimal money) {
		AccountMoney accountMoney = queryAccountMoney(uuid, null);
//		accountMoney.setTotalamount(accountMoney.getTotalamount() + money);
		accountMoney.setTotalamount(money);
		accountMoney.setUpdateDate(new Date());
		return accountMoneyDao.updateTotalAmount(accountMoney);
	}

	@Override
	public boolean modifyPayOrder(PayOrder payOrder) throws Exception {
		return payOrderDao.update(payOrder);
	}

	@Override
	public AccountMoney queryAccountMoney(Long uuid, Integer status) {
		Map param = new HashMap(2);
		param.put("uiid", uuid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			param.put("status", status);
		}
		AccountMoney accountMoney = (AccountMoney) accountMoneyDao.queryForObject(accountMoneyDao.getSelectQuery(),
				param);
		return StringUtils.isNotBlank(ObjectUtils.toString(accountMoney)) ? accountMoney : null;
	}

	@Override
	public AccountMoney saveAccountMoney(AccountMoney accountMoney) {
		accountMoney = (AccountMoney) accountMoneyDao.save(accountMoney);
		return StringUtils.isNotBlank(ObjectUtils.toString(accountMoney.getAmid())) ? accountMoney : null;
	}

	@Override
	public IPSPayment hxPrePayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("HXO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setPpid(2L);
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			IPSPayment ips = new IPSPayment();
			ips.setMerCode(payPlat.getCiphercode()); // 031645
			ips.setBillNo(order.getPlatformorders());
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
			ips.setAmount(numberFormat.format(Double.parseDouble(params.get("amount").toString())));
			ips.setMerchanturl(payPlat.getNoticeUrl());
			ips.setFailUrl(payPlat.getReturnUrl() + "/pay/payment/view.do");
			ips.setServerUrl(payPlat.getNoticeUrl());
			ips.setSignMD5(ips.getSign(payPlat.getPlatformkey()));

			return ips;
		} catch (Exception e) {
			logger.error("第三方支付失败。", e);
		}
		return null;
	}
	
	private Map<String, String> getScale(String type){
		Map<String, String> map = new HashMap();
		if("SZX".equals(type)){ //神州行充值卡
			map.put(type, "神州行充值卡_0.95");
		}else if("UNICOM".equals(type)){ //联通充值卡
			map.put(type, "联通充值卡_0.95");
		}else if("TELECOM".equals(type)){ //电信充值卡
			map.put(type, "电信充值卡_0.95");
		}else if("SFTCARD".equals(type)){ // 盛付通卡
			map.put(type, "盛付通卡_0.86");
		}else if("JUNNET".equals(type)){ //骏网一卡通
			map.put(type, "骏网一卡通_0.84");
		}else if("WANMEI".equals(type)){ //完美一卡通
			map.put(type, "完美一卡通_0.86");
		}else if("NETEASE".equals(type)){ //网易一卡通
			map.put(type, "网易一卡通_0.86");
		}else if("ZHENGTU".equals(type)){ //征途一卡通
			map.put(type, "征途一卡通_0.85");
		}else if("JIUYOU".equals(type)){ //久游一卡通
			map.put(type, "久游一卡通_0.82");
		}else if("QQCARD".equals(type)){ //QQ币卡
			map.put(type, "QQ币卡_0.86");
		}else if("ZONGYOU".equals(type)){ //纵游一卡通
			map.put(type, "纵游一卡通_0.84");
		}else if("TIANHONG".equals(type)){ //天宏一卡通
			map.put(type, "天宏一卡通_0.84");
		}else if("SOHU".equals(type)){ //搜狐一卡通
			map.put(type, "搜狐一卡通_0.86");
		}
		return map;
	}

	@Override
	public synchronized void otherPay(PayOrder order) {
		PayOrder orgiOrder = queryPayOrderId(order.getPlatformorders(), null, null, "0");
		// 新增帐变记录。
		PayOrderLog log = new PayOrderLog();
		if(orgiOrder.getStatus() !=3){
			boolean flag = true;
			try {
				BigDecimal amount = order.getAmount();
				if(StringUtils.isNotEmpty(orgiOrder.getKfremarks())){
					String type = getScale(orgiOrder.getKfremarks()).get(orgiOrder.getKfremarks());
					String scale = type.split("_")[1];
					order.setAmount(new BigDecimal(scale).multiply(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
					order.setCwremarks(type+"充值金额："+amount+",实际到账："+order.getAmount()+"");
					amount = order.getAmount();
					log.setRemark("点卡充值");
				}else{
					if(StringUtils.isNotEmpty(order.getOrdercontent()) && Double.valueOf(order.getOrdercontent())>0){
						// 微信、支付宝用户承担1%的手续费
						amount = amount.multiply(new BigDecimal(1).subtract(new BigDecimal(order.getOrdercontent())).setScale(2,BigDecimal.ROUND_HALF_UP));
						log.setRemark("支付金额："+order.getAmount().doubleValue()+",实际到账："+amount);
					}
				}
				AccountMoney accountMoney = queryAccountMoney(order.getUiid(), null);
				BigDecimal beforebalance = accountMoney.getTotalamount();
				order.setBeforebalance(beforebalance.toString());
				BigDecimal laterbalance = amount.add(accountMoney.getTotalamount()).setScale(2,BigDecimal.ROUND_HALF_UP);
				order.setLaterbalance(String.valueOf(laterbalance));
				order.setStatus(3);
				flag = payOrderDao.update(order);
			
				log.setUiid(order.getUiid());
				log.setOrderid(order.getPoid());
				log.setAmount(order.getAmount());
				log.setType(order.getPaytyple());
				log.setWalletlog(order.getBeforebalance() + ">>>" + order.getLaterbalance());
				
				log.setCreatetime(DateUtil.getStrByDate(order.getUpdateDate(), DateUtil.TIME_FORMAT));
				payOrderDao.insertPayLog(log);
	
				if (!flag) {
					logger.info("修改订单状态失败。");
					throw new Exception("修改订单状态失败。");
				} else {
					/**
					 * 标示用户是否可洗码 0：不洗码 1：洗码
					 */
					int isxima = 1;
	
					// 验证用户是否有选择参加优惠活动
					Activity activity = null;
					if (null != order.getHdnumber() && !"".equals(order.getHdnumber())) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("hdnumber", order.getHdnumber());
						activity = activityDao.getObject(params);
						// 校验活动存在并且为正常开放状态并且参加此活动后任然可以洗码
						// 条件成立设置用户为可洗码
						if (null != activity && activity.getStatus() == 1) {
							if (activity.getIsxima() == 1) {
								isxima = 1;
							} else {
								isxima = 0;
							}
						}
					}
	
					BigDecimal activityMoney = BigDecimal.ZERO;// 活动金额
					PayOrder hdRecord = null;
					/**
					 * 活动金额类型 0：正常订单 1:优惠金额 2：活动彩金 3：洗码金额 4：首存优惠
					 */
					int activityType = 0;
					if (null != activity) {// 添加活动金额
						// 充值金额大于或等于活动的最小金额
						if (order.getAmount().intValue() >= Double.valueOf(activity.getMinmoney())) {
							if ("1".equals(activity.getHdtype())) {// 首存、2、3、4、5存活动
								activityMoney = new BigDecimal(activity.getRewmoney());
								activityType = 1;
							}else if("2".equals(activity.getHdtype()) || "3".equals(activity.getHdtype()) || "5".equals(activity.getHdtype())
									|| "10".equals(activity.getHdtype())) {// 次次存活动
								activityMoney = order.getAmount().multiply(new BigDecimal(activity.getHdscale()));
								// 验证优惠的金额是否大于活动最大优惠金额
								if (activityMoney.doubleValue() > Double.valueOf(activity.getMaxmoney())) {
									// 将活动金额赠送为最大金额
									activityMoney = new BigDecimal(activity.getMaxmoney());
								}
								activityType = 1;
							}else if ("8".equals(activity.getHdtype()) || "9".equals(activity.getHdtype())) {// 存200送50、存500送100
								activityMoney = new BigDecimal(activity.getMaxmoney());
								activityType = 1;
							}
						}
						activityMoney = activityMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
						// 添加一条加款订单用于记录用户参加活动赠送
						if (activityMoney.doubleValue() > 0) {
							hdRecord = order;
							String serialID = IdGenerator.genOrdId16("REWARD");
							Date date = new Date();
							hdRecord.setPoid(serialID);
							hdRecord.setPaytyple(2);
							hdRecord.setDeposittime(date);
							hdRecord.setAmount(activityMoney);
							hdRecord.setPaystatus(0);
							hdRecord.setStatus(3);
							// 查询是否有关联订单
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("remarks", order.getPlatformorders());
							PayOrder payOrder = queryPayOrder(param);
							if (payOrder != null) {
								return;
							}
							// 关联充值订单
							hdRecord.setRemarks(order.getPlatformorders());
							hdRecord.setOrdertype(activityType);
							hdRecord.setCreateDate(date);
							hdRecord.setUpdateDate(date);
							hdRecord.setKfremarks(activity.getHdtext() + "->充值金额：" + orgiOrder.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP) + "->优惠金额:" + activityMoney);
							hdRecord.setCwremarks(activity.getHdtext() + "->充值金额：" + orgiOrder.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP) + "->优惠金额:" + activityMoney);
							hdRecord.setBeforebalance(laterbalance + "");
							BigDecimal laterbalance2 = laterbalance.add(activityMoney).setScale(2,BigDecimal.ROUND_HALF_UP);
							hdRecord.setLaterbalance(String.valueOf(laterbalance2));
							this.savePayOrder(hdRecord);
	
							// 新增帐变记录。
							PayOrderLog origlog = new PayOrderLog();
							origlog.setUiid(hdRecord.getUiid());
							origlog.setOrderid(hdRecord.getPoid());
							origlog.setAmount(hdRecord.getAmount());
							origlog.setType(2);
							origlog.setWalletlog(hdRecord.getBeforebalance() + ">>>" + hdRecord.getLaterbalance());
							origlog.setRemark(activity.getHdtext());
							origlog.setCreatetime(DateUtil.getStrByDate(hdRecord.getUpdateDate(), DateUtil.TIME_FORMAT));
							payOrderDao.insertPayLog(origlog);
	
							ActivityFlag activityFlag = new ActivityFlag();
							activityFlag.setType(Integer.valueOf(activity.getHdtype()));
							activityFlag.setFlagtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
							activityFlag.setHms(DateUtil.getStrByDate(new Date(), "HH:mm:ss"));
							activityFlag.setUiid(order.getUiid());
							activityFlag.setAcid(activity.getAid());
							activityFlag.setAcgroup(activity.getAcgroup());
							activityDao.saveActivityFlag(activityFlag);
						}
					}
					// 设置用户是维护会员还是开发会员
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("uiid", order.getUiid());
					UserManager u = this.getUserManager(mp);
					if (u != null) {
						if (u.getClienttype() != 1) {
							u.setClienttype(1);
							this.updateUManager(u);
						}
					}
					// 将用户洗码状态改成可洗码状态
					XimaFlag ximaflag = ximaFlagDao.getNewestXimaFlag(order.getUiid());
					if (ximaflag == null || ximaflag.getIsxima() != isxima) {
						ximaflag = new XimaFlag();
						ximaflag.setFlaguiid(order.getUiid());
						ximaflag.setFlagaccount(order.getUaccount());
						ximaflag.setIsxima(isxima);
						if(hdRecord !=null){
							ximaflag.setRemark(hdRecord.getCwremarks());
						}else{
							ximaflag.setRemark("在线支付(无优惠)");
						}
						ximaflag.setUpdatetime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
						if (ximaFlagDao.save(ximaflag) == false) {
							logger.warn("第三方充值，添加记录不洗码数据失败。");
						}
					}
					// 修改用户余额
					flag = accumulationAccountMoney(order.getUiid(), amount.add(activityMoney));
					if (!flag) {
						logger.info("用户加款失败。");
						throw new Exception("修改用户余额失败。");
					}
				}
			} catch (Exception e) {
				logger.error("第三方支付修改订单状态失败。", e);
			}
		}
	}

	@Override
	public UnitedPayment unitedPrePayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("UNO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
//			Object obj = params.get("couponId");
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			UnitedPayment united = new UnitedPayment();
			united.setMerchantCode(payPlat.getCiphercode());
			united.setOutOrderId(order.getPlatformorders());
			// 注意: 支付金额 ：分
			united.setTotalAmount(Long.parseLong(params.get("amount").toString()) * 100);
			united.setMerUrl(payPlat.getReturnUrl());
			united.setOutUserId(String.valueOf(user.getUiid()));
			united.setNotifyUrl(payPlat.getNoticeUrl());
			united.setSign(united.getSignStr(payPlat.getPlatformkey()));

			return united;
		} catch (Exception e) {
			logger.error("中联支付失败。", e);
		}
		return null;
	}

	@Override
	public FCSOpenApiWxPayRequest jfwxPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			FCSOpenApiWxPayRequest request = new FCSOpenApiWxPayRequest();
			if(payPlat.getPpid() ==20){
				order.setPoid(IdGenerator.genOrdId16("JFP"));
				request.setService("wx_pay");
			    request.setWxPayType("wx_sm");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
	        request.setPartner(payPlat.getCiphercode());
	        request.setAmount(numberFormat.format(order.getAmount()));
	        request.setOutTradeNo(order.getPlatformorders());
	        request.setSubBody(RandomUtil.getRandomCode(6));
	        request.setSubject(RandomUtil.getRandomCode(5));
	        request.setReturnUrl(payPlat.getNoticeUrl());
	        request.setRemark("备注");
			return request;
		} catch (Exception e) {
			logger.error("微信支付失败。", e);
		}
		return null;
	}
	

	@Override
	public FCSOpenApiAliPayRequest jfaliPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			FCSOpenApiAliPayRequest request = new FCSOpenApiAliPayRequest();
			order.setPoid(IdGenerator.genOrdId16("FBO"));
			if(params.containsKey("channel")){
				order.setPoid(IdGenerator.genOrdId16("FBP"));
			}
			request.setService("ali_pay");
		    request.setAliPayType("ali_sm");
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
	        request.setPartner(payPlat.getCiphercode());
	        request.setAmount(numberFormat.format(order.getAmount()));
	        request.setOutTradeNo(order.getPlatformorders());
	        request.setSubBody(RandomUtil.getRandomCode(6));
	        request.setSubject(RandomUtil.getRandomCode(5));
	        request.setReturnUrl(payPlat.getNoticeUrl());
	        request.setRemark("备注");
			return request;
		} catch (Exception e) {
			logger.error("微信支付失败。", e);
		}
		return null;
	}

	@Override
	public MoBoPayment mbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("MBO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			// 第三方订单
			MoBoPayment moBo = new MoBoPayment();
			if(payPlat.getPpid() ==4){
				order.setPoid(IdGenerator.genOrdId16("MBW"));
				moBo.setChoosePayType("5");
			}else if(payPlat.getPpid() ==21){
				moBo.setApiName("WAP_PAY_B2C");
				moBo.setApiVersion("1.0.0.0");
				moBo.setMerchParam(params.get("requestHost").toString());
				moBo.setChoosePayType("5");
				order.setPoid(IdGenerator.genOrdId16("LFP"));
			}else{
				order.setPoid(IdGenerator.genOrdId16("MBO"));
				moBo.setChoosePayType("1");
			}
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			moBo.setMerchNo(payPlat.getCiphercode());
			moBo.setPlatformID(payPlat.getCiphercode());
			moBo.setOrderNo(order.getPlatformorders());
			DecimalFormat numberFormat = new DecimalFormat("#.00");
			moBo.setAmt(numberFormat.format(order.getAmount()));
			moBo.setMerchUrl(payPlat.getReturnUrl());
			moBo.setTradeSummary(IdGenerator.genOrdId16("U"));
//			moBo.setCustomerIP(params.get("customerIP").toString());
			moBo.setSignMsg(moBo.getSignStr(payPlat.getPlatformkey()));
			return moBo;
		} catch (Exception e) {
			logger.error("微信支付失败。", e);
		}
		return null;
	}

	@Override
	public CHPayment chPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("CHO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			CHPayment chPay = new CHPayment();
			chPay.setP1MerId(payPlat.getCiphercode());
			chPay.setP2Order(order.getPlatformorders());
			chPay.setP4Amt(order.getAmount().doubleValue());
			chPay.setP8Url(payPlat.getNoticeUrl());
			chPay.setPiUrl(payPlat.getReturnUrl());
			chPay.setPaFrpId(params.get("paFrpId")+"");
			if(params.containsKey("pgBankCode")){
				chPay.setPgBankCode(params.get("pgBankCode")+"");
			}
			chPay.setHmac(chPay.getSignStr(payPlat.getPlatformkey()));
			return chPay;
		} catch (Exception e) {
			logger.error("国付宝支付失败。", e);
		}
		return null;
	}

	@Override
	public BaoFooPayment baoFooPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("BFO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(payPlat.getPpid());
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			// 第三方订单
			BaoFooPayment baoFoo = new BaoFooPayment();
			baoFoo.setMemberID(payPlat.getCiphercode());
			baoFoo.setTransID(order.getPlatformorders());
			baoFoo.setOrderMoney(String.valueOf(order.getAmount().intValue() * 100));
			baoFoo.setPageUrl(payPlat.getReturnUrl());
			baoFoo.setReturnUrl(payPlat.getNoticeUrl());
			// 正式终端号
			baoFoo.setTerminalID("28275");
			// 测试终端号
			// baoFoo.setTerminalID("10000001");
			baoFoo.setSignature(baoFoo.bulidSignature(payPlat.getPlatformkey()));
			return baoFoo;
		} catch (Exception e) {
			logger.error("宝付支付失败。", e);
		}
		return null;
	}

	@Override
	public ThPayment thPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("THO"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(payPlat.getPpid());
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			// 第三方订单
			ThPayment thpay = new ThPayment();
			// 微信扫码支付
			if(payPlat.getPpid() ==6){
				order.setPoid(IdGenerator.genOrdId16("TWO"));
				thpay.setBankCode("WEIXIN");
			}
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);

			thpay.setMerchantCode(payPlat.getCiphercode());
			thpay.setOrderNo(order.getPlatformorders());
			thpay.setOrderAmount(String.valueOf(order.getAmount()));
			thpay.setReturnUrl(payPlat.getReturnUrl());
			thpay.setNotifyUrl(payPlat.getNoticeUrl());
			thpay.setCustomerIp(params.get("clientIp").toString());
			thpay.setReqReferer(params.get("referer").toString());
			thpay.setSign(thpay.bulidSignature(payPlat.getPlatformkey()));
			return thpay;
		} catch (Exception e) {
			logger.error("通汇支付失败。", e);
		}
		return null;
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		return payOrderDao.getRecordCount(params);
	}

	@Override
	public boolean updateUManager(UserManager entity) {
		return payOrderDao.updateUManager(entity);
	}

	public UserManager getUserManager(Map<String, Object> params) {
		return payOrderDao.getUserManager(params);
	}

	@Override
	public List<Activity> queryActivity(Map<String, Object> params) {
		return activityDao.getList(params);
	}

	@Override
	public List<ActivityFlag> queryActivityFlag(Map<String, Object> params) {
		return activityDao.queryActivityFlag(params);
	}

	@Override
	public synchronized void thwPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void thPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void mbPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void mbwPay(PayOrder order) {
		otherPay(order);
	}
	
	@Override
	public synchronized void jfpPay(PayOrder order){
		otherPay(order);
	}

	@Override
	public YBPayment ybPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			// 第三方订单
            YBPayment ybpay = new YBPayment();
			order.setPoid(IdGenerator.genOrdId16("YBW"));
			if(payPlat.getPpid() ==2){
			  order.setPoid(IdGenerator.genOrdId16("ZFB"));
			  ybpay.setBanktype("ALIPAY"); //支付宝
			}else if(payPlat.getPpid() ==24){
				order.setPoid(IdGenerator.genOrdId16("ZFBP"));
				ybpay.setBanktype("ALIPAYWAP"); //wap支付宝
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(payPlat.getPpid());
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			ybpay.setPartner(payPlat.getCiphercode());
			ybpay.setOrdernumber(order.getPlatformorders());
			ybpay.setPaymoney(String.valueOf(order.getAmount()));
			ybpay.setCallbackurl(payPlat.getReturnUrl());
			ybpay.setHrefbackurl(payPlat.getNoticeUrl());
			ybpay.setSign(ybpay.bulidSign(payPlat.getPlatformkey()));
			return ybpay;
		} catch (Exception e) {
			logger.error("银宝支付失败。",e);
		}
		return null;
	}
	
	@Override
	public RFPayment rfPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat) {
		try {
			if (payPlat == null) {
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			// 第三方订单
			RFPayment rfpay = new RFPayment();
			order.setPoid(IdGenerator.genOrdId16("RFO"));
			
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			rfpay.setOrderNo("A19E"+order.getPlatformorders());
			rfpay.setGoods("A19E");
			if(payPlat.getPpid() ==40){ //wap微信
				order.setPoid(IdGenerator.genOrdId16("RFWP"));
				rfpay.setOrderNo("W19T"+order.getPlatformorders());
				rfpay.setGoods("W19T");
				rfpay.setAppType("WECHAT");
				rfpay.setPartyId("gateway_W01649");
				rfpay.setAccountId("gateway_W01649001");
			}else if(payPlat.getPpid() ==41){ //wap支付宝
				order.setPoid(IdGenerator.genOrdId16("RFZP"));
				rfpay.setOrderNo("A19E"+order.getPlatformorders());
				rfpay.setGoods("A19E");
				rfpay.setAppType("ALIPAY");
				rfpay.setPartyId("gateway_A01634");
				rfpay.setAccountId("gateway_A01634001");
			}else if(payPlat.getPpid() ==48){ //web微信
				order.setPoid(IdGenerator.genOrdId16("RFW"));
				rfpay.setOrderNo("W19T"+order.getPlatformorders());
				rfpay.setGoods("W19T");
				rfpay.setAppType("WECHAT");
				rfpay.setPartyId("gateway_W01649");
				rfpay.setAccountId("gateway_W01649001");
			}else if(payPlat.getPpid() ==49){ //web支付宝
				order.setPoid(IdGenerator.genOrdId16("RFZ"));
				rfpay.setOrderNo("A19E"+order.getPlatformorders());
				rfpay.setGoods("A19E");
				rfpay.setAppType("ALIPAY");
				rfpay.setPartyId("gateway_A01634");
				rfpay.setAccountId("gateway_A01634001");
			}
			payOrderDao.save(order);

			rfpay.setOrderAmount(String.valueOf(order.getAmount()));
			rfpay.setReturnUrl(payPlat.getNoticeUrl());
			rfpay.setSignMD5(rfpay.getSignStr(payPlat.getPlatformkey()));
			return rfpay;
		} catch (Exception e) {
			logger.error("锐付支付失败。", e);
		}
		return null;
	}
	
	@Override
	public GstPayment gstPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			GstPayment gst = new GstPayment();
			order.setPoid(IdGenerator.genOrdId16("GSO"));
			if(payPlat.getPpid() ==25 || payPlat.getPpid() ==45 || payPlat.getPpid() ==47){
				gst.setPayType("3");
				order.setPoid(IdGenerator.genOrdId16("GSZ"));
			}else if(payPlat.getPpid() ==26 || payPlat.getPpid() ==43 || payPlat.getPpid() ==46){
				gst.setPayType("2");
				order.setPoid(IdGenerator.genOrdId16("GSW"));
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			gst.setMerchantCode(payPlat.getCiphercode());
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
			gst.setOrderAmount(AES.encrypt(numberFormat.format(order.getAmount()),payPlat.getPlatformkey()));
			gst.setOrderNo(order.getPlatformorders());
			gst.setInformUrl(payPlat.getReturnUrl());
			gst.setReturnUrl(payPlat.getNoticeUrl());
			gst.setOrderTime(DateUtil.getStrByDate(order.getDeposittime(),"yyyy-MM-dd HH:mm:ss"));
			return gst;
		} catch (Exception e) {
			logger.error("国盛通支付失败。",e);
		}
		return null;
	}
	
	@Override
	public XTCPayment xtcPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			XTCPayment xtc = new XTCPayment();
			order.setPoid(IdGenerator.genOrdId16("XTZ"));
			if(payPlat.getPpid() ==50 || payPlat.getPpid() ==52){
				xtc.setBanktype("WEIXIN");
				order.setPoid(IdGenerator.genOrdId16("XTW"));
			}else if(payPlat.getPpid() ==100){
				order.setPoid(IdGenerator.genOrdId16("XTD"));
				order.setKfremarks(String.valueOf(params.get("cardType"))); //点卡类型
			}else if(payPlat.getPpid() ==54){
				order.setPoid(IdGenerator.genOrdId16("XTO"));
//				xtc.setBanktype("SHORTCUT");
				xtc.setBanktype(String.valueOf(params.get("bankCode")));
			}else if(payPlat.getPpid() ==205 ||payPlat.getPpid() ==206){
				order.setPoid(IdGenerator.genOrdId16("XTQ"));
				xtc.setBanktype("QQ");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			xtc.setParter(payPlat.getCiphercode());
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
			xtc.setPaymoney(numberFormat.format(order.getAmount()));
			xtc.setOrdernumber(order.getPlatformorders());
			xtc.setCallbackurl(payPlat.getReturnUrl());
			xtc.setHrefbackurl(payPlat.getNoticeUrl());
			xtc.setSign(xtc.buildSignature(payPlat.getPlatformkey()));
			return xtc;
		} catch (Exception e) {
			logger.error("新天诚支付失败。",e);
		}
		return null;
	}
	
	@Override
	public ZLPayment zlPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			ZLPayment zl = new ZLPayment();
			if(payPlat.getPpid() ==55 || payPlat.getPpid() ==56){
				order.setPoid(IdGenerator.genOrdId16("ZLZ"));
			}else if(payPlat.getPpid() ==57 || payPlat.getPpid() ==58){
				order.setPoid(IdGenerator.genOrdId16("ZLW"));
				zl.setSource("0");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			zl.setAccount(payPlat.getCiphercode());
			zl.setAmount((order.getAmount().intValue()*100)+""); 
			zl.setSettleAmt(Double.valueOf(order.getAmount().intValue()*100*0.983).intValue()+""); //手续费1.7%
			zl.setOrgOrderNo(order.getPlatformorders());
			zl.setNotifyUrl(payPlat.getReturnUrl());
			return zl;
		} catch (Exception e) {
			logger.error("新天诚支付失败。",e);
		}
		return null;
	}
	
	@Override
	public LePayment lePayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			LePayment lp = new LePayment();
			if(payPlat.getPpid() ==200 || payPlat.getPpid() ==202){
				order.setPoid(IdGenerator.genOrdId16("LPW"));
				lp.setPayTypeCode("wxpay.qrpay.lm.qdzg");
			}else if(payPlat.getPpid() ==225){
				order.setPoid(IdGenerator.genOrdId16("LPO"));
				lp.setPayTypeCode("wap.pay");
			}else if(payPlat.getPpid() ==231 || payPlat.getPpid() ==232){
				order.setPoid(IdGenerator.genOrdId16("LPQ"));
				lp.setPayTypeCode("qqpay.qrpay.lm.qdzg");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			lp.setOutTradeNo(order.getPlatformorders());
			lp.setAmount((order.getAmount().intValue()*100)+"");
			lp.setDeviceIp(params.get("clientIp").toString());
			lp.setReturnUrl(payPlat.getReturnUrl());
			return lp;
		} catch (Exception e) {
			logger.error("乐付支付失败。",e);
		}
		return null;
	}
	
	/**
	 * 掌智支付
	 */
	@Override
	public ZZPayment zzPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			ZZPayment zz = new ZZPayment();
			if(payPlat.getPpid() ==207 || payPlat.getPpid() ==209){
				order.setPoid(IdGenerator.genOrdId16("ZZW"));
				zz.setService("pay.weixin.native");
			}else if(payPlat.getPpid() ==208 || payPlat.getPpid() ==210){
				order.setPoid(IdGenerator.genOrdId16("ZZZ"));
				zz.setService("pay.alipay.native");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			zz.setOutTradeNo(order.getPlatformorders());
			zz.setTotalFee(order.getAmount().intValue()*100);
			zz.setMchCreateIp(params.get("clientIp").toString());
			zz.setNotifyUrl(payPlat.getReturnUrl());
			zz.setMchId(payPlat.getCiphercode());
			zz.setSign(zz.buildSignatrue(payPlat.getPlatformkey()));
			
			return zz;
		} catch (Exception e) {
			logger.error("掌智支付失败。",e);
		}
		return null;
	}
	
	@Override
	public YPayment ypPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			YPayment yp = new YPayment();
			if(payPlat.getPpid() ==213){
				order.setPoid(IdGenerator.genOrdId16("YPO"));
				yp.setSfrom("pc");
				yp.setPaytype("");
			}else if(payPlat.getPpid() ==211){
				order.setPoid(IdGenerator.genOrdId16("YPW"));
				yp.setSfrom("pc");
				yp.setPaytype("31");
			}else if(payPlat.getPpid() ==212){
				order.setPoid(IdGenerator.genOrdId16("YPW"));
				yp.setSfrom("pc");
				yp.setPaytype("31");
			}else if(payPlat.getPpid() ==223 || payPlat.getPpid() ==224){
				order.setPoid(IdGenerator.genOrdId16("YPZ"));
				yp.setSfrom("pc");
				yp.setPaytype("32");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			yp.setAppid(payPlat.getCiphercode());
			yp.setOrderid(order.getPlatformorders());
			yp.setClientip(params.get("clientIp").toString());
			yp.setTongbu_url(payPlat.getReturnUrl());
			yp.setBack_url(payPlat.getNoticeUrl());
			yp.setSign(yp.signauture(payPlat.getPlatformkey()));
			yp.setFee(String.valueOf(order.getAmount().intValue()*100));
			return yp;
		} catch (Exception e) {
			logger.error("优付支付失败。",e);
		}
		return null;
	}
	
	@Override
	public SHBPayment shbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			SHBPayment shb = new SHBPayment();
			if(payPlat.getPpid() ==220 || payPlat.getPpid() ==222){
				order.setPoid(IdGenerator.genOrdId16("SHW"));
				shb.setServiceType("weixin_scan");
			}else if(payPlat.getPpid() ==221){
				order.setPoid(IdGenerator.genOrdId16("SHO"));
				shb.setServiceType("direct_pay");
			}else if(payPlat.getPpid() ==225 || payPlat.getPpid() ==226){
				order.setPoid(IdGenerator.genOrdId16("SHZ"));
				shb.setServiceType("alipay_scan");
			}else if(payPlat.getPpid() ==229 || payPlat.getPpid() ==230){
				order.setPoid(IdGenerator.genOrdId16("SHQ"));
				shb.setServiceType("qq_scan");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			shb.setMerchantCode(payPlat.getCiphercode());
			DecimalFormat numberFormat = new DecimalFormat("#0.00");
			shb.setOrderAmount(numberFormat.format(order.getAmount()));
			shb.setNotifyUrl(payPlat.getReturnUrl());
			shb.setClientIp(params.get("clientIp").toString());
			shb.setOrderNo(order.getPlatformorders());
			return shb;
		} catch (Exception e) {
			logger.error("速汇宝支付失败。",e);
		}
		return null;
	}
	
	@Override
	public ThPayment hbPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			ThPayment hb = new ThPayment();
			if(payPlat.getPpid() ==227 || payPlat.getPpid() ==228){
				order.setPoid(IdGenerator.genOrdId16("HBZ"));
				hb.setBankCode("ZHIFUBAO");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			hb.setMerchantCode(payPlat.getCiphercode());
			hb.setOrderNo(order.getPlatformorders());
			hb.setOrderAmount(String.valueOf(order.getAmount()));
			hb.setReturnUrl(payPlat.getReturnUrl());
			hb.setNotifyUrl(payPlat.getNoticeUrl());
			hb.setCustomerIp(params.get("clientIp").toString());
			hb.setReqReferer(params.get("referer").toString());
			hb.setSign(hb.bulidSignature(payPlat.getPlatformkey()));
			
			return hb;
		} catch (Exception e) {
			logger.error("速汇宝支付失败。",e);
		}
		return null;
	}
	
	@Override
	public ZSPayment zsPayment(UserInfo user, Map<String, Object> params, PayPlatform payPlat, String channel) {
		try {
			if(payPlat == null){
				throw new Exception("第三方支付已经关闭。");
			}
			// 平台生成订单
			PayOrder order = new PayOrder();
			ZSPayment sz = new ZSPayment();
			if(payPlat.getPpid() ==233 || payPlat.getPpid() ==234){
				order.setPoid(IdGenerator.genOrdId16("SZZ"));
				sz.setPayChannel("30");
			}else if(payPlat.getPpid() ==235 || payPlat.getPpid() ==236){
				order.setPoid(IdGenerator.genOrdId16("SZW"));
				sz.setPayChannel("21");
			}
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setUiid(user.getUiid());
			order.setUaccount(user.getAccount());
			order.setUrealname(user.getUname());
			order.setPaytyple(0); // 0存款，1提款，2赠送，3扣款
			order.setPaymethods(1); // 0公司入款，1第三方支付
			order.setDeposittime(new Date());
			order.setAmount(new BigDecimal(params.get("amount").toString()));
			order.setPaystatus(0); // 支付中
			order.setStatus(2); // 处理中
			order.setCreateDate(new Date());
			order.setUpdateDate(new Date());
			order.setPpid(3L);
			order.setHdnumber(params.get("hd").toString());
			order.setOrdercontent(String.valueOf(payPlat.getFee().doubleValue())); //手续费
			if(StringUtils.isNotEmpty(user.getPuiid()+"") && user.getPuiid()>0){
				UserInfo pUser =(UserInfo) userInfoDao.findById(user.getPuiid().intValue());
				order.setProxyName(pUser.getAccount());
			}
			payOrderDao.save(order);
			
			// 第三方订单
			sz.setMerchantCode(payPlat.getCiphercode());
			sz.setOutOrderId(order.getPlatformorders());
			sz.setAmount(order.getAmount().longValue()*100);
			sz.setNoticeUrl(payPlat.getReturnUrl());
			sz.setIp(params.get("clientIp").toString());
			return sz;
		} catch (Exception e) {
			logger.error("速汇宝支付失败。",e);
		}
		return null;
	}
	
	@Override
	public synchronized void ybPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void zfbPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void rfPay(PayOrder order) {
		otherPay(order);
	}
	
	@Override
	public synchronized void gstPay(PayOrder order) {
		otherPay(order);
	}
	
	@Override
	public synchronized void xtcPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void zlPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void lpPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void zzPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void ypPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void shbPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void hbPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void zsPay(PayOrder order) {
		otherPay(order);
	}

	@Override
	public synchronized void chPay(PayOrder order) {
		otherPay(order);
	}
}
