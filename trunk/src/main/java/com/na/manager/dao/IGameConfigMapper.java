package com.na.manager.dao;

import com.na.manager.entity.GameConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Mapper
public interface IGameConfigMapper {

    @Select("select * from gameconfig limit 0,50")
    List<GameConfig> listAllGameConfig();

    @Select("select * from gameconfig where id = #{id} limit 1")
    GameConfig findGameConfigById(@Param("id") Integer id);

    void updateGameConfig(GameConfig config);
}
