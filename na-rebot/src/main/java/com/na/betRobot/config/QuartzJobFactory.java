package com.na.betRobot.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
	
	private transient AutowireCapableBeanFactory beanFactory;

    @Override  
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {  
        //调用父类的方法 
    	final Object jobInstance = super.createJobInstance(bundle);
        //进行注入  
        beanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
			throws BeansException {
		beanFactory = applicationContext.getAutowireCapableBeanFactory();
	}
	
}
