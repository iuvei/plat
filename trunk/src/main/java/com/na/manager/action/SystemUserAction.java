package com.na.manager.action;

import com.google.common.base.Preconditions;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.UserSearchRequest;
import com.na.manager.bean.UserUpdateRoleRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.Role;
import com.na.manager.entity.User;
import com.na.manager.enums.UserType;
import com.na.manager.service.IRoleService;
import com.na.manager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunny on 2017/6/23 0023.
 */
@Auth("SysAccountManage")
@RestController
@RequestMapping("/systemUser")
public class SystemUserAction {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;

    @PostMapping("/search")
    public NaResponse<Page> search(@RequestBody UserSearchRequest request){
        request.setUserType(UserType.SYS.get());
        return NaResponse.createSuccess(userService.search(request));
    }

    @PostMapping("/add")
    public NaResponse add(@RequestBody User user){
        user.setUserType(UserType.SYS);
        userService.add(user);
        return NaResponse.createSuccess();
    }

    @PostMapping("/updatePassword")
    public NaResponse updatePassword(@RequestBody User user){
        userService.updatePassword(user.getId(),UserType.SYS,user.getPassword());
        return NaResponse.createSuccess();
    }

    @PostMapping("/updateStatus")
    public NaResponse updateStatus(@RequestBody User user){
        userService.changeStatus(user.getId(),UserType.SYS,user.getUserStatusEnum());
        return NaResponse.createSuccess();
    }

    @PostMapping("/updateUserRole")
    public NaResponse updateUserRole(@RequestBody UserUpdateRoleRequest request){
        userService.updateUserRole(request.getUserId(),UserType.SYS,request.getRoleIds());
        return NaResponse.createSuccess();
    }

    @GetMapping("/findAllRole")
    public NaResponse findAllRole(){
        return NaResponse.createSuccess(roleService.getAllRole());
    }

    @GetMapping("/findRole/{userId}")
    public NaResponse findRoleByUser(@PathVariable("userId") Long userId){
        NaResponse res = NaResponse.createSuccess();
        List<Role> allRoles = roleService.getAllRole();

        List<Role> hasRoles = roleService.getRoleByUserId(userId);
        List<Role> waitRoles = allRoles.stream().filter(item -> {
            boolean isHas = false;
            for (Role role : hasRoles) {
                isHas = role.getRoleID().equals(item.getRoleID());
                if (isHas) break;
            }
            return !isHas;
        }).collect(Collectors.toList());

        res.put("hasRoles",hasRoles);
        res.put("waitRoles",waitRoles);

        return res;
    }

    @GetMapping("/checkLoginName/{loginName}")
    public NaResponse checkLoginName(@PathVariable("loginName") String loginName){
        User user = userService.findUserByLoginName(loginName);
        Preconditions.checkArgument(user==null,"user.loginname.exist");
        return NaResponse.createSuccess();
    }
}
