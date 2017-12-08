package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.BetLog;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.model.UserLoginLog;
import com.gameportal.web.user.model.UserSign;

@Component
@SuppressWarnings("all")
public class UserInfoDao extends BaseIbatisDAO {

    @Override
    public Class getEntityClass() {
        return UserInfo.class;
    }

    public void saveOrUpdate(UserInfo entity) {
        if (entity.getUiid() == null) {
            save(entity);
        } else {
            update(entity);
        }
    }

    public List<Map<String, Object>> selectProxyUrl(Map<String, Object> map){
        return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyUrl",map);
    }

    /**
     * 获取电销列表
     * @return
     */
    public List<String> getDXList(){
        return getSqlMapClientTemplate().queryForList(getSimpleName()+".getDXList");
    }

    /**
     * 查询游戏注单
     * @return
     */
    public List<BetLog> selectGameOrder(Map<String, Object> params){
        return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectGameOrder",params);
    }

    /**
     * 统计游戏注单
     * @param params
     * @return
     */
    public Long selectGameOrderCount(Map<String, Object> params){
        return (Long)getSqlMapClientTemplate().queryForObject(getSimpleName()+".selectGameOrderCount", params);
    }

    public int updateLogin(UserInfo userInfo){
        return getSqlMapClientTemplate().update(getSimpleName()+".loginUpdate", userInfo);
    }

    public void insertLog(UserLoginLog entity){
        super.save(getSimpleName()+".insertLog", entity);
    }

    /**
     * 通过条件获取用户的关联账号
     * @param params
     * @return
     */
    public List<String> selectUserLoginLog(Map<String, Object> params){
        return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectUserLoginLog",params);
    }
    
    /**
     * 通过条件获取用户的关联账号
     * @param params
     * @return
     */
    public List<String> selectUserLoginIps(Map<String, Object> params){
        return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectUserLoginIps",params);
    }

    /**
     * 根据条件查询游戏前一定数量的注单
     * @return
     */
    public List<BetLog> selectGameBetLog(Map<String, Object> params){
        return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectGameBetLog",params);
    }
    
    /**
     * 查询所有有转账到MG记录的用户
     * @return
     */
    public List<UserInfo> selectUserOfTransferMg(){
        return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectUserOfTransferMg");
    }
    
    /**
     * 新增会员签到记录
     * @param sign
     */
    public void insertUserSign(UserSign sign){
    	 super.save(getSimpleName()+".insertUserSign", sign);
    }
    
    /**
     * 更新会员签到状态
     * @param sign
     */
    public void updateUserSign(UserSign sign){
    	 super.getSqlMapClientTemplate().update(getSimpleName()+".updateUserSign", sign);
    }
    
    /**
     * 查询签到记录数
     */
    public List<UserSign> queryUserSignList(Map<String,Object> param){
    	return getSqlMapClientTemplate().queryForList(getSimpleName()+".queryUserSignList", param);
    }
}
