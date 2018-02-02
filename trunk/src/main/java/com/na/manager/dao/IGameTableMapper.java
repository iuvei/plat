package com.na.manager.dao;

import com.na.manager.bean.vo.GameTableAndGameNameVo;
import com.na.manager.entity.GameTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author andy
 * @date 2017年6月22日 下午1:17:43
 * 
 */
@Mapper
public interface IGameTableMapper {
	
	@Select("select * from gametable")
	List<GameTable> findAllGameTable();

	List<GameTable> listGameTables(@Param("gameid")Integer gameid, @Param("name")String name, @Param("status")Integer status,
			@Param("type")Integer type, @Param("page")Integer page, @Param("pagesize")Integer pagesize);

	Integer countGameTables(@Param("gameid")Integer gameid, @Param("name")String name, @Param("status")Integer status,
			@Param("type")Integer type);

	Integer saveGameTable(@Param("gameid")Integer gid,@Param("name")String name,@Param("status")Integer status,
			@Param("type")Integer type, @Param("countDownSeconds")Integer countDownSeconds, @Param("isMany")Integer isMany,
			@Param("isBegingMi")Integer isBegingMi,@Param("min")Integer min, @Param("max")Integer max);

	@Update("update gametable set status = #{status} where id = #{tid}")
	int updateTableStatus(@Param("tid")Integer tid, @Param("status")Integer status);

	int updateTable(GameTable gameTable);

	List<GameTableAndGameNameVo> listAbnormalTables(@Param("gameId")Integer gameId, @Param("name")String name);
} 
