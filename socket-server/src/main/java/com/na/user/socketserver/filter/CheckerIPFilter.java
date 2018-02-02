package com.na.user.socketserver.filter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.common.enums.DeviceTypeEnum;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.enums.IpBlackWhitePlatType;
import com.na.user.socketserver.common.enums.IpBlackWhiteType;
import com.na.user.socketserver.entity.IpBlackWhiteAddr;
import com.na.user.socketserver.util.IpUtils;

/**
 * 检查IP黑白名单过滤器
 * 
 * @author alan
 * @date 2017年7月21日 下午3:14:25
 */
@Component
public class CheckerIPFilter {

	private final static Logger log = LoggerFactory.getLogger(CheckerIPFilter.class);
	
	public boolean doFilter(SocketIOClient client, DeviceTypeEnum deviceType) {
		//检测IP黑白名单
		String clientIp = client.getRemoteAddress().toString();
		clientIp = (String) clientIp.subSequence(clientIp.indexOf("/") + 1, clientIp.lastIndexOf(":"));
		return doFilter(clientIp, deviceType);
	}
	
	public boolean doFilter(String ip, DeviceTypeEnum deviceType) {
		int type;
		
		if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
			//根据网卡取本机配置的IP
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				log.error(e.getMessage(),e);
			}
			ip = inet.getHostAddress();
		}
		
		if(deviceType == DeviceTypeEnum.PC) {
			type = IpBlackWhitePlatType.DEALER.get();
		} else {
			type = IpBlackWhitePlatType.GAMEUI.get();
		}
		
		String whiteKey = StringUtils.join(new Object[] { type, IpBlackWhiteType.WHITE.get() }, ".");
		String blackKey = StringUtils.join(new Object[] { type, IpBlackWhiteType.BLACK.get() }, ".");

		if(AppCache.getBlackWhiteIpMap().containsKey(whiteKey) && validateIp(whiteKey, ip)){
			return true;
		}

		if(AppCache.getBlackWhiteIpMap().containsKey(blackKey) && validateIp(blackKey, ip)) {
			log.info("【{}】拒绝访问",ip);
			return false;
		}

		return true;
	}
	
	private boolean validateIp(String key,String ip){
    	boolean flag = false;
    	long ipNum = IpUtils.ip2Num(ip);
    	for(IpBlackWhiteAddr ipAddr : AppCache.getBlackWhiteIpMap().get(key)){
    		if(ipNum>=ipAddr.getStartNum() && ipNum<=ipAddr.getEndNum()){
    			flag =true;
    			break;
    		}
    	}
    	return flag;
    }
	
}
