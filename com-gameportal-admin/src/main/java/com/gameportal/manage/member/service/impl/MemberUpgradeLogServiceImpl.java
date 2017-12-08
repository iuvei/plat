package com.gameportal.manage.member.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.gameportal.manage.member.dao.MemberUpgradeLogDao;
import com.gameportal.manage.member.model.MemberUpgradeLog;
import com.gameportal.manage.member.service.IMemberUpgradeLogService;

@Service
public class MemberUpgradeLogServiceImpl implements IMemberUpgradeLogService {

	@Resource(name = "memberUpgradeLogDao")
	private MemberUpgradeLogDao memberUpgradeLogDao = null;
	
	@Override
	public MemberUpgradeLog findMemberUpgradeLogId(Long uiid) {
		return (MemberUpgradeLog) memberUpgradeLogDao.findById(uiid);
		
	}
	
	@Override
	public boolean saveMemberUpgradeLog(MemberUpgradeLog memberUpgradeLog) throws Exception {
		return memberUpgradeLogDao.saveOrUpdate(memberUpgradeLog);
		
	}

	@Override
	public boolean deleteMemberUpgradeLog(Long uiid) {
		return	memberUpgradeLogDao.delete(uiid);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<MemberUpgradeLog> queryMemberUpgradeLog(Map paramMap, Integer startNo,
			Integer pagaSize) {
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List<MemberUpgradeLog> list = memberUpgradeLogDao.getList(paramMap);
		return list;
	}

	@Override
	public Long queryMemberUpgradeLogCount(Map map) {
		return memberUpgradeLogDao.getRecordCount(map);
	}


	

	







	

}
