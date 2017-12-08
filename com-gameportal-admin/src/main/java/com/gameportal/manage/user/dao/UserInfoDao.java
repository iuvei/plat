package com.gameportal.manage.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.member.model.UserInfoRemark;
import com.gameportal.manage.system.dao.BaseIbatisDAO;
import com.gameportal.manage.user.model.UserInfo;

@Component
public class UserInfoDao extends BaseIbatisDAO {

	public Class getEntityClass() {
		return UserInfo.class;
	}

	public boolean saveOrUpdate(UserInfo entity) {
		if (entity.getUiid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true : false;
		else
			return update(entity);
	}

	public boolean updateStatus(Map params) {
		int cnt = getSqlMapClientTemplate().update(getSimpleName() + ".updateStatus", params);
		return cnt > 0;
	}

	public List<UserInfo> queryAbove(Map<String, Object> params, Integer startNo, Integer pageSize) {
		return getSqlMapClientTemplate().queryForList(getSimpleName() + ".selectAbove", params, startNo, pageSize);
	}

	public void saveRemark(UserInfoRemark remark) {
		save(getSimpleName() + ".insertRemark", remark);
	}

	public List<UserInfoRemark> getRemarkList(Map<String, Object> map, Integer startNo, Integer pageSize) {
		map.put("thisPage", startNo);
		map.put("pageSize", pageSize);
		return getSqlMapClientTemplate().queryForList(getSimpleName() + ".pageSelectRemark", map);
	}
}
