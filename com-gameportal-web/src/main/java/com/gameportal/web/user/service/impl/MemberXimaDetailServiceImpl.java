package com.gameportal.web.user.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.user.dao.MemberXimaDetailDao;
import com.gameportal.web.user.model.MemberXimaDetail;
import com.gameportal.web.user.service.IMemberXimaDetailService;

@Service("memberXimaDetailServiceImpl")
public class MemberXimaDetailServiceImpl implements IMemberXimaDetailService {

	@Resource(name = "memberXimaDetailDao")
	private MemberXimaDetailDao memberXimaDetailDao;
	
	@Override
	public List<MemberXimaDetail> queryMemberXimaDetailList(
			Map<String, Object> params) {
		return memberXimaDetailDao.findMemberXimaDetailList(params);
	}
	
	@Override
	public long queryMemberXimaDetailCount(Map<String, Object> params) {
		return memberXimaDetailDao.getRecordCount(params);
	}
}
