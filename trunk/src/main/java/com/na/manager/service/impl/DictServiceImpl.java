package com.na.manager.service.impl;

import com.na.manager.dao.IDictMapper;
import com.na.manager.entity.Dict;
import com.na.manager.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunny on 2017/6/24 0024.
 */
@Service
public class DictServiceImpl implements IDictService {
    @Autowired
    private IDictMapper dictMapper;

    @Override
    public List<Dict> findAllDict() {
        return dictMapper.findAllDict();
    }
}
