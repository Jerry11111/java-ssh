package com.java.ssh.netty.pool;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NIOConnPool {
	public Map<Long, NIOConn> connMap = new HashMap<Long, NIOConn>();
	public static final AttributeKey<Long> ID_ATTR = AttributeKey.valueOf("ID_ATTR");
	public int maxConnCount = 5;
	public ReadWriteLock lock = new ReentrantReadWriteLock();
	public Lock readLock = lock.readLock();
	public Lock writeLock = lock.writeLock();

	public NIOConn addChannel(Channel channel) {
		try {
			writeLock.lock();
			NIOConn conn = new NIOConn();
			Long id = NIOConn.nextId();
			Attribute<Long> attribute = channel.attr(NIOConnPool.ID_ATTR);
			attribute.set(id);
			conn.id = id;
			conn.channel = channel;
			conn.createTimestamp = System.currentTimeMillis();
			conn.lastActiveTimestamp = conn.createTimestamp;
			connMap.put(conn.id, conn);
			return conn;
		} finally{
			writeLock.unlock();
		}
		
	}
	
	public static Long getId(Channel channel){
		Attribute<Long> attribute = channel.attr(NIOConnPool.ID_ATTR);
		if(attribute == null){
			return null;
		}
		Long id = attribute.get();
		return id;
	}
	
	public void removeChannel(Channel channel){
		try{
			writeLock.lock();
			Long id = getId(channel);
			connMap.remove(id);
		}finally{
			writeLock.unlock();
		}
	}
	
	public void active(Channel channel){
		try{
			readLock.lock();
			Long id = getId(channel);
			NIOConn conn = connMap.get(id);
			if(conn != null){
				conn.lastActiveTimestamp = System.currentTimeMillis();
			}
		}finally{
			readLock.unlock();
		}
		
	}
	
	public int size(){
		try{
			readLock.lock();
			return connMap.size();
		}finally{
			readLock.unlock();
		}
	}
	
	public boolean exceedMax(){
		try{
			readLock.lock();
			return connMap.size() >= maxConnCount; 
		}finally{
			readLock.unlock();
		}
	}

	@Override
	public String toString() {
		return connMap.toString();
	}
	
	

}
