package com.na.baccarat.socketserver.command.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.baccarat.socketserver.command.requestpara.ChangeDealerPara;
import com.na.baccarat.socketserver.command.send.TableCommand;
import com.na.baccarat.socketserver.command.sendpara.ChangeDealerResponse;
import com.na.baccarat.socketserver.entity.GameTable;
import com.na.baccarat.socketserver.entity.User;
import com.na.remote.IPlatformUserRemote;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.GameEnum;
import com.na.user.socketserver.common.enums.SocketClientStoreEnum;
import com.na.user.socketserver.common.enums.UserTypeEnum;
import com.na.user.socketserver.entity.DealerClassRecordPO;
import com.na.user.socketserver.entity.DealerUserPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IDealerClassRecordService;
import com.na.user.socketserver.service.IUserService;

/**
 * 更换荷官 请求："{'cmd':'changeDealer','body':{tid:'',uid:''}}
 * 
 * @author Administrator
 *
 */
@Cmd(paraCls = ChangeDealerPara.class, name = "更换荷官命令")
@Component
public class ChangeDealerCommand implements ICommand {
	
	private Logger log = LoggerFactory.getLogger(ChangeDealerCommand.class);

	@Autowired
	private IUserService userService;
	@Autowired
	private TableCommand tableCommand;
	@Autowired
	private IDealerClassRecordService dealerClassRecordService;
	@Autowired
	private IPlatformUserRemote platformUserRemote;
	
	@Override
	public boolean exec(SocketIOClient client,
			CommandReqestPara commandReqestPara) {
		ChangeDealerPara params = (ChangeDealerPara) commandReqestPara;
		
		//获取到当前荷官信息
		User preDealer = BaccaratCache.getUserByClient(client);
		if(preDealer == null)
			throw SocketException.createError("user.not.exist");
		UserPO preDealerPO = preDealer.getUserPO();
		GameTable table = BaccaratCache.getGameTableById(preDealerPO.getTableId());

		//查询当前荷官信息
		UserPO nowUserPO = userService.login(params.getBarcode());
        User nowUser = new User(nowUserPO);
		if(nowUserPO == null || nowUserPO.getUserTypeEnum()!= UserTypeEnum.DEALER)
			throw SocketException.createError("login.failure");

		//移除当前桌的荷官
		table.setDealer(null);
		if(null != AppCache.getLoginUser(preDealerPO.getId()))
			AppCache.removeLoginUser(preDealerPO);

		nowUserPO.setTableId(table.getGameTablePO().getId());
		nowUserPO.setInTable(true);
		table.setDealer(nowUser);
		
		//更新app缓存
		AppCache.addLoginUser(nowUserPO);
		BaccaratCache.addLoginUser(nowUser);
		
		DealerClassRecordPO dealerClassRecordPO = dealerClassRecordService.dealerLogout(((DealerUserPO)preDealer.getUserPO()).getDealerClassRecordId());
		dealerClassRecordService.add((DealerUserPO)nowUserPO);
		dealerClassRecordService.dealerJoinTable(((DealerUserPO)nowUserPO).getDealerClassRecordId(), 
				AppCache.getGame(GameEnum.get(client.get(SocketClientStoreEnum.GAME_CODE.get()))), 
				table.getGameTablePO());
		
		try {
			platformUserRemote.sendDealerClassRecord(JSONObject.toJSONString(dealerClassRecordPO));
		} catch (Exception e) {
			log.error("远程调用异常", e);
		}
		
		ChangeDealerResponse response = new ChangeDealerResponse();
		response.setDealerName(nowUserPO.getNickName());
		response.setDealerPic(nowUserPO.getHeadPic());
		response.setDealerUid(nowUserPO.getId());
		response.setLoginName(nowUserPO.getLoginName());
		client.set(SocketClientStoreEnum.USER_ID.get(),nowUserPO.getId()+"");
		//向所有客户端跟新荷官信息
		tableCommand.sendAllTablePlayer(response, RequestCommandEnum.DEALER_CHANGE,table.getGameTablePO().getId());
				
		return true;
	}
}
