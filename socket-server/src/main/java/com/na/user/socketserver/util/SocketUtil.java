package com.na.user.socketserver.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.ResponseCommandEnum;
import com.na.baccarat.socketserver.entity.User;
import com.na.baccarat.socketserver.entity.VirtualGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.sendpara.CommandResponse;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.config.QuartzConfig;
import com.na.user.socketserver.constant.Constant;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.task.DeleteKeyTask;
import com.na.user.socketserver.util.aes.AESEncryptKit;

/**
 * Created by sunny on 2017/4/27 0027.
 */
public class SocketUtil {
    private static Logger log = LoggerFactory.getLogger(SocketUtil.class);

    /**
     * 通讯解密和验签
     * @return
     */
    public static JSONObject connectDetrypt(final SocketIOClient client, String jsonStr, String key, SchedulerFactoryBean schedulerFactoryBean) {
        if (client == null) {
            log.warn("Client is logout client UUID:{}" , client.getSessionId());
            throw new RuntimeException("Client is logout");
        }
        if(log.isDebugEnabled()) {
//            log.debug(" old key :{}", client.get(Constant.SECRET_OLD_KEY));
//            log.debug(" now key :{}", client.get(Constant.SECRET_KEY));
//            log.debug(" new key :{}", client.get(Constant.SECRET_NEW_KEY));
        }
        //解密内容
        String content = null;
        try {
        	if(key == null) {
        		throw new BadPaddingException("秘钥为空");
        	}
    		content = AESEncryptKit.detrypt(jsonStr, key);
    		if(client.has(Constant.SECRET_OLD_KEY)) {
    			Scheduler scheduler = schedulerFactoryBean.getScheduler();
				if(!scheduler.checkExists(new JobKey(DeleteKeyTask.class.getName() + client.getSessionId()))) {
					//启动删除秘钥倒计时线程
					Map<String,Object> dataMap = new HashMap<>();
					dataMap.put("client", client);
					QuartzConfig.addJob(schedulerFactoryBean, DeleteKeyTask.class, 
							DeleteKeyTask.class.getName() + client.getSessionId(), 
							DateUtil.nextMinuteDate(null, 1), dataMap);
				}
    		}
        } catch (BadPaddingException e) {
            try {
                if(client.has(Constant.SECRET_OLD_KEY)) {
                    content = AESEncryptKit.detrypt(jsonStr, client.get(Constant.SECRET_OLD_KEY));
                    log.debug("【Client SessionId】{}  Detrypt content:" , client.getSessionId() , content);
                } else {
                    throw SocketException.createError("connect.error.please.reconnect");
                }
            } catch (Exception e1) {
            	log.error("Detrypt fail：", e1);
                throw SocketException.createError("connect.error.please.reconnect");
            }
        } catch (IllegalBlockSizeException e) {
            log.error("Detrypt fail：", e);
            throw SocketException.createError("server.decode.failure");
        } catch (Exception e) {
        	log.error("Detrypt fail：", e);
            throw SocketException.createError("server.decode.failure");
        }
        JSONObject params = JSONObject.parseObject(content,Feature.OrderedField);
        if(!"heartBeat".equals(params.get("cmd"))) {
        	log.debug("【解密】 Detrypt content:{}",content);
        }
        
        String signMsg = params.remove("signMsg") + "";
        String signSelf = MD5SignatoryKit.getMD5(JSONObject.toJSONString(params).getBytes());

        if(!signMsg.equals(signSelf)) {
//            log.error("Validate signator fail：" + params);
//            throw SocketException.createError("服务器验签失败");
        } else {
//            log.debug("Validate signator success");
        }

        return params;
    }

    /**
     * 签名。
     * @param response
     * @return
     */
    private static String sign(CommandResponse response){
        String json = JSONObject.toJSONString(response);
        String signMsg = MD5SignatoryKit.getMD5(json.getBytes());
        response.setSignMsg(signMsg);
        return JSONObject.toJSONString(response);
    }

    /**
     * 对输入内容加密。
     * @param context
     * @param key
     * @return
     */
    private static String encrypt(String context,String key){
        try {
            if(key == null) {
                throw new BadPaddingException("秘钥为空");
            }
            String encryMsg = AESEncryptKit.encrypt(context, key);
            return Base64.byteArrayToBase64(ZLibUtils.compress(encryMsg.replace("\r\n", "").getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    /**
     * 发送指令。
     * @param client
     * @param responseCommandEnum
     * @param response
     */
    public static void send(SocketIOClient client,ResponseCommandEnum responseCommandEnum, CommandResponse response){
    	if (client == null || !client.isChannelOpen()) {
            log.debug("client.already.disconnect");
            return;
        }
        if(!RequestCommandEnum.SERVER_ALL_TABLE_STATUS.get().equals(response.getType())
        		&& !RequestCommandEnum.HEART_BEAT.get().equals(response.getType())) {
            log.debug("【发送】：[{}] -> 进入send方法,命令： {}",client.getRemoteAddress().toString(), response.getType());
        }
        String signResponse = sign(response);
    	String content = encrypt(signResponse,client.get(Constant.SECRET_KEY));
        client.sendEvent(responseCommandEnum.get(), content);

        if(!RequestCommandEnum.SERVER_ALL_TABLE_STATUS.get().equals(response.getType())
                && !RequestCommandEnum.SERVER_BET_OTHER.get().equals(response.getType())
                && !RequestCommandEnum.COMMON_GAME_RESULT.get().equals(response.getType())
                && !RequestCommandEnum.SERVER_USER_BALANCE_CHANGE.get().equals(response.getType())
                && !RequestCommandEnum.HEART_BEAT.get().equals(response.getType())
                ) {
            String userId = client.get(SocketClientStoreEnum.USER_ID.get());
        	log.info("【发送】用户[{}] ip [{}] 成功, 命令： {},内容：{}",userId,client.getRemoteAddress().toString(), response.getType(),signResponse);
        }
    }
    
    /**
     * 返回指定User的client.
     *
     * @param server
     * @return
     */
    public static SocketIOClient getClientByUser(SocketIOServer server, Long userId) {
        if (userId == null) return null;
        
        List<SocketIOClient> clientList = server.getAllClients()
        .stream()
        .filter((item) -> {
            UserPO user = AppCache.getUserByClient(item);
            if (user == null)
                return false;
            else if(user.getId().compareTo(userId) == 0) {
            	return true;
            }
            return false;
        })
        .collect(Collectors.toList());
        if(clientList != null && clientList.size() > 0) {
        	return clientList.get(0);
        }
        return null;
    }
    
    /**
     * 返回指定User的client.
     *
     * @param server
     * @param targetUser
     * @return
     */
    public static SocketIOClient getClientByUser(SocketIOServer server, UserPO targetUser) {
        if (targetUser == null) return null;
        return getClientByUser(server, targetUser.getId());
    }
    
    /**
     * 返回指定User集合的所有client.
     *
     * @param server
     * @param userList
     * @return
     */
    public static Collection<SocketIOClient> getClientByUserList(SocketIOServer server, Collection<User> userList) {
    	Collection<UserPO> userPOList = new HashSet<>();
    	for(User user: userList) {
    		userPOList.add(user.getUserPO());
    	}
    	return getClientByUserPOList(server, userPOList);
    }
    
    /**
     * 返回指定User集合的所有client.
     *
     * @param server
     * @param userList
     * @return
     */
    public static Collection<SocketIOClient> getClientByUserPOList(SocketIOServer server, Collection<UserPO> userList) {
        if (userList==null || userList.size() < 1) return new HashSet<>();
        return server.getAllClients()
                .stream()
                .filter((item) -> {
                    UserPO user = AppCache.getUserByClient(item);
                    if (user == null)
                        return false;
                    else if(userList.contains(user)) {
                    	return true;
                    }
                    return false;
                })
                .collect(Collectors.toSet());
    }

    /**
     * 返回指定tableID的所有client.
     *
     * @param server
     * @param tableId
     * @return
     */
    public static Collection<SocketIOClient> getTableClientList(SocketIOServer server, Integer tableId) {
        if (tableId==null) return new HashSet<>();
        
        Collection<SocketIOClient> clientColl = new HashSet<>();
        if(BaccaratCache.isMultipleTalbe(tableId)) {
        	Collection<User> userList = BaccaratCache.getMultipleUserMap().values();
        	clientColl.addAll(getClientByUserList(server, userList));
        }
        
        clientColl.addAll(server.getAllClients()
                .stream()
                .filter((item) -> {
                    UserPO user = AppCache.getUserByClient(item);
                    if (user == null || user.getTableId() == null)
                        return false;
                    else
                        return user.getTableId().compareTo(tableId) == 0;
                })
                .collect(Collectors.toSet()));
        
        if(log.isDebugEnabled()) {
        	for(SocketIOClient client : clientColl) {
            	UserPO user = AppCache.getUserByClient(client);
            	log.debug("【通知】用户：" + user.getLoginName() + ",通知用户数：" + clientColl.size());
            }
        }
        return clientColl;
    }
    
    /**
     * 返回该实体桌其他client.(不包含多台用户)
     *
     * @param server
     * @param tableId
     * @return
     */
    public static Collection<SocketIOClient> getTableOtherClientList(SocketIOServer server, Integer tableId, SocketIOClient client) {
        if (tableId==null) return new ArrayList<>();

        return server.getAllClients()
                .stream()
                .filter((item) -> {
                    UserPO user = AppCache.getUserByClient(item);
                    if (user == null || user.getTableId() == null || item == client)
                        return false;
                    else
                        return user.getTableId().equals(tableId);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取虚桌的所有其他用户的client(包括自己)。
     *
     * @param server
     * @param client
     * @return
     */
    public static Collection<SocketIOClient> getVirtualTableClients(SocketIOServer server, SocketIOClient client, Integer vTableId) {
    	if(!StringUtils.isEmpty(vTableId)) {
    		VirtualGameTable virtualTable = BaccaratCache.getVirtualTableById(vTableId);
    		Map<Integer, Long> userMap = BaccaratCache.getVirtualTableSeatUser(virtualTable.getVirtualGameTablePO().getGameTableId(), virtualTable.getVirtualGameTablePO().getId());
    		Collection<SocketIOClient> clients = null;
    		clients = getClientByUserPOList(server, AppCache.getLoginUserList(userMap.values()));
    		
    		return clients;
    	}
    	
		return null;
    }
    
    /**
     * 获取虚桌的所有其他用户的client(不包括自己)。
     *
     * @param server
     * @param client
     * @return
     */
    public static Collection<SocketIOClient> getOtherVirtualTableClients(SocketIOServer server, SocketIOClient client, Integer vTableId) {
    	if (client==null) return new ArrayList<>();
    	
    	Collection<SocketIOClient> clients = getVirtualTableClients(server, client, vTableId);
    	if (clients == null) {
    		return null;
    	}
		return clients.stream().filter( item -> {
			if(item != client) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
    	
    }
    
    
}
