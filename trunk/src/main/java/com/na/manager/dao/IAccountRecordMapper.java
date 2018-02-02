package com.na.manager.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.na.manager.bean.AccountRecordReportRequest;
import com.na.manager.entity.AccountRecord;

/**
 * @author andy
 * @date 2017年6月22日 下午5:07:12
 * 
 */
@Mapper
public interface IAccountRecordMapper {
	void add(AccountRecord accountRecord);
	
	List<AccountRecord> queryAccountRecordByPage(AccountRecordReportRequest reportRequest);
	
	Map<String,BigDecimal> queryAccountRecordByPageTotal(AccountRecordReportRequest reportRequest);
	
	long getAccountRecordCount(AccountRecordReportRequest reportRequest);
	
	/**
	 * 推送大厅成功，将记录状态修改为1：已推送
	 * @param list
	 */
	void updataFlag(@Param("list")List<AccountRecord> list);
}
