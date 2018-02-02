package com.na.gate.action;

import com.na.gate.proto.SocketClient;
import com.na.gate.proto.bean.MerchantJson;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.manager.remote.IUserRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by sunny on 2017/8/1 0001.
 */
//@RestController
//@RequestMapping("/test")
public class TestAction {
    @Autowired
    private IUserRemote userRemote;
    @Autowired
    private IPlatformUserAdapterService platformUserAdapterService;
    @Autowired
    private SocketClient socketClient;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/{userId}/save/{balance}")
    public String saveMoney(@PathVariable("userId") Long userId, @PathVariable("balance") BigDecimal balance){
        userRemote.saveMoney(userId,balance);
        return "ok";
    }

    @GetMapping("/{userId}/draw/{balance}")
    public String drawMoney(@PathVariable("userId") Long userId, @PathVariable("balance") BigDecimal balance){
        userRemote.drawMoney(userId,balance);
        return "ok";
    }

    @PutMapping
    public String add(@RequestBody MerchantJson request){
        platformUserAdapterService.add(request);
        return "ok";
    }

    @GetMapping("login/{userId}")
    public void login(@PathVariable("userId") Long userId){
        socketClient.clientLogin(userId);
    }

    @GetMapping("logout/{userId}")
    public void logout(@PathVariable("userId") Long userId){
        socketClient.loginOut(userId);
    }
}
