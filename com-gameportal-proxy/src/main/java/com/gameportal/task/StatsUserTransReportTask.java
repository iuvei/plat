package com.gameportal.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gameportal.comms.DateUtil;
import com.gameportal.domain.DownUserReportTotal;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.UserTranDailyReport;
import com.gameportal.service.IMemberInfoService;
import com.gameportal.service.IUserTranDailyReportService;

/**
 * 用户每日交易报表。
 * @author Administrator
 *
 */
public class StatsUserTransReportTask {
	@Autowired
	private IUserTranDailyReportService userTranDailyReportService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	
	private List<MemberInfo> memberList;
	
	private List<UserTranDailyReport> transList;
	
	public void run(){
		transList = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D)+" 00:00:00");
		param.put("endDate", DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D)+" 23:59:59");
		// 统计昨日交易玩家
		memberList = memberInfoService.getTransByDate(param);
		MemberInfo info = null;
		UserTranDailyReport trans = null;
		Map<String, Object> map = new HashMap<>();
		for(MemberInfo member :memberList){
			map.clear();
			map.put("account", member.getAccount());
			info = memberInfoService.queryMemberInfo(map);
			if(info ==null) continue;
			trans = new UserTranDailyReport();
			trans.setPuid(info.getPuiid()==null?null:info.getPuiid().toString());
			trans.setAccount(info.getAccount());
			trans.setUiid(info.getUiid());
			trans.setRealName(info.getUname());
			param.put("account", info.getAccount());
			param.put("uiid", info.getUiid());
			DownUserReportTotal result =memberInfoService.queryUserDailyReport(param);
			trans.setDeposit(new BigDecimal(result.getDepositTotal()));
			trans.setWithdrawal(new BigDecimal(result.getWinLossTotal()));
			trans.setPreferential(new BigDecimal(result.getPreferentialTotal()));
			trans.setWashCode(new BigDecimal(result.getXimaTotal()));
			trans.setFinalAmount(new BigDecimal(result.getWinLossTotal()));
			trans.setCreateDate(DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D));
			transList.add(trans);
		}
		userTranDailyReportService.insertTransDailyStatList(transList);
		System.out.println("会员昨日交易统计完成！");
	}
}
