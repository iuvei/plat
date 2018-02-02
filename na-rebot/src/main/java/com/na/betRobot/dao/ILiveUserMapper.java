package com.na.betRobot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.betRobot.entity.LiveUser;

/**
 * @author andy
 * @date 2017年6月22日 下午1:28:41
 * 
 */
@Mapper
public interface ILiveUserMapper {

	@Select("select * from live_user left join user on id = user_id where user_id=#{userId}")
	LiveUser findById(long userId);
	
	@Select("select * from live_user left join user on id = user_id where login_name=#{loginName}")
	LiveUser findByLoginName(@Param("loginName") String loginName);
	
	@Select("select * from live_user left join user on id = user_id where parent_id = #{parentId} and balance != 0 order by user.balance desc limit 0, ${number}")
	List<LiveUser> findByParentId(@Param("parentId") long parentId,@Param("number") int number);
	
}
