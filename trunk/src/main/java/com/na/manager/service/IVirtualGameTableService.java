package com.na.manager.service;

import java.util.List;

import com.na.manager.bean.Page;
import com.na.manager.bean.VirtualGameTableSearchRequest;
import com.na.manager.entity.PercentageConfig;
import com.na.manager.entity.VirtualGameTable;

/**
 * Created by Administrator on 2017/7/25.
 */
public interface IVirtualGameTableService {
	
	VirtualGameTable add(VirtualGameTable virtualGameTable);
	
	Page<VirtualGameTable> queryVIPRoomByPage(VirtualGameTableSearchRequest request);
	
	Page<VirtualGameTable> queryVirtualGameTableByPage(VirtualGameTableSearchRequest request);
		
	void update(VirtualGameTable virtualGameTable);
	
	void modifyPassword(VirtualGameTable virtualGameTable);
	
	VirtualGameTable findById(Integer id);
	
	void delete(Integer id);
	
	void delete(VirtualGameTable gameTable);

	/**
	 * 批量添加普通房间功能。
	 * @param gameTableId
	 * @param size
	 */
	void addCommonBatch(Integer gameTableId,int size);
	
	List<PercentageConfig> getPercentageConfig(Long userId, String idName, String textName);
}
