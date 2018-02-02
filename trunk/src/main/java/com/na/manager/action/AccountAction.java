package com.na.manager.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.na.manager.bean.AccountModifyBalanceRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.facade.ILiveUserFacade;
import com.na.manager.service.IChipService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IUserService;

/**
 * 账户管理
 * 
 * @author alan
 * @date 2017年6月23日 下午5:58:59
 */
@Auth("AccountManager")
@RestController
@RequestMapping("/accountManager")
public class AccountAction  {
	
    @Autowired
    private ILiveUserService liveUserService;
    
    @Autowired
    private ILiveUserFacade liveUserFacade;
    
    @Autowired
    private IChipService chipService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IFinancialFacade financialFacade;

    @RequestMapping("/search")
    public NaResponse<AccountVO> search(@RequestBody(required=false) LiveUser liveUser) {
        List<AccountVO> liveUserList = liveUserService.search(liveUser);
        if(CollectionUtils.isEmpty(liveUserList)){
        	return NaResponse.createSuccess();
        }
        Map<String, Object> result = new HashMap<>();
        if(liveUserList != null && liveUserList.size() > 0) {
        	String parentPathName = liveUserService.getParentPathName(liveUserList.get(0).getParentPath());
        	result.put("parentPathName", parentPathName);
        }
        result.put("list", liveUserList);
        return NaResponse.createSuccess(result);
    }
    
    @RequestMapping("/getLowerUser")
    public NaResponse<AccountVO> getLowerUser(@RequestBody LiveUser liveUser) {
    	Preconditions.checkArgument(liveUser.getParentId() != null,"request.param.not.null");
    	
        List<AccountVO> liveUserList = liveUserService.findByParentId(liveUser.getParentId());
        Map<String, Object> result = new HashMap<>();
        if(liveUserList != null && liveUserList.size() > 0) {
        	String parentPathName = liveUserService.getParentPathName(liveUserList.get(0).getParentPath());
        	result.put("parentPathName", parentPathName);
        }
        result.put("list", liveUserList);
        return NaResponse.createSuccess(result);
    }
    
    @PostMapping("/create")
    public NaResponse create(@RequestBody LiveUser user) {
    	user.setSource(LiveUserSource.PROXY);
    	liveUserService.add(user);
        return NaResponse.createSuccess();
    }
    
    @RequestMapping("/getChips")
    public NaResponse<LiveUser> getChips(@RequestBody LiveUser liveUser) {
    	Preconditions.checkArgument(liveUser.getId() != null,"request.param.not.null");
        return NaResponse.createSuccess(chipService.findTreeChip(liveUser.getId(), "data", "label","type"));
    }
    
    @RequestMapping("/modify")
    public NaResponse<LiveUser> modify(@RequestBody LiveUser liveUser) {
    	liveUserService.update(liveUser);
        return NaResponse.createSuccess("modify.success");
    }
    
    @RequestMapping("/modifyStatus")
    public NaResponse<LiveUser> modifyStatus(@RequestBody LiveUser liveUser) {
    	liveUserFacade.modifyStatus(liveUser.getId(), liveUser.getTypeEnum(), liveUser.getUserStatusEnum());
        return NaResponse.createSuccess("modify.success");
    }
    
    @RequestMapping("/modifyBetStatus")
    public NaResponse<LiveUser> modifyBetStatus(@RequestBody LiveUser liveUser) {
    	liveUserFacade.modifyBetStatus(liveUser.getId(), liveUser.getTypeEnum(), liveUser.getIsBetEnum());
        return NaResponse.createSuccess("modify.success");
    }
    
    @RequestMapping("/modifyPassword")
    public NaResponse<LiveUser> modifyPassword(@RequestBody LiveUser liveUser) {
    	userService.updatePassword(liveUser.getId(), liveUser.getUserTypeEnum(), liveUser.getPassword());
        return NaResponse.createSuccess("modify.success");
    }
    
    @RequestMapping("/getUserById")
    public NaResponse<LiveUser> getUserById(@RequestBody LiveUser liveUser) {
    	Preconditions.checkArgument(liveUser != null,"request.param.not.null");
        return NaResponse.createSuccess(liveUserService.findById(liveUser.getId()));
    }
    
    @RequestMapping("/modifyBalance")
    public NaResponse<LiveUser> updateBalance(@RequestBody AccountModifyBalanceRequest request) {
    	financialFacade.updateBalance(request.getId(), request.getParentId(), request.getUserTypeEnum(), request.getAccountRecordTypeEnum(),ChangeBalanceTypeEnum.PROXY,request.getBalance(), request.getRemark());
    	return NaResponse.createSuccess();
    }

}
