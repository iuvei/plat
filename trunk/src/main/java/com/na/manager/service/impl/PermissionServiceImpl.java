package com.na.manager.service.impl;

import com.google.common.base.Preconditions;
import com.na.manager.dao.IMenuMapper;
import com.na.manager.dao.IPermissionMapper;
import com.na.manager.entity.Menu;
import com.na.manager.entity.Permission;
import com.na.manager.entity.User;
import com.na.manager.service.IPermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sunny on 2017/6/23 0023.
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private IPermissionMapper permissionMapper;
    @Autowired
    private IMenuMapper menuMapper;

    @Override
    public List<Menu> findAllMenu() {
        return menuMapper.findAll();
    }

    @Override
    public List<Permission> findAllPermission() {
        return permissionMapper.findAllPermission();
    }

    @Override
    public List<Permission> findPermissionByRoleId(String roleId) {
        Preconditions.checkNotNull(roleId);

        return permissionMapper.findPermissionByRoleId(roleId);
    }

	@Override
	public List<Permission> findPermissionByChildUserId(Long userId) {
		if(userId != null) {
			return permissionMapper.findPermissionByRolePermission(userId);
		}
		return null;
	}

	@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
	@Override
	public void addUserPermission(User user, List<String> permissionList) {
		for(String permissionId : permissionList) {
			permissionMapper.addUserPermission( user.getId(), permissionId);
		}
	}

	@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
	@Override
	public void updateUserPermission(User user, List<String> permissionList) {
		permissionMapper.deleteUserPermission(user.getId());
		
		for(String permissionId : permissionList) {
			permissionMapper.addUserPermission( user.getId(), permissionId);
		}
	}

	@Override
	public List<Permission> findPermissionByUserPermission(Long userId) {
		if(userId != null) {
			return permissionMapper.findPermissionByUserPermission(userId);
		}
		return null;
	}

	@Override
	public List<Permission> findPermissionByUserId(Long userId) {
		return permissionMapper.findPermissionByUserId(userId);
	}
}
