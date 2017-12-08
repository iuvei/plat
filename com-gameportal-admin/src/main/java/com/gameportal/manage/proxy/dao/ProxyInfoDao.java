package com.gameportal.manage.proxy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.manage.proxy.model.ProxyInfo;
import com.gameportal.manage.proxy.model.ProxyReportEntity;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class ProxyInfoDao extends BaseIbatisDAO {

	@Override
	public Class<ProxyInfo> getEntityClass() {
		return ProxyInfo.class;
	}

	
	public boolean saveOrUpdate(ProxyInfo entity) {
//		if(entity.getPyid() == null) 
//			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
//					: false;
//		else 
//			return update(entity);
		return false;
	}
	
	
	public List<ProxyInfo> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	/**
	 * 获取代理详细信息
	 * @param params
	 * @return
	 */
	public ProxyReportEntity getUserMsg(Map<String, Object> params){
		List<ProxyReportEntity> list = super.getSqlMapClientTemplate().queryForList(getSimpleName()+".getUserMsg",params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取代理报表信息
	 * @param params
	 * @return
	 */
	public ProxyReportEntity getProxySpreadInfo(Map<String, Object> params){
		return (ProxyReportEntity)super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".getProxySpreadInfo",params);
	}
	
	/**
	 * 获取代理报表投注信息
	 * @param params
	 * @return
	 */
	public ProxyReportEntity getProxyBetInfo(Map<String, Object> params){
		return (ProxyReportEntity)super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".getProxyBetInfo",params);
	}
	
	/**
	 * 统计代理线下首存会员存款总额
	 * @param params
	 * @return
	 */
	public Long selectProxyFcPayOrderTotal(Map<String, Object> params){
		return super.getRecordCount(getSimpleName()+".selectProxyFcPayOrderTotal", params);
	}
}
