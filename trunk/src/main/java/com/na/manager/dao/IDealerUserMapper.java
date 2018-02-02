package com.na.manager.dao;

import com.na.manager.bean.DealerUserSearchRequest;
import com.na.manager.entity.DealerUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
@Mapper
public interface IDealerUserMapper {
    long count(DealerUserSearchRequest request);
    List<DealerUser> search(DealerUserSearchRequest request);
    void add(DealerUser user);
}
