package com.na.manager.action;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.na.manager.bean.NaResponse;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.GameTable;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.IRoundService;
import com.na.manager.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * game results
 * @author v
 */
@RestController
@RequestMapping("/gameresult")
@Auth("SearchGameResult")
public class GameResultAction {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IRoundService roundService;
	
	@Autowired
	private IGameTableService gameTableService;
	
	/**
	 * gametableList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/gametables")
	public NaResponse<Object> gameTableList(){
		try {
			Collection<GameTable> allGameTable = AppCache.getAllGameTable().stream().filter(item->item.getStatus()==0).collect(Collectors.toList());
			return NaResponse.createSuccess(allGameTable);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
		
	}
	/**
	 * game results detail
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/gameResultList")
	public NaResponse<Object> gameResultList(Integer page,Integer rows,
			String startDt, String endDt,String gtid){
		if(Strings.isNullOrEmpty(gtid))	return NaResponse.createError("param.null");
		if(page == null || rows == null) return NaResponse.createError("param.null");
		if(startDt == null || endDt == null) return NaResponse.createError("param.null");
		try {
			List<String> gtidList = Splitter.on("-").splitToList(gtid);
			if(gtidList.size() != 2) return NaResponse.createError("param.null");
			Date startDate = DateUtil.parseDate(startDt, "yyyy-MM-dd HH:mm:ss");
			Date endDate = DateUtil.parseDate(endDt, "yyyy-MM-dd HH:mm:ss");
			Integer pageNumber = page <= 1 ? 0 : page;
			Integer pageSize = rows < 10 ? 20 : rows;
			return NaResponse.createSuccess(roundService.listGameResult(pageNumber,pageSize,Integer.parseInt(gtidList.get(0)),Integer.parseInt(gtidList.get(1)),startDate,endDate));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError();
		}
		
	}
	

	/**
	 * round results detail
	 * @param bid boot id 
	 * @param gtid gameid-tid
	 * @return round results detail
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/gameLand")
	public NaResponse<Object> gameLand(String bid,String gtid){
		if(Strings.isNullOrEmpty(gtid))	return NaResponse.createError("param.null");
		List<String> gtidList = Splitter.on("-").splitToList(gtid);
		if(gtidList.size() != 2) return NaResponse.createError("param.null");
		try {
			List<String> results = roundService.listGameLand(bid,gtidList.get(0),gtidList.get(1));
			if(results != null){
				results = results.stream().filter(p ->{
					return p != null;
				}).collect(Collectors.toList());
			}
			List<Integer> datas = Lists.transform(results,n -> Integer.parseInt(n));
			LinkedHashMap<String, Object> datamap = Maps.newLinkedHashMapWithExpectedSize(2);
			datamap.put("viewlandtid", gtidList.get(1));
			datamap.put("list", datas);
			return NaResponse.createSuccess(datamap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError();
		}
	}
}
