package com.na.manager.action;

import java.util.Collection;

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
import com.na.manager.entity.Game;
import com.na.manager.entity.GameTable;
import com.na.manager.entity.VirtualGameTable;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.IVirtualGameTableService;

/**
 * 虚拟房间action层
 * @create 2017-07
 */
@RestController
@RequestMapping("/virtualGameTable")
@Auth("VirtualGameTable")
public class VirtualGameTableAction {
	
	@Autowired
	private IVirtualGameTableService virtualGameTableService;
	
	@Autowired
	private IGameTableService gameTableService;

	@PostMapping("/search")
	public NaResponse<VirtualGameTableSearchRequest> search(@RequestBody VirtualGameTableSearchRequest searchRequest) {
		return NaResponse.createSuccess(virtualGameTableService.queryVirtualGameTableByPage(searchRequest));
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
	
	@PostMapping("/batchDelete")
	public NaResponse batchDelete(@RequestBody VirtualGameTable gameTable) {
		virtualGameTableService.delete(gameTable);
		return NaResponse.createSuccess();
	}
	
	
	@PostMapping("/update")
	public NaResponse update(@RequestBody VirtualGameTable virtualGameTable) {
		virtualGameTableService.update(virtualGameTable);
		return NaResponse.createSuccess();
	}
	
	/**
	 * 获取游戏列表
	 * @return
	 */
	@PostMapping("/gametables")
	public NaResponse<Object> gameTableList(){
		try {
			Collection<GameTable> allGameTable = AppCache.getAllGameTable();
			return NaResponse.createSuccess(allGameTable);
		} catch (Exception e) {
			return NaResponse.createError(e.getMessage());
		}
	}
	
	/**
	 * 获取游戏列表
	 * @return
	 */
	@PostMapping("/games")
	public NaResponse<Object> gamesList(){
		try {
			Collection<Game> allGameTable = AppCache.getAllGame();
			return NaResponse.createSuccess(allGameTable);
		} catch (Exception e) {
			return NaResponse.createError(e.getMessage());
		}
	}
}
