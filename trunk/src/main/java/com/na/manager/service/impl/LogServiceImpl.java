package com.na.manager.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.na.manager.bean.LogSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.ApiLogVO;
import com.na.manager.bean.vo.LoginLogVO;
import com.na.manager.cache.AppCache;
import com.na.manager.service.ILogService;
import com.na.manager.util.ESUtil;

/**
 * 操作日志服务类。
 * @author andy
 * @date 2017年6月23日 下午3:29:45
 * 
 */
@Service
public class LogServiceImpl implements ILogService{
	private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
	@Value("${cluster.name}")
	private String clusterName;
	@Value("${cluster.ip}")
	private String ip;
	@Value("${transport.tcp.port}")
	private int port;
	
	@Override
	public Page<LoginLogVO> searchLoginLog(LogSearchRequest searchRequest) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		//只能看到自己或者其下级的登陆日志
		qb.should(QueryBuilders.wildcardQuery("login.parentPath","*/" + AppCache.getCurrentUser().getId() + "*")).should(QueryBuilders.termQuery("login.loginName",AppCache.getCurrentUser().getLoginName()));
		queryBuilder.must(qb);
		
		if (searchRequest.getStatus() != null && searchRequest.getStatus() !=-1) {
			queryBuilder.filter(QueryBuilders.termQuery("login.loginStatus",searchRequest.getStatus()));
		}
		if (StringUtils.isNotEmpty(searchRequest.getIp())) {
			queryBuilder.filter(QueryBuilders.termQuery("login.ipAddr",searchRequest.getIp()));
		}
		if (StringUtils.isNotEmpty(searchRequest.getUserName())) {
			queryBuilder.filter(QueryBuilders.termQuery("login.loginName",searchRequest.getUserName()));
		}
		if (searchRequest.getType() !=-1) {
			queryBuilder.filter(QueryBuilders.termQuery("login.type",searchRequest.getType()));
		}
		
		queryBuilder.filter(QueryBuilders.rangeQuery("login.loginDate")
					.from(searchRequest.getStartTime()).to(searchRequest.getEndTime()).includeLower(true).includeUpper(true));
		logger.info(queryBuilder.toString());
		JSONArray jsonArray = new JSONArray();
		SearchResponse response = null;
		try {
			ESUtil esUtil= new ESUtil(clusterName, ip, port);
			response = esUtil.dynamicSearch(queryBuilder, "login.loginDate", SortOrder.DESC, (int)searchRequest.getCurrentPage(), (int)searchRequest.getPageSize());
			for (SearchHit hit : response.getHits()) {
				JSONObject json = ESUtil.parseObject("login",hit.getSourceAsString());
				jsonArray.add(json);
			}
			Page<LoginLogVO> page = new Page<>(searchRequest);
			page.setTotal(esUtil.getCount(queryBuilder));
			page.setData(JSON.parseArray(jsonArray.toJSONString(),LoginLogVO.class));
			return page;
		} catch (Exception e) {
			logger.error("查询登录日志异常：",e);
			throw new RuntimeException("日志服务器连接失败！");
		}
	}

	@Override
	public Page<ApiLogVO> searchApiLog(LogSearchRequest searchRequest) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.isNotEmpty(searchRequest.getMerchantNo())) {
			queryBuilder.filter(QueryBuilders.termQuery("api.merchantNo",searchRequest.getMerchantNo()));
		}
		if (StringUtils.isNotEmpty(searchRequest.getIp())) {
			queryBuilder.filter(QueryBuilders.termQuery("api.ip",searchRequest.getIp()));
		}
		if (searchRequest.getStatus() != -1) {
			queryBuilder.filter(QueryBuilders.termQuery("api.status",searchRequest.getStatus()));
		}
		queryBuilder.filter(QueryBuilders.rangeQuery("api.createTime")
				.from(searchRequest.getStartTime()).to(searchRequest.getEndTime()).includeLower(true).includeUpper(true));
		logger.info(queryBuilder.toString());
		JSONArray jsonArray = new JSONArray();
		SearchResponse response = null;
		Page<ApiLogVO> page = new Page<>(searchRequest);
		try {
			ESUtil esUtil= new ESUtil(clusterName, ip, port);
			response = esUtil.dynamicSearch(queryBuilder, "api.createTime", SortOrder.DESC, (int)searchRequest.getCurrentPage(), (int)searchRequest.getPageSize());
			for (SearchHit hit : response.getHits()) {
				JSONObject json = ESUtil.parseObject("api",hit.getSourceAsString());
				jsonArray.add(json);
			}
			page.setTotal( esUtil.getCount(queryBuilder));
			page.setData(JSON.parseArray(jsonArray.toJSONString(),ApiLogVO.class));
			return page;
		} catch (Exception e) {
			logger.error("查询登录日志异常：",e);
			throw new RuntimeException("日志服务器连接失败。");
		}
	}

}
