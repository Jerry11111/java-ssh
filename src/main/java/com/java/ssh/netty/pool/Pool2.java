package com.java.ssh.netty.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pool2 {
	
	public List<String>idleList = new ArrayList<String>();
	public List<String>activeList = new ArrayList<String>();
	public Map<String, String>allMap = new HashMap<String, String>();
	public static final int maxConn = 10;
	public Object lock = new Object();
	
	
	public String getConn(){
		synchronized (lock) {
			String conn = null;
			int size = allMap.size();
			if(!idleList.isEmpty()){
				conn = idleList.remove(0);
			} else if(size < maxConn){
				conn = newConn();
				allMap.put(conn, conn);
			} else if(!activeList.isEmpty()){
				String last = activeList.get(0);
				return last;
			}
			if(conn != null){
				activeList.add(conn);
			}
			return conn;
		}
	}
	
	public void returnConn(String conn){
		synchronized (lock) {
			activeList.remove(conn);
			idleList.add(conn);
		}
	}
	
	public String newConn(){
		return UUID.randomUUID().toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
