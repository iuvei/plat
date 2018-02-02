package com.na.manager.common;

import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
* @author Andy
* @version 创建时间：2018年1月19日 上午9:54:29
*/
@Component
public class ActiveMQUtil {
	
	@Resource(name="platMaintenanceTopic")
	private Topic platMaintenanceTopic;
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	public void send(Object data, Map<String,Object> headers) {
		this.jmsMessagingTemplate.convertAndSend(this.platMaintenanceTopic, JSONObject.toJSONString(data), headers);
	}

}
