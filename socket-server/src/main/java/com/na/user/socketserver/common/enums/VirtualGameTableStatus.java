package com.na.user.socketserver.common.enums;

public enum VirtualGameTableStatus {
	CANCEL(0,"暂停"),NORMAL(1, "正常"),;
	
	private int val;
    private String desc;

    VirtualGameTableStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int get(){return val;}

    public String getDesc() {
        return desc;
    }

    public static VirtualGameTableStatus get(int val){
        for (VirtualGameTableStatus tableStatus : VirtualGameTableStatus.values()) {
            if(tableStatus.get()==val){
                return tableStatus;
            }
        }
        return NORMAL;
    }
}
