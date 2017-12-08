package com.gameportal.manage.xima.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.xima.model.MemberXimaSet;
import com.gameportal.manage.xima.model.ProxyXimaSet;

@Component
public class MemberXimaSetDao extends BaseIbatisDAO {

	public Class<MemberXimaSet> getEntityClass() {
		return MemberXimaSet.class;
	}

	public boolean saveOrUpdate(MemberXimaSet entity) {
		if (entity.getMxsid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	/**
	 * 查询洗码配置
	 * @param gpid
	 * @param grade
	 * @return
	 */
	public MemberXimaSet queryByUiid(int gpid,int grade) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gpid", gpid);
		params.put("grade", grade);
		List<MemberXimaSet> list = queryForPager(params, 0, 0);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public MemberXimaSet queryByX(Map<String, Object> params) {
		List<MemberXimaSet> list = queryForPager(params, 0, 0);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<MemberXimaSet> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	}

}
