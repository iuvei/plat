package com.na.socketio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.FileCopyUtils;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOChannelInitializer;
import com.na.user.socketserver.util.BeanUtil;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class MySocketIOChannelInitializer extends SocketIOChannelInitializer {
    private byte[] crossDomainXML;
    private Configuration config;
    public MySocketIOChannelInitializer(String crossDomain) throws IOException {
    	InputStream in = new FileInputStream(System.getProperty("user.dir") + crossDomain);
        crossDomainXML = FileCopyUtils.copyToByteArray(in);
        config = BeanUtil.getFieldValue(SocketIOChannelInitializer.class,this,"configuration");
    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addAfter(super.HTTP_ENCODER,"crossDomain",new CrossDomainHandle(config,crossDomainXML));
    }
}
