package com.jj.client;

import com.jj.protocol.MessageRequest;
import com.jj.protocol.MessageResponse;
import com.jj.protocol.RpcDecoder;
import com.jj.protocol.RpcEncoder;
import com.jj.server.ServerDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class ClientInitialTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ClientInitialTask.class);

    private EventLoopGroup eventLoopGroup;
    private RpcServerLoader rpcServerLoader;

    public ClientInitialTask(EventLoopGroup eventLoopGroup, RpcServerLoader rpcServerLoader) {
        this.eventLoopGroup = eventLoopGroup;
        this.rpcServerLoader = rpcServerLoader;
    }

    @Override
    public void run() {
        logger.info("------------------ run");
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder(MessageRequest.class));
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                        pipeline.addLast(new RpcDecoder(MessageResponse.class));
                        pipeline.addLast(new ClientHandler());
                    }
                });
        ServerDiscovery serverDiscovery = new ServerDiscovery("192.168.138.129:2181");
        String discovery = serverDiscovery.discovery();
        logger.info((discovery.toString()));
        String[] address = discovery.split(":");
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(address[0], Integer.parseInt(address[1])).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    ClientHandler clientHandler = channelFuture.channel().pipeline().get(ClientHandler.class);
                    ClientInitialTask.this.rpcServerLoader.setClientHandler(clientHandler);
                }
            }
        });
    }
}
