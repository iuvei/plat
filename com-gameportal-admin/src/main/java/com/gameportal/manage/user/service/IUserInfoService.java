package com.gameportal.manage.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.UserInfoRemark;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.CardPackage;
import com.gameportal.manage.user.model.UserInfo;

public abstract interface IUserInfoService {

	boolean saveOrUpdateUserInfo(UserInfo member);

	boolean deleteUserInfo(Long uiid);

	boolean updateStatus(Long uiid, Integer status);

	boolean resetPwd(Integer type, Long userId);

	Long queryMemberCount(String key);

	List<UserInfo> queryMember(String key, Integer startNo, Integer pageSize);

	Long queryUserinfoCount(Map<String, Object> params);

	List<UserInfo> queryUserinfo(Map<String, Object> params, Integer startNo,
			Integer pageSize);
	
	public UserInfo queryUserInfo2(Map<String, Object> params);

	Long queryAboveCount(Map<String, Object> params);

	List<UserInfo> queryAbove(Map<String, Object> params, Integer startNo,
			Integer pageSize);

	UserInfo queryById(Long uiid);
	
	CardPackage queryCardPackage(Long uiid,Integer status);
	
	void saveRemark(UserInfoRemark remark);
	
	boolean updateTotalamount(AccountMoney am);
	
	List<UserInfoRemark> getRemarkList(Map<String, Object> map, Integer startNo, Integer pageSize);
}
