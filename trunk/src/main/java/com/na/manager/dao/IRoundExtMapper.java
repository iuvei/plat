package com.na.manager.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.entity.RoundExt;

@Mapper
public interface IRoundExtMapper {

	@Select("select * from roundext where round_id = #{rid} limit 1")
	RoundExt getRoundExtById(@Param("rid")Long rid);
	
	int updateRoundExtById(RoundExt roundExt);

}
