package com.na.baccarat.socketserver.command.flow.user;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.requestpara.ClearCardPara;
import com.na.baccarat.socketserver.command.requestpara.CutCardPara;
import com.na.baccarat.socketserver.command.requestpara.NewBootPara;
import com.na.baccarat.socketserver.command.requestpara.SetCardPara;
import com.na.baccarat.socketserver.command.requestpara.ShuffleEndPara;
import com.na.baccarat.socketserver.command.requestpara.ShufflePara;
import com.na.baccarat.socketserver.command.sendpara.ClearCardResponse;
import com.na.baccarat.socketserver.command.sendpara.CutCardResponse;
import com.na.baccarat.socketserver.command.sendpara.NewBootResponse;
import com.na.baccarat.socketserver.command.sendpara.NewGameResponse;
import com.na.baccarat.socketserver.command.sendpara.SetCardResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleEndResponse;
import com.na.baccarat.socketserver.command.sendpara.ShuffleResponse;
import com.na.baccarat.socketserver.command.sendpara.StartBetResponse;
import com.na.baccarat.socketserver.command.sendpara.StopBetResponse;
import com.na.user.socketserver.command.flow.game.GameAction;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;

/**
 * 百家乐荷官动作
 * 
 * @author alan
 * @date 2017年8月1日 下午3:53:51
 */
public interface BaccaratDealerAction extends GameAction {
	
	public void shuffleEnd(ShuffleEndPara params, SocketIOClient client, ShuffleEndResponse response);
	
	public void shuffle(ShufflePara params, SocketIOClient client, ShuffleResponse response);
	
	public void newBoot(NewBootPara params, SocketIOClient client, NewBootResponse response);
	
	public void newGame(CommandReqestPara params, SocketIOClient client, NewGameResponse response);
	
	public void startBet(CommandReqestPara params, SocketIOClient client, StartBetResponse response);
			
	public void stopBet(CommandReqestPara params, SocketIOClient client, StopBetResponse response);
			
	public void setCard(SetCardPara params, SocketIOClient client, SetCardResponse response);
	
	public void cutCard(CutCardPara params, SocketIOClient client, CutCardResponse response);
	
	public void clearCard(ClearCardPara params, SocketIOClient client, ClearCardResponse response);
	
}
