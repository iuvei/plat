package com.na.manager.bean;

import java.math.BigDecimal;

/**
 * 运营总账号入账请求。
 * Created by sunny on 2017/6/29 0029.
 */
public class FundMangerIncomeOperateadminRequest {
    private String remark;
    private BigDecimal money;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getRemark() {

        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
