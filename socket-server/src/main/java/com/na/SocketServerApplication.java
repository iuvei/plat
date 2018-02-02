package com.na;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.fastjson.parser.ParserConfig;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.na.baccarat.socketserver.entity.Play;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.socketio.MySocketIOChannelInitializer;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.config.QuartzConfig;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.entity.GamePO;
import com.na.user.socketserver.listener.DefaultListeners;
import com.na.user.socketserver.listener.redis.RedisEventListener;
import com.na.user.socketserver.listener.redis.UserAnnounceListener;
import com.na.user.socketserver.listener.redis.UserBalanceListener;
import com.na.user.socketserver.service.IGameService;
import com.na.user.socketserver.service.IGameTableService;
import com.na.user.socketserver.service.II18NService;
import com.na.user.socketserver.service.IIpBlackWhiteService;
import com.na.user.socketserver.service.ILoginStatusService;
import com.na.user.socketserver.service.IPlayService;
import com.na.user.socketserver.task.ChangeKeyMoitorTask;
import com.na.user.socketserver.task.ClearUesrDataTask;
import com.na.user.socketserver.task.PushTabelsStatusTask;
import com.na.user.socketserver.task.RefreshBlackWhiteIpTask;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SocketServerApplication implements ApplicationListener {
	
	private final static Logger log = LoggerFactory.getLogger(SocketServerApplication.class);
	
	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private SocketIoConfig socketIoConfig;
	@Autowired
	private DefaultListeners socketIoListeners;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameTableService gameTableService;
	@Autowired
	private IPlayService playService;
	@Qualifier("schedulerFactoryBean")
	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	
	@Qualifier("schedulerFactoryBean2")
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean2;
	@Autowired  
	private SpringContextUtil springContextUtil;

	@Autowired
	private UserBalanceListener userBalanceListener;
	
	@Autowired
	private UserAnnounceListener userAnnounceListener;
	
	@Autowired
	private RedisMessageListenerContainer redisMessageListenerContainer;
	
	@Autowired
	private RedisEventListener redisEventListener;
	
	@Autowired
	private ILoginStatusService loginStatusService;
	
	@Autowired
	private II18NService i18NService;
	
	@Autowired
	private IIpBlackWhiteService ipBlackWhiteService;
	@Resource(name="AccountRecordTopic")
	private Topic accountRecordTopic;
	
	public static void main(String[] args) {
		SpringApplication.run(SocketServerApplication.class, args);
	}

	/**
	 * 容器加载完成时间。<br>
	 *     注意里面的判断用于去除重复加载。
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
			if(applicationContext.equals(appContext))
				init();
		}
	}
	
	/**
	 * 初始化主函数
	 */
	private void init(){
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		
		log.info("开始初始化服务器");
		//读取游戏数据
		initGames();
		//启动定时器
		initTask();
		//Redis订阅
		initRedisTopic();
		log.info("初始化服务器完毕");
	}

	@Bean
	public SocketIOServer socketIOServer() throws Exception {
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setTcpNoDelay(true);
		socketConfig.setTcpSendBufferSize(socketIoConfig.getTcpSendBufferSize());
		socketConfig.setTcpReceiveBufferSize(socketIoConfig.getTcpReceiveBufferSize());
		socketConfig.setAcceptBackLog(socketConfig.getAcceptBackLog());
		socketConfig.setReuseAddress(true);
		
		Configuration gameConfig = new Configuration();
		gameConfig.setAllowCustomRequests(true);
		gameConfig.setPort(socketIoConfig.getServerPort());
		gameConfig.setMaxHttpContentLength(socketIoConfig.getMaxHttpContentLength());
		gameConfig.setWorkerThreads(socketIoConfig.getWorkerThreads());
		gameConfig.setBossThreads(socketIoConfig.getBossThreads());
		gameConfig.setHeartbeatTimeout(socketIoConfig.getHeartbeatTimeout());
		gameConfig.setHeartbeatInterval(socketIoConfig.getHeartbeatInterval());
		gameConfig.setCloseTimeout(socketIoConfig.getServerCloseTimeout());
		gameConfig.setTransports(Transport.WEBSOCKET,Transport.FLASHSOCKET);
		gameConfig.setOrigin(socketIoConfig.getOrigin());
		gameConfig.setSocketConfig(socketConfig);

		final SocketIOServer socketIOServer = new SocketIOServer(gameConfig);
		socketIOServer.setPipelineFactory(new MySocketIOChannelInitializer(socketIoConfig.getCrossDomainPolicy()));

		// 指定缺省的监听器
		socketIOServer.addListeners(socketIoListeners);
		socketIOServer.start();
		
        log.info("socketio 初始化成功【{}】。",gameConfig.getPort());
        return socketIOServer;
	}

	@Bean("betOrderSnowflakeIdWorker")
	public SnowflakeIdWorker betOrderSnowflakeIdWorker(){
		return new SnowflakeIdWorker(socketIoConfig.getWorkerId(),socketIoConfig.getDatacenterId());
	}

	@Bean("accountRecordSnowflakeIdWorker")
	public SnowflakeIdWorker accountRecordSnowflakeIdWorker(){
		return new SnowflakeIdWorker(socketIoConfig.getWorkerId(),socketIoConfig.getDatacenterId());
	}
	
	/**
	 * 加载游戏
	 */
	private void initGames() {
		//初始化登陆表
		loginStatusService.deleteAll();
		//初始化黑白名单列表
		AppCache.initBlackWhiteIpMap(ipBlackWhiteService.findAll());
		//初始化国家化表
		AppCache.setI18NMap(i18NService.getAllMap());
		//加载所有游戏
		List<GamePO> gameList = gameService.getAllGame();
		AppCache.initGame(gameList);
		//加载所有玩法
		List<Play> playList = playService.getAll();
		AppCache.setPlayList(playList);
		
		String gamesConfig = socketIoConfig.getGames();
		String[] games = org.apache.commons.lang3.StringUtils.split(gamesConfig, ",");
		for (String s : games) {
			AutoGame game = (AutoGame) springContextUtil.getBean( s + "AutoGame");
			game.init();
		}
		
	}
	
	/**
	 * 初始化定时器
	 */
	private void initTask() {
		log.info("启动秘钥监听任务");
		//修改秘钥定时任务
		QuartzConfig.addJob(schedulerFactoryBean2, ChangeKeyMoitorTask.class, "ChangeKeyMoitor", "*/5 * * * * ?", null);
		//推送桌子消息定时任务
		QuartzConfig.addJob(schedulerFactoryBean2, PushTabelsStatusTask.class, "PushTabelsStatusTask", "*/2 * * * * ?", null);
		//清除离线用户数据
		QuartzConfig.addJob(schedulerFactoryBean, ClearUesrDataTask.class, "ClearUserDataTask", "0 */1 * * * ?", null);
		//定时刷新黑白名单列表(每小时一次)
		QuartzConfig.addJob(schedulerFactoryBean, RefreshBlackWhiteIpTask.class, "refreshBlackWhiteIpTask", "0 0 * * * ?", null);
	}
	
	/**
	 * Redis订阅
	 */
	private void initRedisTopic() {
		log.info("启动Redis订阅服务");
		redisMessageListenerContainer.addMessageListener(messageListener(redisEventListener), new ChannelTopic("GameRedisEvent"));
	}
	
    MessageListenerAdapter messageListener(Object listener) {
		MessageListenerAdapter adapter = new MessageListenerAdapter( listener );
        return adapter;
    }

	@Bean("AccountRecordTopic")
	public Topic topic() {
		return new ActiveMQTopic("account.record");
	}
	
	@Bean("BetRecordTopicTopic")
	public Topic betRecordTopic() {
		return new ActiveMQTopic("bet.record");
	}

}
