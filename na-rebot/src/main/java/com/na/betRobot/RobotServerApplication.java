package com.na.betRobot;


import com.na.betRobot.service.ILiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

@SpringBootApplication
@Configuration
public class RobotServerApplication implements ApplicationListener {
	
	private final static Logger log = LoggerFactory.getLogger(RobotServerApplication.class);
	
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private ILiveUserService liveUserService;

	public static void main(String[] args) {
		SpringApplication.run(RobotServerApplication.class, args);
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
			if(applicationContext.equals(applicationContext))
				init();
		}
	}

	/**
	 * 初始化主函数
	 */
	private void init(){
		log.info("初始化机器人完毕");
	}

	@Bean
	@Scope("prototype")
	public ServerClient serverClient() {
		ServerClient client = new ServerClient();
		return client;
	}

}
