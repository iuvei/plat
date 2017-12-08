package com.gameportal.manage.marketing.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.marketing.model.MarketAnalysis;
import com.gameportal.manage.marketing.model.MarketingChannel;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 市场推广渠道
 * @author Administrator
 *
 */
@SuppressWarnings("all")
@Component
public class MarketingChannelDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return MarketingChannel.class;
	}
	
	public boolean save(MarketingChannel entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true
				: false;
	}
	
	public boolean update(MarketingChannel entity){
		return super.update(entity);
	}
	
	public boolean delete(String channelid){
		return super.delete(channelid);
	}
	
	public List<MarketingChannel> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public Long getCount(Map<String, Object> params){
		return super.getRecordCount(params);
	}
	
	public MarketingChannel getMarketingChannel(Map<String, Object> params){
		List<MarketingChannel> list = this.getList(params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 查询各个渠道的注册人数
	 * @param params
	 * @return
	 */
	public List<MarketAnalysis> getRegList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectReglist", params);
	}
	
	/**
	 * 统计各渠道充值人数和充值金额
	 * @param params
	 * @return
	 */
	public List<MarketAnalysis> getPayList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectPaylist", params);
	}

}
