package com.gameportal.web.mail.task;

import org.apache.log4j.Logger;


/**
 * 邮件发送工厂类
 * @author Administrator
 *
 */
public class ExecutorFactoryEmail {
	
	public static final Logger logger = Logger.getLogger(ExecutorFactoryEmail.class);
	
	public void run(){
		CollectEmail cemail = new CollectEmail();
		Thread thread = new Thread(cemail);
		thread.start();
	}
}
