package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.bean.RoleSearchRequest;
import com.na.manager.entity.Role;
import com.na.manager.entity.UserRole;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
@Mapper
public interface IRoleMapper {
    @Select("SELECT r.* FROM role r JOIN user_role u ON r.`ROLE_ID`=u.`ROLE_ID` WHERE r.status=1 and u.`USER_ID`=#{userId}")
    List<Role> findRoleByUserId(Long userId);

    @Select("select * from role where status=1")
    List<Role> findAll();
    
    @Select("select * from role where status=1 and role_name = #{roleName}")
    Role findRoleByName(@Param("roleName") String roleName);

    List<Role> search(RoleSearchRequest condition );

    long count(RoleSearchRequest condition);

    void update(Role role);

    @Insert("insert into role(role_ID,role_Name,role_Desc,status) value(#{roleID},#{roleName},#{roleDesc},#{status})")
    void add(Role role);
    
    @Insert("insert into user_role (ROLE_ID,USER_ID) value (#{roleID},#{userID})")
    void addUserRole(UserRole userRole);
}
