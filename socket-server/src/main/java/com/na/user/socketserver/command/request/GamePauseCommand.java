package com.na.user.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.common.enums.GameTableInstantStateEnum;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.roulette.socketserver.cache.RouletteCache;
import com.na.roulette.socketserver.common.enums.RouletteGameTableInstantStateEnum;
import com.na.roulette.socketserver.entity.RouletteGameTable;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.sendpara.GamePauseResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;

/**
 * 游戏暂停
 *
 * v
 *
 * @create 2017-07
 */
@Cmd(paraCls = CommandReqestPara.class,name = "游戏暂停")
@Component
public class GamePauseCommand implements ICommand {

    @Autowired
    private TableCommand tableCommand;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
        UserPO userPO = AppCache.getUserByClient(client);
        if(userPO == null || userPO.getTableId() == null || userPO.getUserTypeEnum() != UserTypeEnum.DEALER){
            throw SocketException.createError("game.not.pause");
        }
        GameTable gameTable = BaccaratCache.getGameTableById(userPO.getTableId());
        RouletteGameTable rouletteGameTable = RouletteCache.getGameTableById(userPO.getTableId());

        Boolean isGamePause = false;
        if(gameTable != null && gameTable.getRound().getRoundPO().getStatusEnum() == RoundStatusEnum.BETTING ){
        	if(RoundStatusEnum.PAUSE == gameTable.getRound().getRoundPO().getStatusEnum()) {
            	throw SocketException.createError("game.already.pause");
            }
        	
        	if(gameTable.getRound() != null && gameTable.getRound().getRoundPO() != null && gameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING ){
                GamePauseResponse gamePauseResponse = new GamePauseResponse();
                gamePauseResponse.setTableStatus(GameTableInstantStateEnum.PAUSE.get());
                gamePauseResponse.setTid(gameTable.getGameTablePO().getId());
                gameTable.setOldStatus(gameTable.getInstantStateEnum());
                gameTable.getRound().setOldStatus(gameTable.getRound().getRoundPO().getStatusEnum());
                gameTable.setInstantStateEnum(GameTableInstantStateEnum.PAUSE);
                gameTable.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.PAUSE);
                tableCommand.sendAllTablePlayer(gamePauseResponse, RequestCommandEnum.COMMON_DEALER_GAMEPAUSE, userPO.getTableId());
                /*if(BaccaratCache.isMultipleTalbe(gameTable.getGameTablePO().getId())){
                    tableCommand.sendAllMultipleUser(gamePauseResponse, RequestCommandEnum.COMMON_DEALER_GAMEPAUSE);
                };*/
                isGamePause = true;
            }
        }
        if(isGamePause == false && rouletteGameTable != null && rouletteGameTable.getInstantStateEnum() != RouletteGameTableInstantStateEnum.BETTING ){
            if(RoundStatusEnum.PAUSE == rouletteGameTable.getRound().getRoundPO().getStatusEnum()) {
            	throw SocketException.createError("game.already.pause");
            }
        	
        	if(rouletteGameTable.getRound() != null && rouletteGameTable.getRound().getRoundPO() != null && rouletteGameTable.getRound().getRoundPO().getStatusEnum() != RoundStatusEnum.BETTING ){
                GamePauseResponse gamePauseResponse = new GamePauseResponse();
                gamePauseResponse.setTableStatus(RouletteGameTableInstantStateEnum.PAUSE.get());
                gamePauseResponse.setTid(rouletteGameTable.getGameTablePO().getId());
                rouletteGameTable.setOldStatus(rouletteGameTable.getInstantStateEnum());
                rouletteGameTable.getRound().setOldStatus(rouletteGameTable.getRound().getRoundPO().getStatusEnum());
                rouletteGameTable.setInstantStateEnum(RouletteGameTableInstantStateEnum.PAUSE);
                rouletteGameTable.getRound().getRoundPO().setStatusEnum(RoundStatusEnum.PAUSE);
                tableCommand.sendAllTablePlayer(gamePauseResponse, RequestCommandEnum.COMMON_DEALER_GAMEPAUSE, userPO.getTableId());
                isGamePause = true;
            }
        }
        if(isGamePause == false){
            throw SocketException.createError("game.not.pause");
        }
        return true;
    }
}
