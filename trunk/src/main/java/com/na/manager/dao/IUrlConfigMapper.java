package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.manager.entity.UrlConfig;

/**
* @author Andy
* @version 创建时间：2017年9月14日 上午11:48:49
*/
@Mapper
public interface IUrlConfigMapper {
	
	@Select("select * from url_config")
	List<UrlConfig> list();
	
	@Update("update url_config set url=#{url} where id=#{id}")
	void update(@Param("id") Long id,@Param("url") String url);
}
