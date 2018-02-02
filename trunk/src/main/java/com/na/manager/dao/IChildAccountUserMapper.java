package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.Permission;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Mapper
public interface IChildAccountUserMapper {
    
    @Select("select * from child_account_user fca left join user u on fca.user_id = u.id  where fca.user_id=#{id}")
    ChildAccountUser findChildUserById(long id);
    
    @Select("select * from child_account_user c LEFT JOIN user u on u.id = c.user_id where c.parent_id = #{parentId}")
    List<ChildAccountUser> findChildUserByParentId(@Param("parentId") long parentId);
    
    void add(ChildAccountUser childAccountUser);
    
    void update(ChildAccountUser childAccountUser);
    
    @Select("select PERMISSION_NAME as permissionId, PERMISSION_DESC as permissionName from permission where PERMISSION_ID in "
    		+ "(select PERMISSION_ID from user_permission where USER_ID = #{userId} and STATUS = TRUE order by USER_PERMISSION_ORDER) and STATUS = TRUE ")
    List<Permission> getPermission(@Param("userId") long userId);
    
}
