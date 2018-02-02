package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.na.manager.bean.AccountModifyBalanceRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.MerchantUser;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.service.IChipService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IMerchantUserService;
import com.na.manager.service.IUserService;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@RestController
@RequestMapping("/merchantUser")
@Auth("SysBuManage")
public class MerchantUserAction  {
	
    @Autowired
    private IMerchantUserService merchantUserService;
    
    @Autowired
    private ILiveUserService liveUserService;
    
    @Autowired
    private IChipService chipService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IFinancialFacade financialFacade;

    @RequestMapping("/search")
    public NaResponse<MerchantUser> search(@RequestBody(required=false) MerchantUser merchantUser) {
        return NaResponse.createSuccess(merchantUserService.search(merchantUser));
    }
    
    @RequestMapping("/create")
    public NaResponse<MerchantUser> create(@RequestBody MerchantUser merchantUser) {
        merchantUserService.add(merchantUser);
        return NaResponse.createSuccess();
    }
    
    @RequestMapping("/update")
    public NaResponse<MerchantUser> update(@RequestBody MerchantUser merchantUser) {
        merchantUserService.update(merchantUser);
        return NaResponse.createSuccess();
    }
    
    @RequestMapping("/getChips")
    public NaResponse<MerchantUser> getChips(@RequestBody LiveUser liveUser) {
    	Preconditions.checkArgument(liveUser.getId() != null,"request.param.not.null");
        return NaResponse.createSuccess(chipService.findTreeChip(liveUser.getId(), "id", "text","type"));
    }
    
    @RequestMapping("/modifyStatus")
    public NaResponse<MerchantUser> modifyStatus(@RequestBody MerchantUser merchantUser) {
    	merchantUserService.modifyStatus(
				merchantUser.getId(), merchantUser.getTypeEnum(),
				merchantUser.getSourceEnum(), merchantUser.getStatusEnum());
        return NaResponse.createSuccess();
    }
    
    @RequestMapping("/modifyBalance")
    @Transactional(rollbackFor=Exception.class)
    public NaResponse<MerchantUser> updateBalance(@RequestBody AccountModifyBalanceRequest request) {
    	financialFacade.updateBalance(request.getId(), request.getParentId(), request.getUserTypeEnum(), request.getAccountRecordTypeEnum(), 
    			ChangeBalanceTypeEnum.PROXY,request.getBalance(), request.getRemark());
    	return NaResponse.createSuccess();
    }
    
}
