package com.jj.protocol;

import com.jj.protocol.protostuff.ProtostuffSerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clz;

    public RpcDecoder(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) return;
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        Object obj = ProtostuffSerializationUtil.deserialize(data, clz);
        list.add(obj);
    }
}
