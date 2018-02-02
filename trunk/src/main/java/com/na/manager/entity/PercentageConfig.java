package com.na.manager.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 占成配置
 *
 * @create 2017-07
 */
public class PercentageConfig implements Serializable{

    private static final long serialVersionUID = 3128768606681778591L;

    private Integer id;
    private Long userId;
    //打水占成
    private BigDecimal waterPercentage;
    //对冲占成
    private BigDecimal hedgePercentage;
    //非对冲占成
    private BigDecimal noHedgePercentage;
    
    public String getLid(){
    	return this.getHedgePercentage().doubleValue() + "-" + this.getNoHedgePercentage().doubleValue()+"-"+this.getWaterPercentage().doubleValue();
    }
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getWaterPercentage() {
        return waterPercentage;
    }

    public void setWaterPercentage(BigDecimal waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    public BigDecimal getHedgePercentage() {
        return hedgePercentage;
    }

    public void setHedgePercentage(BigDecimal hedgePercentage) {
        this.hedgePercentage = hedgePercentage;
    }

    public BigDecimal getNoHedgePercentage() {
        return noHedgePercentage;
    }

    public void setNoHedgePercentage(BigDecimal noHedgePercentage) {
        this.noHedgePercentage = noHedgePercentage;
    }
}
