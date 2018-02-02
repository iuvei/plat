package com.na.user.socketserver.service;

import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundPO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IRoundService {
	
    /**
     * 根据房间初始化路子
     * @return
     */
	public RoundPO initLastRound(GameTablePO table);

    /**
     * 增加新一局。
     * @param round
     */
	public void add(RoundPO round);
	
	/**
     * 更新新一局。
     * @param round
     */
	public void update(RoundPO round);
	
	/**
     * 获取局数据
     * @param round
     */
	public RoundPO getRound(Integer roundId);
	
}
