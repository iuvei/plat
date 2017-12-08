package com.gameportal.manage.reportform.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gameportal.manage.member.model.DailyReportDetail;
import com.gameportal.manage.reportform.model.ReportForm;
import com.gameportal.manage.reportform.service.IReportFormService;
import com.gameportal.manage.util.DateUtil;

/**
 * 每日报表统计
 * @author Administrator
 *
 */
public class DailyReport {
	public static Logger logger = Logger.getLogger(DailyReport.class);
	@Resource(name="reportFormService")
	private IReportFormService reportFormService; 
	
	public void run(){
		Map<String,Object> map=new HashMap<String,Object>();
		Date yesterDay = DateUtil.addDay(new Date(), -1);
		map.put("startDate", DateUtil.FormatDate(yesterDay));
		map.put("endDate", DateUtil.FormatDate(yesterDay));
		ReportForm report= reportFormService.getReportResult(map,0,2).get(0);
		Long start=System.currentTimeMillis();
		DailyReportDetail daily = new DailyReportDetail();
		//统计当前首冲总金额
		map.put("nowDate", DateUtil.FormatDate(yesterDay));
		String firstPayMoney=reportFormService.getFirstPayMoney(report.getReporttime());
		if(firstPayMoney==null){
			firstPayMoney="0.00";
		}
		//统计当前有效投注额
		String realbetMoney=reportFormService.getRealBetMoney(report.getReporttime());
		if(realbetMoney==null){
			realbetMoney="0.00";
		}
		daily.setRealBetMoney(new BigDecimal(realbetMoney));
		
		daily.setMemberCoupon(new BigDecimal(report.getMemberCoupon()));
		daily.setProxyCoupon(new BigDecimal(report.getProxyCoupon()));
		daily.setPayOrderXimaMoney(new BigDecimal(report.getPayOrderXimaMoney()));
		daily.setProxyXimaMoney(new BigDecimal(report.getProxyXimaMoney()));
		daily.setProxyClearMoney(new BigDecimal(report.getProxyClearMoney()));
		daily.setPayMoney(new BigDecimal(report.getPayMoney()));
		daily.setPayMoneyPerson(report.getPayMoneyPerson());
		daily.setPickUpMoney(new BigDecimal(report.getPickUpMoney()));
		daily.setPickUpMoneyPerson(report.getPickUpMoneyPerson());
		daily.setPayMoneyCount(report.getPayMoneyCount());
		daily.setPickUpMoneyCount(report.getPickUpMoneyCount());
		
		
		// 活跃人数(有玩过游戏的会员人数)
		long activeCount = reportFormService.selectActiveCount(map);
		daily.setActiveCount(activeCount);
		
		daily.setReporttime(DateUtil.getDateByStr(report.getReporttime()));
		daily.setRegisterNumber(report.getRegisterNumber());
		daily.setFirstPayNumber(report.getFirstPayNumber());
		daily.setFirstPayTotalMoney(new BigDecimal(firstPayMoney));
		
		// 查询正常登录人数
		long loginCount = reportFormService.selectLoginNumberCount(map);
		daily.setLoginCount(loginCount);
		reportFormService.insertStatDailyReport(daily);
		Long end=System.currentTimeMillis();
		logger.info("统计每日报表运行时间:"+(end-start)+"毫秒");
	}
}
