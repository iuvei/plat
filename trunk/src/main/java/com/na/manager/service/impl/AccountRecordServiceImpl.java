package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.na.manager.bean.AccountRecordReportRequest;
import com.na.manager.bean.Page;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IAccountRecordMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.AccountRecord;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.service.IAccountRecordService;
import com.na.manager.util.SnowflakeIdWorker;

/**
 * @author andy
 * @date 2017年6月23日 上午10:19:22
 * 
 */
@Service
@Transactional(propagation=Propagation.NESTED,rollbackFor=Exception.class)
public class AccountRecordServiceImpl implements IAccountRecordService {
	
	@Autowired
	private IAccountRecordMapper accountRecordMapper;
	
	@Autowired
	private IUserMapper userMapper;
	
	@Resource(name="accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
	
	@Value("${spring.na.platform.proxy.path}")
	private String proxyPath; //成都代理网
	
	@Value("${spring.na.platform.merchant.path}")
	private String merchantPath; //成都现金网
	
	@Override
	@Transactional(readOnly=true)
	public Page<AccountRecord> queryAccountRecordByPage(AccountRecordReportRequest reportRequest) {
		Page<AccountRecord> page = new Page<>(reportRequest);
		reportRequest.setParentPath(getParentPath());
		if(StringUtils.isNotEmpty(reportRequest.getUserName())){
			User user = userMapper.getUser(reportRequest.getUserName());
			if(user !=null){
				reportRequest.setUserId(user.getId());
			}else{
				return page;
			}
		}
		List<AccountRecord> data = accountRecordMapper.queryAccountRecordByPage(reportRequest);
		Map<String, BigDecimal> totalMap = accountRecordMapper.queryAccountRecordByPageTotal(reportRequest);
		Map<String, Object> staMap = new HashMap<>();
		BigDecimal deposit=BigDecimal.ZERO;
		BigDecimal withwraw =BigDecimal.ZERO;
		for (AccountRecord ar : data) {
			ar.setAfterAmount(ar.getPreBalance().add(ar.getAmount()));

			if(ar.getType()==1){ //存点
				deposit =deposit.add(ar.getAmount());
			}else{ //提点
				withwraw=withwraw.add(ar.getAmount());
			}
		}
		staMap.put("deposit", deposit);
		staMap.put("withwraw", withwraw);
		if(totalMap == null || totalMap.size() < 1) {
			staMap.put("totalDeposit", 0);
			staMap.put("totalWithwraw", 0);
		} else {
			staMap.put("totalDeposit", totalMap.get("totalDeposit"));
			staMap.put("totalWithwraw", totalMap.get("totalWithwraw"));
		}
		page.setOtherInfo(staMap);
		page.setData(data);
		page.setTotal(accountRecordMapper.getAccountRecordCount(reportRequest));
		return page;
	}


	public List<AccountRecord> findAccountRecordBy(Date startTime,Long userId){
		AccountRecordReportRequest request = new AccountRecordReportRequest();
//		request.setStartTime(startTime);
		request.setUserId(userId);
		request.setFlag("0"); //未推送
		request.setPageSize(Long.MAX_VALUE);
		return accountRecordMapper.queryAccountRecordByPage(request);
	}

	private String getParentPath() {
		User currentUser = AppCache.getCurrentUser();
		switch (currentUser.getUserTypeEnum()){
			case SYS:
				return "/";
			case SUB_ACCOUNT:
				ChildAccountUser childAccountUser = (ChildAccountUser)currentUser;
				LiveUser liveUser = childAccountUser.getParentUser();
				return liveUser.getParentPath();
			case LIVE:
				return ((LiveUser)currentUser).getParentPath();
			default:
				throw new IllegalArgumentException("user.illegal.user");
		}
	}

	@Override
	public void add(User user, AccountRecordType type,ChangeBalanceTypeEnum changeType, BigDecimal changeBalance, String remark) {
		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setUserId(user.getId());
		accountRecord.setId(accountRecordSnowflakeIdWorker.nextId());
		accountRecord.setSn("ZR"+accountRecord.getId());
		accountRecord.setTime(new Date());
		accountRecord.setAmount(changeBalance);
		if(user.getBalance() == null) {
			accountRecord.setPreBalance(new BigDecimal(0));
		} else {
			accountRecord.setPreBalance(user.getBalance());
		}
		accountRecord.setTypeEnum(type);
		accountRecord.setRemark(remark);
		if(changeType ==ChangeBalanceTypeEnum.SELF){
			accountRecord.setExecUser(user.getLoginName());
		}else{
			accountRecord.setExecUser(AppCache.getCurrentUser()==null?null:AppCache.getCurrentUser().getLoginName());
		}
		accountRecordMapper.add(accountRecord);
	}


	@Override
	public List<AccountRecord> findAccountRecordByRoundId(Long roundId) {
		AccountRecordReportRequest request = new AccountRecordReportRequest();
		request.setRoundId(roundId);
		request.setPageSize(Long.MAX_VALUE);
		request.setProxyPath(proxyPath);
		request.setMerchantPath(merchantPath);
		return accountRecordMapper.queryAccountRecordByPage(request);
	}


	@Override
	public void updataFlag(List<AccountRecord> list) {
		accountRecordMapper.updataFlag(list);
	}
}
