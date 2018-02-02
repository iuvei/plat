package com.na.user.socketserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.IIpBlackWhiteMapper;
import com.na.user.socketserver.entity.IpBlackWhiteAddr;
import com.na.user.socketserver.service.IIpBlackWhiteService;

/**
 * @author andy
 * @date 2017年6月24日 下午5:56:09
 * 
 */
@Service
public class IpBlackWhiteServiceImpl implements IIpBlackWhiteService {
	
	@Autowired
	private IIpBlackWhiteMapper ipBlackWhiteMapper;

	@Override
	public List<IpBlackWhiteAddr> findAll() {
		return ipBlackWhiteMapper.findAll();
	}
}
