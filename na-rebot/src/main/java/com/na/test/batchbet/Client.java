package com.na.test.batchbet;

import io.socket.IOAcknowledge;
import io.socket.IOConnection;
import io.socket.SocketIO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.na.betRobot.entity.UserChips;
import com.na.test.batchbet.common.CommandRequest;
import com.na.test.batchbet.common.CommandResponse;
import com.na.test.batchbet.common.RequestCommandEnum;
import com.na.test.batchbet.para.BetPara;
import com.na.test.batchbet.para.GameTableJson;
import com.na.test.batchbet.para.JoinRoomResponse;
import com.na.test.batchbet.para.LoginInfoPara;
import com.na.test.batchbet.para.LoginInfoResponse;
import com.na.test.batchbet.para.StartBetResponse;
import com.na.test.batchbet.para.UserBetResultResponse;
import com.na.test.batchbet.util.AESEncryptKit;
import com.na.test.batchbet.util.RandUtils;
import com.na.test.batchbet.util.SocketUtil;
import com.na.test.batchbet.util.TimeLeft;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class Client{
    final static Logger logger = LoggerFactory.getLogger(Client.class);
    /**下注起始时间*/
    final static int BET_TIME_START = 10;
//    /**每局下注次数*/
//    final static int BET_TIME_NUM = 5;

    /**
     * 测试桌子类型
     * 1为百家乐
     * 3为轮盘
     */
    final static Integer TEST_GAME = 1;

    /**
     * 测试桌子类型
     * 0为普通桌
     * 1为竞咪桌
     */
    final static Integer TEST_TYPE = 0;

    /**
     * 测试桌子类型
     * 1为座位
     * 2为旁注 未实现
     * 4为多台
     */
    final static Integer JOINROOM_TYPE = 1;

    private String url = null;
    private String key = "0123456789ABCDEF";
    private String oldKey = null;
    private SocketIO socketIO = new SocketIO();
    private LoginInfoResponse loginInfoResponse = null;
    private JoinRoomResponse joinRoomResponse;
    //下注时间
    private Set<Integer> betTimes ;
    private CountTime countTime;

    //当前桌子
    private  GameTableJson gameTableJson;
    //当前限红
    private UserChips userChips;

    private LoginInfoPara loginInfoPara;

    private Map<String,Integer> betMap;

    private Integer virtualTableId;

    private JoinRoomResponse.PlayInfo playInfo;

    //此次投注交易项EKY
    private String tradeItemInfoKey;
    //此次投注交易金额
    private Integer betAmount;

    private TimeLeft timeLeft = new TimeLeft();
    private Timer timer = new Timer();
    /**每局下注次数*/
    private int betTimeNum = 5;

    private final ExecutorService service = Executors.newSingleThreadExecutor();

    public Client(String url, String userName,String pwd,int betTimeNum) {
        this.url = url;
        loginInfoPara = new LoginInfoPara();
        loginInfoPara.setLoginName(userName);
        loginInfoPara.setPwd(pwd);
        loginInfoPara.setDeviceType(1);
        loginInfoPara.setDeviceInfo("robot");
        this.betTimeNum=betTimeNum;
    }

    public void start(){
        try {
            socketIO.connect(url, new SocketCallback(){
                @Override
                public void onDisconnect() {
                    logger.info("onDisconnect");
                }

                @Override
                public void onConnect() {
                    logger.info("onConnect");
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    });
                }

                @Override
                public void onMessage(String data, IOAcknowledge ack) {
                    logger.info("onMessage"+data);
                }

                @Override
                public void onMessage(JSONObject json, IOAcknowledge ack) {
                    logger.info("onMessagejson");
                }

                @Override
                public void on(String event, IOAcknowledge ack, Object... args) {
                    try {
                        com.alibaba.fastjson.JSONObject content = SocketUtil.connectDetrypt((String) args[0], key, oldKey);
                        CommandResponse res = content.toJavaObject(CommandResponse.class);
                        if(res.getTypeEnum()!=RequestCommandEnum.SERVER_ALL_TABLE_STATUS) {
                            logger.info("内容：" + content.toJSONString());
                        }
                        if ("ok".equals(event)) {
                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    commandAction(res);
                                }
                            });

                        } else if ("error".equals(event)) {
                            switch (res.getTypeEnum()){
                                case CLIENT_BET:{
                                    if(RequestCommandEnum.CLIENT_BET == res.getTypeEnum()) {
                                        Integer amount = betMap.get(tradeItemInfoKey);
                                        betMap.put(tradeItemInfoKey, amount - betAmount);
                                    }
                                    logger.error("异常：{} <-> {} <-> {} <-> {}",
                                            socketIO.getConnection().getSessionId(),
                                            loginInfoPara==null ? "" : loginInfoPara.getLoginName(),
                                            res.getType(),
                                            res.getMsg());
                                    break;
                                }
                                case COMMON_LOGIN:{
                                    logger.error("{} {} 登录失败。",socketIO.getConnection().getSessionId(),loginInfoPara.getLoginName());
                                    socketIO.disconnect();
                                    break;
                                }

                            }

                        } else {
                            logger.error("无法识别事件：" + event);
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage(),e);
                    }
                }
            });

//            Thread.sleep(30);

            logger.info(String.format("机器人【%s】自动投注开启成功。",loginInfoPara.getLoginName()));
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    private void login() {
        int waitTime = RandUtils.randNum(5000);
        try {
            Thread.sleep(waitTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        send(RequestCommandEnum.COMMON_LOGIN,loginInfoPara);
    }

    public void send(RequestCommandEnum cmd,Object bean){
        CommandRequest res = new CommandRequest();
        res.setBody(bean);
        res.setCmd(cmd.get());
        String str = SocketUtil.connectEncrypt(res,key);
        socketIO.send(str);
        logger.info("{}- ------------指令：{}",socketIO.getConnection().getSessionId(),cmd.get());
    }


    public void joinRoom(LoginInfoResponse loginInfoResponse) {
        new Thread() {
            @Override
            public void run() {
                List<GameTableJson> gameTableJsonList = null;
                CountDownLatch countDownLatch = new CountDownLatch(0);
                do {
                    logger.info("尝试加入桌子……");
                    gameTableJsonList = loginInfoResponse.getGameTableList().stream().filter(item -> {
//                        if(item.getGid() == TEST_GAME &&
//                                (item.getDealerNickName() != null)) {
//                            return item.getIsBegingMi() == TEST_TYPE;
//                        }
                        return false;
                    }).collect(Collectors.toList());
                    if (gameTableJsonList.size() == 0) {
                        logger.error("没有可用桌子……，2秒后收重试");
                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } while (gameTableJsonList.size() == 0);

//                List<UserChips> userChipsList = loginInfoResponse.getUserChips().stream().filter(item -> item.getGid() == 1).collect(Collectors.toList());
//                gameTableJson = gameTableJsonList.get(RandUtils.randNum(gameTableJsonList.size()));
//                userChips = userChipsList.get(RandUtils.randNum(userChipsList.size()));
//                JoinRoomPara joinRoomPara = new JoinRoomPara();
//                joinRoomPara.setChipId(userChips.getCid());

                if(JOINROOM_TYPE.compareTo(4) != 0) {
//                    joinRoomPara.setTableId(gameTableJson.getTid());
                }
//                joinRoomPara.setType(JOINROOM_TYPE.toString());

//                send(RequestCommandEnum.COMMON_JOIN_ROOM, joinRoomPara);
            }
        }.start();
    }

    public void bet(){
        timeLeft.start();
        BetPara betPara = new BetPara();
//        betPara.setChipsCid(userChips.getCid());
//        betPara.setBetType(BetOrderBetTypeEnum.COMMON.get());
//        betPara.setSource(BetOrderSourceEnum.SEAT.get());
//        betPara.setTableId(gameTableJson.getTid());
//        betPara.setVirtualGameTableId(virtualTableId);
//        String[] amounts = userChips.getJtton().split(",");
//        int amount =  RandUtils.randNum(userChips.getMax().intValue()/11,userChips.getMin().intValue());
//        Integer amount = Integer.valueOf(amounts[RandUtils.randNum(amounts.length)]);
//        if(this.playInfo == null) {
//            JoinRoomResponse.PlayInfo playInfo = joinRoomResponse.getPlayList().get(RandUtils.randNum(joinRoomResponse.getPlayList().size()));
//            this.playInfo = playInfo;
//        }
//        JoinRoomResponse.TradeItemInfo tradeItemInfo = this.playInfo.tradeList.get(RandUtils.randNum(playInfo.tradeList.size()));
//
//        betPara.addBet(amount, tradeItemInfo.id);
//        this.tradeItemInfoKey = tradeItemInfo.key;
//        this.betAmount = amount;
//        if(betMap != null) {
//            betMap.put(tradeItemInfo.key, amount + (betMap.get(tradeItemInfo.key) == null ? 0 : betMap.get(tradeItemInfo.key)));
//        } else {
//            betMap = new HashMap<>();
//            betMap.put(tradeItemInfo.key, amount);
//        }
//        logger.info("{}-【{}/{}】下注【{}】,限红为【{}】.",socketIO.getConnection().getSessionId(),loginInfoPara.getLoginName(),loginInfoResponse.getUserId(),amount,betPara.getChipsCid());
//        send(RequestCommandEnum.CLIENT_BET,betPara);
    }


    private void commandAction(CommandResponse res){
//        logger.info("{}-【响应】数据：{}",socketIO.getConnection().getSessionId(),res.getType());
        switch (res.getTypeEnum()){
            case COMMON_CHANGE_KEY:{
                com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)res.getBody();
                String key = body.getString("key");
                String test = body.getString("test");
                try {
                    String msg = AESEncryptKit.detrypt(test, key);
                    CommandRequest commandRequest = new CommandRequest();

                    commandRequest.setMsg("success");
                    commandRequest.setTest(msg);
                    String str = SocketUtil.connectEncrypt(commandRequest,key);
                    socketIO.emit("changeKey",str);

                    this.oldKey = this.key;
                    this.key = key;

                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }

                break;
            }
            case SERVER_ALL_TABLE_STATUS:{
                if(loginInfoResponse==null){return;}

                com.alibaba.fastjson.JSONArray body = (com.alibaba.fastjson.JSONArray)res.getBody();
                List<GameTableJson> gameTableJsonList = body.toJavaList(GameTableJson.class);
                loginInfoResponse.setGameTableList(gameTableJsonList);
//                for(GameTableJson table : gameTableJsonList){
//                    logger.info(table.toString());
//                }
                break;
            }
            case COMMON_LOGIN:{
                com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)res.getBody();
                loginInfoResponse = body.toJavaObject(LoginInfoResponse.class);
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        IOConnection conn = socketIO.getConnection();
                        conn.transportMessage("2::");
                    }
                }, 10000L, 180*1000);

                logger.info(loginInfoResponse.toString());

                logger.info("尝试加入桌子……");
                joinRoom(loginInfoResponse);
                break;
            }
            case COMMON_JOIN_ROOM:{
                com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)res.getBody();
                joinRoomResponse = body.toJavaObject(JoinRoomResponse.class);
//                joinRoomResponse.setTimeLeft(new AtomicInteger(joinRoomResponse.getCountDown()==null ? 0 : joinRoomResponse.getCountDown()));
                logger.info("{}- 加入桌子成功, 加入类型: {} 座位：{} ",socketIO.getConnection().getSessionId(), JOINROOM_TYPE ,joinRoomResponse.getSeatNum());

                virtualTableId = joinRoomResponse.getVirtualTableId();
//                joinRoomResponse.setTimeLeft(new AtomicInteger(joinRoomResponse.getCountDown()==null ? 0 : joinRoomResponse.getCountDown()));
                break;
            }
            case DEALER_START_BET:{
                com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)res.getBody();
                StartBetResponse startBetResponse = body.toJavaObject(StartBetResponse.class);
                joinRoomResponse.setCountDown(startBetResponse.getCountDown());

                countTime = new CountTime();
                countTime.start();
                break;
            }
            case CLIENT_BET:{
                logger.info("{}- 用户下注：{},时间:{}",socketIO.getConnection().getSessionId(),loginInfoResponse.getUserId(),timeLeft.end());
                break;
            }
            case COMMON_GAME_RESULT: {
                com.alibaba.fastjson.JSONObject body = (com.alibaba.fastjson.JSONObject)res.getBody();
                UserBetResultResponse startBetResponse = body.toJavaObject(UserBetResultResponse.class);

                List<UserBetResultResponse.ResultInfo> resultInfo = startBetResponse.getResultList();

                logger.info("{}- 当前玩法为:{}" ,socketIO.getConnection().getSessionId(), playInfo!=null ? this.playInfo.name : "--");
                for(UserBetResultResponse.ResultInfo item : resultInfo) {
                    logger.info("{}- 交易项:{},投注金额：{},输赢金额:{}",socketIO.getConnection().getSessionId(),item.type,betMap.get(item.type),item.number);
                }
                break;
            }
            case COMMOM_STOP_BET: {
                if(countTime!=null) {
                    countTime.stopThead();
                }
                logger.info("{}- 停止下注",socketIO.getConnection().getSessionId());
                break;
            }
            default:{
//                logger.info("该指令未实现："+res.getType());
                break;
            }
        }
    }

    public void initCountTime(){
        betTimes = RandUtils.randomSet(BET_TIME_START,joinRoomResponse.getCountDown(),betTimeNum);
//        joinRoomResponse.setTimeLeft(new AtomicInteger(joinRoomResponse.getCountDown()));
    }

    public class CountTime extends Thread{
        private boolean isStop = false;
        public CountTime() {
            initCountTime();
        }

        @Override
        public void start(){
            isStop = false;
            super.start();
        }

        public void stopThead(){
            isStop = true;
        }

        @Override
        public void run() {
            super.setName("table-count-thread");
            while (!isStop){
                try {
                    Thread.sleep(1000);
//                    if(Client.this.joinRoomResponse.getTimeLeft()!=null && Client.this.joinRoomResponse.getTimeLeft().get()>0) {
//                        int cur = Client.this.joinRoomResponse.getTimeLeft().decrementAndGet();
//                        if (Client.this.betTimes.contains(cur)) {
//                            bet();
//                        }
//                    }
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }

            }
        }
    }

    public static void main(String[] args) {
//        for (int i=0;i<100;i++) {
//            int[] num = RandUtils.randomCommon(5, 30, 5);
//            for (int n : num) {
//                System.out.print(n+",");
//            }
//            System.out.println();
//        }

        Set<Integer> set = new HashSet();
        set.add(2);
        set.add(3);
        set.add(6);
        System.out.println(set.contains(3));
    }
}
