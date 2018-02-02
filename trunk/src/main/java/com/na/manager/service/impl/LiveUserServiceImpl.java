package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.cache.AppCache;
import com.na.manager.common.Constant;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.Role;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserStatus;
import com.na.manager.enums.UserType;
import com.na.manager.service.IBetOrderService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IRoleService;
import com.na.manager.service.IUserService;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Service
public class LiveUserServiceImpl implements ILiveUserService {
	
	private static final Logger log =LoggerFactory.getLogger(LiveUserServiceImpl.class);
	
	@Autowired
	private ILiveUserMapper liveUserMapper;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IRoleService roleService;

	@Autowired
	private IBetOrderService betOrderService;
	
	@Value("${spring.na.platform.proxy.path}")
	private String proxyPath; //成都代理网
	
	@Value("${spring.na.platform.merchant.path}")
	private String merchantPath; //成都现金网
	
	@Value("${spring.na.platform.robot.root}")
	private String robotId; //机器人代理

	@Override
	@Transactional(readOnly = true)
	public LiveUser findById(Long userId) {
		Preconditions.checkArgument(userId != null,"request.param.not.null");
		return liveUserMapper.findLiveUserById(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AccountVO> search(LiveUser liveUser) {
		Preconditions.checkArgument(UserType.LIVE == AppCache.getCurrentUser().getUserTypeEnum()
				|| UserType.SUB_ACCOUNT == AppCache.getCurrentUser().getUserTypeEnum(),"user.has.no.permission");
		if(liveUser!=null && !StringUtils.isEmpty(liveUser.getLoginName())){
			liveUser.setLoginName(liveUser.getLoginName().trim());
		}
		User currenUser = AppCache.getCurrentUser();
		if(liveUser == null) {
			liveUser = new LiveUser();
			if(UserType.SUB_ACCOUNT == currenUser.getUserTypeEnum()) {
				LiveUser parentUser = (LiveUser) ((ChildAccountUser)currenUser).getParentUser();
				liveUser.setId(parentUser.getId());
				liveUser.setParentPath(parentUser.getParentPath());
			} else if (UserType.LIVE == currenUser.getUserTypeEnum()) {
				liveUser.setId(currenUser.getId());
				liveUser.setParentPath(((LiveUser)currenUser).getParentPath());
			}
		} else {
			liveUser.setParentPath(((LiveUser)currenUser).getParentPath());
		}
		
		return liveUserMapper.search(liveUser);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void add(LiveUser liveUser) {
		Preconditions.checkArgument(liveUser.getType() != null,"liveuser.type.not.null");
		Preconditions.checkArgument(liveUser.getSource() != null,"liveuser.source.not.null");
		Preconditions.checkArgument(liveUser.getChips() != null,"user.washpercentage.not.null");
		
		LiveUser parentUser = findById(liveUser.getParentId());
		if(parentUser.getWashPercentage()==null || parentUser.getWashPercentage().doubleValue()==-1){
			parentUser.setWashPercentage(BigDecimal.ZERO);
		}
		Preconditions.checkArgument(LiveUserType.MEMBER != parentUser.getTypeEnum(),"member.not.allow.create.user");
		
		if(liveUser.getWashPercentage()==null || liveUser.getWashPercentage().doubleValue()==-1){
			liveUser.setWashPercentage(BigDecimal.ZERO);
		}else{
			liveUser.setWashPercentage(liveUser.getWashPercentage().divide(new BigDecimal(100)));
		}
		if(parentUser.getSourceEnum() == LiveUserSource.PROXY){
			Preconditions.checkArgument(parentUser.getWashPercentage().compareTo(liveUser.getWashPercentage()) >= 0,"washpercentage.not.allow.high.than.parent");
		}
		if(liveUser.getType()!=LiveUserType.MEMBER.get()){
			if(liveUser.getIntoPercentage()==null || liveUser.getIntoPercentage().doubleValue()==-1){
				liveUser.setIntoPercentage(BigDecimal.ZERO);
			}else{
				liveUser.setIntoPercentage(liveUser.getIntoPercentage().divide(new BigDecimal(100)));
			}
			Preconditions.checkArgument(parentUser.getIntoPercentage().compareTo(liveUser.getIntoPercentage()) >= 0,"intopercentage.not.allow.high.than.parent");
		}
		
		BigDecimal initBlance = null;
		if(liveUser.getBalance() != null && liveUser.getBalance().compareTo(new BigDecimal(0)) > 0) {
			initBlance = liveUser.getBalance();
			liveUser.setBalance(null);
		}
		
		liveUser.setUserType(UserType.LIVE);
		userService.add(liveUser);
		liveUser.setUserId(liveUser.getId());
		liveUser.setIsBet(LiveUserIsBet.TRUE);
		liveUser.setParentPath(parentUser.getParentPath() + liveUser.getId() + Constant.COMMA);
		
		liveUserMapper.add(liveUser);
		
		List<Role> roleList = roleService.getRoleByUserId(parentUser.getId());
		if(roleList != null && roleList.size() > 0) {
			roleService.addUserRole(roleList.get(0).getRoleID(), liveUser.getId());
		}
		
		if(LiveUserSource.CASH != liveUser.getSourceEnum() && liveUser.getParentPath().indexOf(proxyPath) == -1) { //非现金网
			if(initBlance != null) {
				updateBalance(liveUser.getId(), liveUser.getParentId(), liveUser.getUserTypeEnum(),AccountRecordType.INTO,ChangeBalanceTypeEnum.PROXY,initBlance, "创建用户");
			}
		}else{
			if(initBlance != null) {
				updateBalance(liveUser.getId(), liveUser.getParentId(), liveUser.getUserTypeEnum(),AccountRecordType.INTO,ChangeBalanceTypeEnum.SELF,initBlance, "创建用户");
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<AccountVO> findByParentId(Long parentId) {
		return liveUserMapper.findByParentId(parentId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public LiveUser modifyBetStatus(Long id, LiveUserType liveUserType, LiveUserIsBet isBet) {
		Preconditions.checkArgument( id != null ,"request.param.not.null");
		Preconditions.checkArgument( liveUserType != null ,"request.param.not.null");
		Preconditions.checkArgument( isBet != null ,"request.param.not.null");
		
		LiveUser nowLiveUser = findById(id);
		
		LiveUser liveUser = new LiveUser();
		liveUser.setId(id);
		liveUser.setType(liveUserType);
		liveUser.setIsBet(isBet);
		liveUser.setParentPath(nowLiveUser.getParentPath());
		liveUserMapper.modifyBetStatus(liveUser);
		return liveUser;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void update(LiveUser liveUser) {
		Preconditions.checkArgument(liveUser.getId() != null ,"common.id.not.null");
		Preconditions.checkArgument(liveUser.getUserType() != null ,"common.field.not.null");
		
		LiveUser dataLiveUser = findById(liveUser.getId());
		if(LiveUserType.MEMBER == dataLiveUser.getTypeEnum() && liveUser.getBiggestBalance() != null && liveUser.getBiggestBalance().compareTo(BigDecimal.ZERO) != 0) {
			Preconditions.checkArgument(liveUser.getBiggestBalance().compareTo(dataLiveUser.getBalance()) >= 0,"not.allow.high.than.maxbalance");
		}
		if(liveUser.getWashPercentage() !=null && liveUser.getWashPercentage().doubleValue()!=-1){
			liveUser.setWashPercentage(liveUser.getWashPercentage().divide(new BigDecimal(100)));
		}
		if(liveUser.getIntoPercentage() !=null && liveUser.getIntoPercentage().doubleValue() !=-1){
			liveUser.setIntoPercentage(liveUser.getIntoPercentage().divide(new BigDecimal(100)));
		}
		userService.update(liveUser);
		LiveUser oldLiveUser =liveUserMapper.findLiveUserById(liveUser.getId());
		
		liveUser.setUserId(liveUser.getId());
		liveUserMapper.update(liveUser);
		
		String removeChip=",";
		for (String oldChip : oldLiveUser.getChips().split(",")) {
			if(StringUtils.isEmpty(oldChip)) continue;
			if (liveUser.getChips().indexOf("," + oldChip + ",") == -1) {
				if (removeChip.length() > 1) {
					removeChip += ",";
				}
				removeChip += oldChip;
			}
		}
		//存在移除限红项,则处理下级
		if(removeChip.length()>1){
			Boolean isUpdate=false;
			List<LiveUser> liveUserList = liveUserMapper.findTeamMemberById(liveUser.getId());
			for (LiveUser item : liveUserList) {
				Boolean isBAC=false;
				Boolean isRou=false;
				if(item.getId() !=liveUser.getId()){
					for(String chip : removeChip.split(",")){
						if(StringUtils.isEmpty(chip)) continue;
						if(item.getChips().indexOf(","+chip+",")!=-1){
							isUpdate =true;
							item.setChips(item.getChips().replace(chip+",", ""));
						}
					}
					if(isUpdate){
						//1-6百家乐  7-12轮盘
						for(String c : item.getChips().split(",")){
							if(StringUtils.isEmpty(c)) continue;
							if(Integer.valueOf(c)<=6){
								isBAC =true;
							}else{
								isRou =true;
							}
						}
						// 没有百家乐限红，取上级最小的
						if(!isBAC){
							log.info("用户{}没有轮盘限红啦",item.getLoginName());
							LiveUser parent =liveUserMapper.findLiveUserById(item.getParentId());
							item.setChips(item.getChips()+getMinChipByParent(parent.getChips(),1,6));
						}
						// 没有轮盘限红，取上级最小的
						if(!isRou){
							log.info("用户{}没有轮盘限红啦",item.getLoginName());
							LiveUser parent =liveUserMapper.findLiveUserById(item.getParentId());
							item.setChips(item.getChips()+getMinChipByParent(parent.getChips(),7,12));
						}
						liveUserMapper.update(item);
					}
				}
			}
		}
	}
	
	private String getMinChipByParent(String chips,int min,int max){
		int x=1000;
		for (String chip : chips.split(",")) {
			if(StringUtils.isEmpty(chip)) continue;
			int c =Integer.valueOf(chip);
			if(c>=min && c<=max && c<x){
				x=c;
			}
		}
		return x+",";
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public LiveUser modifyStatus(Long id, LiveUserType liveUserType, UserStatus userStatus) {
		Preconditions.checkArgument( id != null ,"request.param.not.null");
		Preconditions.checkArgument( liveUserType != null,"request.param.not.null");
		Preconditions.checkArgument( userStatus != null ,"request.param.not.null");
		
		LiveUser nowLiveUser = findById(id);
		
		LiveUser liveUser = new LiveUser();
		liveUser.setId(id);
		liveUser.setType(liveUserType);
		liveUser.setUserStatusEnum(userStatus);
		liveUser.setParentPath(nowLiveUser.getParentPath());
		liveUserMapper.modifyStatus(liveUser);
		return liveUser;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateBalance(Long userId, Long parentId, UserType userType,
			AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance,
			String remark) {
		Preconditions.checkArgument(balance.compareTo(new BigDecimal(0)) >=0 ,"point.only.allow.greater.0");
		
		LiveUser liveUser = findById(userId);
		
		if(AccountRecordType.INTO == accountRecordType) {
			if(LiveUserType.MEMBER == liveUser.getTypeEnum() && liveUser.getBiggestBalance() != null && liveUser.getBiggestBalance().compareTo(BigDecimal.ZERO) != 0) {
				Preconditions.checkArgument(liveUser.getBiggestBalance().compareTo(liveUser.getBalance().add(balance)) >= 0,"not.allow.high.than.maxbalance");
			}
		} else if(AccountRecordType.OUT == accountRecordType) {
			Preconditions.checkArgument(liveUser.getBalance().compareTo(balance) >= 0 ,"user.balance.not.enough");
		}
		
		if(liveUser.getParentId() != null) {
			Preconditions.checkArgument( parentId.compareTo(liveUser.getParentId()) == 0 ,"only.allow.parent.modify");
			
			LiveUser parentUser = findById(liveUser.getParentId());
			
			AccountRecordType parentAccountRecordType = null;
			//成都用户不扣上级资金
			if (liveUser.getParentPath().indexOf(proxyPath) == -1
					&& liveUser.getParentPath().indexOf(merchantPath) == -1
					&& liveUser.getParentPath().indexOf(Constant.COMMA + robotId + Constant.COMMA) == -1) {
				if(AccountRecordType.INTO == accountRecordType) {
					Preconditions.checkArgument(parentUser.getBalance().compareTo(balance) >= 0 ,"parent.user.balance.not.enough");
					parentAccountRecordType = AccountRecordType.OUT;
				} else if(AccountRecordType.OUT == accountRecordType) {
					parentAccountRecordType = AccountRecordType.INTO;
				}
				userService.updateBalance(userId, userType, accountRecordType,changeType, balance, remark);
				userService.updateBalance(parentUser.getId(), parentUser.getUserTypeEnum(), parentAccountRecordType,changeType,
						balance, remark);
			}else{
				if(AccountRecordType.INTO == accountRecordType) {
					parentAccountRecordType = AccountRecordType.OUT;
				} else if(AccountRecordType.OUT == accountRecordType) {
					parentAccountRecordType = AccountRecordType.INTO;
				}
				userService.updateBalance(userId, userType, accountRecordType,changeType, balance, remark);
			}
		} else {
			userService.updateBalance(userId, userType,accountRecordType,changeType, balance, remark);
		}
		betOrderService.updateModifiedUserBalance(userId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void modifyBalance(Long userId, Long parentId, UserType userType,
			AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance,
			String remark) {
		Preconditions.checkArgument(balance.compareTo(new BigDecimal(0)) >=0 ,"point.only.allow.greater.0");
		
		LiveUser liveUser = findById(userId);
		
		if(AccountRecordType.INTO == accountRecordType) {
			if(LiveUserType.MEMBER == liveUser.getTypeEnum() && liveUser.getBiggestBalance() != null && liveUser.getBiggestBalance().compareTo(BigDecimal.ZERO) != 0) {
				Preconditions.checkArgument(liveUser.getBiggestBalance().compareTo(liveUser.getBalance().add(balance)) >= 0,"not.allow.high.than.maxbalance");
			}
		} else if(AccountRecordType.OUT == accountRecordType) {
			Preconditions.checkArgument(liveUser.getBalance().compareTo(balance) >= 0 ,"user.balance.not.enough");
		}
		
		if(liveUser.getParentId() != null) {
			Preconditions.checkArgument( parentId.compareTo(liveUser.getParentId()) == 0 ,"only.allow.parent.modify");
			
			LiveUser parentUser = findById(liveUser.getParentId());
			
			AccountRecordType parentAccountRecordType = null;
			//成都用户不扣上级资金
			if (liveUser.getParentPath().indexOf(proxyPath) == -1
					&& liveUser.getParentPath().indexOf(merchantPath) == -1 
					&& liveUser.getParentPath().indexOf(Constant.COMMA + robotId + Constant.COMMA) == -1) {
				if(AccountRecordType.INTO == accountRecordType) {
					Preconditions.checkArgument(parentUser.getBalance().compareTo(balance) >= 0 ,"parent.user.balance.not.enough");
					parentAccountRecordType = AccountRecordType.OUT;
				} else if(AccountRecordType.OUT == accountRecordType) {
					parentAccountRecordType = AccountRecordType.INTO;
				}
				userService.modifyBalance(userId, userType, accountRecordType,changeType, balance, remark);
				userService.modifyBalance(parentUser.getId(), parentUser.getUserTypeEnum(), parentAccountRecordType,changeType,
						balance, remark);
			}else{
				if(AccountRecordType.INTO == accountRecordType) {
					parentAccountRecordType = AccountRecordType.OUT;
				} else if(AccountRecordType.OUT == accountRecordType) {
					parentAccountRecordType = AccountRecordType.INTO;
				}
				userService.modifyBalance(userId, userType, accountRecordType,changeType, balance, remark);
			}
		} else {
			userService.modifyBalance(userId, userType,accountRecordType,changeType, balance, remark);
		}
		betOrderService.updateModifiedUserBalance(userId);
	}
	
	

	@Override
	public String getParentPathName(String parentPath) {
		String path = Constant.COMMA;
		for(String s : parentPath.split(Constant.COMMA)) {
			if(!StringUtils.isEmpty(s)) {
				LiveUser user = findById(Long.parseLong(s));
				if(user != null) {
					path += user.getLoginName() + Constant.COMMA;
				}
			}
			
		}
		return path;
	}

	@Override
	public List<AccountVO> findOnlineAllUserByParentId(String parentPath) {
		return liveUserMapper.findOnlineAllUserByParentId(parentPath);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void betchAdd(List<LiveUser> liveUserList) {
		liveUserList.forEach( item -> {
			add(item);
		});
	}

	@Override
	public LiveUser findByLoginName(String loginName) {
		return liveUserMapper.findLiveUserByUserName(loginName);
	}

	@Override
	public List<LiveUser> findLiveUserByParentId(Long parentId) {
		return liveUserMapper.findLiveUserByParentId(parentId);
	}
	
	@Override
	public List<AccountVO> findLiveUserByParentIdForPage(Long parentId, Long startRow, Long pageSize) {
		return liveUserMapper.findLiveUserByParentIdForPage(parentId, startRow, pageSize);
	}

	@Override
	public long countByParentId(Long parentId) {
		return liveUserMapper.countByParentId(parentId);
	}

	@Override
	public LiveUser findLiveUserByUserName(String userName) {
		return liveUserMapper.findLiveUserByUserName(userName);
	}

	@Override
	public int countOnlineByParentName(String agentName) {
		return liveUserMapper.countOnlineByParentName(agentName);
	}
}
