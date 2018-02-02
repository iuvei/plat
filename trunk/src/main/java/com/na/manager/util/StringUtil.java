package com.na.manager.util;

import com.google.common.base.Preconditions;

/**
 * @author andy
 * @date 2017年6月26日 下午3:46:06
 * 
 */
public class StringUtil {
	/**
	 * 验证IP 的格式是否正确
	 * @param ip
	 * @return
	 */
	public static String checkIpFormat(String ip) {
		String regex = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
        if (ip.matches(regex)) {
            return ip;
        } else {
            return null;
        }
    }
	
	/**
	 * 验证IP 的格式是否正确
	 * @param ip
	 * @return
	 */
	public static void checkManyIpFormat(String ips) {
		Preconditions.checkNotNull(ips,"black.white.ip.format.error");
		
		String ipStr = BeanUtil.cloneTo(ips);
		ipStr = ipStr.replace("\r", "").replace("\n", "");
		final String[] arrIps = ipStr.split(";");
		for(String s : arrIps) {
			if("".equals(s)) {
				continue;
			}
			Preconditions.checkArgument(checkIpFormat(s) != null,"black.white.ip.format.error");
		}
    }
	
}
