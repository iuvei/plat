package com.na.user.socketserver.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerUser;
import com.na.user.socketserver.dao.IDealerClassRecordMapper;
import com.na.user.socketserver.entity.DealerClassRecordPO;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.service.IDealerClassRecordService;


/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class DealerClassRecordServiceImpl implements IDealerClassRecordService {
	
	private Logger log = LoggerFactory.getLogger(DealerClassRecordServiceImpl.class);
	
    @Autowired
    private IDealerClassRecordMapper dealerClassRecordMapper;
    
	@Override
	public void add(DealerClassRecordPO dealerClassRecordPO) {
		dealerClassRecordMapper.add(dealerClassRecordPO);
	}
	
	

	@Override
	public void update(DealerClassRecordPO dealerClassRecordPO) {
		dealerClassRecordMapper.update(dealerClassRecordPO);
	}

	@Override
	public void add(DealerUserPO userPO) {
		DealerClassRecordPO dealerClassRecordPO = new DealerClassRecordPO();
		dealerClassRecordPO.setUserId(userPO.getId());
		dealerClassRecordPO.setLoginName(userPO.getLoginName());
		dealerClassRecordPO.setStartTime(new Date());
		dealerClassRecordMapper.add(dealerClassRecordPO);
		userPO.setDealerClassRecordId(dealerClassRecordPO.getId());
	}

	@Override
	public DealerClassRecordPO findById(Integer id) {
		return dealerClassRecordMapper.findById(id);
	}
	
	@Override
	public void dealerJoinTable(Integer id, GamePO gamePO, GameTablePO gameTablePO) {
		DealerClassRecordPO dealerClassRecordPO = dealerClassRecordMapper.findById(id);
		if(dealerClassRecordPO == null) {
			log.warn("未找到荷官交接记录");
			return ;
		}
		dealerClassRecordPO.setGameId(gamePO.getId());
		dealerClassRecordPO.setGameName(gamePO.getName());
		dealerClassRecordPO.setGameTableId(gameTablePO.getId());
		dealerClassRecordPO.setGameTableName(gameTablePO.getName());
		dealerClassRecordMapper.update(dealerClassRecordPO);
	}

	@Override
	public DealerClassRecordPO dealerLogout(Integer id) {
		DealerClassRecordPO dealerClassRecordPO = dealerClassRecordMapper.findById(id);
		dealerClassRecordPO.setEndTime(new Date());
		dealerClassRecordMapper.update(dealerClassRecordPO);
		return dealerClassRecordPO;
	}

}
