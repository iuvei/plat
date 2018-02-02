package com.na.user.socketserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.DealerClassRecordPO;


@Mapper
public interface IDealerClassRecordMapper {
	
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
	@Insert("insert into `dealer_class_record` (`user_id`,`login_name`,`start_time`)\n" +
            "values(#{userId},#{loginName},#{startTime});")
    public void add(DealerClassRecordPO dealerClassRecordPO);
	
	public int update(DealerClassRecordPO dealerClassRecordPO);
	
	@Select("SELECT * FROM `dealer_class_record` WHERE id = #{id}")
	public DealerClassRecordPO findById(@Param("id") Integer id);
	
}
