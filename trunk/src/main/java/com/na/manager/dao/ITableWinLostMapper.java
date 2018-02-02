package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.na.manager.bean.TableWinLostReportRequest;
import com.na.manager.bean.vo.TableWinLostVO;

/**
 * @author andy
 * @date 2017年6月23日 下午6:49:47
 * 
 */
@Mapper
public interface ITableWinLostMapper {
	List<TableWinLostVO> queryTableWinLostByPage(TableWinLostReportRequest reportRequest);
	
	long getTableWinLostCount(TableWinLostReportRequest reportRequest);
	
}
