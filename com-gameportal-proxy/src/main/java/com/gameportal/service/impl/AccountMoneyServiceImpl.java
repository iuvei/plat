package com.gameportal.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.comms.DateUtil;
import com.gameportal.domain.AccountMoney;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.PayOrder;
import com.gameportal.domain.PayOrderLog;
import com.gameportal.domain.ProxyTransferLog;
import com.gameportal.domain.XimaFlag;
import com.gameportal.persistence.AccountMoneyMapper;
import com.gameportal.persistence.ProxyTransferLogMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IAccountMoneyService;
import com.gameportal.service.IPayOrderLogService;
import com.gameportal.service.IPayOrderService;
import com.gameportal.service.IXimaFlagService;
import com.gameportal.util.IdGenerator;

/**
 * 钱包余额Service实现
 * @author Administrator
 *
 */
@SuppressWarnings("all")
@Service("accountMoneyServiceImpl")
public class AccountMoneyServiceImpl extends BaseService implements
		IAccountMoneyService {
	
	@Autowired
	private AccountMoneyMapper accountMoneyMapper;
	
	@Autowired
	private ProxyTransferLogMapper proxyTransferLogMapper;
	
	@Resource(name="ximaFlagService")
	private IXimaFlagService ximaFlagService;
	
	@Resource(name="payOrderServiceImpl")
	private IPayOrderService payOrderService;
	
	@Resource(name="payOrderLogService")
	private IPayOrderLogService payOrderLogService;
	
	@Override
	public AccountMoney queryAccountMoney(Map<String, Object> params) {
		try {
			List<AccountMoney> list=accountMoneyMapper.getMoneyInfo(params);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		} catch (Exception e) {
			logger.error("查询钱包余额错误：", e);
		}
		return null;
	}

	@Override
	public int insertAccountMoney(AccountMoney money) {
		return accountMoneyMapper.insertAccountMoney(money);
	}

	@Override
	public int updateAccountMoney(AccountMoney money) {
		return accountMoneyMapper.updateAccountMoney(money);
	}

	@Override
	public void updateProxyTransfer(MemberInfo member,MemberInfo lower, Integer amount) {
		ProxyTransferLog log = new ProxyTransferLog();
		Map<String, Object> map = new HashMap<>();
		map.put("uiid", member.getUiid());
		// 上级减钱
		AccountMoney money = queryAccountMoney(map);
		log.setBeforebalance(money.getTotalamount());
		money.setTotalamount(money.getTotalamount().subtract(new BigDecimal(amount)));
		log.setAfterbalance(money.getTotalamount());
		money.setUpdateDate(new Date());
		int result =updateAccountMoney(money);
		
		// 上级扣款记录
		PayOrder payOrder = new PayOrder();
		String serialID = IdGenerator.genOrdId16("PUNISH");
		payOrder.setPoid(serialID);
		payOrder.setPlatformorders("");
		payOrder.setPaytyple(3);
		payOrder.setAmount(Double.valueOf(amount+""));
		payOrder.setKfopttime(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setUiid(member.getUiid());
		payOrder.setUaccount(member.getAccount());
		payOrder.setUrealname(member.getUname());
		payOrder.setOrdertype(1);
		payOrder.setKfremarks("给下线["+lower.getAccount()+"]转账");
		payOrder.setCwremarks("给下线["+lower.getAccount()+"]转账");
		payOrder.setDeposittime(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setCreateDate(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setUpdateDate(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setStatus(3); // status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		payOrder.setBeforebalance(log.getBeforebalance().toString());
		payOrder.setLaterbalance(log.getAfterbalance().toString());
		payOrderService.insert(payOrder);
		
		// 新增帐变记录。
		PayOrderLog payLog = new PayOrderLog();
		payLog.setUiid(member.getUiid());
		payLog.setOrderid(payOrder.getPlatformorders());
		payLog.setAmount(BigDecimal.ZERO.subtract(new BigDecimal(amount)));
		payLog.setType(3); //扣款
		payLog.setWalletlog(payOrder.getBeforebalance() + ">>>" + payOrder.getLaterbalance());
		payLog.setRemark(payOrder.getCwremarks());
		payLog.setCreatetime(payOrder.getCreateDate());
		payOrderLogService.insert(payLog);
				
		// 下级加钱
		map.put("uiid", lower.getUiid());
		money = queryAccountMoney(map);
		String lowBeforeBalance ="0";
		String lowAfterBalance ="0";
		if(money ==null){
			money = new AccountMoney();
			money.setUiid(lower.getUiid());
			money.setTotalamount(new BigDecimal(amount));
			money.setStatus(1);
			money.setCreateDate(new Date());
			money.setUpdateDate(new Date());
			lowAfterBalance=String.valueOf(amount);
			insertAccountMoney(money);
		}else{
			lowBeforeBalance = money.getTotalamount().toString();
			money.setTotalamount(money.getTotalamount().add(new BigDecimal(amount)));
			lowAfterBalance= money.getTotalamount().toString();
			money.setUpdateDate(new Date());
			updateAccountMoney(money);
		}
		// 下线优惠记录
		payOrder = new PayOrder();
		serialID = IdGenerator.genOrdId16("REWARD");
		payOrder.setPoid(serialID);
		payOrder.setPlatformorders("");
		payOrder.setPaytyple(2);
		payOrder.setAmount(Double.valueOf(amount+""));
		payOrder.setKfopttime(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setUiid(lower.getUiid());
		payOrder.setUaccount(lower.getAccount());
		payOrder.setUrealname(lower.getUname());
		payOrder.setOrdertype(1);
		payOrder.setKfremarks("上级["+member.getAccount()+"]转账");
		payOrder.setCwremarks("上级["+member.getAccount()+"]转账");
		payOrder.setDeposittime(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setCreateDate(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setUpdateDate(DateUtil.convert2Str(new Date(), DateUtil.DATE_PATTERN_S));
		payOrder.setStatus(3); // status 状态 0 作废 1 发起（待财务审核） 2 处理中 3 存取成功 4 存取失败
		payOrder.setBeforebalance(lowBeforeBalance);
		payOrder.setLaterbalance(lowAfterBalance);
		payOrderService.insert(payOrder);
		
		// 新增帐变记录。
		payLog = new PayOrderLog();
		payLog.setUiid(lower.getUiid());
		payLog.setOrderid(payOrder.getPlatformorders());
		payLog.setAmount(new BigDecimal(amount));
		payLog.setType(2); //优惠赠送
		payLog.setWalletlog(payOrder.getBeforebalance() + ">>>" + payOrder.getLaterbalance());
		payLog.setRemark(payOrder.getCwremarks());
		payLog.setCreatetime(payOrder.getCreateDate());
		payOrderLogService.insert(payLog);
				
		// 查询最新洗码状态
		map.clear();
		map.put("flaguiid", lower.getUiid());
		XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(map);
		if(ximaflag == null || ximaflag.getIsxima() != 0){
			ximaflag = new XimaFlag();
			ximaflag.setFlaguiid(lower.getUiid());
			ximaflag.setFlagaccount(lower.getAccount());
			ximaflag.setIsxima(0);
			ximaflag.setRemark(payOrder.getCwremarks());
			ximaFlagService.insert(ximaflag);
		}
				
		log.setPid(member.getUiid());
		log.setPaccount(member.getAccount());
		log.setLid(lower.getUiid());
		log.setLaccount(lower.getAccount());
		log.setAmount(amount);
		log.setCreatedate(new Date());
		proxyTransferLogMapper.insertProxyTransferLog(log);
		
	}
}
