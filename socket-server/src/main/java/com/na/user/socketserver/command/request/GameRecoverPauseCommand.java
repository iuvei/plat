package com.na.user.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.request.StopBetCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.command.request.RouletteStopBetCommand;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.sendpara.GameRecoverPauseResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 游戏恢复暂停
 *
 * v
 *
 * @create 2017-07
 */
@Cmd(paraCls = CommandReqestPara.class,name = "游戏恢复暂停")
@Component
public class GameRecoverPauseCommand implements ICommand {

    @Autowired
    private TableCommand tableCommand;
    @Autowired
    private StopBetCommand baccaratStopBetCommand;
    @Autowired
    private RouletteStopBetCommand rouletteStopBetCommand;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
        UserPO userPO = AppCache.getUserByClient(client);
        if(userPO == null || userPO.getTableId() == null || userPO.getUserTypeEnum() != UserTypeEnum.DEALER){
            throw SocketException.createError("game.not.recoverpause");
        }
        GameTable gameTable = BaccaratCache.getGameTableById(userPO.getTableId());
        RouletteGameTable rouletteGameTable = RouletteCache.getGameTableById(userPO.getTableId());

        Boolean isGamePause = false;
        if(gameTable != null && gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.PAUSE ){
            if(gameTable.getRound() != null && gameTable.getRound().getRoundPO() != null && gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING ){
                GameRecoverPauseResponse response = new GameRecoverPauseResponse();
                response.setTableStatus(gameTable.getOldStatus().get());
                response.setTid(gameTable.getGameTablePO().getId());
                gameTable.setInstantStateEnum(gameTable.getOldStatus());
                gameTable.getRound().getRoundPO().setStatusEnum(gameTable.getRound().getOldStatus());
                gameTable.setOldStatus(null);
                gameTable.getRound().setOldStatus(null);
                tableCommand.sendAllTablePlayer(response, RequestCommandEnum.COMMON_DEALER_GAMERECOVERPAUSE, userPO.getTableId());
                /*if(BaccaratCache.isMultipleTalbe(gameTable.getGameTablePO().getId())){
                    tableCommand.sendAllMultipleUser(response, RequestCommandEnum.COMMON_DEALER_GAMERECOVERPAUSE);
                };*/
                isGamePause = true;
            }
        }
        if(isGamePause == false && rouletteGameTable != null && rouletteGameTable.getInstantStateEnum() == RouletteGameTableInstantStateEnum.PAUSE ){
            if(rouletteGameTable.getRound() != null && rouletteGameTable.getRound().getRoundPO() != null && rouletteGameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING ){
                GameRecoverPauseResponse response = new GameRecoverPauseResponse();
                if(rouletteGameTable.getOldStatus() == null) {
                	response.setTableStatus(RouletteGameTableInstantStateEnum.NEWGAME.get());
                } else {
                	response.setTableStatus(rouletteGameTable.getOldStatus().get());
                }
                response.setTid(rouletteGameTable.getGameTablePO().getId());
                rouletteGameTable.setInstantStateEnum(rouletteGameTable.getOldStatus());
                rouletteGameTable.getRound().getRoundPO().setStatusEnum(rouletteGameTable.getRound().getOldStatus());
                rouletteGameTable.setOldStatus(null);
                rouletteGameTable.getRound().setOldStatus(null);
                tableCommand.sendAllTablePlayer(response, RequestCommandEnum.COMMON_DEALER_GAMERECOVERPAUSE, userPO.getTableId());
                isGamePause = true;
            }
        }
        if(isGamePause == false){
            throw SocketException.createError("game.not.recoverpause");
        }
        return true;
    }
}
