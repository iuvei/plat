package com.na.gate.proto.event;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2017/7/25 0025.
 */
public class OnConnect extends BaseEvent{
    public OnConnect(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
