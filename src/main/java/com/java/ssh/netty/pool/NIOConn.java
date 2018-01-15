package com.java.ssh.netty.pool;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicLong;

public class NIOConn {
	public long id;
	public long createTimestamp;
	public long lastActiveTimestamp;
	public Channel channel;
	public int countInUse;
	public static AtomicLong atLong = new AtomicLong(0);
	public static long nextId(){
		return atLong.incrementAndGet();
	}
	
	public synchronized void use(){
		countInUse++;
	}
	
	public synchronized void unUse(){
		if(countInUse == 0){
			throw new RuntimeException("channel not in use!");
		}
		countInUse--;
	}
	
	public synchronized boolean isIdle(){
		return countInUse == 0;
	}
	@Override
	public String toString() {
		return "NIOConn [id=" + id + ", createTimestamp=" + createTimestamp
				+ ", lastActiveTimestamp=" + lastActiveTimestamp + ", channel="
				+ channel + "]";
	}

	
}
