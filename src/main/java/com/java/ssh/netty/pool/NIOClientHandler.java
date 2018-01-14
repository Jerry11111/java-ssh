package com.java.ssh.netty.pool;

import com.java.ssh.netty.pool.NIOClientPool.CallbackService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NIOClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf responseBuf = (ByteBuf)msg;
        responseBuf.markReaderIndex();
        int length = responseBuf.readInt();
        int seq = responseBuf.readInt();
        responseBuf.resetReaderIndex();
        CallbackService callback = (CallbackService)channel.attr(NIOClientPool.DATA_MAP_ATTRIBUTEKEY).get().remove(seq);
        callback.receiveMessage(responseBuf);
    }
}
