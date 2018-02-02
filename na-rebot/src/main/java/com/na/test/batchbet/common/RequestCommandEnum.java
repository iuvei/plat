package com.na.test.batchbet.common;

/**
 * 命令集合.
 * <ul>
 * <LI>DEALER_开头的为荷官发出的指令。</LI>
 * <LI>CLIENT_开头的为客户发出的指令。</LI>
 * <LI>COMMON_开头的为多个不同端发出的指令。</LI>
 * <LI>SERVER_开头的为本服务器端发出的指令。</LI>
 * </ul>
 * Created by Sunny on 2017/4/27 0027.
 */
public enum RequestCommandEnum {

    //游戏暂停
    COMMON_DEALER_GAMERECOVERPAUSE("gameRecoverPause","游戏恢复暂停"),
    //游戏暂停
    COMMON_DEALER_GAMEPAUSE("gamePause","游戏暂停"),
	//获取交易项列表
	COMMON_USER_ANNOUNCE("userAnnounce","用户公告"),
	//获取交易项列表
	COMMON_TRADEITEM("tradeItem","交易项列表"),
	//系统错误
	COMMON_SYSTEM("system","系统问题"),
	//修改秘钥
	COMMON_CHANGE_KEY("changeKey","修改秘钥"),
	//桌子状态
	DEALER_TABLESTATUS("tableStatus","桌子状态"),
	//更换荷官
	DEALER_CHANGE("changeDealer","更换荷官"),
	//停止下注
	COMMOM_STOP_BET("stopBet","停止下注"),
	//推送用户最新金额
	COMMON_UPDATE_USER_INFO("updateUser","获取用户最新账户金额"),
	//好路
    COMMON_GOOD_ROADS_CHANGE("goodRoads","好路"),
    //新一靴
    COMMON_NEW_BOOT("newBoot","新一靴"),
    //荷官进入桌子
    DEALER_JOIN ("dealerJoin","荷官进入桌子"),
    // 荷官 获取 当前 下注情况
    DEALER_BETS_INFO("dBetsInfo","荷官 获取 当前 下注情况"),
    // 获取 所有桌子的 路子信息
    DEALER_REQUEST_ROUNDS_INFO ("reqRds","获取 所有桌子的 路子信息"),
    // 荷官发给用户 消息(请求)
    DEALER_CALL_USER_MESSAGE("callUser","荷官发给用户 消息(请求)"),
    // 荷官洗牌
    DEALER_SHUFFLE("shuffle","荷官洗牌"),
    // 荷官洗牌
    DEALER_SHUFFLE_END("shuffleEnd","荷官洗牌结束"),
    // 荷官暂停游戏，注销
    DEALER_PAUSE ( "pause","荷官暂停游戏，注销"),
    // 荷官发牌
    DEALER_SET_CARD ( "setCard","荷官发牌"),
    //新一局
    DEALER_NEW_GAME ( "newGame","新一局"),
    //开始下注
    DEALER_START_BET ( "startBet","开始下注"),
    //荷官离开桌子
    DEALER_LEVEL_TABLE("dealerLeaveTable","荷官离开实桌"),
    // 用户发给荷官 消息(请求)
    SEND_DEALER_MESSAGE("sendDealer","用户发给荷官 消息(请求)"),
    //选择房间
    SELECT_ROOM("selectRoom","选择房间"),
    //加入桌子
    COMMON_JOIN_ROOM("join","加入桌子"),
    //别人加入桌子
    COMMON_OTHER_JOIN_ROOM("otherJoin","别人加入桌子"),
    //离开房间
    COMMON_LEAVE_ROOM("leaveRoom","离开房间"),   
    //别人离开房间
    COMMON_OTHER_LEAVE_ROOM("otherLeaveRoom","别人离开房间"),
    //用户开牌
    CLIENT_USER_CARD("userCard","用户开牌"),
    //结算
    COMMON_GAME_RESULT("gameResult","结算"),
    //登录
    COMMON_LOGIN("loginInfo","登录"),
    COMMON_LOGOUT("logout","退出"),
    
    //推送桌子状态
    SERVER_ALL_TABLE_STATUS("allTableStatus","推送桌子状态"),
    CLIENT_BET("bet","用户投注"),
    SERVER_BET_OTHER("betOther","用户投注，本桌其他用户更新信息指令"),
    SERVER_USER_BALANCE_CHANGE("userBalanceChange","用户余额变动"),
    //用户切牌
    CLIENT_CUT_CARD("cutCard","用户切牌"),
    //用户切牌结束
    CLIENT_CUT_CARD_OVER("cutCardOver","用户切牌结束"),
    CLIENT_SELECT_TABLE("selectTable","查询指定游戏桌子列表，包含路单"),
    //通知用户消息
    CLIENT_USER_MESSAGE("userMessage","用户通知"),
	//用户重连
    CLIENT_RECONNECT("reconnect","用户重连"),
    CLIENT_QUERT_BET_RECORD("queryBetRecord","查询用户投注记录"),
    CLIENT_MODIFY_INFO("modifyInfo","修改资料"),
    CLIENT_USER_INFO("userInfo","获取个人资料"),
    CLIENT_QUICK_CHANGE_ROOM("quickChangeRoom","用户快速换桌"),
    CLIENT_MICARD_OVER("miCardOver","用户咪牌结束"),
    HEART_BEAT("heartBeat","心跳")
    ;

    private String val;
    private String desc;
    public String get(){return val;}
    RequestCommandEnum(String val,String desc){
        this.val = val;
        this.desc = desc;
    }
    public static RequestCommandEnum get(String val){
        for (RequestCommandEnum commandEnum : RequestCommandEnum.values()) {
            if(commandEnum.val.equalsIgnoreCase(val)){
                return commandEnum;
            }
        }
        return null;
    }
}
