package com.na.user.socketserver.service;

import java.math.BigDecimal;
import java.util.List;

import com.na.baccarat.socketserver.entity.LoginStatus;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
public interface IUserService {
	
	
	public UserPO login(Long userId);
	
    public UserPO login(String userName, String password, Integer userType);
    public UserPO login(String barcode);
    
    public void demoUpdate(UserPO user);
    public LiveUserPO getUserById(Long userId);
    public LoginStatus getLoginStatus(LoginStatus loginStatus);
    public void addLoginStatus(LoginStatus loginStatus);

    /**
     * 查询制定限红列表。
     * @param cids 限红主键（多个用,隔开）
     * @return
     */
    public List<UserChipsPO> getUserChips(String cids);

    /**
     * 退出操作。
     * @param user
     */
    public void logout(UserPO user);

    /**
     * 更新用户余额。<br>
     * 只更新余额与洗码额，增量式更新。
     * @param uid
     * @param betAmount
     */
    public void updateUserBalance(Long uid, BigDecimal betAmount, String affect);
    
    /**
     * 批量更新用户余额
     * @param userPO
     */
    public void batchUpdateBalance(List<UserPO> userPOList, String affect);
    
    /**
     * 根据名称寻找下级账户
     * @param loginName
     * @return
     */
    public List<LiveUserPO> getLowerLevelByName(String loginName);
    
    /**
     * 根据名称寻找账户
     * @param loginName
     * @return
     */
    public UserPO getUserByLoginName(String loginName);
    
    /**
     * 根据名称寻找账户
     * @param loginName
     * @return
     */
    public LiveUserPO getLiveUserByLoginName(String loginName);
    
    /**
     * 通过userid查询代理团队用户ID
     * @param userIds
     * @return
     */
	public List<Long> listUserIdByParentPath(String[] userIds);
	
	/**
     * 通过userid查询代理直线用户ID
     * @param userIds
     * @return
     */
	public List<Long> listUserIdBySuperiorID(String[] userIds);
	
	/**
	 * 修改个人资料
	 * @param userIds
	 * @return
	 */
	public void modifyUserInfo(UserPO userPO);
	
	/**
	 * 修改用户状态
	 * @param status
	 */
	public void updateStatus(UserPO userPO);
}
