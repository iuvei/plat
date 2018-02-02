package com.na.gate.proto.handler;

import java.nio.ByteOrder;

import com.google.common.eventbus.AsyncEventBus;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by sunny on 2017/7/21 0021.
 */
public class GateInitializer extends ChannelInitializer {
    private AsyncEventBus eventBus;

    public GateInitializer(AsyncEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,-4,4,true));
        pipeline.addLast(new GateProtoDecoder(eventBus));
        pipeline.addLast(new GateProtoEncoder(eventBus));
        pipeline.addLast(new GateProtoEncoderList(eventBus));
    }
}
