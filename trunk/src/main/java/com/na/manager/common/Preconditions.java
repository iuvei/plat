package com.na.manager.common;

/**
 * @author Andy
 * @version 创建时间：2017年10月6日 下午2:50:20
 */
public class Preconditions {
	public static void checkArgument(boolean b, String key, Object[] args) {
		if (!b) {
			throw new BusinessException(key, args);
		}
	}
}
