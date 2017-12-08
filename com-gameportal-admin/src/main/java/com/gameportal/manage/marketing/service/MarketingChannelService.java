package com.gameportal.manage.marketing.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.marketing.model.MarketAnalysis;
import com.gameportal.manage.marketing.model.MarketingChannel;

public interface MarketingChannelService {

	public boolean save(MarketingChannel entity);
	public boolean update(MarketingChannel entity);
	public boolean delete(String channelid);
	public List<MarketingChannel> getList(Map<String, Object> params,Integer startNo,Integer pagaSize);
	public Long getCount(Map<String, Object> params);
	public MarketingChannel getMarketingChannel(Map<String, Object> params);
	public List<MarketAnalysis> findMarketAnalysis(Map<String, Object> params);
}
