package com.na.user.socketserver.command.sendpara;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.baccarat.socketserver.command.sendpara.GameTableJson;
import com.na.user.socketserver.entity.UserChipsPO;
import com.na.user.socketserver.entity.UserPO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 登录成功响应。
 * Created by sunny on 2017/4/27 0027.
 */
public class LoginInfoResponse {

    /**
     * 用户限红(荷官端不返回)
     */
    @JSONField(name = "userChips")
    private List<UserChipsPO> userChips;

    /**
     * 所有可用游戏桌子列表。(荷官端不返回)
     * key 游戏英文名， val
     */
    @JSONField(name = "gameTableList")
    private List<GameTableJson> gameTableList;

    /***
     * 荷官端才返回。
     */
    @JSONField(name = "gameTable")
    private GameTableJson gameTable;

    //以下为用户信息
    @JSONField(name = "loginId")
    private Long loginId;
    @JSONField(name = "userId")
    private Long userId;
    @JSONField(name = "balance")
    private BigDecimal balance;
    @JSONField(name = "nickName")
    private String nickName;
    @JSONField(name = "userStatus")
    private Integer userStatus;
    @JSONField(name = "typeId")
    private Integer typeId;
    @JSONField(name = "headpic")
    private String headPic;
    @JSONField(name = "loginName")
    private String loginName;
    
    /**
     * 用于断线连接
     */
    private String token;

    /**
     * 用户公告
     */
    private List<LoginInfoResponse.UserAnnounce> userAnnounceList;
    
    /**
     * 用来控制用户可玩游戏的模块
     */
    private List<Integer> gameModules;

    public List<LoginInfoResponse.UserAnnounce> getUserAnnounceList() {
        return userAnnounceList;
    }

    public void setUserAnnounceList(List<LoginInfoResponse.UserAnnounce> userAnnounceList) {
        this.userAnnounceList = userAnnounceList;
    }

    public class UserAnnounce{
        @JSONField(serialize = false)
        public List<Long> uidList;
        public String title;
        public String content;
    }
    public void setUser(UserPO user){
        this.loginId = user.getLoginId();
        this.userId = user.getId();
        this.balance = user.getBalance();
        this.nickName = user.getNickName();
        this.userStatus = user.getUserStatus();
        this.typeId = user.getUserType();
        this.headPic = user.getHeadPic();
        this.loginName = user.getLoginName();
    }




	public void setUserChips(List<UserChipsPO> userChips) {
        this.userChips = userChips;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Long getLoginId() {
        return loginId;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public void setGameTableList(List<GameTableJson> gameTableList) {
        this.gameTableList = gameTableList;
    }

    public GameTableJson getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameTableJson gameTable) {
        this.gameTable = gameTable;
    }

    public List<UserChipsPO> getUserChips() {
        return userChips;
    }

    public List<GameTableJson> getGameTableList() {
        return gameTableList;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

	public List<Integer> getGameModules() {
		return gameModules;
	}

	public void setGameModules(List<Integer> gameModules) {
		this.gameModules = gameModules;
	}
}
