package com.na.manager.entity;


/**
 * 子账号实体
 * 
 * @author alan
 * @date 2017年6月23日 上午10:07:24
 */
public class ChildAccountUser extends User{
	private Long userId;
    private Long parentId;

    private LiveUser parentUser;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LiveUser getParentUser() {
        return parentUser;
    }

    public void setParentUser(LiveUser parentUser) {
        this.parentUser = parentUser;
    }
}
