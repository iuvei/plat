package com.na.manager.service;

import com.na.manager.entity.Menu;
import com.na.manager.entity.Permission;
import com.na.manager.entity.User;

import java.util.List;

/**
 * Created by sunny on 2017/6/23 0023.
 */
public interface IPermissionService {
    List<Menu> findAllMenu();
    List<Permission> findAllPermission();
    List<Permission> findPermissionByRoleId(String roleId);
    
    List<Permission> findPermissionByChildUserId(Long userId);
    
    List<Permission> findPermissionByUserId(Long userId);
    
    void addUserPermission(User user, List<String> permissionList);
    
    void updateUserPermission(User user, List<String> permissionList);

    List<Permission> findPermissionByUserPermission(Long userId);
}
