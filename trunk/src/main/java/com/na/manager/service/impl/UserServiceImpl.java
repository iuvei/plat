package com.na.manager.service.impl;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.na.manager.bean.Page;
import com.na.manager.bean.UserSearchRequest;
import com.na.manager.bean.vo.LoginLogVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IPermissionMapper;
import com.na.manager.dao.IRoleMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.MerchantUser;
import com.na.manager.entity.Permission;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LoginStatusEnum;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.enums.UserStatus;
import com.na.manager.enums.UserType;
import com.na.manager.service.IAccountRecordService;
import com.na.manager.service.IChildAccountUserService;
import com.na.manager.service.IMerchantUserService;
import com.na.manager.service.IUserService;
import com.na.manager.util.Md5Util;
import com.na.manager.util.RequestUtil;
import com.na.manager.util.TokenUtil;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Service
@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {
    private Logger loginLogger = LoggerFactory.getLogger("login");

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private IRoleMapper roleMapper;
    @Autowired
    private IPermissionMapper permissionMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IAccountRecordService accountRecordService;
    @Autowired
    private ILiveUserMapper liveUserMapper;
    @Autowired
    private IChildAccountUserService childAccountUserService;
    @Autowired
    private IMerchantUserService merchantUserService;

    @Override
    @Transactional(readOnly = true)
    public User login(String loginName, String pwd, String code, String token, Integer language){
        LoginLogVO loginLogVO = new LoginLogVO();
        String ip = RequestUtil.getIpAddr();
        User user = null;
        try {
            if(token==null) {
                Preconditions.checkNotNull(loginName, "user.loginname.not.null");
                Preconditions.checkNotNull(loginName, "user.pwd.not.null");
                Preconditions.checkNotNull(code, "user.code.not.null");


                String rCode = stringRedisTemplate.boundValueOps(RedisKeyEnum.CAPTCHA_ATTR.get(loginName)).get();
                stringRedisTemplate.delete(RedisKeyEnum.CAPTCHA_ATTR.get(loginName));

                Preconditions.checkArgument(code.equals(rCode), "user.code.error");

                user = userMapper.getUser(loginName);
                Preconditions.checkNotNull(user, "user.loginname.pwd.error");

                String userPwd = digestPassword(pwd, user.getPasswordSalt());
                Preconditions.checkArgument(user.getPassword().equals(userPwd), "user.loginname.pwd.error");
            }else {
                String sUserId = stringRedisTemplate.boundValueOps(RedisKeyEnum.PLATFORM_USER_TOKEN.get(token)).get();
                Preconditions.checkNotNull(sUserId,"user.token.invalid");

                Long userId = Long.valueOf(sUserId);
                ChildAccountUser childAccountUser =childAccountUserService.findChildAccountUserById(userId);
                if(childAccountUser !=null){
                	userId = childAccountUser.getParentId();
                }
                
                user = userMapper.findUserById(userId);
                Preconditions.checkNotNull(user, "user.loginname.pwd.error");
            }
            if(language != null){
                user.setLanguage(language);
            }
            check(user.getUserStatusEnum() == UserStatus.DIS_ENABLED, "user.status.dis");
            check(user.getUserStatusEnum() == UserStatus.LOCK, "user.status.lock");

            user = getReaUser(user);

            user.setRoles(roleMapper.findRoleByUserId(user.getId()));
            user.setPermissions(permissionMapper.findPermissionByUserId(user.getId()));
            for (Permission item : user.getPermissions()) {
                user.addMenu(AppCache.getMenuBy(item.getGroupID()));
            }

            user.setToken(TokenUtil.createToken(user.getId()));

            redisTemplate.boundValueOps(RedisKeyEnum.USER_LOGIN_TOEKN.get(user.getToken())).set(user, RedisKeyEnum.USER_LOGIN_TOEKN.getTtl(), TimeUnit.SECONDS);
            
            AppCache.setCurrentUser(user);
            
            String message = new StringBuilder()
                    .append(user.getId()).append(",")
                    .append(user.getToken()).append(",")
                    .append(ip)
                    .toString();
            stringRedisTemplate.convertAndSend(RedisKeyEnum.EVENT_MANAGE_FORCE_EXIT.get(), message);

            loginLogVO.setLoginStatus(LoginStatusEnum.SUCCESS.get());
        }catch (Exception e){
            loginLogVO.setLoginStatus(LoginStatusEnum.FAIL.get());
            loginLogVO.setRemark(e.getMessage());
            throw  e;
        }finally {
            if(user!=null) {
                loginLogVO.setLoginName(user.getLoginName());
                loginLogVO.setNickName(user.getNickName());
                loginLogVO.setIpAddr(ip);
                loginLogVO.setLoginDate(new Date());
                loginLogVO.setUserType(user.getUserType());
                if (user instanceof LiveUser) {
                    LiveUser temp = (LiveUser) user;
                    loginLogVO.setParentPath(temp.getParentPath());
                    loginLogVO.setType(temp.getType());
                }
                loginLogger.info(JSON.toJSONString(loginLogVO));
            }
        }
        return user;
    }

    /**
     * 返回真实用户实体。
     * @param user
     * @return
     */
    private User getReaUser(final User user) {
        User realUser = user;
        switch (user.getUserTypeEnum()){
            case SYS:{
                break;
            }
            case SUB_ACCOUNT:{
                realUser = childAccountUserService.findChildAccountUserById(user.getId());
                break;
            }
            case LIVE:{
                LiveUser liveUser = liveUserMapper.findLiveUserById(user.getId());
                switch (liveUser.getTypeEnum()){
                    case PROXY:
                    case GENERAL_PROXY: {
                        if(liveUser.getSourceEnum()== LiveUserSource.ALL || liveUser.getSourceEnum()==LiveUserSource.CASH){
                            MerchantUser merchantUser = merchantUserService.findById(user.getId());
                            if(merchantUser!=null) {
                                if (!isMerchantUserAllowAccess(merchantUser)) {
                                    throwIllegalUser();
                                }
                                realUser = merchantUser;
                            }else{
                            	realUser = liveUser;// 成都商户数据
                            }
                        }else {
                            realUser = liveUser;
                        }
                        break;
                    }
                    default:{
                        throwIllegalUser();
                    }
                }
                break;
            }
            default:{
                throwIllegalUser();
            }
        }
        return realUser;
    }

    /**
     * 抛出非法用户访问异常.
     */
    private void throwIllegalUser(){
        throw new IllegalArgumentException("user.illegal.user");
    }

    /**
     * 检查条件是否为真，为真则抛出异常。
     */
    private void check(boolean b,String msg){
        if(b) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 判定当前商户访问IP是否在允许范围以内。
     * @param merchantUser
     */
    private boolean isMerchantUserAllowAccess(MerchantUser merchantUser) {
        boolean result = false;
        if(StringUtils.isBlank(merchantUser.getAllowIpList())){
            return true;
        }

        String[] allowIpList = merchantUser.getAllowIpList().split(";");
        String ip = RequestUtil.getIpAddr();
        for(String item : allowIpList){
            result = ip.equals(item.trim());
            if (result)break;
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> search(UserSearchRequest request) {
        Page page = new Page(request);
        page.setTotal(userMapper.count(request));
        if(page.getTotal()>0){
            page.setData(userMapper.search(request));
        }
        return page;
    }

	@Override
	public void update(User user) {
		userMapper.update(user);
	}
	
	@Override
	public void update(Long userId, String headPic, String nickName) {
		userMapper.modify(userId, headPic, nickName);
	}

	@Override
	public void changePassword(User user) {
		if(user.getPassword() == null) {
			throw new RuntimeException("密码不允许为空");
		}
        updatePassword(user.getId(),user.getUserTypeEnum(),user.getPassword());
	}
	
    @Override
    public void add(User user) {
        Preconditions.checkArgument(StringUtils.isNotBlank(user.getLoginName()),"user.loginname.not.null");
        Preconditions.checkArgument(StringUtils.isNotBlank(user.getPassword()),"user.pwd.not.null");
        //成都代理用户没有带前缀且5位以上
        //Preconditions.checkArgument(user.getLoginName().length() >= 5 && user.getLoginName().length() <= 30,"user.loginname.check.length");
        Preconditions.checkArgument(user.getPassword().length() >= 6 && user.getPassword().length() <= 30,"user.pwd.check.length");
        Preconditions.checkArgument(findUserByLoginName(user.getLoginName()) == null,"loginname.exist");
        
        user.setPasswordSalt(RandomStringUtils.randomAlphabetic(24));

        user.setPassword(digestPassword(user.getPassword(),user.getPasswordSalt()));
        user.setUserStatusEnum(UserStatus.ENABLED);
        user.setCreateBy(AppCache.getCurrentUser()==null ? null : AppCache.getCurrentUser().getLoginName());

        if(user.getUserTypeEnum()==UserType.DEALER){
            User temp = null;
            do {
                user.setBarrcode(makeBarracode(UserType.DEALER));
                temp = userMapper.findUserByBarrcode(user.getBarrcode());
            }while(temp!=null);
        }

        userMapper.addUser(user);
        if(user.getUserTypeEnum()== UserType.SYS && user.getRoleIds()!=null && user.getRoleIds().size()>0){
            userMapper.addUserRole(user.getId(),user.getRoleIds());
        }
    }

    /**
     *
     * 随机产生二维码。
     * @param userType
     * @return
     */
    private String makeBarracode(UserType userType) {
        DecimalFormat df = new DecimalFormat("00");
        return
                new StringBuilder(df.format(userType.get()))
                        .append(RandomStringUtils.random(10,false,true))
                        .toString();
    }

    @Override
    public void changeStatus(Long userId, UserType userType,UserStatus status) {
        Preconditions.checkNotNull(userId,"common.id.not.null");
        Preconditions.checkNotNull(status,"common.field.not.null");
        Preconditions.checkNotNull(userType,"common.field.not.null");

        User user = new User();
        user.setId(userId);
        user.setUserStatusEnum(status);
        user.setUserType(userType);
        user.setCreateBy(AppCache.getCurrentUser().getLoginName());
        userMapper.update(user);
    }

    @Override
    public void updatePassword(Long userId,UserType userType, String newPwd, String oldPwd) {
        Preconditions.checkNotNull(userId,"common.id.not.null");
        Preconditions.checkNotNull(newPwd,"common.field.not.null");
        Preconditions.checkNotNull(oldPwd,"common.field.not.null");
        Preconditions.checkNotNull(userType,"common.field.not.null");

        Preconditions.checkArgument(newPwd.equals(oldPwd),"user.newPwd.oldPwd.not.equals");

        updatePassword(userId,userType,newPwd);
    }

    @Override
    public void updatePassword(Long userId,UserType userType, String newPwd) {
        Preconditions.checkNotNull(userId,"common.id.not.null");
        Preconditions.checkNotNull(newPwd,"common.field.not.null");
        Preconditions.checkArgument(newPwd.length() >= 6 && newPwd.length() <= 15,"user.pwd.check.length");
        User oleUser = userMapper.findUserById(userId);

        User user = new User();
        user.setId(userId);
        user.setUserType(userType);
        if(AppCache.getCurrentUser()!=null){
            user.setCreateBy(AppCache.getCurrentUser().getLoginName());
        }
        user.setPassword(digestPassword(newPwd,oleUser.getPasswordSalt()));
        userMapper.update(user);
    }

    @Override
    public void updatePassword(String oldPwd,String newPwd) {
        Preconditions.checkNotNull(newPwd,"common.field.not.null");
        User user = AppCache.getCurrentUser();
        user = userMapper.findUserById(user.getId());

        String encodeOldPwd = digestPassword(oldPwd,user.getPasswordSalt());
        Preconditions.checkArgument(encodeOldPwd.equals(user.getPassword()),"user.old.password.not.equil");

        updatePassword(user.getId(),user.getUserTypeEnum(), newPwd);
    }

    @Override
    public void updateUserRole(Long userId,UserType userType, List<String> roleIds){
        Preconditions.checkNotNull(userId,"common.id.not.null");
        userMapper.deleteUserAllRole(userId);

        if(roleIds!=null && roleIds.size()>0) {
            Set<String> temps = new HashSet<>(roleIds);
            userMapper.addUserRole(userId, temps);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByLoginName(String loginName) {
        return userMapper.getUser(loginName);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long id) {
    	return userMapper.findUserById(id);
    }


    @Override
    public void updateUserStatus(ChildAccountUser childAccountUser) {
        User user = userMapper.findUserById(childAccountUser.getId());
        if(user.getUserStatusEnum() == UserStatus.ENABLED){
            user.setUserStatusEnum(UserStatus.DIS_ENABLED);
            userMapper.update(user);
            return;
        }
        if(user.getUserStatusEnum() == UserStatus.DIS_ENABLED){
            user.setUserStatusEnum(UserStatus.ENABLED);
            userMapper.update(user);
            return;
        }
    }

    /**
     * 加密工具类.
     * @param pwd
     * @param salt
     * @return
     */
    private String digestPassword(String pwd,String salt){
        return Md5Util.digest("MD5",pwd.getBytes(),salt.getBytes(),3);
    }

	@Override
	@Transactional(rollbackFor= Exception.class)
	public void updateBalance(Long userId, UserType userType,AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark) {
		Preconditions.checkNotNull(userId,"common.id.not.null");
        Preconditions.checkNotNull(userType,"common.field.not.null");
        Preconditions.checkNotNull(balance,"common.field.not.null");
        
        User user = findUserById(userId);
        
        if (AccountRecordType.OUT == accountRecordType) {
        	balance = balance.negate();
        }
        accountRecordService.add(user, accountRecordType,changeType,balance, remark);
        userMapper.updateBalance(userId, balance);
	}
	
	@Override
	@Transactional(rollbackFor= Exception.class)
	public void modifyBalance(Long userId, UserType userType,AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType,BigDecimal balance, String remark) {
		Preconditions.checkNotNull(userId,"common.id.not.null");
        Preconditions.checkNotNull(userType,"common.field.not.null");
        Preconditions.checkNotNull(balance,"common.field.not.null");
        
        User user = findUserById(userId);
        
        if (AccountRecordType.OUT == accountRecordType) {
        	balance = balance.negate();
        }
        accountRecordService.add(user, accountRecordType,changeType,balance, remark);
        userMapper.modifyBalance(userId, balance);
	}
}
