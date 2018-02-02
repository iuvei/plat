package com.na.manager.service;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.RoundCorrectDataRequest;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IRoundService {

	List<String> listGameLand(String bid, String gid, String tid);

	LinkedHashMap<String, Object> listGameResult(Integer pageNumber, Integer pageSize, Integer gid,
			Integer tid, Date startDt, Date endDt);

	List<Map> listAbnormalTableRound(Integer tid);

	Boolean updateRoundAndRoundExt(RoundCorrectDataRequest param) throws ParseException;

	NaResponse<Object> settleBetOrders(RoundCorrectDataRequest param);

}
