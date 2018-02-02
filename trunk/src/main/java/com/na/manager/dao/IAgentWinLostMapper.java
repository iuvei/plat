package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.AgentWinLostReportRequest;
import com.na.manager.bean.vo.AgentWinLostVO;

/**
 * @author andy
 * @date 2017年6月24日 上午10:34:48
 * 
 */
@Mapper
public interface IAgentWinLostMapper {
	
	List<AgentWinLostVO> statisAgentTeam(AgentWinLostReportRequest agentReportRequest);
	
	List<AgentWinLostVO> statisDirectProxy(AgentWinLostReportRequest agentReportRequest);
	
	AgentWinLostVO statisDirectMember(AgentWinLostReportRequest agentReportRequest);
	
	List<AgentWinLostVO> listDirectdMember(AgentWinLostReportRequest agentReportRequest);

}
