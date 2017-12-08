package com.gameportal.manage.reportform.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.mail.model.EmailLog;
import com.gameportal.manage.member.model.ProxyXimaFlag;
import com.gameportal.manage.reportform.model.PlatMoneyLog;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

/**
 * 平台余额记录Dao
 * @author Administrator
 *
 */
@Component("platMoneyLogDao")
@SuppressWarnings("all")
public class PlatMoneyLogDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return PlatMoneyLog.class;
	}
	
	/**
	 * 保存平台余额数据
	 * @param entity
	 * @return
	 */
	public boolean save(PlatMoneyLog entity) {
		return StringUtils.isNotBlank(ObjectUtils.toString(super.save(entity))) ? true: false;
	}
	
	/**
	 * 平台余额记录
	 * @param params
	 * @return
	 */
	public List<PlatMoneyLog> selectPlatMoneyLog(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectPlatMoneyLog", params);
	}

}
