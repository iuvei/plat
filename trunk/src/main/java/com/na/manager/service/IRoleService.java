package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.Page;
import com.na.manager.bean.RoleSearchRequest;
import com.na.manager.bean.RoleUpdateRequest;
import com.na.manager.entity.Role;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
public interface IRoleService {
    Page<Role> search(RoleSearchRequest condition );
    void changeStatus(Role role);

    /**
     * 更新角色信息及权限。
     * @param request
     */
    void update(RoleUpdateRequest request);

    /**
     * 增加权限并同时授权。
     * @param request
     */
    void add(RoleUpdateRequest request);
    
    void addUserRole(String roleID, Long userID);

    List<Role> getRoleByUserId(Long userId);
    
    Role findRoleByName(String roleName);

    List<Role> getAllRole();

}
