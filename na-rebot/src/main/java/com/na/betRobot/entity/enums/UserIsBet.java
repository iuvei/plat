package com.na.betRobot.entity.enums;

/**
 * 用户是否能投注枚举。
 * Created by sunny on 2017/5/1 0001.
 */
public enum UserIsBet {
        /**正常*/
        NORMAL(1,"允许投注"),
        /**锁定*/
        LOCKED(2,"禁止投注");

        private int val;
        private String desc;

        public int get() {
            return val;
        }

        UserIsBet(int val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        public static UserIsBet get(int val){
            for(UserIsBet item : UserIsBet.values()){
                if(item.get()==val){
                    return item;
                }
            }
            return null;
        }
    }
