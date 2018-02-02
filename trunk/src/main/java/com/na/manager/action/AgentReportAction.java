package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.AgentWinLostReportRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.User;
import com.na.manager.enums.StatisType;
import com.na.manager.service.IAgentWinLostService;
import com.na.manager.service.ILiveUserService;

/**
 * 输赢报表
 * 
 * @author andy
 * @date 2017年6月24日 上午10:31:56
 * 
 */
@RestController
@RequestMapping("/agentReport")
@Auth("SearchProfitReport")
public class AgentReportAction {

	@Autowired
	private IAgentWinLostService agentWinLostService;

	@Autowired
	private ILiveUserService liveUserService;

	@PostMapping("/queryAgentWinLostReport")
	public NaResponse<AgentWinLostReportRequest> search(@RequestBody AgentWinLostReportRequest agentReportRequest) {
		User currentUser = AppCache.getCurrentUser();
		LiveUser liveUser =null;
		// 登录名
		if (agentReportRequest.getUserId() !=null) {
			liveUser = liveUserService.findById(agentReportRequest.getUserId());
		}else{ // 子帐号,查询上级数据
			if(currentUser instanceof ChildAccountUser){
				ChildAccountUser childAccountUser = (ChildAccountUser)currentUser;
				liveUser = childAccountUser.getParentUser();
			}else if(currentUser instanceof LiveUser){
				liveUser = (LiveUser)currentUser;
			}else{
				return NaResponse.createError("暂无权限");
			}
		}
		
		agentReportRequest.setUserId(liveUser.getId());
		if (agentReportRequest.getType() == StatisType.TEAM.get()) {
			agentReportRequest.setUserPath(liveUser.getParentPath());
			return NaResponse.createSuccess(agentWinLostService.statisAgentTeam(agentReportRequest));
		}else if (agentReportRequest.getType() == StatisType.AGENT.get()) {
			agentReportRequest.setUserPath(liveUser.getParentPath());
			return NaResponse.createSuccess(agentWinLostService.statisDirectProxyAndMember(agentReportRequest,liveUser));
		}else if (agentReportRequest.getType() == StatisType.MEMBERSUM.get()) {
			return NaResponse.createSuccess(agentWinLostService.listDirectdMember(agentReportRequest));
		}else{
			return NaResponse.createSuccess(agentWinLostService.statisAgentTeam(agentReportRequest));
		}
	}

}
