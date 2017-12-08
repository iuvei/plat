package com.gameportal.web.game.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Component
@SuppressWarnings("all")
public class GameTransferDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return GameTransfer.class;
	}

	public void saveOrUpdate(GameTransfer entity) {
		if (entity.getGtid() == null)
			save(entity);
		else
			update(entity);
	}
	
	/**
	 * 查询转账订单
	 * @return
	 */
	public List<GameTransfer> selectTranferOrder(Map<String, Object> params){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectTranferOrder",params);
	}
	
	/**
	 * 统计转账数量
	 * @param params
	 * @return
	 */
	public Long selectTranferOrderCount(Map<String, Object> params){
		return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectTranferOrderCount", params);
	}
	
	public GameTransfer getGameTransfer(Map<String, Object> params){
		List<GameTransfer> list = selectTranferOrder(params);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
