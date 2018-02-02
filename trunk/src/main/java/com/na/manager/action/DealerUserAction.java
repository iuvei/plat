package com.na.manager.action;

import com.na.manager.bean.DealerUserSearchRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.DealerUser;
import com.na.manager.entity.User;
import com.na.manager.enums.UserType;
import com.na.manager.remote.IGameRemote;
import com.na.manager.service.IDealerUserService;
import com.na.manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/6/26 0026.
 */
@RestController
@RequestMapping("/dealerUser")
@Auth("BetterManage")
public class DealerUserAction {
    @Autowired
    private IDealerUserService dealerUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IGameRemote gameRemote;

    @PostMapping("/search")
    public NaResponse search(@RequestBody DealerUserSearchRequest request){
        return NaResponse.createSuccess(dealerUserService.search(request));
    }

    @PostMapping("/updateStatus")
    public NaResponse updateStatus(@RequestBody User user){
        user.setUserType(UserType.DEALER);
        userService.changeStatus(user.getId(),UserType.DEALER, user.getUserStatusEnum());
        return NaResponse.createSuccess();
    }

    @PostMapping("/updatePassword")
    public NaResponse updatePassword(@RequestBody User user){
        userService.updatePassword(user.getId(),UserType.DEALER,user.getPassword());
        return NaResponse.createSuccess();
    }

    @PostMapping("/add")
    public NaResponse add(@RequestBody DealerUser user){
        dealerUserService.add(user);
        return NaResponse.createSuccess();
    }

    @PostMapping("/update")
    public NaResponse updateUserDetail(@RequestBody DealerUser user){
        user.setUserType(UserType.DEALER);
        userService.update(user);
        return NaResponse.createSuccess();
    }
    
    @PostMapping("/clearRouletteDealer")
    public NaResponse clearRouletteDealer(){
    	try {
    		gameRemote.clearRouletteDealer();
    		return NaResponse.createSuccess();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return NaResponse.createError(e.getMessage());
		}
        
    }

}
