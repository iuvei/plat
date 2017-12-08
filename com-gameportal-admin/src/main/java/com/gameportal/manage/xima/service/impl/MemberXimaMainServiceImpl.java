package com.gameportal.manage.xima.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercent;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.BetLogDao;
import com.gameportal.manage.member.dao.MemberInfoDao;
import com.gameportal.manage.member.dao.UserXimaSetDao;
import com.gameportal.manage.member.model.MemberClearingFlag;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.pay.model.PayOrder;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.user.dao.AccountMoneyDao;
import com.gameportal.manage.user.dao.UserInfoDao;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.manage.util.DateUtil;
import com.gameportal.manage.util.DateUtil2;
import com.gameportal.manage.util.HttpUtil;
import com.gameportal.manage.util.PropertyContext;
import com.gameportal.manage.xima.dao.MemberXimaDetailDao;
import com.gameportal.manage.xima.dao.MemberXimaMainDao;
import com.gameportal.manage.xima.dao.MemberXimaSetDao;
import com.gameportal.manage.xima.model.MemberXimaDetail;
import com.gameportal.manage.xima.model.MemberXimaMain;
import com.gameportal.manage.xima.model.MemberXimaSet;
import com.gameportal.manage.xima.service.IMemberXimaMainService;

@Service("memberXimaMainServiceImpl")
public class MemberXimaMainServiceImpl implements IMemberXimaMainService {

	private static final Logger logger = Logger
			.getLogger(MemberXimaMainServiceImpl.class);
	private static Properties prop = PropertyContext
			.PropertyContextFactory("qiantai.properties").getPropertie();
	@Resource(name = "userInfoDao")
	private UserInfoDao userInfoDao = null;
	
	@Resource(name = "memberInfoDao")
	private MemberInfoDao memberInfoDao = null;

	@Resource
	private MemberXimaMainDao memberXimaMainDao;
	@Resource
	private MemberXimaDetailDao memberXimaDetailDao;
	@Resource
	private MemberXimaSetDao memberXimaSetDao;
	@Resource
	private BetLogDao betLogDao;
	
	@Resource(name = "accountMoneyDao")
	private AccountMoneyDao accountMoneyDao = null;
	
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	
	@Resource(name = "userXimaSetDao")
	private UserXimaSetDao userXimaSetDao;

	public MemberXimaMainServiceImpl() {
		super();
	}

	@Override
	public String saveOrUpdateMemberXimaMain(Map<String, Object> params) {
		String[] ximaset = params.get("gameplat").toString().split(",");
		if(ximaset == null || ximaset.length == 0){
			return "2001";//没有选择洗码游戏平台
		}
		List<String> accountList = (List<String>)params.get("account");
		//循环用户
		for(String account : accountList){
			Map<String, Object> selectParams = new HashMap<String, Object>();
			String accountstr = account;
			selectParams.put("account", accountstr);
			selectParams.put("ymdstart", params.get("ymdstart"));
			selectParams.put("ymdend", params.get("ymdend"));
			selectParams.put("platformcodeparams", params.get("platformcodeparams"));
			List<Map<String, String>> betlist = betLogDao.selectSum(selectParams);//统计用户投注记录
			if(null == betlist || betlist.size() <=0){
				continue;
			}
			selectParams.clear();
			selectParams.put("account", accountstr);
			UserInfo userList = userInfoService.queryUserInfo2(selectParams);
			MemberXimaMain memberXimaMain = new MemberXimaMain();
			memberXimaMain.setUiid(userList.getUiid());
			memberXimaMain.setName(userList.getUname());
			memberXimaMain.setYmdstart(params.get("ymdstart").toString());
			memberXimaMain.setYmdend(params.get("ymdend").toString());
			memberXimaMain.setAccount(accountstr);
			memberXimaMain.setLocked(0);// 锁定状态 0未锁定，1锁定
			memberXimaMain.setUpdatetime(DateUtil.getStrYMDHMByDate(new Date()));
			String gpidStr = "";//游戏平台
			double totalAmount = 0;
			List<MemberXimaDetail> listXmD = new ArrayList<MemberXimaDetail>();
			//循环需要洗码的游戏厅
			for(int i =0;i<ximaset.length;i++){
				String[] key_v = ximaset[i].split("#");
				String gpid = key_v[0]; //游戏平台对于ID
				String platformcode = key_v[1];//平台CODE
				//UserXimaSet
				// 获取该会员对应的返水比例值
				MemberXimaSet mxset = memberXimaSetDao.queryByUiid(Integer.valueOf(gpid),userList.getGrade());
				if(mxset == null){
					continue;//没有找到洗码设置
				}
				MemberXimaDetail mxd = new MemberXimaDetail();
				// 生成指定日期区间的洗码明细记录（返水金额为0）
				mxd.setUiid(userList.getUiid());
				mxd.setGpid(Long.valueOf(gpid));
				mxd.setOpttime(DateUtil.getStrYMDHMByDate(new Date()));
				mxd.setOpttype(2); // opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
				mxd.setOptuiid(Long.valueOf(params.get("optuiid").toString()));
				mxd.setOptusername(params.get("optuname").toString());
				mxd.setYmdstart(params.get("ymdstart").toString());
				mxd.setYmdend(params.get("ymdend").toString());
				double ptTotalAmount=0;
				StringBuffer log = new StringBuffer();//洗码log
				try {
					JSONObject ximasetJson = JSONObject.fromObject(mxset.getScale());//获取洗码比例设置
					for(Map m : betlist){
						if(platformcode.equals(m.get("platformcode"))){//验证用户要洗码的平台是否存在游戏记录
							String dbgamecode = m.get("gamecode").toString();
							double scaleDob = 0.00;
							if(ximasetJson.containsKey(dbgamecode)){//验证是否有该游戏的洗码比例设置
								scaleDob = ximasetJson.getDouble(dbgamecode);
							}else{//如果没有读取其他
								scaleDob = ximasetJson.getDouble("ALL");
							}
							Map<String, Object> ximaparams = new HashMap<String, Object>();
							ximaparams.put("uiid", userList.getUiid());
							ximaparams.put("proxyid", "0");
							ximaparams.put("status", "1");
							UserXimaSet uxinaSet = userXimaSetDao.getObject(ximaparams);
							if(null != uxinaSet && StringUtils.isNotBlank(uxinaSet.getXimascale())){
								scaleDob = Double.valueOf(uxinaSet.getXimascale());
							}
							if(scaleDob > 0){//表示设置了洗码比例
								String betamontTotal = m.get("betamontSum").toString();
								if(!StringUtils.isNotBlank(betamontTotal) || Double.valueOf(betamontTotal) <= 0){
									continue;
								}
								double betamontSum = Double.valueOf(m.get("betamontSum").toString());//获取投注总额
								double amount = betamontSum * scaleDob;
								totalAmount = totalAmount + amount;
								ptTotalAmount = ptTotalAmount+amount;//单个平台洗码总金额
								log.append("平台：").append(platformcode).append(" 游戏：").append(dbgamecode+"-"+m.get("gamename")).append("->总投注额：").append(betamontSum).append("->洗码比例：").append(scaleDob).append("\n");
							}
						}
					}
					if(ptTotalAmount > 0){//单个平台洗码成功
						gpidStr += platformcode+",";
						mxd.setParamlog(log.toString());
						log = null;
						mxd.setAmount(com.gameportal.manage.util.StringUtils.convertNumber(ptTotalAmount));
						listXmD.add(mxd);
						//平台添加结算标识
						MemberClearingFlag entity = new MemberClearingFlag();
						entity.setFlaguiid(userList.getUiid().intValue());
						entity.setFlagtime(params.get("jstimes").toString());
						entity.setPlatname(platformcode);
						memberInfoDao.insertMemberClearingFlag(entity);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
			//添加洗码记录
			if(!"".equals(gpidStr) && totalAmount > 0){
				memberXimaMain.setGpid(gpidStr);
				memberXimaMain.setTotal(com.gameportal.manage.util.StringUtils.convertNumber(totalAmount));
				memberXimaMainDao.saveOrUpdate(memberXimaMain);
				if(listXmD.size()>0){
					for(MemberXimaDetail obj : listXmD){
						memberXimaDetailDao.saveOrUpdate(obj);
					}
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		Integer s = null;
		System.out.println(s.toString());
	}

	@Override
	public boolean deleteMemberXimaMain(Long id) {
		return memberXimaMainDao.delete(id);
	}

	@Override
	public Long queryMemberXimaMainCount(Map<String, Object> params) {
		return memberXimaMainDao.getRecordCount(params);
	}

	@Override
	public List<MemberXimaMain> queryMemberXimaMain(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 100;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return memberXimaMainDao.getList(params);
	}
	
	@Override
	public List<MemberXimaMain> queryMemberXimaMain(Map<String, Object> params) {
			return memberXimaMainDao.getList(params);
	}

	@Override
	public boolean clearXima(MemberXimaMain mxm, Date ymdstartDate,
			Date ymdendDate, SystemUser user) {
		if (null == mxm || null == ymdendDate || null == ymdstartDate
				|| null == user) {
			return false;
		}
		MemberXimaDetail mxd = new MemberXimaDetail();
		// 生成指定日期区间的洗码明细记录（返水金额为0）
		//mxd.setAmount(0L);
		//mxd.setGpid(mxm.getGpid());
		//mxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		mxd.setOpttype(1); // opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		mxd.setOptuiid(user.getUserId());
		mxd.setOptusername(user.getRealName());
		mxd.setUiid(mxm.getUiid());
		//mxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		//mxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
		mxd.setParamlog("<|>返水金额 = 投注金额 x 返水比例值<|>洗码清零操作");
		// 保存洗码明细记录
		return memberXimaDetailDao.saveOrUpdate(mxd);
	}

	@Override
	public boolean forceXima(MemberXimaMain mxm, Date ymdstartDate,
			Date ymdendDate, SystemUser user) {
		if (null == mxm || null == ymdendDate || null == ymdstartDate
				|| null == user) {
			return false;
		}
		// 获取该会员对应的返水比例值
		MemberXimaSet mxs = memberXimaSetDao.queryByUiid(0,0);
		if (null == mxs) {
			return false;
		}
		MemberXimaDetail mxd = new MemberXimaDetail();
		// 查询指定日期区间中该会员在各游戏平台的投注记录，计算总投注额(按游戏平台分别计算)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", mxm.getAccount());
		params.put("ymdstart", new java.sql.Date(ymdstartDate.getTime()));
		params.put("ymdend", new java.sql.Date(ymdendDate.getTime()));
		params.put("platformcode", mxm.getGpname().toUpperCase());
		Long sum = betLogDao.selectBackAmount(params);
		sum = (null == sum ? 0L : sum);
		// 根据公式计算返水金额
		//mxd.setAmount((long) (mxs.getScale().doubleValue() * sum));
		//mxd.setGpid(mxm.getGpid());
		//mxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		mxd.setOpttype(2);// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		mxd.setOptuiid(user.getUserId());
		mxd.setOptusername(user.getRealName());
		mxd.setUiid(mxm.getUiid());
		//mxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		//mxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
//		mxd.setParamlog("<|>返水金额 = 投注金额 x 返水比例值<|>" + mxd.getAmount() + " = "
//				+ sum + " x " + mxs.getScale().doubleValue());
		// 保存洗码明细记录
		return memberXimaDetailDao.saveOrUpdate(mxd);
	}

	@Override
	public boolean normalXima(MemberXimaMain mxm, Date ymdstartDate,
			Date ymdendDate) {
		if (null == mxm || null == ymdendDate || null == ymdstartDate) {
			return false;
		}
		// 获取该会员对应的返水比例值
		MemberXimaSet mxs = memberXimaSetDao.queryByUiid(0,0);
		if (null == mxs) {
			return false;
		}
		MemberXimaDetail mxd = new MemberXimaDetail();
		// 查询指定日期区间中该会员在各游戏平台的投注记录，计算总投注额(按游戏平台分别计算)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", mxm.getAccount());
		params.put("ymdstart", new java.sql.Date(ymdstartDate.getTime()));
		params.put("ymdend", new java.sql.Date(ymdendDate.getTime()));
		params.put("platformcode", mxm.getGpname().toUpperCase());
		Long sum = betLogDao.selectBackAmount(params);
		sum = (null == sum ? 0L : sum);
		// 根据公式计算返水金额
		//mxd.setAmount((long) (mxs.getScale().doubleValue() * sum));
		//mxd.setGpid(mxm.getGpid());
		//mxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		mxd.setOpttype(0);// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		mxd.setOptuiid(mxm.getUiid());
		mxd.setOptusername(mxm.getName());
		mxd.setUiid(mxm.getUiid());
		//mxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		//mxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
		//mxd.setParamlog("<|>返水金额 = 投注金额 x 返水比例值<|>" + mxd.getAmount() + " = "
		//		+ sum + " x " + mxs.getScale().doubleValue());
		// 保存洗码明细记录
		return memberXimaDetailDao.saveOrUpdate(mxd);
	}

	@Override
	public String checkBy(MemberXimaMain mxm) throws Exception{
		boolean flag = true;
		Map<String, String> smsparams = new HashMap<String, String>();
		smsparams.put("channel","1");
		smsparams.put("spuname","钱包余额变更通知");
		AccountMoney am = null;
		String expStr = "审核失败";
		Date now = new Date();
		if(mxm.getLocked() == 1){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uiid", mxm.getUiid());
			List<AccountMoney> amList = accountMoneyDao.getList(params);
			if (null == amList || amList.size() <= 0) {
				am = new AccountMoney();
				am.setTotalamount(BigDecimal.ZERO);
				am.setUiid(mxm.getUiid());
				am.setStatus(1);
				am.setCreateDate(now);
				am.setUpdateDate(now);
				am = (AccountMoney) accountMoneyDao.save(am);
			} else {
				am = amList.get(0);
			}
			am.setUpdateDate(now);
			
			double amoney = Double.valueOf(mxm.getTotal());
			am.setTotalamount(new BigDecimal(amoney).setScale(2, BigDecimal.ROUND_HALF_UP));
			flag = accountMoneyDao.updateTotalamount(am);
		}
		if(flag){
			if(memberXimaMainDao.saveOrUpdate(mxm)){
				if(mxm.getLocked() == 1){
					expStr = "洗码审核成功。";
					smsparams.put("content", "尊敬的会员，您的" + am.getTotalamount()
							+ "元洗码已经到账，请留意银钱包余额变化，如有问题请联系客服。");
				}
			}
		}
		if (smsparams.containsKey("content")) {
			// 下发通知我短信
//			smsparams.put("content",
//					URLEncoder.encode(smsparams.get("content"), "utf-8"));
			this.sendSms(mxm.getUiid(), smsparams);
		}
		if (flag == false) {
			throw new Exception(expStr);
		}
		return null;
	}
	
	private void sendSms(Long uiid, Map<String, String> params) {
//		try {
//			UserInfo user = (UserInfo) userInfoDao.findById(uiid);
//			String smsHttpUrlPrefix = prop
//					.getProperty("inter.sms.httpUrlPrefix");
//			String rtn = HttpUtil.doPost(smsHttpUrlPrefix
//					+ "api/sms/sendOneSMS/" + user.getPhone() + ".do", params);
//			logger.info(rtn);
//		} catch (Exception e) {
//			logger.error("Exception: ", e);
//		}
	}
}
