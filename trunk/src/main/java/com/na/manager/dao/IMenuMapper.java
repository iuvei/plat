package com.na.manager.dao;

import com.na.manager.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
@Mapper
public interface IMenuMapper {
    @Select("select * from menu")
    List<Menu> findAll();
}
