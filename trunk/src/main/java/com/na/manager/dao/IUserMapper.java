package com.na.manager.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.manager.bean.UserSearchRequest;
import com.na.manager.entity.User;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Mapper
public interface IUserMapper {

    @Select("select * from user where login_name=#{loginName}")
    User getUser(String loginName);
    
    @Select("select * from user where id=#{id}")
    User findUserById(long id);

    @Select("select * from user where barrcode=#{barrcode}")
    User findUserByBarrcode(String barrcode);
    
    void addUser(User user);

    /**
     * 根据用户ID和类型修改资料。
     * @param user
     */
    void update(User user);
    
    @Update("UPDATE `user` set `head_pic`=#{headPic},`nick_name`=#{nickName} WHERE `id` = #{userId}")
    void modify(@Param("userId") Long userId,@Param("headPic") String headPic,@Param("nickName") String nickName);
    
    /**
     * 修改玩家余额
     * @param userId
     * @param balance
     */
    @Update("UPDATE `user` set `balance`=`balance`+#{balance} WHERE `id` = #{userId} and (`balance`+#{balance}) >=0")
    void updateBalance(@Param("userId") Long userId,@Param("balance") BigDecimal balance);
    
    /**
     * 以清算的用户，以大厅余额为准
     * @param userId
     * @param balance
     */
    @Update("UPDATE `user` set `balance`=#{balance} WHERE `id` = #{userId}")
    void modifyBalance(@Param("userId") Long userId,@Param("balance") BigDecimal balance);

    @Delete("DELETE FROM user_role WHERE USER_ID = #{userId}")
    void deleteUserAllRole(Long userId);

    void addUserRole(@Param("userId") Long userId, @Param("roleIds") Set<String> roleIds);

    List<User> search(UserSearchRequest request);

    long count(UserSearchRequest request);
    
}
