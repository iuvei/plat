package com.gameportal.manage.payplatform.service;

import java.util.List;

import com.gameportal.manage.payplatform.model.PayPlatform;

/**
 * @ClassName: IPayPlatformService
 * @Description: TODO(支付平台接口)
 * @author shejia@gz-mstc.com
 * @date 2014-4-10 下午4:39:05
 */
public abstract interface IPayPlatformService {
	public abstract PayPlatform queryPayPlatformById(Long ppid);
	
	public abstract List<PayPlatform> queryPayPlatform(Long ppid,
			String pname,String channelType, Integer startNo, Integer pageSize);
	
	public abstract List<PayPlatform> queryPayPlatform(Long ppid,
			String pname, Integer status,String channelType, Integer startNo, Integer pageSize);
	
	public abstract List<PayPlatform> queryPayPlatform(Long ppid,
			String pname, String domainname, String platformkey, 
			String ciphercode, String returnUrl, String noticeUrl, 
			Integer status,String channelType, Integer startNo, Integer pageSize);
	
	public abstract Long queryPayPlatformCount(Long ppid,
			String pname,String channelType);
	
	public abstract Long queryPayPlatformCount(Long ppid,
			String pname, Integer status,String channelType);
	
	public abstract Long queryPayPlatformCount(Long ppid,
			String pname, String domainname, String platformkey, 
			String ciphercode, String returnUrl, String noticeUrl, Integer status,String channelType);
	
	public abstract PayPlatform savePayPlatform(PayPlatform payPlatform)
			throws Exception;

	public abstract boolean saveOrUpdatePayPlatform(PayPlatform payPlatform)
			throws Exception;

	public abstract boolean deletePayPlatform(Long ppid) throws Exception;
	
}
