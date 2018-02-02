package com.na.user.socketserver.service;

import java.util.List;

import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.entity.VirtualGameTablePO;


/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IVirtualGameTableService {
	
	
	public List<VirtualGameTablePO> getTableList(int gameId, int tableId);
	
	public List<Integer> getVirtualTableIdList(int gameId, int tableId);
	
	public List<Integer> getVirtualTypeList(int gameId, VirtualGameTableType typeEnum);
	
	public List<Integer> getVirtualTypeList(int gameId, int type);
	
	public VirtualGameTablePO getVirtualTypeById(int virtualTableId);
}
