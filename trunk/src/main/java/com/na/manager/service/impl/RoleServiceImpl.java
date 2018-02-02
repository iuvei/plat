package com.na.manager.service.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.na.manager.bean.Page;
import com.na.manager.bean.RoleSearchRequest;
import com.na.manager.bean.RoleUpdateRequest;
import com.na.manager.dao.IPermissionMapper;
import com.na.manager.dao.IRoleMapper;
import com.na.manager.entity.Role;
import com.na.manager.entity.UserRole;
import com.na.manager.enums.RoleStatus;
import com.na.manager.service.IRoleService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
@Service
@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleMapper roleMapper;
    @Autowired
    private IPermissionMapper permissionMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<Role> search(RoleSearchRequest condition ) {
        Page<Role> page = new Page(condition);
        page.setTotal(roleMapper.count(condition));
        page.setData(roleMapper.search(condition));
        return page;
    }

    @Override
    public void changeStatus(Role role) {
        Preconditions.checkNotNull(role.getRoleID());
        roleMapper.update(role);
    }

    @Override
    public void update(RoleUpdateRequest request) {
        Preconditions.checkNotNull(request.getRoleID());
        Preconditions.checkArgument(StringUtils.isNoneBlank(request.getRoleName()),"role.rolename.not.null");
        Preconditions.checkArgument(StringUtils.isNoneBlank(request.getRoleName()),"role.roledesc.not.null");

        Role role = new Role();
        BeanUtils.copyProperties(request,role);
        roleMapper.update(role);

        permissionMapper.deletePermissionByRoleId(role.getRoleID());

        addRolePermission(request);
    }

    @Override
    public void add(RoleUpdateRequest request){
        Preconditions.checkArgument(StringUtils.isNoneBlank(request.getRoleName()),"role.rolename.not.null");
        Preconditions.checkArgument(StringUtils.isNoneBlank(request.getRoleName()),"role.roledesc.not.null");

        Role role = new Role();
        BeanUtils.copyProperties(request,role);
        role.setStatus(RoleStatus.ENABLED.get());
        role.setRoleID(UUID.randomUUID().toString().replace("-",""));
        roleMapper.add(role);

        request.setRoleID(role.getRoleID());

        addRolePermission(request);
    }

    @Transactional(readOnly = true)
    public List<Role> getRoleByUserId(Long userId){
        return roleMapper.findRoleByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRole(){
        return roleMapper.findAll();
    }

    private void addRolePermission(RoleUpdateRequest request) {
        if(request.getPermissionList()==null || request.getPermissionList().size()==0){
            return;
        }

        Set<String> permissions = new TreeSet<>();
        permissions.addAll(request.getPermissionList());
        permissionMapper.addPermissionByRoleId(request.getRoleID(),permissions);
    }

	@Override
	public void addUserRole(String roleID, Long userID) {
		UserRole userRole = new UserRole();
		userRole.setRoleID(roleID);
		userRole.setUserID(userID);
		roleMapper.addUserRole(userRole);
	}

    @Transactional(readOnly = true)
	@Override
	public Role findRoleByName(String roleName) {
		return roleMapper.findRoleByName(roleName);
	}

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
