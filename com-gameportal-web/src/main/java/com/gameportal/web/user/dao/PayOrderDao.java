package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.PayOrder;

@Component
public class PayOrderDao extends BaseIbatisDAO {

	public Class<PayOrder> getEntityClass() {
		return PayOrder.class;
	}

	public void saveOrUpdate(PayOrder entity) {
		if (entity.getPoid() == null)
			save(entity);
		else
			update(entity);
	}
	
	public List<PayOrder> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public List<PayOrder> getPayOrder(Map<String, Object> param){
		return queryForList(param);
	}
	
	/**
	 * 统计订单金额
	 * @param params
	 * @return
	 */
	public long sumPayOrder(Map<String, Object> params){
		return (Long) this.queryForObject(getSimpleName()+".sumPayOrder", params);
	}
	
	/**
	 * 统计订单数
	 * @param params
	 * @return
	 */
	public long countPayOrder(Map<String, Object> params){
		return (Long) this.queryForObject(getSimpleName()+".countPayOrder", params);
	}
}
