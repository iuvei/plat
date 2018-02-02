package com.na.manager.aop;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *  跨域过滤器。
 *  Created by sunny on 2017/6/8 0008.
 */
@Component
public class CrossFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CrossFilter.class);
    @Value("#{'${cross.origin}'.split(',')}")
    protected String[] origin;
    @Value("${cross.methods}")
    protected String methods;
    @Value("${cross.headers}")
    protected String headers;
    @Value("${cross.credentials}")
    protected String credentials;

    public CrossFilter() {
    }

    public CrossFilter(String[] origin, String methods, String headers, String credentials) {
        this.origin = origin;
        this.methods = methods;
        this.headers = headers;
        this.credentials = credentials;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String method = req.getMethod();

        String ori = req.getHeader("origin");
        if(ori!=null) {
            for (String item : this.origin) {
                if ("*".equals(item) || ori.equalsIgnoreCase(item)) {
                    res.setHeader("Access-Control-Allow-Origin", ori);
                    break;
                }
            }
        }
        res.setHeader("Access-Control-Allow-Headers", headers);
        res.setHeader("Access-Control-Allow-Methods", methods);
        res.setHeader("Access-Control-Allow-Credentials", credentials);
        res.setHeader("Access-Control-Max-Age", "60");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
