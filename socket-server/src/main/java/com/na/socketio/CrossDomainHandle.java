package com.na.socketio;

import com.corundumstudio.socketio.Configuration;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class CrossDomainHandle extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Configuration configuration;
    private byte[] crossDomainXML;

    public CrossDomainHandle(Configuration configuration,byte[] crossDomainXML){
        this.configuration = configuration;
        this.crossDomainXML = crossDomainXML;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest && crossDomainXML!=null) {
            FullHttpRequest req = (FullHttpRequest) msg;
            Channel channel = ctx.channel();
            QueryStringDecoder queryDecoder = new QueryStringDecoder(req.getUri());
            if (queryDecoder.path().startsWith("/crossdomain.xml")) {
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(crossDomainXML));
                response.headers().set(CONTENT_TYPE, "application/xml");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
//                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.write(response);
                ctx.flush();
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }
}
