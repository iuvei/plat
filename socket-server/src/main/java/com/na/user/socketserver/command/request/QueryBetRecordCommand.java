package com.na.user.socketserver.command.request;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.corundumstudio.socketio.SocketIOClient;
import com.na.baccarat.socketserver.command.RequestCommandEnum;
import com.na.user.socketserver.cache.AppCache;
import com.na.user.socketserver.command.ICommand;
import com.na.user.socketserver.command.requestpara.CommandReqestPara;
import com.na.user.socketserver.command.requestpara.QueryBetRecordPara;
import com.na.user.socketserver.command.send.UserCommand;
import com.na.user.socketserver.command.sendpara.QueryBetRecordResponse;
import com.na.user.socketserver.common.annotation.Cmd;
import com.na.user.socketserver.common.enums.ErrorCode;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.exception.SocketException;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.util.DateUtil;

/**
 * 查询投注记录
 * 
 * @author alan
 * @date 2017年5月30日 下午3:07:05
 */
@Component
@Cmd(paraCls = QueryBetRecordPara.class,name = "查询投注记录")
public class QueryBetRecordCommand implements ICommand {
	
	private static Logger log = LoggerFactory.getLogger(QueryBetRecordCommand.class);
	
	@Autowired
	private IBetOrderService betOrderService;
	
	@Autowired
	private UserCommand userCommand;
	
	@Override
	public boolean exec(SocketIOClient client, CommandReqestPara commandReqestPara) {
		QueryBetRecordPara para = (QueryBetRecordPara) commandReqestPara;
		
		if(para == null) {
			para = new QueryBetRecordPara();
		}
		
		if(StringUtils.isEmpty(para.getBeginDate())) {
			para.setBeginDate(DateUtil.getDateBeginTime(new Date(), DateUtil.yyyy_MM_ddHHMMss));
		}
		
		if(StringUtils.isEmpty(para.getEndDate())) {
			para.setEndDate(DateUtil.getDateEndTime(new Date(), DateUtil.yyyy_MM_ddHHMMss));
		}
		
		UserPO user = AppCache.getUserByClient(client);
		if(user == null) {
			throw SocketException.createError(ErrorCode.RELOGIN, "user.not.exist");
		}
		
		Integer total = betOrderService.getBetOrderByUserCount(user.getId(), para.getBeginDate(), para.getEndDate());
		List<Map> list = betOrderService.getBetOrderByUser(user.getId(), para.getRowsNumber(), para.getSize(), para.getBeginDate(), para.getEndDate());
		
		QueryBetRecordResponse response = new QueryBetRecordResponse();
		List<QueryBetRecordResponse.BetInfo> data = new ArrayList<>();
		QueryBetRecordResponse.BetInfo betInfo = null;
		for(Map<String,Object> item : list) {
			long roundId = ((BigInteger) item.get("round_id")).longValue();
			Integer gameId = (Integer) item.get("gameId");
			betInfo = response.new BetInfo();
			betInfo.gameId = gameId;
			betInfo.roundId = roundId;
			betInfo.date = DateUtil.format((Date) item.get("bet_time"),DateUtil.yyyy_MM_ddHHMMss);
            betInfo.game = (String) item.get("gameTableName");
            try {
            	if(item.get("round_result") != null) {
            		betInfo.result = new String(Base64.encodeBase64(((String)item.get("round_result")).getBytes("utf-8")), "utf-8");
            	}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			betInfo.bets = (String) item.get("bets");
			betInfo.winAmount = (BigDecimal) item.get("winLostAmount");
			
			data.add(betInfo);
		}
		
		response.setData(data);
		response.setRows(total);
		userCommand.send(client, RequestCommandEnum.CLIENT_QUERT_BET_RECORD, response);
		return true;
	}
	
	/**
	 * 展示结果
	 * @return
	 */
	public String getShowResult(String result) {
		StringBuffer showResult = new StringBuffer();
		
		char first = result.charAt(0);
		char second = result.charAt(1);
		char third = result.charAt(2);
		if(first == '1') {
			showResult.append("庄,");
		} else if(first == '2') {
			showResult.append("闲,");
		} else {
			showResult.append("和,");
		}
		
		if(second == '1') {
			showResult.append("庄例牌,");
		} else if(second == '2') {
			showResult.append("闲例牌,");
		} else if(second == '3') {
			showResult.append("庄闲例牌,");
		}
		
		if(third == '1') {
			showResult.append("庄对,");
		} else if(third == '2') {
			showResult.append("闲对,");
		} else if(third == '3') {
			showResult.append("庄闲对,");
		}
		
		return showResult.substring(0, showResult.length() - 1).toString();
	}
	
	
	

}
