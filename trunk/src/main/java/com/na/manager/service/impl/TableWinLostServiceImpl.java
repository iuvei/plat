package com.na.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.na.manager.bean.Page;
import com.na.manager.bean.TableWinLostReportRequest;
import com.na.manager.bean.vo.TableWinLostVO;
import com.na.manager.dao.ITableWinLostMapper;
import com.na.manager.service.ITableWinLostService;

/**
 * @author andy
 * @date 2017年6月23日 下午6:50:51
 * 
 */
@Service
public class TableWinLostServiceImpl implements ITableWinLostService{
	@Autowired
	private ITableWinLostMapper tableWinLostMapper;
	
	@Override
	@Transactional(readOnly = true)
	public Page<TableWinLostVO> queryTableWinLostByPage(TableWinLostReportRequest reportRequest) {
		Page<TableWinLostVO> page = new Page<>(reportRequest);
		List<TableWinLostVO> list = tableWinLostMapper.queryTableWinLostByPage(reportRequest);
		page.setData(list);
		page.setTotal(tableWinLostMapper.getTableWinLostCount(reportRequest));
		return page;
	}
}
