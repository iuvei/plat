package com.gameportal.web.game.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.game.model.AGElectronic;
import com.gameportal.web.game.model.AGINElectronic;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.MGElectronic;
import com.gameportal.web.game.model.SAElectronic;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Component
public class GamePlatformDao extends   BaseIbatisDAO{

	public Class<GamePlatform> getEntityClass() {
		return GamePlatform.class;
	}
	
	public void saveOrUpdate(GamePlatform entity) {
		if(entity.getGpid() == null) 
			save(entity);
		else 
			update(entity);
	}
	
	public List<GamePlatform> queryAllGame(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".queryAllGame", params);
	}
	
	public List<MGElectronic> queryMobilePT(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".queryMPT",params);
	}

	public void saveMGElectronic(MGElectronic entity){
		getSqlMapClientTemplate().insert(getSimpleName()+".insertMGElectronic",entity);
	}
	
	public Long queryElectronicCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".queryElectronicCount",params);
	}
	
	public List<MGElectronic> queryElectronic(Map<String, Object> params,int startNo,int pagaSize){
		return queryForPager(getSimpleName()+".queryElectronic",params,startNo,pagaSize);
	}
	public List<MGElectronic> queryElectronic(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectElectronic", params);
	}
	public Long queryAgElectronicCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".queryAgElectronicCount",params);
	}
	
	
	public List<AGElectronic> queryAgElectronic(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectAgElectronic", params);
	}
	
	public Long queryAginElecCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".queryAginElectronicCount",params);
	}
	
	
	public List<AGINElectronic> queryAginElec(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectAginElectronic", params);
	}
	
	public Long querySAElecCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".querySAElectronicCount",params);
	}
	
	
	public List<SAElectronic> querySAElec(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectSAinElectronic", params);
	}
}
