package com.na.user.socketserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.entity.LoginStatus;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.common.event.UserBalanceChangeEvent;
import com.na.user.socketserver.dao.ILoginStatusMapper;
import com.na.user.socketserver.dao.IUserMapper;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.Md5Util;

import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Created by sunny on 2017/4/27 0027.
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUserMapper userMapper;
	@Autowired
	private ILoginStatusMapper loginStatusMapper;

    @Autowired
    private ApplicationContext applicationContext;
    
    private ExecutorService batchUpdateBalanceThreadPool;
    
    @PostConstruct
	public void init(){
    	batchUpdateBalanceThreadPool = Executors.newFixedThreadPool(2,new DefaultThreadFactory("batchUpdateBalanceThread"));
	}

    @Override
    public UserPO login(String userName, String password, Integer userType) {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
        	throw SocketException.createError("login.params.loginNameorPassword.error");
        }
        
        UserPO userPO = null;
        if(userType == null) {
        	userPO = userMapper.getLiveUserByLoginName(userName);
        } else {
        	userPO = userMapper.getDealerUserByLoginName(userName);
        }
        
        if(userPO != null) {
        	String md5Pwd = Md5Util.digest("MD5", password.getBytes(), userPO.getPasswordSalt().getBytes(), 3);
        	if(md5Pwd.equals(userPO.getPassword())) {
        		return userPO;
        	} else {
        		throw SocketException.createError("login.params.loginNameorPassword.error");
        	}
		} else {
			throw SocketException.createError("login.params.loginNameorPassword.error");
		}
    }

    @Override
    public LiveUserPO getUserById(Long userId) {
        if (userId==null) return null;

        LiveUserPO user = userMapper.getLiveUserById(userId);
        return user;
    }

    @Override
    public UserPO login(String barcode) {
        if (StringUtils.isBlank(barcode)) return null;

        UserPO user = userMapper.getUserByBarcode(barcode);
        if(user == null) {
        	throw SocketException.createError("not.find.user.data");
        }
        if(UserTypeEnum.REAL == user.getUserTypeEnum()) {
    		user = userMapper.getLiveUserById(user.getId());
    	} else if (UserTypeEnum.DEALER == user.getUserTypeEnum()) {
    		user = userMapper.getDealerUserById(user.getId());
    	}
		return user;
    }

    @Override
    public LoginStatus getLoginStatus(LoginStatus loginStatus) {
        return loginStatusMapper.getLoginStatus(loginStatus);
    }

    @Override
    public void addLoginStatus(LoginStatus loginStatus) {
        loginStatusMapper.add(loginStatus);
    }

	@Override
	public List<UserChipsPO> getUserChips(String cids) {
		if(cids == null) {
			throw SocketException.createError("数据错误");
		}
        String[] idsStr = cids.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String s : idsStr) {
        	if(!org.springframework.util.StringUtils.isEmpty(s)) {
        		ids.add(Integer.valueOf(s));
        	}
        }
        
        List<UserChipsPO> userChipsList = userMapper.findByUserChipsByIds(ids);
        
        Collections.sort(userChipsList,new Comparator<UserChipsPO>() {
        	public int compare(UserChipsPO o1, UserChipsPO o2) {
        		if (o1.getMin().compareTo(o2.getMin()) > 0) {
        			return 0;
        		}
        		return 0;
        	};
		});
        return userChipsList;
	}

    @Override
    public void logout(UserPO user) {
        loginStatusMapper.deleteByUid(user.getId(), user.getUserType());
    }

    @Override
    public void updateUserBalance(Long uid, BigDecimal betAmount, String affect) {
        userMapper.updateUserBalance(uid,betAmount);
        publishUserBalanceEvent(uid, betAmount, affect);
    }

    /**
     * 余额变动事件。
     * @param uid
     * @param betAmount
     */
    private void publishUserBalanceEvent(Long uid, BigDecimal betAmount, String affect) {
        try {
            UserPO loginUser = AppCache.getLoginUser(uid);
            if(loginUser!=null) {
            	Map map = new HashMap<>();
            	map.put("user", loginUser);
            	map.put("affect", affect);
                applicationContext.publishEvent(new UserBalanceChangeEvent(map));
            }
        }catch (Exception e){
            logger.warn(String.format("用户余额变动通知失败：%d -> %s",uid,betAmount==null ? "---" : betAmount.toString()));
        }
    }
    
    @Override
	public void batchUpdateBalance(List<UserPO> userPOList, String affect) {
    	final CountDownLatch countDownLatch = new CountDownLatch(2);
    	
    	batchUpdateBalanceThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					userMapper.batchUpdateBalance(userPOList);
				} catch(Throwable t) {
					logger.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
				}
			}
		});
    	batchUpdateBalanceThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					userMapper.batchUpdateWinMoney(userPOList);
				} catch(Throwable t) {
					logger.error(t.getMessage(),t);
				} finally {
					countDownLatch.countDown();
				}
			}
		});
		
    	try {
			countDownLatch.await();
			for(UserPO userPO: userPOList) {
	            publishUserBalanceEvent(userPO.getId(),null, affect);
			}
		} catch (InterruptedException e) {
			throw SocketException.createError("settlement.abnormal");
		}
	}

	@Override
	public List<LiveUserPO> getLowerLevelByName(String loginName) {
		return userMapper.getLowerLevelByName(loginName);
	}
	
	@Override
    public UserPO getUserByLoginName(String userName) {
        if (userName==null) return null;

        return userMapper.getUserByLoginName(userName);
    }
	
	@Override
	public LiveUserPO getLiveUserByLoginName(String userName) {
		if (userName==null) return null;
		
		return userMapper.getLiveUserByLoginName(userName);
	}

	@Override
	public List<Long> listUserIdByParentPath(String[] userIds) {
		return userMapper.listUserIdByParentPath(userIds);
	}

	@Override
	public List<Long> listUserIdBySuperiorID(String[] userIds) {
		return userMapper.listUserIdBySuperiorID(userIds);
	}

	@Override
	public void demoUpdate(UserPO user) {
		userMapper.demoUpdate(user);
	}

	@Override
	public void modifyUserInfo(UserPO userPO) {
		userMapper.modifyUserInfo(userPO);
	}

	@Override
	public UserPO login(Long userId) {
		if (userId == null) return null;
		
        UserPO user = userMapper.getUserById(userId);
        if(UserTypeEnum.REAL == user.getUserTypeEnum()) {
    		user = userMapper.getLiveUserById(user.getId());
    	} else if (UserTypeEnum.DEALER == user.getUserTypeEnum()) {
    		user = userMapper.getDealerUserById(user.getId());
    	}
		return user;
	}

	@Override
	public void updateStatus(UserPO userPO) {
		userMapper.updateStatus(userPO);
	}

}
