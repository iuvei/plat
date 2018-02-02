package com.na.manager.dao;

import com.na.manager.entity.Round;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface IRoundMapper {

	@Select("SELECT result FROM Round WHERE game_id= #{gid} AND boot_id= #{bid} AND gametable_id= #{tid} order by startTime asc")
	List<String> listRoundResults(@Param("bid")String bid, @Param("gid")Integer gid, @Param("tid")Integer tid);

	@Select("select boot_id,boot_starttime from round WHERE game_id= #{gid} AND gametable_id= #{tid} and boot_starttime > #{startdt} and boot_starttime < #{enddt} order by startTime desc limit #{pagenum},#{pagesize}")
	List<Round> listRouletteGameResult(@Param("pagenum")Integer pageNumber, @Param("pagesize")Integer pageSize,
			@Param("gid")Integer gid, @Param("tid")Integer tid, @Param("startdt")Date startDt, @Param("enddt")Date endDt);
	
	@Select("select count(1) from round WHERE game_id= #{gid} AND gametable_id= #{tid} and boot_starttime > #{startdt} and boot_starttime < #{enddt} ")
	Integer countRouletteGameResult(@Param("gid")Integer gid, @Param("tid")Integer tid, @Param("startdt")Date startDt, @Param("enddt")Date endDt);
	
	@Select("select boot_id,boot_starttime from round WHERE game_id= #{gid} AND gametable_id= #{tid} and boot_starttime > #{startdt} and boot_starttime < #{enddt} group by boot_id order by startTime desc limit #{pagenum},#{pagesize}")
	List<Round> listBaccaratGameResult(@Param("pagenum")Integer pageNumber, @Param("pagesize")Integer pageSize,
			@Param("gid")Integer gid, @Param("tid")Integer tid, @Param("startdt")Date startDt, @Param("enddt")Date endDt);

	@Select("select count(*) from (select boot_id,boot_starttime from round WHERE game_id= #{gid} AND gametable_id= #{tid} and boot_starttime > #{startdt} and boot_starttime < #{enddt} GROUP BY boot_id) as bt")
	Integer countBaccaratGameResult(@Param("gid")Integer gid, @Param("tid")Integer tid, @Param("startdt")Date startDt, @Param("enddt")Date endDt);

	List<Map> listAbnormalTableRound(Integer tid);

	@Select("select * from round where id = #{rid} limit 1")
	Round getRoundById(@Param("rid")Long rid);

	int updateRoundById(Round round);

}
