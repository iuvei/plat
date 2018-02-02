package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.DealerClassRecordSearchRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.service.IDealerClassRecordService;

/**
 * 荷官登录记录
* @author Andy
* @version 创建时间：2017年10月14日 下午3:58:21
*/
@RestController
@RequestMapping("/dealerClassRecord")
@Auth("DealerClassRecord")
public class DealerClassRecordAction {

	@Autowired
	private IDealerClassRecordService dealerClassRecordService;
	
	@PostMapping("/search")
	public NaResponse<DealerClassRecordSearchRequest> search(@RequestBody DealerClassRecordSearchRequest request){
		return NaResponse.createSuccess(dealerClassRecordService.queryRecordByPage(request));
	}
}
