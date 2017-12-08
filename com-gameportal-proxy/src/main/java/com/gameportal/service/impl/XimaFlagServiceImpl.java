package com.gameportal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameportal.domain.XimaFlag;
import com.gameportal.persistence.XimaFlagMapper;
import com.gameportal.service.BaseService;
import com.gameportal.service.IXimaFlagService;

@Service("ximaFlagService")
public class XimaFlagServiceImpl  extends BaseService implements IXimaFlagService {
	@Autowired
	private XimaFlagMapper ximaFlagMapper;

	@Override
	public int insert(XimaFlag flag) {
		return ximaFlagMapper.insert(flag);
	}

	@Override
	public int update(XimaFlag flag) {
		return ximaFlagMapper.update(flag);
	}

	@Override
	public XimaFlag getNewestXimaFlag(Map<String, Object> param) {
		List<XimaFlag> list = ximaFlagMapper.selectXimaFlag(param);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}
