package com.gameportal.web.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.user.dao.AccountMoneyDao;
import com.gameportal.web.user.dao.ActivityDao;
import com.gameportal.web.user.dao.CardPackageDao;
import com.gameportal.web.user.dao.PayOrderDao;
import com.gameportal.web.user.dao.UserInfoDao;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.Activity;
import com.gameportal.web.user.model.BetLog;
import com.gameportal.web.user.model.CardPackage;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserLoginLog;
import com.gameportal.web.user.model.UserSign;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.GetProperty;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements IUserInfoService {
    @Resource(name = "userInfoDao")
    private UserInfoDao userInfoDao = null;
    @Resource(name = "cardPackageDao")
    private CardPackageDao cardPackageDao = null;
    @Resource(name = "accountMoneyDao")
    private AccountMoneyDao accountMoneyDao = null;
    @Resource(name = "activityDao")
    private ActivityDao activityDao = null;
    @Resource(name = "payOrderDao")
	private PayOrderDao payOrderDao;
    private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

    public UserInfoServiceImpl() {
        super();
    }

    @Override
    public UserInfo saveUserInfo(UserInfo userInfo) throws Exception {
        userInfo = (UserInfo) userInfoDao.save(userInfo);
        /*保存用户金币记录*/
        Date now = DateUtil.getDateByStr(DateUtil.getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		AccountMoney accountMoney = new AccountMoney();
		accountMoney.setUiid(userInfo.getUiid());
		accountMoney.setTotalamount(BigDecimal.ZERO);
		accountMoney.setStatus(1);
		accountMoney.setCreateDate(now);
		accountMoney.setUpdateDate(now);
		saveAccountMoney(accountMoney);
        return StringUtils.isNotBlank(ObjectUtils.toString(userInfo.getUiid())) ? userInfo
                : null;
    }

    @Override
    public UserInfo queryUserInfo(String userName) {
        return queryUserInfo(userName, 1);
    }

    @Override
    public UserInfo queryUserInfo(String userName, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", userName);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        UserInfo userInfo = (UserInfo) userInfoDao.queryForObject(
                userInfoDao.getSelectQuery(), map);
        return userInfo;
    }

    @Override
    public UserInfo findUserInfoId(Long uiid) {
        return findUserInfoId(uiid, null);
    }

    @Override
    public UserInfo findUserInfoId(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        UserInfo userInfo = (UserInfo) userInfoDao.queryForObject(
                userInfoDao.getSelectQuery(), map);
        return userInfo;
    }

    @Override
    public boolean modifyUserInfo(UserInfo userInfo) throws Exception {
        return userInfoDao.update(userInfo);
    }

    @Override
    public CardPackage saveCardPackage(CardPackage cardPackage)
            throws Exception {
        cardPackage = (CardPackage) cardPackageDao.save(cardPackage);
        return StringUtils.isNotBlank(ObjectUtils.toString(cardPackage
                .getCpid())) ? cardPackage : null;
    }

    @Override
    public Long getCardPackageCount(Long uiid) {
        return getCardPackageCount(uiid, 1);
    }

    @Override
    public Long getCardPackageCount(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        Long count = cardPackageDao.getRecordCount(map);
        return count;
    }

    @Override
    public List<CardPackage> queryUserInfoCardPackage(Long uiid,
            Integer startNo, Integer pagaSize) {
        return queryUserInfoCardPackage(uiid, 1, startNo, pagaSize);
    }

    @Override
    public List<CardPackage> queryUserInfoCardPackage(Long uiid,
            Integer status, Integer startNo, Integer pagaSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        map.put("sortColumns", " create_date desc");
        List<CardPackage> cardPackageList = cardPackageDao.queryForPager(map,
                startNo, pagaSize);
        return (StringUtils.isNotBlank(ObjectUtils.toString(cardPackageList)) && cardPackageList
                .size() > 0) ? cardPackageList : null;
    }

    @Override
    public Long getAccountMoneyCount(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        Long count = accountMoneyDao.getRecordCount(map);
        return count;
    }

    @Override
    public Long getAccountMoney(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        AccountMoney accountMoney = (AccountMoney) accountMoneyDao
                .queryForObject(accountMoneyDao.getSelectQuery(), map);
        if (StringUtils.isNotBlank(ObjectUtils.toString(accountMoney))) {
            return accountMoney.getTotalamount().longValue();
        }
        return 0L;
    }

    @Override
    public AccountMoney getAccountMoneyObj(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        AccountMoney accountMoney = (AccountMoney) accountMoneyDao
                .queryForObject(accountMoneyDao.getSelectQuery(), map);
        return StringUtils.isNotBlank(ObjectUtils.toString(accountMoney)) ? accountMoney
                : null;
    }

    @Override
    public boolean updateAccountMoneyObj(AccountMoney accountMoney) {
        return accountMoneyDao.update(accountMoney);
    }

    @Override
    public UserInfo queryUserInfo(String account, String phone, String email,
            Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(ObjectUtils.toString(account))) {
            map.put("account", account);
        }
        if (StringUtils.isNotBlank(ObjectUtils.toString(phone))) {
            map.put("phone", phone);
        }
        if (StringUtils.isNotBlank(ObjectUtils.toString(email))) {
            map.put("email", email);
        }
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
		List<UserInfo> userList = userInfoDao.queryForPager(userInfoDao.getSelectQuery(),map,0,20);
		if(CollectionUtils.isNotEmpty(userList)){
			return userList.get(0);
		}
		return null;
    }

    @Override
    public CardPackage queryCardPackage(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(ObjectUtils.toString(uiid))) {
            map.put("uiid", uiid);
        }
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        CardPackage cardPackage = (CardPackage) cardPackageDao.queryForObject(
                cardPackageDao.getSelectQuery(), map);
        return StringUtils.isNotBlank(ObjectUtils.toString(cardPackage)) ? cardPackage
                : null;
    }

    @Override
    public UserInfo findProxyInfoId(Long uiid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uiid", uiid);
        if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
            map.put("status", status);
        }
        map.put("accounttype", 0);
        UserInfo userInfo = (UserInfo) userInfoDao.queryForObject(
                userInfoDao.getSelectQuery(), map);
        return userInfo;
    }

    @Override
    public String queryProxyID(String url) {
        List<Map<String, Object>> list = userInfoDao.selectProxyUrl(null);
        if(list == null || list.size() <=0){
            return "0";
        }
        for(Map map : list){
            String proxyurl = map.get("proxyurl").toString();
            if(url.equals(proxyurl)){
                return map.get("proxyuid").toString();
            }
        }
        return "0";
    }

    @Override
    public List<BetLog> selectGameOrder(Map<String, Object> params) {
        return userInfoDao.selectGameOrder(params);
    }

    @Override
    public Long selectGameOrderCount(Map<String, Object> params) {
        return userInfoDao.selectGameOrderCount(params);
    }

    @Override
    public int updateLogin(UserInfo userInfo) {

        return userInfoDao.updateLogin(userInfo);
    }

    @Override
    public boolean saveAccountMoney(AccountMoney accountMoney) {
        return accountMoneyDao.saveOrUpdate(accountMoney);
    }

    @Override
    public void insertLog(UserLoginLog entity) {
        userInfoDao.insertLog(entity);
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        return userInfoDao.update(userInfo);
    }

    @Override
    public List<String> getDXList() {
        return userInfoDao.getDXList();
    }

    @Override
    public long getUserInfoCount(Map<String, Object> param) {
        return userInfoDao.getRecordCount(param);
    }

    @Override
    public List<Activity> getList(Map<String, Object> params) {
        return activityDao.getList(params);
    }

    @Override
    public boolean updateCardPackage(CardPackage cardPackage) {
        return cardPackageDao.update(cardPackage);
    }

    @Override
    public Activity getActivity(Map<String, Object> params) {
        return activityDao.getObject(params);
    }

    @Override
    public List<Map<String, Object>> selectProxyUrl(Map<String, Object> map) {
        return userInfoDao.selectProxyUrl(map);
    }

    @Override
    public List<String> selectUserLoginLog(Map<String, Object> params) {
        return userInfoDao.selectUserLoginLog(params);
    }

    @Override
    public List<String> selectUserLoginIps(Map<String, Object> params) {
        return userInfoDao.selectUserLoginIps(params);
    }

    /**
     * 根据条件查询游戏前一定数量的注单
     */
    @Override
    public List<BetLog> selectGameBetLog(Map<String, Object> params) {
        return userInfoDao.selectGameBetLog(params);
    }

    /**
     * 根据条件查询游戏前一定数量的注单  并将数据转换成固定格式的字符串集合
     * 如果数据不足，用随机数据补充
     * @param params
     * @return
     */
    @Override
    public List<String> selWinningTopBetLog(Map<String, Object> params) {
        List<String> list = new ArrayList<String>();
//        List<BetLog> betLogs = selectGameBetLog(params);
        Integer size = 0;
//        if (betLogs != null) {
//            size = betLogs.size();
//        }
        Integer limitNum = (Integer) params.get("limitNum");
//        for (BetLog betLog : betLogs) {
//            String dataStr = toWinningTopString(betLog);
//            list.add(dataStr);
//        }
        if(limitNum > size){//数据量不够，用随机数据填充
            int num = limitNum - size;//需要补充的数量
            Properties property = GetProperty.getProp("winningTop.properties");
            for (int i = 0; i < num; i++) {
                String content = property.getProperty("betlog.win.top.content" + i);
                list.add(content);
            }

        }
        return list;
    }

    //将投注记录转换成对应格式的字符串
    private String toWinningTopString(BetLog betLog) {
        BigDecimal money = betLog.getProfitamount();
        String moneyStr = money + "￥元</p>";
        if(money.compareTo(new BigDecimal(10000))>=0){
            moneyStr = money.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP) + "￥万元</p>";
        }
        List<String> list = new ArrayList<>();
        list.add("广东");
        list.add("山东");
        list.add("上海");
        list.add("武汉");
        list.add("长沙");
        list.add("广州");
        int random = new Random().nextInt(5);
        StringBuffer sb = new StringBuffer("<p>"+list.get(random)+" 玩家<span class=\"c-blue\">").append(betLog.getEncodeAccount()).append("</span></p><p>在").append(betLog.getGamename()).append("赢得<span class=\"c-red\">").append(moneyStr);
        return sb.toString();
    }
    
    /**
     * 查询所有有转账到MG记录的用户
     * @return
     */
    public List<UserInfo> selectUserOfTransferMg(){
        return userInfoDao.selectUserOfTransferMg();
    }

	@Override
	public List<CardPackage> queryCardPackage(Map<String, Object> params) {
		List<CardPackage> cardPackageList = cardPackageDao.queryForPager(params, 0, 20);
		return cardPackageList;
	}
	
	@Override
    public boolean updateTotalamount(AccountMoney accountMoney) {
        return accountMoneyDao.updateTotalamount(accountMoney);
    }

	@Override
	public boolean cancelWithdraw(Map<String, Object> params) {
		//查询提款订单
		List<PayOrder> payOrders = payOrderDao.getList(params);
		PayOrder order =null;
		if(CollectionUtils.isNotEmpty(payOrders)){
			order = payOrders.get(0);
		}else{
			return true;
		}
		//防止重复撤销
		if(order.getStatus() ==0){
			return true;
		}
		order.setStatus(0);
		order.setRemarks("客户主动撤销");
		order.setUpdate_Date(new Date());
		payOrderDao.update(order);
		AccountMoney accountMoney = getAccountMoneyObj(Long.valueOf(params.get("uiid").toString()),null);
		//回充金额
		accountMoney.setTotalamount(order.getAmount());
		accountMoney.setUpdateDate(new Date());
		updateTotalamount(accountMoney);
		return true;
	}
	
	@Override
	public void insertUserSign(UserSign sign) {
		userInfoDao.insertUserSign(sign);
	}

	@Override
	public List<UserSign> queryUserSignList(Map<String, Object> param) {
		return userInfoDao.queryUserSignList(param);
	}

	@Override
	public void updateUserSign(UserSign sign) {
		userInfoDao.updateUserSign(sign);
	}
	
	
}
