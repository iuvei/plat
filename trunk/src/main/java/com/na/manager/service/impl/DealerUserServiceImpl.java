package com.na.manager.service.impl;

import com.google.common.base.Preconditions;
import com.na.manager.bean.DealerUserSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.dao.IDealerUserMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.DealerUser;
import com.na.manager.enums.UserType;
import com.na.manager.service.IDealerUserService;
import com.na.manager.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
@Service
public class DealerUserServiceImpl implements IDealerUserService{
    @Autowired
    private IUserService userService;
    @Autowired
    private IDealerUserMapper dealerUserMapper;

    public Page<DealerUser> search(DealerUserSearchRequest request){
        Page page = new Page(request);
        page.setTotal(dealerUserMapper.count(request));
        if(page.getTotal()>0){
            page.setData(dealerUserMapper.search(request));
        }
        return page;
    }

    @Override
    public void add(DealerUser dealerUser) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dealerUser.getHeadPic()),"request.param.not.null");
        Preconditions.checkArgument(dealerUser.getType()!=null,"request.param.not.null");

        dealerUser.setUserType(UserType.DEALER);
        userService.add(dealerUser);
        dealerUser.setUserId(dealerUser.getId());
        dealerUserMapper.add(dealerUser);
    }

}
