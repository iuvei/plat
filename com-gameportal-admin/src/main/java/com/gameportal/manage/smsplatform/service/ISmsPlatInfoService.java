package com.gameportal.manage.smsplatform.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.smsplatform.model.SmsPlatAccount;
import com.gameportal.manage.smsplatform.model.SmsPlatBlacklist;
import com.gameportal.manage.smsplatform.model.SmsPlatInfo;
import com.gameportal.manage.smsplatform.model.SmsPlatSendlog;

/**
 * @ClassName: ISmsPlatInfoService
 * @Description: TODO(短信平台接口)
 * @author wbm
 * @date 2014-4-27 下午22:51:41
 */
public abstract interface ISmsPlatInfoService {

	// ******************************短信平台******************************
	SmsPlatInfo querySmsPlatInfoById(Long spiid);

	List<Map<String, Object>> querySelectSmsPlatInfos();

	List<SmsPlatInfo> querySmsPlatInfo(Long spiid, String name,
			Integer startNo, Integer pageSize);

	List<SmsPlatInfo> querySmsPlatInfo(Long spiid, String name, Integer status,
			Integer startNo, Integer pageSize);

	Long querySmsPlatInfoCount(Long spiid, String name);

	Long querySmsPlatInfoCount(Long spiid, String name, Integer status);

	SmsPlatInfo saveSmsPlatInfo(SmsPlatInfo smsPlatInfo) throws Exception;

	boolean saveOrUpdateSmsPlatInfo(SmsPlatInfo smsPlatInfo) throws Exception;

	boolean deleteSmsPlatInfo(Long spiid) throws Exception;

	// ******************************短信平台账号******************************
	SmsPlatAccount querySmsPlatAccountById(Long spaid);

	Long querySmsPlatAccountCount(Long spaid, Long spiid, String name);

	Long querySmsPlatAccountCount(Long spaid, Long spiid, String name,
			Integer status);

	List<SmsPlatAccount> querySmsPlatAccount(Long spaid, Long spiid,
			String name, Integer status, Integer startNo, Integer pageSize);

	boolean saveOrUpdateSmsPlatAccount(SmsPlatAccount smsPlatAccount);

	boolean deleteSmsPlatAccount(Long spaid);

	boolean updateSmsPlatAccountOnlyStatus(Long spaid, Integer status);

	// ******************************短信平台黑名单******************************
	SmsPlatBlacklist querySmsPlatBlacklistById(Long spbid);

	Long querySmsPlatBlacklistCount(Long spbid, Long spiid, String mobile);

	List<SmsPlatBlacklist> querySmsPlatBlacklist(Long spbid, Long spiid,
			String mobile, Integer startNo, Integer pageSize);

	boolean saveOrUpdateSmsPlatBlacklist(SmsPlatBlacklist smsPlatBlacklist);

	boolean deleteSmsPlatBlacklist(Long spbid);
	
	// ******************************短信发送日志******************************
	Long querySmsLogCount(Map<String, Object> params);
	List<SmsPlatSendlog> querySmsLoglist(Map<String, Object> params,int pageSize,int thisPage);
	
	/**
	 * 查询发送平台信息。
	 * @param params
	 * @return
	 */
	List<SmsPlatAccount> getList(Map<String, Object> params);
	
	/**
	 * 保存短信日志。
	 * 
	 * @param log
	 *            短信发送日志信息
	 * @return 返回短信实体。
	 */
	SmsPlatSendlog saveSmsPlat(SmsPlatSendlog log);
	
	/**
	 * 发送短信接口。
	 * 
	 * @param mobile
	 *            要发送的手机号码(多个手机用","分隔。一次最多99个号码)
	 * @param message
	 *            短信内容，一条短信最大长度，视所选择的通道而不同。(通道二：64个字；即时通道：50个字)
	 * @param type
	 *            (通道选择 0：默认通道； 2：通道2； 3：即时通道)
	 *  @param sms
	 *  短信发送平台          
	 * @return 返回发送结果。
	 */
	String sendSMS(String mobile, String message, String type,SmsPlatAccount sms) throws Exception;
}
