package com.gameportal.manage.member.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.member.dao.MemberGradeDao;
import com.gameportal.manage.member.model.MemberGrade;
import com.gameportal.manage.member.service.IMemberGradeService;

@Service
public class MemberGradeServiceImpl implements IMemberGradeService {

	@Resource(name = "memberGradeDao")
	private MemberGradeDao memberGradeDao = null;
	
	@Override
	public MemberGrade findMemberGradeId(Long uiid) {
		return (MemberGrade) memberGradeDao.findById(uiid);
		
	}
	
	@Override
	public boolean saveMemberGrade(MemberGrade memberGrade) throws Exception {
		return memberGradeDao.saveOrUpdate(memberGrade);
		
	}

	@Override
	public boolean deleteMemberGrade(Long uiid) {
		return	memberGradeDao.delete(uiid);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<MemberGrade> queryMemberGrade(Map paramMap, Integer startNo,
			Integer pagaSize) {

		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List<MemberGrade> list = memberGradeDao.getList(paramMap);
		return list;
	}

	@Override
	public Long queryMemberGradeCount(Map map) {
		return memberGradeDao.getRecordCount(map);
	}
	
	

	







	

}
