package com.java.ssh.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NIOServerHandler extends ChannelInboundHandlerAdapter {
	public NIOConnPool pool;
	
	public NIOServerHandler(NIOConnPool pool){
		this.pool = pool;
	}
	
    @Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	Channel channel = ctx.channel();
    	System.out.println(String.format("[server] [%s channelRegistered]", channel));
    	if(pool.exceedMax()){
    		System.err.println(String.format("[server] [%s disconnect]", channel));
    		channel.disconnect();
    		return;
    	}
		pool.addChannel(channel);
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
    	System.out.println(String.format("[server] [%s channelActive]", channel));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		//System.out.println(String.format("[server] [%s channelInactive]", channel));
		pool.removeChannel(channel);
		super.channelInactive(ctx);
	}

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf)msg;
        int length = buf.readInt();
        int seq = buf.readInt();
        byte[]data = new byte[length - 8];
        buf.readBytes(data);
        pool.active(channel);
        System.out.println(String.format("[server] [%d channelRead %s %s]", NIOConnPool.getId(channel), new String(data), pool));
        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
        ByteBuf responseBuf = allocator.buffer(length);
        responseBuf.writeInt(length);
        responseBuf.writeInt(seq);
        responseBuf.writeBytes(data);
        channel.writeAndFlush(responseBuf);
    }
}
