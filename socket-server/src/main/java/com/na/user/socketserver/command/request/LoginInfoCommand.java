package com.na.user.socketserver.command.request;

import java.net.InetSocketAddress;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.common.util.concurrent.RateLimiter;
import com.na.baccarat.socketserver.common.enums.DeviceTypeEnum;
import com.na.baccarat.socketserver.entity.LoginStatus;
import com.na.remote.IPlatformUserRemote;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.flow.UserClassHandler;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.LoginInfoPara;
import com.na.user.socketserver.command.sendpara.LoginInfoResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserStatusEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.filter.CheckerIPFilter;
import com.na.user.socketserver.service.IUserService;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.log.LoginLogVO;
import com.na.user.socketserver.util.log.LoginStatusEnum;

/**
 * 用户登录
 */
@Cmd(paraCls = LoginInfoPara.class,name = "登录指令（包括荷官、客户端）")
@Component
public class LoginInfoCommand implements ICommand {
	private Logger log = LoggerFactory.getLogger(LoginInfoCommand.class);
	private Logger userLog = LoggerFactory.getLogger("user.login");
	//限速器。
	private RateLimiter rateLimiter;
	@Value("${na.limiter.login}")
	private double limiterRate;
	
	@Autowired
	private SocketIOServer socketIOServer;

	@Autowired
	private IUserService userService;
    
    @Autowired
    private CheckerIPFilter checkerIPFilter;
    
    @Autowired
    private UserClassHandler userClassHandler;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
	private IPlatformUserRemote platformUserRemote;

    @PostConstruct
    public void init(){
    	this.rateLimiter = RateLimiter.create(this.limiterRate);
	}

	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		return exe(client,(LoginInfoPara) commandReqestPara);
	}

	private boolean exe(SocketIOClient client, LoginInfoPara loginInfoPara) {
    	if(loginInfoPara.getUserType()==null || loginInfoPara.getUserType()== UserTypeEnum.REAL.get()) {
			SocketException.isTrue(!rateLimiter.tryAcquire(),"server.is.busy");
		}

		client.set(SocketClientStoreEnum.LANG.get(), loginInfoPara.getLanguage());
		// 判断用户来源,非正常来源返回错误信息
		if (loginInfoPara.getDeviceType()==null ||  DeviceTypeEnum.get(loginInfoPara.getDeviceType()) == null) {
			throw SocketException.createError("login.user.source.error");
		}
		if(!checkerIPFilter.doFilter(client, loginInfoPara.getDeviceTypeEnum())) {
			throw SocketException.createError("ip.cannot.login");
		}
		if(loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.ANDROID ||
				loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.IOS ||
				loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.PC) {
			if(loginInfoPara.getDeviceNumber() == null) {
				throw SocketException.createError("param.not.allow.empty");
			}
		}

		// 执行登录，三种登录方式（账号密码,二维码,加密Token）
		UserPO user = null;
		if(!StringUtils.isEmpty(loginInfoPara.getToken())) {
			if(StringUtils.isEmpty(loginInfoPara.getUserId())) {
				throw SocketException.createError("param.not.allow.empty");
			}
			
			JSONObject token = (JSONObject) redisTemplate.opsForValue().get(Constant.PLATFORM_GAME_USER_TOKEN + loginInfoPara.getUserId());
			if(token == null) {
				log.info("用户[{}]通过设备[{}]登录，redis中无该token[{}]。",loginInfoPara.getUserId(),loginInfoPara.getDeviceNumber(),loginInfoPara.getToken());
				throw SocketException.createError("userInfo.valiedate.failure");
			}
			
			if(token.get("authcode").equals(loginInfoPara.getToken())) {
				Integer id = (Integer) token.get("liveUserId");
				user = userService.login(Long.valueOf(id));
			} else {
				log.info("用户[{}]登录，token验证失败。",loginInfoPara.getUserId());
				throw SocketException.createError("userInfo.valiedate.failure");
			}
			
		} else if (StringUtils.isEmpty(loginInfoPara.getBarcode())) {
			SocketException.isNull(loginInfoPara.getLoginName(),"login.params.loginName.null");
			SocketException.isNull(loginInfoPara.getPwd(),"login.params.password.null");
			user = userService.login(loginInfoPara.getLoginName(), loginInfoPara.getPwd(), loginInfoPara.getUserType());
		} else {
			//二维码登陆
			user = userService.login(loginInfoPara.getBarcode());
		}

		SocketException.isNull(user, "login.params.loginNameorPassword.error");

		LoginLogVO loginLogVO = new LoginLogVO();
		try {
			checkUserStatus(user);

			//写登录日志
			LoginStatus newLoginStatusPara = new LoginStatus();
			newLoginStatusPara.setLoginType(user.getUserType());
			newLoginStatusPara.setUid(user.getId());
			
			if(loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.ANDROID ||
					loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.IOS) {
				/**
				 * 1.先检测手机用户是否在线
				 * 2.若在线则检测设备号是否一致 若不在线则添加在线记录
				 * 3.若一致则无需校验登陆状态 直接登陆  若不一致则提示已登录
				 */
				UserPO userPO = AppCache.getLoginUser(user.getId());
				if(userPO != null) {
					UserPO disuserPO = AppCache.getDisConnectUser(user.getId());
					if(disuserPO == null) {
						if(StringUtils.isEmpty(userPO.getDeviceNumber())) {
							userPO.setDeviceNumber(loginInfoPara.getDeviceNumber());
						} else {
							if(userPO.getDeviceNumber().equals(loginInfoPara.getDeviceNumber())) {
								SocketIOClient oldClient = SocketUtil.getClientByUser(socketIOServer, userPO);
								if(oldClient != null && oldClient.isChannelOpen()) {
									oldClient.disconnect();
								}
							} else {
								log.info("[{}]设备码变化，用户已登录",userPO.getLoginName());
								throw SocketException.createError("user.already.login");
							}
						}
					} else {
						userPO.setDeviceNumber(loginInfoPara.getDeviceNumber());
					}
				} else {
					user.setDeviceNumber(loginInfoPara.getDeviceNumber());
				}
				
				LoginStatus oldLoginStatus = userService.getLoginStatus(newLoginStatusPara);
				if (oldLoginStatus == null) {
					userService.addLoginStatus(newLoginStatusPara);
				}
			} else if(loginInfoPara.getDeviceTypeEnum() == DeviceTypeEnum.PC) {

				UserPO userPO = AppCache.getLoginUser(user.getId());
				if(userPO != null) {
					UserPO disuserPO = AppCache.getDisConnectUser(user.getId());
					if(disuserPO != null) {
						if(StringUtils.isEmpty(disuserPO.getDeviceNumber())) {
							disuserPO.setDeviceNumber(loginInfoPara.getDeviceNumber());
						} else if(disuserPO.getDeviceNumber().equals(loginInfoPara.getDeviceNumber())) {
							SocketIOClient oldClient = SocketUtil.getClientByUser(socketIOServer, disuserPO);
							if(oldClient != null && oldClient.isChannelOpen()) {
								oldClient.disconnect();
							}
						}
					} else {
						if(userPO.isInTable()) {
							throw SocketException.createError("user.already.login");
						}
					}
				} else {
					userService.addLoginStatus(newLoginStatusPara);
				}
			} else {
				LoginStatus oldLoginStatus = userService.getLoginStatus(newLoginStatusPara);
				if (oldLoginStatus != null) {
					log.info("[{}]LoginStatus不为空，用户已登录",loginInfoPara.getLoginName());
					throw SocketException.createError("user.already.login");
				} else {
					if(AppCache.getLoginUser(user.getId()) == null ||
						AppCache.getDisConnectClientMap(user.getId()) != null) {
						userService.addLoginStatus(newLoginStatusPara);
					} else {
						log.info("[{}]存在缓存中，用户已登录",loginInfoPara.getLoginName());
						throw SocketException.createError("user.already.login");
					}
				}
			}
			
			user.setLoginId(newLoginStatusPara.getId());
			user.setDeviceType(loginInfoPara.getDeviceType());
			user.setDeviceInfo(loginInfoPara.getDeviceInfo());

			// 必须设置 client的游戏信息
			client.set(SocketClientStoreEnum.USER_ID.get(),user.getId()+"");

			//保存用户客户端信息
			AppCache.addLoginUser(user);

			LoginInfoResponse loginInfoResponse = new LoginInfoResponse();

			//荷官 荷官主管 电视用户  投注员登录 不做特殊处理
			userClassHandler.user(user).login(loginInfoPara, client, loginInfoResponse);
			
			loginLogVO.setLoginStatus(LoginStatusEnum.SUCCESS.get());
		}catch (Exception e){
			loginLogVO.setLoginStatus(LoginStatusEnum.FAIL.get());
			loginLogVO.setRemark(e.getMessage());
			throw e;
		}finally {
			loginLogVO.setLoginName(user.getLoginName());
			loginLogVO.setNickName(user.getNickName());
			
			InetSocketAddress insocket = (InetSocketAddress) client.getRemoteAddress();
			String clientIP = insocket.getAddress().getHostAddress();
			loginLogVO.setIpAddr(clientIP);
			loginLogVO.setLoginDate(new Date());
			loginLogVO.setUserType(user.getUserType());
			loginLogVO.setDeviceInfo(loginInfoPara.getDeviceInfo());
			loginLogVO.setDeviceType(loginInfoPara.getDeviceType());
			if(user instanceof LiveUserPO){
				LiveUserPO temp = (LiveUserPO)user;
				loginLogVO.setParentPath(temp.getParentPath());
				loginLogVO.setType(temp.getType());
			}

			userLog.info(JSONObject.toJSONString(loginLogVO));
		}
		
		UserPO leaveSeatUser = AppCache.getDisConnectUser(user.getId());
		if (leaveSeatUser!=null) {
			userClassHandler.user(user).reconnect(client);
		}
		try {
			platformUserRemote.login(user.getId());
		} catch (Exception e) {
			log.error("远程调用异常", e);
		}
		
		return true;
	}

	private void checkUserStatus(UserPO user) {
		if (user.getUserStatus() != 1) {
			String msg = null;
			// 1 启用 2 停用 3 锁定
			if (user.getUserStatusEnum() == UserStatusEnum.LOCKED) {
				msg = "user.status.lock";
			} else if (user.getUserStatusEnum() == UserStatusEnum.FREEZE) {
				msg = "user.status.frozen";
			} else {
				msg = "user.status.delete";
			}
			throw SocketException.createError(msg);
		}
	}
	

}
