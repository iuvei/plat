package com.gameportal.web.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.adver.model.Bulletin;
import com.gameportal.web.adver.service.IBulletinService;
import com.gameportal.web.user.dao.BetLogDao;
import com.gameportal.web.user.dao.MemberXimaDetailDao;
import com.gameportal.web.user.dao.MemberXimaMainDao;
import com.gameportal.web.user.dao.PayOrderDao;
import com.gameportal.web.user.dao.UserInfoDao;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.BetLogTotal;
import com.gameportal.web.user.model.MemberClearingFlag;
import com.gameportal.web.user.model.MemberXimaDetail;
import com.gameportal.web.user.model.MemberXimaMain;
import com.gameportal.web.user.model.MemberXimaSet;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.ProxySet;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserXimaSet;
import com.gameportal.web.user.model.XimaFlag;
import com.gameportal.web.user.service.IMemberXimaMainService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.user.service.IUserPropertyService;
import com.gameportal.web.user.service.IXimaFlagService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.IdGenerator;
import com.gameportal.web.util.WebConstants;

import net.sf.json.JSONObject;

@Service("memberXimaMainServiceImpl")
public class MemberXimaMainServiceImpl implements IMemberXimaMainService {

	private static final Logger logger = Logger.getLogger(MemberXimaMainServiceImpl.class);
	@Resource(name = "memberXimaMainDao")
	private MemberXimaMainDao memberXimaMainDao;
	@Resource(name = "memberXimaDetailDao")
	private MemberXimaDetailDao memberXimaDetailDao;
	@Resource(name = "betLogDao")
	private BetLogDao betLogDao;
	@Resource(name = "userInfoDao")
	private UserInfoDao userInfoDao = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "userPropertyService")
	private IUserPropertyService userPropertyService;
	@Resource(name = "bulletinServiceImpl")
	private IBulletinService bulletinService;
	@Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao;
	@Resource(name="ximaFlagService")
	private IXimaFlagService ximaFlagService;

	@Override
	public List<MemberXimaMain> queryMemberXimaMainList(Map<String, Object> params) {
		return memberXimaMainDao.findMemberXimaList(params);
	}

	@Override
	public long queryMemberXimaMainCount(Map<String, Object> params) {
		return memberXimaMainDao.getRecordCount(params);
	}

	@Override
	public List<BetLogTotal> queryXimaBetTotal(Map<String, Object> params) {
		List<BetLogTotal> list = betLogDao.selectXimaBetTotal(params);
		List<BetLogTotal> result = null;
		if (null != list && list.size() > 0) {
			BetLogTotal betLogTotal = null;
			result = new ArrayList<BetLogTotal>();
			for (BetLogTotal object : list) {
				betLogTotal = object;
				String subAccount = betLogTotal.getAccount();
				// 获取该会员对应的返水比例值
				String platformcode = object.getPlatformcode();// 游戏平台CODE
				Map<String, Object> ximaparams = new HashMap<String, Object>();
				int puiid = betLogTotal.getPuiid();// 上级ID
				// 过滤按天洗码可洗码的代理下线
				if (puiid > 0) {// 0标示公司直属会员
					ximaparams.put("uiid", puiid);
					ximaparams.put("status", "1");
					ProxySet proxySet = memberXimaMainDao.getProxySetObject(ximaparams);
					if (proxySet != null) {
						if (proxySet.getIsximaflag() == 1 && proxySet.getClearingtype() == 2) {
							continue;
						}
					}
				}
				ximaparams.clear();
				ximaparams.put("account", subAccount);
				List<UserInfo> userlist = userInfoDao.queryForPager(ximaparams, 0, 1);
				UserInfo memberinfo = null;
				if (null != userlist && userlist.size() > 0) {
					memberinfo = userlist.get(0);
				}
				if (null != memberinfo) {
					boolean is = true;
					/* 查询是否有单独设置用户洗码比例 */
					ximaparams.clear();
					ximaparams.put("uiid", memberinfo.getUiid());
					ximaparams.put("proxyid", "0");
					ximaparams.put("status", "1");
					UserXimaSet uxinaSet = memberXimaMainDao.getUserXimaSetObject(ximaparams);
					if (uxinaSet != null && StringUtils.isNotBlank(uxinaSet.getXimascale())) {
						double scaleDob = Double.valueOf(uxinaSet.getXimascale());
						double betamontSum = Double.valueOf(object.getValidBetAmountTotal());// 获取投注总额
						double amount = betamontSum * scaleDob;
						betLogTotal.setXimaAmount(
								com.gameportal.web.util.StringUtils.convertNumber(amount) + "#" + scaleDob);
						is = false;
					}
					if (is) {
						MemberXimaSet mxs = memberXimaMainDao.getMemberXimaSetObject(
								WebConstants.getGameMap(platformcode).intValue(), memberinfo.getGrade());
						if (null != mxs && null != memberinfo) {// 没有查询到洗码设置表示不能洗码
							try {
								JSONObject ximasetJson = JSONObject.fromObject(mxs.getScale());
								String dbgamecode = object.getGamecode();
								double scaleDob = 0.00;// 获取洗码比例
								if (ximasetJson.containsKey(dbgamecode)) {
									scaleDob = ximasetJson.getDouble(dbgamecode);// 获取洗码比例
								} else {
									scaleDob = ximasetJson.getDouble("ALL");// 获取洗码比例
								}
								if (scaleDob > 0) {
									if (StringUtils.isNotBlank(object.getValidBetAmountTotal())) {
										double betamontSum = Double.valueOf(object.getValidBetAmountTotal());// 获取投注总额
										double amount = betamontSum * scaleDob;
										betLogTotal.setXimaAmount(
												com.gameportal.web.util.StringUtils.convertNumber(amount) + "#"
														+ scaleDob);
									} else {
										betLogTotal.setXimaAmount("0.00");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							betLogTotal.setXimaAmount("0.00");
						}
					}
				} else {
					betLogTotal.setXimaAmount("0.00");
				}
				result.add(betLogTotal);
			}
		}
		return result;
	}

	@Override
	public long queryXimaBetLogCount(Map<String, Object> params) {
		return betLogDao.selectXimaBetTotalCount(params);
	}

	@Override
	public String saveMemberXima(Map<String, Object> params) {
		PayOrder order = null;
		String[] ximaset = params.get("gameplat").toString().split(",");
		if (ximaset == null || ximaset.length == 0) {
			return "2001";// 没有选择洗码游戏平台
		}
		String account = (String) params.get("account");
		Map<String, Object> selectParams = new HashMap<String, Object>();
		selectParams.put("account", account);
		selectParams.put("ymdstart", params.get("ymdstart"));
		selectParams.put("ymdend", params.get("ymdend"));
		selectParams.put("platformcodeparams", params.get("platformcodeparams"));
		List<Map<String, String>> betlist = betLogDao.selectSum(selectParams);// 统计用户投注记录
		if (null == betlist || betlist.size() <= 0) {
			return "2002";// 没有投注记录
		}
		selectParams.clear();
		selectParams.put("account", account);
		UserInfo userinfo = (UserInfo) userInfoDao.queryForObject(userInfoDao.getSelectQuery(), selectParams);
		MemberXimaMain memberXimaMain = new MemberXimaMain();
		memberXimaMain.setUiid(userinfo.getUiid());
		memberXimaMain.setName(userinfo.getUname());
		memberXimaMain.setYmdstart(params.get("ymdstart").toString());
		memberXimaMain.setYmdend(params.get("ymdend").toString());
		memberXimaMain.setAccount(account);
		memberXimaMain.setLocked(1);// 锁定状态 0未锁定，1锁定
		memberXimaMain.setUpdatetime(DateUtil.getStrYMDHMByDate(new Date()));
		String gpidStr = "";// 游戏平台
		double totalAmount = 0;
		List<MemberXimaDetail> listXmD = new ArrayList<MemberXimaDetail>();
		// 循环需要洗码的游戏厅
		for (int i = 0; i < ximaset.length; i++) {
			String[] key_v = ximaset[i].split("#");
			String gpid = key_v[0]; // 游戏平台对于ID
			String platformcode = key_v[1];// 平台CODE
			// 已结算过得平台
			Map<String, Object> clearMap = new HashMap<String, Object>();
			clearMap.put("uiid", params.get("optuiid"));
			clearMap.put("jstime", params.get("jstimes"));
			clearMap.put("platname", platformcode);
			long counts = betLogDao.selectUserClearnFlag(clearMap);
			if (counts > 0) {
				continue; // 已结算过得平台
			}
			// UserXimaSet
			// 获取该会员对应的返水比例值
			MemberXimaSet mxset = memberXimaMainDao.getMemberXimaSetObject(Integer.valueOf(gpid), userinfo.getGrade());
			if (mxset == null) {
				continue;// 没有找到洗码设置
			}
			MemberXimaDetail mxd = new MemberXimaDetail();
			// 生成指定日期区间的洗码明细记录（返水金额为0）
			mxd.setUiid(userinfo.getUiid());
			mxd.setGpid(Long.valueOf(gpid));
			mxd.setOpttime(DateUtil.getStrYMDHMByDate(new Date()));
			mxd.setOpttype(2); // opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
			mxd.setOptuiid(Long.valueOf(params.get("optuiid").toString()));
			mxd.setOptusername(params.get("optuname") == null ? "" : params.get("optuname").toString());
			mxd.setYmdstart(params.get("ymdstart").toString());
			mxd.setYmdend(params.get("ymdend").toString());
			double ptTotalAmount = 0;
			StringBuffer log = new StringBuffer();// 洗码log
			try {
				JSONObject ximasetJson = JSONObject.fromObject(mxset.getScale());// 获取洗码比例设置
				for (Map m : betlist) {
					if (platformcode.equals(m.get("platformcode"))) {// 验证用户要洗码的平台是否存在游戏记录
//						String dbgamecode = m.get("gamecode").toString();
					    Object gameCodeObj = m.get("gamecode");
						double scaleDob = 0.00;
						if (gameCodeObj!=null && ximasetJson.containsKey(gameCodeObj.toString())) {// 验证是否有该游戏的洗码比例设置
							scaleDob = ximasetJson.getDouble(gameCodeObj.toString());
						} else {// 如果没有读取其他
							scaleDob = ximasetJson.getDouble("ALL");
						}
						Map<String, Object> ximaparams = new HashMap<String, Object>();
						ximaparams.put("uiid", userinfo.getUiid());
						ximaparams.put("proxyid", "0");
						ximaparams.put("status", "1");
						UserXimaSet uxinaSet = memberXimaMainDao.getUserXimaSetObject(ximaparams);
						if (null != uxinaSet && StringUtils.isNotBlank(uxinaSet.getXimascale())) {
							scaleDob = Double.valueOf(uxinaSet.getXimascale());
						}
						if (scaleDob > 0) {// 表示设置了洗码比例
							String betamontTotal = m.get("betamontSum").toString();
							if (!StringUtils.isNotBlank(betamontTotal) || Double.valueOf(betamontTotal) <= 0) {
								continue;
							}
							double betamontSum = Double.valueOf(m.get("betamontSum").toString());// 获取投注总额
							double amount = betamontSum * scaleDob;
							totalAmount = totalAmount + amount;
							ptTotalAmount = ptTotalAmount + amount;// 单个平台洗码总金额
							String dbgamecode = m.get("gamecode")==null?"null":m.get("gamecode").toString();
							log.append("平台：").append(platformcode).append(" 游戏：")
									.append(dbgamecode + "-" + m.get("gamename")).append("->总投注额：").append(betamontSum)
									.append("->洗码比例：").append(scaleDob).append("\n");
						}
					}
				}
				if (ptTotalAmount > 0) {// 单个平台洗码成功
					gpidStr += platformcode + ",";
					mxd.setParamlog(log.toString());
					log = null;
					mxd.setAmount(com.gameportal.web.util.StringUtils.convertNumber(ptTotalAmount));
					listXmD.add(mxd);
					// 添加平台结算标识记录
					MemberClearingFlag entity = new MemberClearingFlag();
					entity.setFlaguiid(userinfo.getUiid().intValue());
					entity.setFlagtime(params.get("jstimes").toString());
					entity.setPlatname(platformcode);
					memberXimaMainDao.insertMemberClearingFlag(entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		// 添加洗码记录
		if (!"".equals(gpidStr) && totalAmount > 0) {
			memberXimaMain.setGpid(gpidStr);
			memberXimaMain.setTotal(com.gameportal.web.util.StringUtils.convertNumber(totalAmount));
			memberXimaMainDao.saveOrUpdate(memberXimaMain);
			if (listXmD.size() > 0) {
				for (MemberXimaDetail obj : listXmD) {
					memberXimaDetailDao.saveOrUpdate(obj);
				}
			}
			// 更新钱包余额
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userinfo.getUiid(), 1);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			BigDecimal latrbalance = accountMoney.getTotalamount().add(new BigDecimal(totalAmount)).setScale(2, BigDecimal.ROUND_HALF_UP);
			accountMoney.setTotalamount(latrbalance);
			accountMoney.setUpdateDate(new Date());
			userInfoService.updateAccountMoneyObj(accountMoney);
			// 新增帐变记录
			PayOrderLog log = new PayOrderLog();
			log.setUiid(userinfo.getUiid());
			log.setAmount(new BigDecimal(totalAmount));
			log.setType(8);
			log.setWalletlog(beforebalance + ">>>" + latrbalance);
			log.setRemark(gpidStr.substring(0, gpidStr.length() - 1) + "平台" + params.get("jstimes").toString() + "洗码");
			log.setCreatetime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
			userPropertyService.insertPayLog(log);
			// 新增赠送订单
			order = new PayOrder();
			order.setPoid(IdGenerator.genOrdId16("REWARD"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setPaytyple(2);
			order.setDeposittime(new Date());
			order.setCreate_Date(new Date());
			order.setCwopttime(new Date());
			order.setKfopttime(new Date());
			order.setAmount(new BigDecimal(totalAmount));
			order.setPaystatus(0);
			order.setStatus(3);
			order.setOrdertype(3);
			order.setUaccount(userinfo.getAccount());
			order.setUrealname(userinfo.getUname());
			order.setUiid(userinfo.getUiid());
			order.setCreate_Date(new Date());
			order.setUpdate_Date(new Date());
			order.setKfremarks("领取"+params.get("jstimes").toString()+"返水");
			order.setCwremarks("领取"+params.get("jstimes").toString()+"返水");
			order.setBeforebalance(beforebalance);
			order.setLaterbalance(latrbalance);
			payOrderDao.save(order);
			
			return "2000";
		}
		return "2003";
	}

	@Override
	public String saveMemberXimaYesterDay(Map<String, Object> params, List<String> accounts) {
		PayOrder order = null;
		String[] ximaset = params.get("gameplat").toString().split(",");
		if (ximaset == null || ximaset.length == 0) {
			return "2001";// 没有选择洗码游戏平台
		}
		String startDate = DateUtil.FormatDate(DateUtil.addDay(new Date(), -1));
		for (String account : accounts) {// 循环洗码
			try {
				Map<String, Object> selectParams = new HashMap<String, Object>();
				selectParams.put("account", account);
				selectParams.put("ymdstart", params.get("ymdstart"));
				selectParams.put("ymdend", params.get("ymdend"));
				selectParams.put("platformcodeparams", params.get("platformcodeparams"));
				List<Map<String, String>> betlist = betLogDao.selectSum(selectParams);// 统计用户投注记录
				if (null == betlist || betlist.size() <= 0) {
					continue;
				}
				selectParams.clear();
				selectParams.put("account", account);
				UserInfo userinfo = (UserInfo) userInfoDao.queryForObject(userInfoDao.getSelectQuery(), selectParams);
				if(userinfo ==null){
					continue;
				}
				MemberXimaMain memberXimaMain = new MemberXimaMain();
				memberXimaMain.setUiid(userinfo.getUiid());
				memberXimaMain.setName(userinfo.getUname());
				memberXimaMain.setYmdstart(params.get("ymdstart").toString());
				memberXimaMain.setYmdend(params.get("ymdend").toString());
				memberXimaMain.setAccount(account);
				memberXimaMain.setLocked(1);// 锁定状态 0未锁定，1锁定
				memberXimaMain.setUpdatetime(DateUtil.getStrYMDHMByDate(new Date()));
				String gpidStr = "";// 游戏平台
				double totalAmount = 0;
				List<MemberXimaDetail> listXmD = new ArrayList<MemberXimaDetail>();
				// 循环需要洗码的游戏厅
				for (int i = 0; i < ximaset.length; i++) {
					String[] key_v = ximaset[i].split("#");
					String gpid = key_v[0]; // 游戏平台对于ID
					String platformcode = key_v[1];// 平台CODE
					// 已结算过得平台
					Map<String, Object> clearMap = new HashMap<String, Object>();
					clearMap.put("uiid", userinfo.getUiid());
					clearMap.put("jstime", params.get("jstimes"));
					clearMap.put("platname", platformcode);
					long counts = betLogDao.selectUserClearnFlag(clearMap);
					if (counts > 0) {
						continue; // 已结算过得平台
					}
					// UserXimaSet
					// 获取该会员对应的返水比例值
					MemberXimaSet mxset = memberXimaMainDao.getMemberXimaSetObject(Integer.valueOf(gpid),
							userinfo.getGrade());
					if (mxset == null) {
						continue;// 没有找到洗码设置
					}
					MemberXimaDetail mxd = new MemberXimaDetail();
					// 生成指定日期区间的洗码明细记录（返水金额为0）
					mxd.setUiid(userinfo.getUiid());
					mxd.setGpid(Long.valueOf(gpid));
					mxd.setOpttime(DateUtil.getStrYMDHMByDate(new Date()));
					mxd.setOpttype(2); // opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
					mxd.setOptuiid(userinfo.getUiid());
					mxd.setOptusername(userinfo.getUname());
					mxd.setYmdstart(params.get("ymdstart").toString());
					mxd.setYmdend(params.get("ymdend").toString());
					double ptTotalAmount = 0;
					StringBuffer log = new StringBuffer();// 洗码log
					
						JSONObject ximasetJson = JSONObject.fromObject(mxset.getScale());// 获取洗码比例设置
						for (Map m : betlist) {
							if (platformcode.equals(m.get("platformcode"))) {// 验证用户要洗码的平台是否存在游戏记录
	//							String dbgamecode = m.get("gamecode").toString();
							    Object gameCodeObj = m.get("gamecode");
								double scaleDob = 0.00;
								if (gameCodeObj!=null && ximasetJson.containsKey(gameCodeObj.toString())) {// 验证是否有该游戏的洗码比例设置
									scaleDob = ximasetJson.getDouble(gameCodeObj.toString());
								} else {// 如果没有读取其他
									scaleDob = ximasetJson.getDouble("ALL");
								}
								Map<String, Object> ximaparams = new HashMap<String, Object>();
								ximaparams.put("uiid", userinfo.getUiid());
								ximaparams.put("proxyid", "0");
								ximaparams.put("status", "1");
								UserXimaSet uxinaSet = memberXimaMainDao.getUserXimaSetObject(ximaparams);
								if (null != uxinaSet && StringUtils.isNotBlank(uxinaSet.getXimascale())) {
									scaleDob = Double.valueOf(uxinaSet.getXimascale());
								}
								if (scaleDob > 0) {// 表示设置了洗码比例
									String betamontTotal = m.get("betamontSum").toString();
									if (StringUtils.isBlank(betamontTotal) || Double.valueOf(betamontTotal) <= 0) {
										continue;
									}
									double betamontSum = Double.valueOf(m.get("betamontSum").toString());// 获取投注总额
									double amount = betamontSum * scaleDob;
									totalAmount = totalAmount + amount;
									ptTotalAmount = ptTotalAmount + amount;// 单个平台洗码总金额
									//String dbgamecode = m.get("gamecode")==null?"null":m.get("gamecode").toString();
									log.append("平台：").append(platformcode).append(" 游戏：")
											.append(m.get("gamename")).append("->总投注额：")
											.append(betamontSum).append("->洗码比例：").append(scaleDob).append("\n");
								}
							}
						}
						if (ptTotalAmount > 0) {// 单个平台洗码成功
							gpidStr += platformcode + ",";
							mxd.setParamlog(log.toString());
							log = null;
							mxd.setAmount(com.gameportal.web.util.StringUtils.convertNumber(ptTotalAmount));
							listXmD.add(mxd);
							// 添加平台结算标识记录
							MemberClearingFlag entity = new MemberClearingFlag();
							entity.setFlaguiid(userinfo.getUiid().intValue());
							entity.setFlagtime(params.get("jstimes").toString());
							entity.setPlatname(platformcode);
							memberXimaMainDao.insertMemberClearingFlag(entity);
						}
				}
				// 添加洗码记录
				if (!"".equals(gpidStr) && totalAmount > 0) {
					memberXimaMain.setGpid(gpidStr);
					memberXimaMain.setTotal(com.gameportal.web.util.StringUtils.convertNumber(totalAmount));
					memberXimaMainDao.saveOrUpdate(memberXimaMain);
					if (listXmD.size() > 0) {
						for (MemberXimaDetail obj : listXmD) {
							memberXimaDetailDao.saveOrUpdate(obj);
						}
					}
					// 更新钱包余额
					AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userinfo.getUiid(),null);
					BigDecimal beforebalance = accountMoney.getTotalamount();
					BigDecimal latrbalance = accountMoney.getTotalamount().add(new BigDecimal(totalAmount)).setScale(2, BigDecimal.ROUND_HALF_UP);
					accountMoney.setTotalamount(new BigDecimal(totalAmount));
					accountMoney.setUpdateDate(new Date());
					
					userInfoService.updateTotalamount(accountMoney);
					//新增积分
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("startDate", startDate);
					map.put("endDate", startDate);
					map.put("account", account);
					long betRecord = betLogDao.sumBetTotalAmount(map);
					int integral =((int)betRecord)/10;
					accountMoney.setTotalamount(latrbalance);
					accountMoney.setIntegral(accountMoney.getIntegral()+integral);
					userInfoService.updateAccountMoneyObj(accountMoney);
					
					// 新增帐变记录
					PayOrderLog log = new PayOrderLog();
					log.setUiid(userinfo.getUiid());
					log.setAmount(new BigDecimal(totalAmount));
					log.setType(8);
					log.setWalletlog(beforebalance + ">>>" + latrbalance);
					log.setRemark(
							gpidStr.substring(0, gpidStr.length() - 1) + "平台" + params.get("jstimes").toString() + "洗码");
					log.setCreatetime(DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT));
					userPropertyService.insertPayLog(log);
					// 新增赠送订单
					order = new PayOrder();
					order.setPoid(IdGenerator.genOrdId16("REWARD"));
					order.setPlatformorders(IdGenerator.genOrdId16(""));
					order.setPaytyple(2);
					order.setDeposittime(new Date());
					order.setCreate_Date(new Date());
					order.setCwopttime(new Date());
					order.setKfopttime(new Date());
					order.setAmount(new BigDecimal(totalAmount));
					order.setPaystatus(0);
					order.setStatus(3);
					order.setOrdertype(3);
					order.setUaccount(userinfo.getAccount());
					order.setUrealname(userinfo.getUname());
					order.setUiid(userinfo.getUiid());
					order.setCreate_Date(new Date());
					order.setUpdate_Date(new Date());
					order.setKfremarks("领取"+params.get("jstimes").toString()+"返水");
					order.setCwremarks("领取"+params.get("jstimes").toString()+"返水");
					order.setBeforebalance(beforebalance);
					order.setLaterbalance(latrbalance);
					payOrderDao.save(order);
					
					/*设置用户是否可洗码*/
					XimaFlag ximaflag = ximaFlagService.getNewestXimaFlag(order.getUiid());
					if(ximaflag == null || ximaflag.getIsxima() !=1){
						ximaflag = new XimaFlag();
						ximaflag.setFlaguiid(order.getUiid());
						ximaflag.setFlagaccount(order.getUaccount());
						ximaflag.setIsxima(1);
						ximaflag.setRemark(order.getCwremarks());
						ximaFlagService.save(ximaflag);
					}
				}
			} catch (Exception e) {
				logger.error("会员{"+account+"}洗码失败。",e);
			}
		}
		Bulletin bu = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("btitle", "返水公告");
		List<Bulletin> list = bulletinService.queryAllBulletin(map);
		if (list != null && list.size() > 0) {
			bu = list.get(0);
			bu.setTitle("返水公告");
			bu.setEditTime(new Date());
			bu.setStatus(1);
			bu.setContent(
					"返水公告：不计输赢天天返水无上限！北京时间" + params.get("jstimes") + "(00:00至23:59)的返水额度已添加至您的账号内，请查收～祝亲们游戏开心 ，手气棒棒！");
			bulletinService.updateBulletin(bu);
		} else {
			bu = new Bulletin();
			bu.setTitle("返水公告");
			bu.setEditTime(new Date());
			bu.setStatus(1);
			bu.setContent(
					"返水公告：不计输赢天天返水无上限!北京时间" + params.get("jstimes") + "(00:00至23:59)的返水额度已添加至您的账号内，请查收～祝亲们游戏开心 ，手气棒棒!");
			bulletinService.saveBulletin(bu);
		}
		logger.info(params.get("jstimes") + "洗码完成添加公告");
		return "2000";
	}

	/**
	 * 代理天洗码
	 * 
	 * @param accounts
	 * @return
	 */
	@Override
	public String saveProxyDayClearing(Map<String, Object> params, List<ProxySet> proxyList) throws Exception {
		UserInfo userInfo = null;
		PayOrder order = null;
		BigDecimal reward = BigDecimal.ZERO;
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(params);
		// 循环单个用户
		for (ProxySet proxy : proxyList) {
			userInfo = (UserInfo)userInfoDao.findById(Long.valueOf(proxy.getUiid()));
			if (userInfo == null) {
				continue;
			}
			logger.info("代理用户："+userInfo.getAccount()+"按天洗码开始，");
			// 统计用户投注记录
			map.put("flag", 1);
			map.put("puiid", userInfo.getUiid());
			String betDate = DateUtil.FormatDate(DateUtil.addDay(new Date(), -1)); // 昨天日期
			map.put("startDate", betDate);
			map.put("endDate", betDate);
			long betRecord = betLogDao.sumBetTotalAmount(map);
			logger.info("下级投注量："+betRecord);
			if (betRecord <= 0) {
				continue;
			}
			order = new PayOrder();
			reward = new BigDecimal(betRecord).multiply(new BigDecimal(proxy.getXimascale())).setScale(2,BigDecimal.ROUND_HALF_UP);
			order.setKfremarks("下级投注返水：" + betRecord + "x"+proxy.getXimascale()+"=" + reward + "元");
			if(reward.doubleValue() <=0){
				continue;
			}
			AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
			BigDecimal beforebalance = accountMoney.getTotalamount();
			BigDecimal latrbalance = accountMoney.getTotalamount().add(reward);
			String date = DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT);
			// 新增帐变记录
			PayOrderLog log = new PayOrderLog();
			log.setUiid(userInfo.getUiid());
			log.setAmount(reward);
			log.setType(2);
			log.setRemark("下级投注返水");
			log.setWalletlog(beforebalance + ">>>" + latrbalance);
			log.setCreatetime(date);
			userPropertyService.insertPayLog(log);
			// 调整钱包额度
			accountMoney.setTotalamount(reward);
			accountMoney.setUpdateDate(new Date());
			userInfoService.updateTotalamount(accountMoney);
			// 新增赠送订单
			order.setPoid(IdGenerator.genOrdId16("REWARD"));
			order.setPlatformorders(IdGenerator.genOrdId16(""));
			order.setPaytyple(2);
			order.setDeposittime(new Date());
			order.setCreate_Date(new Date());
			order.setCwopttime(new Date());
			order.setKfopttime(new Date());
			order.setAmount(reward);
			order.setPaystatus(0);
			order.setStatus(3);
			order.setOrdertype(3);
			order.setUaccount(userInfo.getAccount());
			order.setUrealname(userInfo.getUname());
			order.setUiid(userInfo.getUiid());
			order.setCreate_Date(new Date());
			order.setUpdate_Date(new Date());
			order.setCwremarks(order.getKfremarks());
			order.setBeforebalance(beforebalance);
			order.setLaterbalance(latrbalance);
			payOrderDao.save(order);
		}
		return "2000";
	}

	@Override
	public List<ProxySet> selectProxyDayClearing(Map<String, Object> params) {
		return memberXimaMainDao.selectProxyDayClearing(params);
	}
	
	public static void main(String[] args) {
		System.out.println(new BigDecimal(1678).multiply(new BigDecimal("0.002")).setScale(2,BigDecimal.ROUND_HALF_UP));
	}
}
