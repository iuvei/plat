package com.na.user.socketserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.ILoginStatusMapper;
import com.na.user.socketserver.service.ILoginStatusService;

/**
 * Created by sunny on 2017/5/8 0008.
 */
@Service
public class LoginStatusServiceImpl implements ILoginStatusService {
	
    @Autowired
    private ILoginStatusMapper loginStatusMapper;

	@Override
	public void deleteAll() {
		loginStatusMapper.deleteAll();
	}
    
}
