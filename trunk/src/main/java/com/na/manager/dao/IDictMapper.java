package com.na.manager.dao;

import com.na.manager.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by sunny on 2017/6/24 0024.
 */
@Mapper
public interface IDictMapper {
    @Select("SELECT * FROM dict ORDER BY TYPE,`order`")
    List<Dict> findAllDict();
}
