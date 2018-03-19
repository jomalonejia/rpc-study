package com.jj.protocol;

import com.jj.protocol.protostuff.ProtostuffSerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by jomalone_jia on 2018/3/19.
 */
public class RpcEncoder extends MessageToByteEncoder{

    private Class<?> clz;

    public RpcEncoder(Class<?> clz) {
        this.clz = clz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        if(clz.isInstance(o)){
            byte[] data = ProtostuffSerializationUtil.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
