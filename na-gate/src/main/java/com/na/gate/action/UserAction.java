package com.na.gate.action;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.enums.RedisKeyEnum;
import com.na.gate.service.IPlatformUserAdapterService;
import com.na.gate.util.SignUtil;
import com.na.gate.vo.FindBetOrderRequestVO;
import com.na.gate.vo.PlatformManageLoginRequest;
import com.na.gate.vo.PlatformResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.remote.FindBetOrderRequest;
import com.na.manager.remote.IUserRemote;

/**
 * Created by sunny on 2017/7/27 0027.
 */
@RequestMapping("/player")
@RestController
public class UserAction {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.na.platform.game.key}")
    private String gameKey;
    @Value("${spring.na.webbackoffice.url}")
    private String webbackOfficeUrl;

    @Autowired
    private IPlatformUserAdapterService platformUserAdapterService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IUserRemote userRemote;

    @PostMapping("/login")
    public PlatformResponse login(@RequestBody PlatformManageLoginRequest request){
        log.info("请求:"+request.toString());

        Preconditions.checkNotNull(request.getId(),"gameId not empty!!");
        Preconditions.checkNotNull(request.getTimestamp(),"timestramp not empty!!");
        Preconditions.checkNotNull(request.getSign(),"sign not empty!!");
        Preconditions.checkArgument(SignUtil.verifySign(request,gameKey),"sign error!");

        PlatformUserAdapter adapter = platformUserAdapterService.findByMerchantUserId(request.getId());
        Preconditions.checkNotNull(adapter,"user name not exist!");

        String token = UUID.randomUUID().toString().replaceAll("-","");
        stringRedisTemplate.boundValueOps(RedisKeyEnum.PLATFORM_USER_TOKEN.get(token)).set(adapter.getLiveUserId()+"",RedisKeyEnum.PLATFORM_USER_TOKEN.getTtl(), TimeUnit.SECONDS);
        return PlatformResponse.createSuccess(webbackOfficeUrl+"#/login?token="+token);
    }

    @PostMapping("/betorder")
    public Page<BetOrderVO> findBetOrder(@RequestBody FindBetOrderRequestVO request){
        Preconditions.checkArgument(SignUtil.verifySign(request,gameKey),"sign error!");
        PlatformUserAdapter adapter = platformUserAdapterService.findByMerchantUserId(request.getUserId());

        FindBetOrderRequest orderRequest = new FindBetOrderRequest();
        orderRequest.setCurrentPage(request.getCurrentPage());
        orderRequest.setPageSize(request.getPageSize());
        orderRequest.setStartTime(request.getStartTime());
        orderRequest.setEndTime(request.getEndTime());
        orderRequest.setParentId(adapter.getLiveUserId());

        return userRemote.findBetOrder(orderRequest);
    }

    @GetMapping("/test")
    public String test(@RequestParam String test){
        return userRemote.test(test);
    }

}
