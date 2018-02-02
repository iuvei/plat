package com.na.user.socketserver.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;


/**
 * 用户Mapper
 * 
 * @author alan
 * @date 2017年4月27日 下午3:43:56
 */
@Mapper
public interface IUserMapper {
    @Select("select * from user where login_name=#{loginName}")
	public UserPO getUserByLoginName(@Param("loginName") String loginName);
    
    @Select("select * from user where id=#{userId}")
    public UserPO getUserById(@Param("userId") Long userId);
    
    @Select("select * from live_user l join user u on u.id = l.user_id where u.login_name = #{loginName}")
    public LiveUserPO getLiveUserByLoginName(@Param("loginName") String loginName);
    
    @Select("select * from dealer_user d join user u on u.id = d.user_id where u.login_name = #{loginName}")
    public DealerUserPO getDealerUserByLoginName(@Param("loginName") String loginName);
    
    @Select("select * from live_user l join user u on u.id = l.user_id where l.user_id = #{userId}")
    public LiveUserPO getLiveUserById(@Param("userId") Long userId);
    
    @Select("select * from dealer_user d join user u on u.id = d.user_id where d.user_id = #{userId}")
    public DealerUserPO getDealerUserById(@Param("userId") Long userId);

    @Select("select * from user where barrcode=#{barcode}")
    public UserPO getUserByBarcode(@Param("barcode") String barcode);

    public List<UserChipsPO> findByUserChipsByIds(List<Integer> chipIds);

    @Update("update user set balance = balance+#{betAmount} where id=#{id}")
    public void updateUserBalance(@Param("id") Long id, @Param("betAmount") BigDecimal betAmount);
    
    public int batchUpdateBalance(List<UserPO> userPOList);
    
    public int batchUpdateWinMoney(List<UserPO> userPOList);
    
    @Select("select * from live_user, user where id = user_id and parent_id = (select id from user where login_name = #{loginName})")
    public List<LiveUserPO> getLowerLevelByName(@Param("loginName") String loginName);

	public List<Long> listUserIdByParentPath(String[] userIds);

	public List<Long> listUserIdBySuperiorID(String[] userIds);
	
	public int demoUpdate(UserPO user);
	
	public int modifyUserInfo(UserPO user);
	
	public int updateStatus(UserPO user);

}
