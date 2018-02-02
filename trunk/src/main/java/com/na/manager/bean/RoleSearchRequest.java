package com.na.manager.bean;

/**
 * Created by sunny on 2017/6/22 0022.
 */
public class RoleSearchRequest extends PageCondition{
    private String roleName;
    private Integer status;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
