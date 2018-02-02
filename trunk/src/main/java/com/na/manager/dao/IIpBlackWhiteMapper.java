package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.na.manager.bean.IpBlackWhiteSearchRequest;
import com.na.manager.entity.IpBlackWhiteAddr;

/**
 * @author andy
 * @date 2017年6月24日 下午5:36:38
 * 
 */
@Mapper
public interface IIpBlackWhiteMapper {
	
	@Select("select * from ip_black_white_addr where plat_type in (2,3)")
	List<IpBlackWhiteAddr> findAll();
	
	List<IpBlackWhiteAddr> queryListByPage(IpBlackWhiteSearchRequest searchRequest);
	
	long count(IpBlackWhiteSearchRequest searchRequest);
	
	void insert(List<IpBlackWhiteAddr> ipBlackWhiteAddr);
	
	void delete(List<Long> id);
	
	List<IpBlackWhiteAddr> list(IpBlackWhiteAddr condition);
}
