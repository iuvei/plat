package com.na.manager.remote;




import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.na.light.LightRpcService;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.entity.AccountRecord;
import com.na.manager.entity.LiveUser;

/**
 * Created by sunny on 2017/7/26 0026.
 */
@LightRpcService("userRemote")
public interface IUserRemote {
    /**
     * 存款
     * @param userId
     * @param balance
     */
    void saveMoney(Long userId, BigDecimal balance);

    /**
     * 取款
     * @param userId
     * @param balance
     */
    void drawMoney(Long userId, BigDecimal balance);

    /**
     * 提取全部存款。
     * @param userId
     */
    BigDecimal drawAllMoney(Long userId);

    /**
     * 增加真人用户.
     * @param addLiveUserRequest
     */
    Long  addLiveUser(AddLiveUserRequest addLiveUserRequest);

    /**
     * 增加子账号。
     * @param username
     * @param parentId
     */
    Long addSubAccount(String username, Long parentId);

    /**
     * 修改用户资料。
     * @param request
     */
    void updateUser(UpdateUserRequest request);

    /**
     * 查看指定用户的账户流水。
     * @param startTime
     * @param userId
     * @return
     */
    List<AccountRecord> findAccountRecordBy(Date startTime, Long userId);

    /**
     * 查看订单记录。
     * @param request
     * @return
     */
    Page<BetOrderVO> findBetOrder(FindBetOrderRequest request);
    
    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    LiveUser getLiveUser(Long userId);
    
    /**
     * 查询未结算订单
     * @param userId
     * @return
     */
    long findUnsettlementOrder(Long userId);
    
    
    /**
     * 查询单局派奖流水
     * @param roundId
     * @return
     */
    List<AccountRecord> findAccountRecordByRoundId(Long roundId);
    
    /**
     * 修改账单标识为已推送
     * @param list
     */
    void updataFlag(List<AccountRecord> list);
    
    String test(String test);
}
