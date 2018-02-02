package com.na.manager.bean;

import java.util.List;

/**
 * Created by sunny on 2017/6/23 0023.
 */
public class UserUpdateRoleRequest {
    private Long userId;
    private List<String> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
