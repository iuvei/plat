package com.na.manager.action;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.na.manager.bean.ChildAccountUserRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.cache.AppCache;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.Permission;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.service.IChildAccountUserService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IPermissionService;
import com.na.manager.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 子账号
 *
 * @author alan
 * @date 2017年6月23日 上午9:50:18
 */
@RestController
@RequestMapping("/childAccountUser")
@Auth("SubAgentAccountManage")
public class ChildAccountUserAction {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IChildAccountUserService childAccountUserService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ILiveUserService liveUserService;

    /**
     * get local user
     *
     * @return
     */
    @RequestMapping("/getLocalUserNode")
    public NaResponse<Object> getLocalUser() {

        try {
            return NaResponse.createSuccess(userService.findUserById(AppCache.getCurrentUser().getId()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return NaResponse.createError(e.getMessage());
        }

    }

    @RequestMapping("/getAgentAccount")
    public NaResponse<LiveUser> getAgentAccount(Long id) {

        Preconditions.checkNotNull(id, "param.null");
        //判断该真人用户是够在登陆用户的权限下
        LiveUser liveUser = liveUserService.findById(id);
        if (id.longValue() != AppCache.getCurrentUser().getId().longValue()) {
            //判断该真人用户是够在登陆用户的权限下
            Preconditions.checkNotNull(liveUser, "暂无子账号");
            Iterable<String> parentPathIds = Splitter.on("/").split(liveUser.getParentPath());

            Boolean isParent = false;
            for (String ppid : parentPathIds) {
                if (ppid.equals(AppCache.getCurrentUser().getId().toString())) isParent = true;
            }
            if (!isParent) return NaResponse.createError("暂无权限");
        }

        if (LiveUserType.PROXY == liveUser.getTypeEnum() || LiveUserType.GENERAL_PROXY == liveUser.getTypeEnum()) {
            List<AccountVO> liveUsers = liveUserService.findByParentId(id);
            if (liveUsers == null) return NaResponse.createSuccess();
            LinkedList<Map> dataList = Lists.newLinkedList();
            Map<String, Object> data = null;
            for (LiveUser item : liveUsers) {
                if (item == null) continue;
                if(item.getTypeEnum() != LiveUserType.PROXY ) continue;
                data = Maps.newLinkedHashMapWithExpectedSize(4);
                data.put("label", item.getLoginName());
                data.put("data", item.getId());
                data.put("expandedIcon", "fa-folder-open");
                data.put("collapsedIcon", "fa-folder");
                data.put("leaf", false);
                dataList.add(data);
            }
            return NaResponse.createSuccess(dataList);
        }
        return NaResponse.createError("非代理");
    }

    @RequestMapping("/getChild")
    public NaResponse<ChildAccountUser> getChild(Long uid) {

        Preconditions.checkNotNull(uid, "param.null");
        if (uid.longValue() != AppCache.getCurrentUser().getId().longValue()) {
            //判断该真人用户是够在登陆用户的权限下
            LiveUser liveUser = liveUserService.findById(uid);
            Preconditions.checkNotNull(liveUser, "暂无子账号");
            Preconditions.checkNotNull(liveUser.getParentPath(), "system.error");
            Iterable<String> parentPathIds = Splitter.on("/").split(liveUser.getParentPath());

            Boolean isParent = false;
            for (String ppid : parentPathIds) {
                if (ppid.equals(AppCache.getCurrentUser().getId().toString())) isParent = true;
            }
            if (!isParent) return NaResponse.createError("暂无权限");
        }

        List<ChildAccountUser> childUserList = childAccountUserService.findChildUser(uid);
//        if (childUserList != null && childUserList.size() > 0) {
//            return NaResponse.createSuccess(childUserList);
//        } else {
//            return NaResponse.createError("暂无子账号");
//        }
        return NaResponse.createSuccess(childUserList);
    }

    @RequestMapping("/create")
    public NaResponse create(@RequestBody ChildAccountUserRequest request) {

        Preconditions.checkNotNull(request.getLoginName(), "param.null");
        Preconditions.checkNotNull(request.getNickName(), "param.null");
        Preconditions.checkNotNull(request.getPassword(), "param.null");
        Preconditions.checkNotNull(request.getParentId(), "param.null");
        Preconditions.checkArgument(!(request.getPassword().contains(" ") || request.getPassword().length() < 6 || request.getPassword().length() > 15),"childaccountuser.password.warning");
        Preconditions.checkArgument(!(request.getLoginName().contains(" ") || request.getLoginName().length() < 6 || request.getLoginName().length() > 20),"childaccountuser.loginname.warning");
        Preconditions.checkArgument(!(request.getNickName().contains(" ")),"childaccountuser.nickname.error");


        ChildAccountUser childAccountUser = new ChildAccountUser();
        childAccountUser.setLoginName(request.getLoginName());
        childAccountUser.setNickName(request.getNickName());
        childAccountUser.setPassword(request.getPassword());
        childAccountUser.setParentId(request.getParentId());
        childAccountUserService.add(childAccountUser, request.getPermissions());
        return NaResponse.createSuccess();
    }

    @RequestMapping("/update")
    public NaResponse update(@RequestBody ChildAccountUserRequest request) {

        Preconditions.checkNotNull(request.getUserId(), "param.null");
        Preconditions.checkNotNull(request.getNickName(), "param.null");
        Preconditions.checkArgument(!(request.getNickName().contains(" ")),"childaccountuser.nickname.error");



        ChildAccountUser childAccountUser = new ChildAccountUser();
        childAccountUser.setId(request.getUserId());
        childAccountUser.setNickName(request.getNickName());
        childAccountUserService.update(childAccountUser, request.getPermissions());
        return NaResponse.createSuccess("修改成功");
    }

    @RequestMapping("/getSelfPermission/{userId}")
    public NaResponse<Permission> getSelfPermission(@PathVariable("userId") Long userId) {
        Preconditions.checkNotNull(userId, "param.null");
        Preconditions.checkNotNull(userId.equals(AppCache.getCurrentUser().getId()), "user.has.no.permission");
        List<Permission> assignPermissionList = permissionService.findPermissionByChildUserId(userId);

        NaResponse response = NaResponse.createSuccess();
        response.put("unAssignRce", assignPermissionList);

        return response;
    }
    @RequestMapping("/getPermission/{userId}/{parentId}")
    public NaResponse<Permission> getPermission(@PathVariable("userId") Long userId,@PathVariable("parentId") Long parentId) {
        Preconditions.checkNotNull(userId, "param.null");
        Preconditions.checkNotNull(AppCache.getCurrentUser(),"login.first");
        List<Permission> assignPermissionList = permissionService.findPermissionByUserPermission(userId);
        List<Permission> allPermissionList = permissionService.findPermissionByChildUserId(parentId);
        List<Permission> unAssignPermissionList = allPermissionList.stream().filter(p -> {
            boolean isHas = false;
            for (Permission temp : assignPermissionList) {
                isHas = temp.getPermissionID().equals(p.getPermissionID());
                if (isHas) break;
            }
            return !isHas;
        }).collect(Collectors.toList());

        NaResponse response = NaResponse.createSuccess();
        response.put("unAssignRce", unAssignPermissionList);
        if (assignPermissionList.size() > 0) {
            response.put("assignRce", assignPermissionList);
        }
        return response;
    }

    @RequestMapping("/setStatus")
    public NaResponse setStatus(@RequestBody ChildAccountUser request) {
        Preconditions.checkNotNull(request.getId(),"param.null");
        ChildAccountUser childAccountUser = new ChildAccountUser();
        childAccountUser.setId(request.getId());
        userService.updateUserStatus(childAccountUser);
        return NaResponse.createSuccess("修改成功");
    }

    @RequestMapping("/changePassword")
    public NaResponse changePassword(@RequestBody ChildAccountUser request) {

        Preconditions.checkNotNull(request.getPassword(), "param.null");
        Preconditions.checkArgument(!(request.getPassword().contains(" ") || request.getPassword().length() < 6 || request.getPassword().length() > 15),"childaccountuser.password.warning");

        ChildAccountUser childAccountUser = new ChildAccountUser();
        childAccountUser.setId(request.getId());
        childAccountUser.setPassword(request.getPassword());
        List<ChildAccountUser> childUser = childAccountUserService.findChildUser(AppCache.getCurrentUser().getId());
        Boolean isChange = false;
        for (ChildAccountUser item:childUser) {
            if(item.getId().equals(request.getId())) isChange = true;
        }
        Preconditions.checkNotNull(isChange, "user.has.no.permission");
        userService.updatePassword(request.getId(),UserType.SUB_ACCOUNT,request.getPassword());
        return NaResponse.createSuccess("修改成功");
    }

}
