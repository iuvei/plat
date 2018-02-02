package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.baccarat.socketserver.entity.Play;

/**
 * 玩法
 * 
 * @author alan
 * @date 2017年5月5日 上午9:25:54
 */
@Mapper
public interface IPlayMapper {
	
	@Select("select * from play ")
    List<Play> getAll();
	
	@Select("select * from play where gameId = #{gameId}")
    List<Play> getPlayByGame(@Param("gameId") Integer gameId);
	
}
