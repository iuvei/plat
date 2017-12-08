package com.gameportal.manage.reportform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.reportform.model.OrderManageReportForm;
import com.gameportal.manage.xima.model.MemberXimaMain;

/**
 * 订单管理业务层接口
 * @author Administrator
 *
 */
public interface IOrderManageReportFormService {

	/**
	 * 查询钱包余额
	 * @param map 条件
	 * @return
	 */
	public Map<String,Object> getAccountMoney(Map<String, Object> map);
	
	/**
	 * 查询订单管理列表
	 * @param map   查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public List<OrderManageReportForm>  getOrderManageList(Map<String, Object> map,Integer startNo,Integer pageSize);
	
	/**
	 * 查询订单管理总数
	 * @param map 查询条件
	 * @param startNo 当前页
	 * @param pageSize 每页行数
	 * @return
	 */
	public Long  getOrderManageCount(Map<String, Object> map);
	
	/**
	 * 查询订单管理导出
	 * @param map   查询条件
	 * @return
	 */
	public List<OrderManageReportForm>  getOrderManageListForReport(Map<String, Object> map);
	
	/**
	 * 查询会员洗码金额
	 * @param params
	 * @return
	 */
	public String getMemberXimaMainMoney(Map<String, Object> params);
	
	/**
	 * 查询代理洗码金额
	 * @param params
	 * @return
	 */
	public String getProxyClearLogMoney(Map<String, Object> params);
	
	/**
	 * 查询代理下线洗码金额
	 * @param params
	 * @return
	 */
	public String getProxyUserXimaLogMoney(Map<String, Object> params);

}
