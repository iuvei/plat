package com.gameportal.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gameportal.comms.DateUtil;
import com.gameportal.domain.BetLogTotal;
import com.gameportal.domain.MemberInfo;
import com.gameportal.domain.UserBetDailyStats;
import com.gameportal.domain.UserXimaSet;
import com.gameportal.service.IMemberInfoService;
import com.gameportal.service.IUserBetDailyStatsService;

/**
 * 用户每日投注统计。
 * @author Administrator
 *
 */
public class StatsUserBetTask{
	@Autowired
	private IUserBetDailyStatsService userBetDailyStatsService;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	private List<MemberInfo> memberList;
	
	private List<UserBetDailyStats> betList;
	
	public void run(){
		betList = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		param.put("startDate", DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D)+" 00:00:00");
		param.put("endDate", DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D)+" 23:59:59");
		// 统计昨日投注玩家
		memberList = memberInfoService.getPlayerByDate(param);
		MemberInfo info = null;
		UserBetDailyStats bet = null;
		Map<String, Object> map = new HashMap<>();
		for(MemberInfo member :memberList){
			map.clear();
			map.put("account", member.getAccount());
			info = memberInfoService.queryMemberInfo(map);
			if(info ==null) continue;
			bet = new UserBetDailyStats();
			bet.setPuid(info.getPuiid()==null?null:info.getPuiid().toString());
			bet.setAccount(info.getAccount());
			bet.setRealName(info.getUname());
			bet.setUiid(info.getUiid());
			param.put("account", info.getAccount());
			BetLogTotal result =memberInfoService.userBetDailyStats(param);
			bet.setBetCount(Integer.valueOf(result.getBetTotel()));
			bet.setBetAmount(new BigDecimal(result.getBetAmountTotal()));
			bet.setPayoutAmount(new BigDecimal(result.getProfitamountTotal()));
			bet.setFinalAmount(new BigDecimal(result.getFinalamountTotal()));
			bet.setValidAmount(new BigDecimal(result.getValidBetAmountTotal()));
			bet.setCreateDate(DateUtil.format(DateUtil.getAddDate(new Date(), -1),DateUtil.DATE_PATTERN_D));
			map.clear();
			map.put("uiid", info.getUiid());
			UserXimaSet set =memberInfoService.queryUserXimaSet(map);
			if(set == null){
				bet.setScale(0.00);
			}else{
				bet.setScale(Double.valueOf(set.getXimascale()));
			}
			betList.add(bet);
		}
		
		userBetDailyStatsService.insertBetDailyStatList(betList);
		System.out.println("会员昨日投注统计完成！");
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal("[B@31baf647"));
	}
}
