package com.gameportal.web.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gameportal.web.user.model.ProxySet;
import com.gameportal.web.user.service.IMemberXimaMainService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.SpringUtils;

import net.sf.json.JSONArray;

/**
 * 代理按天洗码结算。
 * @author Administrator
 *
 */
public class ProxyDayClearingMemberXimaTask {
	private static final Logger logger = Logger.getLogger(ProxyDayClearingMemberXimaTask.class);
	private IMemberXimaMainService memberXimaMainServiceImpl;
	/**
	 * 代理每天洗码。
	 */
	private List<ProxySet> proxyList;
	
	public void run(){
		String betDate = DateUtil.FormatDate(DateUtil.addDay(new Date(), -1)); // 昨天日期
		logger.info("代理按天洗码结算开始。");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", betDate);
		map.put("endDate", betDate);
		try {
			memberXimaMainServiceImpl = (IMemberXimaMainService) SpringUtils.getBean("memberXimaMainServiceImpl");
			
			proxyList = memberXimaMainServiceImpl.selectProxyDayClearing(map);
			if(CollectionUtils.isEmpty(proxyList)){
				logger.info("按天洗码代理列表为空");
				return;
			}
			logger.info("按天洗码代理列表："+JSONArray.fromObject(proxyList).toString());
			if (CollectionUtils.isNotEmpty(proxyList)) {
				memberXimaMainServiceImpl.saveProxyDayClearing(map, proxyList);
			}
			logger.info("代理按天洗码结算结束。");
		} catch (Exception e) {
			logger.error("代理按天洗码结算异常。", e);
		}
	}
}
