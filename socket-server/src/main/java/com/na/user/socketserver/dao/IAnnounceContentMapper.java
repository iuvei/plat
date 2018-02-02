package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.user.socketserver.entity.AnnounceContent;

/**
 * Created by sunny on 2017/5/3 0003.
 */
@Mapper
public interface IAnnounceContentMapper {
    @Insert("insert into `account_record` (id,`user_id`,`sn`,`time`,`amount`,`pre_balance`,`type`,`business_key`,`remark`) " +
            "values(#{id},#{userId},#{sn},#{time},#{amount},#{preBalance},#{type},#{businessKey},#{remark});")
    void add(AccountRecord acountRecord);
    
    public void batchAdd(List<AccountRecord> acountRecord);

    @Select("SELECT "+
	"announce_content.id AS contentId, "+
	"announce_content.content_desc AS contentDesc, "+
	"announce_content.content_title AS contentTitle "+
	"FROM "+
	"announce_content "+
	"WHERE "+
	"announce_content.id = #{contentId}")
	AnnounceContent getAnnounceContentByContentId(@Param("contentId")Long contentId);
}
