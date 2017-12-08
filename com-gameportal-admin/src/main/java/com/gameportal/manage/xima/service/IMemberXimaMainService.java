package com.gameportal.manage.xima.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.xima.model.MemberXimaMain;

public abstract interface IMemberXimaMainService {

	String saveOrUpdateMemberXimaMain(Map<String, Object> params);

	boolean deleteMemberXimaMain(Long id);

	Long queryMemberXimaMainCount(Map<String, Object> params);

	List<MemberXimaMain> queryMemberXimaMain(Map<String, Object> params,
			Integer startNo, Integer pageSize);

	/**
	 * 会员洗码记录管理 做报表
	 * @param params
	 * @return
	 */
	List<MemberXimaMain> queryMemberXimaMain(Map<String, Object> params);
	
	boolean clearXima(MemberXimaMain mxm, Date ymdstartDate, Date ymdendDate,
			SystemUser systemUser);

	boolean forceXima(MemberXimaMain mxm, Date ymdstartDate, Date ymdendDate,
			SystemUser systemUser);

	boolean normalXima(MemberXimaMain mxm, Date ymdstartDate, Date ymdendDate);
	
	/**
	 * 通过审核
	 * @param mxm
	 * @return
	 */
	String checkBy(MemberXimaMain mxm)throws Exception;

}
