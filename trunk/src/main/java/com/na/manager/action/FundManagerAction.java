package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.FundMangerIncomeOperateadminRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.Constant;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.UserType;
import com.na.manager.service.IUserService;

/**
 * 资金管理。
 * Created by sunny on 2017/6/29 0029.
 */
@Auth("FundManager")
@RestController
@RequestMapping("/fundManager")
public class FundManagerAction {
    @Autowired
    private IUserService userService;

    /**
     * 返回运营总账号余额。
     * @return
     */
    @GetMapping("/getOperateadminBalance")
    public NaResponse getOperateadminBalance(){
        User user = userService.findUserByLoginName(Constant.OPERATEADMIN);

        return NaResponse.createSuccess(user.getBalance());
    }

    /**
     * 给运营总账号加钱。
     * @param request
     * @return
     */
    @PostMapping("/incomeOperateadmin")
    public NaResponse incomeOperateadmin(@RequestBody FundMangerIncomeOperateadminRequest request){
        User user = userService.findUserByLoginName(Constant.OPERATEADMIN);
        userService.updateBalance(user.getId(),UserType.LIVE,AccountRecordType.INTO,ChangeBalanceTypeEnum.PROXY,request.getMoney(),request.getRemark());
        return NaResponse.createSuccess();
    }
}
