package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.IpBlackWhiteSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.entity.IpBlackWhiteAddr;

/**
 * @author andy
 * @date 2017年6月24日 下午5:52:30
 * 
 */
public interface IIpBlackWhiteService {
	
	List<IpBlackWhiteAddr> findAll();
	
	Page<IpBlackWhiteAddr> queryListByPage(IpBlackWhiteSearchRequest searchRequest);

	void insert(IpBlackWhiteAddr ipBlackWhiteAddr);

	void delete(IpBlackWhiteAddr ipBlackWhiteAddr);
}
