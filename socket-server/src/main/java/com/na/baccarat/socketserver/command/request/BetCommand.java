package com.na.baccarat.socketserver.command.request;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.flow.BaccaratClassHandler;
import com.na.baccarat.socketserver.command.flow.user.BaccaratPlayerUser;
import com.na.baccarat.socketserver.command.requestpara.BetPara;
import com.na.baccarat.socketserver.command.sendpara.BetResponse;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;

/**
 * 投注.
 * Created by sunny on 2017/5/1 0001.
 */
@Cmd(paraCls = BetPara.class,name = "投注")
@Component
@Transactional(rollbackFor = Exception.class)
public class BetCommand implements ICommand{
    
    @Autowired
    private BaccaratClassHandler baccaratClassHandler;

    @Override
    public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
    	BetPara betPara = (BetPara)commandReqestPara;
    	
        SocketException.isNull(betPara.getTableId(),"param.not.allow.empty");
        SocketException.isTrue(betPara.getBets() == null || betPara.getBets().isEmpty(),"please.bet");
        SocketException.isNull(betPara.getChipsCid() == null,"please.select.chip");
        
        if(betPara.getSource() == null || betPara.getSourceEnum() == null) {
        	throw SocketException.createError("param.not.allow.empty");
        }
        
        if(betPara.getBetType() == null || betPara.getBetTypeEnum() == null) {
        	throw SocketException.createError("param.not.allow.empty");
        }
        
        betPara.getBets().forEach(item->{
            if(item.amount.compareTo(BigDecimal.ZERO)<=0){
                throw SocketException.createError("param.error");
            }
        });
        BetResponse response = new BetResponse();
        
        ((BaccaratPlayerUser)baccaratClassHandler.baccaratAction(client)).bet(betPara, client, response);
        
        return true;
    }

}
