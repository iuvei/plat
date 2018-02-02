package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.I18NPO;


@Mapper
public interface II18NMapper {
    
	@Select("select * from i18n")
    public List<I18NPO> getAll();
}
