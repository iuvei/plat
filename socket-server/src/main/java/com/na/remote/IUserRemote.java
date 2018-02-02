package com.na.remote;

import java.math.BigDecimal;

import com.na.light.LightRpcService;
import com.na.manager.remote.AddLiveUserRequest;

/**
 * Created by sunny on 2017/7/26 0026.
 */
@LightRpcService("userRemote")
public interface IUserRemote {
	
	 /**
     * 存款
     * @param userId
     * @param balance
     */
    void saveMoney(Long userId, BigDecimal balance);
	
    /**
     * 增加真人用户.
     * @param addLiveUserRequest
     */
    Long addLiveUser(AddLiveUserRequest addLiveUserRequest);

}
