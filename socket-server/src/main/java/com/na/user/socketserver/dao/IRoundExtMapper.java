package com.na.user.socketserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.user.socketserver.entity.RoundExtPO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IRoundExtMapper {

    @Select("select * from roundExt where round_id = #{roundId}")
    RoundExtPO findById(@Param("roundId") long roundId);

    @Insert("insert into roundext(round_id) values(#{roundId})")
    void add(RoundExtPO roundExt);
    
    @Update("UPDATE roundext SET bankCard1_mode = #{bankCard1Mode}, bankCard1_number = #{bankCard1Number}, bankCard2_mode = #{bankCard2Mode},"
    		+ "bankCard2_number = #{bankCard2Number}, bankCard3_mode = #{bankCard3Mode}, bankCard3_number = #{bankCard3Number},"
    		+ "playerCard1_mode = #{playerCard1Mode}, playerCard1_number = #{playerCard1Number}, playerCard2_mode = #{playerCard2Mode},"
    		+ "playerCard2_number = #{playerCard2Number}, playerCard3_mode = #{playerCard3Mode}, playerCard3_number = #{playerCard3Number} "
    		+ " WHERE round_id = #{roundId}")
    public void update(RoundExtPO roundExt);
    
}
