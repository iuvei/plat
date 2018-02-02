package com.na.gate.proto.event;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2017/7/25 0025.
 */
public abstract class BaseEvent {
    protected ChannelHandlerContext ctx;

    public BaseEvent(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
