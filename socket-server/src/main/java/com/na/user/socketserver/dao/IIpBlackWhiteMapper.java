package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.na.user.socketserver.entity.IpBlackWhiteAddr;

/**
 * @author andy
 * @date 2017年6月24日 下午5:36:38
 * 
 */
@Mapper
public interface IIpBlackWhiteMapper {
	
	@Select("select * from ip_black_white_addr")
	List<IpBlackWhiteAddr> findAll();	
}
