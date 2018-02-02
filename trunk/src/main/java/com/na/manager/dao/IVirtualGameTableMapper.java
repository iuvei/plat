package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.na.manager.bean.VirtualGameTableSearchRequest;
import com.na.manager.entity.VirtualGameTable;

/**
 * Created by Administrator on 2017/7/25.
 */
@Mapper
public interface IVirtualGameTableMapper {
	
	void add(VirtualGameTable virtualGameTable);
	
	List<VirtualGameTable> queryVirtualGameTableByPage(VirtualGameTableSearchRequest request);
	
	long getVirtualGameTableCount(VirtualGameTableSearchRequest request);
	
	void update(VirtualGameTable virtualGameTable);
	
	@Select("select * from virtual_gametable where id=#{id}")
	VirtualGameTable findById(Integer id);
	
	@Select("select count(1) from virtual_gametable where owner_id=#{owerId} and status =1")
	long queryCountByOwnerId(long owerId);
}
