package com.na.manager.util;

import java.net.InetAddress;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ESUtil {
	private Logger logger = LoggerFactory.getLogger(ESUtil.class);

	public static final String CLUSTER_NAME = "cluster-test"; // 实例名称
	private static final String IP = "192.168.0.221";
	private static final int PORT = 9300; // 端口

	private TransportClient transportClient;

	public ESUtil(String clusterName, String ip, int port) {
		if (transportClient == null) {
			try {
				Settings settings = Settings.builder().put("cluster.name", clusterName)
						.put("client.transport.sniff", true).build();
				transportClient = new PreBuiltTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
			} catch (Exception e) {
				logger.error("连接服务器异常", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构造动态查询条件。
	 * 
	 * @param paramMap
	 * @return
	 */
	public static BoolQueryBuilder buildDynamicQuery(Map<String, Object> paramMap, String logType) {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			if (!"sDate".equals(entry.getKey()) && !"eDate".equals(entry.getKey())) {
				queryBuilder.filter(QueryBuilders.termQuery("json_message." + entry.getKey(), entry.getValue()));
			}
		}
		if (paramMap.containsKey("sDate") && paramMap.containsKey("eDate")) {
			queryBuilder.filter(QueryBuilders.rangeQuery(".logindate").from(paramMap.get("sDate"))
					.to(paramMap.get("eDate")).includeLower(true).includeUpper(true));
		} else if (paramMap.containsKey("sDate") && !paramMap.containsKey("eDate")) {
			queryBuilder.filter(
					QueryBuilders.rangeQuery("json_message.logindate").from(paramMap.get("sDate")).includeLower(true));
		} else if (!paramMap.containsKey("sDate") && paramMap.containsKey("eDate")) {
			queryBuilder.filter(
					QueryBuilders.rangeQuery("json_message.logindate").to(paramMap.get("eDate")).includeUpper(true));
		}
		System.out.println(queryBuilder.toString());

		return queryBuilder;
	}

	/**
	 * 分页动态查询。
	 * 
	 * @param boolQueryBuilder
	 * @param sortField
	 *            排序字段
	 * @param sortType
	 *            排序类型
	 * @param pageIndex
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return
	 */
	public SearchResponse dynamicSearch(BoolQueryBuilder boolQueryBuilder, String sortField, SortOrder sortType,
			int pageIndex, int pageSize) {
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("filebeat*").setTypes("log")
				.setFrom((pageIndex - 1) * pageSize).setSize(pageSize).setExplain(true);
		if (StringUtils.isNotEmpty(sortField)) {
			searchRequestBuilder.addSort(SortBuilders.fieldSort(sortField).order(sortType));
		}
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response;
	}

	/**
	 * 查询总数。
	 * 
	 * @param boolQueryBuilder
	 * @return
	 */
	public long getCount(BoolQueryBuilder boolQueryBuilder) {
		SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("filebeat*").setTypes("log")
				.setExplain(true).setSearchType(SearchType.QUERY_THEN_FETCH);
		searchRequestBuilder.setQuery(boolQueryBuilder);
		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response.getHits().getTotalHits();
	}

	/**
	 * 转换结果集。
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param source
	 *            数据源串
	 * @return
	 */
	public static JSONObject parseObject(String nodeName, String source) {
		return JSON.parseObject(JSON.parseObject(source).getString(nodeName));
	}

	public TransportClient getTransportClient() {
		return transportClient;
	}

	public void setTransportClient(TransportClient transportClient) {
		this.transportClient = transportClient;
	}

	public static void test() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.filter(QueryBuilders.termQuery("login.loginStatus", 1));

		boolQueryBuilder.filter(QueryBuilders.rangeQuery("login.loginDate").from("2016-05-10 00:00:00.000")
				.to("2018-05-12 10:59:59.999").includeLower(true).includeUpper(true));
		ESUtil es = new ESUtil(CLUSTER_NAME, IP, PORT);
		System.out.println(boolQueryBuilder.toString());
		SearchResponse response = es.dynamicSearch(boolQueryBuilder, "login.loginDate", SortOrder.DESC, 1, 5);
		JSONArray jsonArray = new JSONArray();
		for (SearchHit hit : response.getHits()) {
			System.out.println(hit.getSourceAsString());
			JSONObject json = parseObject("login", hit.getSourceAsString());
			jsonArray.add(json);
		}

		System.out.println(jsonArray.toJSONString());
		System.out.println(es.getCount(boolQueryBuilder));
	}

	public static void main(String[] args) {
		test();
	}

}
