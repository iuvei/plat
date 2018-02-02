package com.na.manager.dao;

import com.na.manager.bean.PercentConfigSearchRequest;
import com.na.manager.entity.PercentageConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
@Mapper
public interface IPercentageConfigMapper {

    List<PercentageConfig> listPercentConfigByPage(PercentConfigSearchRequest searchRequest);

    long countPercentConfigs(PercentConfigSearchRequest searchRequest);

    void savePercentConfig(PercentageConfig percentageConfig);

    @Delete("delete from percentage_config where id = #{id}")
    void removePercentConfig(@Param("id")Integer id);

    void updatePercentConfig(PercentageConfig percentageConfig);
}
