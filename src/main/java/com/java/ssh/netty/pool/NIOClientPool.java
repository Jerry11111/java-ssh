package com.java.ssh.netty.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
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
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOClientPool {
	public Bootstrap bootstrap;
	public NIOConn[] channels;
	public List<NIOConn> idleConnList = new ArrayList<NIOConn>();
	public List<NIOConn> activeConnList = new ArrayList<NIOConn>();
	public Object lock = new Object();
	public Object[] locks;
	public static final int MAX_CHANNEL_COUNT = 4;
	public static final AttributeKey<Map<Integer, Object>> DATA_MAP_ATTRIBUTEKEY = AttributeKey.valueOf("dataMap");
	public static final AttributeKey<Long> ID_ATTR = AttributeKey.valueOf("ID_ATTR");
	public static final AtomicInteger INSTANCE = new AtomicInteger(0);
	
	 public static class CallbackService{
	        public volatile ByteBuf result;
	        public void receiveMessage(ByteBuf receiveBuf) throws Exception {
	            synchronized (this) {
	                result = receiveBuf;
	                this.notify();
	            }
	        }
	    }
	
	
	public static void register(Channel channel, CallbackService callback){
		
	}
	
	public static Long getId(Channel channel){
		Attribute<Long> attribute = channel.attr(NIOConnPool.ID_ATTR);
		if(attribute == null){
			return null;
		}
		Long id = attribute.get();
		return id;
	}

	public void init() {
		this.channels = new NIOConn[MAX_CHANNEL_COUNT];
		this.locks = new Object[MAX_CHANNEL_COUNT];
		for (int i = 0; i < MAX_CHANNEL_COUNT; i++) {
			this.locks[i] = new Object();
		}
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
				.option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new SelfDefineEncodeHandler());
						pipeline.addLast(new NIOClientHandler());
					}
				});
	}
	
	public NIOConn newConn() throws InterruptedException {
		Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
		//Thread.sleep(1000);
		//channel.closeFuture().sync();
		if(!channel.isActive()){
			channel.close();
			throw new RuntimeException("Can not get channel!");
		}
		System.out.println(String.format("[newConn] [%s %b]", channel, channel.isActive()));
        Attribute<Map<Integer,Object>> attribute = channel.attr(DATA_MAP_ATTRIBUTEKEY);
        ConcurrentHashMap<Integer, Object> dataMap = new ConcurrentHashMap<Integer, Object>();
        attribute.set(dataMap);
        NIOConn conn = new NIOConn();
		Long id = NIOConn.nextId();
		Attribute<Long> attribute2 = channel.attr(NIOConnPool.ID_ATTR);
		attribute2.set(id);
		conn.id = id;
		conn.channel = channel;
		conn.createTimestamp = System.currentTimeMillis();
		conn.lastActiveTimestamp = conn.createTimestamp;
		return conn;
	}

	public NIOConn getConn() throws InterruptedException {
		int index = new Random().nextInt(MAX_CHANNEL_COUNT);
		NIOConn conn = channels[index];
		if (conn != null && conn.channel.isActive()) {
			return conn;
		}
		Object lock = locks[index];
		synchronized (lock) {
			conn = channels[index];
			if (conn != null && conn.channel.isActive()) {
				return conn;
			}
			conn = newConn();
			channels[index] = conn;
		}
		return conn;
	}
	
	public NIOConn getConn2() throws InterruptedException {
		synchronized (lock) {
			NIOConn conn = null;
			int size = idleConnList.size() + activeConnList.size();
			if(!idleConnList.isEmpty()){
				conn = idleConnList.remove(0);
			}else if(size < MAX_CHANNEL_COUNT){
				conn = newConn();
			}else if (!activeConnList.isEmpty()) {
				conn = activeConnList.get(0);
				conn.use();
				return conn;
			}
			if(conn != null){
				conn.use();
				activeConnList.add(conn);
			}
			return conn;
		}
	}
	
	public void returnConn(NIOConn conn){
		if(conn == null){
			return;
		}
		synchronized (lock) {
			synchronized (conn) {
				conn.unUse();
				if(conn.isIdle()){
					idleConnList.add(conn);
					activeConnList.remove(conn);
				}
			}
		}
	}
}
