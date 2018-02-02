package com.na.manager.bean;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
public class DealerUserSearchRequest extends PageCondition{
    private String loginName;
    private Integer type;
    private Integer userStatus;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }
}
