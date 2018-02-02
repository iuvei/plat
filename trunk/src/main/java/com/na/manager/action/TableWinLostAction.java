package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.TableWinLostReportRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.ITableWinLostService;

/**
 * @author andy
 * @date 2017年6月23日 下午6:27:28
 * 
 */
@RestController
@RequestMapping("/tableWinLoseReport")
@Auth("TableWinLoseReport")
public class TableWinLostAction {
	
	@Autowired
	private ITableWinLostService tableWinLostService;
	
	@PostMapping("/search")
	public NaResponse<TableWinLostReportRequest> search(@RequestBody TableWinLostReportRequest reportRequest){
		return NaResponse.createSuccess(tableWinLostService.queryTableWinLostByPage(reportRequest));
	}
}
