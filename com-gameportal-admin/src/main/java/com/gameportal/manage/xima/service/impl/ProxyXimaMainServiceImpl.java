package com.gameportal.manage.xima.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.BetLogDao;
import com.gameportal.manage.pay.dao.PayOrderDao;
import com.gameportal.manage.system.model.SystemUser;
import com.gameportal.manage.xima.dao.ProxyXimaDetailDao;
import com.gameportal.manage.xima.dao.ProxyXimaMainDao;
import com.gameportal.manage.xima.dao.ProxyXimaSetDao;
import com.gameportal.manage.xima.model.ProxyXimaDetail;
import com.gameportal.manage.xima.model.ProxyXimaMain;
import com.gameportal.manage.xima.model.ProxyXimaSet;
import com.gameportal.manage.xima.service.IProxyXimaMainService;

@Service("proxyXimaMainServiceImpl")
public class ProxyXimaMainServiceImpl implements IProxyXimaMainService {

	private static final Logger logger = Logger
			.getLogger(ProxyXimaMainServiceImpl.class);

	@Resource
	private ProxyXimaMainDao proxyXimaMainDao;
	@Resource
	private ProxyXimaDetailDao proxyXimaDetailDao;
	@Resource
	private ProxyXimaSetDao proxyXimaSetDao;
	@Resource
	private PayOrderDao payOrderDao;
	@Resource
	private BetLogDao betLogDao;

	public ProxyXimaMainServiceImpl() {
		super();
	}

	@Override
	public boolean saveOrUpdateProxyXimaMain(ProxyXimaMain card) {
		return proxyXimaMainDao.saveOrUpdate(card);
	}

	@Override
	public boolean deleteProxyXimaMain(Long id) {
		return proxyXimaMainDao.delete(id);
	}

	@Override
	public Long queryProxyXimaMainCount(Map<String, Object> params) {
		return proxyXimaMainDao.getRecordCount(params);
	}

	@Override
	public List<ProxyXimaMain> queryProxyXimaMain(Map<String, Object> params,
			Integer startNo, Integer pageSize) {
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return proxyXimaMainDao.getList(params);
	}

	@Override
	public boolean clearXima(ProxyXimaMain pxm, Date ymdstartDate,
			Date ymdendDate, SystemUser user) {
		if (null == pxm || null == ymdendDate || null == ymdstartDate
				|| null == user) {
			return false;
		}
		ProxyXimaDetail pxd = new ProxyXimaDetail();
		// 生成指定日期区间的洗码明细记录（返水金额为0）
		pxd.setAmount(0L);
		pxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		pxd.setOpttype(1); // opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		pxd.setOptuiid(user.getUserId());
		pxd.setOptusername(user.getRealName());
		pxd.setUiid(pxm.getUiid());
		pxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		pxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
		pxd.setParamlog("<|>返水金额 = 投注金额 x 返水比例值<|>洗码清零操作");
		// 保存洗码明细记录
		return proxyXimaDetailDao.saveOrUpdate(pxd);
	}

	@Override
	public boolean forceXima(ProxyXimaMain pxm, Date ymdstartDate,
			Date ymdendDate, SystemUser user) {
		if (null == pxm || null == ymdendDate || null == ymdstartDate
				|| null == user) {
			return false;
		}
		// 获取该会员对应的返水比例值
		ProxyXimaSet mxs = proxyXimaSetDao.queryByUiid(pxm.getUiid());
		if (null == mxs) {
			return false;
		}
		ProxyXimaDetail mxd = new ProxyXimaDetail();
		// 查询指定日期区间中该会员在各游戏平台的投注记录，计算总投注额(按游戏平台分别计算)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", pxm.getAccount());
		params.put("ymdstart", new java.sql.Date(ymdstartDate.getTime()));
		params.put("ymdend", new java.sql.Date(ymdendDate.getTime()));
		Long sum = betLogDao.selectBackAmount(params);
		sum = (null == sum ? 0L : sum);
		// 获取下级会员的赠送金额
		params.put("paytyple", 2); // 存款方式 0存款，1提款，2赠送，3扣款
		Long gift = payOrderDao.selectGiftAmount(params);
		gift = (null == gift ? 0L : gift);
		// 根据公式计算返水金额 : 返水金额 = 下线会员的投注金额 x 返水比例值 - 下线会员的存惠金额
		mxd.setAmount((long) (mxs.getScale().doubleValue() * sum - gift));
		mxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		mxd.setOpttype(2);// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		mxd.setOptuiid(user.getUserId());
		mxd.setOptusername(user.getRealName());
		mxd.setUiid(pxm.getUiid());
		mxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		mxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
		mxd.setParamlog("<|>返水金额 = 下线会员的投注金额 x 返水比例值 - 下线会员的存惠金额<|>"
				+ mxd.getAmount() + " = " + sum + " x "
				+ mxs.getScale().doubleValue());
		// 保存洗码明细记录
		return proxyXimaDetailDao.saveOrUpdate(mxd);
	}

	@Override
	public boolean normalXima(ProxyXimaMain pxm, Date ymdstartDate,
			Date ymdendDate) {
		if (null == pxm || null == ymdendDate || null == ymdstartDate) {
			return false;
		}
		// 获取该会员对应的返水比例值
		ProxyXimaSet mxs = proxyXimaSetDao.queryByUiid(pxm.getUiid());
		if (null == mxs) {
			return false;
		}
		ProxyXimaDetail mxd = new ProxyXimaDetail();
		// 查询指定日期区间中该会员在各游戏平台的投注记录，计算总投注额(按游戏平台分别计算)
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", pxm.getAccount());
		params.put("ymdstart", new java.sql.Date(ymdstartDate.getTime()));
		params.put("ymdend", new java.sql.Date(ymdendDate.getTime()));
		Long sum = betLogDao.selectBackAmount(params);
		sum = (null == sum ? 0L : sum);
		// 获取下级会员的赠送金额
		params.put("paytyple", 2); // 存款方式 0存款，1提款，2赠送，3扣款
		Long gift = payOrderDao.selectGiftAmount(params);
		gift = (null == gift ? 0L : gift);
		// 根据公式计算返水金额 : 返水金额 = 下线会员的投注金额 x 返水比例值 - 下线会员的存惠金额
		mxd.setAmount((long) (mxs.getScale().doubleValue() * sum - gift));
		mxd.setOpttime(new java.sql.Timestamp(new Date().getTime()));
		mxd.setOpttype(0);// opttype 操作类型 0自助洗码，1洗码清零，2强制洗码
		mxd.setOptuiid(pxm.getUiid());
		mxd.setOptusername(pxm.getName());
		mxd.setUiid(pxm.getUiid());
		mxd.setYmdstart(new java.sql.Date(ymdstartDate.getTime()));
		mxd.setYmdend(new java.sql.Date(ymdendDate.getTime()));
		mxd.setParamlog("<|>返水金额 = 下线会员的投注金额 x 返水比例值 - 下线会员的存惠金额<|>"
				+ mxd.getAmount() + " = " + sum + " x "
				+ mxs.getScale().doubleValue());
		// 保存洗码明细记录
		return proxyXimaDetailDao.saveOrUpdate(mxd);
	}
}
