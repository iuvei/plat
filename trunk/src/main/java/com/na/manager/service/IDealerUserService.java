package com.na.manager.service;

import com.na.manager.bean.DealerUserSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.entity.DealerUser;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
public interface IDealerUserService {
    void add(DealerUser dealerUser);
    Page<DealerUser> search(DealerUserSearchRequest request);
}
