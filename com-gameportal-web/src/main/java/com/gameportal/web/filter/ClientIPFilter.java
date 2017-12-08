package com.gameportal.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.gameportal.web.util.ClientIP;
import com.gameportal.web.util.PropertiesUtils;
import com.gameportal.web.util.ip.IPUtils;


/**
 * 限制客户端IP过滤器
 * @author Administrator
 *
 */
public class ClientIPFilter implements Filter{
    private static final Logger logger = Logger.getLogger(ClientIPFilter.class);

    /*不过滤URL地址*/
    public static final String PARAM_NAME_NOURL 			= "filterurl";
    /*运行访问IP列表*/
//    public static final String  PARAM_NAME_ALLOW            = "allow";
    /*运行访问IP列表*/
    public static final String  IP_ALLOW_FILE            = "ip_allow.properties";
    /*是否启用IP控制*/
    public static final String  PARAM_NAME_RESET_ENABLE     = "resetEnable";
    private final static String TEMPLATE_PAGE_RESOURCE_PATH = "/forbiddenPage.htm";
    private boolean statService;

    /**
     * 不过滤的IP地址
     */
    private List<String> nourlList= new ArrayList<String>();

    /**
     * 允许访问IP地址列表
     */
    private List<String>       allowList                   = new ArrayList<String>();

    private FilterConfig filterConfig;

    /**
     * 是否允许请求
     * @param remoteAddress
     * @return true 允许请求
     */
    public boolean isPermittedRequest(String remoteAddress) {
        if (allowList.size() > 0) {
            for (String range : allowList) {
                String[] clientip = remoteAddress.split(",");
                for(String cip : clientip){
                    if(range.equals(cip)){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    /**
     * 验证用户访问URL是否不过滤
     * @param remoteUrl 当前用户访问URL
     * @return 如果不过滤URL列表中存在当前用户访问的URL则返回<code>true</code> 否则返回<code>false</code>
     */
    public boolean isFilterUrl(String remoteUrl){
        if(nourlList.size() > 0){
            for(String item : nourlList){
                if(item.equals(remoteUrl)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否允许访问
     * @param address
     * @return true 允许访问  false 禁止访问
     */
    public boolean isRequest(String clientip){
        try {
//            logger.info("是否允许访问校验："+clientip);
            if("".equals(clientip) || null == clientip){
                return true;
            }
            boolean isPHIP = IPUtils.checkIpIsPH(clientip);
//            logger.info("isPHIP:" + isPHIP);
            if(isPHIP){//是菲律宾本地IP
                return false;
            }
            /*String[] clientipList = clientip.split(",");
			for(String cip : clientipList){
				String address = IPSeeker.getInstance().getAddress(cip);
				if(StringUtils.isNotBlank(address)){
					if(address.indexOf("菲律宾") > -1){
						return false;
					}
				}
			}*/
            return true;
        } catch (Exception e) {
            logger.error("校验IP归属地错误："+e.getMessage());
            return true;
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        //读取是否启用状态
        try {
            String param = this.filterConfig.getInitParameter(PARAM_NAME_RESET_ENABLE);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                boolean resetEnable = Boolean.parseBoolean(param);
                statService = resetEnable;
            }
        }catch (Exception e) {
            String msg = "initParameter config error, resetEnable : " + this.filterConfig.getInitParameter(PARAM_NAME_RESET_ENABLE);
            logger.error(msg);
        }

        //读取可以访问的IP地址
        try {
//            String param = this.filterConfig.getInitParameter(PARAM_NAME_ALLOW);
            Properties ipProperties = PropertiesUtils.loadProperties(IP_ALLOW_FILE);
            String param = ipProperties.getProperty("allowIp");
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                String[] items = param.split(",");
                for (String item : items) {
                    if (item == null || item.length() == 0) {
                        continue;
                    }
                    allowList.add(item);
                }
            }
        } catch (Exception e) {
//            String msg = "initParameter config error, allow : " + this.filterConfig.getInitParameter(PARAM_NAME_ALLOW);
            logger.error("读取可以访问的IP地址异常！msg:"+e.getMessage());
        }

        //读取不过滤URL
        try {
            String param = this.filterConfig.getInitParameter(PARAM_NAME_NOURL);
            if(param != null && param.trim().length() != 0){
                param = param.trim();
                String[] items = param.split(",");
                for(String item : items){
                    if(item == null || item.length() == 0){
                        continue;
                    }
                    nourlList.add(item);
                }
            }
        } catch (Exception e) {
            String msg = "initParameter config error, url : " + this.filterConfig.getInitParameter(PARAM_NAME_NOURL);
            logger.error(msg);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //HttpServletResponse httpPesponse = (HttpServletResponse)response;
        String requestURI = httpRequest.getRequestURI();
        if(statService){
            String clientip = ClientIP.getInstance().getIpAddr(httpRequest);
//            logger.info("是否允许请求："+isPermittedRequest(clientip));
            if(!isPermittedRequest(clientip)){
                boolean urlFlag = isFilterUrl(requestURI);
//                logger.info("当前访问的地址是否在过滤地址中："+urlFlag);
                //当前访问的地址不在过滤地址中
                if(!urlFlag){
                    if(isRequest(clientip) == false){
                        //转发
                        RequestDispatcher dispatch=request.getRequestDispatcher(TEMPLATE_PAGE_RESOURCE_PATH);
                        dispatch.forward(request, response);
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }

}
