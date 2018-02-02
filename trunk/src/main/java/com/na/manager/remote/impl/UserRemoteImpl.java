package com.na.manager.remote.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.na.light.LightRpcService;
import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.cache.AppCache;
import com.na.manager.entity.AccountRecord;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.Permission;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.remote.AddLiveUserRequest;
import com.na.manager.remote.FindBetOrderRequest;
import com.na.manager.remote.IUserRemote;
import com.na.manager.remote.UpdateUserRequest;
import com.na.manager.service.IAccountRecordService;
import com.na.manager.service.IBetOrderService;
import com.na.manager.service.IChildAccountUserService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IPermissionService;
import com.na.manager.service.IUserService;

/**
 * 远程调用方法.
 * Created by sunny on 2017/7/26 0026.
 */
@LightRpcService(value = "userRemote",group = "remote")
public class UserRemoteImpl implements IUserRemote {
    @Autowired
    private ILiveUserService liveUserService;
    @Autowired
    private IAccountRecordService accountRecordService;
    @Autowired
    private IChildAccountUserService childAccountUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBetOrderService betOrderService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IFinancialFacade financialFacade;

    @PostConstruct
    public void init(){
    }

    @Override
    public void saveMoney(Long userId, BigDecimal balance) {
        LiveUser user = liveUserService.findById(userId);
        financialFacade.modifyBalance(userId,user.getParentId(),UserType.LIVE, AccountRecordType.INTO,ChangeBalanceTypeEnum.SELF,balance,null);
    }

    @Override
    public void drawMoney(Long userId, BigDecimal balance) {
        LiveUser user = liveUserService.findById(userId);
        financialFacade.updateBalance(userId,user.getParentId(),UserType.LIVE, AccountRecordType.OUT,ChangeBalanceTypeEnum.SELF,balance,null);
    }

    @Override
    public BigDecimal drawAllMoney(Long userId) {
        LiveUser user = liveUserService.findById(userId);
        AppCache.setCurrentUser(user);
        financialFacade.updateBalance(userId,user.getParentId(),UserType.LIVE, AccountRecordType.OUT,ChangeBalanceTypeEnum.SELF,user.getBalance(),null);
        return user.getBalance();
    }

    @Override
    public Long addLiveUser(AddLiveUserRequest addLiveUserRequest) {
        LiveUser parentUser = liveUserService.findById(addLiveUserRequest.getParentId());

        LiveUser liveUser = new LiveUser();
        liveUser.setLoginName(addLiveUserRequest.getLoginName());
        liveUser.setNickName(addLiveUserRequest.getNickName());
        liveUser.setHeadPic(addLiveUserRequest.getHeadPic());
        liveUser.setParentId(addLiveUserRequest.getParentId());
        if(addLiveUserRequest.getWashPercentage() !=null && addLiveUserRequest.getWashPercentage().doubleValue() !=-1){
        	liveUser.setWashPercentage(addLiveUserRequest.getWashPercentage());
        }
        if(addLiveUserRequest.getIntoPercentage() !=null && addLiveUserRequest.getIntoPercentage().doubleValue() !=-1){
        	liveUser.setIntoPercentage(addLiveUserRequest.getIntoPercentage());
        }
        liveUser.setSource(addLiveUserRequest.getSource()==null?LiveUserSource.PROXY.get():addLiveUserRequest.getSource());
        liveUser.setIsBet(LiveUserIsBet.TRUE);
        liveUser.setBiggestBalance(BigDecimal.ZERO);
        liveUser.setType(addLiveUserRequest.isPlayer() ? LiveUserType.MEMBER : LiveUserType.PROXY);
        liveUser.setChips(parentUser.getChips());
        liveUser.setPassword(RandomStringUtils.randomAscii(15));
        if(liveUser.getType()==LiveUserType.PROXY.get()){
        	liveUser.setRoomMember(0);
        }
        
        liveUserService.add(liveUser);
        return liveUser.getId();
    }

    @Override
    public Long addSubAccount(String username, Long parentId){
        ChildAccountUser user = new ChildAccountUser();
        user.setLoginName(username);
        user.setParentId(parentId);
        user.setPassword(RandomStringUtils.randomAscii(15));
        List<Permission> permissions = permissionService.findPermissionByUserId(parentId);
        List<String> permissionList = new ArrayList<>();
        permissions.forEach(item->{
            permissionList.add(item.getPermissionID());
        });

        childAccountUserService.add(user,permissionList);
        return user.getId();
    }

    @Override
    public void updateUser(UpdateUserRequest request){
        userService.update(request.getUserId(),request.getHeadPic(),request.getNickName());
        
        LiveUser liveUser = liveUserService.findById(request.getUserId());
        if(liveUser !=null){
        	liveUser.setWashPercentage(request.getWashPercentage());
        	liveUser.setIntoPercentage(request.getIntoPercentage());
        	liveUserService.update(liveUser);
        }
    }

    @Override
    public List<AccountRecord> findAccountRecordBy(Date startTime, Long userId) {
        return accountRecordService.findAccountRecordBy(startTime,userId);
    }

    @Override
    public Page<BetOrderVO> findBetOrder(FindBetOrderRequest request) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmSS");
        try {
            LiveUser user = liveUserService.findById(request.getParentId());
            AppCache.setCurrentUser(user);

            BetOrderReportRequest orderReportRequest = new BetOrderReportRequest();
            if(StringUtils.isNotEmpty(request.getStartTime())){
            	orderReportRequest.setLastUpdateTimeStart(sf.parse(request.getStartTime()));
            }
            if(StringUtils.isNotEmpty(request.getEndTime())){
            	orderReportRequest.setLastUpdateTimeEnd(sf.parse(request.getEndTime()));
            }
            orderReportRequest.setRoundId(request.getRoundId().intValue());
            orderReportRequest.setParentId(request.getParentId());
            orderReportRequest.setPageSize(request.getPageSize());
            orderReportRequest.setCurrentPage(request.getCurrentPage());
            orderReportRequest.setPaths(request.getPath());
            return betOrderService.queryRemoteBetOrderByPage(orderReportRequest);
        }catch (Exception e){
            throw new RuntimeException("查询订单失败",e);
        }
    }

    @Override
    public LiveUser getLiveUser(Long userId) {
    	return liveUserService.findById(userId);
    }
    
    @Override
    public String test(String test) {
        return "hello "+test;
    }

	@Override
	public long findUnsettlementOrder(Long userId) {
		return betOrderService.findUnsettlementOrder(userId);
	}

	@Override
	public List<AccountRecord> findAccountRecordByRoundId(Long roundId) {
		return accountRecordService.findAccountRecordByRoundId(roundId);
	}

	@Override
	public void updataFlag(List<AccountRecord> list) {
		accountRecordService.updataFlag(list);
	}
}
