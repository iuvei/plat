package com.gameportal.manage.betlog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.BetLogDao;
import com.gameportal.manage.betlog.dao.GameInfoDao;
import com.gameportal.manage.betlog.model.BetClearing;
import com.gameportal.manage.betlog.model.BetLog;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.model.BetLogVo;
import com.gameportal.manage.betlog.service.IBetLogService;
import com.gameportal.manage.member.dao.MemberInfoDao;
import com.gameportal.manage.member.dao.UserXimaSetDao;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.model.UserXimaSet;
import com.gameportal.manage.proxy.dao.ProxySetDao;
import com.gameportal.manage.proxy.model.ProxySet;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.manage.xima.dao.MemberXimaSetDao;
import com.gameportal.manage.xima.model.MemberXimaSet;

import net.sf.json.JSONObject;


@Service
public class BetLogServiceImpl implements IBetLogService {

	@Resource(name = "betLogDao")
	private BetLogDao betLogDao = null;
	
	@Resource(name = "memberInfoDao")
	private MemberInfoDao memberInfoDao = null;
	
	@Resource
	private MemberXimaSetDao memberXimaSetDao;
	
	@Resource(name = "gameInfoDao")
	private GameInfoDao gameInfoDao = null;
	
	@Resource(name = "userXimaSetDao")
	private UserXimaSetDao userXimaSetDao;
	
	@Resource(name = "proxySetDao")
	private ProxySetDao proxySetDao;
	
	@Override
	public boolean deleteById(Long id) {
	
		return	betLogDao.delete(id);
	}

	@Override
	public BetLog findEntityById(Long id) {
		return (BetLog) betLogDao.findById(id);
	}

	@Override
	public boolean modify(BetLog entity) {
		return betLogDao.saveOrUpdate(entity);
	}

	@Override
	public Long queryForCount(Map map) {
		return betLogDao.getRecordCount(map);
	}

	@Override
	public List<BetLog> queryForList(Map<String,Object> map, Integer startNo, Integer pageSize) {
		map.put("limit", true);
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		List<BetLog> list = betLogDao.getList(map);
		return list;
	}

	@Override
	public BetLog save(BetLog entity) {
		BetLog betLog= (BetLog) betLogDao.save(entity);
		return StringUtils.isNotBlank(ObjectUtils.toString(betLog.getPdid())) ? betLog : null;
	}

	@Override
	public List<BetLogVo> queryForListVo(Map map, Integer startNo, Integer pageSize) {
		
		List<BetLogVo> list= betLogDao.queryForPager(BetLog.class.getSimpleName()+".selectBetLog", map, startNo, pageSize);				
		return list;
	}

	@Override
	public Long getMaxBetNo(Map map) {
		return	(Long)betLogDao.queryForObject(BetLog.class.getSimpleName()+".getMaxNo", map);
		
	}

	@Override
	public List<BetLogTotal> selectBetTotal(Map<String, Object> params,
			int thisPage, int pageSize) {
		params.put("groupColumns", "bet.account,bet.platformcode,bet.gamename");
		params.put("limit", true);
		params.put("thisPage", thisPage);
		params.put("pageSize", pageSize);
		return betLogDao.selectBetTotal(params, thisPage, pageSize);
	}
	
	@Override
	public List<BetLogTotal> selectBetTotalForReport(Map<String, Object> params) {
		params.put("groupColumns", "bet.account,bet.platformcode,bet.gamename");
		return betLogDao.selectBetTotal(params,0,0);
	}
	
	@Override
	public Long selectBetTotalCount(Map<String, Object> params) {
		return betLogDao.selectBetTotalCount(params);
	}

	@Override
	public String getProxyLoss(Map<String, Object> params) {
		String los = betLogDao.getProxyLoss(params);
		return StringUtils.isNotBlank(los)==true?los:"0.00";
	}

	@Override
	public List<BetClearing> getBetClearing(Map<String, Object> params) {
		return betLogDao.getBetClearing(params);
	}

	@Override
	public List<BetLogTotal> selectXimaBetTotal(Map<String, Object> params,int thisPage,int pageSize) {

		List<BetLogTotal> list =  betLogDao.selectXimaBetTotal(params);
		List<BetLogTotal> result = null;
		if(null != list && list.size()>0){
			BetLogTotal betLogTotal = null;
			result = new ArrayList<BetLogTotal>();
			for(BetLogTotal object : list){
				betLogTotal = object;
				String subAccount = betLogTotal.getAccount();
				// 获取该会员对应的返水比例值
				String platformcode = object.getPlatformcode();//游戏平台CODE
				Map<String, Object> ximaparams = new HashMap<String, Object>();
				int puiid = betLogTotal.getPuiid();//上级ID
				//过滤按天洗码可洗码的代理下线
				if(puiid > 0){//0标示公司直属会员
					ximaparams.put("uiid", puiid);
					ximaparams.put("status", "1");
					ProxySet proxySet = proxySetDao.getObject(ximaparams);
					if(proxySet != null){
						if(proxySet.getIsximaflag() == 1 && proxySet.getClearingtype() ==2){
							continue;
						}
					}
				}
				ximaparams.clear();
				ximaparams.put("account", subAccount);
				List<MemberInfo> userlist = memberInfoDao.queryForPager(ximaparams,0, 1);
				MemberInfo memberinfo = null;
				if(null != userlist && userlist.size() >0 ){
					memberinfo = userlist.get(0);
				}
				if(null != memberinfo){
					boolean is = true;
					/*查询是否有单独设置用户洗码比例*/
					ximaparams.clear();
					ximaparams.put("uiid", memberinfo.getUiid());
					ximaparams.put("proxyid", "0");
					ximaparams.put("status", "1");
					UserXimaSet uxinaSet = userXimaSetDao.getObject(ximaparams);
					if(uxinaSet!=null && StringUtils.isNotBlank(uxinaSet.getXimascale())){
						double scaleDob = Double.valueOf(uxinaSet.getXimascale());
						double betamontSum = Double.valueOf(object.getValidBetAmountTotal());//获取投注总额
						double amount = betamontSum * scaleDob;
						betLogTotal.setXimaAmount(com.gameportal.manage.util.StringUtils.convertNumber(amount)+"#"+scaleDob);
						is = false;
					}
					if(is){
						MemberXimaSet mxs = memberXimaSetDao.queryByUiid(WebConstants.getGameMap(platformcode).intValue(),memberinfo.getGrade());
						if(null != mxs && null != memberinfo){//没有查询到洗码设置表示不能洗码
							try {
								JSONObject ximasetJson = JSONObject.fromObject(mxs.getScale());
								String dbgamecode = object.getGamecode();
								double scaleDob = 0.00;//获取洗码比例
								if(ximasetJson.containsKey(dbgamecode)){
									scaleDob = ximasetJson.getDouble(dbgamecode);//获取洗码比例
								}else{
									scaleDob = ximasetJson.getDouble("ALL");//获取洗码比例
								}
								if(scaleDob > 0){
									if(StringUtils.isNotBlank(object.getValidBetAmountTotal())){
										double betamontSum = Double.valueOf(object.getValidBetAmountTotal());//获取投注总额
										double amount = betamontSum * scaleDob;
										betLogTotal.setXimaAmount(com.gameportal.manage.util.StringUtils.convertNumber(amount)+"#"+scaleDob);
									}else{
										betLogTotal.setXimaAmount("0.00");
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							betLogTotal.setXimaAmount("0.00");
						}
					}
				}else{
					betLogTotal.setXimaAmount("0.00");
				}
				result.add(betLogTotal);
			}
		}
		return result;
	}
	
	/**
	 * 统计要洗码的会员
	 * @param params
	 * @return
	 */
	public Long selectXimaBetTotalCount(Map<String, Object> params){
		return betLogDao.selectXimaBetTotalCount(params);
	}

	@Override
	public List<BetClearing> selectProxyXima(Map<String, Object> params) {
		return betLogDao.selectProxyXima(params);
	}

	@Override
	public List<BetLogTotal> selectProxyDownUserXima(
			Map<String, Object> params, int thisPage, int pageSize) {
		// TODO Auto-generated method stub
		return betLogDao.selectProxyDownUserXima(params, thisPage, pageSize);
	}

	@Override
	public Long selectProxyDownUserXimaCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return betLogDao.selectProxyDownUserXimaCount(params);
	}

	@Override
	public String getProxyPreferential(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return betLogDao.getProxyPreferential(params);
	}

}
