package com.na.manager.service;

import java.math.BigDecimal;
import java.util.List;

import com.na.manager.bean.vo.AccountVO;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserStatus;
import com.na.manager.enums.UserType;


/**
 * Created by sunny on 2017/6/21 0021.
 */
public interface ILiveUserService {
	
	LiveUser findById(Long id);
	
	LiveUser findByLoginName(String loginName);
	
	List<LiveUser> findLiveUserByParentId(Long parentId);
	
	List<AccountVO> findLiveUserByParentIdForPage(Long parentId, Long startRow, Long pageSize);
	
	long countByParentId(Long parentId);
	
	String getParentPathName(String parentPath);
	
	List<AccountVO> findByParentId(Long parentId);
	
	/**
	 * 找到所有下级在线用户
	 * @param parentId
	 * @return
	 */
	List<AccountVO> findOnlineAllUserByParentId(String parentPath);
	
	List<AccountVO> search(LiveUser liveUser);
	
	void add(LiveUser liveUser);
	
	void betchAdd(List<LiveUser> liveUserList);
	
	void update(LiveUser liveUser);
	
	LiveUser modifyStatus(Long id, LiveUserType liveUserType, UserStatus userStatus);
	
	LiveUser modifyBetStatus(Long id, LiveUserType liveUserType, LiveUserIsBet isBet);
	
	void updateBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance, String remark);
	
	void modifyBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance, String remark);
	
	LiveUser findLiveUserByUserName(String userName);
	
	/**
	 * 查询下级用户在线人数
	 * @param agentName  代理名称
	 * @return
	 */
	int countOnlineByParentName(String agentName);
}
