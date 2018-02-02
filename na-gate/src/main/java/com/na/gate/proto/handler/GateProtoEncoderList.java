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
public class GateProtoEncoderList extends MessageToMessageEncoder<List<Request>> {
    private Logger log = LoggerFactory.getLogger(GateProtoEncoderList.class);
    private AsyncEventBus eventBus;

    public GateProtoEncoderList(AsyncEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, List<Request> msg, List<Object> out) throws Exception {
    	 try {
    		 if(msg.size()==0) return;
    		 Class<?> cls = msg.get(0).getClass();
             ByteBuf buf = ctx.alloc().buffer();
             DecodeUtils.encodeRequest(msg, buf);

             ByteBuf header = ctx.alloc().buffer(8);
             header.writeIntLE(buf.readableBytes()+8);
             CommandEnum cmd = CommandEnum.get(cls);
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
