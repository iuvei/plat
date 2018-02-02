package com.na.manager.service;

import java.util.List;

import com.na.manager.entity.MerchantUser;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.MerchantUserStatus;


/**
 * 
 * 
 * @author alan
 * @date 2017年6月23日 上午10:51:11
 */
public interface IMerchantUserService {
	
	void add(MerchantUser merchantUser);
	
	void update(MerchantUser merchantUser);
	
	MerchantUser findById(Long userId);
	
	MerchantUser getMerchantUser(String number, String privateKey);
	
	List<MerchantUser> search(MerchantUser merchantUser);
	
	void modifyStatus(Long id, LiveUserType liveUserType, LiveUserSource source, MerchantUserStatus status);
	
}
