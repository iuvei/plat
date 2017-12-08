package com.gameportal.manage.pay.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.MemberUpgradeLogDao;
import com.gameportal.manage.member.dao.XimaFlagDao;
import com.gameportal.manage.member.model.MemberUpgradeLog;
import com.gameportal.manage.order.dao.CompanyCardDao;
import com.gameportal.manage.pay.dao.ActivityDao;
import com.gameportal.manage.pay.dao.PayOrderDao;
import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.pay.service.IPayOrderService;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.dao.UserInfoDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.model.WithdrawalcountLog;
import com.gameportal.manage.util.DateUtil;

@Service("payOrderServiceImpl")
public class PayOrderServiceImpl implements IPayOrderService {
	private static final Logger logger = Logger
			.getLogger(PayOrderServiceImpl.class);
	@Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao = null;
	@Resource(name = "companyCardDao")
	private CompanyCardDao companyCardDao = null;
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao = null;
	@Resource(name = "userInfoDao")
	private UserInfoDao userInfoDao = null;
	@Resource(name = "activityDao")
	private ActivityDao activityDao = null;
	@Resource(name = "ximaFlagDao")
	private XimaFlagDao ximaFlagDao = null;
	@Resource(name = "memberUpgradeLogDao")
	private MemberUpgradeLogDao memberUpgradeLogDao = null;
	public PayOrderServiceImpl() {
		super();
	}

	@Override
	public boolean savePayOrder(PayOrder depositOrder) {
		return StringUtils.isNotBlank(ObjectUtils.toString(payOrderDao
				.save(depositOrder))) ? true : false;
	}

	@Override
	public boolean updatePayOrder(PayOrder depositOrder) {
		return payOrderDao.update(depositOrder);
	}

	@Override
	public boolean deletePayOrder(String id) {
		PayOrder po =(PayOrder) payOrderDao.findById(id);
		po.setStatus(4); // status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		return payOrderDao.update(po);
	}
	
	@Override
	public boolean makePayOrder(String id) {
		Timestamp date = new Timestamp(new Date().getTime());
		PayOrder po =(PayOrder) payOrderDao.findById(id);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		po.setUpdateDate(fmt.format(new Date()));
		po.setStatus(3); // status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		po.setPaystatus(2); // paystatus 状态 0 已接受  1 处理中 2 处理成功 3 处理失败
		po.setKfremarks(po.getKfremarks()+" 补单");//客服备注
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("uiid", po.getUiid());
		AccountMoney accountMoney=accountMoneyDao.getByUiid(params);
		if(accountMoney!=null){
			accountMoney.setTotalamount(po.getAmount());
			accountMoney.setUpdateDate(date);
			boolean flag=accountMoneyDao.updateTotalamount(accountMoney);
			if(!flag){
				return false;
			}
		}
		return payOrderDao.update(po);
	}

	@Override
	public Long queryPayOrderCount(Map<String, Object> params) {
		return payOrderDao.getRecordCount(params);
	}

	@Override
	public List<PayOrder> queryPayOrder(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return payOrderDao.getList(params);
	}

	@Override
	public List<PayOrder> queryPayOrder(Map<String, Object> params) {
		return payOrderDao.getList(params);
	}
	
	@Override
	public PayOrder queryById(String poid) {
		return (PayOrder) payOrderDao.findById(poid);
	}
	
	@Override
	public Long queryPayOrderRPCount(Map<String, Object> params) {
		return payOrderDao.getRecordCount("PayOrder.countRP", params);
	}
	
	@Override
	public List<PayOrder> queryPayOrderRP(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return payOrderDao.queryPayOrderRP(params);
	}

	@Override
	public boolean isMoneyLocked(Long uiid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", uiid);
		List<AccountMoney> amList = accountMoneyDao.queryForPager(params, 0, 0);
		if (null != amList && amList.size() > 0) {
			if (amList.get(0).getStatus() != 1) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean audit(PayOrder payOrder) throws Exception {
		boolean flag = true;
		Map<String, String> smsparams = new HashMap<String, String>();
		smsparams.put("channel","1");
		smsparams.put("spuname","钱包余额变更通知");
		if (false == payOrderDao.update(payOrder)) {
			return false;
		}
		AccountMoney am = null;
		Date now = new Date();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", payOrder.getUiid());
		List<AccountMoney> amList = accountMoneyDao.queryForPager(params, 0, 0);
		if (null == amList || amList.size() <= 0) {
			am = new AccountMoney();
			am.setTotalamount(BigDecimal.ZERO);
			am.setUiid(payOrder.getUiid());
			am.setStatus(1);
			am.setCreateDate(now);
			am.setUpdateDate(now);
			am = (AccountMoney) accountMoneyDao.save(am);
		} else {
			am = amList.get(0);
		}
		am.setUpdateDate(now);
		String expStr = "审核失败";
		switch (payOrder.getPaytyple().intValue()) {
		case 0:
			if (payOrder.getStatus() == 3) { // 存款成功,钱添加到钱包
				BigDecimal amount=payOrder.getAmount();
				//操作前用户余额 和之后余额
				payOrder.setBeforebalance(am.getTotalamount().toString());
				payOrder.setLaterbalance(am.getTotalamount().add(payOrder.getAmount()).toString());
				am.setTotalamount(amount);
				flag = accountMoneyDao.updateTotalamount(am);
				
				// 新增帐变记录。
				PayOrderLog log = new PayOrderLog();
				log.setUiid(payOrder.getUiid());
				log.setOrderid(payOrder.getPoid());
				log.setAmount(payOrder.getAmount());
				log.setType(payOrder.getPaytyple());
				log.setWalletlog(payOrder.getBeforebalance()+">>>"+payOrder.getLaterbalance());
				log.setRemark(payOrder.getCwremarks());
				log.setCreatetime(payOrder.getUpdateDate());
				payOrderDao.insertPayLog(log);
				
				expStr = "存款订单审核成功";
				smsparams.put("content", "尊敬的会员，您的" + am.getTotalamount()
						+ "元存款已经到账，请留意银钱包余额变化，如有问题请联系客服。");
			} else if (payOrder.getStatus() == 4) { // 存款失败
				smsparams.put("content", "尊敬的会员，您的" + am.getTotalamount()
						+ "元存款订单财务审核没通过，如有问题请联系客服。");
			}
			break;
		case 1:
			if (payOrder.getStatus() == 3) { // 提款成功
				//操作前用户余额 和之后余额
				payOrder.setBeforebalance((am.getTotalamount().add(payOrder.getAmount())).toString());
				payOrder.setLaterbalance(am.getTotalamount().toString());
				// 新增帐变记录。
				PayOrderLog log = new PayOrderLog();
				log.setUiid(payOrder.getUiid());
				log.setOrderid(payOrder.getPoid());
				log.setAmount(BigDecimal.ZERO.subtract(payOrder.getAmount()));
				log.setType(payOrder.getPaytyple());
				log.setWalletlog(payOrder.getBeforebalance()+">>>"+payOrder.getLaterbalance());
				log.setRemark(payOrder.getCwremarks());
				log.setCreatetime(payOrder.getUpdateDate());
				payOrderDao.insertPayLog(log);
				smsparams.put("content", "尊敬的会员，您的" + am.getTotalamount()
						+ "元提款订单财务审核通过，请留意银行卡余额变化，如有问题请联系客服。");
			} else if (payOrder.getStatus() == 4) { // 提款失败,钱返还到钱包
				am.setTotalamount(payOrder.getAmount());
				flag = accountMoneyDao.updateTotalamount(am);
				expStr = "提款订单审核失败";
				smsparams.put("content", "尊敬的会员，您的" + am.getTotalamount()
						+ "元提款订单财务审核没通过，如有问题请联系客服。");
			}
			break;
		case 2:
			if (payOrder.getStatus() == 3) { // 赠送成功
				//操作前用户余额 和之后余额
				payOrder.setBeforebalance(am.getTotalamount().toString());
				payOrder.setLaterbalance(am.getTotalamount().add(payOrder.getAmount()).toString());
				am.setTotalamount(payOrder.getAmount());
				flag = accountMoneyDao.updateTotalamount(am);
				
				// 新增帐变记录。
				PayOrderLog log = new PayOrderLog();
				log.setUiid(payOrder.getUiid());
				log.setOrderid(payOrder.getPoid());
				log.setAmount(payOrder.getAmount());
				log.setType(payOrder.getPaytyple());
				log.setWalletlog(payOrder.getBeforebalance()+">>>"+payOrder.getLaterbalance());
				log.setRemark(payOrder.getCwremarks());
				log.setCreatetime(payOrder.getUpdateDate());
				payOrderDao.insertPayLog(log);
				
				String hdType ="";
                if(payOrder.getOrdertype() ==4){
                	hdType ="3";
                }else if(payOrder.getOrdertype() ==6){
                	hdType ="2";
                }else if(payOrder.getOrdertype() ==9){
                	hdType ="1";
                }else if(payOrder.getOrdertype() ==10){
                	hdType ="7";
                }else if(payOrder.getOrdertype() ==11){
                	hdType ="8";
                }else if(payOrder.getOrdertype() ==12){
                	hdType ="9";
                }else if(payOrder.getOrdertype() ==14){
                	hdType ="11";
                }else if(payOrder.getOrdertype() ==15){
                	hdType ="12";
                }else if(payOrder.getOrdertype() ==16){
                	hdType ="13";
                }
                if(StringUtils.isNotEmpty(hdType)){
                	// 新增活动标识
                	ActivityFlag activityFlag=new ActivityFlag();
                    activityFlag.setType(3);
                    activityFlag.setFlagtime(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd"));
                    activityFlag.setHms(DateUtil.getStrByDate(new Date(), "HH:mm:ss"));
                    activityFlag.setUiid(payOrder.getUiid());
                    activityFlag.setAcid(7);
                    activityFlag.setAcgroup("1");
                    activityDao.saveActivityFlag(activityFlag);
                    
                    // 关联存款订单
                    Map<String, Object> map = new HashMap<>();
					map.put("uiid", payOrder.getUiid());
					map.put("paytyple", 0);
					map.put("ordertype", 0);
					map.put("status", 3);
					map.put("sortColumns", "deposittime desc");
					List<PayOrder> orders = payOrderDao.queryForPager(map, 0, 20);
					if(CollectionUtils.isNotEmpty(orders)){
						PayOrder order = orders.get(0);
						order.setHdnumber(hdType);
						payOrderDao.update(order);
					}
                }else if(payOrder.getOrdertype() ==20){ //晋级礼金
                	MemberUpgradeLog upLog =new MemberUpgradeLog();
                	upLog.setUid(payOrder.getUiid());
                	UserInfo user =(UserInfo)userInfoDao.findById(payOrder.getUiid());
                	upLog.setOldgrade(Long.valueOf(user.getGrade()));
                	upLog.setNewgrade(Long.valueOf(user.getGrade()+1));
                	upLog.setAccount(user.getAccount());
                	upLog.setCreateDate(new Timestamp(new Date().getTime()));
                	upLog.setReason("系统晋级");
                	upLog.setRemark("系统晋级");
                	memberUpgradeLogDao.saveOrUpdate(upLog);
                	
                	user.setGrade(user.getGrade()+1);
                	userInfoDao.saveOrUpdate(user);
                }
			}
			break;
		case 3:
			if (payOrder.getStatus() == 3) { // 扣款成功
				//操作前用户余额 和之后余额
				payOrder.setBeforebalance(am.getTotalamount().toString());
				payOrder.setLaterbalance(am.getTotalamount().subtract(payOrder.getAmount()).toString());
				am.setTotalamount(BigDecimal.ZERO.subtract(payOrder.getAmount()));
				flag = accountMoneyDao.updateTotalamount(am);
				// 新增帐变记录。
				PayOrderLog log = new PayOrderLog();
				log.setUiid(payOrder.getUiid());
				log.setOrderid(payOrder.getPoid());
				log.setAmount(BigDecimal.ZERO.subtract(payOrder.getAmount()));
				log.setType(payOrder.getPaytyple());
				log.setWalletlog(payOrder.getBeforebalance()+">>>"+payOrder.getLaterbalance());
				log.setRemark(payOrder.getCwremarks());
				log.setCreatetime(payOrder.getUpdateDate());
				payOrderDao.insertPayLog(log);
			}
			//修改资金状态
			Map<String, Object> map = new HashMap<>();
			map.put("uiid", am.getUiid());
			am = accountMoneyDao.getByUiid(map);
			am.setStatus(1);
			accountMoneyDao.saveOrUpdate(am);
			break;
		}
		if (smsparams.containsKey("content")) {
			this.sendSms(payOrder, smsparams);
		}
		if (flag == false) {
			throw new Exception(expStr);
		}else{
			payOrderDao.update(payOrder);
		}
		return flag;
	}

	private void sendSms(PayOrder payOrder, Map<String, String> params) {
		
	}
	
	public String savePickupOrder(PayOrder payOrder) {
		if (null == payOrder || null == payOrder.getPaytyple()
				|| 1 != payOrder.getPaytyple().intValue()) { // 非提款订单
			return "-1";
		}
		// 扣除提款
		AccountMoney am = null;
		Date now = new Date();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", payOrder.getUiid());
		List<AccountMoney> amList = accountMoneyDao.getList(params);
		if (null == amList || amList.size() <= 0) {
			am = new AccountMoney();
			am.setTotalamount(BigDecimal.ZERO);
			am.setUiid(payOrder.getUiid());
			am.setStatus(1);
			am.setCreateDate(now);
			am.setUpdateDate(now);
			am = (AccountMoney) accountMoneyDao.save(am);
		} else {
			am = amList.get(0);
		}
		if(am.getTotalamount().intValue() < payOrder.getAmount().intValue()){
			return "-2";//会员账号余额不足
		}
		am.setUpdateDate(now);
		am.setTotalamount(new BigDecimal(0).subtract(payOrder.getAmount()));
		if (accountMoneyDao.updateTotalamount(am)) {
			// 保存提款订单
			payOrderDao.save(payOrder);
			return "0";
		}
		return null;
	}
	
	@Override
	public Map<String, Integer> getAlertCount() {
		List<String> list = payOrderDao.queryAlertCount();
		Map<String, Integer> result = null;
		if(null != list && list.size() > 0){
			result = new HashMap<String, Integer>();
			int newWithdraw = 0;//新提款条数
			int recharge = 0;//新充值条数
			int notadd = 0;//加款未处理
			int notdeduct = 0;//扣款
			for(String val : list){
				int i = Integer.valueOf(val);
				if(i == 0){
					recharge++;
				}
				if(i == 1){
					newWithdraw++;
				}
				if(i == 2){
					notadd++;
				}
				if(i == 3){
					notdeduct++;
				}
			}
			result.put("newWithdraw", newWithdraw);
			result.put("notadd", notadd);
			result.put("recharge", recharge);
			result.put("notdeduct", notdeduct);
		}
		return result;
	}

	@Override
	public Long selectProxyPayOrderLogCount(Map<String, Object> params) {
		return payOrderDao.selectProxyPayOrderLogCount(params);
	}

	@Override
	public List<PayOrder> selectProxyPayOrderLog(Map<String, Object> params,
			int thisPage, int pageSize) {
		return payOrderDao.selectProxyPayOrderLog(params, thisPage, pageSize);
	}
	
	@Override
	public String selectProxyPayOrderTotal(Map<String, Object> params) {
		return payOrderDao.selectProxyPayOrderTotal(params);
	}

	@Override
	public boolean updateWithdrawalcount(WithdrawalcountLog entity) {
		// TODO Auto-generated method stub
		return payOrderDao.updateWithdrawalcount(entity);
	}

	@Override
	public WithdrawalcountLog getWithdrawalcount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return payOrderDao.getWithdrawalcount(params);
	}

	@Override
	public Activity getObject(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return activityDao.getObject(params);
	}
	
	@Override
	public boolean saveActivityFlag(ActivityFlag activityFlag) {
		// TODO Auto-generated method stub
		return activityDao.saveActivityFlag(activityFlag);
	}

	@Override
	public void insertPayLog(PayOrderLog log) {
		payOrderDao.insertPayLog(log);
	}
}
