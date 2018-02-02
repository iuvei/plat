package com.na.user.socketserver.util;

import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class ActiveMQUtil {
	
	@Resource(name="AccountRecordTopic")
	private Topic accountRecordTopic;
	
	@Resource(name="BetRecordTopicTopic")
	private Topic betRecordTopicTopic;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	public void send(Object data, Map<String,Object> headers) {
		this.jmsMessagingTemplate.convertAndSend(this.accountRecordTopic, JSONObject.toJSONString(data), headers);
	}
	
	public void sendResult(Object data, Map<String,Object> headers) {
		this.jmsMessagingTemplate.convertAndSend(this.betRecordTopicTopic, JSONObject.toJSONString(data), headers);
	}

}
