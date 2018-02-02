package com.na.user.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.SelectTableCommandPara;
import com.na.user.socketserver.command.sendpara.SelectTableResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.event.SelectTableEvent;

/**
 * Created by sunny on 2017/5/15 0015.
 */
@Cmd(paraCls = SelectTableCommandPara.class,name = "桌子列表")
@Component
public class SelectTableCommand implements ICommand {
	
    @Autowired
    private TableCommand tableCommand;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
        SelectTableCommandPara selectTableCommand = (SelectTableCommandPara)commandReqestPara;
        
        SelectTableResponse selectTableResponse = new SelectTableResponse();
        
        applicationContext.publishEvent(new SelectTableEvent(selectTableResponse));
        selectTableResponse.sort();
        tableCommand.selectTableCommandResponse(client,selectTableResponse);
        return true;
    }
}
