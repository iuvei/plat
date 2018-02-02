package com.na.manager.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.VirtualGameTableSearchRequest;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.GameTable;
import com.na.manager.entity.VirtualGameTable;
import com.na.manager.enums.GameEnum;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.IVirtualGameTableService;

/**
 * 包房action层
 * V
 *
 * @create 2017-07
 */
@RestController
@RequestMapping("/privateRoom")
@Auth("PrivateRoom")
public class PrivateRoomAction {
	
	@Autowired
	private IVirtualGameTableService virtualGameTableService;
	
	@Autowired
	private IGameTableService gameTableService;

	@PostMapping("/search")
	public NaResponse<VirtualGameTableSearchRequest> search(@RequestBody VirtualGameTableSearchRequest searchRequest) {
		return NaResponse.createSuccess(virtualGameTableService.queryVIPRoomByPage(searchRequest));
	}

	/**
	 * 获取百家乐&非竞咪列表
	 * @return
	 */
	@PostMapping("/gametables")
	public NaResponse<Object> gameTableList(){
		try {
			Collection<GameTable> allGameTable = AppCache.getAllGameTable();
			List<GameTable> GameTableList = new ArrayList<>();
			for (GameTable gameTable : allGameTable) {
				if(gameTable.getGameId()==GameEnum.BACCARAT.get() && (gameTable.getType() ==1 || gameTable.getType() ==2) && gameTable.getStatus()==0){ //百家乐&咪牌普通桌&不咪牌普通桌
					GameTableList.add(gameTable);
				}
			}
			return NaResponse.createSuccess(GameTableList);
		} catch (Exception e) {
			return NaResponse.createError(e.getMessage());
		}
	}
	
	/**
	 * 获取该代理占成配置列表
	 * @return
	 */
	@PostMapping("/getPercentageConfigList")
	public NaResponse<Object> getPercentageConfigList(){
		return NaResponse.createSuccess(virtualGameTableService.getPercentageConfig(AppCache.getCurrentUser().getId(), "data", "label"));
	}
	
	@PostMapping("/create")
	public NaResponse create(@RequestBody VirtualGameTable virtualGameTable) {
		virtualGameTable =virtualGameTableService.add(virtualGameTable);
		return NaResponse.createSuccess(virtualGameTable);
	}
	
	@GetMapping("/deleteById/{id}")
	public NaResponse delete(@PathVariable Integer id) {
		virtualGameTableService.delete(id);
		return NaResponse.createSuccess();
	}
	
	
	@PostMapping("/update")
	public NaResponse update(@RequestBody VirtualGameTable virtualGameTable) {
		virtualGameTableService.update(virtualGameTable);
		return NaResponse.createSuccess();
	}
	
	@PostMapping("/modifyPassword")
	public NaResponse modifyPassword(@RequestBody VirtualGameTable virtualGameTable) {
		virtualGameTableService.modifyPassword(virtualGameTable);
		return NaResponse.createSuccess();
	}

}
