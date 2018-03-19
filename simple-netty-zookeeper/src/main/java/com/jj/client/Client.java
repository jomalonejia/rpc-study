package com.jj.client;

import com.jj.protocol.model.MessageRequest;
import com.jj.server.ServerDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

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
        ServerDiscovery serverDiscovery = new ServerDiscovery("192.168.138.129:2181");
        String discovery = serverDiscovery.discovery();
        String[] address = discovery.split(":");
        ChannelFuture channelFuture = bootstrap.connect(address[0], Integer.parseInt(address[1])).sync();

        channelFuture.channel().writeAndFlush(request);
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
