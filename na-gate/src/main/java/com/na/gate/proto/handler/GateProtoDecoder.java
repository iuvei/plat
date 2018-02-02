package com.na.gate.proto.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.na.gate.proto.bean.CommandEnum;
import com.na.gate.proto.event.OnConnect;
import com.na.gate.proto.event.OnDisConnect;
import com.na.gate.proto.util.DecodeUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * Created by sunny on 2017/7/21 0021.
 */
public class GateProtoDecoder extends MessageToMessageDecoder<ByteBuf> {
    private Logger log = LoggerFactory.getLogger(GateProtoDecoder.class);
    private AsyncEventBus eventBus;


    public GateProtoDecoder(AsyncEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        log.info("len:"+msg.readableBytes());
        log.info("\n"+ByteBufUtil.prettyHexDump(msg));

        int cmd = msg.readIntLE();
        CommandEnum commandEnum = CommandEnum.get(cmd, CommandEnum.Direct.PLATFORM_LIVE);
		log.debug("接收命令【{}】:{}", commandEnum.get(), commandEnum.getDesc());
        if(commandEnum!=CommandEnum.UNKNOWN) {
            Object res = DecodeUtils.decode(msg, commandEnum);
            eventBus.post(res);
            log.info(commandEnum.name()+":"+res.toString());
        }else {
            log.info("未知命令:【{}】 {}",cmd,commandEnum.name());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        eventBus.post(new OnConnect(ctx));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.error("连接断开.....");
        ctx.close();
        eventBus.post(new OnDisConnect(null));
    }
}
