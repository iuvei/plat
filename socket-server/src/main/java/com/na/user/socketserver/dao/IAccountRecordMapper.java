package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.na.baccarat.socketserver.entity.AccountRecord;

/**
 * Created by sunny on 2017/5/3 0003.
 */
@Mapper
public interface IAccountRecordMapper {
    @Insert("insert into `account_record` (id,`user_id`,`sn`,`time`,`amount`,`pre_balance`,`type`,`business_key`,`remark`)\n" +
            "values(#{id},#{userId},#{sn},#{time},#{amount},#{preBalance},#{type},#{businessKey},#{remark});")
    void add(AccountRecord acountRecord);
    
    public void batchAdd(List<AccountRecord> acountRecord);
}
