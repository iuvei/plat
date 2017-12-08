package com.gameportal.manage.pay.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.pay.model.Activity;
import com.gameportal.manage.pay.model.ActivityFlag;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.user.model.WithdrawalcountLog;

public abstract interface IPayOrderService {

	Long queryPayOrderCount(Map<String, Object> params);

	List<PayOrder> queryPayOrder(Map<String, Object> params, Integer startNo,
			Integer pageSize);

	boolean savePayOrder(PayOrder payOrder);

	boolean updatePayOrder(PayOrder payOrder);

	boolean deletePayOrder(String id);
	
	/**
	 * 补单
	 * @param id
	 * @return
	 */
	boolean makePayOrder(String id);

	PayOrder queryById(String id);

	boolean isMoneyLocked(Long uiid);

	boolean audit(PayOrder payOrder) throws Exception;

	List<PayOrder> queryPayOrderRP(Map<String, Object> params, Integer startNo,
			Integer pageSize);

	Long queryPayOrderRPCount(Map<String, Object> params);

	String savePickupOrder(PayOrder payOrder);
	
	/**
	 * 获取未处理充值订单和提款订单
	 * key:newWithdraw 提款
	 * key:notadd 充值
	 * @return
	 */
	public Map<String, Integer> getAlertCount();
	
	public Long selectProxyPayOrderLogCount(Map<String, Object> params);
	public List<PayOrder> selectProxyPayOrderLog(Map<String, Object> params,int thisPage,int pageSize);
	public String selectProxyPayOrderTotal(Map<String, Object> params);
	
	/**
	 * 导出报表用
	 * @param params 条件
	 * @return
	 */
	List<PayOrder> queryPayOrder(Map<String, Object> params);
	
	/**
	 * 更新提款次数
	 * @param entity
	 * @return
	 */
	public boolean updateWithdrawalcount(WithdrawalcountLog entity);
	/**
	 * 查询提款次数
	 * @param params
	 * @return
	 */
	public WithdrawalcountLog getWithdrawalcount(Map<String, Object> params);
	
	/**
	 * 查询活动详情
	 * @param params
	 * @return
	 */
	public Activity getObject(Map<String, Object> params);
	
	/**
	 * 新增活动标示
	 * @param activityFlag
	 * @return
	 */
	public boolean saveActivityFlag(ActivityFlag activityFlag);
	
	/**
	 * 新增帐变日志
	 * @param log
	 */
	public void insertPayLog(PayOrderLog log);
}
