package com.na.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.Topic;
import javax.servlet.MultipartConfigElement;

import org.I0Itec.zkclient.ZkClient;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.na.manager.aop.CrossFilter;
import com.na.manager.aop.FlowControlAop;
import com.na.manager.cache.AppCache;
import com.na.manager.common.Constant;
import com.na.manager.common.QuartzConfig;
import com.na.manager.common.websocket.MyWebSocket;
import com.na.manager.entity.User;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.mail.MailInfo;
import com.na.manager.mail.SimpleMail;
import com.na.manager.service.IDictService;
import com.na.manager.service.IGameService;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.II18NService;
import com.na.manager.service.IIpBlackWhiteService;
import com.na.manager.service.IPermissionService;
import com.na.manager.service.IRoleService;
import com.na.manager.service.IUserService;
import com.na.manager.task.RefreshBlackWhiteIpTask;
import com.na.manager.task.StatisticsUserDataTask;
import com.na.manager.util.DateUtil;
import com.na.manager.util.SnowflakeIdWorker;
import com.na.manager.util.TokenUtil;
import com.na.monitor.core.NodeData;

@SpringBootApplication
@EnableTransactionManagement
// @EnableDiscoveryClient
public class ManagerApplication implements ApplicationListener {

	private static Logger log = Logger.getLogger(ManagerApplication.class);

	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPermissionService permissionService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private CrossFilter crossFilter;
	@Autowired
	private IGameService gameService;
	@Autowired
	private IGameTableService gameTableService;
	@Autowired
	private II18NService i18NService;
	@Autowired
	private IDictService dictService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private MyWebSocket myWebSocket;
	@Autowired
	private IIpBlackWhiteService ipBlackWhiteService;
	@Autowired
	private RedisMessageListenerContainer redisMessageListenerContainer;
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Value("${server.datacenter.id}")
	private short datacenterId;
	@Value("${server.worker.id}")
	private short workerId;
	@Autowired
	private FlowControlAop flowControlAop;
	@Autowired
	private ZkClient zkClient;
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private String port;
	@Value("${spring.mail.username}")
	private String form;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.to}")
	private String to;
	@Value("${spring.mail.subject}")
	private String subject;
	
	@Resource(name="platMaintenanceTopic")
	private Topic platMaintenanceTopic;
	
	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
			if (applicationContext.equals(this.appContext)){
				init();
			}
		}
	}

	@Bean("accountRecordSnowflakeIdWorker")
	public SnowflakeIdWorker accountRecordSnowflakeIdWorker() {
		return new SnowflakeIdWorker(workerId, datacenterId);
	}

	private void init() {
		AppCache.setI18NMap(i18NService.getAllMap());
		AppCache.initMenu(permissionService.findAllMenu());
		AppCache.initGame(gameService.listGameTypes());
		AppCache.initGameTable(gameTableService.listAllTable());
		AppCache.initDictMap(dictService.findAllDict());
		AppCache.initPermission(permissionService.findAllPermission());
		AppCache.initBlackWhiteIpMap(ipBlackWhiteService.findAll());
		initTask();
		initTryAgent();
		subReidsForceExit();
	}

	/**
	 * 订阅强制推出事件。
	 */
	private void subReidsForceExit() {
		MessageListenerAdapter adapter = new MessageListenerAdapter(myWebSocket);
		// adapter.setSerializer(new JdkSerializationRedisSerializer());
		redisMessageListenerContainer.addMessageListener(adapter,
				new ChannelTopic(RedisKeyEnum.EVENT_MANAGE_FORCE_EXIT.get()));
	}

	@Configuration
	public class BeanConfig {
		/* 注入Bean : HttpMessageConverters，以支持fastjson */
		@Bean
		public HttpMessageConverters fastJsonHttpMessageConverters() {
			FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
			fastConvert.setFastJsonConfig(fastJsonConfig);
			return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
		}

		@Bean
		public FilterRegistrationBean crossFilterRegistration() {
			FilterRegistrationBean registration = new FilterRegistrationBean();
			registration.setFilter(crossFilter);
			registration.addUrlPatterns("/*");
			registration.setName("crossFilter");
			registration.setOrder(1);
			return registration;
		}
	}

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(flowControlAop).addPathPatterns("/*");
			}
		};
		return webMvcConfigurerAdapter;
	}

	@Configuration
	public class WebSocketConfig {
		@Bean
		public ServerEndpointExporter serverEndpointExporter() {
			return new ServerEndpointExporter();
		}
	}

	/**
	 * 文件上传配置
	 * 
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("50MB");
		// 设置总上传数据总大小
		factory.setMaxRequestSize("50MB");
		return factory.createMultipartConfig();
	}
	
	@Bean("platMaintenanceTopic")
	public Topic platMaintenanceTopic() {
		return new ActiveMQTopic("plat.maintenance");
	}

	/**
	 * 添加试玩Token 为了游戏后台创建试玩用户使用
	 */
	@SuppressWarnings("unchecked")
	private void initTryAgent() {
		User demoAgentUser = userService.findUserByLoginName(Constant.CREDITSHIWAN);
		if (demoAgentUser != null) {
			demoAgentUser.setRoles(roleService.getRoleByUserId(demoAgentUser.getId()));
			demoAgentUser.setPermissions(permissionService.findPermissionByUserId(demoAgentUser.getId()));
			demoAgentUser.getPermissions().forEach(item -> {
				demoAgentUser.addMenu(AppCache.getMenuBy(item.getGroupID()));
			});
			demoAgentUser.setToken(TokenUtil.createToken(demoAgentUser.getId()));
			stringRedisTemplate.opsForValue().set(RedisKeyEnum.TRY_USER_LOGIN_TOKEN.get(), demoAgentUser.getToken());
			redisTemplate.boundValueOps(RedisKeyEnum.USER_LOGIN_TOEKN.get(demoAgentUser.getToken())).set(demoAgentUser);
		} else {
			log.error("无法找到试玩代理");
		}
	}

	/**
	 * 初始化IP黑白名单列表
	 */
	// private void initIpBlackWhiteList(List<IpBlackWhiteAddr> ipList) {
	// Set<String> set = new HashSet<>();
	// if(set!=null && set.size()>0) {
	// ipList.forEach(item -> {
	// set.add(StringUtils.join(new
	// Object[]{item.getIpType(),item.getPlatType(),item.getStartNum(),item.getEndNum()},"."));
	// });
	// BoundSetOperations<String, String> ops =
	// stringRedisTemplate.boundSetOps(RedisKeyEnum.IP_BLACK_WHITE_LIST.get());
	// ops.expire(RedisKeyEnum.IP_BLACK_WHITE_LIST.getTtl(), TimeUnit.SECONDS);
	// ops.add(set.toArray(new String[]{}));
	// }
	// }

	/**
	 * 初始化定时器
	 */
	private void initTask() {
		log.info("启动黑白名单定时刷新任务");
		// 定时刷新黑白名单列表(每小时一次)
		QuartzConfig.addJob(schedulerFactoryBean, RefreshBlackWhiteIpTask.class, "refreshBlackWhiteIpTask", "0 0 0/1 * * ?", null);
		// 定时统计数据
		QuartzConfig.addJob(schedulerFactoryBean, StatisticsUserDataTask.class, "statisticsUserDataTask", "0 0 0/1 * * ?", null);
		// QuartzConfig.addJob(schedulerFactoryBean,RefreshBlackWhiteIpTask.class, "refreshBlackWhiteIpTask", "0 0/10 * * * ?", null);
	}
	
	private void initServerList(){
		List<NodeData> dataList = new ArrayList<>();
		if (zkClient.exists("/app")) {
			List<String> urls = zkClient.getChildren("/app");
			urls.forEach(url -> {
				NodeData data = zkClient.readData("/app/" + url);
				dataList.add(data);
			});
		}
		AppCache.initServerListMap(dataList);
		
		
	}

	@PostConstruct
	public void zookeeperListener() {
		initServerList();
		// 邮件发送开关 1:发送  0:不发送
		if(!stringRedisTemplate.hasKey(RedisKeyEnum.EMAIL_NOTICE_FLAG.get())){
			stringRedisTemplate.opsForValue().set(RedisKeyEnum.EMAIL_NOTICE_FLAG.get(), "1");
		}
		zkClient.subscribeChildChanges("/app", ((parentPath, currentChilds) -> {
			boolean flag =false;
			if("1".equals(stringRedisTemplate.opsForValue().get(RedisKeyEnum.EMAIL_NOTICE_FLAG.get()))){
				List<NodeData> dataList = new ArrayList<>();
				if ("/app".equals(parentPath) && currentChilds != null) {
					List<String> urls = zkClient.getChildren("/app");
					for(String url:urls){
						NodeData data = zkClient.readData("/app/" + url);
						dataList.add(data);
						if(!AppCache.getServerListMap().containsKey(data.getServerAddress()+":"+data.getServerPort())){
							flag =true;
						}
					}
				}
				if(flag){
					MailInfo mailInfo = new MailInfo();  
			        mailInfo.setMailServerHost(host);  
			        mailInfo.setMailServerPort(port);  
			        mailInfo.setValidate(true);  
			        mailInfo.setUsername(form);  
			        mailInfo.setPassword(password);
			        mailInfo.setFromAddress(form);  
			        mailInfo.setToAddress(to);  
			        mailInfo.setSubject(DateUtil.string(new Date(), "yyyy-MM-dd HH:mm:ss")+" "+subject);
			        mailInfo.setContent(SimpleMail.buildHtml(dataList));
			        SimpleMail.sendHtmlMail(mailInfo);// 发送html格式 
			        initServerList(); //刷新缓存数据
			        log.info("邮件发送成功...");
				}
			}
		}));
	}
}
