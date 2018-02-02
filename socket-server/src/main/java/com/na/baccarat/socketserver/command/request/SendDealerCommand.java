package com.na.baccarat.socketserver.command.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.requestpara.SendDealerPara;
import com.na.baccarat.socketserver.command.send.DealerCommand;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.SendDealerResponse;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.util.SocketUtil;

/**
 * 会员 给荷官发送消息
 * 
 * @author alan
 * @date 2017年5月2日 下午2:22:25
 */
@Cmd(name="给荷官发送消息",paraCls=SendDealerPara.class)
@Component
public class SendDealerCommand implements ICommand {
	
	@Autowired
    private SocketIOServer socketIOServer;
	
	@Autowired
	private DealerCommand dealerCommand;
	
	@Autowired
	private TableCommand tableCommand;

	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		SendDealerPara params = (SendDealerPara) commandReqestPara;
		SendDealerResponse response = new SendDealerResponse();
		
		if (params == null) {
			throw SocketException.createError("param.not.allow.empty");
		}
		User user = BaccaratCache.getUserByClient(client);
		GameTable table = BaccaratCache.getGameTableById(user.getUserPO().getTableId());
		if(table != null) {
//			if("cutCardOver".equals(params.getType())) {
//				//切牌完毕  通知该桌其他用户切牌结束
//				tableCommand.sendAllTableOtherClient(client, null, RequestCommandEnum.CLIENT_CUT_CARD_OVER, user.getUserPO().getTableId());
//			}
			
			User dealer = table.getDealer();
			if(dealer != null) {
				SocketIOClient dealerClient = SocketUtil.getClientByUser(socketIOServer, dealer.getUserPO());
				dealerCommand.sendMessage(dealerClient, response);
			} else {
				throw SocketException.createError("table.not.exist.dealer");
			}
			
		} else {
			throw SocketException.createError("table.not.exist");
		}
		
		
		return false;
	}
}