package com.na.manager.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.na.manager.entity.Game;

@Mapper
public interface IGameMapper {

	@Select("select * from game")
	List<Game> listGameTypes();

}
