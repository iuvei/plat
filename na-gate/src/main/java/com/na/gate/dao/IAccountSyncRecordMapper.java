package com.na.gate.dao;

import org.apache.ibatis.annotations.*;

import com.na.gate.entity.AccountSyncRecord;

import java.util.Date;
import java.util.List;

/**
 * 平台玩家账单同步记录
 * Created by sunny on 2017/8/9 0009.
 */
@Mapper
public interface IAccountSyncRecordMapper {
	@Insert("INSERT INTO `account_sync_record` (`user_id`,`id`) values(#{userId},#{id})")
    void add(@Param("userId") Long userId,@Param("id") Long id);
	
	void batchAdd(List<AccountSyncRecord> list);

    @Select("select sync_time from account_sync_record where user_id=#{userId} order by sync_time desc limit 1")
    Date findLastSyncTime(Long userId);
    
    @Select("select * from account_sync_record where user_id=#{userId} and sync_time>=#{loginTime}")
    List<AccountSyncRecord> queryList(@Param("userId") Long userId,@Param("loginTime") String loginTime);
}
