package com.na.user.socketserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.dao.IRoundExtMapper;
import com.na.user.socketserver.entity.RoundExtPO;
import com.na.user.socketserver.service.IRoundExtService;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class RoundExtServiceImpl implements IRoundExtService {
	
    @Autowired
    private IRoundExtMapper roundMapper;

	@Override
	public RoundExtPO getRoundExt(long roundId) {
		return roundMapper.findById(roundId);
	}

	@Override
	public void add(RoundExtPO ext) {
		roundMapper.add(ext);
	}

	@Override
	public void update(RoundExtPO ext) {
		roundMapper.update(ext);
	}
}
