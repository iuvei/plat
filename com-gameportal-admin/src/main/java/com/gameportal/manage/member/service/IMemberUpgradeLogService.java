package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.MemberUpgradeLog;

public interface IMemberUpgradeLogService {
	public abstract boolean saveMemberUpgradeLog(MemberUpgradeLog memberUpgradeLog) throws Exception;

	public abstract MemberUpgradeLog findMemberUpgradeLogId(Long uiid);

	public abstract boolean deleteMemberUpgradeLog(Long uiid) ;
	
	public abstract List<MemberUpgradeLog> queryMemberUpgradeLog(Map map,Integer startNo, Integer pagaSize);
	
	public abstract Long queryMemberUpgradeLogCount(Map map);
	

	
}
