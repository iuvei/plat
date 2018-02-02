package com.na.manager.service;

import com.na.manager.bean.Page;
import com.na.manager.bean.PercentConfigSearchRequest;
import com.na.manager.entity.PercentageConfig;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IPercentageConfigService {
    Page<PercentageConfig> queryPercentConfigByPage(PercentConfigSearchRequest searchRequest);

    void add(PercentageConfig percentageConfig);

    void delete(Integer id);

    void update(PercentageConfig percentageConfig);
}
