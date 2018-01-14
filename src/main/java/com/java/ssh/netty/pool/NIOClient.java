package com.java.ssh.netty.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.concurrent.CountDownLatch;
import com.java.ssh.netty.pool.NIOClientPool.CallbackService;

public class NIOClient {
	public NIOClientPool pool;
	
	public void init(){
		pool = new NIOClientPool();
		pool.init();
	}
	
	public void doRun(){
		try {
			final CountDownLatch countDownLatchBegin = new CountDownLatch(1);
	        final CountDownLatch countDownLatchEnd = new CountDownLatch(10);
	        for (int i = 0; i < 10; i++) {
	            new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							countDownLatchBegin.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						NIOClient.this.run();
						countDownLatchEnd.countDown();
					}
	            }).start();
	        }
	        countDownLatchBegin.countDown();
	        System.out.println("begin");
	        countDownLatchEnd.await();
	        System.out.println("end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			NIOConn conn = pool.getConn();
			CallbackService callback = new CallbackService();
			//NIOClientPool.register(channel, callback);
			int seq = NIOClientPool.INSTANCE.incrementAndGet();
			conn.channel.attr(NIOClientPool.DATA_MAP_ATTRIBUTEKEY).get().put(seq, callback);
			synchronized (callback) {
                UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
                String threadName = Thread.currentThread().getName();
                byte[]data = threadName.getBytes();
                ByteBuf buffer = allocator.buffer(20);
                int length = 8 + data.length;
                buffer.writeInt(length);
                buffer.writeInt(seq);
                buffer.writeBytes(data);
                conn.channel.writeAndFlush(buffer);
                System.out.println(String.format("[client] [%d %s send %s %d %b]", conn.id, threadName, threadName, seq, conn.channel.isActive()));
                callback.wait();
                ByteBuf result = callback.result;
                int resLength = result.readInt();
                int resSeq = result.readInt();
                byte[] resData = new byte[resLength - 8];
                result.readBytes(resData);
                String str = new String(resData);
                System.out.println(String.format("[client] [%d %s recv %s %d]", conn.id, threadName, str, resSeq));
            }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void start(){
		try {
			EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	        Bootstrap bootstrap = new Bootstrap();
	        bootstrap.group(eventLoopGroup)
	                 .channel(NioSocketChannel.class)
	                 .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
	                 .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
	                 .handler(new LoggingHandler(LogLevel.INFO))
	                 .handler(new ChannelInitializer<SocketChannel>() {
	                     @Override
	                     protected void initChannel(SocketChannel ch) throws Exception {
	                         ChannelPipeline pipeline = ch.pipeline();
	                         pipeline.addLast(new NIOClientHandler());
	                     }
	                 });

	        ChannelFuture channelFuture = bootstrap.connect("localhost", 8899).sync();
	        Channel channel = channelFuture.channel();
	        String msg = "Hello";
	        byte[]data = msg.getBytes();
	        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
	        ByteBuf buf = allocator.buffer(data.length + 4);
	        buf.writeInt(data.length);
	        buf.writeBytes(data);
	        channel.writeAndFlush(buf);
	        Thread.sleep(10000);
	        ByteBuf buf2 = allocator.buffer(data.length + 4);
	        buf2.writeInt(data.length);
	        buf2.writeBytes(data);
	        channel.writeAndFlush(buf2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//start();
		NIOClient client = new NIOClient();
		client.init();
		client.doRun();

	}

}
