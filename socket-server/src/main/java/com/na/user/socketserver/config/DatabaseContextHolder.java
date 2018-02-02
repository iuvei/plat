package com.na.user.socketserver.config;

public class DatabaseContextHolder {
	
	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDatabaseType(String type) {
		contextHolder.set(type);
	}

	public static String getDatabaseType() {
		return contextHolder.get();
	}

	public static void clearCustomerType() {
		contextHolder.remove();
	}

}
