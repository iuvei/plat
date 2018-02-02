package com.na.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.manager.dao.IChildAccountUserMapper;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.UserType;
import com.na.manager.service.IChildAccountUserService;
import com.na.manager.service.IPermissionService;
import com.na.manager.service.IUserService;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Service
public class ChildAccountUserServiceImpl implements IChildAccountUserService {

	@Autowired
	private IUserMapper userMapper;
	
	@Autowired
	private ILiveUserMapper liveUserMapper;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IChildAccountUserMapper childAccountUserMapper;
	
	@Autowired
	private IPermissionService permissionService;
	
	@Override
	public List<ChildAccountUser> findChildUser(Long parentId) {
		if(parentId == null) {
			return null;
		}
		List<ChildAccountUser> childUser = childAccountUserMapper.findChildUserByParentId(parentId);
		return childUser;
	}

	@Override
	public void add(ChildAccountUser childAccountUser, List<String> permissionList) {
		childAccountUser.setUserType(UserType.SUB_ACCOUNT.get());
		userService.add(childAccountUser);
		childAccountUser.setUserId(childAccountUser.getId());
		childAccountUserMapper.add(childAccountUser);
		permissionService.addUserPermission(childAccountUser, permissionList);
	}

	@Override
	public void update(ChildAccountUser childAccountUser, List<String> permissionList) {
		childAccountUser.setUserType(UserType.SUB_ACCOUNT);
		userMapper.update(childAccountUser);
		permissionService.updateUserPermission(childAccountUser, permissionList);
		
	}

	@Override
	public ChildAccountUser findChildAccountUserById(Long childAccountUserId){
		ChildAccountUser childAccountUser = childAccountUserMapper.findChildUserById(childAccountUserId);
		if(childAccountUser!=null) {
			LiveUser parentUser = liveUserMapper.findLiveUserById(childAccountUser.getParentId());
			childAccountUser.setParentUser(parentUser);
		}
		return childAccountUser;
	}

}
