package com.na.manager.action;

import com.na.manager.bean.LoginRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.UserModifyPasswordRequest;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.User;
import com.na.manager.service.IUserService;
import com.na.manager.util.LocalLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@RestController
@Auth(isPublic = true)
public class UserAction  {
    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public NaResponse<User> login(@RequestBody LoginRequest params, HttpServletRequest request){
        User user = userService.login(params.username,params.password,params.captcha, params.token,params.language);
        LocalLanguageUtil.localLanguage(request, user.getLanguageType());

        NaResponse res = NaResponse.createSuccess("user",user);
        res.put("dicts", AppCache.getDictMultimap());
        return res;
    }



    @PostMapping("/modifyPassword")
    public NaResponse modifyPassword(@RequestBody UserModifyPasswordRequest request){
        userService.updatePassword(request.getOldPwd(),request.getNewPwd());
        return NaResponse.createSuccess();
    }
}
