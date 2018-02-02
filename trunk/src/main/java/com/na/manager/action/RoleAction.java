package com.na.manager.action;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.RoleSearchRequest;
import com.na.manager.bean.RoleUpdateRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.Permission;
import com.na.manager.entity.Role;
import com.na.manager.service.IPermissionService;
import com.na.manager.service.IRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunny on 2017/6/22 0022.
 */
@RestController
@RequestMapping("/role")
@Auth("RoleManage")
public class RoleAction {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IPermissionService permissionService;

    @PostMapping("/search")
    public NaResponse<Page> search(@RequestBody RoleSearchRequest pageCondition){
        return NaResponse.createSuccess(roleService.search(pageCondition));
    }

    @PostMapping("/changeStatus")
    public NaResponse changeStatus(@RequestBody Role role){
        roleService.changeStatus(role);
        return NaResponse.createSuccess();
    }

    @PostMapping("/update")
    public NaResponse update(@RequestBody RoleUpdateRequest request){
        roleService.update(request);
        return NaResponse.createSuccess();
    }

    @PostMapping("/add")
    public NaResponse add(@RequestBody RoleUpdateRequest request){
        roleService.add(request);
        return NaResponse.createSuccess();
    }

    @GetMapping("/{roleId}")
    public NaResponse findPermissionByRoleId(@PathVariable("roleId") String roleId){
        NaResponse res =  NaResponse.createSuccess();
        List<Permission> assignPermissionList = permissionService.findPermissionByRoleId(roleId);
        List<Permission> allPermissionList = permissionService.findAllPermission();
        List<Permission> unAssignPermissionList = allPermissionList.stream().filter(p->{
            boolean isHas = false;
            for(Permission temp : assignPermissionList){
                isHas = temp.getPermissionID().equals(p.getPermissionID());
                if(isHas)break;
            }
            return !isHas;
        }).collect(Collectors.toList());

        res.put("unAssignPermissionList", unAssignPermissionList);
        res.put("assignPermissionList", assignPermissionList);
        return res;
    }

}
