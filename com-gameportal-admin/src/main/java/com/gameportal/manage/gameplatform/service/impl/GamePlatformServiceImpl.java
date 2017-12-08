package com.gameportal.manage.gameplatform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.manage.gameplatform.dao.GamePlatformDao;
import com.gameportal.manage.gameplatform.model.GamePlatform;
import com.gameportal.manage.gameplatform.service.IGamePlatformService;

@Service
public class GamePlatformServiceImpl implements IGamePlatformService {
	@Resource(name = "gamePlatformDao")
	private GamePlatformDao gamePlatformDao = null;
	private Logger logger = Logger.getLogger(GamePlatformServiceImpl.class);// 日志对象

	public GamePlatformServiceImpl() {
		super();
	}
	
	@Override
	public List<GamePlatform> queryGamePlatform(Long gpid, String gpname,
			Integer startNo, Integer pageSize) {
		return queryGamePlatform(gpid, gpname, null, null, null, null,
				null, startNo, pageSize);
	}
	
	@Override
	public List<GamePlatform> queryGamePlatform(Long gpid, String gpname,
			Integer status, Integer startNo, Integer pageSize) {
		return queryGamePlatform(gpid, gpname, null, null, null, null,
				status, startNo, pageSize);
	}
	
	@Override
	public List<GamePlatform> queryGamePlatform(Long gpid, String gpname,
			String domainname, String domainip, String deskey,
			String ciphercode, Integer status, Integer startNo, Integer pageSize) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(gpid))){
			params.put("gpid", gpid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(gpname))){
			params.put("gpname", gpname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainname))){
			params.put("domainname", domainname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainip))){
			params.put("domainip", domainip);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(deskey))){
			params.put("deskey", deskey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(ciphercode))){
			params.put("ciphercode", ciphercode);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		if (!StringUtils.isNotBlank(ObjectUtils.toString(startNo))
				|| !StringUtils.isNotBlank(ObjectUtils.toString(pageSize))) {
			startNo = 0;
			pageSize = 30;
		}
		if(pageSize == 0){
			pageSize = 30;
		}
		params.put("limit", true);
		params.put("thisPage", startNo);
		params.put("pageSize", pageSize);
		return gamePlatformDao.getList(params);
	}
	
	@Override
	public Long queryGamePlatformCount(Long gpid, String gpname) {
		return queryGamePlatformCount(gpid, gpname, null, null, null, null, null);
	}
	
	@Override
	public Long queryGamePlatformCount(Long gpid, String gpname,
			Integer status) {
		return queryGamePlatformCount(gpid, gpname, null, null, null, null, status);
	}
	
	@Override
	public Long queryGamePlatformCount(Long gpid, String gpname,
			String domainname, String domainip, String deskey,
			String ciphercode, Integer status) {
		Map params = new HashMap();
		if(StringUtils.isNotBlank(ObjectUtils.toString(gpid))){
			params.put("gpid", gpid);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(gpname))){
			params.put("gpname", gpname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainname))){
			params.put("domainname", domainname);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(domainip))){
			params.put("domainip", domainip);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(deskey))){
			params.put("deskey", deskey);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(ciphercode))){
			params.put("ciphercode", ciphercode);
		}
		if(StringUtils.isNotBlank(ObjectUtils.toString(status))){
			params.put("status", status);
		}
		return gamePlatformDao.getRecordCount(params);
	}

	@Override
	public GamePlatform saveGamePlatform(GamePlatform gamePlatform) throws Exception {
		gamePlatform = (GamePlatform) gamePlatformDao.save(gamePlatform);
		return StringUtils.isNotBlank(ObjectUtils.toString(gamePlatform
				.getGpid())) ? gamePlatform : null;
	}

	@Override
	public boolean saveOrUpdateGamePlatform(GamePlatform gamePlatform)
			throws Exception {
		return gamePlatformDao.saveOrUpdate(gamePlatform);
	}

	@Override
	public boolean deleteGamePlatform(Long gpid) throws Exception {
		return gamePlatformDao.delete(gpid);
	}

	@Override
	public GamePlatform queryGamePlatformById(String gpid) {
		return (GamePlatform) gamePlatformDao.findById(gpid);
	}

	@Override
	public List<GamePlatform> getList(Map<String, Object> params) {
		return gamePlatformDao.getList(params);
	}

	@Override
	public GamePlatform getObject(Map<String, Object> params) {
		List<GamePlatform> list = gamePlatformDao.getList(params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}