package com.na.user.socketserver.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.user.socketserver.entity.RoundPO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IRoundMapper {

	@Select("select * from round where id = #{id}")
	RoundPO findByRid(@Param("id") int roundId);
	
	
    @Select("select * from round where game_id = #{gameId} and gametable_id = #{gameTableId} order by startTime desc limit 1")
    RoundPO findByGidAndTid(@Param("gameId") long gameId,@Param("gameTableId") int gameTableId);

    @Insert("insert into round (game_id,gametable_id,boot_id,round_number,boot_number,boot_starttime,startTime,endTime,result,status )"
            +"values(#{gameId},#{gameTableId},#{bootId},#{roundNumber},#{bootNumber},#{bootStartTime},#{startTime},#{endTime},#{result},#{status})"
    )
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    public void add(RoundPO round);
    
    @Update("UPDATE round SET game_id=#{gameId}, gametable_id=#{gameTableId}, boot_id=#{bootId}, "
    		+ "round_number=#{roundNumber}, boot_number=#{bootNumber}, boot_starttime=#{bootStartTime}, startTime=#{startTime},"
    		+ "endTime=#{endTime}, result=#{result}, status=#{status}"
    		+ " WHERE id = #{id}")
    public void update(RoundPO round);
}
