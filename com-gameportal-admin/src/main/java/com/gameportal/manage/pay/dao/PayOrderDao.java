package com.gameportal.manage.pay.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.WithdrawalcountLog;

@SuppressWarnings("rawtypes")
@Component
public class PayOrderDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return PayOrder.class;
	}

	public boolean saveOrUpdate(PayOrder entity) {
		if (entity.getPoid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<PayOrder> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

	@SuppressWarnings("unchecked")
	public List<PayOrder> queryPayOrderRP(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList(
				getSimpleName() + ".pageSelectRP", params);
	}

	public Long selectGiftAmount(Map<String, Object> params) {
		return (Long) getSqlMapClientTemplate().queryForObject(
				getSimpleName() + ".selectGiftAmount", params);
	}
	
	/**
	 * 查询未处理提款和充值订单
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryAlertCount(){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectAlertCount");
	}
	
	/**
	 * 代理查询下线出入款
	 * @param params
	 * @return
	 */
	public Long selectProxyPayOrderLogCount(Map<String, Object> params){
		return super.getRecordCount(getSimpleName()+".selectProxyPayOrderLogCount", params);
	}
	
	@SuppressWarnings("unchecked")
	public List<PayOrder> selectProxyPayOrderLog(Map<String, Object> params,int thisPage,int pageSize){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyPayOrderLog",params, thisPage, pageSize);
	}
	
	public String selectProxyPayOrderTotal(Map<String, Object> params){
		return (String) getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectProxyPayOrderTotal",params);
	}
	
	/**
	 * 更新提款次数
	 * @param entity
	 * @return
	 */
	public boolean updateWithdrawalcount(WithdrawalcountLog entity){
		try {
			super.getSqlMapClientTemplate().update(getSimpleName()+".updateWithdrawalcount", entity);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * 查询提款次数
	 * @param params
	 * @return
	 */
	public WithdrawalcountLog getWithdrawalcount(Map<String, Object> params){
		return (WithdrawalcountLog)super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".getWithdrawalcount", params);
	}
	
	/**
	 * 新增帐变日志
	 * @param log
	 */
	public void insertPayLog(PayOrderLog log){
		super.getSqlMapClientTemplate().insert(getSimpleName()+".insertPayLog", log);
	}

}
