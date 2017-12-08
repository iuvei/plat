package com.gameportal.web.mail.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.cache.PortalGameCache;
import com.gameportal.web.mail.IEmailService;
import com.gameportal.web.mail.dao.MailSenderInfoDao;
import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.EncryptDecryt;
import com.gameportal.web.util.WebConst;

/**
 * 邮件发送服务类。
 * 
 * @author sum
 *
 */
@Service("emailService")
public class EmailServiceImpl implements IEmailService {

	public static final Logger logger = Logger.getLogger(EmailServiceImpl.class);
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService;
	@Resource(name="mailSenderInfoDao")
	private MailSenderInfoDao mailSenderInfoDao;
	@Resource(name="activityFlagService")
	private IActivityFlagService activityFlagService;

	@Override
	public void addEmailList(UserInfo userInfo, String email,String urlPrefix, Properties prop) throws Exception{
		try {
			if(iRedisService.keyExists(WebConst.E_MAIL_PREFIX + userInfo.getUiid() + WebConst.E_MAIL_QUEUE)){
				return;
			}
			List<ActivityFlag> activities = activityFlagService.getActivityFlags();
			if (CollectionUtils.isEmpty(activities)) {
				ActivityFlag activityFlag = new ActivityFlag();
				activityFlag.setType(1);
				activityFlag.setNumbers(0);
				activityFlag.setFlagtime(new Date());
				String hms = DateUtil.getStrByDate(new Date(), "HH:mm:ss");
				if (hms.compareTo("11:59:59") > 0 && hms.compareTo("23:59:59") < 0) {
					activityFlag.setHms("23:59:59");
				} else {
					activityFlag.setHms("11:59:59");
				}
				activityFlagService.save(activityFlag);
			}
			String emailKey = prop.getProperty("email.key");
			String urlSuffix = prop.getProperty("email.returnURL");
			StringBuffer sb = new StringBuffer();
			sb.append(userInfo.getAccount()).append("#").append(email).append("#").append(emailKey).append("#").append(userInfo.getUiid());
			EncryptDecryt e = new EncryptDecryt(WebConst.EMAILCODE_DES_KEY);
			String ekey = e.encrypt(sb.toString());// E-MAIL校验key
			String redis_key = email + WebConst.E_MAIL_KEY;
			if (iRedisService.keyExists(redis_key)) {// 存在redis
				iRedisService.delete(redis_key);
			}
			iRedisService.setObjectFromRedis(redis_key, ekey, 60*120);// 将key保存到redis中
			String context = urlPrefix + urlSuffix + ekey;
			
			int random = new Random().nextInt(PortalGameCache.mailerverList.size()) % (PortalGameCache.mailerverList.size() - 0 + 1) + 0;
			
			// 设置邮件
			MailSenderInfo mailInfo = PortalGameCache.mailerverList.get(random);
			mailInfo.setFromAddress(mailInfo.getUserName());
			mailInfo.setToAddress(email);
			mailInfo.setValidate(true);
			mailInfo.setCreateDate(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			String account = userInfo.getAccount();
			mailInfo.setSubject(mailInfo.getSubject().replace("%s", account));
			String emailContext = "<div style=\"background-color:#fcfcfc; width:100%;height:414px;\">";
			emailContext+="<div style=\"padding-top:100px;font-family:'新宋体';width:602px;margin: 0px auto;\">";
			emailContext +="<div>"+account+"您好！</div><div>您的邮箱地址："+email+",操作时间为："+mailInfo.getCreateDate()+"</div>";
			emailContext+="<div><h3><a href='" + context + "' target='_blank' style='color:red;'>点击进行邮箱激活</a></h3></div>"
					+ "<div>如非您本人发送，请忽略此信息。</div><div>-----------------------------</div><div>本邮件为系统自动通知，请勿直接回复！</div></div></div>";

			mailInfo.setContent(emailContext);
			mailInfo.setUuid(userInfo.getUiid());
			mailInfo.setMailid(null);
			// 保存邮件至数据库
			mailSenderInfoDao.save(mailInfo);
			// 将邮件内容保存至redis中
			iRedisService.addToRedis(WebConst.E_MAIL_PREFIX + userInfo.getUiid() + WebConst.E_MAIL_QUEUE, mailInfo,60*120);

		} catch (Exception e) {
			logger.error("申请邮箱认证失败。",e);
		}
	}
	

	@Override
	public void update(MailSenderInfo mail) {
		mailSenderInfoDao.update(mail);
	}

	@Override
	public List<MailSenderInfo> getMailSenderInfo(Map values) {
		return mailSenderInfoDao.getMailSenderInfo(values);
	}


	@Override
	public MailSenderInfo getObject(Map<String, Object> params) {
		return mailSenderInfoDao.getObject(params);
	}
}
