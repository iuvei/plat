package com.na.gate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.na.gate.cache.GateCache;
import com.na.gate.common.QuartzConfig;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.gate.task.SenderAccountRecordTask;
import com.na.gate.task.SenderBetOrderTask;

@SpringBootApplication
public class Application implements ApplicationListener{
	private Logger logger = LoggerFactory.getLogger(Application.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private ApplicationContext appContext;
    
	@Value("${spring.na.platform.sender.betOrder.cron}")
	private String sendBetOrderCron;
	
	@Autowired
	private IPlatformUserAdapterService platformUserAdapterService;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		if(arg0 instanceof ContextRefreshedEvent){
			ApplicationContext applicationContext = ((ContextRefreshedEvent) arg0).getApplicationContext();
			if (applicationContext.equals(this.appContext)){
				initTask();
				initPlatformUserAdapterMap();
			}
		}
	}
	
	@Configuration
	public class BeanConfig {
		@Bean
		public HttpMessageConverters fastJsonHttpMessageConverters() {
			FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
					SerializerFeature.NotWriteRootClassName, SerializerFeature.WriteMapNullValue);
			fastConvert.setFastJsonConfig(fastJsonConfig);
			ValueFilter valueFilter = new ValueFilter() {
				public Object process(Object o, String s, Object o1) {
					return null == o1 ? "" : o1;
				}
			};
			fastJsonConfig.setSerializeFilters(valueFilter);
			return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
		}
	}
	
	private void initTask() {
		QuartzConfig.addJob(schedulerFactoryBean, SenderBetOrderTask.class, "senderBetOrderTask",
				sendBetOrderCron,null);
		QuartzConfig.addJob(schedulerFactoryBean, SenderAccountRecordTask.class, "senderAccountRecordTask",
				"0 0/3 * * * ?",null);
	}
	
	private void initPlatformUserAdapterMap(){
		List<PlatformUserAdapter> list =platformUserAdapterService.findAll();
		list.forEach(item->{
			GateCache.PLATFORM_USER_ADAPTER_MAP.put(item.getLiveUserId(), item);
		});
		logger.info("玩家用户缓存完成。");
	}
	
}
