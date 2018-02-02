package com.na.gate.proto.handler;

import com.google.common.eventbus.AsyncEventBus;
import com.na.gate.proto.bean.CommandEnum;
import com.na.gate.proto.base.Request;
import com.na.gate.proto.util.DecodeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by sunny on 2017/7/21 0021.
 */
public class GateProtoEncoder extends MessageToMessageEncoder<Request> {
    private Logger log = LoggerFactory.getLogger(GateProtoEncoder.class);
    private AsyncEventBus eventBus;

    public GateProtoEncoder(AsyncEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, List<Object> out) throws Exception {
        try {
            ByteBuf buf = ctx.alloc().buffer();
            DecodeUtils.encodeRequest(msg, buf);

            ByteBuf header = ctx.alloc().buffer(8);
            header.writeIntLE(buf.readableBytes()+8);
            CommandEnum cmd = CommandEnum.get(msg.getClass());
            header.writeIntLE(cmd.get());
            out.add(header);
            out.add(buf);
            log.info("cmd:{}",cmd);
            log.debug("cmd:{},\n{}\n{}",cmd,ByteBufUtil.prettyHexDump(header),ByteBufUtil.prettyHexDump(buf));

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
