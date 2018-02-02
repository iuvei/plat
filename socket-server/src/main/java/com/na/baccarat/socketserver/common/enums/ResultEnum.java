package com.na.baccarat.socketserver.common.enums;

/**
 * 开奖结果枚举
 * @author Administrator
 *
 */
public enum ResultEnum {

	BANKER(0,"庄"),
	PLAYER(1,"闲"),
	TIED(2,"和"),
	BBANKERD(3,"庄庄对"), 
	BPLAYERD(4,"庄闲对"),
	TBANKERD(5,"和庄对"),
	TPLAYERD(6,"和闲对"),
	PBANKERD(7,"闲庄对"),
	PPLAYERD(8,"闲闲对"),
	BBPDOUBLE(9,"庄庄对闲对"),
	TBPDOUBLE(10,"和庄对闲对"), 
	PBPDOUBLE(11,"闲庄对闲对"),
	DRAGON(12,""),
	TIGER(13,""),
	TIEDT(14,""),
	ROULETTE(15,""),
	SICBO(16,"");
	
	private Integer val;
	private String desc;
	
	ResultEnum(Integer val,String desc){
		this.val = val;
		this.desc = desc;
	}
	
	
	public int get(){return val;}

    public static ResultEnum get(int val){
        for(ResultEnum item : ResultEnum.values()){
            if(item.get()==val){
                return item;
            }
        }
        return null;
    }
	      
}
