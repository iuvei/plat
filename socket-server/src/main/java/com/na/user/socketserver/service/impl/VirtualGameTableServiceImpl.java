package com.na.user.socketserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.dao.IVirtualGameTableMapper;
import com.na.user.socketserver.entity.VirtualGameTablePO;
import com.na.user.socketserver.service.IVirtualGameTableService;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class VirtualGameTableServiceImpl implements IVirtualGameTableService {
	
	@Autowired
    private IVirtualGameTableMapper virtualGameTableMapper;
	
	@Override
	public List<VirtualGameTablePO> getTableList(int gameId, int tableId) {
		return virtualGameTableMapper.findAllByGameId(gameId, tableId);
	}

	@Override
	public List<Integer> getVirtualTableIdList(int gameId, int tableId) {
		return virtualGameTableMapper.findIdsByTableIdAndGameId(gameId, tableId);
	}

	@Override
	public List<Integer> getVirtualTypeList(int gameId, VirtualGameTableType typeEnum) {
		if(typeEnum == null) {
			return null;
		}
		return getVirtualTypeList(gameId, typeEnum.get());
	}

	@Override
	public List<Integer> getVirtualTypeList(int gameId, int type) {
		return virtualGameTableMapper.findIdsByTypeAndGameId(gameId, type);
	}

	@Override
	public VirtualGameTablePO getVirtualTypeById(int virtualTableId) {
		return virtualGameTableMapper.findById(virtualTableId);
	}
	

}
