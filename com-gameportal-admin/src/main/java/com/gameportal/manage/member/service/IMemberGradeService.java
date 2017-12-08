package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.MemberGrade;

public interface IMemberGradeService {
	public abstract boolean saveMemberGrade(MemberGrade memberGrade) throws Exception;

	public abstract MemberGrade findMemberGradeId(Long uiid);

	public abstract boolean deleteMemberGrade(Long uiid) ;
	
	public abstract List<MemberGrade> queryMemberGrade(Map map,Integer startNo, Integer pagaSize);
	
	public abstract Long queryMemberGradeCount(Map map);
	

	
}
