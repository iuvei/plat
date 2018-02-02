package com.na.betRobot.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.betRobot.entity.LiveUser;
import com.na.betRobot.entity.User;
import com.na.betRobot.entity.UserChips;

/**
 * 用户Mapper
 * 
 * @author alan
 * @date 2017年4月27日 下午3:43:56
 */
@Mapper
public interface IUserMapper {
    @Select("select * from user where login_name=#{loginName}")
	public User getUserByLoginName(@Param("loginName") String loginName);
    
    @Select("select * from user where id=#{userId}")
    public User getUserById(@Param("userId") Long userId);
    
    @Select("select * from live_user l join user u on u.id = l.user_id where u.login_name = #{loginName}")
    public LiveUser getLiveUserByLoginName(@Param("loginName") String loginName);
    
    
    @Select("select * from live_user l join user u on u.id = l.user_id where l.user_id = #{userId}")
    public LiveUser getLiveUserById(@Param("userId") Long userId);
    

    @Select("select * from user where barcode=#{barcode}")
    public User getUserByBarcode(@Param("barcode") String barcode);

    public List<UserChips> findByUserChipsByIds(List<Integer> chipIds);

    @Update("update user set balance = balance+#{betAmount} where id=#{id}")
    public void updateUserBalance(@Param("id") Long id, @Param("betAmount") BigDecimal betAmount);
    
    public int batchUpdateBalance(List<User> userList);
    
    public int batchUpdateWinMoney(List<User> userList);
    
    @Select("select * from live_user, user where id = user_id and parent_id = (select id from user where login_name = #{loginName})")
    public List<LiveUser> getLowerLevelByName(@Param("loginName") String loginName);

	public List<Long> listUserIdByParentPath(String[] userIds);

	public List<Long> listUserIdBySuperiorID(String[] userIds);
	
	public int demoUpdate(User user);
	
	public int modifyUserInfo(User user);

}
