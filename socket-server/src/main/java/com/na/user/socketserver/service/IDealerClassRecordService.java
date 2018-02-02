package com.na.user.socketserver.service;

import com.na.user.socketserver.entity.DealerClassRecordPO;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.entity.GameTablePO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IDealerClassRecordService {
	
	public DealerClassRecordPO findById(Integer id);
	
	public void add(DealerClassRecordPO dealerClassRecordPO);
	
	public void add(DealerUserPO userPO);
	
	public void update(DealerClassRecordPO dealerClassRecordPO);
	
	public void dealerJoinTable(Integer id, GamePO gamePO, GameTablePO gameTablePO);
	
	public DealerClassRecordPO dealerLogout(Integer id);
}
