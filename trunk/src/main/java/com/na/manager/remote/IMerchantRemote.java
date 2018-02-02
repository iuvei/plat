package com.na.manager.remote;

import java.math.BigDecimal;

import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.MerchantUser;

/**
 * @author Andy
 * @version 创建时间：2017年8月16日 下午3:23:31
 */
public interface IMerchantRemote {

	/**
	 * 获取商户信息
	 * 
	 * @param number
	 *            商户号
	 * @param number
	 *            商户私钥
	 * @return
	 */
	MerchantUser getMerchantUser(String number, String privateKey);

	/**
	 * 新增会员
	 * 
	 * @param userName
	 *            会员账号
	 * @param password
	 *            登陆密码
	 * @param nickName
	 *            昵称
	 * @param parentId
	 *            父级ID
	 * @return
	 */
	Long addLiveUser(String userName, String password, String nickName, Long parentId);

	/**
	 * 获取用户信息&游戏余额
	 * 
	 * @param userName
	 *            会员账号
	 * @return
	 */
	LiveUser getLiveUser(String userName);

	/**
	 * 修改用户登陆密码
	 * 
	 * @param userId
	 *            会员ID
	 * @param newPassword
	 *            新密码
	 */
	void updatePassword(Long userId, String newPassword);

	/**
	 * 存款
	 * 
	 * @param userId
	 *            会员ID
	 * @param parentId
	 *            上级ID
	 * @param amount
	 *            存款金额
	 * @param transactionId
	 *            交易订单号
	 */
	void deposit(Long userId, Long parentId, BigDecimal amount, String transactionId);

	/**
	 * 取款
	 * 
	 * @param userId
	 *            会员ID
	 * @param parentId
	 *            上级ID
	 * @param amount
	 *            提款金额
	 * @param transactionId
	 *            交易订单号
	 */
	void withdraw(Long userId, Long parentId, BigDecimal amount, String transactionId);
	
	/**
	 * 查询投注记录
	 * 
	 * @param request
	 * @return
	 */
	Page<BetOrderVO> pageBetOrder(String startTime,String endTime,Integer pageSize,Integer currentPage,Long parentId);

}
