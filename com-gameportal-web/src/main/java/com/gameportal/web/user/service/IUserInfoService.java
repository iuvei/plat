package com.gameportal.web.user.service;

import java.util.List;
import java.util.Map;

import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.Activity;
import com.gameportal.web.user.model.BetLog;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserLoginLog;
import com.gameportal.web.user.model.UserSign;

public abstract interface IUserInfoService {

    public abstract UserInfo saveUserInfo(UserInfo userInfo) throws Exception;

    public abstract UserInfo queryUserInfo(String userName);

    public abstract UserInfo queryUserInfo(String account, String phone,
            String email, Integer status);

    public abstract UserInfo queryUserInfo(String userName, Integer status);

    public abstract UserInfo findUserInfoId(Long uiid);

    public abstract UserInfo findUserInfoId(Long uiid, Integer status);

    public abstract UserInfo findProxyInfoId(Long uiid, Integer status);

    public abstract boolean modifyUserInfo(UserInfo userInfo) throws Exception;

    public CardPackage saveCardPackage(CardPackage cardPackage)
            throws Exception;

    public abstract boolean updateCardPackage(CardPackage cardPackage);

    public CardPackage queryCardPackage(Long uiid, Integer status);

    public List<CardPackage> queryUserInfoCardPackage(Long uiid,
            Integer startNo, Integer pagaSize);
    
    public List<CardPackage> queryCardPackage(Map<String, Object> params);

    public List<CardPackage> queryUserInfoCardPackage(Long uiid,
            Integer status, Integer startNo, Integer pagaSize);

    public Long getCardPackageCount(Long uiid);

    long getUserInfoCount(Map<String, Object> param);

    public Long getCardPackageCount(Long uiid, Integer status);

    public Long getAccountMoneyCount(Long uiid, Integer status);

    public abstract Long getAccountMoney(Long uiid, Integer status);

    public abstract AccountMoney getAccountMoneyObj(Long uiid, Integer status);

    public boolean saveAccountMoney(AccountMoney accountMoney);

    public abstract boolean updateAccountMoneyObj(AccountMoney accountMoney);

    public abstract String queryProxyID(String url);

    /**
     * 条件查询代理域名信息
     * @param map
     * @return
     */
    List<Map<String, Object>> selectProxyUrl(Map<String, Object> map);
    /**
     * 查询游戏注单
     * @return
     */
    public List<BetLog> selectGameOrder(Map<String, Object> params);
    public Long selectGameOrderCount(Map<String, Object> params);
    public int updateLogin(UserInfo userInfo);

    public void insertLog(UserLoginLog entity);

    /**
     * 修改用户信息。
     * @param userInfo
     * @return
     */
    boolean updateUserInfo(UserInfo userInfo);

    /**
     * 获取电销列表
     * @return
     */
    public List<String> getDXList();

    public List<Activity> getList(Map<String, Object> params);

    Activity getActivity(Map<String, Object> params);

    /**
     * 通过条件获取用户的关联账号
     * @param params
     * @return
     */
    List<String> selectUserLoginLog(Map<String, Object> params);
    
    /**
     * 查询用户登录过的IP
     * @param params
     * @return
     */
    List<String> selectUserLoginIps(Map<String, Object> params);

    /**
     * 根据条件查询游戏前一定数量的注单
     * @return
     */
    public List<BetLog> selectGameBetLog(Map<String, Object> params);

    /**
     * 根据条件查询游戏前一定数量的注单  并将数据转换成固定格式的字符串集合
     * @param params
     * @return
     */
    public List<String> selWinningTopBetLog(Map<String, Object> params);
    
    /**
     * 查询所有有转账到MG记录的用户
     * @return
     */
    public List<UserInfo> selectUserOfTransferMg();
    
    
    public abstract boolean updateTotalamount(AccountMoney accountMoney);
    
    boolean cancelWithdraw(Map<String, Object> params);
    
    /**
     * 新增会员签到记录
     * @param sign
     */
    public void insertUserSign(UserSign sign);
    
    
    /**
     * 更新会员签到状态
     * @param sign
     */
    public void updateUserSign(UserSign sign);
    
    /**
     * 查询签到记录数
     */
    public List<UserSign> queryUserSignList(Map<String,Object> param);
}
