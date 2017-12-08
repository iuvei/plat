package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyClearingLog;
import com.gameportal.manage.proxy.model.ProxyUserXimaLog;
import com.gameportal.manage.reportform.model.OrderManageReportForm;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaMain;

/**
 * 订单管理Dao
 * @author Administrator
 *
 */
@Component("orderManageReportDao")
public class OrderManageReportFormDao extends BaseIbatisDAO {

	@Override
	public Class<OrderManageReportForm> getEntityClass() {
		return OrderManageReportForm.class;
	}
	
	/**
	 * 钱包余额
	 * @param params 参数
	 * @return
	 */
	public Map<String,Object> getAccountMoney(Map<String, Object> params){
		return  (Map<String, Object>) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectAccountMoney", params);
	}
	
	/**
	 * 查询订单管理列表
	 * @param params
	 * @return
	 */
	public List<OrderManageReportForm> getOrderManageList(Map<String, Object> params){
		return  super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectOrderList", params);

	}
	
	/**
	 * 查询订单管理总数
	 * @param params
	 * @return
	 */
	public Long getOrderManageCount(Map<String, Object> params){
		return  (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectOrderCount", params);

	}

	/**
	 * 查询会员洗码金额
	 * @param params
	 * @return
	 */
	public String getMemberXimaMainMoney(Map<String, Object> params){
		return  (String) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectMemberXimaMainMoney", params);
	}
	
	
	/**
	 * 查询代理洗码金额
	 * @param params
	 * @return
	 */
	public String getProxyClearLogMoney(Map<String, Object> params){
		return  (String) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectProxyClearLogMoney", params);
	}
	
	/**
	 * 查询代理下线洗码金额
	 * @param params
	 * @return
	 */
	public String getProxyUserXimaLogMoney(Map<String, Object> params){
		return  (String) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectProxyUserXimaLogMoney", params);
	}

}
