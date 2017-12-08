package com.gameportal.web.adver.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gameportal.web.adver.dao.AdverDao;
import com.gameportal.web.adver.model.Adver;
import com.gameportal.web.adver.service.IAdverService;
/**
 * 广告业务类。
 * @author sum
 *
 */
@Service("adverServiceImpl")
public class AdverServiceImpl implements IAdverService {
	@Resource(name="adverDao")
	private AdverDao adverDao;

	@Override
	public List<Adver> queryAllAdver(Map<String, Object> values) {
		return adverDao.queryAllAdver(values);
	}
}
