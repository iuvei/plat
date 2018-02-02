package com.na.manager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.na.manager.enums.LanguageEnum;
import com.na.manager.enums.UserStatus;
import com.na.manager.enums.UserType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by sunny on 2017/6/21 0021.
 */
public class User implements Serializable{
	private static final long serialVersionUID = 641237128113673450L;
	private Long id;
    private String loginName;
    private String nickName;
    @JSONField(serialize = false)
    private String password;
    private String phone;
    private String email;
    private BigDecimal balance;
    private String barrcode;
    private String headPic;
    private Integer userType;
    private Integer userStatus;
    @JSONField(serialize = false)
    private String passwordSalt;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createBy;
    private List<Role> roles;
    private List<Permission> permissions;
    private Set<Menu> menus = new TreeSet<>();
    private String token;
    private Set<String> roleIds;
    public Integer language;

    public Integer getLanguage() {
        return language == null? LanguageEnum.China.get():language;
    }
    public LanguageEnum getLanguageType() {
        return language == null? LanguageEnum.China:LanguageEnum.get(language);
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalance() {
    	if(balance == null) {
    		balance = new BigDecimal(0);
    	}
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBarrcode() {
        return barrcode;
    }

    public void setBarrcode(String barrcode) {
        this.barrcode = barrcode;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public UserType getUserTypeEnum(){
    	if(userType == null) {
    		return null;
    	}
        return UserType.get(this.userType);
    }
    public void setUserType(UserType userType){
        this.userType = userType.get();
    }

    public UserStatus getUserStatusEnum(){
    	if(userStatus == null) {
    		return null;
    	}
        return UserStatus.get(this.userStatus);
    }
    public void setUserStatusEnum(UserStatus userStatus){
        this.userStatus = userStatus.get();
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void addMenu(Menu menu){
        if(menu!=null) {
            this.menus.add(menu);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
