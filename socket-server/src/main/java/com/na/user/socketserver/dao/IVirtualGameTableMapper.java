package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.VirtualGameTablePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IVirtualGameTableMapper {

	@Select("select * from virtual_gametable where id = #{id}")
	VirtualGameTablePO findById(@Param("id") int id);
	
    @Select("select * from virtual_gametable where game_id = #{gameId} and gametable_id = #{gameTableId} and status = 1")
    List<VirtualGameTablePO> findAllByGameId(@Param("gameId") int gameId, @Param("gameTableId") int gameTableId);
    
    @Select("select id from virtual_gametable where game_id = #{gameId} and gametable_id = #{gameTableId} and status = 1")
    List<Integer> findIdsByTableIdAndGameId(@Param("gameId") int gameId, @Param("gameTableId") int gameTableId);
    
    @Select("select v.id from virtual_gametable v join gametable g on g.id = v.gametable_id where v.game_id = #{gameId} and v.type = #{type} and v.status = 1")
    List<Integer> findIdsByTypeAndGameId(@Param("gameId") int gameId, @Param("type") int type);
    
}
