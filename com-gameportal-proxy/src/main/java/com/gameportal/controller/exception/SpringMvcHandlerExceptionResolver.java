package com.gameportal.controller.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.gameportal.comms.StringUtils;



/**
 * Spring MVC 异常捕获
 * @author john
 * @version 1.0.0
 */
@Component
public class SpringMvcHandlerExceptionResolver implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(SpringMvcHandlerExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception ex) {
		String exceptionMessage = StringUtils.getExceptionMessage(ex);
		logger.error("MVC异常",ex);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exceptionMessage", exceptionMessage);
		return new ModelAndView("/error/exceptionMessage", model);
	}
}
