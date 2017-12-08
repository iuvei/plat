package com.gameportal.web.mail.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.activity.model.ActivityFlag;
import com.gameportal.web.activity.service.IActivityFlagService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.mail.IEmailService;
import com.gameportal.web.mail.model.MailSenderInfo;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IXimaFlagService;
import com.gameportal.web.util.AESEncryptDecryt;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.EncryptDecryt;
import com.gameportal.web.util.GetProperty;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/mail")
public class MailController{

	public static final Logger logger = Logger.getLogger(MailController.class);
	@Resource(name="ximaFlagService")
	private IXimaFlagService ximaFlagService;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "gameTransferServiceImpl")
	private IGameTransferService gameTransferService = null;
	@Resource(name="emailService")
	private IEmailService emailService;
	@Resource(name="activityFlagService")
	private IActivityFlagService activityFlagService;
	private static Properties prop = GetProperty.getProp("jdbc.properties");
	
	public MailController(){
		super();
	}
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "/mail/index";
	}
	
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(
			@RequestParam(value = "email", required = false) String email,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			String key = vuid + "GAMEPORTAL_USER";
			Class<Object> c = null;
			UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
			List<String> redisKeys =iRedisService.getKeys("*"+WebConst.E_MAIL_QUEUE);
			List<ActivityFlag> activities = activityFlagService.getActivityFlags();
			Properties actProp = GetProperty.getProp("activity.properties");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("email", email);
			param.put("emailvalid", 0);
			param.put("neuiid", userInfo.getUiid());
			
			long count= userInfoService.getUserInfoCount(param);
			if(count>0){
				json.put("code", "0");
				json.put("info", "该邮箱已经被绑定，同一个邮箱只能绑定一个账号。");
				return json.toString();
			}
			if(CollectionUtils.isNotEmpty(activities)){
				boolean flag = (activities.get(0).getNumbers() + redisKeys.size()) >= Integer
						.valueOf(actProp.getProperty("activity.day.reward.numbers")) ? true : false;
				if (flag) {
					json.put("code", "1");
					json.put("info", "非常抱歉，本时间段信息认证抢礼金名额已满，请下个时间段再来认证吧！");
					return json.toString();
				}
			}
			if(null != userInfo){
				userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
				/**
				if(userInfo.getPhonevalid() !=1){
					json.put("code", "1");
					json.put("info", "您的手机号码还没有通过验证，不能验证邮箱！");
					return json.toString();
				}
				*/
				if(userInfo.getEmailvalid() == 1){
					json.put("code", "1");
					json.put("info", "邮箱已经认证，无需再次认证！");
					return json.toString();
				}
				
				Properties prop = GetProperty.getProp("activity.properties");
				if("false".equals(prop.getProperty("email.validate"))){
					userInfo.setEmailvalid(1);
					userInfo.setEmail(email);
					userInfoService.updateUserInfo(userInfo);
					json.put("code", "1");
					json.put("info", "恭喜，您的邮箱认证成功！");
					return json.toString();
				}
				
				boolean flag = false;
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("uuid", userInfo.getUiid());
				param.put("email", email);
				params.put("status",0); // 待发送
				MailSenderInfo maillog = emailService.getObject(params);
				if (maillog != null) {
					flag = true;
				} else {
					params.put("status", 3); // 发送成功，待验证
					maillog = emailService.getObject(params);
					if (maillog != null && DateUtil.getBetweenMin(
							DateUtil.getDateByStr(maillog.getCreateDate()),new Date()) <= 120) {
						flag = true;
					}
				}
				// 如果邮件已经存在并且邮箱的激活码仍在有效期内，就直接返回。
				if(iRedisService.keyExists(WebConst.E_MAIL_PREFIX + userInfo.getUiid() + WebConst.E_MAIL_QUEUE) || flag){
					json.put("code", "1");
					json.put("info", "邮箱认证申请已经提交，请稍后打开您的邮箱进行认证！");
					return json.toString();
				}/*else if(userInfo.getEmailvalid() ==2){
					if(null != maillog 
							&& DateUtil.getBetweenMin(
									DateUtil.getDateByStr(maillog.getCreateDate()),new Date()) <= 120){
						json.put("code", "1");
						json.put("info", "邮箱认证申请已经提交，请稍后打开您的邮箱进行认证！");
						return json.toString();
					}
				}*/
				String urlPrefix ="";
				if(StringUtils.isNotEmpty(request.getServerPort()+"")){
					urlPrefix = "http://"+request.getServerName()+":"+request.getServerPort()+"/";
				}else{
					urlPrefix = "http://"+request.getServerName()+"/";
				}
				emailService.addEmailList(userInfo,email,urlPrefix,prop);
				userInfo.setEmailvalid(2);
				userInfo.setEmail(email);
				userInfoService.updateUserInfo(userInfo);
				json.put("code", "1");
				json.put("info", "邮箱认证申请成功，请稍后去您的邮箱进行认证！");
			}
		} catch (Exception e) {
			logger.error("邮件发送失败：", e);
			json.put("code", "0");
			json.put("info", "邮箱发送失败。");
		}
		return json.toString();
	}
	
	
	@RequestMapping(value = "/verify")
	public synchronized String verify(
			HttpServletRequest request, HttpServletResponse response) {
		String params = request.getQueryString();
		if(!StringUtils.isNotBlank(params)){
			request.setAttribute("errorMsg", "邮箱认证参数不能为空。");
			return "/mail/index";
		}
		String data = null;
		try {
			EncryptDecryt e = new EncryptDecryt(WebConst.EMAILCODE_DES_KEY);
			data = e.decrypt(params);
			logger.info("解密数据->"+data);
		} catch (Exception e) {
			logger.error("邮箱认证密文解密失败："+e.getMessage());
			request.setAttribute("errorMsg", "邮箱认证密文无效："+e.getMessage());
			return "/mail/index";
		}
		if(!StringUtils.isNotBlank(data)){
			request.setAttribute("errorMsg", "没有获取到认证令牌，邮箱认证失败。");
			return "/mail/index";
		}
		try {
			String[] p = data.split("#");
			String redis_key = p[1]+WebConst.E_MAIL_KEY;
			UserInfo userMsg = userInfoService.findUserInfoId(Long.valueOf(p[3]));
			if(null == userMsg){
				request.setAttribute("errorMsg", "系统繁忙，请稍候重试。");
				return "/mail/index";
			}
			if(userMsg.getEmailvalid() ==1){
				request.setAttribute("errorMsg", "邮箱已经认证成功，无需再次认证。");
				return "/mail/index";
			}
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("uuid", p[3]);
			values.put("status",3); // 待验证
			MailSenderInfo logs =emailService.getObject(values);
			if(iRedisService.keyExists(redis_key) == false){
				if(logs !=null){
					logs.setStatus(4); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				userMsg.setEmailvalid(0); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "认证连接已失效，邮箱认证失败。");
				return "/mail/index";
			}
			String key = iRedisService.getStringFromRedis(redis_key);
			if(!params.equals(key)){//校验key与redis key不匹配
				logger.info("redis_key_value:"+key);
				logger.info("params:"+params);
				if(logs !=null){
					logs.setStatus(4); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				userMsg.setEmailvalid(0); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "认证令牌不匹配，邮箱认证失败。");
				return "/mail/index";
			}
			String emailKey = prop.getProperty("email.key");
			if(!emailKey.equals(p[2])){
				if(logs !=null){
					logs.setStatus(4); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				userMsg.setEmailvalid(0); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "认证被非法串改，邮箱认证失败。");
				return "/mail/index";
			}
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("email", p[1]);
			param1.put("emaileq", 1);
			long count= userInfoService.getUserInfoCount(param1);
			if(count>0){
				if(logs !=null){
					logs.setStatus(4); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				userMsg.setEmailvalid(0); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "该邮箱已经被绑定，同一个邮箱只能绑定一个账号。");
				return "/mail/index";
			}
			if(userMsg.getEmailvalid() ==1){
				if(logs !=null){
					logs.setStatus(1); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "邮箱已经认证成功，无需再次认证。");
				return "/mail/index";
			}
			// 查询后台代理优惠赠送订单
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("uiid", userMsg.getUiid());
			param.put("paytyple", 2);
			param.put("ordertype", 1);
			List<PayOrder> orders = gameTransferService.getPayOrder(param);
			if(CollectionUtils.isNotEmpty(orders)){
				userMsg.setEmailvalid(1); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				request.setAttribute("errorMsg", "邮箱已经认证成功，无需再次认证。");
				return "/mail/index";
			}
			userMsg.setEmail(p[1]);
			userMsg.setEmailvalid(1);
			boolean code = userInfoService.modifyUserInfo(userMsg);
			if(code == false){
				if(logs !=null){
					logs.setStatus(4); //验证失败
					logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					emailService.update(logs);
				}
				userMsg.setEmailvalid(0); //修改会员邮箱验证状态
				userInfoService.updateUserInfo(userMsg);
				iRedisService.delete(redis_key);//删除验证key
				request.setAttribute("errorMsg", "系统错误，邮箱认证失败，请联系在线客服。");
				return "/mail/index";
			}
			iRedisService.delete(redis_key);//删除验证key
			if(logs != null){
				logs.setStatus(1); // 验证成功
				logs.setValidTime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
				emailService.update(logs);
			}
			// 更新换成中的用户信息。
			String vuid = CookieUtil.getOrCreateVuid(request, response);
			key = vuid + "GAMEPORTAL_USER";
			iRedisService.addToRedis(key,userMsg);
			request.setAttribute("successMsg", "邮箱认证成功，祝君游戏愉快！");
//			Properties actProp = GetProperty.getProp("activity.properties");
//			Date regdata = DateUtil.getDateByStr(DateUtil.getStrYMDHMSByDate(userMsg.getCreateDate()));
//			Date hd = DateUtil.getDateByStr(actProp.getProperty("activity.start.time"));
//			if(regdata.before(hd)){//注册时间在活动时间之前
//				return "/mail/index";
//			}
//			
//			param.put("ordertype", 2);
//			orders = gameTransferService.getPayOrder(param);
//			boolean activty_38 = Boolean.valueOf(actProp.getProperty("activity.38").toString());//获取活动状态
//			if(activty_38 && CollectionUtils.isEmpty(orders)){
//				if(userMsg.getPhonevalid() == 1 && userMsg.getEmailvalid() == 1){
//					logger.info("邮箱认证赠送礼金用户->"+userMsg.toString());
//					double rate = Double.valueOf(actProp.getProperty("activity.8.rate"));
//					Integer s = new Random().nextInt(10000) % (10000 - 1 + 1) + 1;
//					Integer reward = (s > rate * 10000) ? 18 : 8;
//					Integer reward = 8;
//					AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userMsg.getUiid(), 1);
//					int beforebalance = accountMoney.getTotalamount();
//					accountMoney.setTotalamount(accountMoney.getTotalamount()+reward);
//					int latrbalance = accountMoney.getTotalamount();
//					userInfoService.updateAccountMoneyObj(accountMoney);
//					Timestamp date = new Timestamp(new Date().getTime());
//					PayOrder payOrder = new PayOrder();
//					payOrder.setPoid(IdGenerator.genOrdId16("REWARD"));
//					payOrder.setPlatformorders(IdGenerator.genOrdId16(""));
//					payOrder.setUiid(userMsg.getUiid());
//					payOrder.setUaccount(userMsg.getAccount());
//					payOrder.setUrealname(userMsg.getUname());
//					payOrder.setPaytyple(2); // 0存款，1提款，2赠送，3扣款
//					payOrder.setPpid(-1L); 
//					payOrder.setPaymethods(0); // 0公司入款，1第三方支付
//					payOrder.setDeposittime(date);
//					payOrder.setAmount(reward);
//					payOrder.setPaystatus(0);
//					payOrder.setStatus(3);
//					payOrder.setOrdertype(1);
//					payOrder.setCwremarks("优惠：邮箱认证系统赠送礼金.");
//					payOrder.setKfremarks("优惠：邮箱认证系统赠送礼金.");
//					payOrder.setCreate_Date(date);
//					payOrder.setUpdate_Date(date);
//					payOrder.setBeforebalance(String.valueOf(beforebalance));
//					payOrder.setLaterbalance(String.valueOf(latrbalance));
//					payOrder = gameTransferService.savePayOrder(payOrder);
//					if(payOrder.getOrdertype()==1 || payOrder.getOrdertype()==2 || payOrder.getOrdertype()==4){
//						Map<String, Object> params2 = new HashMap<String, Object>();
//						params2.put("flaguiid", payOrder.getUiid());
//						XimaFlag ximaflag = ximaFlagService.getObject(params2);
//						if(ximaflag == null){
//							ximaflag = new XimaFlag();
//							ximaflag.setFlaguiid(payOrder.getUiid());
//							ximaflag.setFlagaccount(payOrder.getUaccount());
//							ximaflag.setIsxima(0);
//							ximaflag.setUpdatetime(DateUtil.getStrByDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
//							if(ximaFlagService.save(ximaflag) == false){
//								logger.warn("邮箱认证送礼金，添加记录不洗码数据失败。");
//							}
//						}else{
//							ximaflag.setIsxima(0);
//							if(ximaFlagService.update(ximaflag) == false){
//								logger.warn("邮箱认证送礼金，更新洗码数据。");
//							}
//						}
//					}
//					request.setAttribute("activty38", "恭喜您，由于您成功认证了手机号码和邮箱，系统已自动送您"+reward+"元礼金！");
//					return "/mail/index";
//				}else{
//					request.setAttribute("errorActivty38", "您的手机号码还没有认证通过，手机号码认证通过后您将获得红包好礼！");
//					return "/mail/index";
//				}
//			}
		} catch (Exception e) {
			logger.error("邮件校验失败。");
		}
		return "/mail/index";
	}
	
	public static void main(String[] args) {
		

		String tt = "EF9047BDDCA4A517EB5B4A5A8C7ECD35ED86B229D93408CCDE9254A6E0D27347BDC54AE65AA2280B6B580B0F37C04E3EE91A9BEEBE37B8A22435E6A6CA2BE83F";
		System.out.println(AESEncryptDecryt.decrypt(tt));
	}
}
