package com.na.manager.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.na.manager.bean.Page;
import com.na.manager.bean.PercentConfigSearchRequest;
import com.na.manager.dao.IPercentageConfigMapper;
import com.na.manager.entity.PercentageConfig;
import com.na.manager.service.IPercentageConfigService;

/**
 * v
 *
 * @create 2017-07
 */
@Service
public class PercentageConfigServiceImpl implements IPercentageConfigService{

    @Autowired
    private IPercentageConfigMapper percentageConfigMapper;

    @Override
    public Page<PercentageConfig> queryPercentConfigByPage(PercentConfigSearchRequest searchRequest) {
        Page<PercentageConfig> page = new Page<>(searchRequest);
        page.setData(percentageConfigMapper.listPercentConfigByPage(searchRequest));
        page.setTotal(percentageConfigMapper.countPercentConfigs(searchRequest));
        return page;
    }

    @Override
    public void add(PercentageConfig percentageConfig) {
    	PercentConfigSearchRequest searchRequest = new PercentConfigSearchRequest();
    	searchRequest.setUserId(percentageConfig.getUserId());
    	Preconditions.checkNotNull(CollectionUtils.isEmpty(percentageConfigMapper.listPercentConfigByPage(searchRequest)),"room.percentageConfig.exist");
        percentageConfig.setHedgePercentage(percentageConfig.getHedgePercentage().divide(new BigDecimal(100)));
        percentageConfig.setNoHedgePercentage(percentageConfig.getNoHedgePercentage().divide(new BigDecimal(100)));
        percentageConfig.setWaterPercentage(percentageConfig.getWaterPercentage().divide(new BigDecimal(100)));
        Preconditions.checkNotNull(percentageConfig.getUserId(),"param.null");
        percentageConfigMapper.savePercentConfig(percentageConfig);
    }

    @Override
    public void delete(Integer id) {
        Preconditions.checkNotNull(id,"param.null");
        percentageConfigMapper.removePercentConfig(id);
    }

    @Override
    public void update(PercentageConfig percentageConfig) {
        if(percentageConfig.getHedgePercentage() == null){
            percentageConfig.setHedgePercentage(BigDecimal.ZERO);
        }
        if(percentageConfig.getNoHedgePercentage() == null){
            percentageConfig.setNoHedgePercentage(BigDecimal.ZERO);
        }
        if(percentageConfig.getWaterPercentage() == null){
            percentageConfig.setWaterPercentage(BigDecimal.ZERO);
        }
        Preconditions.checkNotNull(percentageConfig.getUserId(),"param.null");
        Preconditions.checkNotNull(percentageConfig.getId(),"param.null");
        percentageConfigMapper.updatePercentConfig(percentageConfig);
    }
}
