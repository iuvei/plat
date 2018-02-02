package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.entity.Chip;

/**
 * @author andy
 * @date 2017年6月22日 下午5:07:12
 * 
 */
@Mapper
public interface IChipMapper {
	
	@Select("SELECT * from chip")
	List<Chip> findAll();
	
	@Select("SELECT * from chip where id in (${chipIds})")
	List<Chip> findById(@Param("chipIds") String chipIds);
	
}
