package com.na.manager.dao;

import com.na.manager.entity.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * Created by sunny on 2017/6/22 0022.
 */
@Mapper
public interface IPermissionMapper {
    List<Permission> findPermissionByUserId(Long userId);
    @Select("select * from permission")
    List<Permission> findAllPermission();

    @Select("SELECT p.* FROM permission p JOIN role_permission rp ON p.`PERMISSION_ID`=rp.`PERMISSION_ID` WHERE rp.`role_ID`=#{roleId}")
    List<Permission> findPermissionByRoleId(String roleId);
    
    @Select("select * from permission where PERMISSION_ID in "
    		+ "(select PERMISSION_ID from user_permission where USER_ID = #{userId} and STATUS = TRUE order by USER_PERMISSION_ORDER) and STATUS = TRUE ")
    List<Permission> findPermissionByUserPermission(@Param("userId") long userId);

    List<Permission> findPermissionByRolePermission(@Param("userId") long userId);

    void addUserPermission(@Param("userId") Long userId, @Param("permissionId") String permissionId);
    
    @Delete("delete from user_permission where user_id = #{userId} ")
    void deleteUserPermission(@Param("userId") Long userId);

    @Delete("DELETE FROM role_permission  WHERE role_ID=#{roleId}")
    void deletePermissionByRoleId(String roleId);

    void addPermissionByRoleId(@Param("roleId") String roleId, @Param("permissionIds") Set<String> permissionIds);
}
