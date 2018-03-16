package com.jj.client;

import com.jj.model.Command;
import com.jj.model.MessageRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class Client {

    public static void sendMessage(MessageRequest request) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(new ObjectEncoder(),new ClientHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
       /* *//*channelFuture.channel().writeAndFlush(*//**//*"777".getBytes()*//**//*Unpooled.copiedBuffer("777".getBytes()));*//*
        *//*Command command =new Command();
        command.setActionName("Hello action.");
        channelFuture.channel().writeAndFlush(command);*//*
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessageId("aluba");*/
        channelFuture.channel().writeAndFlush(request);
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
