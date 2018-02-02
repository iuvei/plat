package com.na.gate.proto.event;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2017/7/25 0025.
 */
public class OnCommand extends BaseEvent{
    public OnCommand(ChannelHandlerContext ctx) {
        super(ctx);
    }
}
