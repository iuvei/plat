package com.na.gate.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.na.gate.cache.GateCache;
import com.na.gate.common.GateConfig;
import com.na.gate.dao.IPlatformUserAdapterMapper;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.enums.LiveUserSource;
import com.na.gate.enums.PlatformUserAdapterType;
import com.na.gate.proto.bean.MerchantJson;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.manager.remote.AddLiveUserRequest;
import com.na.manager.remote.IUserRemote;
import com.na.manager.remote.UpdateUserRequest;

/**
 * Created by Administrator on 2017/7/26 0026.
 */
@Service
public class PlatformUserAdapterImpl implements IPlatformUserAdapterService {
    public static final String PLATFORM_NO_PARENT = "00";

    @Autowired
    private GateConfig gateConfig;

    @Autowired
    private IPlatformUserAdapterMapper platformUserAdapterMapper;
    @Autowired
    private IUserRemote userRemote;

    @Override
    public PlatformUserAdapter add(MerchantJson user) {
        Preconditions.checkNotNull(user.getId(),"PlatformUserId not null");
        Preconditions.checkNotNull(user.getUsername(),"PlatformUserName not null");
        Preconditions.checkNotNull(user.getType(),"type not null");
        
        //玩家大厅没有传值
        if(StringUtils.isEmpty(user.getRole())){
        	user.setRole("10000");
        }
        PlatformUserAdapter userAdapter = platformUserAdapterMapper.findBy(user.getId(),user.getType().get());
        if(userAdapter!=null) {
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setUserId(userAdapter.getLiveUserId());
            if(user.getHeadPic()!=null){
            	updateUserRequest.setHeadPic(user.getHeadPic());
            }
            if(user.getNickname()!=null){
            	updateUserRequest.setNickName(user.getNickname());
            }
            if(user.getLiveMix() !=null){
            	if(user.getLiveMix().intValue()!=-1){
            		updateUserRequest.setWashPercentage(user.getLiveMix());
            	}else if(user.getLiveMix().intValue()==-1 && gateConfig.getUpdateFlag() == 1){ //1 修改 0 不修改
            		updateUserRequest.setWashPercentage(BigDecimal.ZERO);
            	}
            }else{
            	updateUserRequest.setWashPercentage(BigDecimal.ZERO);
            }
            
            if(user.getRate() !=null){
            	if(user.getRate().intValue() !=-1){
                	updateUserRequest.setIntoPercentage(user.getRate());
                }else if(user.getRate().intValue() ==-1 && gateConfig.getUpdateFlag() == 1){ //1 修改 0 不修改
                	updateUserRequest.setIntoPercentage(BigDecimal.ZERO);
                }
            }else{
            	updateUserRequest.setIntoPercentage(BigDecimal.ZERO);
            }
            userRemote.updateUser(updateUserRequest);
            return userAdapter;
        }

        userAdapter = new PlatformUserAdapter();
        userAdapter.setPlatformUserName(user.getUsername());
        userAdapter.setPlatformUserId(user.getId());
        userAdapter.setType(user.getType().get());
        userAdapter.setPlatformParentId(user.getParentId());
        Long liveUserId = null;
        if (userAdapter.getTypeEnum()==PlatformUserAdapterType.ADMIN){ //平台管理员
        	liveUserId = userRemote.addSubAccount(user.getUsername(),gateConfig.getPlatformAdminUserRoot());
        }else if("00".equals(user.getParentId()) && "1000".equals(user.getRole())){  //代理管理员
        	liveUserId = userRemote.addSubAccount(user.getUsername(),gateConfig.getPlatformProxyUserRoot());
        }else if("00".equals(user.getParentId()) && "100".equals(user.getRole())){ //商户管理员
        	liveUserId = userRemote.addSubAccount(user.getUsername(),gateConfig.getPlatformMerchantRoot());
        }else {
            AddLiveUserRequest request = new AddLiveUserRequest();
            request.setLoginName(userAdapter.getPlatformUserName());
            request.setPlayer(false);
            request.setPlayer(user.getType() == PlatformUserAdapterType.PLAYER ? true : false);
            request.setNickName(user.getNickname());
            request.setHeadPic(user.getHeadPic());
            PlatformUserAdapter parentAdapter = platformUserAdapterMapper.findMerchantBy(user.getParentId());
            if(!"01".equals(user.getParentId())){
            	Preconditions.checkNotNull(parentAdapter, "parent not exist");
            }
            if("1000".equals(user.getRole())){  // 代理1000 商户 100
            	if(parentAdapter ==null){
            		request.setParentId(gateConfig.getPlatformProxyUserRoot());
            	}else{
            		request.setParentId(parentAdapter.getLiveUserId());
            	}
        		if(user.getUsername().equals(gateConfig.getRoomAgentName())){//根据代理名称定制包房专线
        			request.setParentId(gateConfig.getRoomRoot()); 
        		}
        		request.setSource(LiveUserSource.PROXY.get());
            }else{
            	if(parentAdapter ==null){
            		request.setParentId(gateConfig.getPlatformMerchantRoot());
            	}else{
            		request.setParentId(parentAdapter.getLiveUserId());
            	}
            	request.setSource(LiveUserSource.CASH.get());
            }
//            if(parentAdapter.getType()==PlatformUserAdapterType.MERCHANT.get()){
//            	request.setSource(LiveUserSource.CASH.get());
//            }else{
//            	request.setSource(LiveUserSource.PROXY.get());
//            }
            
            if(user.getLiveMix() !=null && user.getLiveMix().intValue()!=-1){
            	request.setWashPercentage(user.getLiveMix());
            }else{
            	request.setWashPercentage(BigDecimal.ZERO);
            }
            if(user.getRate() !=null && user.getRate().intValue() !=-1){
            	request.setIntoPercentage(user.getRate());
            }else{
            	request.setIntoPercentage(BigDecimal.ZERO);
            }
            liveUserId = userRemote.addLiveUser(request);
        }

        userAdapter.setLiveUserId(liveUserId);
        platformUserAdapterMapper.add(userAdapter);
        
        //新增用户加入缓存中
        GateCache.PLATFORM_USER_ADAPTER_MAP.put(liveUserId, userAdapter);

        return userAdapter;
    }


    @Override
    public PlatformUserAdapter findByLiveUserId(Long liveUserId) {
        return platformUserAdapterMapper.findByLiveUserId(liveUserId);
    }

    @Override
    public PlatformUserAdapter findByPlatformUserName(String platformUserName) {
        return platformUserAdapterMapper.findByPlatformUserName(platformUserName);
    }

    @Override
    public PlatformUserAdapter findByMerchantUserId(String platformUserId) {
        return platformUserAdapterMapper.findMerchantBy(platformUserId);
    }

    @Override
    public List<PlatformUserAdapter> findByLiverUserIds(Set<Long> liveUserIds){
        return platformUserAdapterMapper.findByLiverUserIds(liveUserIds);
    }


	@Override
	public PlatformUserAdapter findMerchantByParentId(String platformParentId) {
		return platformUserAdapterMapper.findMerchantByParentId(platformParentId);
	}


	@Override
	public PlatformUserAdapter findBy(String platformUserId, int type) {
		return platformUserAdapterMapper.findBy(platformUserId, type);
	}


	@Override
	public List<PlatformUserAdapter> findAll() {
		return platformUserAdapterMapper.findAll();
	}
}
