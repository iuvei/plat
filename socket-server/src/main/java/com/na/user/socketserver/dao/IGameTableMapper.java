package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.GameTablePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IGameTableMapper {

    @Select("select * from gametable where game_id = #{gameId} and status = #{status} order by id")
    List<GameTablePO> findByGid(@Param("gameId") int gameId, @Param("status") int status);
    
    @Select("select * from gametable where id = #{id}")
    GameTablePO findGametableByTid(int id);
    
    
}
