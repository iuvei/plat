package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratPlayerAction;
import com.na.baccarat.socketserver.command.requestpara.SelectRoomPara;
import com.na.baccarat.socketserver.command.send.RoomCommand;
import com.na.baccarat.socketserver.command.sendpara.SelectRoomResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.VirtualGameTableType;
import com.na.user.socketserver.exception.SocketException;

/**
 * 选择房间
 *
 * @author alan
 * @date 2017年4月27日 下午3:34:58
 */
@Cmd(name = "选择房间", paraCls = SelectRoomPara.class)
@Component
public class SelectRoomCommand implements ICommand {

    private Logger log = LoggerFactory.getLogger(SelectRoomCommand.class);

    @Autowired
    private RoomCommand roomCommand;
    
    @Autowired
	private BaccaratClassHandler baccaratClassHandler;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
        SelectRoomPara params = (SelectRoomPara) commandReqestPara;
        SelectRoomResponse response = new SelectRoomResponse();

        if (params == null) {
            throw SocketException.createError("param.not.allow.empty");
        }
        
        if (params.getTypeEnum() == null || params.getTypeEnum() == VirtualGameTableType.UNKNOWN) {
            throw SocketException.createError("param.not.allow.empty");
        }
        
		((BaccaratPlayerAction)baccaratClassHandler.baccaratAction(client)).selectRoom(params, client, response);

        roomCommand.send(client, RequestCommandEnum.SELECT_ROOM, response);
        return true;
    }

}