package commands

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.na.remote.IPlatformUserRemote
import com.na.baccarat.socketserver.cache.BaccaratCache
import com.na.roulette.socketserver.cache.RouletteCache
import com.na.roulette.socketserver.entity.RouletteGameTable
import com.na.user.socketserver.cache.AppCache
import com.na.user.socketserver.common.SpringContextUtil
import com.na.user.socketserver.common.enums.SocketClientStoreEnum
import com.na.user.socketserver.entity.UserPO
import com.na.baccarat.socketserver.entity.User
import com.na.user.socketserver.util.SocketUtil
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum
import org.crsh.cli.Command
import com.na.baccarat.socketserver.command.requestpara.LeaveRoomPara
import com.na.baccarat.socketserver.command.request.LeaveRoomCommand

/**
 * Created by Administrator on 2017/5/15 0015.
 */
import org.crsh.cli.Option
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.crsh.text.ui.UIBuilder

import java.text.SimpleDateFormat

@Usage("查询socket工作情况")
class socketio {
    @Usage("测试")
    @Command
    def test(InvocationContext context) {
        return "Hello"
    }

    @Usage("测试远程调用")
    @Command
    def testRemote(InvocationContext context) {
        IPlatformUserRemote platformUserRemote = SpringContextUtil.getBean(IPlatformUserRemote.class);
        return platformUserRemote.hello("ttt");
    }
    
    @Usage("socketio 客户端连接数")
    @Command
    def clientCount(InvocationContext context){
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Collection<SocketIOClient> clientList = server.getAllClients();
        int allSize = clientList.size();
        int loginedSize = 0;
        int joinRoomSize = 0;
        int joinVRoomSize = 0;
        clientList.each {
            if(AppCache.getUserByClient(it)!=null){
                loginedSize++;
            }
            if(it.get(SocketClientStoreEnum.TABLE_ID.get())){
                joinRoomSize++;
            }
            if(it.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get())){
                joinVRoomSize++;
            }
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        def result =[:];
        result['连接']=allSize;
        result['登录']=loginedSize;
        result['实桌']=joinRoomSize;
        result['虚拟房间']=joinVRoomSize;
        result['时间']=sf.format(new Date());
        return result;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return String.format("共%d连接，已经登录%d, %s.",allSize,loginedSize,sf.format(new Date()));
    }


    @Usage("socketio 客户端连接数")
    @Command
    def clientCountTop(InvocationContext context){
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Collection<SocketIOClient> clientList = server.getAllClients();
        int allSize = clientList.size();
        int loginedSize = 0;
        int joinRoomSize = 0;
        int joinVRoomSize = 0;
        clientList.each {
            if(AppCache.getUserByClient(it)!=null){
                loginedSize++;
            }
            if(it.get(SocketClientStoreEnum.TABLE_ID.get())){
                joinRoomSize++;
            }
            if(it.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get())){
                joinVRoomSize++;
            }
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        def result =[:];
        result['连接']=allSize;
        result['登录']=loginedSize;
        result['实桌']=joinRoomSize;
        result['虚拟房间']=joinVRoomSize;
        result['时间']=sf.format(new Date());

        context.takeAlternateBuffer();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                out.cls()
//                out.show(result);
                context.writer.println(result);
                out.flush();
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
        }
        finally {
            context.releaseAlternateBuffer();
        }

    }
	
	@Usage("所有连接详情")
    @Command
    def allConnect(InvocationContext context) {
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Collection<SocketIOClient> clientList = server.getAllClients();
        def table = new UIBuilder().table(rightCellPadding: 1) {
            header(bold: true, fg: black, bg: white) {
                label("序号");
                label("ip");
                label("SessionId");
                label("状态");
                label("tableId");
                label("vtableId");
            }
            int sn = 0;
            clientList.each {
            	client = it;
                if(it!=null) {
                    row(fg: (sn%2==0 ? green : white) ){
                        label(sn++);
                        label(client==null ? "---" : client.getRemoteAddress().toString());
                        label(client==null ? "---" : client.getSessionId().toString());
                        label(client==null?"killed":"nor");
                        label(client==null ? "" : client.get(SocketClientStoreEnum.TABLE_ID.get()));
                        label(client==null ? "" : client.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get()))
                    }
                }
            }
        }
    }

    @Usage("查看当前登录的用户(根据内存)")
    @Command
    def listUser(InvocationContext context){
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Map<Long,UserPO> userPOMap = AppCache.currentLoginUserMap;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        def table = new UIBuilder().table(rightCellPadding: 1) {
            header(bold: true, fg: black, bg: white) {
                label("序号");
                label("用户ID");
                label("loginName");
                label("ip");
                label("SessionId");
                label("状态");
                label("userTableId");
                label("tableId");
                label("vtableId");
            }
            int sn = 0;
            userPOMap.each {
                UserPO user = it.value;
                SocketIOClient client = SocketUtil.getClientByUser(server,user);
                if(user!=null) {
                    row(fg: (sn%2==0 ? green : white) ){
                        label(sn++);
                        label(user.getId());
                        label(user.loginName);
                        label(client==null ? "---" : client.getRemoteAddress().toString());
                        label(client==null ? "---" : client.getSessionId().toString());
                        label(client==null?"killed":"nor");
                        label(user.tableId);
                        label(client==null ? "" : client.get(SocketClientStoreEnum.TABLE_ID.get()));
                        label(client==null ? "" : client.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get()))
                    }
                }
            }
        }
        return table
    }
    
    @Usage("查看当前登陆用户连接(根据连接)")
    @Command
    def totalUserClient(InvocationContext context){
    	SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
    	
    	def table = new UIBuilder().table(rightCellPadding: 1) {
            header(bold: true, fg: black, bg: white) {
                label("序号");
                label("用户ID");
                label("loginName");
                label("ip");
                label("SessionId");
                label("userTableId");
                label("tableId");
                label("vtableId");
            }
            int sn = 0;
            for(SocketIOClient client : server.getAllClients()) {
	    		UserPO user = AppCache.getUserByClient(client);
	    		if(user != null) {
		    		row(fg: (sn%2==0 ? green : white) ){
	                        label(sn++);
	                        label(user.getId());
	                        label(user.loginName);
	                        label(client==null ? "---" : client.getRemoteAddress().toString());
	                        label(client==null ? "---" : client.getSessionId().toString());
	                        label(user.tableId);
	                        label(client==null ? "" : client.get(SocketClientStoreEnum.TABLE_ID.get()));
	                        label(client==null ? "" : client.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get()))
	                }
	    		}
	    	}
        }
    	
        return true;
    }

    @Usage("统计登录的用户总数")
    @Command
    def countUser(InvocationContext context){
        return AppCache.getCurrentLoginUserMap().size();
    }

    @Usage("统计登录的用户总数")
    @Command
    def getCache(
            InvocationContext context,
                 @Usage("fffff")
                 @Option(names=["c","class"])
                         String clsName) {
        String[] clss = clsName.split(":");
        def obj = getFieldValue(clss[0]);
        if (clss.length == 2) {
            if (obj instanceof Map) {
                return obj.get(clss[2]).toString();
            }
        }
        return JSONObject.toJSONString(obj);
    }

    @Usage("查看指定桌子的连接和客户")
    @Command
    def listClientByTable(InvocationContext context,
                          @Usage("table id")
                            @Option(names=["t","tableId"])
                            Integer tableId){
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Collection<SocketIOClient> clients = SocketUtil.getTableClientList(server,tableId);

        def table = new UIBuilder().table(rightCellPadding: 1) {
            header(bold: true, fg: black, bg: white) {
                label("sessionId");
                label("ip");
                label("userId");
                label("loginName");
                label("tableId");
                label("vTableId");
                label("isChannelOpen");
                label("userTableId");
            }
            int sn = 0;
            clients.each {
                UserPO user = AppCache.getUserByClient(it);
                SocketIOClient client = it;
                if(user!=null) {
                    row(fg: (sn%2==0 ? green : white) ){
                        label(client.sessionId);
                        label(client.remoteAddress);
                        label(user.id);
                        label(user.loginName);
                        label(client.get(SocketClientStoreEnum.TABLE_ID.get()));
                        label(client.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get()));
                        label(client.isChannelOpen());
                        label(client.get(SocketClientStoreEnum.TABLE_ID.get()));
                        label(user.tableId)
                    }
                    sn++;
                }
            }
        }
        return table
    }

    @Usage("查看指定用户的内存信息")
    @Command
    def userCache(InvocationContext context,@Usage("--uid 22")
                  @Option(names=["uid","userId"])
                          Integer userId){
        if(userId==null){
            return "uid 不能为null";
        }

        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);
        Collection<SocketIOClient> clients = server.getAllClients();
        UserPO user = null;
        clients.each {
            SocketIOClient client = it;
            if (client.get(SocketClientStoreEnum.USER_ID.get()).equals(userId+"")) {
                user = AppCache.getUserByClient(client);
                return true;
            }
        }
        return JSON.toJSONString(user,true);

    }
	
	@Usage("查看百家乐的内存信息")
    @Command
    def baccaratCache(InvocationContext context){
        Map<Long,User> userMap = BaccaratCache.getCurrentLoginUserMap();
        SocketIOServer server = SpringContextUtil.getBean(SocketIOServer.class);

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        def table = new UIBuilder().table(rightCellPadding: 1) {
            header(bold: true, fg: black, bg: white) {
                label("序号");
                label("用户ID");
                label("loginName");
                label("ip");
                label("SessionId");
                label("状态");
                label("userTableId");
                label("tableId");
                label("BaccaratCache vtableId");
                label("vtableId");
            }
            int sn = 0;
            userMap.each {
                User user = it.value;
                UserPO userPO = user.getUserPO();
                SocketIOClient client = SocketUtil.getClientByUser(server,userPO);
                if(user!=null) {
                    row(fg: (sn%2==0 ? green : white) ){
                        label(sn++);
                        label(userPO==null ? "---" : userPO.getId());
                        label(userPO==null ? "---" : userPO.loginName);
                        label(client==null ? "---" : client.getRemoteAddress().toString());
                        label(client==null ? "---" : client.getSessionId().toString());
                        label(client==null?"killed":"nor");
                        label(userPO==null ? "---" : userPO.tableId);
                        label(client==null ? "" : client.get(SocketClientStoreEnum.TABLE_ID.get()));
                        label(user.virtualTableId);
                        label(client==null ? "" : client.get(SocketClientStoreEnum.VIRTUAL_TABLE_ID.get()))
                    }
                }
            }
        }
        return table
    }

    @Usage("查看指定轮盘的内存信息")
    @Command
    def rouletteCache(InvocationContext context,@Usage("--tid 22") @Option(names=["tid","tableId"]) Integer tableId){
        if(tableId==null){
            return "tid 不能为null";
        }
        RouletteGameTable table = RouletteCache.getGameTableById(tableId);
        return JSON.toJSONString(table,true);

    }

    static Object getFieldValue(String path) throws Exception {
        int lastDot = path.lastIndexOf(".");
        String className = path.substring(0, lastDot);
        String fieldName = path.substring(lastDot + 1);
        Class myClass = Class.forName(className);

        def myField = myClass.getDeclaredField(fieldName);
        myField.setAccessible(true);
        return myField.get(null);
    }
    
    @Usage("恢复轮盘状态")
    @Command
    def recoverRoulette(InvocationContext context,@Usage("--tid 22") @Option(names=["tid","tableId"]) Integer tableId){
        if(tableId==null){
            return "tid 不能为null";
        }
        RouletteGameTable table = RouletteCache.getGameTableById(tableId);
        
        table.setInstantStateEnum(RouletteGameTableInstantStateEnum.FINISH);
        table.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.FINISH);
        
        return JSON.toJSONString(table,true);
    }
    
    @Usage("清空轮盘荷官")
    @Command
    def clearRouletteDealer(InvocationContext context,@Usage("--tid 22") @Option(names=["tid","tableId"]) Integer tableId){
        if(tableId==null){
            return "tid 不能为null";
        }
        RouletteGameTable table = RouletteCache.getGameTableById(tableId);
        
        table.setDealer(null);
        
        return JSON.toJSONString(table,true);
    }
    
    @Usage("移除百家乐用户")
    @Command
    def removeBaccaratUser(InvocationContext context,@Usage("--uid 22") @Option(names=["uid","userId"]) Integer userId){
        if(userId==null){
            return "uid 不能为null";
        }
        LeaveRoomCommand leaveRoomCom = SpringContextUtil.getBean(LeaveRoomCommand.class);
		SocketIOServer socketIOServer = SpringContextUtil.getBean(SocketIOServer.class);
		
		SocketIOClient client = SocketUtil.getClientByUser(socketIOServer, userId);
		LeaveRoomPara leaveRoomPara = new LeaveRoomPara();
		leaveRoomCom.exec(client, leaveRoomPara);
		
        return "success";
    }
    
    @Usage("直接移除百家乐用户")
    @Command
    def directRemoveBaccaratUser(InvocationContext context,@Usage("--uid 22") @Option(names=["uid","userId"]) Integer userId){
        if(userId==null){
            return "uid 不能为null";
        }
        BaccaratCache.removeUser(userId);
        return "success";
    }
    

}