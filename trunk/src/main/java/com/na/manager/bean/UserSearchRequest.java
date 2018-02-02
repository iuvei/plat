package com.na.manager.bean;

/**
 * Created by Administrator on 2017/6/23 0023.
 */
public class UserSearchRequest extends PageCondition{
    private String loginName;
    private Integer userStatus;
    private Integer userType;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
