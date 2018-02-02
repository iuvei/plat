package com.na.gate.listen;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.na.gate.cache.GateCache;
import com.na.gate.proto.SocketClient;

@Component
public class AccountRecordTopicConsumer {
	private Logger logger=LoggerFactory.getLogger(AccountRecordTopicConsumer.class);

	@Autowired
	private SocketClient client;
	
    @JmsListener(destination = "account.record", containerFactory = "myFactory",selector="path like '/2/6/10/%' or path like '/2/5/11/%'",subscription="accountRecord")
    public void listen(Message<String> msg){
    	String path=(String)msg.getHeaders().get("path");
    	String content=(String)msg.getPayload();
    	logger.info("收到头：[{}]\n收到消息：[{}]",path,content);
        if(!client.sendBetAccountRecord(path, content)){
        	throw new RuntimeException(path+"发送账单异常");
        }
    }
    
    @JmsListener(destination = "bet.record", containerFactory = "betFactory",subscription="betRecord")
    public void betRecord(Message<String> msg){
    	String roundId = (String)msg.getPayload();
    	logger.info("收到局号：{}",roundId);
        if(!client.sendBetOrderByRoundId(Long.valueOf(roundId))){
        	throw new RuntimeException("推送局：["+roundId+"]投注记录发送异常");
        }
    }
    
    @JmsListener(destination = "plat.maintenance", containerFactory = "betFactory",subscription="platMaintenance")
    public void platMaintenance(Message<String> msg){
    	String flag = (String)msg.getPayload();
    	logger.info("收到平台维护通知：{}",flag);
    	GateCache.platMaintenance=flag;
    }


    @Bean
    public JmsListenerContainerFactory<?> myFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSubscriptionDurable(true);
        factory.setClientId("na-gate-account");
        factory.setPubSubDomain(true);

        factory.setMaxMessagesPerTask(100);
        factory.setConcurrency("1");
        factory.setReceiveTimeout(30*1000L);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        factory.setBackOff(new ExponentialBackOff(10*1000,2));
        factory.setRecoveryInterval(30*1000l);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
    
    @Bean
    public JmsListenerContainerFactory<?> betFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSubscriptionDurable(true);
        factory.setClientId("na-gate-bet");
        factory.setPubSubDomain(true);

        factory.setMaxMessagesPerTask(100);
        factory.setConcurrency("1");
        factory.setReceiveTimeout(30*1000L);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setRecoveryInterval(30*1000l);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
    
    @Bean
    public JmsListenerContainerFactory<?> maintainFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSubscriptionDurable(true);
        factory.setClientId("na-gate-maintain");
        factory.setPubSubDomain(true);

        factory.setMaxMessagesPerTask(100);
        factory.setConcurrency("1");
        factory.setReceiveTimeout(30*1000L);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setRecoveryInterval(30*1000l);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
