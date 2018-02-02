package com.na.baccarat;

import com.corundumstudio.socketio.SocketConfig;
import com.na.SocketServerApplication;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.user.socketserver.common.SpringContextUtil;
import com.na.user.socketserver.config.SocketIoConfig;
import com.na.user.socketserver.dao.IBetOrderMapper;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.impl.AccountRecordServiceImpl;
import com.na.user.socketserver.service.impl.BetOrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={SocketServerApplication.class,SocketIoConfig.class},loader = AnnotationConfigContextLoader.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class TestBetOrder {
    @Autowired
    private IBetOrderService betOrderService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test(){
        System.out.println(applicationContext.toString());
        BetOrder betOrder = new BetOrder();
        betOrder.setTableType(1);
        betOrder.setUserPreBalance(new BigDecimal(20));
        betOrder.setId(123456789L);
        betOrder.setParentId(2L);
        betOrder.setTradeItemKey("33");
        betOrder.setTradeItemId(2);
        betOrder.setSource(3);
        betOrder.setSourceValue("22");
        betOrder.setStatus(2);
        betOrder.setLoginName("test");
        betOrder.setUserId(2263325L);
        betOrder.setUserParentPath("/test");
        betOrder.setAmount(new BigDecimal(2000));

        UserPO userPO = new UserPO();
        userPO.setBalance(new BigDecimal(200));
        userPO.setId(2263325L); 
        for(int i=0;i<10;i++) {
            betOrderService.addBetOrder(userPO, betOrder);
        }
    }
}
