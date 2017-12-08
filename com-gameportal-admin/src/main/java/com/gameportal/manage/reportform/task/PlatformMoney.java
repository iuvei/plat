package com.gameportal.manage.reportform.task;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gameportal.manage.reportform.model.PlatMoneyLog;
import com.gameportal.manage.reportform.model.PlatformReportForm;
import com.gameportal.manage.reportform.service.ITotalReportFormService;
import com.gameportal.manage.reportform.service.PlatMoneyLogService;

public class PlatformMoney {

	@Resource(name="platMoneyLogService")
	private PlatMoneyLogService platMoneyLogService = null;
	
	@Resource(name = "totalReportFormService")
	private ITotalReportFormService totalReportFormService;
	 
	public void run(){
		Map<String, Object> params = new HashMap<String, Object>();
		List<PlatformReportForm> list=totalReportFormService.getPlatformReport(params);
		if(CollectionUtils.isNotEmpty(list)){
			PlatMoneyLog platMoneyLog=new PlatMoneyLog();
			platMoneyLog.setCreate_date(new Date());
			platMoneyLog.setTotalmoney(list.get(0).getTotalMoney());
			platMoneyLogService.save(platMoneyLog);
		}
	}
}
