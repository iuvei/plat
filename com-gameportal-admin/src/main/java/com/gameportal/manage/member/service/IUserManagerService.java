package com.gameportal.manage.member.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.member.model.UserManager;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.xima.model.MemberXimaMain;

public interface IUserManagerService {

	public UserManager getObject(Map<String, Object> params);
	
	public boolean save(UserManager entity);
	
	public boolean update(UserManager entity);
	
	public Long getCount(Map<String, Object> params);
	
	public List<UserManager> getList(Map<String, Object> params,int thisPage,int pageSize);
	
	public UserManager getUManager(Map<String, Object> params);
	
	public boolean delete(Long uiid) ;
	
	
	public List<Map<String, Object>> getDXList(Map<String, Object> params);
	
	
	/**
	 * 存款 取款 优惠列表
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<PayOrder> getPayOrderList(Map<String, Object> params,int thisPage,int pageSize);
	/**
	 * 存款 取款 优惠列表
	 * @param params
	 * @return
	 */
	public Long getPayOrderCount(Map<String, Object> params);
	
	
	/**
	 * 洗码列表
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<MemberXimaMain> getXimaList(Map<String, Object> params,int thisPage,int pageSize);
	/**
	 * 洗码总数
	 * @param params
	 * @return
	 */
	public Long getXimaCount(Map<String, Object> params);
	
	/**
	 * 保存系统用户信息
	 * @param systemUser
	 * @return
	 */
	public boolean saveOrUpdateSystemUser(SystemUser systemUser);
}
