package com.na.manager.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.na.manager.bean.Page;
import com.na.manager.bean.UserSearchRequest;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.UserStatus;
import com.na.manager.enums.UserType;

/**
 * 修改用户资料必须加上userType和ID参数。
 * Created by sunny on 2017/6/21 0021.
 */
public interface IUserService {
    User login(String loginName, String pwd, String code, String token, Integer language);
    Page<User> search(UserSearchRequest request);
    void add(User user);
    void changeStatus(Long userId, UserType userType, UserStatus status);
    void updatePassword(Long userId,UserType userType,String newPwd,String oldPwd);
    void updatePassword(Long userId,UserType userType,String newPwd);
    void updatePassword(String oldPwd,String newPwd);
    void updateUserRole(Long userId,UserType userType, List<String> roleIds);

    void update(User user);
    void update(Long userId,String headPic,String nickName);
    
    void changePassword(User user);
    /**
     * 增量更新余额
     * @param userId
     * @param userType
     * @param accountRecordType
     * @param changeType
     * @param balance
     * @param remark
     */
    void updateBalance(Long userId, UserType userType,AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark);
    
    /**
     * 覆盖更新余额
     * @param userId
     * @param userType
     * @param accountRecordType
     * @param changeType
     * @param balance
     * @param remark
     */
    void modifyBalance(Long userId, UserType userType,AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark);

    User findUserByLoginName(String loginName);
    
    User findUserById(Long userId);

    void updateUserStatus(ChildAccountUser childAccountUser);
}
