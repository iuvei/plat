package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.GameTransferLog;


public interface IGameTransferLogService {
	public abstract boolean saveGameTransferLog(GameTransferLog gameTransferLog) throws Exception;

	public abstract GameTransferLog findGameTransferLogId(Long uiid);

	public abstract boolean deleteGameTransferLog(Long uiid) ;
	
	public abstract List<GameTransferLog> queryGameTransferLog(Map map,Integer startNo, Integer pagaSize);
	
	public abstract Long queryGameTransferLogCount(Map map);
	

	
}
