package com.gameportal.web.adver.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gameportal.web.adver.model.Adver;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Repository
@SuppressWarnings("all")
public class AdverDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return Adver.class;
	}

	public List<Adver> queryAllAdver(Map<String, Object> values) {
		return (List<Adver>) queryForList(values);
	}
}
