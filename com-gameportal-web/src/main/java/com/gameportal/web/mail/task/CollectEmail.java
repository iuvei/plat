package com.gameportal.web.mail.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.mail.IEmailService;
import com.gameportal.web.mail.SimpleMailSender;
import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.DateUtil;

/**
 * 邮件发送线程
 * @author Administrator
 *
 */
public class CollectEmail extends Thread{
	
	public static final Logger logger = Logger.getLogger(CollectEmail.class);
	
	private IRedisService iRedisService;
	private IEmailService iEmailService;
	private IActivityFlagService activityFlagService;
	private IUserInfoService userInfoService;

	/**
	 * 从redis里面获取邮件内容邮件内容
	 */
	private MailSenderInfo mailInfo;
	
	@Override
	public synchronized void run() {
		try {
//			logger.info("邮件发送线程->"+this.getName());
//			iRedisService = (IRedisService) SpringUtils.getBean("redisServiceImpl");
//			iEmailService = (IEmailService)SpringUtils.getBean("emailService");
//			activityFlagService = (IActivityFlagService)SpringUtils.getBean("activityFlagService");
//			userInfoService = (IUserInfoService)SpringUtils.getBean("userInfoServiceImpl");
//			List<String> redisKeys =iRedisService.getKeys("*"+WebConst.E_MAIL_QUEUE);
//			logger.info(this.getName()+"->邮件发送redis列表->"+redisKeys);
//			if(null != redisKeys && redisKeys.size() > 0){
//				String curKey = redisKeys.get(0);
//				logger.info(this.getName()+"->邮件发送redis数据->"+curKey);
//				//从redis里面取出邮件数据
//				mailInfo = iRedisService.getRedisResult(curKey, MailSenderInfo.class);
//				if(null != mailInfo){
//					boolean success = sendEmail(curKey);
//					
//					if(success){
//						iRedisService.delete(curKey);//删除rediskey数据
//						destroy();
//					}
//				}
//			}
		} catch (Exception e) {
			logger.error("邮件发送异常->"+e.getMessage(), e);
		}
		
	}

	/**
	 * 邮件发送
	 * @param curKey redis key
	 * @return
	 */
	public synchronized boolean sendEmail(String curKey){
		logger.info(this.getName()+"->开始发送邮件->"+curKey);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uuid", curKey.split("_")[1]);//获取用户ID
		params.put("status",0);
		//从数据库获取邮箱日志
		MailSenderInfo maillogs = iEmailService.getObject(params);
		try {
			SimpleMailSender sms = new SimpleMailSender();
			//发送邮件
			boolean success = sms.sendHtmlMail(mailInfo);
			if(success){
				maillogs.setStatus(3);//发送成功，等待验证状态
			}else{
				maillogs.setStatus(2);//发送失败
			}
			maillogs.setSendDate(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			iEmailService.update(maillogs);
			
			//更新活动数量
			List<ActivityFlag> activities = activityFlagService.getActivityFlags();
			if(CollectionUtils.isNotEmpty(activities)){
				ActivityFlag activityFlag = activities.get(0);
				activityFlag.setNumbers(activityFlag.getNumbers()+1);
				activityFlagService.update(activityFlag);
			}
		} catch (Exception e) {
			iRedisService.delete(curKey);
			UserInfo userInfo =userInfoService.findUserInfoId(Long.valueOf(curKey.split("_")[1]));
		      if(userInfo !=null){
		       userInfo.setEmailvalid(0);
		       userInfoService.updateUserInfo(userInfo);
		       maillogs.setStatus(2);
		       maillogs.setSendDate(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			   iEmailService.update(maillogs);
			    
		      }
			logger.error("邮件发送失败->"+userInfo.getEmail()+"---->"+e.getMessage(), e);
			return false;
		}
		logger.info(this.getName()+"->邮件发送成功->"+curKey);
		return true;
	}
	public MailSenderInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailSenderInfo mailInfo) {
		this.mailInfo = mailInfo;
	}
	
	public void destroy(){
		mailInfo = null;
	}
}
