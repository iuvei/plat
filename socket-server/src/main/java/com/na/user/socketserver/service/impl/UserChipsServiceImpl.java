package com.na.user.socketserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.na.baccarat.socketserver.entity.UserChips;
import com.na.user.socketserver.dao.IUserChipsMapper;
import com.na.user.socketserver.service.IUserChipsService;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class UserChipsServiceImpl implements IUserChipsService {
	
    @Autowired
    private IUserChipsMapper userChipsMapper;

	@Override
	public List<UserChips> getUserChips(String cid) {
		if (StringUtils.isEmpty(cid)) {
			return null;
		}
		String[] cids = cid.split(",");
		String condition = "";
		int index = 0;
		for (int i = 0; i < cids.length; i++) {
			if (cids[i].equals("0")) {
				continue;
			}
			if (index >= 4) {
				break;
			}
			condition += cids[i];
		}
		return userChipsMapper.findAllByCid(condition);
	}


}
