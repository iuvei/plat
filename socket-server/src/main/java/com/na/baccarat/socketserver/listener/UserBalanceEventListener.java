package com.na.baccarat.socketserver.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.UserBalanceChangeResponse;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.common.event.UserBalanceChangeEvent;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 用户余额变动事件。
 * <ul>
 *     <li>用户在大厅，只发给自己</li>
 *     <li>用户在虚桌，则发给虚桌的所有人</li>
 * </ul>
 *
 * Created by sunny on 2017/5/13 0013.
 */
@Component
public class UserBalanceEventListener implements ApplicationListener<UserBalanceChangeEvent>{
    private final static Logger log = LoggerFactory.getLogger(UserBalanceEventListener.class);

    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private RoomCommand roomCommand;


    @Async
    @Override
    public void onApplicationEvent(UserBalanceChangeEvent userBalanceChangeEvent) {
        Collection<SocketIOClient> clientList = new ArrayList<>();
        Map source = userBalanceChangeEvent.getSource();
        if(source==null){
        	log.error("没找到客户，本次余额变动不予通知");
            return ;
        }
        UserPO userPO = (UserPO) source.get("user");
        String affect = (String) source.get("affect");

        User user = BaccaratCache.getLoginUser(userPO.getId());
        if(user==null || user.getVirtualTableId()==null){
            SocketIOClient client = SocketUtil.getClientByUser(socketIOServer,userPO);
            if(client!=null) {
                clientList.add(client);
            }
        }else {
            log.debug("【余额变动】用户： " + user.getUserPO().getId() + ",当前余额为： " + user.getUserPO().getBalance() + "  source: " + userPO.getBalance());
            Collection<SocketIOClient> virtualTableClients = SocketUtil.getVirtualTableClients(socketIOServer,null,user.getVirtualTableId());
            if(virtualTableClients!=null){
                clientList.addAll(virtualTableClients);
            }
        }

        for(SocketIOClient client:clientList){
            try {
                UserBalanceChangeResponse userBalanceChangeResponse = new UserBalanceChangeResponse(userPO.getId(), userPO.getBalance(), affect);
                roomCommand.send(client, RequestCommandEnum.SERVER_USER_BALANCE_CHANGE, userBalanceChangeResponse);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        };
    }
}
