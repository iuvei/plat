package com.na.manager.dao;

import com.na.manager.bean.AnnounceSearchRequest;
import com.na.manager.bean.vo.AnnounceListVO;
import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.UserAnnounce;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author andy
 * @date 2017年6月26日 下午6:10:41
 * 
 */
@Mapper
public interface IAnnounceManageMapper {
	
	List<AnnounceListVO> queryUserAnnouneByPage(AnnounceSearchRequest searchRequest);
	
	long count(AnnounceSearchRequest searchRequest);
	
	void insertAnnounceConent(AnnounceContent announceContent);
	
	void insertUserAnnounce(List<UserAnnounce> userAnnounces);
	
	@Delete("delete from announce_content where id=#{id}")
	void deleteAnnounceContent(Long id);
	
	@Delete("delete from user_announce where content_id=#{id}")
	void deleteUserAnnounce(Long id);
	
	void updateAnnounceContent(AnnounceContent announceContent);
	
	void updateUserAnnounce(UserAnnounce UserAnnounce);
	
	@Select("select * from announce_content where id=#{id}")
	AnnounceContent findAnnounceContent(Long id);

	@Select("select * from user_announce where id=#{id}")
    UserAnnounce findUserAnnouneById(Long id);
}
