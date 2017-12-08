package com.gameportal.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.system.dao.BaseIbatisDAO;


@Component
public class GameTransferDao extends BaseIbatisDAO {

	public Class<GameTransfer> getEntityClass() {
		return GameTransfer.class;
	}

	public void saveOrUpdate(GameTransfer entity) {
		if (entity.getGtid() == null)
			save(entity);
		else
			update(entity);
	}
	
	public GameTransfer save(GameTransfer entity){
		return (GameTransfer)super.save(entity);
	}
	
	public boolean update(GameTransfer entity){ 
		return super.update(entity);
	}
	
	public List<GameTransfer> getList(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}
	
	public List<Map<String, Object>> getTransferResult(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".transferLogReport", params);
	}
	
	public Long getTransferCount(Map<String, Object> params){
		return (Long) super.getSqlMapClientTemplate().queryForObject(getSimpleName()+".transferLogReportCount", params);
	}
	
	public GameTransfer getGameTransfer(Map<String, Object> params){
		List<GameTransfer> gameTransfers = getList(params);
		if(CollectionUtils.isNotEmpty(gameTransfers)){
			return gameTransfers.get(0);
		}
		return null;
	}
}
