package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.na.baccarat.socketserver.entity.UserChips;

/**
 * 用户拓展Mapper
 * 
 * @author alan
 * @date 2017年4月27日 下午3:44:37
 */
@Mapper
public interface IUserChipsMapper {
	
//	@Select("select * from userchips where cid in (${cid})")
	public List<UserChips> findAllByCid(@Param("cid") String cid);

}
