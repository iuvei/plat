package com.na.user.socketserver.listener;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.annotation.OnJsonObject;
import com.corundumstudio.socketio.annotation.OnMessage;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.I18nMessage;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.SocketUtil;
import com.na.user.socketserver.util.TimeLeft;

import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 缺省的 websocket 监听器,不论用户连接哪个 Namespace 此监听器都能监听到
 */
@Component
public class DefaultListeners {
    private static Logger log = LoggerFactory.getLogger(DefaultListeners.class);

	@Autowired
	private ConnectListener connectListener;

	@Autowired
	private MessageSource messageSource;
	
	@Qualifier("schedulerFactoryBean2")
	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private SocketIoConfig socketIoConfig;
	
	@Autowired
	private I18nMessage i18nMessage;

	/**
	 * 普通会员执行线程。
	 */
	private ExecutorService clientPoolService;

	/**
	 * 荷官执行线程。<br>
	 * 通过IP进行识别。
	 */
	private ExecutorService dealerPoolService;

	@PostConstruct
	public void init(){
		dealerPoolService = Executors.newFixedThreadPool(socketIoConfig.getPoolDealerThreadCorePoolSize(),new DefaultThreadFactory("pool-dealer"));

		clientPoolService = Executors.newFixedThreadPool(socketIoConfig.getPoolClientThreads(),new DefaultThreadFactory("pool-client"));
	}

	/**
	 * 建立连接的时候，触发的时间比较简单了。只能告诉服务器，这个人建立了连接，
	 *
	 * 没有实际效果。只有当进入房间的时候，才有处理
	 *
	 * @param client
	 */
	@OnConnect
	public void onConnectHandler(SocketIOClient client) {
		connectListener.onConnect(client);
	}

	private void dispatcherMsg(SocketIOClient client, Object data) {
		String ip = ((InetSocketAddress)client.getRemoteAddress()).getAddress().getHostAddress();
		Task task = new Task(data,client);
		if(ip==null){
			clientPoolService.submit(task);
		}else if(socketIoConfig.getDealerIps().contains(ip)){
			dealerPoolService.submit(task);
		}else{
			clientPoolService.submit(task);
		}
	}

	private class Task implements Runnable{
		private Object data;
		private SocketIOClient client;
		private TimeLeft timeLeft = new TimeLeft();

		public Task(Object data, SocketIOClient client) {
			this.data = data;
			this.client = client;
		}

		@Override
		public void run() {
			timeLeft.start();
			if(client!=null) {
				MDC.put("sessionId", client == null ? "" : client.getSessionId().toString());
				String userId = client.get(SocketClientStoreEnum.USER_ID.get());
				MDC.put("userId", userId);
				MDC.put("eventId", UUID.randomUUID().toString().replace("-",""));
			}

//			log.debug("【请求】：[{}] -> 内容： [{}]", client.getRemoteAddress().toString(), data);
			if (null == data) {
				return;
			}

			JSONObject params = SocketUtil.connectDetrypt(client, data + "", client.get(Constant.SECRET_KEY), schedulerFactoryBean);

			// 参数验证
			if (params == null || params.isEmpty()) {
				sendException(client, RequestCommandEnum.COMMON_SYSTEM.get(), "param.error", null);
				return;
			}

			// 校验命令是否属于服务器
			String cmd = params.getString("cmd");
			ICommand commandService = (ICommand) SpringContextUtil.getBean(cmd + "Command");
			if (commandService == null) {
				sendException(client, RequestCommandEnum.COMMON_SYSTEM.get(), "commant.invalid", null);
				log.error("该指令还未实现：{}", cmd);
				return;
			}

			try {
				Cmd cmdAnnotation = getCmdAnnotation(commandService);
				if (params.getJSONObject("body") != null) {
					CommandReqestPara reqestPara = (CommandReqestPara) params.getJSONObject("body").toJavaObject(cmdAnnotation.paraCls());
					commandService.exec(client, reqestPara);
				} else {
					commandService.exec(client, null);
				}
			} catch (SocketException se) {
				sendException(client, cmd, se.getMsg(), se.getArgsArrays());
				log.error(se.getMsg()+","+se.getArgs(), se);
			} catch (Throwable e) {
				sendException(client, cmd, ErrorCode.SYSTEM_ERROR.get() + ":system.error", null);
				log.error(e.getMessage(), e);
			} finally {
				if(!"heartBeat".equals(cmd)) {
					log.info("【请求】：[{}] -> 命令[{}],花费时间： [{}]", client.getRemoteAddress().toString(), cmd, timeLeft.end());
				}
			}
		}
	}

	/**
	 * 给客户发送异常消息。
	 * @param client
	 * @param cmd
	 * @param msg
	 */
	private void sendException(SocketIOClient client, String cmd, String msg, Object[] args) {
		if (client == null || !client.isChannelOpen()) {
			return;
		}
		
		CommandResponse exceptionResponse = new CommandResponse();
		exceptionResponse.setType(cmd);
		String localeString = client.get(SocketClientStoreEnum.LANG.get());
		if(localeString == null) {
			localeString = "zh_cn";
		}
		
		String[] msgs = msg.split(":");
		if(msgs.length > 1) {
			msg = msgs[1];
			Map<String, String> body = new HashMap<>();
			body.put("errorCode", msgs[0]);
			exceptionResponse.setBody(body);
		}
		exceptionResponse.setMsg(i18nMessage.getMessage(msg, args, new Locale(localeString.split("_")[0], localeString.split("_")[1])));
		SocketUtil.send(client, ResponseCommandEnum.ERROR, exceptionResponse);
	}

	private Cmd getCmdAnnotation(ICommand commandService) {
		if(commandService.getClass().isAnnotationPresent(Cmd.class)){
			return commandService.getClass().getAnnotation(Cmd.class);
		}else if(commandService.getClass().getSuperclass().isAnnotationPresent(Cmd.class)) {
			return commandService.getClass().getSuperclass().getAnnotation(Cmd.class);
		}
		return null;
	}

	@OnMessage
	public void onSomeEventHandler(SocketIOClient client, String data, AckRequest ackRequest) {
		dispatcherMsg(client, data);
	}

	@OnJsonObject
	public void onSomeEventHandler(SocketIOClient client, Object data, AckRequest ackRequest) {
		dispatcherMsg(client, data);
	}

	/**
	 * 更换秘钥消息事件
	 */
	@OnEvent("changeKey")
	public void changeKeyEventHandler(final SocketIOClient client, Object data, AckRequest ackRequest) {
		clientPoolService.submit(new Runnable() {
			@Override
			public void run() {
				log.info("更换秘钥事件 : {}", data);
				if (null == data) {
					return;
				}
				String testNewKeyStr = client.get(Constant.SECRET_TEST);
				String newSecretKey = client.get(Constant.SECRET_NEW_KEY);

				try {
					if (newSecretKey == null) {
						throw SocketException.createError("connect.error");
					}
					Map<String, Object> params = SocketUtil.connectDetrypt(client, data.toString(), newSecretKey, schedulerFactoryBean);
					if ("success".equals(params.get("msg"))) {
						if (testNewKeyStr.equals(params.get("test"))) {
							//更换秘钥
							client.set(Constant.SECRET_OLD_KEY, client.get(Constant.SECRET_KEY));
							client.set(Constant.SECRET_KEY, newSecretKey);

							log.debug("修改秘钥成功!");
						} else {
							log.error("修改秘钥失败 : test validate fail");
						}
					} else {
						log.error("修改秘钥失败 : msg return fail");
					}
					client.del(Constant.SECRET_TEST);
					client.del(Constant.SECRET_NEW_KEY);
				} catch (SocketException e) {
					sendException(client, "changeKey", e.getMsg(), e.getArgsArrays());
					log.error(e.getMsg(), e);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		});
	}


	/**
	 * 客户端断开连接时处理
	 */
	@OnDisconnect
	public void onDisconnectHandler(SocketIOClient client) {
		clientPoolService.submit(new Runnable() {
			@Override
			public void run() {
				connectListener.onDisConnect(client);
			}
		});
	}



	/**
	 * 订阅服务器监听
	 */
//	private void subscribe() {
//		SysConfig.log.infof("REQ--> %s", "订阅服务器监听-开始");
//		// 此服务器器订阅
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.serverTopicQueue, new ServerMonitor()), SysConfig.config.get("socketserver.id"));
//		// 接收所有广播消息
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.msgTopicQueue, new CommonMessageMonitor()), RedisConstant.PUBLISH_SYSTEM_MESSAGE);
//
//		// 接收刷新用户金额的队列
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.reshAmountTopicQueue, new RefreshAmountMonitor()), RedisConstant.REFRESH_USER_AMOUNT);
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.reshMaxLimitTopicQueue, new RefreshMaxLimitMonitor()), RedisConstant.REFRESH_USER_MAXLIMIT);
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.reshTableInfoTopicQueue, new RefreshTableInfoMonitor()), RedisConstant.UPDATE_GAME_TABLE);
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.reshGameConfigTopicQueue, new RefreshGameConfigMonitor()), RedisConstant.UPDATE_GAME_CONFIG);
//		SysConfig.topic.subscribe(new BaseTopic(SysConfig.getIpInfoTopicQueue, new GetipInfoMonitor()), RedisConstant.GET_IP_INFO);
//		SysConfig.log.infof("REQ--> %s", "订阅服务器监听-结束");
//	}
}
