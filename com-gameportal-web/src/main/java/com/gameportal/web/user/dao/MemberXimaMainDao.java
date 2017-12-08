package com.gameportal.web.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.MemberClearingFlag;
import com.gameportal.web.user.model.MemberXimaMain;
import com.gameportal.web.user.model.MemberXimaSet;
import com.gameportal.web.user.model.ProxySet;
import com.gameportal.web.user.model.UserXimaSet;
@Component
@SuppressWarnings("all")
public class MemberXimaMainDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return MemberXimaMain.class;
	}
	
	public boolean saveOrUpdate(MemberXimaMain entity) {
		if(entity.getMxmid() == null) 
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else 
			return update(entity);
	}
	
	public List<MemberXimaMain> findMemberXimaList(Map<String,Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect",params);
	}
	
	/**
	 * 代理洗码设置
	 * @param params
	 * @return
	 */
	public ProxySet getProxySetObject(Map<String, Object> params){
		List<ProxySet> list=getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxySet",params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 用户洗码比例
	 * @param params
	 * @return
	 */
	public UserXimaSet getUserXimaSetObject(Map<String, Object> params){
		List<UserXimaSet> list=getSqlMapClientTemplate().queryForList(getSimpleName()+".selectUserXimaSet",params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 会员洗码比例
	 * @param params
	 * @return
	 */
	public MemberXimaSet getMemberXimaSetObject(int gpid,int grade){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gpid", gpid);
		params.put("grade", grade);
		List<MemberXimaSet> list=getSqlMapClientTemplate().queryForList(getSimpleName()+".selectMemberXimaSet",params);
		if(null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 添加洗码标记
	 * @param entity
	 * @return
	 */
	public boolean insertMemberClearingFlag(MemberClearingFlag entity){
		return StringUtils.isNotBlank(ObjectUtils.toString(save(getSimpleName()+".insertMemberClearingFlag",entity))) ? true : false;
	}
	
	/**
	 * 查询戏码记录数。
	 * @param params
	 * @return
	 */
	public Long queryUserClearingFlagCount(Map<String, Object> params){
		return (Long) this.queryForObject(getSimpleName()+".userclearingFlagCount", params);
	}
	
	/**
	 * 查找按天洗码的会员
	 * @param params
	 * @return
	 */
	public List<ProxySet> selectProxyDayClearing(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyDayClearing", params);
	}
}
