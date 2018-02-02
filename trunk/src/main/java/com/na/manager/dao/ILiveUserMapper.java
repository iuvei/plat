package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.bean.vo.AccountVO;
import com.na.manager.entity.LiveUser;

/**
 * @author andy
 * @date 2017年6月22日 下午1:28:41
 * 
 */
@Mapper
public interface ILiveUserMapper {
	
	List<AccountVO> search(LiveUser liveUser);
	
	List<AccountVO> findByParentId(long parentId);
	
	@Select("select * from live_user join user on id = user_id where parent_id=#{parentId}")
	List<LiveUser> findLiveUserByParentId(@Param("parentId") long parentId);
	
	List<AccountVO> findLiveUserByParentIdForPage(@Param("parentId") long parentId, @Param("startRow") long startRow, @Param("pageSize") long pageSize);
	
	@Select("select count(1) from live_user join user on id = user_id where parent_id=#{parentId}")
	long countByParentId(@Param("parentId") long parentId);
	
	List<AccountVO> findOnlineAllUserByParentId(String parentPath);

	@Select("select * from live_user left join user on id = user_id where user_id=#{userId}")
	LiveUser findLiveUserById(long userId);
	
	void add(LiveUser liveUser);
	
	void update(LiveUser liveUser);
	
	void modifyStatus(LiveUser liveUser);
	
	void modifyBetStatus(LiveUser liveUser);
	
	@Select("select * from live_user left join user on id = user_id where parent_path like CONCAT('%/',#{parentId},'/%') ORDER BY id ASC")
	List<LiveUser> findTeamMemberById(@Param("parentId") long parentId);
	
	@Select("select * from live_user left join user on id = user_id where login_name=#{userName}")
	LiveUser findLiveUserByUserName(String userName);
	
	@Select("select count(0) from loginstatus where Uid in ( " + 
			"select user_id from live_user where parent_id = ( " + 
			"select id from user where login_name = #{agentName}))")
	int countOnlineByParentName(@Param("agentName") String agentName);
	
}
