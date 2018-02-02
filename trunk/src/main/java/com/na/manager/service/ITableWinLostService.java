package com.na.manager.service;

import com.na.manager.bean.Page;
import com.na.manager.bean.TableWinLostReportRequest;
import com.na.manager.bean.vo.TableWinLostVO;

/**
 * @author andy
 * @date 2017年6月23日 下午6:51:18
 * 
 */
public interface ITableWinLostService {
	Page<TableWinLostVO> queryTableWinLostByPage(TableWinLostReportRequest reportRequest);
}
