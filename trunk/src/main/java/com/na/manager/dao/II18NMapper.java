package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.na.manager.entity.I18N;


@Mapper
public interface II18NMapper {
    
	@Select("select * from i18n")
    public List<I18N> getAll();

	public int saveII18N(I18N i18n);
}
