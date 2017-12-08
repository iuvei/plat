package com.gameportal.web.agent.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gameportal.web.agent.model.ProxyApply;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
@SuppressWarnings("all")
public class ProxyApplyDao extends BaseIbatisDAO{

	@Override
	public Class getEntityClass() {
		return ProxyApply.class;
	}
}
