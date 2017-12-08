package com.gameportal.manage.betlog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.GameInfoDao;
import com.gameportal.manage.betlog.dao.SysParamDao;
import com.gameportal.manage.betlog.model.GameInfo;
import com.gameportal.manage.betlog.model.SysParam;
import com.gameportal.manage.betlog.service.ISysParamService;
@Service
public class SysParamServiceImpl implements ISysParamService {

	@Resource(name = "sysParamDao")
	private SysParamDao sysParamDao = null;
	@Override
	public boolean deleteById(Long id) {
		return sysParamDao.delete(id);
		
	}

	@Override
	public SysParam findEntityByKey(String key) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("paramkey", key);
		return (SysParam)sysParamDao.queryForObject(SysParam.class.getSimpleName()+".getByKey", map);
		
	}

	@Override
	public boolean modify(SysParam entity) {
		return sysParamDao.saveOrUpdate(entity);
	}

	@Override
	public Long queryForCount(Map map) {
		return sysParamDao.getRecordCount(map);
	}

	@Override
	public List<SysParam> queryForList(Map map, Integer startNo,
			Integer pageSize) {
		List<SysParam> list = sysParamDao.queryForPager(map,
				startNo, pageSize);
		return list;
	}
	@Override
	public SysParam save(SysParam entity) {
		SysParam sysParam= (SysParam) sysParamDao.save(entity);
		return StringUtils.isNotBlank(ObjectUtils.toString(sysParam.getId())) ? sysParam : null;
	}


	

}
