package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratDealerAction;
import com.na.baccarat.socketserver.command.sendpara.NewGameResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;

/**
 * 新一局。
 * Created by sunny on 2017/5/1 0001.
 */
@Cmd(paraCls = CommandReqestPara.class,name = "新一局")
@Component
public class NewGameCommand implements ICommand{
    
    @Autowired
	private BaccaratClassHandler baccaratClassHandler;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
        NewGameResponse response = new NewGameResponse();
        
        ((BaccaratDealerAction)baccaratClassHandler.baccaratAction(client)).newGame(commandReqestPara, client, response);
    	return true;
    }
}
