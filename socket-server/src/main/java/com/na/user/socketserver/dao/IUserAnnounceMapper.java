package com.na.user.socketserver.dao;

import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.user.socketserver.entity.UserAnnounce;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by sunny on 2017/5/3 0003.
 */
@Mapper
public interface IUserAnnounceMapper {
    @Insert("insert into `account_record` (id,`user_id`,`sn`,`time`,`amount`,`pre_balance`,`type`,`business_key`,`remark`) " +
            "values(#{id},#{userId},#{sn},#{time},#{amount},#{preBalance},#{type},#{businessKey},#{remark});")
    void add(AccountRecord acountRecord);
    
    public void batchAdd(List<AccountRecord> acountRecord);

	@Select("SELECT user_announce.id ,user_announce.user_id AS userId,"+
			"user_announce.user_name AS userName,user_announce.content_id AS contentId,user_announce.type AS type "+
			"FROM user_announce WHERE user_announce.id = #{announceId}")
	UserAnnounce getUserAnnouceById(@Param("announceId")Long announceId);
	
	@Select("SELECT user_announce.id ,user_announce.user_id AS userId,"+
			"user_announce.user_name AS userName,user_announce.content_id AS contentId,user_announce.type AS type "+
			"FROM user_announce ORDER BY user_announce.CREATE_DATETIME DESC LIMIT 1")
	UserAnnounce getNewAnnounce();

    List<UserAnnounce> getAllUserAnnouce();
}
