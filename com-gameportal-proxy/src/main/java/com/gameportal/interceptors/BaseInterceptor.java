package com.gameportal.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class BaseInterceptor implements HandlerInterceptor {

	protected static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
}
