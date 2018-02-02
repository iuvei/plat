package com.na.user.socketserver.common.enums;

public enum VirtualGameTableType {
	UNKNOWN(0,"未知"),
	COMMON(1, "普通房"),
	AGENT_VIP(2, "代理VIP对冲房"),
	COMMON_VIP(3, "代理VIP非对冲房"),
	;
	
	private int val;
    private String desc;

    VirtualGameTableType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static VirtualGameTableType get(int val){
        for (VirtualGameTableType vgType : VirtualGameTableType.values()) {
            if(vgType.get()==val){
                return vgType;
            }
        }
        return UNKNOWN;
    }
}
