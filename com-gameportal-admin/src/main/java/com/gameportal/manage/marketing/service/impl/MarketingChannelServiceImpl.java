package com.gameportal.manage.marketing.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.manage.marketing.dao.MarketingChannelDao;
import com.gameportal.manage.marketing.model.MarketAnalysis;
import com.gameportal.manage.marketing.model.MarketingChannel;
import com.gameportal.manage.marketing.service.MarketingChannelService;

/**
 * 市场推广
 * @author Administrator
 *
 */
@Service("marketingChannelService")
public class MarketingChannelServiceImpl implements MarketingChannelService{

	@Resource(name = "marketingChannelDao")
	private MarketingChannelDao marketingChannelDao;

	@Override
	public boolean save(MarketingChannel entity) {
		// TODO Auto-generated method stub
		return marketingChannelDao.save(entity);
	}

	@Override
	public boolean update(MarketingChannel entity) {
		// TODO Auto-generated method stub
		return marketingChannelDao.update(entity);
	}

	@Override
	public boolean delete(String channelid) {
		// TODO Auto-generated method stub
		return marketingChannelDao.delete(channelid);
	}

	@Override
	public List<MarketingChannel> getList(Map<String, Object> params,Integer startNo,
			Integer pagaSize) {
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pagaSize);
		return marketingChannelDao.getList(params);
	}

	@Override
	public MarketingChannel getMarketingChannel(Map<String, Object> params) {
		return marketingChannelDao.getMarketingChannel(params);
	}

	@Override
	public Long getCount(Map<String, Object> params) {
		return marketingChannelDao.getCount(params);
	}

	@Override
	public List<MarketAnalysis> findMarketAnalysis(Map<String, Object> params) {
		List<MarketAnalysis> regList = marketingChannelDao.getRegList(params);
		List<MarketAnalysis> result = new ArrayList<MarketAnalysis>();
		if(null != regList && regList.size() > 0){
			List<MarketAnalysis> payList = marketingChannelDao.getPayList(params);
			if(null != payList && payList.size() > 0){
				MarketAnalysis resultObj = null;
				for(MarketAnalysis reg : regList){
					resultObj = reg;
					for(MarketAnalysis pay : payList){
						if(pay.getChannel().equals(reg.getChannel())){
							resultObj.setPaycount(pay.getPaycount());
							resultObj.setPayamont(pay.getPayamont());
							continue;
						}
					}
					result.add(resultObj);
				}
			}else{
				result = regList;
			}
		}
		return result;
	}
}
