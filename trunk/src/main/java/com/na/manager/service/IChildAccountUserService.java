package com.na.manager.service;

import java.util.List;

import com.na.manager.entity.ChildAccountUser;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public interface IChildAccountUserService {
    
    /**
     * 根据上级ID获取子账号
     * @param parentId
     * @return
     */
	List<ChildAccountUser> findChildUser(Long parentId);
    
    void add(ChildAccountUser childAccountUser, List<String> permissionList);
    
    void update(ChildAccountUser childAccountUser, List<String> permissionList);

    ChildAccountUser findChildAccountUserById(Long childAccountUserId);
    
}
