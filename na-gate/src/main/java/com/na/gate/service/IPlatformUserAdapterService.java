package com.na.gate.service;

import java.util.List;
import java.util.Set;

import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.proto.bean.MerchantJson;

/**
 * Created by sunny on 2017/7/26 0026.
 */
public interface IPlatformUserAdapterService {
    PlatformUserAdapter add(MerchantJson user);

    PlatformUserAdapter findByLiveUserId(Long liveUserId);

    PlatformUserAdapter findByPlatformUserName(String platformUserName);
    
    PlatformUserAdapter findBy(String platformUserId,int type);

    PlatformUserAdapter findByMerchantUserId(String platformUserId);
    
    PlatformUserAdapter findMerchantByParentId(String platformParentId);

    List<PlatformUserAdapter> findByLiverUserIds(Set<Long> liveUserIds);
    
    List<PlatformUserAdapter> findAll();
}
