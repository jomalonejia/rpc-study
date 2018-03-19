package com.jj.server;

import com.jj.protocol.MessageRequest;
import com.jj.protocol.MessageResponse;
import com.jj.protocol.RpcDecoder;
import com.jj.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class Server {
    private int port;
    private Map<String, Class<?>> handlerMap;

    public Server(int port, Map<String, Class<?>> handlerMap) {
        this.port = port;
        this.handlerMap = handlerMap;
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            /*pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ServerHandler(handlerMap));*/
                            pipeline.addLast(new ReadTimeoutHandler(30))
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder(MessageRequest.class))
                                    .addLast(new RpcEncoder(MessageResponse.class))
                                    .addLast(new ServerHandler(handlerMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            ServerRegistry serverRegistry = new ServerRegistry("192.168.138.129:2181");
            serverRegistry.registry("localhost:" + 8080);
            System.out.println("server run on 8080");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
