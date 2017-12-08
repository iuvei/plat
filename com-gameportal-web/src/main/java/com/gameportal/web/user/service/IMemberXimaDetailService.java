package com.gameportal.web.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.user.model.MemberXimaDetail;

public interface IMemberXimaDetailService {

	/**
	 * 查询会员洗码明细
	 * @param params
	 * @return
	 */
	public List<MemberXimaDetail> queryMemberXimaDetailList(Map<String, Object> params);
	
	/**
	 * 分页总计
	 * @param params
	 * @return
	 */
	public long queryMemberXimaDetailCount(Map<String, Object> params);
}
