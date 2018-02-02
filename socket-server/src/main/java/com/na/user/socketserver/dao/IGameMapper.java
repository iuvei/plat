package com.na.user.socketserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.GamePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IGameMapper {
    @Select("select * from game where status = 0")
    List<GamePO> findAll();

    @Select("select `key`,`value` from gameconfig where game_id=#{gameId}")
    @ResultType(Map.class)
    List<Map<String,String>> findGameConfigByGameId(@Param("gameId") Integer gameId);
    
    @Select("select `key`,`value`,`game_id` from gameconfig where id=#{id}")
    @ResultType(Map.class)
    Map<String,String> findGameConfigById(@Param("id") Integer id);
}
