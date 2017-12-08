package com.gameportal.web.game.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.api.AGApiResponse;
import com.gameportal.web.api.sa.CheckOrderId;
import com.gameportal.web.api.sa.CreditBalanceDV;
import com.gameportal.web.api.sa.DebitBalanceDV;
import com.gameportal.web.game.dao.GameAccountDao;
import com.gameportal.web.game.dao.GameTransferDao;
import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.game.util.ApiException;
import com.gameportal.web.user.dao.PayOrderDao;
import com.gameportal.web.user.dao.UserPropertyDao;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserGrade;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.WithdrawalcountLog;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.RandomUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Service("gameTransferServiceImpl")
@SuppressWarnings("all")
public class GameTransferServiceImpl implements IGameTransferService {

	@Resource(name = "gameAccountDao")
	private GameAccountDao gameAccountDao = null;
	@Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao = null;
	@Resource(name = "gameTransferDao")
	private GameTransferDao gameTransferDao = null;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService = null;
	@Resource(name = "userInfoServiceImpl")
	private IUserInfoService userInfoService = null;
	@Resource(name = "gamePlatformHandlerMap")
	private Map<String, IGameServiceHandler> gamePlatformHandlerMap = null;

	@Resource(name = "userPropertyDao")
	private UserPropertyDao userPropertyDao;
	private static Logger logger = Logger.getLogger(GameTransferServiceImpl.class);

	public GameTransferServiceImpl() {
		super();
	}

	@Override
	public List<GameAccount> queryGameAccountList(Long uiid, Integer startNo, Integer pagaSize) {
		return queryGameAccountList(uiid, null, startNo, pagaSize);
	}

	@Override
	public List<GameAccount> queryGameAccountList(Long uiid, Integer status, Integer startNo, Integer pagaSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uiid", uiid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			map.put("status", status);
		}
		map.put("sortColumns", " money desc");
		List<GameAccount> gameAccountList = gameAccountDao.queryForPager(map, startNo, pagaSize);
		return StringUtils.isNotBlank(ObjectUtils.toString(gameAccountList)) && gameAccountList.size() > 0
				? gameAccountList : null;
	}

	@Override
	public PayOrder savePayOrder(PayOrder payOrder) throws Exception {
		payOrder = (PayOrder) payOrderDao.save(payOrder);
		return StringUtils.isNotBlank(ObjectUtils.toString(payOrder.getID())) ? payOrder : null;
	}

	@Override
	public List<PayOrder> getPayOrder(Map<String, Object> param) throws Exception {
		return payOrderDao.getPayOrder(param);
	}

	@Override
	public GameAccount queryGameAccountObj(Long uiid, Long gaid, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uiid", uiid);
		map.put("gpid", gaid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			map.put("status", status);
		}
		map.put("sortColumns", " money desc");
		GameAccount gameAccount = (GameAccount) gameAccountDao.queryForObject(gameAccountDao.getSelectQuery(), map);
		return gameAccount;
	}

	@Override
	public String updateAGGameTransfer(Map<String, Object> params) throws ApiException {
		String transferOut = (String) params.get("transferOut");
		String transferIn = (String) params.get("transferIn");
		Integer transferNum = Integer.valueOf(params.get("transferNum").toString());
		String billno = (String) params.get("billno");
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GamePlatform gamePlatform = (GamePlatform) params.get("gamePlatform");
		IGameServiceHandler gameInstance = (IGameServiceHandler) params.get("gameInstance");
		GameTransfer gameTransfer = (GameTransfer) params.get("gameTransfer");
		// 查询用户游戏金额信息
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		Map<String, Object> map = new HashMap<String, Object>();
		String gamePlats = (userInfo.getPlatforms() == null ? "" : userInfo.getPlatforms());
		String result = "0";
		String balance = "";
		map.put("billno", billno);
		map.put("credit", transferNum);
		if ("AA".equals(transferOut)) { // 平台转出
			if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
				// 游戏帐号不存在，程序自动创建
				result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("创建AG账号CODE：" + userInfo.getAccount());
				} else {
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
					userInfoService.updateUserInfo(userInfo);
				}
			}
			if ("0".equals(result)) {
				// 调用第三方预转账接口
				AGApiResponse response = (AGApiResponse) gameInstance.deposit(userInfo, gamePlatform,
						transferNum.toString(), map);
				if (StringUtils.isNotEmpty(response.getInfo())) {
					if (!"0".equals(response.getInfo())) {
						throw new ApiException("转账操作失败，请稍后重试！");
					}
					map.put("type", "IN");
					result = transferConfirm(gameInstance, userInfo, gamePlatform, map);
					if ("1".equals(result)) {
						try {
							Thread.sleep(5000); // 5秒后调用确认转账
							result = transferConfirm(gameInstance, userInfo, gamePlatform, map);
							if (!"0".equals(result)) {
								throw new ApiException("转账操作失败，请稍后重试！");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if ("2".equals(result)) {
						throw new ApiException("转账操作失败，请稍后重试！");
					}
					if ("0".equals(result)) {
						// 更新用户金额
						if(accountMoney.getTotalamount().doubleValue()<transferNum.doubleValue()){
							throw new ApiException("您的余额不足，请充值！");
						}
						accountMoney
								.setTotalamount(accountMoney.getTotalamount().subtract(new BigDecimal(transferNum)));
						accountMoney.setUpdateDate(new Date());
						userInfoService.updateAccountMoneyObj(accountMoney);
						// 调用第三方查询余额接口
						balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
						gameTransfer.setOtherafter(new BigDecimal(balance));
						// 记录用户余额
						gameTransfer.setBalance(accountMoney.getTotalamount());
						gameTransfer.setStatus(1);
						gameTransfer.setUpdateDate(new Date());
						gameTransferDao.update(gameTransfer);
						// 新增帐变记录。
						PayOrderLog log = new PayOrderLog();
						log.setUiid(gameTransfer.getUuid());
						log.setOrderid(gameTransfer.getGpid().toString());
						log.setAmount(BigDecimal.ZERO.subtract(gameTransfer.getAmount()));
						log.setType(9);
						log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
						log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
						log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
						log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
						userPropertyDao.insertPayLog(log);
						return "0000";
					}
				} else {
					throw new ApiException("error", "创建游戏账号失败！");
				}
			}
		}
		// 游戏平台转入主平台
		if ("AA".equals(transferIn)) {
			AGApiResponse response = (AGApiResponse) gameInstance.withdrawal(userInfo, gamePlatform,
					transferNum.toString(), map);
			if (StringUtils.isNotEmpty(response.getInfo())) {
				if (!"0".equals(response.getInfo())) {
					throw new ApiException("转账操作失败，请稍后重试！");
				}
				// 调用第三方确认转账接口
				map.put("type", "OUT"); // 表示从游戏账号转入网站账号
				result = transferConfirm(gameInstance, userInfo, gamePlatform, map);
				if ("1".equals(result)) {
					try {
						Thread.sleep(5000); // 5秒后调用确认转账
						result = transferConfirm(gameInstance, userInfo, gamePlatform, map);
						if (!"0".equals(result)) {
							throw new ApiException("转账操作失败，请稍后重试！");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("2".equals(result)) {
					throw new ApiException("转账操作失败，请稍后重试！");
				}
				if ("0".equals(result)) {
					// 回滚用户金额
					accountMoney.setTotalamount(accountMoney.getTotalamount().add(new BigDecimal(transferNum)));
					accountMoney.setUpdateDate(new Date());
					userInfoService.updateAccountMoneyObj(accountMoney);

					// 调用第三方查询余额接口
					balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
					gameTransfer.setOtherafter(new BigDecimal(balance));
					gameTransfer.setUpdateDate(new Date());
					gameTransfer.setStatus(1);

					// 记录用户余额
					gameTransfer.setBalance(accountMoney.getTotalamount());
					gameTransfer.setUpdateDate(new Date());
					gameTransferDao.update(gameTransfer);
					// 新增帐变记录。
					PayOrderLog log = new PayOrderLog();
					log.setUiid(gameTransfer.getUuid());
					log.setOrderid(gameTransfer.getGpid().toString());
					log.setAmount(gameTransfer.getAmount());
					log.setType(9);
					log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
					log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
					log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
					log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					userPropertyDao.insertPayLog(log);
					return "0000";
				}
			} else {
				throw new ApiException(response.getInfo(), response.getMsg());
			}
		}
		return null;
	}

	/**
	 * 确认转账
	 * 
	 * @param gameInstance
	 * @param userInfo
	 * @param gamePlatform
	 * @param map
	 * @return
	 */
	private String transferConfirm(IGameServiceHandler gameInstance, UserInfo userInfo, GamePlatform gamePlatform,
			Map<String, Object> map) {
		AGApiResponse response = (AGApiResponse) gameInstance.transferCreditConfirm(userInfo, gamePlatform, map);
		boolean flag = true;
		if (StringUtils.isNotEmpty(response.getMsg()) && !"0".equals(response.getInfo())) {
			try {
				// 轮询订单状态
				while (flag) {
					response = (AGApiResponse) gameInstance.queryOrderStatus(userInfo, gamePlatform, map);
					if ("error".equals(response.getInfo()) || "network_error".equals(response.getInfo())
							|| "key_error".equals(response.getInfo())) {
						Thread.sleep(5000);
					} else {
						break;
					}
				}
			} catch (Exception e) {
				logger.error(userInfo.getAccount() + "确认转账失败：", e);
			}
		}
		return response.getInfo();
	}

	@Override
	public String updateBBINGameTransfer(Map<String, Object> params) throws ApiException {
		String transferOut = (String) params.get("transferOut");
		String transferIn = (String) params.get("transferIn");
		Integer transferNum = Integer.valueOf(params.get("transferNum").toString());
		String billno = (String) params.get("billno");
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GamePlatform gamePlatform = (GamePlatform) params.get("gamePlatform");
		IGameServiceHandler gameInstance = (IGameServiceHandler) params.get("gameInstance");
		GameTransfer gameTransfer = (GameTransfer) params.get("gameTransfer");
		// 查询用户游戏金额信息
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		Map<String, Object> map = new HashMap<String, Object>();
		String gamePlats = (userInfo.getPlatforms() == null ? "" : userInfo.getPlatforms());
		String result = "0";
		String balance = "";
		map.put("billno", billno);
		if ("AA".equals(transferOut)) { // 平台转出
			if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
				// 游戏帐号不存在，程序自动创建
				result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("创建BBIN账号CODE：" + userInfo.getAccount());
				} else {
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
					userInfoService.updateUserInfo(userInfo);
				}
			}
			if ("0".equals(result)) {
				// 调用第三方预转账接口
				AGApiResponse response = (AGApiResponse) gameInstance.deposit(userInfo, gamePlatform,
						transferNum.toString(), map);
				if (StringUtils.isNotEmpty(response.getInfo())) {
					if (!"0".equals(response.getInfo())) {
						for (int i = 0; i < 3; i++) {
							try {
								result = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, map); // 轮询订单状态
								if ("1".equals(result)) {
									break;
								}
								Thread.sleep(2000);
							} catch (Exception e) {
								logger.error("轮询BBIN订单状态：", e);
							}
						}
					}
					// 调用第三方查询余额接口
					balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
					if (balance.equals(gameTransfer.getOtherbefore().toString())) {
						throw new ApiException("转账操作失败，请稍后重试！");
					}
					// 更新用户金额
					if(accountMoney.getTotalamount().doubleValue()<transferNum.doubleValue()){
						throw new ApiException("您的余额不足，请充值！");
					}
					accountMoney.setTotalamount(accountMoney.getTotalamount().subtract(new BigDecimal(transferNum)));
					accountMoney.setUpdateDate(new Date());
					boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);
					if (codes == false) {
						gameTransfer.setStatus(2);
						gameTransferDao.update(gameTransfer);
						throw new ApiException("更新钱包操作失败！");
					}
					gameTransfer.setOtherafter(new BigDecimal(balance));
					// 记录用户余额
					gameTransfer.setBalance(accountMoney.getTotalamount());
					gameTransfer.setStatus(1);
					gameTransfer.setUpdateDate(new Date());
					codes = gameTransferDao.update(gameTransfer);
					if (codes == false) {
						throw new ApiException("转账记录状态更新失败！");
					}
					// 新增帐变记录。
					PayOrderLog log = new PayOrderLog();
					log.setUiid(gameTransfer.getUuid());
					log.setOrderid(gameTransfer.getGpid().toString());
					log.setAmount(BigDecimal.ZERO.subtract(gameTransfer.getAmount()));
					log.setType(9);
					log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
					log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
					log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
					log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					userPropertyDao.insertPayLog(log);
					return "0000";
				} else {
					throw new ApiException(response.getInfo(), response.getMsg());
				}
			}
		}
		// 游戏平台转入主平台
		if ("AA".equals(transferIn)) {
			AGApiResponse response = (AGApiResponse) gameInstance.withdrawal(userInfo, gamePlatform,
					transferNum.toString(), map);
			if (StringUtils.isNotEmpty(response.getInfo())) {
				if (!"0".equals(response.getInfo())) {
					for (int i = 0; i < 3; i++) {
						try {
							result = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, map); // 轮询订单状态
							if ("1".equals(result)) {
								break;
							}
							Thread.sleep(2000);
						} catch (Exception e) {
							logger.error("轮询BBIN订单状态：", e);
						}
					}
				}
				// 调用第三方查询余额接口
				balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
				if (balance.equals(gameTransfer.getOtherbefore().toString())) {
					throw new ApiException("转账操作失败，请稍后重试！");
				}
				gameTransfer.setOtherafter(new BigDecimal(balance));
				gameTransfer.setUpdateDate(new Date());
				accountMoney.setTotalamount(accountMoney.getTotalamount().add(new BigDecimal(transferNum)));
				if (accountMoney.getTotalamount().doubleValue() < 0) {
					throw new ApiException("您的余额不足，转账操作失败！");
				}
				accountMoney.setUpdateDate(new Date());
				boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);
				if (codes) {
					gameTransfer.setStatus(1);
				} else {
					gameTransfer.setStatus(2);
					throw new ApiException("转账记录状态更新失败！");
				}
				// 记录用户余额
				gameTransfer.setBalance(accountMoney.getTotalamount());
				gameTransfer.setUpdateDate(new Date());
				boolean cdoes = gameTransferDao.update(gameTransfer);
				if (cdoes == false) {
					throw new ApiException("转账记录状态更新失败！");
				}
				// 新增帐变记录。
				PayOrderLog log = new PayOrderLog();
				log.setUiid(gameTransfer.getUuid());
				log.setOrderid(gameTransfer.getGpid().toString());
				log.setAmount(gameTransfer.getAmount());
				log.setType(9);
				log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
				log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
				log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
				log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
				userPropertyDao.insertPayLog(log);
				return "0000";
			} else {
				logger.info("调用AG第三方确认转账接口失败。");
				throw new ApiException(response.getInfo(), response.getMsg());
			}
		}
		return null;
	}

	@Override
	public String updatePTGameTransfer(Map<String, Object> params) throws ApiException {
		String transferOut = (String) params.get("transferOut");
		String transferIn = (String) params.get("transferIn");
		Integer transferNum = Integer.valueOf(params.get("transferNum").toString());
		String billno = (String) params.get("billno");
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GamePlatform gamePlatform = (GamePlatform) params.get("gamePlatform");
		IGameServiceHandler gameInstance = (IGameServiceHandler) params.get("gameInstance");
		GameTransfer gameTransfer = (GameTransfer) params.get("gameTransfer");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("externaltranid", billno);
		String gamePlats = (userInfo.getPlatforms() == null ? "" : userInfo.getPlatforms());
		String balance = "";
		// 查询用户游戏金额信息
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		if ("AA".equals(transferOut)) {// 平台转出
			if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
				// 游戏帐号不存在，程序自动创建
				String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("登录PT游戏->创建账号CODE：" + userInfo.getAccount());
				} else {
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
					userInfoService.updateUserInfo(userInfo);
				}
			}

			gameTransfer.setUpdateDate(new Date());
			logger.info("平台余额：" + accountMoney.getTotalamount() + ",转出金额：" + transferNum);
			if(accountMoney.getTotalamount().doubleValue()<transferNum.doubleValue()){
				throw new ApiException("您的余额不足，请充值！");
			}
			accountMoney.setTotalamount(accountMoney.getTotalamount().subtract(new BigDecimal(transferNum)));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(accountMoney.getTotalamount());
			// 调用接口充值
			String code = (String) gameInstance.deposit(userInfo, gamePlatform, transferNum.toString(), paramMap);
			if ("44".equals(code)) {
				throw new ApiException("PT转账失败，用户被冻结！");
			}
			if("302".equals(code)){ //External transaction ID already exists
				paramMap.put("externaltranid", new RandomUtil().getRandomCode(9));
				code = (String) gameInstance.deposit(userInfo, gamePlatform, transferNum.toString(), paramMap);
			}
			// 调用第三方查询余额接口&检查账户余额是否变化
			balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
			if (!"0".equals(code) && gameTransfer.getOtherbefore().compareTo(new BigDecimal(balance)) == 0) {
				// 如果出现407,说明交易是在处理中，需要调用查询存取状态接口。
				int i = 0;
				try {
					do {
						code = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
						if ("approved".equals(code)) {
							break;
						}
						i++;
						Thread.sleep(2000);
					} while (i < 3);
				} catch (Exception e) {
					logger.error("查询PT转账状态失败：", e);
					throw new ApiException("PT转账查询订单状态失败！");
				}
				if (!"approved".equals(code)) {
					throw new ApiException("PT存款失败，查询存取状态异常！");
				}
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);// 更新用户金额
			if (!codes) {
				gameTransfer.setStatus(2);
				gameTransferDao.update(gameTransfer);
				throw new ApiException("调用游戏接口存钱失败！");
			}
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(BigDecimal.ZERO.subtract(gameTransfer.getAmount()));
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		// 游戏平台转入主平台
		if ("AA".equals(transferIn)) {
			// 调用转账接口
			String code = (String) gameInstance.withdrawal(userInfo, gamePlatform, transferNum.toString(), paramMap);
			if ("44".equals(code)) {
				throw new ApiException("PT转账失败，用户被冻结！");
			}else if ("98".equals(code)) { //{"error":"InstantCash amount '23' is greater than balance","errorcode":98}
				throw new ApiException("第三方游戏余额不足，或者您存在未完成的游戏，请稍等重试！");
			}else if("302".equals(code)){ //External transaction ID already exists
				paramMap.put("externaltranid", new RandomUtil().getRandomCode(9));
				code = (String) gameInstance.withdrawal(userInfo, gamePlatform, transferNum.toString(), paramMap);
			}
			if ("99".equals(code)) {
				return "99";
			}
			// 调用第三方查询余额接口
			balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
			if (!"0".equals(code) && gameTransfer.getOtherbefore().compareTo(new BigDecimal(balance)) == 0) {
				// 如果出现407,说明交易是在处理中，需要调用查询存取状态接口。
				int i = 0;
				try {
					do {
						code = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
						if ("approved".equals(code)) {
							break;
						}
						i++;
						Thread.sleep(2000);
					} while (i < 3);
				} catch (Exception e) {
					logger.error("查询PT转账状态失败：", e);
					throw new ApiException("PT转账查询订单状态失败！");
				}
				if (!"approved".equals(code)) {
					throw new ApiException("PT存款失败，查询存取状态异常！");
				}
			}
			logger.info("平台余额：" + accountMoney.getTotalamount() + "---转入金额：" + transferNum);
			accountMoney.setTotalamount(accountMoney.getTotalamount().add(new BigDecimal(transferNum)));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(accountMoney.getTotalamount());
			boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新状态失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(gameTransfer.getAmount());
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		return null;
	}
	
	@Override
	public String updateSAGameTransfer(Map<String, Object> params) throws ApiException {
		String transferOut = (String) params.get("transferOut");
		String transferIn = (String) params.get("transferIn");
		Integer transferNum = Integer.valueOf(params.get("transferNum").toString());
		String billno = String.valueOf(params.get("billno")).toLowerCase();
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GamePlatform gamePlatform = (GamePlatform) params.get("gamePlatform");
		IGameServiceHandler gameInstance = (IGameServiceHandler) params.get("gameInstance");
		GameTransfer gameTransfer = (GameTransfer) params.get("gameTransfer");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String gamePlats = (userInfo.getPlatforms() == null ? "" : userInfo.getPlatforms());
		String balance = "";
		// 查询用户游戏金额信息
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		if ("AA".equals(transferOut)) {// 平台转出
			if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
				// 游戏帐号不存在，程序自动创建
				String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if (!"0".equals(result)) {
					logger.info("登录SA游戏->创建账号CODE：" + userInfo.getAccount());
				} else {
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
					userInfoService.updateUserInfo(userInfo);
				}
			}
			paramMap.put("billno", "IN"+billno);
			gameTransfer.setUpdateDate(new Date());
			logger.info("平台余额：" + accountMoney.getTotalamount() + ",转出金额：" + transferNum);
			if(accountMoney.getTotalamount().doubleValue()<transferNum.doubleValue()){
				throw new ApiException("您的余额不足，请充值！");
			}
			BigDecimal totalamount =accountMoney.getTotalamount().subtract(new BigDecimal(transferNum));
			accountMoney.setTotalamount(BigDecimal.ZERO.subtract(new BigDecimal(transferNum)));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(totalamount);
			// 调用接口充值
			CreditBalanceDV creditBalance = (CreditBalanceDV) gameInstance.deposit(userInfo, gamePlatform, transferNum.toString(), paramMap);
			if (!"0".equals(creditBalance.getErrorMsgId())) {
				CheckOrderId checkOrder= (CheckOrderId) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
				if("true".equals(checkOrder.getExist())){
					paramMap.put("billno", "IN"+DateUtil.getStrByDate(new Date(), "yyyMMddHHmmss")+userInfo.getAccount().toLowerCase());
					creditBalance = (CreditBalanceDV) gameInstance.deposit(userInfo, gamePlatform, transferNum.toString(), paramMap);
					if (!"0".equals(creditBalance.getErrorMsgId())) {
						throw new ApiException("SA转入失败，请刷新页面后重试！");
					}
				}else if(!"0".equals(checkOrder.getErrorMsgId())){
					throw new ApiException("SA转入失败，查询存取状态异常！");
				}else{
					balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
				}
			}else{
				// 调用第三方查询余额接口
				balance = String.valueOf(creditBalance.getBalance());
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			boolean codes = userInfoService.updateTotalamount(accountMoney);// 更新用户金额
			if (!codes) {
				gameTransfer.setStatus(2);
				gameTransferDao.update(gameTransfer);
				throw new ApiException("调用游戏接口存钱失败！");
			}
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(BigDecimal.ZERO.subtract(gameTransfer.getAmount()));
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		// 游戏平台转入主平台
		if ("AA".equals(transferIn)) {
			// 调用转账接口
			paramMap.put("billno", "OUT"+billno);
			DebitBalanceDV debitBalance = (DebitBalanceDV) gameInstance.withdrawal(userInfo, gamePlatform, transferNum.toString(), paramMap);
			if(!"0".equals(debitBalance.getErrorMsgId())){
				CheckOrderId checkOrder= (CheckOrderId) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
				if("true".equals(checkOrder.getExist())){
					paramMap.put("billno", "OUT"+DateUtil.getStrByDate(new Date(), "yyyMMddHHmmss")+userInfo.getAccount().toLowerCase());
					debitBalance = (DebitBalanceDV) gameInstance.withdrawal(userInfo, gamePlatform, transferNum.toString(), paramMap);
					if (!"0".equals(debitBalance.getErrorMsgId())) {
						throw new ApiException("SA转出失败，请刷新页面后重试！");
					}
				}else if(!"0".equals(checkOrder.getErrorMsgId())){
					throw new ApiException("SA转出失败，查询存取状态异常！");
				}else{
					balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
				}
			}else{
				balance = String.valueOf(debitBalance.getBalance());
			}
			logger.info("平台余额：" + accountMoney.getTotalamount() + "---转入金额：" + transferNum);
			BigDecimal totalamount = accountMoney.getTotalamount().add(new BigDecimal(transferNum));
			accountMoney.setTotalamount(new BigDecimal(transferNum));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(totalamount);
			boolean codes = userInfoService.updateTotalamount(accountMoney);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新状态失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(gameTransfer.getAmount());
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		return null;
	}
	
	@Override
	public String updateMGGameTransfer(Map<String, Object> params) throws ApiException {
		String transferOut = (String) params.get("transferOut");
		String transferIn = (String) params.get("transferIn");
		Integer transferNum = Integer.valueOf(params.get("transferNum").toString());
		String billno = String.valueOf(params.get("billno")).toLowerCase();
		UserInfo userInfo = (UserInfo) params.get("userInfo");
		GamePlatform gamePlatform = (GamePlatform) params.get("gamePlatform");
		IGameServiceHandler gameInstance = (IGameServiceHandler) params.get("gameInstance");
		GameTransfer gameTransfer = (GameTransfer) params.get("gameTransfer");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String gamePlats = (userInfo.getPlatforms() == null ? "" : userInfo.getPlatforms());
		String balance = "";
		// 查询用户游戏金额信息
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		if ("AA".equals(transferOut)) {// 平台转出
			if (userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
				// 游戏帐号不存在，程序自动创建
				String result = (String) gameInstance.createAccount(userInfo, gamePlatform, null);
				if ("-1".equals(result)) {
					logger.info("登录MG游戏->创建账号CODE：" + userInfo.getAccount());
				} else {
					// 添加创建第三方账号标识
					userInfo.updatePlats(gamePlatform.getGpname() + gamePlatform.getGpid());
					userInfo.setMgId(result);
					userInfoService.updateUserInfo(userInfo);
				}
			}
			paramMap.put("externaltranid", billno);
			gameTransfer.setUpdateDate(new Date());
			logger.info("平台余额：" + accountMoney.getTotalamount() + ",转出金额：" + transferNum);
			if(accountMoney.getTotalamount().doubleValue()<transferNum.doubleValue()){
				throw new ApiException("您的余额不足，请充值！");
			}
			BigDecimal totalamount =accountMoney.getTotalamount().subtract(new BigDecimal(transferNum));
			accountMoney.setTotalamount(BigDecimal.ZERO.subtract(new BigDecimal(transferNum)));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(totalamount);
			// 调用接口充值
			String result = (String) gameInstance.deposit(userInfo, gamePlatform, transferNum.toString(), paramMap);
			JSONObject json = JSONObject.fromObject(result);
			if (json.containsKey("error")) {
				result = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
				if("-1".equals(result)){
					throw new ApiException("MG转入失败，请刷新页面后重试！");
				}else{
					balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
				}
			}else{
				// 调用第三方查询余额接口
				balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			boolean codes = userInfoService.updateTotalamount(accountMoney);// 更新用户金额
			if (!codes) {
				gameTransfer.setStatus(2);
				gameTransferDao.update(gameTransfer);
				throw new ApiException("调用游戏接口存钱失败！");
			}
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(BigDecimal.ZERO.subtract(gameTransfer.getAmount()));
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		// 游戏平台转入主平台
		if ("AA".equals(transferIn)) {
			// 调用转账接口
			paramMap.put("externaltranid", billno);
			String result = (String) gameInstance.withdrawal(userInfo, gamePlatform, transferNum.toString(), paramMap);
			JSONObject json = JSONObject.fromObject(result);
			if (json.containsKey("error")) {
				result = (String) gameInstance.queryOrderStatus(userInfo, gamePlatform, paramMap);
				if("-1".equals(result)){
					throw new ApiException("MG转出失败，请刷新页面后重试！");
				}else{
					balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
				}
			}else{
				// 调用第三方查询余额接口
				balance =(String)gameInstance.queryBalance(userInfo, gamePlatform, paramMap);
			}
			logger.info("平台余额：" + accountMoney.getTotalamount() + "---转入金额：" + transferNum);
			BigDecimal totalamount = accountMoney.getTotalamount().add(new BigDecimal(transferNum));
			accountMoney.setTotalamount(new BigDecimal(transferNum));
			accountMoney.setUpdateDate(new Date());
			// 记录用户余额
			gameTransfer.setBalance(totalamount);
			boolean codes = userInfoService.updateTotalamount(accountMoney);
			if (!codes) {
				throw new ApiException("转账记录更新失败！");
			}
			gameTransfer.setOtherafter(new BigDecimal(balance));
			gameTransfer.setStatus(1);
			codes = gameTransferDao.update(gameTransfer);
			if (!codes) {
				throw new ApiException("转账记录更新状态失败！");
			}
			// 新增帐变记录。
			PayOrderLog log = new PayOrderLog();
			log.setUiid(gameTransfer.getUuid());
			log.setOrderid(gameTransfer.getGpid().toString());
			log.setAmount(gameTransfer.getAmount());
			log.setType(9);
			log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
			log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
			log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
			log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			userPropertyDao.insertPayLog(log);
			return "0000";
		}
		return null;
	}

	@Override
	public boolean updateGameAccount(GameAccount gameAccount) {
		return gameAccountDao.update(gameAccount);
	}

	@Override
	public String modifyCaseout(PayOrder payOrder, UserInfo userInfo, Integer caseoutFigure) throws Exception {
		AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
		if (StringUtils.isBlank(ObjectUtils.toString(accountMoney))
				|| accountMoney.getTotalamount().intValue() < caseoutFigure.intValue()) {// 账户余额不足
			return "-1";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uiid", userInfo.getUiid());
		String today = DateUtil.FormatDate(new Date());
		params.put("daytime", today);
		
		WithdrawalcountLog withdrawalcountLog = userPropertyDao.getWithdrawalcountLog(params);
		params.clear();
		params.put("grade", userInfo.getGrade());
		UserGrade userGrade = userPropertyDao.getUserGrade(params);
		if (null == userGrade) {
			return "-5";// 没有取款额度请联系在线客服。
		}
		if(withdrawalcountLog ==null){
			// 新增当天提款次数
			withdrawalcountLog = new WithdrawalcountLog();
			withdrawalcountLog.setUiid(userInfo.getUiid());
			withdrawalcountLog.setDaytime(today);
			withdrawalcountLog.setWithdrawalcount(userGrade.getWithdrawalcount());
			userPropertyDao.insertWithdrawalcountLog(withdrawalcountLog);
		}else if(withdrawalcountLog.getWithdrawalcount() <= 0) {
			return "-4";// 提款次数上限
		}
		if (caseoutFigure > Double.valueOf(userGrade.getWithdrawalquota())) {
			return "-6";// 您的单笔提款超过提款限额。
		}
		params.clear();
		params.put("uiid", userInfo.getUiid());
		params.put("paytyple", 1);
		params.put("status", 1);
		Long pay = userPropertyDao.getPayOrder(params);
		if (pay > 0) {
			return "-7";// 您有提款申请正在审核，请等待审核通过后再次申请提款。
		}
		payOrder = (PayOrder) payOrderDao.save(payOrder);
		if (StringUtils.isNotBlank(ObjectUtils.toString(payOrder.getID()))) {
			accountMoney.setTotalamount(accountMoney.getTotalamount().subtract(new BigDecimal(caseoutFigure)));
			accountMoney.setUpdateDate(new Date());
			if (userInfoService.updateAccountMoneyObj(accountMoney)) {
				// 更新提款次数
				 withdrawalcountLog.setWithdrawalcount(withdrawalcountLog.getWithdrawalcount()-1);
				 userPropertyDao.updateWithdrawalcountLog(withdrawalcountLog);
				return "0000";
			}
			return "-2";
		}
		return "-3";
	}

	@Override
	public List<GameTransfer> selectTranferOrder(Map<String, Object> params) {
		return gameTransferDao.selectTranferOrder(params);
	}

	@Override
	public Long selectTranferOrderCount(Map<String, Object> params) {
		return gameTransferDao.selectTranferOrderCount(params);
	}

	@Override
	public void updatePayOrder(PayOrder payOrder) {
		payOrderDao.saveOrUpdate(payOrder);
	}

	@Override
	public GameTransfer saveGameTransfer(GameTransfer entity) {
		return (GameTransfer) gameTransferDao.save(entity);
	}

	@Override
	public void updateGameTransfer(GameTransfer entity) {
		gameTransferDao.update(entity);
	}

	@Override
	public GameTransfer getGameTransfer(Map<String, Object> params) {
		return gameTransferDao.getGameTransfer(params);
	}
}
