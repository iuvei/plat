package com.na.gate.dao;

import com.na.gate.entity.PlatformSyncRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2017/8/22 0022.
 */
@Mapper
public interface IPlatformSyncRecordMapper {
    @Insert("insert into platform_sync_record(start_time,end_time) values(#{startTime},#{endTime})")
    void add(PlatformSyncRecord syncRecord);

    @Select("SELECT * FROM platform_sync_record ORDER BY id DESC LIMIT 1")
    PlatformSyncRecord findLastSyncRecord();
}
