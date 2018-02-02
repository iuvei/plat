package com.na.betRobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
	
	@Value("${server.url}")
    private String serverUrl;
	
	@Value("${server.defaultKey}")
    private String defaultKey;
	
	@Value("${server.robotAgent}")
    private String robotAgent;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getDefaultKey() {
		return defaultKey;
	}

	public void setDefaultKey(String defaultKey) {
		this.defaultKey = defaultKey;
	}

	public String getRobotAgent() {
		return robotAgent;
	}

	public void setRobotAgent(String robotAgent) {
		this.robotAgent = robotAgent;
	}
	
	
	
}
