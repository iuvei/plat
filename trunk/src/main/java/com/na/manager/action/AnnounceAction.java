package com.na.manager.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.AnnounceSearchRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.AnnounceContent;
import com.na.manager.entity.User;
import com.na.manager.entity.UserAnnounce;
import com.na.manager.facade.IAnnounceFacade;
import com.na.manager.service.IAnnounceManageService;

/**
 * 用户公告
 * 
 * @author andy
 * @date 2017年6月26日 下午6:05:00
 * 
 */
@RestController
@RequestMapping("/announcemanage")
@Auth("AnnounceManage")
public class AnnounceAction {
	@Autowired
	private IAnnounceManageService announceManagerService;
	
	@Autowired
	private IAnnounceFacade announceFacade;

	@PostMapping("/search")
	public NaResponse<AnnounceSearchRequest> search(@RequestBody AnnounceSearchRequest searchRequest) {
		return NaResponse.createSuccess(announceManagerService.queryUserAnnouneByPage(searchRequest));
	}
	@PostMapping("/checkSysUser")
	public NaResponse checkSysUser(@RequestBody User user) {
		announceManagerService.checkSysUserExist(user);
		return NaResponse.createSuccess();
	}
	
	@PostMapping("/create")
	public NaResponse create(@RequestBody AnnounceContent announceContent) {
		announceFacade.insertAnnounceConent(announceContent);
		return NaResponse.createSuccess();
	}
	
	@GetMapping("/deleteById/{id}")
	public NaResponse delete(@PathVariable Long id) {
		announceFacade.delete(id);
		return NaResponse.createSuccess();
	}
	
	
	@PostMapping("/updateAnnounce")
	public NaResponse update(@RequestBody UserAnnounce userAnnounce) {
		announceFacade.updateUserAnnounce(userAnnounce);
		return NaResponse.createSuccess();
	}
}
