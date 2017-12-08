package com.gameportal.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.gameportal.comms.AESEncryptDecryt;

/**
 * 数据连接池加密
 * @author brooke
 *
 */
public class PoolDataSource extends DruidDataSource{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2653277120328777696L;

	public PoolDataSource(){
		this(false);
	}

	public PoolDataSource(boolean fairLock){
        super(fairLock);
    }
	
	@Override
	public String getUsername() {
        try {
			return AESEncryptDecryt.decrypt(username);
		} catch (Exception e) {
			System.err.println("用户名解码错误："+e.getMessage());
		}
		return null;
    }
	
	@Override
	public String getPassword() {
        try {
			return AESEncryptDecryt.decrypt(password);
		} catch (Exception e) {
			System.err.println("数据库解码错误："+e.getMessage());
		}
		return null;
    }
	
	public static void main(String[] args) {
		System.out.println(AESEncryptDecryt.encrypt("root"));
		System.out.println(AESEncryptDecryt.encrypt("vn124567"));
	}
}
