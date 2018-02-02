package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IMerchantUserMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.MerchantUser;
import com.na.manager.entity.Role;
import com.na.manager.entity.User;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.MerchantUserStatus;
import com.na.manager.enums.MerchantUserType;
import com.na.manager.enums.UserType;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IMerchantUserService;
import com.na.manager.service.IRoleService;
import com.na.manager.util.Md5Util;
import com.na.manager.util.StringUtil;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Service
public class MerchantUserServiceImpl implements IMerchantUserService{
    @Autowired
    private IMerchantUserMapper merchantUserMapper;
    @Autowired
    private ILiveUserService liveUserService;
    @Autowired
    private IRoleService roleService;
    
	@Override
	@Transactional(rollbackFor= Exception.class)
	public void add(MerchantUser merchantUser) {
		Preconditions.checkArgument(merchantUserMapper.isRepeatMerchantPerfix(merchantUser.getMerchantPrefix()) < 1,"merchant.perfix.isexit");
		Preconditions.checkArgument(merchantUser.getMerchantTypeEnum() != null,"common.field.not.null");
		Preconditions.checkArgument(merchantUser.getChips() != null,"common.field.not.null");
		
		if(!StringUtils.isEmpty(merchantUser.getAllowIpList())) {
			StringUtil.checkManyIpFormat(merchantUser.getAllowIpList());
		}
		
		merchantUser.setPassword("123456");
		merchantUser.setUserType(UserType.LIVE);
		merchantUser.setSource(LiveUserSource.CASH);
		merchantUser.setType(LiveUserType.PROXY);
		merchantUser.setRoomMember(merchantUser.getRoomMember());
		merchantUser.setNickName(merchantUser.getMerchantPrefix());
		merchantUser.setLoginName("BU_" + merchantUser.getMerchantPrefix());
		//商户代理设置洗码比0
		merchantUser.setWashPercentage(BigDecimal.ZERO);
		
		liveUserService.add(merchantUser);
		
		String randomStr = RandomStringUtils.randomAscii(20);
		merchantUser.setPrivateKey(Md5Util.digest("MD5", randomStr.getBytes(), merchantUser.getPasswordSalt().getBytes(), 3).substring(16));
		merchantUser.setStatusEnum(MerchantUserStatus.ENABLED);
		
		merchantUserMapper.add(merchantUser);
		
		merchantUser.setNumber(generateMerchantNumber(merchantUser.getId()));
		merchantUserMapper.update(merchantUser);
		
		//根据类型添加角色
		if(MerchantUserType.LINE_MERCHANT == merchantUser.getMerchantTypeEnum()) {
			List<Role> roleList = roleService.getRoleByUserId(merchantUser.getParentId());
			if(roleList != null && roleList.size() > 0) {
				roleService.addUserRole(roleList.get(0).getRoleID(), merchantUser.getId());
			}
		} else if(MerchantUserType.MERCHANT == merchantUser.getMerchantTypeEnum()) {
			Role role = roleService.findRoleByName("APIMERCHANT");
			if(role != null) {
				roleService.addUserRole(role.getRoleID(), merchantUser.getId());
			}
		}
	}
	
	@Override
	@Transactional(rollbackFor= Exception.class)
	public void update(MerchantUser merchantUser) {
		Preconditions.checkArgument(merchantUser.getMerchantTypeEnum() != null,"common.field.not.null");
		Preconditions.checkArgument(merchantUser.getChips() != null,"common.field.not.null");
		
		if(!StringUtils.isEmpty(merchantUser.getAllowIpList())) {
			StringUtil.checkManyIpFormat(merchantUser.getAllowIpList());
		}
		
		liveUserService.update(merchantUser);
		merchantUserMapper.update(merchantUser);
	}
	
	@Override
	@Transactional(readOnly=true)
	public MerchantUser findById(Long userId) {
		return merchantUserMapper.fingById(userId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<MerchantUser> search(MerchantUser merchantUser) {
		
		User currenUser = AppCache.getCurrentUser();
		LiveUser realUser = null;
		if(UserType.SUB_ACCOUNT == currenUser.getUserTypeEnum()) {
			realUser = (LiveUser) ((ChildAccountUser)currenUser).getParentUser();
		} else if(UserType.LIVE == currenUser.getUserTypeEnum()) {
			realUser = (LiveUser) currenUser;
		}
		
		if(merchantUser == null) {
			merchantUser = new MerchantUser();
			merchantUser.setId(realUser.getId());
		}
		merchantUser.setParentPath(realUser.getParentPath());
		
		List<MerchantUser> data = merchantUserMapper.search(merchantUser);
		return data;
	}
    
	/**
	 * 
	 * 根据UID生成 商户号
	 * @param agnUid
	 * @return 4位数年份+7位随机数+5位UID
	 */
	public static String  generateMerchantNumber(long userId){
		return String.format("%tY%s%05d",new Date(),RandomStringUtils.randomAlphabetic(7),userId);
	}
	
	@Override
	@Transactional(rollbackFor= Exception.class)
	public void modifyStatus(Long id, LiveUserType liveUserType, LiveUserSource source, MerchantUserStatus status) {
		Preconditions.checkNotNull(id,"common.id.not.null");
		Preconditions.checkArgument(liveUserType != null,"common.field.not.null");
		Preconditions.checkArgument(source != null && source == LiveUserSource.CASH,"common.field.not.null");
		Preconditions.checkNotNull(status,"common.field.not.null");
		
		MerchantUser merchantUser = new MerchantUser();
		merchantUser.setId(id);
		merchantUser.setUserId(id);
		merchantUser.setType(liveUserType);
		merchantUser.setStatusEnum(status);
		merchantUserMapper.update(merchantUser);
	}

	@Override
	@Transactional(readOnly=true)
	public MerchantUser getMerchantUser(String number, String privateKey) {
		return merchantUserMapper.getMerchantUser(number, privateKey);
	}
	
}
