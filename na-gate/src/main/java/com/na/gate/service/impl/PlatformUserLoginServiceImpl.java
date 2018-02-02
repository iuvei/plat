package com.na.gate.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.gate.dao.IPlatformUserLoginMapper;
import com.na.gate.service.IPlatformUserLoginService;

/**
 * Created by sunny on 2017/8/9 0009.
 */
@Service
public class PlatformUserLoginServiceImpl implements IPlatformUserLoginService {
    @Autowired
    private IPlatformUserLoginMapper platformUserLoginMapper;

    @Override
    public void add(Long userId) {
        platformUserLoginMapper.add(userId);
    }

    @Override
    public void delete(Long userId) {
        platformUserLoginMapper.delete(userId);
    }

    @Override
    public Date findBy(Long userId) {
        return platformUserLoginMapper.findBy(userId);
    }
}
