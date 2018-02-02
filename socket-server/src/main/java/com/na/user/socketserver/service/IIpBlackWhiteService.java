package com.na.user.socketserver.service;

import java.util.List;

import com.na.user.socketserver.entity.IpBlackWhiteAddr;

/**
 * @author andy
 * @date 2017年6月24日 下午5:52:30
 * 
 */
public interface IIpBlackWhiteService {
	
	List<IpBlackWhiteAddr> findAll();
}
